// Set of JSHint directives
/* global describe: true */
/* global beforeEach: true */
/* global it: true */
/* global expect: true */

'use strict';

var JK4FlowModel = require('../src/jk4flow-model').org.somossuinos.jk4flow.model;

describe('JSON Workflow Model', function() {

    var DUMMY_ID = '#ID';

    describe('Initial Node', function () {

        var OUTGOING_ERR_MSG = 'Initial Node should redirect to an Action, Decision or Fork Node';

        var iNode;

        beforeEach(function() {
            iNode = new JK4FlowModel.InitialNode(DUMMY_ID);
        });

        it('Should have a node type', function () {
            expect(iNode.type).toBe(JK4FlowModel.NODE_TYPE.INITIAL);
        });

        it('Should allow id assignment on construction', function () {
            expect(iNode.id).toBe(DUMMY_ID);
        });

        it('Should not allow any invalid node type as outgoing flow', function () {
            expect(
                function() {
                    iNode.outgoing = { type: 'INVALID' };
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow an Initial Node as outgoing flow', function () {
            expect(
                function() {
                    iNode.outgoing = new JK4FlowModel.InitialNode(DUMMY_ID + '2');
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow a Final Node as outgoing flow', function () {
            expect(
                function() {
                    iNode.outgoing = { type: JK4FlowModel.NODE_TYPE.FINAL };
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow a Join Node as outgoing flow', function () {
            expect(
                function() {
                    iNode.outgoing = new JK4FlowModel.JoinNode(DUMMY_ID + '2');
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should allow an Action Node as outgoing flow', function () {
            var OUTGOING = new JK4FlowModel.ActionNode(DUMMY_ID + '2');
            iNode.outgoing = OUTGOING;

            expect(iNode.outgoing instanceof JK4FlowModel.ActionNode).toBe(true);
        });

        it('Should allow a Decision Node as outgoing flow', function () {
            var OUTGOING = { type: JK4FlowModel.NODE_TYPE.DECISION };
            iNode.outgoing = OUTGOING;

            expect(iNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should allow a Fork Node as outgoing flow', function () {
            var OUTGOING = new JK4FlowModel.ForkNode(DUMMY_ID + '2');
            iNode.outgoing = OUTGOING;

            expect(iNode.outgoing.type).toBe(OUTGOING.type);
        });

    });

    describe('Action Node', function () {

        var OUTGOING_ERR_MSG = 'Action Node should redirect to an Action, Decision, Fork Node, Join or Final Node';
        var OUTGOING_ITSELF_ERR_MSG = 'Action Node should not redirect to itself';
        var CALLBACK_ERR_MSG = 'An Action Node callback should be a function';

        var aNode;

        beforeEach(function() {
            aNode = new JK4FlowModel.ActionNode(DUMMY_ID);
        });

        it('Should have a node type', function () {
            expect(aNode.type).toBe(JK4FlowModel.NODE_TYPE.ACTION);
        });

        it('Should allow id assignment on construction', function () {
            expect(aNode.id).toBe(DUMMY_ID);
        });

        it('Should not allow any invalid node type as outgoing flow', function () {
            expect(
                function() {
                    aNode.outgoing = { type: 'INVALID' };
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow an Initial Node as outgoing flow', function () {
            expect(
                function() {
                    aNode.outgoing = new JK4FlowModel.InitialNode(DUMMY_ID + '2');
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should allow a Final Node as outgoing flow', function () {
            var OUTGOING = { type: JK4FlowModel.NODE_TYPE.FINAL };
            aNode.outgoing = OUTGOING;

            expect(aNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should allow a Join Node as outgoing flow', function () {
            var OUTGOING = new JK4FlowModel.JoinNode(DUMMY_ID + '2');
            aNode.outgoing = OUTGOING;

            expect(aNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should allow another Action Node as outgoing flow', function () {
            var OUTGOING = new JK4FlowModel.ActionNode(DUMMY_ID + '2');
            aNode.outgoing = OUTGOING;

            expect(aNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should not allow itself as an outgoing flow', function () {
            expect(
                function() {
                    aNode.outgoing = aNode;
                }
            ).toThrow(OUTGOING_ITSELF_ERR_MSG);
        });

        it('Should allow a Decision Node as outgoing flow', function () {
            var OUTGOING = { type: JK4FlowModel.NODE_TYPE.DECISION};
            aNode.outgoing = OUTGOING;

            expect(aNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should allow a Fork Node as outgoing flow', function () {
            var OUTGOING = new JK4FlowModel.ForkNode(DUMMY_ID + '2');
            aNode.outgoing = OUTGOING;

            expect(aNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should not allow an empty callback', function () {
            expect(
                function() {
                    aNode.callback = undefined;
                }
            ).toThrow(CALLBACK_ERR_MSG);
        });

        it('Should not allow an regular object as callback', function () {
            expect(
                function() {
                    aNode.callback = {};
                }
            ).toThrow(CALLBACK_ERR_MSG);
        });

        it('Should not allow a primitive type as callback', function () {
            expect(
                function() {
                    aNode.callback = 1;
                }
            ).toThrow(CALLBACK_ERR_MSG);
        });

        it('Should allow a function as a callback', function () {
            var FUNCTION_RETURN = 'a function';
            aNode.callback = function() { return FUNCTION_RETURN };

            expect(aNode.callback()).toBe(FUNCTION_RETURN);
        });

    });

    describe('Fork Node', function () {

        var AT_LEAST_TWO_OUTGOING_MSG_ERR = 'A Fork Node should redirect to at least two asynchronous processes';
        var OUTGOING_ERR_MSG = 'Fork Node should redirect to an Action or Decision Node';

        var fNode;

        beforeEach(function() {
            fNode = new JK4FlowModel.ForkNode(DUMMY_ID);
        });

        it('Should have a node type', function () {
            expect(fNode.type).toBe(JK4FlowModel.NODE_TYPE.FORK);
        });

        it('Should allow id assignment on construction', function () {
            expect(fNode.id).toBe(DUMMY_ID);
        });

        it('Should not allow an empty outgoing flow', function () {
            expect(
                function() {
                    fNode.outgoings = undefined;
                }
            ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
        });

        it('Should not allow a simple object as outgoing flow', function () {
            expect(
                function() {
                    fNode.outgoings = new JK4FlowModel.ActionNode(DUMMY_ID + '2');
                }
            ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
        });

        it('Should not allow a set of a single object as outgoing flow', function () {
            expect(
                function() {
                    fNode.outgoings = [ new JK4FlowModel.ActionNode(DUMMY_ID + '2') ];
                }
            ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
        });

        it('Should not allow Initial Nodes in the set of outgoing flows', function () {
            expect(
                function() {
                    fNode.outgoings = [
                        new JK4FlowModel.ActionNode(DUMMY_ID + '2'),
                        new JK4FlowModel.InitialNode(DUMMY_ID + '3')
                    ];
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow Final Nodes in the set of outgoing flows', function () {
            expect(
                function() {
                    fNode.outgoings = [
                        new JK4FlowModel.ActionNode(DUMMY_ID + '2'),
                        { type: JK4FlowModel.NODE_TYPE.FINAL }
                    ];
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow another Fork Node in the set of outgoing flows', function () {
            expect(
                function() {
                    fNode.outgoings = [
                        new JK4FlowModel.ActionNode(DUMMY_ID + '2'),
                        new JK4FlowModel.ForkNode(DUMMY_ID + '3')
                    ];
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow a Join Node in the set of outgoing flows', function () {
            expect(
                function() {
                    fNode.outgoings = [
                        new JK4FlowModel.ActionNode(DUMMY_ID + '2'),
                        new JK4FlowModel.JoinNode(DUMMY_ID + '2')
                    ];
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should allow any set of Action and Decision Nodes for outgoing flows', function () {
            fNode.outgoings = [
                new JK4FlowModel.ActionNode(DUMMY_ID + '2'),
                { type: JK4FlowModel.NODE_TYPE.DECISION },
                new JK4FlowModel.ActionNode(DUMMY_ID + '3'),
                { type: JK4FlowModel.NODE_TYPE.DECISION },
                new JK4FlowModel.ActionNode(DUMMY_ID + '4'),
                { type: JK4FlowModel.NODE_TYPE.DECISION }
            ];

            expect(fNode.outgoings.length).toBe(6);
        });

    });

    describe('Join Node', function () {

        var OUTGOING_ERR_MSG = 'Join Node should redirect to an Action, Decision, Fork Node or Final Node';

        var jNode;

        beforeEach(function() {
            jNode = new JK4FlowModel.JoinNode(DUMMY_ID);
        });

        it('Should have a node type', function () {
            expect(jNode.type).toBe(JK4FlowModel.NODE_TYPE.JOIN);
        });

        it('Should allow id assignment on construction', function () {
            expect(jNode.id).toBe(DUMMY_ID);
        });

        it('Should not allow any invalid node type as outgoing flow', function () {
            expect(
                function() {
                    jNode.outgoing = { type: 'INVALID' };
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow an Initial Node as outgoing flow', function () {
            expect(
                function() {
                    jNode.outgoing = new JK4FlowModel.InitialNode(DUMMY_ID + '2');
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should not allow another Join Node as outgoing flow', function () {
            expect(
                function() {
                    jNode.outgoing = new JK4FlowModel.JoinNode(DUMMY_ID + '2');
                }
            ).toThrow(OUTGOING_ERR_MSG);
        });

        it('Should allow a Final Node as outgoing flow', function () {
            var OUTGOING = { type: JK4FlowModel.NODE_TYPE.FINAL };
            jNode.outgoing = OUTGOING;

            expect(jNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should allow an Action Node as outgoing flow', function () {
            var OUTGOING = new JK4FlowModel.ActionNode(DUMMY_ID + '2');
            jNode.outgoing = OUTGOING;

            expect(jNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should allow a Decision Node as outgoing flow', function () {
            var OUTGOING = { type: JK4FlowModel.NODE_TYPE.DECISION};
            jNode.outgoing = OUTGOING;

            expect(jNode.outgoing.type).toBe(OUTGOING.type);
        });

        it('Should allow a Fork Node as outgoing flow', function () {
            var OUTGOING = new JK4FlowModel.ForkNode(DUMMY_ID + '2');
            jNode.outgoing = OUTGOING;

            expect(jNode.outgoing.type).toBe(OUTGOING.type);
        });

    });

    describe('Final Node', function () {

        var fNode;

        beforeEach(function() {
            fNode = new JK4FlowModel.FinalNode(DUMMY_ID);
        });

        it('Should have a node type', function () {
            expect(fNode.type).toBe(JK4FlowModel.NODE_TYPE.FINAL);
        });

        it('Should allow id assignment on construction', function () {
            expect(fNode.id).toBe(DUMMY_ID);
        });

        it('Has no outgoing flow', function () {
            expect(fNode.outgoing).toBeUndefined();
        });

    });

    describe('Context Outgoing', function() {

        describe('Creation', function() {
            it('Should not be created with an empty context attribute parameter', function() {
                // TODO: Write a test
            });

            it('Should not be created with an empty condition parameter', function() {
                // TODO: Write a test
            });

            it('Should not be created with an invalid condition parameter value', function() {
                // TODO: Write a test
            });

            it('Should not be created with an invalid condition parameter value', function() {
                // TODO: Write a test
            });
        });

        describe('Target Property', function() {
            // TODO: Write tests
        });

        describe('Condition Execution', function() {

            describe('"Equals"', function() {

                it('Should be able to evaluate on numbers', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate on strings', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate on booleans', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate between strings and booleans', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate between numbers and booleans', function () {
                    // TODO: Write a test
                });

            });

            describe('"Not Equals"', function() {

                it('Should be able to evaluate on numbers', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate on strings', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate on booleans', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate between strings and booleans', function () {
                    // TODO: Write a test
                });

                it('Should be able to evaluate between numbers and booleans', function () {
                    // TODO: Write a test
                });

            });

            describe('"Greater Than"', function() {
                // TODO: Write tests
            });

            describe('"Greater or Equals Than"', function() {
                // TODO: Write tests
            });

            describe('"Lower Than"', function() {
                // TODO: Write tests
            });

            describe('"Lower or Equals Than"', function() {
                // TODO: Write tests
            });

            describe('"In"', function() {
                // TODO: Write tests
            });

            describe('"Ends With"', function() {
                // TODO: Write tests
            });

            describe('"Starts With"', function() {
                // TODO: Write tests
            });

        });
    });

    describe('Decision Node', function () {

        var AT_LEAST_TWO_OUTGOING_MSG_ERR = 'A Decision Node should redirect at least two outgoings';
        var OUTGOING_ERR_MSG = 'Decision Node should redirect to an Action, Decision, Fork Node or Final Node';

        var dNode;

        beforeEach(function() {
            dNode = new JK4FlowModel.DecisionNode(DUMMY_ID);
        });

        it('Should have a node type', function () {
            expect(dNode.type).toBe(JK4FlowModel.NODE_TYPE.DECISION);
        });

        // MORE TO COME IN HERE

    });

});
