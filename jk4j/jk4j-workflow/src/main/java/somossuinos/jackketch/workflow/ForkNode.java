package somossuinos.jackketch.workflow;

import java.util.HashSet;
import java.util.Set;
import somossuinos.jackketch.workflow.controlflow.MultipleControlFlow;
import somossuinos.jackketch.workflow.node.AbstractNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

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
 */
public class ForkNode extends AbstractNode implements MultipleControlFlow {

    private static final NodeType[] ALLOWED_TYPES = { NodeType.ACTION, NodeType.DECISION };

    private static final int MIN_OUTGOINGS_ALLOWED = 2;

    private Set<Node> flows = new HashSet<>(0);

    ForkNode(String id) {
        super(id);
    }

    /**
     * Will always return a copy of the inner list so the control flows are kept
     * the same regardless external changes to the returned list.
     *
     * @return A list with all outgoing flows of this node.
     */
    @Override
    public Set<Node> getFlows() {
        final Set<Node> copy = new HashSet<>(this.flows.size());
        copy.addAll(this.flows);
        return copy;
    }

    @Override
    public void setFlows(final Set<Node> flows) {
        this.flows = ControlFlowFactory.create(this, flows, ALLOWED_TYPES, MIN_OUTGOINGS_ALLOWED);
    }

    @Override
    public NodeType getType() {
        return NodeType.FORK;
    }
}
