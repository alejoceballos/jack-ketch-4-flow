package somossuinos.jackketch.transform.model.json;

public class JsonActionNode extends JsonBaseNode {

    public JsonActionNode() {
    }

    public JsonActionNode(String id, String callback) {
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
