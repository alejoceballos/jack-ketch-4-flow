package somossuinos.jackketch.transform.model.json;

public class JsonControlFlow {

    private String id;
    private String from;
    private String to;

    public JsonControlFlow() {
    }

    public JsonControlFlow(String id, String from, String to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
