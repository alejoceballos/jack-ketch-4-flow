package somossuinos.jackketch.webclient.transformer;

import somossuinos.jackketch.transform.Jk4flowTransformer;
import somossuinos.jackketch.transform.MetaToJsonTransformer;
import somossuinos.jackketch.transform.MetaToWorkflowTransformer;
import somossuinos.jackketch.transform.VioletToMetaTransformer;
import somossuinos.jackketch.transform.XmlToVioletTransformer;
import somossuinos.jackketch.transform.meta.MetaWorkflow;
import somossuinos.jackketch.transform.violet.VltWorkflow;
import somossuinos.jackketch.workflow.Jk4flowWorkflow;

public class Transformers {
    public static final Jk4flowTransformer<String, VltWorkflow> XML_TO_VIOLET = new XmlToVioletTransformer();
    public static final Jk4flowTransformer<VltWorkflow, MetaWorkflow> VIOLET_TO_META = new VioletToMetaTransformer();
    public static final Jk4flowTransformer<MetaWorkflow, Jk4flowWorkflow> META_TO_WORKFLOW = new MetaToWorkflowTransformer();
    public static final Jk4flowTransformer<MetaWorkflow, String> META_TO_JSON_TRANSFORMER = new MetaToJsonTransformer();
}
