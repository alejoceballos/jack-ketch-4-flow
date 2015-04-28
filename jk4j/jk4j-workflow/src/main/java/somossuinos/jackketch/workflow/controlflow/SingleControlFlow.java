package somossuinos.jackketch.workflow.controlflow;

import somossuinos.jackketch.workflow.node.Node;

public interface SingleControlFlow {

    Node getFlow();
    void setFlow(final Node node);

}
