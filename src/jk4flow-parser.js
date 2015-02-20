'use strict';

var FLOW_ATTRS = {
    INITIAL: 'initial-node',
    ACTIONS: 'action-nodes',
    DECISIONS: 'decision-nodes',
    FINAL: 'final-node',
    CONTROL_FLOW: 'control-flow',
    ID: 'id',
    CALLBACK: 'callback',
    TO_NODE: 'to',
    CONTEXT: 'context',
    ATTRIBUTE: 'attribute',
    VALUE: 'value'
};

var ERROR_MESSAGES = {
    WORKFLOW: {
        NONE: 'Invalid workflow'
    },
    INITIAL_NODE: {
        NONE: 'No Initial Node',
        NO_ID: 'No Initial Node id',
        NO_OUTGOING: 'Initial Node has no outgoing control flow'
    },
    FINAL_NODE: {
        NONE: 'No Final Node',
        NO_ID: 'No Final Node id'
    },
    ACTION_NODE: {
        NONE: 'No Action Nodes',
        NO_ID: 'No Action Node id',
        NO_OUTGOING: 'Action Node has no outgoing control flow',
        NO_CALLBACK: 'Action Node has no callback associated'
    },
    DECISION_NODE: {
        NONE: 'No Decision Nodes',
        NO_ID: 'No Decision Node id',
        NO_OUTGOING: 'Decision Node has no outgoing control flows',
        NO_CONTEXT: 'Decision Node has no context to take a decision',
        NO_CONTEXT_ATTRIBUTE: 'Decision Node has no context attribute to take a decision',
        NO_CONTEXT_ATTRIBUTE_VALUE: 'Decision Node has no context attribute value to take a decision'
    }
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var currentUUID = 0;

var getUUID = function() {
    return '#' + (currentUUID++)
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var validateFinalNode = function(finalNode) {
    if (!finalNode) {
        return {
            success: false,
            message: ERROR_MESSAGES.FINAL_NODE.NONE
        };
    }

    if (!finalNode.id) {
        return {
            success: false,
            message: ERROR_MESSAGES.FINAL_NODE.NO_ID
        };
    }

    return { success: true };
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var getActionNode = function(actionNode) {
    if (!actionNode) {
        return {
            success: false,
            message: ERROR_MESSAGES.ACTION_NODE.NONE
        };
    }

    if (!actionNode[FLOW_ATTRS.ID]) {
        return {
            success: false,
            message: ERROR_MESSAGES.ACTION_NODE.NO_ID
        };
    }

    if (!actionNode[FLOW_ATTRS.CALLBACK]) {
        return {
            success: false,
            message: ERROR_MESSAGES.ACTION_NODE.NO_CALLBACK
        };
    }

    var result = getControlFlow(actionNode[FLOW_ATTRS.CONTROL_FLOW], ERROR_MESSAGES.ACTION_NODE);

    if (!result.success) return result;

    result.controlFlow.from = actionNode[FLOW_ATTRS.ID];

    return {
        success: true,
        actionNode: [ { id: actionNode[FLOW_ATTRS.ID], callback: actionNode[FLOW_ATTRS.CALLBACK] } ],
        controlFlow: [ result.controlFlow ]
    };

    return { success: true };
};

var getActionNodes = function(actionNodes) {
    if (!actionNodes || !(actionNodes instanceof Array) || actionNodes.length === 0) {
        return {
            success: false,
            message: ERROR_MESSAGES.ACTION_NODE.NONE
        };
    }

    for (var i = 0; i < actionNodes.length; i++) {
        var result = getActionNode(actionNodes[i]);
        if (!result.success) return result;
    }

    return { success: true };
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var validateDecisionOutgoingContext = function(validateDecisionOutgoingContext) {
    if (!validateDecisionOutgoingContext) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NO_CONTEXT
        };
    }

    if (!validateDecisionOutgoingContext[FLOW_ATTRS.ATTRIBUTE]) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NO_CONTEXT_ATTRIBUTE
        };
    }

    if (!validateDecisionOutgoingContext[FLOW_ATTRS.VALUE]) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NO_CONTEXT_ATTRIBUTE_VALUE
        };
    }

    return { success: true };
};

