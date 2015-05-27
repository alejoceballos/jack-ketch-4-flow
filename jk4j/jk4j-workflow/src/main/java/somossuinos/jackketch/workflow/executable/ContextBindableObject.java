package somossuinos.jackketch.workflow.executable;

import somossuinos.jackketch.workflow.context.WorkflowContext;

/**
 * Objects that are bound to action nodes and need to use the context flow
 * object must implement this interface.
 * <p>
 * All objects that will be executed by an action node are considered "bindable"
 * objects, but those that need to make any use of the flow context object must
 * receive it anyhow. This type of objects must implement this interface so the
 * action node will be able to pass the context flow object to the object being
 * executed.
 * </p>
 */
public interface ContextBindableObject {

    /**
     * Sets the workflow context on a bindable object.
     *
     * @param context The workflow context object to be used during the bounded object execution.
     */
    void setContext(final WorkflowContext context);

}
