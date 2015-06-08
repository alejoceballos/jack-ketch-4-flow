package somossuinos.jackketch.workflow.context;

import java.util.HashMap;
import java.util.Map;

/**
 * The basic workflow context object is a simple wrapper for a Map object.
 * Future implementations will come along a new factory method allowing the
 * creation of other types of wrappers.
 */
public class BaseWorkflowContext implements WorkflowContext {
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
}
