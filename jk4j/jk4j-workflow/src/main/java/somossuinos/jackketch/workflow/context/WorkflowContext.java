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

package somossuinos.jackketch.workflow.context;

import java.util.Map;

/**
 * The main contract for those who wants to pass along a set of context values
 * in the workflow execution.
 * <p>
 * Instances that implement this interface will work as a {@link java.util.HashMap}
 * of context attribute values. How it will really be implemented depends on the engine.
 * </p>
 */
public interface WorkflowContext {

    /**
     * Getter for attribute values in the context flow.
     *
     * @param key The attribute name that holds the context value.
     * @return An object in the flow context. You must be aware of the real type of this
     * object to use it accordingly.
     */
    Object get(final String key);

    /**
     * Setter for a context flow's attribute value.
     *
     * @param key The attribute name that will hold the context value.
     * @param value An object to be shared along the workflow execution.
     */
    void set(final String key, final Object value);

    /**
     * Gets all attributes and related objects in a {@link java.util.Map} format,
     * independently of the real implementation.
     *
     * @return A map with an attribute/value set.
     */
    Map<String, Object> getMap();

    /**
     * Clears all attributes and related values from the flow context.
     */
    void clear();

}
