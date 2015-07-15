package somossuinos.jackketch.webclient.transformer;

import somossuinos.jackketch.transform.meta.MetaWorkflow;
import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.executable.ContextBindableObject;

public class MetamodelWorkflowToMetmodelJson implements ContextBindableObject {

    private WorkflowContext context;

    @Override
    public void setContext(final WorkflowContext context) {
        this.context = context;
    }

    public void execute() {
        context.set("META_JSON", Transformers.META_TO_JSON_TRANSFORMER.transform((MetaWorkflow) context.get("META_WORKFLOW")));
    }
}
