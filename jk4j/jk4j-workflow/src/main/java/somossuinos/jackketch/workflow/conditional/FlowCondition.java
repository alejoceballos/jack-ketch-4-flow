/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Alejo Ceballos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package somossuinos.jackketch.workflow.conditional;

import org.apache.commons.lang3.StringUtils;

/**
 * Flow condition is used to help deciding what course should a current
 * workflow execution take depending on attributes' values in the flow
 * context.
 */
public class FlowCondition {

    private String attribute;
    private ConditionType conditionType;
    private String value;

    /**
     * Constructor. All parameter values are mandatory.
     *
     * @param attribute The attribute name of the current context flow. Cannot be blank.
     * @param conditionType The type of comparison that will be performed on attribute value.
     * @param value The value expected set to the current attribute when the flow goes through this condition.
     */
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
     * Default getter for the attribute name.
     *
     * @return The attribute name which value will be validated by this flow condition.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Checks if the current instance of the Context Output satisfies the condition. It must
     * be used by the engine to drive the workflow to the next node.
     *
     * @param value The flow context value to be compared against this flow condition value.
     * @return <b>true</b> if the condition is satisfied (meaning that this will be the following
     * flow in the workflow) or <b>false</b> if the condition is not satisfied, so the engine must
     * try another condition flow or accept the "otherwise flow" as the next one.
     */
    public boolean isValid(final String value) {
        String ctxValue = value;
        String thisValue = this.value;

        if (ctxValue == null || thisValue == null) {
            return false;
        }

        ctxValue = ctxValue.trim().toLowerCase();
        thisValue = this.value.trim().toLowerCase();

        boolean result = false;

        switch(this.conditionType) {
            case EQ:
                result = ctxValue.equals(thisValue);
                break;
            case NEQ:
                result = !ctxValue.equals(thisValue);
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
                result = thisValue.contains(ctxValue);
                break;
            case ENDS:
                result = thisValue.indexOf(ctxValue) == thisValue.length() - ctxValue.length();
                break;
            case STARTS:
                result = thisValue.indexOf(ctxValue) == 0;
                break;
        }

        return result;
    }
}
