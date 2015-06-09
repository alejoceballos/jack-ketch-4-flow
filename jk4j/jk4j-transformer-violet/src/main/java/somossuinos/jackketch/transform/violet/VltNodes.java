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

@XmlAccessorType(XmlAccessType.NONE)
public class VltNodes {

    @XmlElement(name = "ScenarioStartNode")
    private List<VltNodeItem> scenarioStartNodes = new ArrayList<>(0);

    public List<VltNodeItem> getScenarioStartNodes() {
        return scenarioStartNodes;
    }

    @XmlElement(name = "SynchronizationBarNode")
    private List<VltNodeItem> synchronizationBarNodes = new ArrayList<>(0);

    public List<VltNodeItem> getSynchronizationBarNodes() {
        return synchronizationBarNodes;
    }

    @XmlElement(name = "ActivityNode")
    private List<VltNodeItem> activityNodes = new ArrayList<>(0);

    public List<VltNodeItem> getActivityNodes() {
        return activityNodes;
    }

    @XmlElement(name = "DecisionNode")
    private List<VltNodeItem> decisionNodes = new ArrayList<>(0);

    public List<VltNodeItem> getDecisionNodes() {
        return decisionNodes;
    }

    @XmlElement(name = "ScenarioEndNode")
    private List<VltNodeItem> scenarioEndNodes = new ArrayList<>(0);

    public List<VltNodeItem> getScenarioEndNodes() {
        return scenarioEndNodes;
    }

    @XmlElement(name = "NoteNode")
    private List<VltNoteNode> noteNodes = new ArrayList<>(0);

    public List<VltNoteNode> getNoteNodes() {
        return noteNodes;
    }

    public boolean validate() {
        return scenarioStartNodes.size() == 1 && // Must have only one initial node
                scenarioEndNodes.size() == 1 && // Must have only one final node
                synchronizationBarNodes.size() % 2 == 0 && // Forks and Joins are represented the same way, so must be in pairs
                noteNodes.size() >= activityNodes.size(); // At least one executor specification note per action
    }

}
