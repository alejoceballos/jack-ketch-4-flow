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

package somossuinos.jackketch.workflow.conditional;

/**
 * All condition types currently accepted by JK4Flow engine.
 *
 * <p>
 *     <b>Description:</b>
 *     <ul>
 *         <li><b>EQ:</b> Equals (any object comparison);</li>
 *         <li><b>NEQ:</b> Not equals (any object comparison);</li>
 *         <li><b>GT:</b> Greater than (just numbers);</li>
 *         <li><b>GEQT:</b> Greater or equals than (just numbers);</li>
 *         <li><b>LT:</b> Lesser than (just numbers);</li>
 *         <li><b>LEQT:</b> Lesser or equals than (just numbers);</li>
 *         <li><b>IN:</b> String inside another (strings only);</li>
 *         <li><b>ENDS:</b> String ends with (strings only);</li>
 *         <li><b>STARTS:</b> String starts with (strings only);</li>
 *     </ul>
 * </p>
 */
public enum ConditionType {

    /**
     * Equals (any object comparison)
     */
    EQ,

    /**
     * Not equals (any object comparison)
     */
    NEQ,

    /**
     * Greater than (just numbers)
     */
    GT,

    /**
     * Greater or equals than (just numbers)
     */
    GEQT,

    /**
     * Lesser than (just numbers)
     */
    LT,

    /**
     * Lesser or equals than (just numbers)
     */
    LEQT,

    /**
     * String inside another (strings only)
     */
    IN,

    /**
     * String ends with (strings only)
     */
    ENDS,

    /**
     * String starts with (strings only)
     */
    STARTS
}
