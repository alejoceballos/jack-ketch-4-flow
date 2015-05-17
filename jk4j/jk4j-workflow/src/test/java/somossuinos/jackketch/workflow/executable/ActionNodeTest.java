package somossuinos.jackketch.workflow.executable;

import java.lang.reflect.Method;
import java.util.Map;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.node.NodeType;

public class ActionNodeTest {

    private final static String ID = "#ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private WorkflowContext getWorkflowContext() {
        WorkflowContext wc = new WorkflowContext() {
            private Object whatever;
            @Override public Object get(String key) {return this.whatever;}
            @Override public void set(String key, Object value) { this.whatever = value;}
            @Override public Map<String, Object> getMap() { return null;}
            @Override public void clear() {}
        };

        return wc;
    }

    private Object getExecutableObject() {
        class ExecutableObjectClass {
            public void doSomething(final WorkflowContext wc) {
                wc.set("KEY", "WHATEVER");
            }
        }

        return new ExecutableObjectClass();
    }

    @Test
    public void test_create_Assign_Empty_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final ActionNode an = new ActionNode(" ");
    }

    @Test
    public void test_create_Assign_Null_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final ActionNode an = new ActionNode(null);
    }

    @Test
    public void test_getType() {
        final ActionNode an = new ActionNode(ID);
        Assert.assertEquals(NodeType.ACTION, an.getType());
    }

    @Test
    public void test_getId_Is_The_Same_As_Assigned_Id() {
        final ActionNode an = new ActionNode(ID);
        Assert.assertEquals(ID, an.getId());
    }

    @Test
    public void test_getAllowedTypes() {
        final ActionNode an = new ActionNode(ID);
        Assert.assertArrayEquals(new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL, NodeType.JOIN }, an.getAllowedTypes());
    }

    @Test
    public void test_execute_Without_Method_Fails() {
        final ActionNode an = new ActionNode(ID);
        an.setObject(this.getExecutableObject());

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Cannot execute an operation without a method");

        an.execute(this.getWorkflowContext());
    }

    @Test
    public void test_execute_Without_Object_Fails() {
        WorkflowContext wc = this.getWorkflowContext();

        final Object obj = this.getExecutableObject();
        final Method method;

        try {
            final Class clz = Class.forName(obj.getClass().getName());
            method = clz.getMethod("doSomething", WorkflowContext.class);

        } catch (ReflectiveOperationException e) {
            throw  new RuntimeException(e);
        }

        final ActionNode an = new ActionNode(ID);
        an.setMethod(method);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Cannot execute an operation without an object");

        an.execute(this.getWorkflowContext());
    }

    @Test
    public void test_execute_Object_Method() {
        WorkflowContext wc = this.getWorkflowContext();

        final Object obj = this.getExecutableObject();
        final Method method;

        try {
            final Class clz = Class.forName(obj.getClass().getName());
            method = clz.getMethod("doSomething", WorkflowContext.class);

        } catch (ReflectiveOperationException e) {
            throw  new RuntimeException(e);
        }


        final ActionNode an = new ActionNode(ID);
        an.setObject(obj);
        an.setMethod(method);

        an.execute(wc);

        Assert.assertEquals("WHATEVER", wc.get("KEY"));
    }

}
