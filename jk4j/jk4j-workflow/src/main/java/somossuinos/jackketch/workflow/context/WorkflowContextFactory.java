package somossuinos.jackketch.workflow.context;

/**
 * The main idea of this factory is to, in the future, be able to
 * dynamically define a series of workflow context object that can be used
 * according some configuration.
 */
public class WorkflowContextFactory {

    /**
     * Private constructor so this class cannot be instantiated.
     */
    private WorkflowContextFactory() { }

    /**
     * Creates a basic workflow context.
     *
     * @return An workflow context instance.
     */
    public static WorkflowContext create() {
        return createBasic();
    }

    /**
     * The {@link WorkflowContext} object of the current workflow execution.
     */
    private static WorkflowContext createBasic() {
        return new BaseWorkflowContext();
    }

}
