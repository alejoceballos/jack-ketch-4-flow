package somossuinos.jackketch.workflow;

import somossuinos.jackketch.workflow.controlflow.SingleControlFlow;
import somossuinos.jackketch.workflow.node.AbstractNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

/**
 * It is only a kick start point to let the engine know where to begin processing.
 *
 * Basic Rules: (1) No flow coming into; (2) Only one flow going out; (3) Its outgoing flow
 * must target an Action Node, a Decision Node or a Fork Node.
 */
public class InitialNode extends AbstractNode implements SingleControlFlow {

    private static final NodeType[] ALLOWED_TYPES = { NodeType.ACTION, NodeType.DECISION, NodeType.FORK };

    private Node flow;

    public InitialNode(String id) {
        super(id);
    }

    @Override
    public NodeType getType() {
        return NodeType.INITIAL;
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
