package somossuinos.jackketch.transform.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * The meta structure of a workflow representation before its transformation to a
 * executable Workflow object.
 * <p>
 *     Only few validations are performed here (validates final and initial nodes
 *     amounts) since most of workflow's creation rules will be applied when
 *     transforming form meta model to executable model.
 * </p>
 * <p>
 *     Workflow validation takes place three different times:
 *     <ol>
 *         <li>During workflow meta object creation (here);</li>
 *         <li>After workflow meta object creation (checks if the number of fork nodes is the same as the number of join nodes);</li>
 *         <li>During workflow transformation, from meta object to executable object (check documentation for all rules that takes place).</li>
 *     </ol>
 * </p>
 * <b>NOTE:</b> This workflow meta representation is, actually, a java representation
 * of a simple JSon model.
 */
public class MetaWorkflow {

    private List<MetaBaseNode> initialNodes = new ArrayList<>(0);

    public List<MetaBaseNode> getInitialNodes() {
        return initialNodes;
    }

    public void setInitialNodes(final List<MetaBaseNode> initialNodes) {
        if (initialNodes == null || initialNodes.size() == 0) throw new RuntimeException("Workflow must have one initial node");
        if (initialNodes.size() > 1) throw new RuntimeException("Workflow must have only one initial node");

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

    public void setFinalNodes(final List<MetaBaseNode> finalNodes) {
        if (finalNodes == null || finalNodes.size() == 0) throw new RuntimeException("Workflow must have one final node");
        if (finalNodes.size() > 1) throw new RuntimeException("Workflow must have only one final node");

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
