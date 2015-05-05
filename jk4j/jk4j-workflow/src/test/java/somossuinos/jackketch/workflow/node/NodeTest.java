package somossuinos.jackketch.workflow.node;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NodeTest {

    private final static String ID = "#ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public Node createNode(final String id) {
        return new Node(id) {
            @Override
            public NodeType getType() {
                return NodeType.FINAL;
            }
        };
    }

    @Test
    public void test_create_Assign_Empty_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final Node node = this.createNode(" ");
    }

    @Test
    public void test_create_Assign_Null_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final Node node = this.createNode(null);
    }

    @Test
    public void test_getType_Is_The_Right_One() {
        final Node node = this.createNode(ID);
        Assert.assertEquals(NodeType.FINAL, node.getType());
    }

    @Test
    public void test_getId_Is_The_Same_As_Assigned_Id() {
        final Node node = this.createNode(ID);
        Assert.assertEquals(ID, node.getId());
    }

}
