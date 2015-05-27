package somossuinos.jackketch.workflow.context;

import java.util.HashMap;
import java.util.Map;

/**
 * The main idea of this factory is to, in the future, be able to
 * dynamically define a series of workflow context object that can be used
 * according some configuration.
 * <p>
 * The basic workflow context object is a simple wrapper for a Map objects.
 * Future implementations will come along a new factory method allowing the
 * creation of other types of wrappers.
 * </p>
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
        return new WorkflowContext() {

            /**
             * A basic Map with String/Object double to store the context attributes
             * and values.
             */
            private Map<String, Object> contextMap = new HashMap<>(0);

            /**
             * {@inheritDoc}
             */
            @Override
            public Object get(final String key) {
                return contextMap.get(key);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void set(final String key, final Object value) {
                if (value != null) {
                    contextMap.put(key, value);
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public Map<String, Object> getMap() {
                final Map<String, Object> clone = new HashMap<>(this.contextMap.size());
                clone.putAll(this.contextMap);

                return clone;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void clear() {
                contextMap.clear();
            }
        };
    }

}
