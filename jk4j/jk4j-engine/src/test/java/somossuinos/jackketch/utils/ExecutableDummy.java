package somossuinos.jackketch.utils;

import java.lang.reflect.Method;
import somossuinos.jackketch.workflow.context.WorkflowContext;

public class ExecutableDummy {
    public static final String KEY = "KEY";
    public static final String VALUE = "VALUE";

    public void doSomething(final WorkflowContext wc) {
        wc.set(KEY, VALUE);
    }

    public static ExecutableDummy create() {
        return new ExecutableDummy();
    }

    public Method getExecutableMethod() {
        final Method method;

        try {
            final Class clz = Class.forName(this.getClass().getName());
            method = clz.getMethod("doSomething", WorkflowContext.class);

        } catch (ReflectiveOperationException e) {
            throw  new RuntimeException(e);
        }

        return method;
    }
}
