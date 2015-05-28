package somossuinos.jackketch.transform.model.json;

import java.util.ArrayList;
import java.util.List;

public class JsonWorkflow {

    private List<JsonBaseNode> initialNodes = new ArrayList<>();

    public List<JsonBaseNode> getInitialNodes() {
        return initialNodes;
    }

    public void setInitialNodes(List<JsonBaseNode> initialNodes) {
        this.initialNodes = initialNodes;
    }

    private List<JsonBaseNode> forkNodes = new ArrayList<>();

    public List<JsonBaseNode> getForkNodes() {
        return forkNodes;
    }

    public void setForkNodes(List<JsonBaseNode> forkNodes) {
        this.forkNodes = forkNodes;
    }

    private List<JsonActionNode> actionNodes = new ArrayList<>();

    public List<JsonActionNode> getActionNodes() {
        return actionNodes;
    }

    public void setActionNodes(List<JsonActionNode> actionNodes) {
        this.actionNodes = actionNodes;
    }

    private List<JsonBaseNode> joinNodes = new ArrayList<>();

    public List<JsonBaseNode> getJoinNodes() {
        return joinNodes;
    }

    public void setJoinNodes(List<JsonBaseNode> joinNodes) {
        this.joinNodes = joinNodes;
    }
}
