package somossuinos.jackketch.workflow;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.exception.Jk4flowWorkflowException;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

public class Jk4flowWorkflowTest {

    private final static String ID = "#ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_create_With_Null_Parameter_Fails() {
        thrown.expect(Jk4flowWorkflowException.class);
        thrown.expectMessage("Workflow's Initial Node required");

        new Jk4flowWorkflow(null);
    }

    @Test
    public void test_create_With_No_Initial_Node_Fails() {
        final Node node = NodeFactory.createNode(ID, NodeType.ACTION);

        thrown.expect(Jk4flowWorkflowException.class);
        thrown.expectMessage("Workflow's Initial Node required");

        new Jk4flowWorkflow(node);
    }

    @Test
    public void test_create_Successful() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);
        Assert.assertThat(new Jk4flowWorkflow(node), CoreMatchers.notNullValue());
    }

    @Test
    public void test_getInitialNode_Is_Not_Null() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);
        final Jk4flowWorkflow wf = new Jk4flowWorkflow(node);

        Assert.assertThat(node, CoreMatchers.is(wf.getInitialNode()));
    }

}
