package somossuinos.jackketch.workflow.node;

import org.apache.commons.lang3.StringUtils;

/**
 * Abstract Node is the template class for all nodes in the Activity diagram
 */
public abstract class AbstractNode implements Node {

    private String id;

    public AbstractNode(final String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("\"id\" must not be empty");
        }

        this.id = id.trim();
    }

    public String getId() {
        return this.id;
    }
}
