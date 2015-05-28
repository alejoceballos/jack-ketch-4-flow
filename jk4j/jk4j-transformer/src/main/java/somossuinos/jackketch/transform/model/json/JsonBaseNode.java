package somossuinos.jackketch.transform.model.json;

public class JsonBaseNode {

    public JsonBaseNode() {
    }

    public JsonBaseNode(String id) {
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
