package somossuinos.jackketch.transform.violet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class VltEdge {

    @XmlElement(name = "start")
    private VltReference start;

    @XmlElement(name = "end")
    private VltReference end;

}
