package somossuinos.jackketch.transform.violet;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class VltNode {

    @XmlElement(name = "ScenarioStartNode")
    private List<VltNodeItem> scenarioStartNodes = new ArrayList<>(0);

    @XmlElement(name = "SynchronizationBarNode")
    private List<VltNodeItem> synchronizationBarNodes = new ArrayList<>(0);

    @XmlElement(name = "ActivityNode")
    private List<VltNodeItem> activityNodes = new ArrayList<>(0);

    @XmlElement(name = "DecisionNode")
    private List<VltNodeItem> decisionNodes = new ArrayList<>(0);

    @XmlElement(name = "ScenarioEndNode")
    private List<VltNodeItem> scenarioEndNodes = new ArrayList<>(0);

    @XmlElement(name = "NoteNode")
    private List<VltNodeItem> noteNodes = new ArrayList<>(0);

    public boolean validate() {
        return scenarioStartNodes.size() == 1 && // Must have only one initial node
                scenarioEndNodes.size() == 1 && // Must have only one final node
                synchronizationBarNodes.size() % 2 == 0 && // Forks and Joins are represented the same way, so must be in pairs
                noteNodes.size() >= activityNodes.size(); // At least one executor specification note per action
    }

}
