if (!jk4flow) {
    var jk4flow = { };
}

jk4flow.model = (function() {
    "use strict";

    /**
     * All types of nodes accepted in jk4flow diagrams
     *
     * @type {{INITIAL: string, ACTION: string, DECISION: string, FORK: string, JOIN: string, FINAL: string}}
     */
    var NODE_TYPE = {
        INITIAL: 'INITIAL',
        ACTION: 'ACTION',
        DECISION: 'DECISION',
        FORK: 'FORK',
        JOIN: 'JOIN',
        FINAL: 'FINAL'
    };

    function isValidNode(node) {
        var aNode = new AbstractNode('DUMMY_ID', NODE_TYPE.ACTION);
        if (!node || !(node instanceof AbstractNode) || aNode.constructor === node.constructor) {
            return false;
        }

        return true
    }

    function isNodeTypeOf(node, types) {
        for (var idx in types) {
            if (types[idx] === node.type) {
                return true;
            }
        }

        return false;
    }

    function areValidNodes(nodes) {
        if (!nodes || !(nodes instanceof Array)) {
            return false;
        }

        for (var idx in nodes) {
            if (!isValidNode(nodes[idx])) {
                return false;
            }
        }

        return true;
    }

    function areNodesTypesOf(nodes, types) {
        for (var nIdx in nodes) {
            var result = false;

            for (var tIdx in types) {
                if (types[tIdx] === nodes[nIdx].type) {
                    result = true;
                    break;
                }
            }

            if (!result) return false;
        }

        return true;
    }

    /**
     * Abstract Node is the template class for all nodes in the Activity diagram
     *
     * @param {string} id The node identification
     * @param {NODE_TYPE} type Defines the type of the node being created using this constructor
     * @constructor
     */
    var AbstractNode = function(id, type) {
        function validateNodeType(type) {
            if (!NODE_TYPE[type]) {
                throw 'Invalid node type: ' + type;
            }
        }

        var _id = id;
        Object.defineProperty(this, 'id', {
            get: function() {
                return _id;
            },
            set: function(val) {
                _id = val;
            },
            enumerable: true,
            configurable: false
        });

        validateNodeType(type);
        var _type = type;
        Object.defineProperty(this, 'type', {
            get: function() {
                return _type;
            },
            enumerable: false,
            configurable: false
        });
    };

    /**
     * It is only a kick start point to let the engine know where to begin processing.
     *
     * Basic Rules: (1) No flow coming into; (2) Only one flow going out; (3) Its outgoing flow
     * must target an Action Node, a Decision Node or a Fork Node.
     *
     * @param {string} id The node identification
     * @constructor
     */
    var InitialNode = function(id) {
        AbstractNode.call(this, id, NODE_TYPE.INITIAL);

        var _outgoing;
        Object.defineProperty(this, 'outgoing', {
            get: function() {
                return _outgoing;
            },
            set: function(val) {
                if (!isValidNode(val) ||
                    !isNodeTypeOf(val, [
                        NODE_TYPE.ACTION,
                        NODE_TYPE.DECISION,
                        NODE_TYPE.FORK])) {
                    throw 'Initial Node should redirect to an Action, Decision or Fork Node';
                }

                _outgoing = val;
            },
            enumerable: true,
            configurable: false
        });
    };

    InitialNode.prototype = Object.create(AbstractNode.prototype, {
        constructor: {
            configurable: true,
            enumerable: true,
            value: InitialNode,
            writable: true
        }
    });

    /**
     * Where the magic happens! Each action node corresponds to a programming unit responsible for
     * some real processing of the workflow. It is done by associating one callback (function/method)
     * to it. Any callback will always receive one flow context object as first argument, this
     * object wraps all data that mst be passed by through the flow execution. If the action node
     * needs to pass along some information, just put it into the flow context object, no need to
     * return anything.
     *
     * Basic Rules: (1) Many as possible flows coming into; (2) Only one flow going out; (3) Its
     * outgoing flow may target another Action Node, a Final Node, a Decision Node, a Fork Node
     * and even a Join Node, but only if it is part of an asynchronous flow started by a previous
     * Fork Node. (4) Its outgoing flow cannot target itself.
     *
     * @param {string} id The node identification
     * @constructor
     */
    var ActionNode = function(id) {
        AbstractNode.call(this, id, NODE_TYPE.ACTION);

        var _outgoing;
        Object.defineProperty(this, 'outgoing', {
            get: function() {
                return _outgoing;
            },
            set: function(val) {
                if (!isValidNode(val) ||
                    !isNodeTypeOf(val, [
                        NODE_TYPE.ACTION,
                        NODE_TYPE.DECISION,
                        NODE_TYPE.FORK,
                        NODE_TYPE.FINAL,
                        NODE_TYPE.JOIN])) {
                    throw 'Action Node should redirect to an Action, Decision, Fork Node, Join or Final Node';

                } else if (val instanceof ActionNode && val.id === this.id) {
                    throw 'Action Node should not redirect to itself';
                }

                _outgoing = val;
            },
            enumerable: true,
            configurable: false
        });

        var _callback;
        Object.defineProperty(this, 'callback', {
            get: function() {
                return _callback;
            },
            set: function(val) {
                if (!val || typeof(val) !== 'function') {
                    throw 'An Action Node callback should be a function';
                }

                _callback = val;
            },
            enumerable: true,
            configurable: false
        });
    };

    ActionNode.prototype = Object.create(AbstractNode.prototype, {
        constructor: {
            configurable: true,
            enumerable: true,
            value: ActionNode,
            writable: true
        }
    });

    /**
     * Starts an asynchronous process.
     *
     * All flows going out a Fork Node will be treated asynchronously until they find a Join Node,
     * where processing becomes synchronous again.
     *
     * Be aware that starting many asynchronous flows may be hard to manage, it also may happen
     * if an asynchronous flow drives back to some node in the flow that was previously synchronous.
     * Pay attention when diagramming complex workflows.
     *
     * Basic Rules: (1) Many as possible flows coming into; (2) Two or more flows going out.
     * (3) Its outgoing flow may target an Action Node or a Decision Node. Do not terminate an
     * asynchronous process without joining it again, please.
     *
     * @param {string} id The node identification
     * @constructor
     */
    var ForkNode = function(id) {
        AbstractNode.call(this, id, NODE_TYPE.FORK);

        var _outgoings;
        Object.defineProperty(this, 'outgoings', {
            get: function() {
                return _outgoings;
            },
            set: function(val) {
                if (!val || !(val instanceof Array) || val.length < 2) {
                    throw 'A Fork Node should redirect to at least two asynchronous processes';
                }

                if (!areValidNodes(val) ||
                    !areNodesTypesOf(val, [
                        NODE_TYPE.ACTION,
                        NODE_TYPE.DECISION])) {
                    throw 'Fork Node should redirect to an Action or Decision Node';
                }

                _outgoings = val;
            },
            enumerable: true,
            configurable: false
        });
    };

    ForkNode.prototype = Object.create(AbstractNode.prototype, {
        constructor: {
            configurable: true,
            enumerable: true,
            value: ForkNode,
            writable: true
        }
    });

    /**
     * Responsible for gathering all asynchronous processes started by a Fork Node.
     *
     * Basic Rules: (1) Many as possible flows coming into; (2) Only one flow going out;
     * (3) Its outgoing flow may target an Action Node, Decision Node, a Final Node or
     * another Fork Node. Think about it! I could only start a set of asynchronous processes
     * to speed up data gathering. After having all data needed, start another to speed up
     * its use.
     *
     * @param {string} id The node identification
     * @constructor
     */
    var JoinNode = function(id) {
        AbstractNode.call(this, id, NODE_TYPE.JOIN);

        var _outgoing;
        Object.defineProperty(this, 'outgoing', {
            get: function() {
                return _outgoing;
            },
            set: function(val) {
                if (!isValidNode(val) ||
                    !isNodeTypeOf(val, [
                        NODE_TYPE.ACTION,
                        NODE_TYPE.DECISION,
                        NODE_TYPE.FORK,
                        NODE_TYPE.FINAL])) {
                    throw 'Join Node should redirect to an Action, Decision, Fork Node or Final Node';
                }

                _outgoing = val;
            },
            enumerable: true,
            configurable: false
        });
    };

    JoinNode.prototype = Object.create(AbstractNode.prototype, {
        constructor: {
            configurable: true,
            enumerable: true,
            value: JoinNode,
            writable: true
        }
    });

    /**
     * Establishes the end of the flow.
     *
     * At first I thought this node wasn't really necessary, for example, if I just reach a last
     * Action Node (without outgoing control flow) the flow should be terminated too. But the Final
     * Node, besides establishing a formal end to our workflow, should allow the return of the
     * context flow object to the programming structure that started it. So I decided to make it
     * mandatory, and to simplify (my life, of course) I also insist that it shall be unique (there
     * can be only one!).
     *
     * Basic Rules: (1) Many as possible flows coming into; (2) No flow going out.
     *
     * @param {string} id The node identification
     * @constructor
     */
    var FinalNode = function(id) {
        AbstractNode.call(this, id, NODE_TYPE.FINAL);
    };

    FinalNode.prototype = Object.create(AbstractNode.prototype, {
        constructor: {
            configurable: true,
            enumerable: true,
            value: FinalNode,
            writable: true
        }
    });

    /**
     * All condition types currently accepted by JK4Flow engine.
     *
     * @type {{EQ: string, NEQ: string, GT: string, GEQT: string, LT: string, LEQT: string, IN: string, ENDS: string, STARTS: string}}
     */
    var CONDITION_TYPE = {
        EQ: 'EQ',
        NEQ: 'NEQ',
        GT: 'GT',
        GEQT: 'GEQT',
        LT: 'LT',
        LEQT: 'LEQT',
        IN: 'IN',
        ENDS: 'ENDS',
        STARTS: 'STARTS'
    };

    /**
     * The Context Outgoing object is a special outgoing instance that is used only by the
     * Decision Node to verify which outgoing flow is appropriate to be taken based on a condition
     * evaluation of a flow context object attribute.
     *
     * The flow context object is just a regular Javascript object that will be passed on to each
     * node in the diagram so each part of the flow can make use of previous processed information.
     *
     * @param {DecisionNode} fromNode The attribute that must exist in the flow context object
     * @param {string} attribute The attribute that must exist in the flow context object
     * @param {CONDITION_TYPE} condition The condition type to verify if this is the outgoing flow
     * to be taken by the workflow
     * @param value The value to be compared against the flow context attribute
     * @constructor
     */
    var ContextOutgoing = function(fromNode, attribute, condition, value) {
        if (!fromNode || (!fromNode instanceof AbstractNode) || fromNode.type !== NODE_TYPE.DECISION) throw 'A "from" Abstract Node subclass object with type DECISION must be provided';
        if (!attribute) throw 'A defined value for attribute parameter must be provided';
        if (!condition) throw 'A defined value for condition parameter must be provided';

        if (!CONDITION_TYPE[condition]) throw 'Invalid condition. Check for CONDITION_TYPE possible values';

        var _fromNode = fromNode;
        var _attribute = attribute;
        var _condition = condition;
        var _value = value;

        var _target;
        Object.defineProperty(this, 'target', {
            get: function() {
                return _target;
            },
            set: function(val) {
                isValidNode(val);

                if (!isValidNode(val) ||
                    !isNodeTypeOf(val, [
                        NODE_TYPE.ACTION,
                        NODE_TYPE.DECISION,
                        NODE_TYPE.FORK,
                        NODE_TYPE.FINAL])) {
                    throw 'Context Outgoings should redirect to an Action, Decision, Fork or Final Node';

                } else if (val instanceof DecisionNode && val.id === _fromNode.id) {
                    throw 'Decision Node should not redirect to itself';
                }

                _target = val;
            },
            enumerable: true,
            configurable: false
        });

        /**
         * Checks if the current instance of the Context Output satisfies the condition. It must
         * be used by the engine to drive the workflow to thenext node.
         *
         * @param {object} context The flow context object is just a regular Javascript object that will
         * be passed on to each node in the diagram so each part of the flow can make use of previous
         * processed information.
         * @returns {boolean}
         */
        this.isConditionSatisfied = function(context) {
            if (!context) throw 'Cannot evaluate a condition without a flow context object';
            if (!context[_attribute] && context[_attribute] !== 0 && typeof context[_attribute] !== 'boolean') throw 'No attribute match between this Context Outgoing and the flow context object';

            var ctx = context[_attribute].toString().toLowerCase();
            var val = !_value && _value !== 0 && typeof _value !== 'boolean' ? _value : _value.toString().toLowerCase();

            switch(_condition) {
                case CONDITION_TYPE.EQ:
                    var result = ctx == val;
                    break;
                case CONDITION_TYPE.NEQ:
                    var result = ctx != val;
                    break;
                case CONDITION_TYPE.GT:
                    var result = Number(ctx) > Number(val);
                    break;
                case CONDITION_TYPE.GEQT:
                    var result = Number(ctx) >= Number(val);
                    break;
                case CONDITION_TYPE.LT:
                    var result = Number(ctx) < Number(val);
                    break;
                case CONDITION_TYPE.LEQT:
                    var result = Number(ctx) <= Number(val);
                    break;
                case CONDITION_TYPE.IN:
                    var result = ctx.indexOf(val) > -1;
                    break;
                case CONDITION_TYPE.ENDS:
                    var result = ctx.indexOf(val, ctx.length - val.length) !== -1;
                    break;
                case CONDITION_TYPE.STARTS:
                    var result = ctx.indexOf(val) === 0;
                    break;
            }

            return result;
        }
    };

    /**
     * Will take the decision of which will be the next step of the workflow. It will check a
     * previously set attribute value from the flow context object that is passed along through
     * the entire flow. According to the attribute value it will redirect the flow to one of its
     * outgoing control flows.
     *
     * Basic Rules: (1) Many as possible flows coming into; (2) Two or more flows going out.
     * Actually, there must be at least one flow to match to the context attribute and one
     * flow otherwise. There must always be an outgoing otherwise flow; (3) Its outgoing flow
     * may target an Action Node, a Final Node, another Decision Node or a Fork Node. (4) Its
     * outgoing flow cannot target itself.

     * @param {string} id The node identification
     * @constructor
     */
    var DecisionNode = function(id) {
        AbstractNode.call(this, id, NODE_TYPE.DECISION);

        var _outgoings;
        Object.defineProperty(this, 'outgoings', {
            get: function() {
                return _outgoings;
            },
            set: function(val) {
                if (!val || !(val instanceof Array) || val.length === 0) {
                    throw 'A Decision Node should redirect to at least one outgoing flow besides the default one (a.k.a otherwise flow)';
                }

                for (var idx in val) {
                    if (!(val[idx] instanceof ContextOutgoing) && !val[idx].target) {
                        throw 'A Decision Node\'s outgoing must be of Context Outgoing type with a valid node as target';
                    }
                }

                _outgoings = val;
            },
            enumerable: true,
            configurable: false
        });

        var _otherwise;
        Object.defineProperty(this, 'otherwise', {
            get: function() {
                return _otherwise;
            },
            set: function(val) {
                if (!isValidNode(val) ||
                    !isNodeTypeOf(val, [
                        NODE_TYPE.ACTION,
                        NODE_TYPE.DECISION,
                        NODE_TYPE.FORK,
                        NODE_TYPE.FINAL])) {
                    throw 'Decision Node default outgoing (otherwise condition) should redirect to an Action, Decision, Fork Node or Final Node';

                } else if (val instanceof DecisionNode && val.id === this.id) {
                    throw 'Decision Node should not redirect to itself';
                }

                _otherwise = val;
            },
            enumerable: true,
            configurable: false
        });
    };

    DecisionNode.prototype = Object.create(AbstractNode.prototype, {
        constructor: {
            configurable: true,
            enumerable: true,
            value: DecisionNode,
            writable: true
        }
    });


    /**
     *
     */
    var Workflow = function(initialNode) {

        if (!(initialNode instanceof InitialNode)) {
            throw 'First argument must be an instance of InitialNode';
        }

        var _initialNode = initialNode;
        Object.defineProperty(this, 'initialNode', {
            get: function() {
                return _initialNode;
            },
            configurable: false,
            enumerable: true
        });

        var _context = { };
        Object.defineProperty(this, 'context', {
            get: function() {
                return _context;
            },
            set: function(val) {
                if (val === undefined || val === null) {
                    val = { };

                } else if (typeof val !== 'object') {
                    throw 'Context must be of type "object"';
                }

                _context = val;
            },
            enumerable: true,
            configurable: false
        });

    };

    /**
     * Externalization
     */
    var result = {
        NODE_TYPE: NODE_TYPE,
        CONDITION_TYPE: CONDITION_TYPE,
        AbstractNode: AbstractNode,
        InitialNode: InitialNode,
        ActionNode: ActionNode,
        ForkNode: ForkNode,
        JoinNode: JoinNode,
        FinalNode: FinalNode,
        ContextOutgoing: ContextOutgoing,
        DecisionNode: DecisionNode,
        Workflow: Workflow
    };

    if (module && module.exports) {
    }
    module.exports = {
        jk4flow: {
            model: result
        }
    };

    return result;
})();