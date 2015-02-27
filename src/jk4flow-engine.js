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

    var Executor = function(workflow) {

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