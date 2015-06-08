package somossuinos.jackketch.transform.meta;

/**
 * Part of the workflow validation can be done here.
 * <p>
 *     The workflow validation takes place three different times:
 *     <ol>
 *         <li>During workflow meta object creation (validates final and initial nodes amounts);</li>
 *         <li>After workflow meta object creation (this validation here);</li>
 *         <li>During workflow transformation, from meta object to executable object (check documentation for all rules that takes place).</li>
 *     </ol>
 * </p>
 */
public class MetaValidator {

    /**
     * Constructor. Being private ensures that it will not be instantiated.
     */
    private MetaValidator() {}

    /**
     * Part of the workflow validation can be done here.
     *
     * @param mwf The meta model object representing a workflow.
     */
    public static void validate(final MetaWorkflow mwf) {
        validateForkAndJoins(mwf);
    }

    private static void validateForkAndJoins(final MetaWorkflow mwf) {
        // Number of forks and joins must be the same
        boolean error = false;
        if (mwf.getForkNodes() != null && mwf.getJoinNodes() != null) {
            error = (mwf.getForkNodes().size() != mwf.getJoinNodes().size());

        } else {
            // Or both are null or both aren't null. Other way means they have different sizes
            error = !(mwf.getForkNodes() == null && mwf.getJoinNodes() == null);
        }

        if (error) {
            throw new RuntimeException("Each fork node must have a corresponding join node.");
        }
    }

}
