package somossuinos.jackketch.transform.meta;

import java.util.List;

public class MetaDecisionNode extends MetaBaseNode {

    private List<MetaFlowCondition> flowConditions;
    private MetaOtherwiseFlow otherwise;

    public MetaDecisionNode() {
    }

    public MetaDecisionNode(String id, List<MetaFlowCondition> flowConditions, MetaOtherwiseFlow otherwise) {
        super(id);
        this.flowConditions = flowConditions;
        this.otherwise = otherwise;
    }

    public List<MetaFlowCondition> getFlowConditions() {
        return flowConditions;
    }

    public void setFlowConditions(List<MetaFlowCondition> flowConditions) {
        this.flowConditions = flowConditions;
    }

    public MetaOtherwiseFlow getOtherwise() {
        return otherwise;
    }

    public void setOtherwise(MetaOtherwiseFlow otherwise) {
        this.otherwise = otherwise;
    }
}
