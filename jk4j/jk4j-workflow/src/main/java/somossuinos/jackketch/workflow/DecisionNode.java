package somossuinos.jackketch.workflow;

import java.util.HashMap;
import java.util.Map;
import somossuinos.jackketch.workflow.controlflow.ConditionalControlFlow;
import somossuinos.jackketch.workflow.controlflow.FlowCondition;
import somossuinos.jackketch.workflow.node.AbstractNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

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
 */
public class DecisionNode extends AbstractNode implements ConditionalControlFlow {

    private static final NodeType[] ALLOWED_TYPES = { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL };

    private static final int MIN_OUTGOINGS_ALLOWED = 1;

    private Map<FlowCondition, Node> flows = new HashMap<>(0);
    private Node otherwise;

    DecisionNode(String id) {
        super(id);
    }

    @Override
    public Node getFlow(final String attribute, final String value) {
        for (final FlowCondition condition : this.flows.keySet()) {
            if (condition.isValid(attribute, value)) {
                return this.flows.get(condition);
            }
        }

        return otherwise;
    }

    @Override
    public void setFlows(final Map<FlowCondition, Node> flows, final Node otherwise) {
        this.flows = ControlFlowFactory.create(this, flows, ALLOWED_TYPES, 1);
        this.otherwise = ControlFlowFactory.create(this, otherwise, ALLOWED_TYPES);
    }

    @Override
    public NodeType getType() {
        return NodeType.DECISION;
    }
}
