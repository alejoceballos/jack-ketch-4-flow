/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Alejo Ceballos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package somossuinos.jackketch.workflow;

import somossuinos.jackketch.workflow.exception.Jk4flowWorkflowException;
import somossuinos.jackketch.workflow.executable.ActionNode;
import somossuinos.jackketch.workflow.node.ConditionalControlFlowNode;
import somossuinos.jackketch.workflow.node.MultipleControlFlowNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

/**
 * <b>Initial Node:</b> It is only a kick start point to let the engine know where to begin
 * processing.
 * <p>
 * <i>Basic Rules: (1) No flow coming into; (2) Only one flow going out; (3) Its outgoing flow
 * must target an Action Node, a Decision Node or a Fork Node.</i>
 * </p>
 *
 * <b>Action Node:</b> Where the magic happens! Each action node corresponds to a programming
 * unit responsible for some real processing of the workflow. It is done by associating one
 * callback (function/method) to it. Any callback will always receive one flow context object
 * as first argument, this object wraps all data that mst be passed by through the flow
 * execution. If the action node needs to pass along some information, just put it into the
 * flow context object, no need to return anything.
 * <p>
 * <i>Basic Rules: (1) Many as possible flows coming into; (2) Only one flow going out; (3) Its
 * outgoing flow may target another Action Node, a Final Node, a Decision Node, a Fork Node
 * and even a Join Node, but only if it is part of an asynchronous flow started by a previous
 * Fork Node. (4) Its outgoing flow cannot target itself.</i>
 * </p>
 *
 * <b>Fork Node:</b> Starts an asynchronous process. All flows going out a Fork Node will be
 * treated asynchronously until they find a Join Node, where processing becomes synchronous
 * again.
 * <p>
 * Be aware that starting many asynchronous flows may be hard to manage, it also may happen
 * if an asynchronous flow drives back to some node in the flow that was previously synchronous.
 * Pay attention when diagramming complex workflows.
 * </p>
 * <p>
 * <i>Basic Rules: (1) Many as possible flows coming into; (2) Two or more flows going out.
 * (3) Its outgoing flow may target an Action Node or a Decision Node. Do not terminate an
 * asynchronous process without joining it again, please.</i>
 * </p>
 *
 * <b>Join Node:</b> Responsible for gathering all asynchronous processes started by a Fork
 * Node.
 * <p>
 * <i>Basic Rules: (1) Many as possible flows coming into; (2) Only one flow going out;
 * (3) Its outgoing flow may target an Action Node, Decision Node, a Final Node or
 * a Fork Node. Think about it! I could only start a set of asynchronous processes
 * to speed up data gathering. After having all data needed, start another to speed up
 * its use.</i>
 * </p>
 *
 * <b>Decision Node:</b> Will take the decision of which will be the next step of the
 * workflow. It will check a previously set attribute value from the flow context object
 * that is passed along through the entire flow. According to the attribute value it will
 * redirect the flow to one of its outgoing control flows.
 * <p>
 * <i>Basic Rules: (1) Many as possible flows coming into; (2) Two or more flows going out.
 * Actually, there must be at least one flow to match to the context attribute and one
 * flow otherwise. There must always be an outgoing otherwise flow; (3) Its outgoing flow
 * may target an Action Node, a Final Node, another Decision Node or a Fork Node. (4) Its
 * outgoing flow cannot target itself.</i>
 * </p>
 *
 * <b>Final Node:</b> Establishes the end of the flow. At first I thought this node wasn't
 * really necessary, for example, if I just reach a last Action Node (without outgoing control
 * flow) the flow should be terminated too. But the Final Node, besides establishing a formal
 * end to our workflow, should allow the return of the context flow object to the programming
 * structure that started it. So I decided to make it mandatory, and to simplify (my life, of
 * course) I also insist that it shall be unique (there can be only one!).
 * <p>
 * <i>Basic Rules: (1) Many as possible flows coming into; (2) No flow going out.</i>
 * </p>
 *
 * @see SingleControlFlowNode
 * @see MultipleControlFlowNode
 * @see ConditionalControlFlowNode
 * @see ActionNode
 */
public class NodeFactory {

    /**
     * Private constructor to ensure that this class will not be instantiate.
     */
    private NodeFactory() {
    }

    /**
     * Factory method that creates all types of nodes.
     * <p>
     * The real node objects created may be of the following  types:
     * <ul>
     *     <li>{@link SingleControlFlowNode}</li>
     *     <li>{@link MultipleControlFlowNode}</li>
     *     <li>{@link ConditionalControlFlowNode}</li>
     *     <li>{@link ActionNode}</li>
     * </ul>
     * </p>
     *
     * @param id The node id. It is mandatory and cannot be blank.
     * @param type The node type according to {@link NodeType}.
     * @return An instance of {@link Node}. Can be of type
     * {@link SingleControlFlowNode}, {@link MultipleControlFlowNode},
     * {@link ConditionalControlFlowNode} or {@link ActionNode}.
     *
     */
    public static Node createNode(final String id, final NodeType type) {
        if (type == null) {
            throw new Jk4flowWorkflowException("Must have a node type to create a node");
        }

        final Node node;

        switch (type) {
            case INITIAL:
                node = new SingleControlFlowNode(id) {
                    @Override
                    public NodeType getType() {
                        return NodeType.INITIAL;
                    }

                    @Override
                    public NodeType[] getAllowedTypes() {
                        return new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK };
                    }
                };
                break;

            case ACTION:
                node = new ActionNode(id);
                break;

            case JOIN:
                node = new SingleControlFlowNode(id) {
                    @Override
                    public NodeType getType() {
                        return NodeType.JOIN;
                    }

                    @Override
                    public NodeType[] getAllowedTypes() {
                        return new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL };
                    }
                };
                break;

            case FORK:
                node = new MultipleControlFlowNode(id) {
                    @Override
                    public NodeType getType() {
                        return NodeType.FORK;
                    }

                    @Override
                    public int getMinFlowsAllowed() {
                        return 2;
                    }

                    @Override
                    public NodeType[] getAllowedTypes() {
                        return new NodeType[] { NodeType.ACTION, NodeType.DECISION };
                    }
                };
                break;

            case DECISION:
                node = new ConditionalControlFlowNode(id) {
                    @Override
                    public NodeType getType() {
                        return NodeType.DECISION;
                    }

                    @Override
                    public int getMinFlowsAllowed() {
                        return 1;
                    }

                    @Override
                    public NodeType[] getAllowedTypes() {
                        return new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL };
                    }
                };
                break;

            case FINAL:
                node = new Node(id) {
                    @Override
                    public NodeType getType() {
                        return NodeType.FINAL;
                    }
                };
                break;

            default:
                throw new Jk4flowWorkflowException(String.format("% not implemented yet", type.name()));
        }

        return node;
    }
}
