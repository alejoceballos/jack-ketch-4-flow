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

/**
 * Any node that intends to execute a workflow task must implement this
 * interface.
 * <p>
 *     "Executable nodes", like Action Nodes, are bound to java objects holding
 *     at least one method that can receive a {@link WorkflowContext} and execute it.
 * </p>
 * <p>
 *     Since Java works with dynamic method execution using <i>Reflection</i>, a simple
 *     {@link Method} must be defined to executed along its object's instance.
 * </p>
 */
public interface ContextExecutable {

    /**
     * Sets the instance that holds the method to be executed.
     *
     * @param object The object that holds the method to be executed.
     */
    void setObject(final Object object);

    /**
     * The method to be executed. This method object must be extracted using reflection.
     *
     * @param method The method to be executed.
     */
    void setMethod(final Method method);

    /**
     * The execution. Actually will call {@link Method#invoke(Object, Object...)} passing
     * {@link WorkflowContext} as parameter.
     *
     * @param context The {@link WorkflowContext} object of the current workflow execution.
     * @return The same result as declared by the method. Null if the method declares void.
     */
    Object execute(final WorkflowContext context);

}
