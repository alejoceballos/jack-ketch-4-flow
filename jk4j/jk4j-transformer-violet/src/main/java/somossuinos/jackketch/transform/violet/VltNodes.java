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

    public void validate() {
        if (scenarioStartNodes.size() != 1) {
            throw new RuntimeException("Scenario START node rule is \"there must be one and only one in the whole diagram\".");
        }

        if (scenarioEndNodes.size() != 1) {
            throw new RuntimeException("Scenario END node rule is \"there must be one and only one in the whole diagram\".");
        }

        if (synchronizationBarNodes.size() % 2 != 0) {
            throw new RuntimeException("Synchronization bar node represent both Forks and Joins, there must be pairs of them.");
        }

        if (activityNodes == null || activityNodes.size() < 1) {
            throw new RuntimeException("For a workflow to make sense, it should have at least one START node, one ACTION node bound to an executable object and one END node.");
        }

        if (noteNodes.size() < activityNodes.size()) {
            throw new RuntimeException("Note node holds information about the the bindable object executed by action nodes. There should be at least one executor specification note per action.");
        }

        for (final VltNodeItem node : scenarioStartNodes) {
            node.validate();
        }

        for (final VltNodeItem node : synchronizationBarNodes) {
            node.validate();
        }

        for (final VltNodeItem node : activityNodes) {
            node.validate();
        }

        for (final VltNodeItem node : decisionNodes) {
            node.validate();
        }

        for (final VltNodeItem node : scenarioEndNodes) {
            node.validate();
        }

        for (final VltNoteNode node : noteNodes) {
            node.validate();
        }
    }

}
