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

/**
 * The basic abstraction for all nodes that redirects to at least
 * one flow (this excludes Final Node).
 */
public abstract class ControlFlowNode extends Node {

    /**
     * Constructor. Node id is mandatory and cannot be blank.
     *
     * @param id The node id. It is mandatory and cannot be blank.
     */
    public ControlFlowNode(String id) {
        super(id);
    }

    /**
     * Returns all types that are allowed to be outgoing flow from this node.
     * <p>
     *     Works as a filter that strictly defines the relationship of two node. It
     *     means that defines which node type can be forwarded from another node
     *     type.
     * </p>
     * <p>
     *     e.g. A Fork Node can only forward to an Action Node or a Decision Node.
     * </p>
     *
     * @return An array of node types allowed as outgoing flow for the implementing subclass.
     */
    public abstract NodeType[] getAllowedTypes();

}
