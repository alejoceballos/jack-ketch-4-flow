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

package somossuinos.jackketch.transform.violet;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import somossuinos.jackketch.transform.exception.Jk4flowTranformerException;

@XmlAccessorType(XmlAccessType.NONE)
public class VltEdges {

    @XmlElement(name = "ActivityTransitionEdge")
    private List<VltEdge> transitionEdges = new ArrayList<>(0);

    public List<VltEdge> getTransitionEdges() {
        return transitionEdges;
    }

    @XmlElement(name = "NoteEdge")
    private List<VltEdge> noteEdges = new ArrayList<>(0);

    public List<VltEdge> getNoteEdges() {
        return noteEdges;
    }

    public void validate() {
        if (transitionEdges == null || transitionEdges.size() <= 1) {
            throw new Jk4flowTranformerException("At least a scenario start nodes, an activity node and a scenario end node are necessary to a valid workflow diagram. It means that at least two transitions edges should be present.");
        }

        if (noteEdges == null || noteEdges.size() < 1) {
            throw new Jk4flowTranformerException("At least an activity node is necessary to a valid workflow diagram. It means that at least one node edge should be present.");
        }
    }

}
