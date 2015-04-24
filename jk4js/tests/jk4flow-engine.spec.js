// Set of JSHint directives
/* global describe: true */
/* global beforeEach: true */
/* global it: true */
/* global expect: true */

'use strict';

var Q = require('q');

var jk4flow = {
    model: require('jk4flow-model').jk4flow.model,
    engine: require('jk4flow-engine').jk4flow.engine
};

describe('JSON Workflow Engine', function() {

    var DUMMY_INVALID_VALUE = 'INVALID';
    var DUMMY_ID = '#ID';
    var DUMMY_ATTR = 'ATTR';
    var DUMMY_VAL = 'VAL';

    describe('Executor Creation', function () {

        var NO_WORKFLOW_ERR_MSG = 'First argument must be an instance of Workflow';

        var engine = new jk4flow.engine.Engine();

        it('+ Should not allow an empty argument', function() {
            expect(
                function() {
                    engine.createExecutor();
                }
            ).toThrow(NO_WORKFLOW_ERR_MSG);
        });

        it('+ Should not allow a number as argument', function() {
            expect(
                function() {
                    engine.createExecutor(1);
                }
            ).toThrow(NO_WORKFLOW_ERR_MSG);
        });

        it('+ Should not allow a boolean as argument', function() {
            expect(
                function() {
                    engine.createExecutor(true);
                }
            ).toThrow(NO_WORKFLOW_ERR_MSG);
        });

        it('+ Should not allow a string as argument', function() {
            expect(
                function() {
                    engine.createExecutor('I\'m a string');
                }
            ).toThrow(NO_WORKFLOW_ERR_MSG);
        });

        it('+ Should not allow an ordinary object as argument', function() {
            expect(
                function() {
                    engine.createExecutor( { } );
                }
            ).toThrow(NO_WORKFLOW_ERR_MSG);
        });

        it('+ Should only allow a workflow object as argument', function() {
            var iNode = new jk4flow.model.InitialNode(DUMMY_ID);
            var wf = new jk4flow.model.Workflow(iNode);
            var executor = engine.createExecutor(wf);

            expect(executor).toBeDefined();
        });

        it('+ Each executor creation must generate a different object', function() {
            var iNode = new jk4flow.model.InitialNode(DUMMY_ID);
            var wf = new jk4flow.model.Workflow(iNode);
            var executor1 = engine.createExecutor(wf);
            var executor2 = engine.createExecutor(wf);

            expect(executor1).not.toBe(executor2);
        });

    });

    describe('Execution', function () {

        var engine = new jk4flow.engine.Engine();

        describe('Level: simple (only actions)', function () {
            it('+ Workflow: (INITIAL)->(ACTION)->(FINAL)', function(done) {
                var final = new jk4flow.model.FinalNode('#FN:0003');

                var action = new jk4flow.model.ActionNode('#AN:0002');
                action.outgoing = final;
                action.callback = function(context) {
                    context.result = 'Written by an Action Node!';
                };

                var initial = new jk4flow.model.InitialNode('#IN:0001');
                initial.outgoing = action;

                var workflow = new jk4flow.model.Workflow(initial);

                var executor = engine.createExecutor(workflow);

                Q.try(executor.run()).then(
                    function(result) {
                        expect(workflow.context.result).toBe('Written by an Action Node!');
                    }
                ).catch(
                    function(err) {
                        expect(err.toString()).toBe('Error should have never happened')
                    }
                ).finally(done);
            });

            it('+ Workflow: (INITIAL)->(ACTION)->(ACTION)->(FINAL)', function(done) {
                var final = new jk4flow.model.FinalNode('#FN:0004');

                var action2 = new jk4flow.model.ActionNode('#AN:0003');
                action2.outgoing = final;
                action2.callback = function(context) {
                    context.result += ' Actually, by two!!!';
                };

                var action1 = new jk4flow.model.ActionNode('#AN:0002');
                action1.outgoing = action2;
                action1.callback = function(context) {
                    context.result = 'Written by an Action Node!';
                };

                var initial = new jk4flow.model.InitialNode('#IN:0001');
                initial.outgoing = action1;

                var workflow = new jk4flow.model.Workflow(initial);

                var executor = engine.createExecutor(workflow);

                Q.try(executor.run()).then(
                    function(result) {
                        expect(workflow.context.result).toBe('Written by an Action Node! Actually, by two!!!');
                    }
                ).catch(
                    function(err) {
                        expect(err.toString()).toBe('Error should have never happened')
                    }
                ).finally(done);
            });
        });

        describe('Level: medium (with actions and decisions):\n' +
        '                             \n' +
        '            (INI)       #1   \n' +
        '              |              \n' +
        '             \\|/            \n' +
        '            (DEC)       #2   \n' +
        '            /   \\           \n' +
        '          |/_   _\\|         \n' +
        '        (ACT)   (ACT)   #3/#4\n' +
        '           \\     /          \n' +
        '           _\\| |/_          \n' +
        '            (FIN)       #5   \n' +
        '                             ', function () {

            var action1;
            var action2;
            var decision;
            var workflow;

            beforeEach(function() {
                var final = new jk4flow.model.FinalNode('#FN:0005');

                action2 = new jk4flow.model.ActionNode('#AN:0004');
                action2.outgoing = final;
                action2.callback = function(context) {
                    context.result = action2.id;
                };

                action1 = new jk4flow.model.ActionNode('#AN:0003');
                action1.outgoing = final;
                action1.callback = function(context) {
                    context.result = action1.id;
                };

                decision = new jk4flow.model.DecisionNode('#DN:0002');

                var initial = new jk4flow.model.InitialNode('#IN:0001');
                initial.outgoing = decision;

                workflow = new jk4flow.model.Workflow(initial);
            });

            it('The result path should be: #1 -> #2 -> #3 -> #5', function(done) {
                var ctxOut = new jk4flow.model.ContextOutgoing(decision, 'test', jk4flow.model.CONDITION_TYPE.EQ, 'Go_To_ThE_LeFt');
                ctxOut.target = action1;
                decision.outgoings = [ ctxOut ];
                decision.otherwise = action2;

                workflow.context.test = 'GO_TO_THE_LEFT';

                var executor = engine.createExecutor(workflow);

                Q.try(executor.run()).then(
                    function(result) {
                        expect(workflow.context.result).toBe(action1.id);
                    }
                ).catch(
                    function(err) {
                        expect(err.toString()).toBe('Error should have never happened')
                    }
                ).finally(done);
            });

            it('The result path should be: #1 -> #2 -> #4 -> #5', function(done) {
                var ctxOut = new jk4flow.model.ContextOutgoing(decision, 'test', jk4flow.model.CONDITION_TYPE.EQ, 'Go_To_ThE_RiGhT');
                ctxOut.target = action2;
                decision.outgoings = [ ctxOut ];
                decision.otherwise = action1;

                workflow.context.test = 'GO_TO_THE_RIGHT';

                var executor = engine.createExecutor(workflow);

                Q.try(executor.run()).then(
                    function(result) {
                        expect(workflow.context.result).toBe(action2.id);
                    }
                ).catch(
                    function(err) {
                        expect(err.toString()).toBe('Error should have never happened')
                    }
                ).finally(done);
            });

        });

        describe('Level: Complex (with actions, decisions, forks and joins):\n' +
        '                             \n' +
        '            (INI)       #1   \n' +
        '              |              \n' +
        '             \\|/            \n' +
        '            -----       #2   \n' +
        '            /   \\           \n' +
        '          |/_   _\\|         \n' +
        '        (ACT)   (ACT)   #3/#4\n' +
        '           \\     /          \n' +
        '           _\\| |/_          \n' +
        '            -----       #5   \n' +
        '              |              \n' +
        '             \\|/            \n' +
        '            (ACT)       #6   \n' +
        '              |              \n' +
        '             \\|/            \n' +
        '            (DEC)       #7   \n' +
        '            /   \\           \n' +
        '          |/_   _\\|         \n' +
        '        (ACT)   (ACT)   #8/#9\n' +
        '           \\     /          \n' +
        '           _\\| |/_          \n' +
        '            (FIN)       #10  \n' +
        '                             ', function () {


            var OK = 'Ok';
            var NOT_OK = 'Not Ok';

            var workflow;

            beforeEach(function() {
                var fin10 = new jk4flow.model.FinalNode('#FN:0010');

                var act9 = new jk4flow.model.ActionNode('#AN:0009');
                act9.outgoing = fin10;
                act9.callback = function(context) {
                    context.result = NOT_OK;
                };

                var act8 = new jk4flow.model.ActionNode('#AN:0008');
                act8.outgoing = fin10;
                act8.callback = function(context) {
                    context.result = OK;
                };

                var dec7 = new jk4flow.model.DecisionNode('#DN:0007');
                var ctxOut = new jk4flow.model.ContextOutgoing(dec7, 'v', jk4flow.model.CONDITION_TYPE.LEQT, 3);
                ctxOut.target = act8;
                dec7.outgoings = [ ctxOut ];
                dec7.otherwise = act9;

                var act6 = new jk4flow.model.ActionNode('#AN:0006');
                act6.outgoing = dec7;
                act6.callback = function(context) {
                    context.v = context.x + context.y;
                };

                var join5 = new jk4flow.model.JoinNode('#AN:0005');
                join5.outgoing = act6;

                var act4 = new jk4flow.model.ActionNode('#AN:0004');
                act4.outgoing = join5;
                act4.callback = function(context) {
                    context.y = context.val1 * context.val2;
                };

                var act3 = new jk4flow.model.ActionNode('#AN:0003');
                act3.outgoing = join5;
                act3.callback = function(context) {
                    context.x = context.val1 + context.val2;
                };

                var fork2 = new jk4flow.model.ForkNode('#AN:0002');
                fork2.outgoings = [ act3, act4 ];

                var ini1 = new jk4flow.model.InitialNode('#IN:0001');
                ini1.outgoing = fork2;

                workflow = new jk4flow.model.Workflow(ini1);
            });

            it('For values "val1 = 1" and "val2 = 1" the result path should be: #1 -> #2 -> (#3,#4) -> #5 -> #6 -> #7 -> #8 -> #10 (result: "Ok")', function(done) {
                workflow.context.val1 = 1;
                workflow.context.val2 = 1;

                var executor = engine.createExecutor(workflow);

                Q.try(executor.run()).then(
                    function(result) {
                        expect(workflow.context.result).toBe(OK);
                    }
                ).catch(
                    function(err) {
                        expect(err.toString()).toBe('Error should have never happened')
                    }
                ).finally(done);
            });

            it('For values "val1 = 2" and "val2 = 3" the result path should be: #1 -> #2 -> (#3,#4) -> #5 -> #6 -> #7 -> #9 -> #10 (result: "Not Ok")', function(done) {
                workflow.context.val1 = 2;
                workflow.context.val2 = 3;

                var executor = engine.createExecutor(workflow);

                Q.try(executor.run()).then(
                    function(result) {
                        expect(workflow.context.result).toBe(NOT_OK);
                    }
                ).catch(
                    function(err) {
                        expect(err.toString()).toBe('Error should have never happened')
                    }
                ).finally(done);
            });
        });
    });

});
