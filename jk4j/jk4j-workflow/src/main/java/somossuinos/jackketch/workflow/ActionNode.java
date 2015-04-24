package somossuinos.jackketch.workflow;

import somossuinos.jackketch.workflow.controlflow.SingleControlFlowNode;
import somossuinos.jackketch.workflow.node.AbstractNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

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
 */
public class ActionNode extends AbstractNode implements SingleControlFlowNode {

    private static final NodeType[] ALLOWED_TYPES = { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL, NodeType.JOIN };

    private Node flow;

    ActionNode(final String id) { super(id); }

    @Override
    public NodeType getType() {
        return NodeType.ACTION;
    }

    @Override
    public void setFlow(final Node flow) {
        this.flow = ControlFlowFactory.create(this, flow, ALLOWED_TYPES);
    }

    @Override
    public Node getFlow() {
        return this.flow;
    }
}
