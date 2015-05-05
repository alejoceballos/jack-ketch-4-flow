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

public abstract class MultipleControlFlowNode extends ControlFlowNode {

    private Set<Node> flows = new HashSet<>(0);

    public MultipleControlFlowNode(String id) {
        super(id);
    }

    /**
     * Will always return a copy of the inner list so the control flows are kept
     * the same regardless external changes to the returned list.
     *
     * @return A list with all outgoing flows of this node.
     */
    public Set<Node> getFlows() {
        final Set<Node> copy = new HashSet<>(this.flows.size());
        copy.addAll(this.flows);
        return copy;
    }

    public void setFlows(final Set<Node> flows) {
        this.flows = ControlFlowFactory.create(this, flows, this.getAllowedTypes(), this.getMinFlowsAllowed());
    }

    public abstract int getMinFlowsAllowed();

}
