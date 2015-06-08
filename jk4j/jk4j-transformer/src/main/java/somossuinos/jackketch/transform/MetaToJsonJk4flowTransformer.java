package somossuinos.jackketch.transform;

import com.google.gson.Gson;
import somossuinos.jackketch.transform.meta.MetaWorkflow;

public class MetaToJsonJk4flowTransformer implements Jk4flowTransformer<MetaWorkflow, String> {

    private static final Gson GSON = new Gson();

    @Override
    public String transform(final MetaWorkflow meta) {
        return GSON.toJson(meta);
    }
}
