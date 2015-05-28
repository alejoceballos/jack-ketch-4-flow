package somossuinos.jackketch.transform.model.json;

import java.util.List;

public class JsonDecisionNode extends JsonBaseNode {

    private List<JsonFlowCondition> flowConditions;
    private JsonOtherwiseFlow otherwise;

    public JsonDecisionNode() {
    }

    public JsonDecisionNode(String id, List<JsonFlowCondition> flowConditions, JsonOtherwiseFlow otherwise) {
        super(id);
        this.flowConditions = flowConditions;
        this.otherwise = otherwise;
    }

    public List<JsonFlowCondition> getFlowConditions() {
        return flowConditions;
    }

    public void setFlowConditions(List<JsonFlowCondition> flowConditions) {
        this.flowConditions = flowConditions;
    }

    public JsonOtherwiseFlow getOtherwise() {
        return otherwise;
    }

    public void setOtherwise(JsonOtherwiseFlow otherwise) {
        this.otherwise = otherwise;
    }
}
