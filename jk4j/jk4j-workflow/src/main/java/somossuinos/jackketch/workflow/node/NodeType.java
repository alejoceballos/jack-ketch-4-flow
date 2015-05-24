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
 * All types of nodes accepted in jk4flow diagrams.
 * <ul>
 *     <li><b>INITIAL:</b> It is only a kickstart point to let the engine know where to begin processing;</li>
 *     <li><b>ACTION:</b> Where the magic happens! Each action node corresponds to a programming unit responsible for some real processing of the workflow;</li>
 *     <li><b>DECISION:</b> Will take the decision of which will be the next step of the workflow;</li>
 *     <li><b>FORK:</b> Starts more than one asynchronous processes for each node in the outgoing flow;</li>
 *     <li><b>JOIN:</b> Responsible for gathering all asynchronous processes started by a Fork Node;</li>
 *     <li><b>FINAL:</b> Establishes the end of the flow;</li>
 * </ul>
 * <p>
 * <i>Hope that are more to come if this framework starts to be widely used</i>
 * </p>
 */
public enum NodeType {
    /**
     * It is only a kickstart point to let the engine know where to begin processing
     */
    INITIAL,

    /**
     * Where the magic happens! Each action node corresponds to a programming unit
     * responsible for some real processing of the workflow
     */
    ACTION,

    /**
     * Will take the decision of which will be the next step of the workflow.
     */
    DECISION,

    /**
     * Starts more than one asynchronous processes for each node in the outgoing flow.
     */
    FORK,

    /**
     * Responsible for gathering all asynchronous processes started by a Fork Node.
     */
    JOIN,

    /**
     * Establishes the end of the flow.
     */
    FINAL
}
