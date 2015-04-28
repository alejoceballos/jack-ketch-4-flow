package somossuinos.jackketch.workflow.controlflow;

import java.util.Set;
import somossuinos.jackketch.workflow.node.Node;

public interface MultipleControlFlow {

    Set<Node> getFlows();
    void setFlows(final Set<Node> flows);

}
