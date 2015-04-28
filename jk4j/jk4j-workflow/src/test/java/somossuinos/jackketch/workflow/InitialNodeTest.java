package somossuinos.jackketch.workflow;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.node.NodeType;

public class InitialNodeTest {

    private final static String ID = "#ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_create_Should_Not_Assign_Empty_Id() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final InitialNode in = new InitialNode(" ");
    }

    @Test
    public void test_create_Should_Not_Assign_Null_Id() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final InitialNode in = new InitialNode(null);
    }

    @Test
    public void test_getType_Should_Be_The_Right_One() {
        final InitialNode in = new InitialNode(ID);
        Assert.assertEquals(NodeType.INITIAL, in.getType());
    }

    @Test
    public void test_getId_Should_Be_The_Same_As_Assigned_Id() {
        final InitialNode in = new InitialNode(ID);
        Assert.assertEquals(ID, in.getId());
    }

}
