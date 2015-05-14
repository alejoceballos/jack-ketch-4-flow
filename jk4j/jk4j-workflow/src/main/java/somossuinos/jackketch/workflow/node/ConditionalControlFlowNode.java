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

package somossuinos.jackketch.workflow.node;

import java.util.HashMap;
import java.util.Map;
import somossuinos.jackketch.workflow.conditional.FlowCondition;
import somossuinos.jackketch.workflow.context.WorkflowContext;

public abstract class ConditionalControlFlowNode extends ControlFlowNode {

    private Map<FlowCondition, Node> flows = new HashMap<>(0);

    private Node otherwise;

    public ConditionalControlFlowNode(final String id) {
        super(id);
    }

    public Node getFlow(final WorkflowContext context) {
        for (final FlowCondition condition : this.flows.keySet()) {
            final Object obj = context.get(condition.getAttribute());

            if (obj != null && condition.isValid(obj.toString())) {
                return this.flows.get(condition);
            }
        }

        return otherwise;
    }

    public void setFlows(final Map<FlowCondition, Node> flows, final Node otherwise) {
        this.flows = ControlFlowFactory.create(this, flows, this.getAllowedTypes(), this.getMinFlowsAllowed());
        this.otherwise = ControlFlowFactory.create(this, otherwise, this.getAllowedTypes());
    }

    public abstract int getMinFlowsAllowed();


}
