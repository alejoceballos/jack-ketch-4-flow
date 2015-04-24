if (!jk4flow) {
    var jk4flow = { };
}

jk4flow.engine = (function(model) {
    'use strict';

    if (!model && module && require) {
        model = require('./jk4flow-model').jk4flow.model;
    }

    if (!Q) {
        var Q = require('q');
    }

    /**
     * The executor object is the core of the executing engine! Its goals are:
     *
     * + To execute the connected objects instances by "traveling" through each one in the
     * "dependency tree";
     * + Make sure that the flow context object "travels along", node by node;
     * + Execute Action Nodes' callbacks;
     * + Check for context attributes in Decision Nodes to drive the flow;
     * + Start a set of promises to be called asynchronously when a Fork Nodes is found;
     * + Assure that Join Nodes gather all asynchronous promises started by a Fork Node.
     *
     * @param {model.Workflow} workflow The Workflow to be executed
     * @constructor
     */
    var Executor = function(workflow) {
        if (!(workflow instanceof model.Workflow)) {
            throw 'First argument must be an instance of Workflow';
        }

        var _executionError;
        Object.defineProperty(this, 'executionError', {
            get: function() {
                return _executionError;
            },
            enumerable: true,
            configurable: false
        });

        this.run = function() {
            var deferred = Q.defer();
            var result = handleNode(workflow.initialNode.outgoing);
            deferred.resolve(
                function() {
                    return result
                }
            );
            return deferred.promise;
        };

        function handleNode(node) {
            var promise;

            if (node instanceof model.ActionNode) {
                promise = Q.try(
                    function() {
                        return handleActionNode(node);
                    }
                ).then(
                    function(next) {
                        return handleNode(next);
                    }
                );

            } else if (node instanceof model.DecisionNode) {
                promise = Q.try(
                    function() {
                        return handleDecision(node);
                    }
                ).then(
                    function(next) {
                        return handleNode(next);
                    }
                );

            } else if (node instanceof model.ForkNode) {
                promise = Q.try(
                    function() {
                        return handleForkNode(node);
                    }
                ).then(
                    function(jNode) {
                        return handleNode(jNode.outgoing);
                    }
                );

            } else if (node instanceof model.FinalNode) {
                promise = Q(undefined);

            } else if (node instanceof model.JoinNode) {
                promise = Q(node); // Just return a Join Node as promise to the starting Fork Node

            } else {
                throw 'Next node is not a valid workflow node!'
            }

            return promise;
        }

        function handleActionNode(aNode) {
            var deferred = Q.defer();
            aNode.callback(workflow.context);
            deferred.resolve(aNode.outgoing);
            return deferred.promise;
        }

        function handleDecision(dNode) {
            var deferred = Q.defer();

            for (var idx in dNode.outgoings) {
                var ctxOut = dNode.outgoings[idx];

                if (ctxOut.isConditionSatisfied(workflow.context)) {
                    deferred.resolve(ctxOut.target);
                    return deferred.promise;
                }
            }

            deferred.resolve(dNode.otherwise);
            return deferred.promise;
        }

        function handleForkNode(fNode) {
            function handleOutgoing(outgoing) {
                var handOutDeferred = Q.defer();

                handleNode(outgoing).then(
                    function(result) {
                        handOutDeferred.resolve(result);
                    }
                );

                return handOutDeferred.promise;
            }

            var handleAllDeferred = Q.defer();

            var promises = [];

            for (var idx in fNode.outgoings) {
                promises.push(handleOutgoing(fNode.outgoings[idx]));
            }

            Q.all(promises).then(
                function(results) {
                    if (!results || !(results instanceof Array) || results.length === 0) {
                        handleAllDeferred.reject('No Join Node detected!');

                    } else {
                        var last;
                        var curr;
                        var err;

                        for (idx in results) {
                            if (!last) last = results[idx];
                            curr = results[idx];

                            if (curr !== last) {
                                err = 'After a fork, the same join node should be the target';
                                break;

                            } else {
                                last = curr;
                            }
                        }

                        if (err) {
                            handleAllDeferred.reject(err);

                        } else {
                            handleAllDeferred.resolve(curr)
                        }
                    }
                }
            );

            return handleAllDeferred.promise;
        }

    };

    /**
     * The main goal of the engine is to create executors that will run the workflow. One
     * single engine can create as many executors as needed and each execution will not mess
     * with another, they have different scopes, even if the same workflow is being executed
     * by different engines.
     *
     * An executor object cannot be manually instantiated. It must be created using the Factory
     * Method provided by the Engine object.
     *
     * NOTE: There is no reason to instantiate more than one Engine. The reason that it was not
     * made a Singleton or the factory method is not a static one is that it becomes harder to
     * test (without some workarounds like spies) and even harder to extend.
     *
     * @constructor
     */
    var Engine = function() {
        this.createExecutor = function(workflow) {
            return new Executor(workflow);
        };
    };

    /**
     * Externalization
     */

    var result = {
        Engine: Engine
    };

    if (module && module.exports) {
        module.exports = {
            jk4flow: {
                engine: result
            }
        };
    }

    return result;
})();