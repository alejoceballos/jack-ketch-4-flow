package somossuinos.jackketch.webclient.transformer;

import somossuinos.jackketch.transform.violet.VltWorkflow;
import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.executable.ContextBindableObject;

public class VioletWorkflowToMetamodelWorkflow implements ContextBindableObject {
    private WorkflowContext context;

    @Override
    public void setContext(final WorkflowContext context) {
        this.context = context;
    }

    public void execute() {
        context.set("META_WORKFLOW", Transformers.VIOLET_TO_META.transform((VltWorkflow) context.get("VIOLET_WORKFLOW")));
    }

}
