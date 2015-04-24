package somossuinos.jackketch.workflow;

import somossuinos.jackketch.workflow.controlflow.SingleControlFlowNode;
import somossuinos.jackketch.workflow.node.AbstractNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

/**
 * Responsible for gathering all asynchronous processes started by a Fork Node.
 *
 * Basic Rules: (1) Many as possible flows coming into; (2) Only one flow going out;
 * (3) Its outgoing flow may target an Action Node, Decision Node, a Final Node or
 * a Fork Node. Think about it! I could only start a set of asynchronous processes
 * to speed up data gathering. After having all data needed, start another to speed up
 * its use.
 */
public class JoinNode extends AbstractNode implements SingleControlFlowNode {

    private static final NodeType[] ALLOWED_TYPES = { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL };

    private Node flow;

    public JoinNode(String id) {
        super(id);
    }

    @Override
    public NodeType getType() {
        return NodeType.JOIN;
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
