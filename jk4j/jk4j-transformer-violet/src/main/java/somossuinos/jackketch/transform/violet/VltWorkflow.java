package somossuinos.jackketch.transform.violet;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ActivityDiagramGraph")
@XmlAccessorType(XmlAccessType.NONE)
public class VltWorkflow {

    @XmlElement(name = "nodes")
    private List<VltNode> nodes = new ArrayList<>(0);

    @XmlElement(name = "edges")
    private List<VltEdge> edges = new ArrayList<>(0);

    public boolean validate() {
        return nodes.size() > 2 && edges.size() > 1; // Minimum of 3 nodes (Initial, Action and End) and 2 edges linking them
    }
}
