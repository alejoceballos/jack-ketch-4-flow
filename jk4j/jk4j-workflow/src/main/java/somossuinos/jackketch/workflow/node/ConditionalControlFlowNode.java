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

/**
 * The main abstraction for nodes that can forward to more than one outgoing node,
 * but not at the same time, and always depending of a condition to be met.
 * <p>
 * Right now, only Decision Node fits this description.
 * </p>
 * <b>NOTE:</b> This class highly depends on {@link WorkflowContext} and {@link FlowCondition}
 * classes.
 *
 * @see WorkflowContext
 * @see FlowCondition
 */
public abstract class ConditionalControlFlowNode extends ControlFlowNode {

    /**
     * The set of outgoing nodes, all attached to its condition.
     */
    private Map<FlowCondition, Node> flows = new HashMap<>(0);

    /**
     * The default outgoing flow if no condition is met.
     */
    private Node otherwise;

    /**
     * Constructor. Node id is mandatory and cannot be blank.
     *
     * @param id The node id. It is mandatory and cannot be blank.
     */
    public ConditionalControlFlowNode(final String id) {
        super(id);
    }

    /**
     * Gets the outgoing flow according to the {@link WorkflowContext} attributes values.
     * <p>
     *     If any double of attribute/value meets the conditions in the outgoing flows, the
     *     related node is returned, otherwise, the default node is returned.
     * </p>
     *
     * @param context The {@link WorkflowContext} object of the current workflow execution.
     * @return A node satisfying the condition, the default node otherwise.
     */
    public Node getFlow(final WorkflowContext context) {
        for (final FlowCondition condition : this.flows.keySet()) {
            final Object obj = context.get(condition.getAttribute());

            if (obj != null && condition.isValid(obj.toString())) {
                return this.flows.get(condition);
            }
        }

        return otherwise;
    }

    /**
     * Sets all the possible outgoing flows.
     *
     * @param targets A map of {@link FlowCondition} and its corresponding outgoing nodes.
     * @param otherwise The default outgoing node if no condition is met.
     */
    public void setFlows(final Map<FlowCondition, Node> targets, final Node otherwise) {
        ControlFlowValidator.validate(this, targets, this.getAllowedTypes(), this.getMinFlowsAllowed());

        this.flows = new HashMap<>(targets.size());
        this.flows.putAll(targets);

        ControlFlowValidator.validate(this, otherwise, this.getAllowedTypes());
        this.otherwise = otherwise;
    }

    /**
     * Each type of ConditionalControlFlowNode must have a minimum number of allowed
     * outgoing flows (besides the default one). For example, a Decision Node cannot
     * have less than one flow or there would make no sense declaring it.
     *
     * @return The minimum number of allowed outgoing flows (besides the default one).
     */
    public abstract int getMinFlowsAllowed();


}
