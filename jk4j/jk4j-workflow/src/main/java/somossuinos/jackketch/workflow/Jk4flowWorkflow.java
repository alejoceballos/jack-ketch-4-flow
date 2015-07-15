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

import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.exception.Jk4flowWorkflowException;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

/**
 * The workflow object is responsible for encapsulating all the objects that
 * define the activity diagram to be executed. It also wraps the flow context object.
 * <p>
 * When a brand new workflow object is created it will also create a new empty flow
 * context object. Another flow context object can be assigned to the workflow after
 * it has been created, if none is assigned it will only restarts an empty flow context
 * object.
 * </p>
 */
public class Jk4flowWorkflow {

    /**
     * The {@link Node} that starts the whole workflow process.  Must
     * be of type {@link NodeType#INITIAL}.
     */
    private Node initialNode;

    /**
     * Private constructor. Instead of calling new to this class call its static
     * factory method.
     * <p>
     * The {@link Node} that starts the whole workflow process must
     * be of type {@link NodeType#INITIAL}.
     * </p>
     *
     * @param initialNode A {@link WorkflowContext} object of type {@link NodeType#INITIAL}.
     */
    public Jk4flowWorkflow(final Node initialNode) {
        if (initialNode == null || !NodeType.INITIAL.equals(initialNode.getType())) {
            throw new Jk4flowWorkflowException("Workflow's Initial Node required");
        }

        this.initialNode = initialNode;
    }

    /**
     * Returns the starting point of the workflow.
     *
     * @return A node object of type {@link NodeType#INITIAL}.
     */
    public Node getInitialNode() {
        return initialNode;
    }

}
