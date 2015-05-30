package somossuinos.jackketch.transform.model.meta;

public class MetaBaseNode {

    public MetaBaseNode() {
    }

    public MetaBaseNode(String id) {
        this.id = id;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
