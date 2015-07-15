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

import org.apache.commons.lang3.StringUtils;
import somossuinos.jackketch.workflow.exception.Jk4flowWorkflowException;

/**
 * Abstract Node is the template class for all nodes in the Activity diagram
 */
public abstract class Node {

    /**
     * The id of the node. Usually its value is defined by an external diagramming
     * tool.
     */
    private String id;

    /**
     * Constructor. Node id is mandatory and cannot be blank.
     *
     * @param id The node id. It is mandatory and cannot be blank.
     */
    public Node(final String id) {
        if (StringUtils.isBlank(id)) {
            throw new Jk4flowWorkflowException("\"id\" must not be empty");
        }

        this.id = id.trim();
    }

    /**
     * Default getter for the Node id.
     *
     * @return The node identification.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Default getter for the node type.
     * <p>
     * Some different nodes may implement the same interface and will only be differentiated
     * by its type attribute.
     * </p>
     *
     * @return The node type of this node instance.
     */
    public abstract NodeType getType();
}
