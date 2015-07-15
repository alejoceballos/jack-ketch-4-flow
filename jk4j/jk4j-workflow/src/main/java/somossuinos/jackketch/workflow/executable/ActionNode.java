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
import somossuinos.jackketch.workflow.exception.Jk4flowWorkflowException;
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

    /**
     * The object that holds the method to be executed.
     */
    private Object object;

    /**
     * The method to be executed. This method object must be extracted using reflection.
     */
    private Method method;

    /**
     * Constructor. Node id is mandatory and cannot be blank.
     *
     * @param id The node id. It is mandatory and cannot be blank.
     */
    public ActionNode(final String id) {
        super(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final Object object) {
        this.object = object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMethod(final Method method) {
        if (method == null) {
            throw new Jk4flowWorkflowException("Method cannot be null");
        }

        if (method.getParameterTypes().length != 0) {
            throw new Jk4flowWorkflowException("The method must not have any parameter");
        }

        this.method = method;
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Obviously, this "getType()" implementation will always return {@link NodeType#ACTION}.
     * </p>
     * @return {@link NodeType#ACTION}.
     */
    @Override
    public NodeType getType() {
        return NodeType.ACTION;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The allowed types for outgoing nodes are: ACTION, DECISION, FORK, FINAL and JOIN.
     * </p>
     */
    @Override
    public NodeType[] getAllowedTypes() {
        return new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL, NodeType.JOIN };
    }

    /**
     * {@inheritDoc}
     * <p>
     *     If the object being executed implements {@link ContextBindableObject}, the action
     *     node will pass it the workflow context object.
     * </p>
     * <p>
     *     The workflow context object cannot be null.
     * </p>
     */
    @Override
    public Object execute(final WorkflowContext context) {
        if (this.method == null) {
            throw new Jk4flowWorkflowException("Cannot execute an operation without a method");
        }

        if (this.object == null) {
            throw new Jk4flowWorkflowException("Cannot execute an operation without an object");
        }

        if (this.object instanceof ContextBindableObject) {
            ((ContextBindableObject) this.object).setContext(context);
        }

        try {
            return this.method.invoke(this.object);

        } catch (ReflectiveOperationException e) {
            throw new Jk4flowWorkflowException("Problem invoking context executable method", e);
        }
    }

}
