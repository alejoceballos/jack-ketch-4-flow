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

package somossuinos.jackketch.transform.meta;

/**
 * Part of the workflow validation can be done here.
 * <p>
 *     The workflow validation takes place three different times:
 *     <ol>
 *         <li>During workflow meta object creation (validates final and initial nodes amounts);</li>
 *         <li>After workflow meta object creation (this validation here);</li>
 *         <li>During workflow transformation, from meta object to executable object (check documentation for all rules that takes place).</li>
 *     </ol>
 * </p>
 */
public class MetaValidator {

    /**
     * Constructor. Being private ensures that it will not be instantiated.
     */
    private MetaValidator() {}

    /**
     * Part of the workflow validation can be done here.
     *
     * @param mwf The meta model object representing a workflow.
     */
    public static void validate(final MetaWorkflow mwf) {
        validateForkAndJoins(mwf);
    }

    private static void validateForkAndJoins(final MetaWorkflow mwf) {
        // Number of forks and joins must be the same
        boolean error = false;
        if (mwf.getForkNodes() != null && mwf.getJoinNodes() != null) {
            error = (mwf.getForkNodes().size() != mwf.getJoinNodes().size());

        } else {
            // Or both are null or both aren't null. Other way means they have different sizes
            error = !(mwf.getForkNodes() == null && mwf.getJoinNodes() == null);
        }

        if (error) {
            throw new RuntimeException("Each fork node must have a corresponding join node.");
        }
    }

}
