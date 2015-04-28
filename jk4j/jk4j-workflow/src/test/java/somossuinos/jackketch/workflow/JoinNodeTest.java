package somossuinos.jackketch.workflow;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.node.NodeType;

public class JoinNodeTest {

    private final static String ID = "#ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_create_Assign_Empty_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final JoinNode jn = new JoinNode(" ");
    }

    @Test
    public void test_create_Assign_Null_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final JoinNode jn = new JoinNode(null);
    }

    @Test
    public void test_getType_Is_The_Right_One() {
        final JoinNode jn = new JoinNode(ID);
        Assert.assertEquals(NodeType.JOIN, jn.getType());
    }

    @Test
    public void test_getId_Is_The_Same_As_Assigned_Id() {
        final JoinNode jn = new JoinNode(ID);
        Assert.assertEquals(ID, jn.getId());
    }

}
