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

package somossuinos.jackketch.workflow.executable;

import java.lang.reflect.Method;
import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

/**
 * Where the magic happens! Each action node corresponds to a programming
 * unit responsible for some real processing of the workflow. It is done by associating one
 * callback (function/method) to it. Any callback will always receive one flow context object
 * as first argument, this object wraps all data that mst be passed by through the flow
 * execution. If the action node needs to pass along some information, just put it into the
 * flow context object, no need to return anything.
 * <p>
 * <i>Basic Rules: (1) Many as possible flows coming into; (2) Only one flow going out; (3) Its
 * outgoing flow may target another Action Node, a Final Node, a Decision Node, a Fork Node
 * and even a Join Node, but only if it is part of an asynchronous flow started by a previous
 * Fork Node. (4) Its outgoing flow cannot target itself.</i>
 * </p>
 */
public class ActionNode extends SingleControlFlowNode implements ContextExecutable {

    public Object object;
    public Method method;

    public ActionNode(final String id) {
        super(id);
    }

    @Override
    public void setObject(final Object object) {
        this.object = object;
    }

    @Override
    public void setMethod(final Method method) {
        if (this.method == null) {
            throw new RuntimeException("Method cannot be null");
        }

        if (method.getParameterTypes().length == 0 || !method.getParameterTypes()[0].isAssignableFrom(WorkflowContext.class)) {
            throw new RuntimeException(String.format("The first parameter of method must be of % type", WorkflowContext.class.getCanonicalName()));
        }

        this.method = method;
    }

    @Override
    public NodeType getType() {
        return NodeType.ACTION;
    }

    @Override
    public NodeType[] getAllowedTypes() {
        return new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL, NodeType.JOIN };
    }

    @Override
    public Object execute(final WorkflowContext context) {
        if (this.method == null) {
            throw new RuntimeException("Cannot execute an operation without a method");
        }

        try {
            return this.method.invoke(this.object, context);

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Problem invoking context executable method", e);
        }
    }

}
