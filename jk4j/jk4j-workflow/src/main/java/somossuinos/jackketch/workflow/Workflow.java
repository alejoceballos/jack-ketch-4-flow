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

package somossuinos.jackketch.workflow;

import java.util.HashMap;
import java.util.Map;
import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

/**
 *
 */
public class Workflow {

    private Node initialNode;

    private WorkflowContext context = new WorkflowContext() {

        private Map<String, Object> contextMap = new HashMap<>(0);

        @Override
        public Object get(final String key) {
            return contextMap.get(key);
        }

        @Override
        public void set(final String key, final Object value) {
            if (value != null) {
                contextMap.put(key, value);
            }
        }

        @Override
        public Map<String, Object> getMap() {
            final Map<String, Object> clone = new HashMap<>(this.contextMap.size());
            clone.putAll(this.contextMap);

            return clone;
        }

        @Override
        public void clear() {
            contextMap.clear();
        }
    };

    private Workflow(final Node initialNode) {
        this.initialNode = initialNode;
    }

    public static Workflow create(final Node initialNode) {
        if (initialNode == null || !NodeType.INITIAL.equals(initialNode.getType())) {
            throw new RuntimeException("Workflow's Initial Node required");
        }

        return new Workflow(initialNode);
    }

    public WorkflowContext getContext() {
        return context;
    }

    public void setContext(final Map<String, Object> contextMap) {
        this.context.clear();

        for (final String attribute : contextMap.keySet()) {
            if (contextMap.get(attribute) != null) {
                this.context.set(attribute, contextMap.get(attribute));
            }
        }
    }
}
