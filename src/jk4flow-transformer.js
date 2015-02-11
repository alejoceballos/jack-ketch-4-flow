'use strict';

var ERROR_MESSAGES = {
    WORKFLOW: {
        NONE: 'Invalid workflow'
    },
    INITIAL_NODE: {
        NONE: 'No Initial Node',
        NO_ID: 'No Initial Node id',
        NO_OUTGOING: 'Initial Node has no outgoing control flow'
    },
    ACTION_NODE: {
        NONE: 'No Action Nodes',
        NO_ID: 'No Action Node id',
        NO_OUTGOING: 'Action Node has no outgoing control flow',
        NO_CALLBACK: 'Action Node has no callback associated'
    }
};

var validateInitialNode = function(initialNode) {
    if (!initialNode) {
        return {
            success: false,
            message: ERROR_MESSAGES.INITIAL_NODE.NONE
        };
    }

    if (!initialNode.id) {
        return {
            success: false,
            message: ERROR_MESSAGES.INITIAL_NODE.NO_ID
        };
    }

    if (!initialNode["to-node"]) {
        return {
            success: false,
            message: ERROR_MESSAGES.INITIAL_NODE.NO_OUTGOING
        };
    }

    return { success: true };
};

var validateActionNodes = function(actionNodes) {
    var validateActionNode = function(actionNode) {
        if (!actionNode) {
            return {
                success: false,
                message: ERROR_MESSAGES.ACTION_NODE.NONE
            };
        }

        if (!actionNode.id) {
            return {
                success: false,
                message: ERROR_MESSAGES.ACTION_NODE.NO_ID
            };
        }

        if (!actionNode["to-node"]) {
            return {
                success: false,
                message: ERROR_MESSAGES.ACTION_NODE.NO_OUTGOING
            };
        }

        if (!actionNode.callback) {
            return {
                success: false,
                message: ERROR_MESSAGES.ACTION_NODE.NO_CALLBACK
            };
        }

        return { success: true };
    };

    if (!actionNodes || !(actionNodes instanceof Array) || actionNodes.length === 0) {
        return {
            success: false,
            message: ERROR_MESSAGES.ACTION_NODE.NONE
        };
    }

    for (var i = 0; i < actionNodes.length; i++) {
        var result = validateActionNode(actionNodes[i]);
        if (!result.success) return result;
    }

    return { success: true };
};

var validateWorkflow = function(workflow) {
    var result = { success: true };

    if (!workflow) {
        result = {
            success: false,
            message: ERROR_MESSAGES.WORKFLOW.NONE
        };
    }

    if (result.success) result = validateInitialNode(workflow['initial-node']);
    if (result.success) result = validateActionNodes(workflow['action-nodes']);

    return result;
};

var transform = function(json) {
    try {
        var workflow = JSON.parse(json);
    } catch (err) {
        return {
            success: false,
            message: err
        }
    }

    var result = validateWorkflow(workflow);
    if (!result.success) return result;

    return {
        success: true,
        workflow: workflow
    };
};

module.exports = {
    validate: validateWorkflow,
    transform: transform
};