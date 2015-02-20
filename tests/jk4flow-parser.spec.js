// Set of JSHint directives
/* global describe: true */
/* global beforeEach: true */
/* global it: true */
/* global expect: true */

'use strict';

var JK4Flow = require('../src/jk4flow-parser');

describe('JSON Workflow Parsing', function() {

    var emptyControlFlow = {};
    var controlFlowOnlyId = {"id": "#CF-X"};
    var completeControlFlow = {"id": "#CF-X", "to": "#AN-2"};

    var emptyActionNodes = [];
    var emptyActionNode = {};
    var actionNodeOnlyId = { "id": "#AN-2" };
    var actionNodeNoControlFlow = { "id": "#AN-2", "callback": "TestAction::sum" };
    var actionNodeComplete = { "id": "#AN-2", "callback": "TestAction::sum", "control-flow": { "id": "#CF-X", "to": "#DN-3" } };

    var JS_WORK_FLOW =
    {
        "initial-node": {
            "id": "#IN-1",
            "control-flow": {
                "id": "#CF-X",
                "to": "#AN-2"
            }
        },
        "action-nodes": [
            {
                "id": "#AN-2",
                "callback": "TestAction::sum",
                "control-flow": {
                    "id": "#CF-X",
                    "to": "#DN-3"
                }
            },
            {
                "id": "#AN-4",
                "callback": "TestAction::displayRight",
                "control-flow": {
                    "id": "#CF-X",
                    "to": "#FN-6"
                }
            },
            {
                "id": "#AN-5",
                "callback": "TestAction::displayWrong",
                "control-flow": {
                    "id": "#CF-X",
                    "to": "#FN-6"
                }
            }
        ],
        "decision-nodes": [
            {
                "id": "#DN-3",
                "decision-flows": [
                    {
                        "context": {
                            "attribute": "result",
                            "condition": "equals",
                            "value": "false"
                        },
                        "control-flow": {
                            "id": "#CF-X",
                            "to": "#AN-4"
                        }
                    },
                    {
                        "context": {
                            "attribute": "result",
                            "condition": "equals",
                            "value": "true"
                        },
                        "control-flow": {
                            "id": "#CF-X",
                            "to": "#AN-5"
                        }
                    }
                ]
            }
        ],
        "final-node": {
            "id": "#FN-6"
        }
    };

    var JSON_WORK_FLOW = JSON.stringify(JS_WORK_FLOW);

    describe('Workflow transformation from JSON', function () {

        it('Should not work on an empty string', function () {
            var result = JK4Flow.getWorkflowFromJson('');

            expect(result.success).toBe(false);
            expect(result.message).toBeDefined();
        });

        it('Should not work on a wrong JSON format', function () {
            var result = JK4Flow.getWorkflowFromJson('{"initial-node:"value"}');

            expect(result.success).toBe(false);
            expect(result.message).toBeDefined();
        });

        it('Should transform a valid JSON to a javascript object', function () {
            var result = JK4Flow.getWorkflowFromJson(JSON_WORK_FLOW);

            expect(result.success).toBe(true);
            expect(result.workflow).toBeDefined();
        });

    });

    describe('Control Flow', function () {

        var ERR_MSG = {
            NO_OUTGOING: 'NO OUTGOING'
        };

        it('Should not be empty (no error message as parameter)', function () {
            var result = JK4Flow.getControlFlow();

            expect(result.success).toBe(false);
            expect(result.message).toBe('No outgoing control flow');
        });

        it('Should not be empty (error message as parameter is passed)', function () {
            var result = JK4Flow.getControlFlow(undefined, ERR_MSG);

            expect(result.success).toBe(false);
            expect(result.message).toBe(ERR_MSG.NO_OUTGOING);
        });

        it('Should not be deprived of an id (no error message as parameter)', function () {
            var result = JK4Flow.getControlFlow(emptyControlFlow);

            expect(result.success).toBe(false);
            expect(result.message).toBe('No outgoing control flow');
        });

        it('Should not be deprived of an id (error message as parameter is passed)', function () {
            var result = JK4Flow.getControlFlow(emptyControlFlow, ERR_MSG);

            expect(result.success).toBe(false);
            expect(result.message).toBe(ERR_MSG.NO_OUTGOING);
        });

        it('Should not be deprived of an outgoing flow (no error message as parameter)', function () {
            var result = JK4Flow.getControlFlow(controlFlowOnlyId);

            expect(result.success).toBe(false);
            expect(result.message).toBe('No outgoing control flow');
        });

        it('Should not be deprived of an outgoing flow (error message as parameter is passed)', function () {
            var result = JK4Flow.getControlFlow(controlFlowOnlyId, ERR_MSG);

            expect(result.success).toBe(false);
            expect(result.message).toBe(ERR_MSG.NO_OUTGOING);
        });

        it('Should result in a control flow object', function () {
            var result = JK4Flow.getControlFlow(completeControlFlow, ERR_MSG);

            expect(result.success).toBe(true);
            expect(result.controlFlow).toBeDefined();
            expect(result.controlFlow.id).toBeDefined();
            expect(result.controlFlow.to).toBeDefined();
        });

    });

    describe('Initial Node', function () {

        it('Should not be empty', function () {
            var result = JK4Flow.getInitialNode();

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.INITIAL_NODE.NONE);
        });

        it('Should not be deprived of an id', function () {
            var result = JK4Flow.getInitialNode({});

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.INITIAL_NODE.NO_ID);
        });

        it('Should not be deprived of an outgoing flow', function () {
            var result = JK4Flow.getInitialNode({id: 'x'});

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.INITIAL_NODE.NO_OUTGOING);
        });

        it('Should be fragmented in two array structures', function () {
            var initialNode = JK4Flow.getWorkflowFromJson(JSON_WORK_FLOW).workflow[JK4Flow.FLOW_ATTRS.INITIAL];
            var result = JK4Flow.getInitialNode(initialNode);

            expect(result.success).toBe(true);
            expect(result.initialNode).toBeDefined();
            expect(result.initialNode instanceof Array).toBe(true);
            expect(result.initialNode.length).toBe(1);
            expect(result.initialNode[0].id).toBeDefined();
            expect(result.controlFlow).toBeDefined();
            expect(result.controlFlow instanceof Array).toBe(true);
            expect(result.controlFlow.length).toBe(1);
            expect(result.controlFlow[0].id).toBeDefined();
            expect(result.controlFlow[0].from).toBe(result.initialNode[0].id);
            expect(result.controlFlow[0].to).toBeDefined();
        });
    });

    describe('Action Node', function() {

        it('Should not be empty', function () {
            var result = JK4Flow.getActionNode();

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.ACTION_NODE.NONE);
        });

        it('Should not be deprived of an id', function () {
            var result = JK4Flow.getActionNode({});

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.ACTION_NODE.NO_ID);
        });

        it('Should not be deprived of a callback', function () {
            var result = JK4Flow.getActionNode(actionNodeOnlyId);

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.ACTION_NODE.NO_CALLBACK);
        });

    });

    describe('Action Nodes List', function() {

        it('Should not be undefined', function () {
            var result = JK4Flow.getActionNodes();

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.ACTION_NODE.NONE);
        });

        it('Should not be empty', function () {
            var result = JK4Flow.getActionNodes([]);

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.ACTION_NODE.NONE);
        });

        it('Should not have empty items', function () {
            var result = JK4Flow.getActionNodes([{}]);

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.ACTION_NODE.NONE);
        });

        it('Should not have empty items', function () {
            var result = JK4Flow.getActionNodes([{}]);

            expect(result.success).toBe(false);
            expect(result.message).toBe(JK4Flow.ERROR_MESSAGES.ACTION_NODE.NONE);
        });

    });

});
