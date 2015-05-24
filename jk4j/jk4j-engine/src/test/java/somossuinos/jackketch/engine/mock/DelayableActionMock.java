package somossuinos.jackketch.engine.mock;

import java.lang.reflect.Method;
import java.util.Date;
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
public class DelayableActionMock {

    private long delayInMs;
    private String id;

    private Date start;
    private Date end;

    private DelayableActionMock(final String id, final long delayInMs) {
        this.delayInMs = delayInMs;
        this.id = id;
    }

    public void delayExecution(final WorkflowContext wc) {
        try {
            this.start = new Date();
            Thread.sleep(delayInMs);
            this.end = new Date();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        wc.set(this.id, this.end);
    }

    public static DelayableActionMock create(final String id, final long delayInMs) {
        return new DelayableActionMock(id, delayInMs);
    }

    public Method getDelayExecutionMethod() {
        final Method method;

        try {
            final Class clz = Class.forName(this.getClass().getName());
            method = clz.getMethod("delayExecution", WorkflowContext.class);

        } catch (ReflectiveOperationException e) {
            throw  new RuntimeException(e);
        }

        return method;
    }
}
