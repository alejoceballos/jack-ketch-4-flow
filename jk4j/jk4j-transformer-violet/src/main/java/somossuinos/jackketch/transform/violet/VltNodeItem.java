package somossuinos.jackketch.transform.violet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang3.StringUtils;

@XmlAccessorType(XmlAccessType.NONE)
public class VltNodeItem {

    @XmlAttribute(name = "id")
    private String id;

    public boolean validate() {
        return !StringUtils.isBlank(id);
    }

}
