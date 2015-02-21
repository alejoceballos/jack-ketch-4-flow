(function() {
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

    /**
     * Abstract Node is the template class for all nodes in the Activity diagram
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
     * It is only a kickstart point to let the engine know where to begin processing.
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
                if (!val || !val.type ||
                    (val.type !== NODE_TYPE.ACTION &&
                     val.type !== NODE_TYPE.DECISION &&
                     val.type !== NODE_TYPE.FORK)) {
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
     * Fork Node.
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
                if (!val || !val.type ||
                    (val.type !== NODE_TYPE.ACTION &&
                     val.type !== NODE_TYPE.DECISION &&
                     val.type !== NODE_TYPE.FORK &&
                     val.type !== NODE_TYPE.FINAL &&
                     val.type !== NODE_TYPE.JOIN)) {

                    throw 'Action Node should redirect to an Action, Decision, Fork Node, Join or Final Node';

                } else if (val.type === NODE_TYPE.ACTION && val.id === this.id) {
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

                for (var idx in val) {
                    if (val[idx].type !== NODE_TYPE.ACTION &&
                        val[idx].type !== NODE_TYPE.DECISION) {
                        throw 'Fork Node should redirect to an Action or Decision Node';
                    }
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
                if (!val || !val.type ||
                    (val.type !== NODE_TYPE.ACTION &&
                    val.type !== NODE_TYPE.DECISION &&
                    val.type !== NODE_TYPE.FORK &&
                    val.type !== NODE_TYPE.FINAL)) {

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

    var CONDITION_TYPE = {
        EQ: 'equals',
        NEQ: 'not equals',
        GT: 'greater than',
        GEQT: 'greater or equals than',
        LT: 'lower than',
        LEQT: 'lower or equals than',
        IN: 'in',
        ENDS: 'ends with',
        STARTS: 'starts with'
    };

    /**
     * The Context Outgoing object is a special outgoing instance that is used only by the
     * Decision Node to verify which outgoing flow is appropriate to be taken based on a condition
     * evaluation of a flow context object attribute.
     *
     * The flow context object is just a regular Javascript object that will be passed on to each
     * node in the diagram so each part of the flow can make use of previous processed information.
     *
     * @param {string} attribute The attribute that must exist in the flow context object
     * @param {CONDITION_TYPE} condition The condition type to verify if this is the outgoing flow
     * to be taken by the workflow
     * @param {string} value The value to be compared against the flow context attribute
     * @constructor
     */
    var ContextOutgoing = function(attribute, condition, value) {
        if (!attribute) throw '';
        if (!condition) throw '';

        if (!CONDITION_TYPE[condition]) throw '';

        var _attribute = attribute;
        var _condition = condition;
        var _value = value;

        var _target;
        Object.defineProperty(this, 'target', {
            get: function() {
                return _target;
            },
            set: function(val) {
                _target = val;
            },
            enumerable: true,
            configurable: false
        });

        /**
         *
         * @param {object} context The flow context object is just a regular Javascript object that will
         * be passed on to each node in the diagram so each part of the flow can make use of previous
         * processed information.
         * @returns {boolean}
         */
        this.isConditionSatisfied = function(context) {
            if (!context) throw '';
            if (!context[_attribute]) throw '';

            var ctx = context[_attribute].toLowerCase() || context[_attribute];
            var val = _value.toLowerCase() || _value;

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
     * may target an Action Node, a Final Node, another Decision Node or a Fork Node.

     * @param {string} id The node identification
     * @constructor
     */
    var DecisionNode = function(id) {
        function validateOutgoing(val) {
            if (!val || !val.type ||
                (val.type !== NODE_TYPE.ACTION &&
                val.type !== NODE_TYPE.DECISION &&
                val.type !== NODE_TYPE.FORK &&
                val.type !== NODE_TYPE.FINAL)) {

                throw 'Decision Node should redirect to an Action, Decision, Fork Node or Final Node. Context and default outgoing flows (a.k.a. otherwise flow) must follow this rule';
            }
        }

        AbstractNode.call(this, id, NODE_TYPE.DECISION);

        var _outgoings;
        Object.defineProperty(this, 'outgoings', {
            get: function() {
                return _outgoings;
            },
            set: function(val) {
                if (!val || !(val instanceof Array) || val.length > 0) {
                    throw 'A Decision Node should redirect to at least one outgoing flow besides the default one (a.k.a otherwise flow)';
                }

                for (var idx in val) {
                    validateOutgoing(val[idx]);
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
                validateOutgoing(val);
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
     * Externalization
     */
    var result = {
        org: {
            somossuinos: {
                jk4flow: {
                    model: {
                        NODE_TYPE: NODE_TYPE,
                        CONDITION_TYPE: CONDITION_TYPE,
                        InitialNode: InitialNode,
                        ActionNode: ActionNode,
                        ForkNode: ForkNode,
                        JoinNode: JoinNode,
                        FinalNode: FinalNode,
                        ContextOutgoing: ContextOutgoing,
                        DecisionNode: DecisionNode
                    }
                }
            }
        }
    };

    if (module && module.exports) {
        module.exports = result;
    }

    return result;
})();