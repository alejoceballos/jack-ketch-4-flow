package somossuinos.jackketch.transform.model.json;

import java.util.ArrayList;
import java.util.List;

public class JsonWorkflow {

    private List<JsonBaseNode> initialNodes = new ArrayList<>(0);

    public List<JsonBaseNode> getInitialNodes() {
        return initialNodes;
    }

    public void setInitialNodes(List<JsonBaseNode> initialNodes) {
        this.initialNodes = initialNodes;
    }

    private List<JsonBaseNode> forkNodes = new ArrayList<>(0);

    public List<JsonBaseNode> getForkNodes() {
        return forkNodes;
    }

    public void setForkNodes(List<JsonBaseNode> forkNodes) {
        this.forkNodes = forkNodes;
    }

    private List<JsonActionNode> actionNodes = new ArrayList<>(0);

    public List<JsonActionNode> getActionNodes() {
        return actionNodes;
    }

    public void setActionNodes(List<JsonActionNode> actionNodes) {
        this.actionNodes = actionNodes;
    }

    private List<JsonBaseNode> joinNodes = new ArrayList<>(0);

    public List<JsonBaseNode> getJoinNodes() {
        return joinNodes;
    }

    public void setJoinNodes(List<JsonBaseNode> joinNodes) {
        this.joinNodes = joinNodes;
    }

    private List<JsonDecisionNode> decisionNodes = new ArrayList<>(0);

    public List<JsonDecisionNode> getDecisionNodes() {
        return decisionNodes;
    }

    public void setDecisionNodes(List<JsonDecisionNode> decisionNodes) {
        this.decisionNodes = decisionNodes;
    }

    private List<JsonBaseNode> finalNodes = new ArrayList<>(0);

    public List<JsonBaseNode> getFinalNodes() {
        return finalNodes;
    }

    public void setFinalNodes(List<JsonBaseNode> finalNodes) {
        this.finalNodes = finalNodes;
    }

    public List<JsonControlFlow> controlFlows = new ArrayList<>(0);

    public List<JsonControlFlow> getControlFlows() {
        return controlFlows;
    }

    public void setControlFlows(List<JsonControlFlow> controlFlows) {
        this.controlFlows = controlFlows;
    }
}
