package somossuinos.jackketch.transform.model.meta;

import java.util.ArrayList;
import java.util.List;

public class MetaWorkflow {

    private List<MetaBaseNode> initialNodes = new ArrayList<>(0);

    public List<MetaBaseNode> getInitialNodes() {
        return initialNodes;
    }

    public void setInitialNodes(List<MetaBaseNode> initialNodes) {
        this.initialNodes = initialNodes;
    }

    private List<MetaBaseNode> forkNodes = new ArrayList<>(0);

    public List<MetaBaseNode> getForkNodes() {
        return forkNodes;
    }

    public void setForkNodes(List<MetaBaseNode> forkNodes) {
        this.forkNodes = forkNodes;
    }

    private List<MetaActionNode> actionNodes = new ArrayList<>(0);

    public List<MetaActionNode> getActionNodes() {
        return actionNodes;
    }

    public void setActionNodes(List<MetaActionNode> actionNodes) {
        this.actionNodes = actionNodes;
    }

    private List<MetaBaseNode> joinNodes = new ArrayList<>(0);

    public List<MetaBaseNode> getJoinNodes() {
        return joinNodes;
    }

    public void setJoinNodes(List<MetaBaseNode> joinNodes) {
        this.joinNodes = joinNodes;
    }

    private List<MetaDecisionNode> decisionNodes = new ArrayList<>(0);

    public List<MetaDecisionNode> getDecisionNodes() {
        return decisionNodes;
    }

    public void setDecisionNodes(List<MetaDecisionNode> decisionNodes) {
        this.decisionNodes = decisionNodes;
    }

    private List<MetaBaseNode> finalNodes = new ArrayList<>(0);

    public List<MetaBaseNode> getFinalNodes() {
        return finalNodes;
    }

    public void setFinalNodes(List<MetaBaseNode> finalNodes) {
        this.finalNodes = finalNodes;
    }

    public List<MetaControlFlow> controlFlows = new ArrayList<>(0);

    public List<MetaControlFlow> getControlFlows() {
        return controlFlows;
    }

    public void setControlFlows(List<MetaControlFlow> controlFlows) {
        this.controlFlows = controlFlows;
    }
}
