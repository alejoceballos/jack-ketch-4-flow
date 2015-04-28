package somossuinos.jackketch.workflow.controlflow;

import java.util.Map;
import somossuinos.jackketch.workflow.node.Node;

public interface ConditionalControlFlow {

    Node getFlow(final String attribute, final String value);
    void setFlows(Map<FlowCondition, Node> flows, Node otherwise);

}
