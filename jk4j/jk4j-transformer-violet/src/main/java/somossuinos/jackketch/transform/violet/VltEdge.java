package somossuinos.jackketch.transform.violet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class VltEdge {

    @XmlAttribute(name = "id")
    private String id;

    public String getId() {
        return id;
    }

    @XmlElement(name = "start")
    private VltReference start;

    public VltReference getStart() {
        return start;
    }

    @XmlElement(name = "end")
    private VltReference end;

    public VltReference getEnd() {
        return end;
    }

    @XmlElement(name = "middleLabel")
    private String middleLabel;

    public String getMiddleLabel() {
        return middleLabel;
    }
}
