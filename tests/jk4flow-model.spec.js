// Set of JSHint directives
/* global describe: true */
/* global beforeEach: true */
/* global it: true */
/* global expect: true */

'use strict';

var jk4flow = {
    model: require('../src/jk4flow-model').jk4flow.model
};

describe('JSON Workflow Model', function() {

    var DUMMY_INVALID_VALUE = 'INVALID';
    var DUMMY_ID = '#ID';
    var DUMMY_ATTR = 'ATTR';
    var DUMMY_VAL = 'VAL';

    describe('Initial Node', function () {

        var OUTGOING_ERR_MSG = 'Initial Node should redirect to an Action, Decision or Fork Node';

        var iNode;

        beforeEach(function() {
            iNode = new jk4flow.model.InitialNode(DUMMY_ID);
        });

        describe('Creation', function () {

            it('Should have a node type', function () {
                expect(iNode.type).toBe(jk4flow.model.NODE_TYPE.INITIAL);
            });

            it('Should allow id assignment on construction', function () {
                expect(iNode.id).toBe(DUMMY_ID);
            });

        });

        describe('Outgoing Property', function () {

            it('Should not allow an empty outgoing flow', function() {
                expect(
                    function() {
                        iNode.outgoing = undefined;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a number as outgoing flow', function() {
                expect(
                    function() {
                        iNode.outgoing = 1;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a string as outgoing flow', function() {
                expect(
                    function() {
                        iNode.outgoing = 'I\'m not a subclass of AbstractNode';
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an arbitrary object, even if has a valid type, as outgoing flow', function() {
                expect(
                    function() {
                        iNode.outgoing = { type: jk4flow.model.NODE_TYPE.ACTION };
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Abstract Node as outgoing flow', function() {
                expect(
                    function() {
                        iNode.outgoing = new jk4flow.model.AbstractNode(DUMMY_ID, jk4flow.model.NODE_TYPE.ACTION);
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Initial Node as outgoing flow', function () {
                expect(
                    function() {
                        iNode.outgoing = new jk4flow.model.InitialNode(DUMMY_ID + '2');
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a Final Node as outgoing flow', function () {
                expect(
                    function() {
                        iNode.outgoing = { type: jk4flow.model.NODE_TYPE.FINAL };
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a Join Node as outgoing flow', function () {
                expect(
                    function() {
                        iNode.outgoing = new jk4flow.model.JoinNode(DUMMY_ID + '2');
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should allow an Action Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.ActionNode(DUMMY_ID + '2');
                iNode.outgoing = OUTGOING;

                expect(iNode.outgoing instanceof jk4flow.model.ActionNode).toBe(true);
            });

            it('Should allow a Decision Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.DecisionNode(DUMMY_ID + '2');
                iNode.outgoing = OUTGOING;

                expect(iNode.outgoing.type).toBe(OUTGOING.type);
            });

            it('Should allow a Fork Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.ForkNode(DUMMY_ID + '2');
                iNode.outgoing = OUTGOING;

                expect(iNode.outgoing.type).toBe(OUTGOING.type);
            });

        });

    });

    describe('Action Node', function () {

        var OUTGOING_ERR_MSG = 'Action Node should redirect to an Action, Decision, Fork Node, Join or Final Node';
        var OUTGOING_ITSELF_ERR_MSG = 'Action Node should not redirect to itself';
        var CALLBACK_ERR_MSG = 'An Action Node callback should be a function';

        var aNode;

        beforeEach(function() {
            aNode = new jk4flow.model.ActionNode(DUMMY_ID);
        });

        describe('Creation', function () {

            it('Should have a node type', function () {
                expect(aNode.type).toBe(jk4flow.model.NODE_TYPE.ACTION);
            });

            it('Should allow id assignment on construction', function () {
                expect(aNode.id).toBe(DUMMY_ID);
            });

        });

        describe('Outgoing Property', function () {

            it('Should not allow an empty outgoing flow', function() {
                expect(
                    function() {
                        aNode.outgoing = undefined;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a number as outgoing flow', function() {
                expect(
                    function() {
                        aNode.outgoing = 1;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a string as outgoing flow', function() {
                expect(
                    function() {
                        aNode.outgoing = 'I\'m not a subclass of AbstractNode';
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an arbitrary object, even if has a valid type, as outgoing flow', function() {
                expect(
                    function() {
                        aNode.outgoing = { type: jk4flow.model.NODE_TYPE.ACTION };
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Abstract Node as outgoing flow', function() {
                expect(
                    function() {
                        aNode.outgoing = new jk4flow.model.AbstractNode(DUMMY_ID, jk4flow.model.NODE_TYPE.ACTION);
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Initial Node as outgoing flow', function () {
                expect(
                    function() {
                        aNode.outgoing = new jk4flow.model.InitialNode(DUMMY_ID + '2');
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should allow a Final Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.FinalNode(DUMMY_ID);
                aNode.outgoing = OUTGOING;

                expect(aNode.outgoing.type).toBe(OUTGOING.type);
            });

            it('Should allow a Join Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.JoinNode(DUMMY_ID + '2');
                aNode.outgoing = OUTGOING;

                expect(aNode.outgoing.type).toBe(OUTGOING.type);
            });

            it('Should allow another Action Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.ActionNode(DUMMY_ID + '2');
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
                var OUTGOING = new jk4flow.model.DecisionNode(DUMMY_ID + '2');
                aNode.outgoing = OUTGOING;

                expect(aNode.outgoing.type).toBe(OUTGOING.type);
            });

            it('Should allow a Fork Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.ForkNode(DUMMY_ID + '2');
                aNode.outgoing = OUTGOING;

                expect(aNode.outgoing.type).toBe(OUTGOING.type);
            });

        });

        describe('Callback Property', function () {

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

    });

    describe('Fork Node', function () {

        var AT_LEAST_TWO_OUTGOING_MSG_ERR = 'A Fork Node should redirect to at least two asynchronous processes';
        var OUTGOING_ERR_MSG = 'Fork Node should redirect to an Action or Decision Node';

        var fNode;

        beforeEach(function() {
            fNode = new jk4flow.model.ForkNode(DUMMY_ID);
        });

        describe('Creation', function () {

            it('Should have a node type', function () {
                expect(fNode.type).toBe(jk4flow.model.NODE_TYPE.FORK);
            });

            it('Should allow id assignment on construction', function () {
                expect(fNode.id).toBe(DUMMY_ID);
            });

        });

        describe('Outgoings Property', function () {

            it('Should not allow an empty outgoing flow', function () {
                expect(
                    function() {
                        fNode.outgoings = undefined;
                    }
                ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
            });

            it('Should not allow numbers in the set of outgoing flows', function() {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            1
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow strings in the set of outgoing flows', function() {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            'I\'m a string!'
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow arbitrary objects, even if they have valid types, in the set of outgoing flows', function() {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            { type: jk4flow.model.NODE_TYPE.ACTION }
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Abstract Node as outgoing flow', function() {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            new jk4flow.model.AbstractNode(DUMMY_ID + '3', jk4flow.model.NODE_TYPE.ACTION)
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a single object as outgoing flow', function () {
                expect(
                    function() {
                        fNode.outgoings = new jk4flow.model.ActionNode(DUMMY_ID + '2');
                    }
                ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
            });

            it('Should not allow a set of a single object as outgoing flow', function () {
                expect(
                    function() {
                        fNode.outgoings = [ new jk4flow.model.ActionNode(DUMMY_ID + '2') ];
                    }
                ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
            });

            it('Should not allow Initial Nodes in the set of outgoing flows', function () {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            new jk4flow.model.InitialNode(DUMMY_ID + '3')
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow Final Nodes in the set of outgoing flows', function () {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            { type: jk4flow.model.NODE_TYPE.FINAL }
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow another Fork Node in the set of outgoing flows', function () {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            new jk4flow.model.ForkNode(DUMMY_ID + '3')
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a Join Node in the set of outgoing flows', function () {
                expect(
                    function() {
                        fNode.outgoings = [
                            new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                            new jk4flow.model.JoinNode(DUMMY_ID + '2')
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should allow any set of Action and Decision Nodes for outgoing flows', function () {
                fNode.outgoings = [
                    new jk4flow.model.ActionNode(DUMMY_ID + '2'),
                    new jk4flow.model.DecisionNode(DUMMY_ID + '3'),
                    new jk4flow.model.ActionNode(DUMMY_ID + '4'),
                    new jk4flow.model.DecisionNode(DUMMY_ID + '5'),
                    new jk4flow.model.DecisionNode(DUMMY_ID + '6'),
                    new jk4flow.model.DecisionNode(DUMMY_ID + '7')
                ];

                expect(fNode.outgoings.length).toBe(6);
            });
        });

    });

    describe('Join Node', function () {

        var OUTGOING_ERR_MSG = 'Join Node should redirect to an Action, Decision, Fork Node or Final Node';

        var jNode;

        beforeEach(function() {
            jNode = new jk4flow.model.JoinNode(DUMMY_ID);
        });

        describe('Creation', function () {

            it('Should have a node type', function () {
                expect(jNode.type).toBe(jk4flow.model.NODE_TYPE.JOIN);
            });

            it('Should allow id assignment on construction', function () {
                expect(jNode.id).toBe(DUMMY_ID);
            });

        });

        describe('Outgoing Property', function () {

            it('Should not allow an empty outgoing flow', function() {
                expect(
                    function() {
                        jNode.outgoing = undefined;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a number as outgoing flow', function() {
                expect(
                    function() {
                        jNode.outgoing = 1;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a string as outgoing flow', function() {
                expect(
                    function() {
                        jNode.outgoing = 'I\'m not a subclass of AbstractNode';
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an arbitrary object, even if has a valid type, as outgoing flow', function() {
                expect(
                    function() {
                        jNode.outgoing = { type: jk4flow.model.NODE_TYPE.ACTION };
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Abstract Node as outgoing flow', function() {
                expect(
                    function() {
                        jNode.outgoing = new jk4flow.model.AbstractNode(DUMMY_ID, jk4flow.model.NODE_TYPE.ACTION);
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Initial Node as outgoing flow', function () {
                expect(
                    function() {
                        jNode.outgoing = new jk4flow.model.InitialNode(DUMMY_ID + '2');
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow another Join Node as outgoing flow', function () {
                expect(
                    function() {
                        jNode.outgoing = new jk4flow.model.JoinNode(DUMMY_ID + '2');
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should allow a Final Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.FinalNode(DUMMY_ID);
                jNode.outgoing = OUTGOING;

                expect(jNode.outgoing.type).toBe(OUTGOING.type);
            });

            it('Should allow an Action Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.ActionNode(DUMMY_ID + '2');
                jNode.outgoing = OUTGOING;

                expect(jNode.outgoing.type).toBe(OUTGOING.type);
            });

            it('Should allow a Decision Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.DecisionNode(DUMMY_ID + '2');
                jNode.outgoing = OUTGOING;

                expect(jNode.outgoing.type).toBe(OUTGOING.type);
            });

            it('Should allow a Fork Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.ForkNode(DUMMY_ID + '2');
                jNode.outgoing = OUTGOING;

                expect(jNode.outgoing.type).toBe(OUTGOING.type);
            });

        });

    });

    describe('Final Node', function () {

        var fNode;

        beforeEach(function() {
            fNode = new jk4flow.model.FinalNode(DUMMY_ID);
        });

        describe('Creation', function() {

            it('Should have a node type', function () {
                expect(fNode.type).toBe(jk4flow.model.NODE_TYPE.FINAL);
            });

            it('Should allow id assignment on construction', function () {
                expect(fNode.id).toBe(DUMMY_ID);
            });
        });

        describe('Outgoing Property', function () {

            it('Has no outgoing flow', function () {
                expect(fNode.outgoing).toBeUndefined();
            });

        });

    });

    describe('Context Outgoing', function() {

        var NO_FROM_NODE_ERR_MSG = 'A "from" Abstract Node subclass object with type DECISION must be provided';
        var NO_ATTR_ERR_MSG = 'A defined value for attribute parameter must be provided';
        var NO_COND_ERR_MSG = 'A defined value for condition parameter must be provided';
        var INVALID_COND_ERR_MSG = 'Invalid condition. Check for CONDITION_TYPE possible values';

        var dAttrNode = new jk4flow.model.DecisionNode(DUMMY_ID);

        describe('Creation', function() {

            it('Should not be created with an empty "from" node parameter', function() {
                expect(
                    function() {
                        new jk4flow.model.ContextOutgoing();
                    }
                ).toThrow(NO_FROM_NODE_ERR_MSG);
            });


            it('Should not be created with a non decision type "from" node parameter', function() {
                var aNode = new jk4flow.model.ActionNode(DUMMY_ID);
                expect(
                    function() {
                        new jk4flow.model.ContextOutgoing(aNode);
                    }
                ).toThrow(NO_FROM_NODE_ERR_MSG);
            });

            it('Should not be created with an empty context attribute parameter', function() {
                expect(
                    function() {
                        new jk4flow.model.ContextOutgoing(dAttrNode);
                    }
                ).toThrow(NO_ATTR_ERR_MSG);
            });

            it('Should not be created with an empty condition parameter', function() {
                expect(
                    function() {
                        new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR);
                    }
                ).toThrow(NO_COND_ERR_MSG);
            });

            it('Should not be created with an invalid condition parameter value', function() {
                expect(
                    function() {
                        new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, DUMMY_INVALID_VALUE);
                    }
                ).toThrow(INVALID_COND_ERR_MSG);
            });

            it('Should be created with right attribute and equals condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and not equals condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and greater than condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and greater or equals than condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and lower than condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and lower or equals than condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and lower or in condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and lower or starts with condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute and lower or ends with condition parameters but no value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS);
                expect(ctxOut).toBeDefined();
            });

            it('Should be created with right attribute, condition and int value parameter', function() {
                var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 'x');
                expect(ctxOut).toBeDefined();
            });

        });

        describe('Target Property', function() {

            var CTX_OUT_ERR_MSG = 'Context Outgoings should redirect to an Action, Decision, Fork or Final Node';
            var TARGET_ITSELF_ERR_MSG = 'Decision Node should not redirect to itself';

            var ctxOut;

            beforeEach(function() {
                ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, DUMMY_VAL);
            });

            it('Can\'t be empty', function() {
                expect(
                    function() {
                        ctxOut.target = undefined;
                    }
                ).toThrow(CTX_OUT_ERR_MSG);
            });

            it('Can\'t be a number', function() {
                expect(
                    function() {
                        ctxOut.target = 1;
                    }
                ).toThrow(CTX_OUT_ERR_MSG);
            });

            it('Can\'t be a string', function() {
                expect(
                    function() {
                        ctxOut.target = 'I\'m not an AbstractNode';
                    }
                ).toThrow(CTX_OUT_ERR_MSG);
            });

            it('Can\'t be an arbitrary object, even if has a valid type', function() {
                expect(
                    function() {
                        ctxOut.target = { type: jk4flow.model.NODE_TYPE.ACTION };
                    }
                ).toThrow(CTX_OUT_ERR_MSG);
            });

            it('Can\'t be an Abstract Node', function() {
                expect(
                    function() {
                        ctxOut.target = new jk4flow.model.AbstractNode(DUMMY_ID, jk4flow.model.NODE_TYPE.ACTION);
                    }
                ).toThrow(CTX_OUT_ERR_MSG);
            });

            it('Can\'t be an Initial Node', function() {
                expect(
                    function() {
                        ctxOut.target = new jk4flow.model.InitialNode(DUMMY_ID);
                    }
                ).toThrow(CTX_OUT_ERR_MSG);
            });

            it('Can\'t be a Join Node', function() {
                expect(
                    function() {
                        ctxOut.target = new jk4flow.model.JoinNode(DUMMY_ID);
                    }
                ).toThrow(CTX_OUT_ERR_MSG);
            });

            it('Can be an Action Node', function() {
                var aNode = new jk4flow.model.ActionNode(DUMMY_ID);
                ctxOut.target = aNode;

                expect(ctxOut.target).toBe(aNode);
            });

            it('Can be a Fork Node', function() {
                var fNode = new jk4flow.model.ForkNode(DUMMY_ID);
                ctxOut.target = fNode;

                expect(ctxOut.target).toBe(fNode);
            });

            it('Can be a Final Node', function() {
                var fNode = new jk4flow.model.FinalNode(DUMMY_ID);
                ctxOut.target = fNode;

                expect(ctxOut.target).toBe(fNode);
            });

            it('Can be a Decision Node', function() {
                var dNode = new jk4flow.model.DecisionNode(DUMMY_ID + '2');
                ctxOut.target = dNode;

                expect(ctxOut.target).toBe(dNode);
            });

            it('Can\'t be the same decision node that it\'s coming from', function() {
                expect(
                    function() {
                        ctxOut.target = dAttrNode;
                    }
                ).toThrow(TARGET_ITSELF_ERR_MSG);
            });

        });

        describe('Condition Execution', function() {

            describe('Before Validate', function() {

                var NO_CTX_OBJ_ERR_MSG = 'Cannot evaluate a condition without a flow context object';
                var NO_MATCH_ATTR_ERR_MSG = 'No attribute match between this Context Outgoing and the flow context object';

                it('Should not be able to evaluate without a flow context object', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 1);

                    expect(
                        function() {
                            ctxOut.isConditionSatisfied();
                        }
                    ).toThrow(NO_CTX_OBJ_ERR_MSG);
                });

                it('Should not be able to evaluate if the attribute being evaluated does not belong to the flow context object', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 1);

                    expect(
                        function() {
                            ctxOut.isConditionSatisfied( { } );
                        }
                    ).toThrow(NO_MATCH_ATTR_ERR_MSG);
                });

            });

            describe('"Equals"', function() {

                it('Should be able to evaluate on numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on numbers when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on strings when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, "I\'m a string!");
                    var ctx = {};
                    ctx[DUMMY_ATTR] = "I\'m a string!";

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on strings when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, "I\'m a string!");
                    var ctx = {};
                    ctx[DUMMY_ATTR] = "I\'m also a string!";

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be case insensitive when evaluating on strings', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, "I\'m a string!");
                    var ctx = {};
                    ctx[DUMMY_ATTR] = "I\'M A String!";

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on booleans when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on booleans when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = false;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 10;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate between strings and booleans', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 'true');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating between strings and booleans', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 'TrUe');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate between numbers and booleans (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, 'true');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 1;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

            });

            describe('"Not Equals"', function() {

                it('Should be able to evaluate on numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on numbers when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on strings when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, "I\'m a string!");
                    var ctx = {};
                    ctx[DUMMY_ATTR] = "I\'m another string!";

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on strings when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, "I\'m a string!");
                    var ctx = {};
                    ctx[DUMMY_ATTR] = "I\'m a string!";

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be case insensitive when evaluating on strings', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, "I\'m a string!");
                    var ctx = {};
                    ctx[DUMMY_ATTR] = "I\'M A String!";

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, false);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on booleans when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, false);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = false;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 10;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between strings and booleans', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, 'true');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be case insensitive when evaluating between strings and booleans', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, 'TrUe');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between numbers and booleans (always true)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.NEQ, 'true');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 1;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

            });

            describe('"Greater Than"', function() {

                it('Should be able to evaluate on numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on numbers when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on strings when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '15';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on strings when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '15';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should always be false if the number cannot be converted', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'NUMBER';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate between numbers and booleans (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 0;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

            });

            describe('"Greater or Equals Than"', function() {

                it('Should be able to evaluate on equal numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on greater numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on numbers when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 10;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on strings when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '15';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on strings when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '10';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should always be false if the number cannot be converted', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'NUMBER';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (true x true, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (true x false, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = false;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (false x false, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, false);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = false;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (false x true, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, false);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate between numbers and booleans (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.GEQT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 0;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

            });

            describe('"Lower Than"', function() {

                it('Should be able to evaluate on different numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 10;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on numbers when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on strings when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '10';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on strings when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '15';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should always be false if the number cannot be converted', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'NUMBER';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 10;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate between numbers and booleans (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 0;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

            });

            describe('"Lower or Equals Than"', function() {

                it('Should be able to evaluate on equal numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on lower numbers when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, 15);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 10;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on numbers when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on strings when result is true', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '15';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate on strings when result is false', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '15';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should always be false if the number cannot be converted', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, '10');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'NUMBER';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (true x true, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (true x false, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = false;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (false x false, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, false);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = false;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate on booleans (false x true, result is always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, false);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = true;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate between strings and numbers', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, '15');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 15;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate between numbers and booleans (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.LEQT, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 0;

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

            });

            describe('"In"', function() {

                it('Should be able to evaluate when substring is not inside another string (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'INVISIBLE');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRING_HAS_NO_INVISIBILITY_POWERS';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate when substring equals to string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRING';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when substring is at the end of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'BIGGER_STRING';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating at the end of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'StRiNg');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'BIGGER_sTrInG';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when substring is at the beginning of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRING_EVEN_BIGGER';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating at the beginning of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'StRiNg');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'sTrInG_EVEN_BIGGER';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when substring is inside the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'WHERE_IS_THE_STRING_HIDDEN?';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating inside the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 'StRiNg');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'Where_is_the_sTrInG_hidden?';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when the substring is actually a number that appears inside a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'There are much more than 10 things to do after installing Ubuntu';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when the substring is actually a boolean that appears inside a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'I believe that is true, Neo. You are the one!';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating a substring that is actually a boolean and appears inside a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.IN, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'WhAt Is TrUe? WhAt Is Not?';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

            });

            describe('"Ends With"', function() {

                it('Should be able to evaluate when substring is not at the end of another string (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, 'INVISIBLE');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRINGS_ARE_NOT_INVISIBL';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate when substring equals to string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRING';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when substring is at the end of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'BIGGER_STRING';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating at the end of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, 'StRiNg');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'BIGGER_sTrInG';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when substring is at the beginning of the string (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRING_EVEN_BIGGER';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate when substring is inside the string (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'WHERE_IS_THE_STRING_HIDDEN?';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate when the substring is actually a number that appears at the end of a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'I\'ve never seen Ben 10';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when the substring is actually a boolean that appears at the end of a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'Neo, I believe that is true';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating a substring that is actually a boolean and appears at the end of a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.ENDS, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'NeO, iT iS tHe MaTrIx, It IsN\'t TrUe';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

            });

            describe('"Starts With"', function() {

                it('Should be able to evaluate when substring is not at the beginning another string (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, 'INVISIBLE');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'INVISIBL_STRINGS_DO_NOT_EXIST';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate when substring equals to string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRING';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when substring is at the beginning of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'STRINGS THEORY? GET REAL! REAL?';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating at the beginning of the string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, 'StRiNg');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'sTrInGs_StReTcH_eAsIlY';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when substring is at the end of the string (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'TALK AGAIN ABOUT THIS THEORY ABOUT ONE SINGLE STRING';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate when substring is inside the string (always false)', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, 'STRING');
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'WHERE_IS_THE_STRING_HIDDEN?';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(false);
                });

                it('Should be able to evaluate when the substring is actually a number that appears at the beginning of a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, 10);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = '10 things to do after installing Ubuntu are not enough';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be able to evaluate when the substring is actually a boolean that appears at the beginning of a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'true colors... What does this mean?';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

                it('Should be case insensitive when evaluating a substring that is actually a boolean and appears at the beginning of a string', function () {
                    var ctxOut = new jk4flow.model.ContextOutgoing(dAttrNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.STARTS, true);
                    var ctx = {};
                    ctx[DUMMY_ATTR] = 'TrUe, I hAvE nO iDeA aBoUt StRiNg ThEoRy';

                    var result = ctxOut.isConditionSatisfied(ctx);

                    expect(result).toBe(true);
                });

            });

        });
    });

    describe('Decision Node', function () {

        var AT_LEAST_TWO_OUTGOING_MSG_ERR = 'A Decision Node should redirect to at least one outgoing flow besides the default one (a.k.a otherwise flow)';
        var OUTGOING_ERR_MSG = 'A Decision Node\'s outgoing must be of Context Outgoing type with a valid node as target';

        var dNode;

        beforeEach(function() {
            dNode = new jk4flow.model.DecisionNode(DUMMY_ID);
        });

        describe('Creation', function() {

            it('Should have a node type', function () {
                expect(dNode.type).toBe(jk4flow.model.NODE_TYPE.DECISION);
            });

            it('Should allow id assignment on construction', function () {
                expect(dNode.id).toBe(DUMMY_ID);
            });

        });

        describe('Otherwise Property (a.k.a. Decision\'s Default Outgoing Flow)', function () {

            var OUTGOING_ERR_MSG = 'Decision Node default outgoing (otherwise condition) should redirect to an Action, Decision, Fork Node or Final Node';
            var OUTGOING_ITSELF_ERR_MSG = 'Decision Node should not redirect to itself';

            it('Should not allow an empty outgoing flow', function() {
                expect(
                    function() {
                        dNode.otherwise = undefined;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a number as outgoing flow', function() {
                expect(
                    function() {
                        dNode.otherwise = 1;
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a string as outgoing flow', function() {
                expect(
                    function() {
                        dNode.otherwise = 'I\'m not a subclass of AbstractNode';
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an arbitrary object, even if has a valid type, as outgoing flow', function() {
                expect(
                    function() {
                        dNode.otherwise = { type: jk4flow.model.NODE_TYPE.ACTION };
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Abstract Node as outgoing flow', function() {
                expect(
                    function() {
                        dNode.otherwise = new jk4flow.model.AbstractNode(DUMMY_ID, jk4flow.model.NODE_TYPE.ACTION);
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Initial Node as outgoing flow', function () {
                expect(
                    function() {
                        dNode.otherwise = new jk4flow.model.InitialNode(DUMMY_ID + '2');
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a Join Node as outgoing flow', function () {
                expect(
                    function() {
                        dNode.otherwise = new jk4flow.model.JoinNode(DUMMY_ID + '2');
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should allow another Decision Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.DecisionNode(DUMMY_ID + '2');
                dNode.otherwise = OUTGOING;

                expect(dNode.otherwise.type).toBe(OUTGOING.type);
            });

            it('Should allow a Final Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.FinalNode(DUMMY_ID);
                dNode.otherwise = OUTGOING;

                expect(dNode.otherwise.type).toBe(OUTGOING.type);
            });

            it('Should not allow itself as an outgoing flow', function () {
                expect(
                    function() {
                        dNode.otherwise = dNode;
                    }
                ).toThrow(OUTGOING_ITSELF_ERR_MSG);
            });

            it('Should allow a Fork Node as outgoing flow', function () {
                var OUTGOING = new jk4flow.model.ForkNode(DUMMY_ID + '2');
                dNode.otherwise = OUTGOING;

                expect(dNode.otherwise.type).toBe(OUTGOING.type);
            });

        });

        describe('Outgoings Property (Context Outgoings)', function() {

            var ctxOut;

            beforeEach(function() {
                ctxOut = new jk4flow.model.ContextOutgoing(dNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, DUMMY_VAL);
            });

            it('Should not allow an undefined outgoing flows set', function () {
                expect(
                    function() {
                        dNode.outgoings = undefined;
                    }
                ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
            });

            it('Should not allow empty outgoing flows set', function () {
                expect(
                    function() {
                        dNode.outgoings = [ ];
                    }
                ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
            });

            it('Should not allow an arbitrary object instead of an array', function () {
                expect(
                    function() {
                        dNode.outgoings = { 0: ctxOut };
                    }
                ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
            });

            it('Should allow a a single object in the outgoing set', function () {
                dNode.outgoings = [ ctxOut ];

                expect(dNode.outgoings.length).toBe(1);
            });


            it('Should not allow numbers in the set of outgoing flows', function() {
                expect(
                    function() {
                        dNode.outgoings = [ ctxOut, 1 ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow strings in the set of outgoing flows', function() {
                expect(
                    function() {
                        dNode.outgoings = [ ctxOut, 'I\'m a string!' ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow arbitrary objects, even if they have valid types, in the set of outgoing flows', function() {
                expect(
                    function() {
                        dNode.outgoings = [ ctxOut, { type: jk4flow.model.NODE_TYPE.ACTION } ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow an Abstract Node as outgoing flow', function() {
                expect(
                    function() {
                        dNode.outgoings = [
                            ctxOut,
                            new jk4flow.model.AbstractNode(DUMMY_ID + '3', jk4flow.model.NODE_TYPE.ACTION)
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a single object as outgoing flow', function () {
                expect(
                    function() {
                        dNode.outgoings = new jk4flow.model.ActionNode(DUMMY_ID + '2');
                    }
                ).toThrow(AT_LEAST_TWO_OUTGOING_MSG_ERR);
            });

            it('Should not allow Initial Nodes in the set of outgoing flows', function () {
                expect(
                    function() {
                        dNode.outgoings = [
                            ctxOut,
                            new jk4flow.model.InitialNode(DUMMY_ID + '3')
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow a Join Node in the set of outgoing flows', function () {
                expect(
                    function() {
                        dNode.outgoings = [
                            ctxOut,
                            new jk4flow.model.JoinNode(DUMMY_ID + '2')
                        ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should not allow itself in the set of outgoing flows', function () {
                expect(
                    function() {
                        dNode.outgoings = [ ctxOut, dNode ];
                    }
                ).toThrow(OUTGOING_ERR_MSG);
            });

            it('Should allow any set of Action, Decision, Fork and Final Nodes for outgoing flows', function () {
                var ctxOutA = new jk4flow.model.ContextOutgoing(dNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, '1');
                ctxOutA.target = new jk4flow.model.ActionNode(DUMMY_ID + '2');

                var ctxOutD = new jk4flow.model.ContextOutgoing(dNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, '2');
                ctxOutD.target = new jk4flow.model.DecisionNode(DUMMY_ID + '3');

                var ctxOutFk = new jk4flow.model.ContextOutgoing(dNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, '3');
                ctxOutFk.target = new jk4flow.model.ForkNode(DUMMY_ID + '4');

                var ctxOutFi = new jk4flow.model.ContextOutgoing(dNode, DUMMY_ATTR, jk4flow.model.CONDITION_TYPE.EQ, '4');
                ctxOutFi.target = new jk4flow.model.FinalNode(DUMMY_ID + '5');

                dNode.outgoings = [ ctxOutA, ctxOutD, ctxOutFk, ctxOutFi ];

                expect(dNode.outgoings.length).toBe(4);
            });

        });

    });

});
