package somossuinos.jackketch.engine.mock;

import java.lang.reflect.Method;
import somossuinos.jackketch.workflow.context.WorkflowContext;

/**
 * Mock object that puts the KEY key in the flow context with its id value
 *
 * An executable object to be associated to an action must be the instance of a
 * public class (not default, protected or private).
 *
 * The method of the object to be called must be public and have one parameter of
 * type WorkflowContext.
 */
public class ExecutableActionMock {

    private String key;
    private String id;

    private ExecutableActionMock(final String key, final String id) {
        this.key = key;
        this.id = id;
    }

    public void putIdInContext(final WorkflowContext wc) {
        wc.set(this.key, this.id);
    }

    public static ExecutableActionMock create(final String key, final String id) {
        return new ExecutableActionMock(key, id);
    }

    public Method getPutIdInContextMethod() {
        final Method method;

        try {
            final Class clz = Class.forName(this.getClass().getName());
            method = clz.getMethod("putIdInContext", WorkflowContext.class);

        } catch (ReflectiveOperationException e) {
            throw  new RuntimeException(e);
        }

        return method;
    }
}
