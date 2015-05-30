package somossuinos.jackketch.transform.model.meta;

public class MetaActionNode extends MetaBaseNode {

    public MetaActionNode() {
    }

    public MetaActionNode(String id, String callback) {
        super(id);
        this.callback = callback;
    }

    private String callback;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
