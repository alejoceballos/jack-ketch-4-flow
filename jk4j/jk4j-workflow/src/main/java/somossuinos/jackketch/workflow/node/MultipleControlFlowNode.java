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

import java.util.HashSet;
import java.util.Set;

/**
 * The main abstraction for nodes that forwards to more than one outgoing nodes
 * at the same time.
 * <p>
 * Right now, only Fork Node fits this description.
 * </p>
 */
public abstract class MultipleControlFlowNode extends ControlFlowNode {

    /**
     * The set of outgoing nodes that can be forwarded.
     */
    private Set<Node> flows = new HashSet<>(0);

    /**
     * Constructor. Node id is mandatory and cannot be blank.
     *
     * @param id The node id. It is mandatory and cannot be blank.
     */
    public MultipleControlFlowNode(String id) {
        super(id);
    }

    /**
     * Gets a set with all outgoing nodes of this node.
     * <p>
     * Will always return a copy of the inner list so the control flows are protected
     * from external changes.
     * </p>
     *
     * @return A set of all outgoing flows of this node.
     */
    public Set<Node> getFlows() {
        final Set<Node> copy = new HashSet<>(this.flows.size());
        copy.addAll(this.flows);
        return copy;
    }

    /**
     * Sets the set of outgoing nodes of this node.
     * <p>
     * Will always make a copy of the parameter set so the inner flows are protected
     * from external changes.
     * </p>
     *
     * @param flows A set of all outgoing flows of this node.
     */
    public void setFlows(final Set<Node> flows) {
        this.flows = ControlFlowFactory.create(this, flows, this.getAllowedTypes(), this.getMinFlowsAllowed());
    }

    /**
     * Each type of MultipleControlFlowNode must have a minimum number of allowed
     * outgoing flows. For example, a Fork Node cannot have less than two flows or
     * there would make no sense declaring it.
     *
     * @return The minimum number of allowed outgoing flows.
     */
    public abstract int getMinFlowsAllowed();

}
