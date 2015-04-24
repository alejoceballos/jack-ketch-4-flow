package somossuinos.jackketch.workflow.controlflow;

import somossuinos.jackketch.workflow.node.Node;

public interface SingleControlFlowNode {

    Node getFlow();
    void setFlow(final Node node);

}
