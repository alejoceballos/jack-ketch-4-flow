package somossuinos.jackketch.workflow.controlflow;

import org.apache.commons.lang3.StringUtils;

public class FlowCondition {

    private String attribute;
    private ConditionType conditionType;
    private String value;

    public FlowCondition(final String attribute, final ConditionType conditionType, final String value) {
        if (StringUtils.isBlank(attribute)) {
            throw new RuntimeException("\"attribute\" must not be empty");
        }

        this.attribute = attribute;

        if (conditionType == null) {
            throw new RuntimeException("conditionType cannot be null");
        }

        this.conditionType = conditionType;

        if (StringUtils.isBlank(value)) {
            throw new RuntimeException("\"value\" must not be empty");
        }

        this.value = value;
    }

    /**
     * Checks if the current instance of the Context Output satisfies the condition. It must
     * be used by the engine to drive the workflow to the next node.
     */
    public boolean isValid(final String attribute, final String value) {
        if (StringUtils.isBlank(attribute)) {
            return false;
        }

        String ctxValue = value;
        if (ctxValue != null) {
            ctxValue = ctxValue.trim().toLowerCase();
        }

        String thisValue = this.value;
        if (thisValue != null) {
            thisValue = this.value.trim().toLowerCase();
        }

        boolean result = false;

        switch(this.conditionType) {
            case EQ:
                result = ctxValue == thisValue;
                break;
            case NEQ:
                result = ctxValue != thisValue;
                break;
            case GT:
                result = Double.parseDouble(ctxValue) > Double.parseDouble(thisValue);
                break;
            case GEQT:
                result = Double.parseDouble(ctxValue) >= Double.parseDouble(thisValue);
                break;
            case LT:
                result = Double.parseDouble(ctxValue) < Double.parseDouble(thisValue);
                break;
            case LEQT:
                result = Double.parseDouble(ctxValue) <= Double.parseDouble(thisValue);
                break;
            case IN:
                result = thisValue.indexOf(ctxValue) > -1;
                break;
            case ENDS:
                result = thisValue.indexOf(ctxValue, ctxValue.length() - thisValue.length()) != -1;
                break;
            case STARTS:
                result = thisValue.indexOf(ctxValue) == 0;
                break;
        }

        return result;
    }
}
