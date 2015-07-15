package somossuinos.jackketch.webclient.transformer;

import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.executable.ContextBindableObject;

public class VioletXmlToVioletWorkflow implements ContextBindableObject {
    private WorkflowContext context;

    @Override
    public void setContext(final WorkflowContext context) {
        this.context = context;
    }

    public void execute() {
        context.set("VIOLET_WORKFLOW", Transformers.XML_TO_VIOLET.transform((String) context.get("VIOLET_XML")));
    }
}