var validateDecisionOutgoing = function(decisionOutgoing) {
    if (!decisionOutgoing) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NO_OUTGOING
        };
    }

    if (!decisionOutgoing[FLOW_ATTRS.TO_NODE]) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NO_OUTGOING
        };
    }

    return validateDecisionOutgoingContext(decisionOutgoing[FLOW_ATTRS.CONTEXT]);
};

var validateDecisionNode = function(decisionNode) {
    if (!decisionNode) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NONE
        };
    }

    if (!decisionNode.id) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NO_ID
        };
    }

    var toNodesDecisionNode = decisionNode[FLOW_ATTRS.TO_NODES];

    if (!toNodesDecisionNode || !(toNodesDecisionNode instanceof Array) || toNodesDecisionNode.length == 0) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NO_OUTGOING
        };
    }

    for (var i = 0; i < toNodesDecisionNode.length; i++) {
        var result = validateDecisionOutgoing(toNodesDecisionNode[i]);
        if (!result.success) return result;
    }

    return { success: true };
};

var validateDecisionNodes = function(decisionNodes) {
    if (!decisionNodes || !(decisionNodes instanceof Array) || decisionNodes.length === 0) {
        return {
            success: false,
            message: ERROR_MESSAGES.DECISION_NODE.NONE
        };
    }

    for (var i = 0; i < decisionNodes.length; i++) {
        var result = validateDecisionNode(decisionNodes[i]);
        if (!result.success) return result;
    }

    return { success: true };
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var validateWorkflow = function(workflow) {
    var result = { success: true };

    if (!workflow) {
        result = {
            success: false,
            message: ERROR_MESSAGES.WORKFLOW.NONE
        };
    }

    var ids = { };

    if (result.success) {
        result = getInitialNode(workflow[FLOW_ATTRS.INITIAL]);
    }
    if (result.success) result = getActionNodes(workflow[FLOW_ATTRS.ACTIONS]);
    if (result.success) result = validateDecisionNodes(workflow[FLOW_ATTRS.DECISIONS]);

    return result;
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var getInitialNode = function(initialNode) {
    if (!initialNode) {
        return {
            success: false,
            message: ERROR_MESSAGES.INITIAL_NODE.NONE
        };
    }

    if (!initialNode[FLOW_ATTRS.ID]) {
        return {
            success: false,
            message: ERROR_MESSAGES.INITIAL_NODE.NO_ID
        };
    }

    var result = getControlFlow(initialNode[FLOW_ATTRS.CONTROL_FLOW], ERROR_MESSAGES.INITIAL_NODE);

    if (!result.success) return result;

    result.controlFlow.from = initialNode[FLOW_ATTRS.ID];

    return {
        success: true,
        initialNode: [ { id: initialNode[FLOW_ATTRS.ID] } ],
        controlFlow: [ result.controlFlow ]
    };
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var getControlFlow = function(controlFlow, ERR_NODE_MSG) {

    if (!controlFlow || !controlFlow[FLOW_ATTRS.ID] || !controlFlow[FLOW_ATTRS.TO_NODE]) {
        return {
            success: false,
            message: (ERR_NODE_MSG) ? ERR_NODE_MSG.NO_OUTGOING : 'No outgoing control flow'
        };
    }

    return {
        success: true,
        controlFlow: {
            id: controlFlow[FLOW_ATTRS.ID],
            to: controlFlow[FLOW_ATTRS.TO_NODE]
        }
    };
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var getWorkflowFromJson = function(json) {
    try {
        var workflow = JSON.parse(json);

    } catch (err) {
        return {
            success: false,
            message: err
        }
    }

    return {
        success: true,
        workflow: workflow
    }
};

// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
// * * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *

var parse = function(json) {
    var result = getWorkflowFromJson(json);

    if (result.success) {
        var workflow = result.workflow;
    }

    return result;
};

module.exports = {
    FLOW_ATTRS: FLOW_ATTRS,
    ERROR_MESSAGES: ERROR_MESSAGES,
    getWorkflowFromJson: getWorkflowFromJson,
    getControlFlow: getControlFlow,
    getInitialNode: getInitialNode,
    getActionNode: getActionNode,
    getActionNodes: getActionNodes,
    parse: parse
};