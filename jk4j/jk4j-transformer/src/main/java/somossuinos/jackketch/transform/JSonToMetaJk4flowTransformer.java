package somossuinos.jackketch.transform;

import com.google.gson.Gson;
import somossuinos.jackketch.transform.meta.MetaWorkflow;

public class JSonToMetaJk4flowTransformer implements Jk4flowTransformer<String, MetaWorkflow> {

    private static final Gson GSON = new Gson();

    @Override
    public MetaWorkflow transform(final String json) {
        return GSON.fromJson(json.toString(), MetaWorkflow.class);
    }
}
