package somossuinos.jackketch.transform.meta;

public class MetaFlowCondition {

    private String attribute;
    private String  condition;
    private String value;
    private String controlFlow;

    public MetaFlowCondition() {
    }

    public MetaFlowCondition(String attribute, String condition, String value, String controlFlow) {
        this.attribute = attribute;
        this.condition = condition;
        this.value = value;
        this.controlFlow = controlFlow;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getControlFlow() {
        return controlFlow;
    }

    public void setControlFlow(String controlFlow) {
        this.controlFlow = controlFlow;
    }
}
