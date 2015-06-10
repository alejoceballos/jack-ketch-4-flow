package somossuinos.jackketch.workflow.node;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConditionalControlFlowNodeTest {

    private final static String ID = "#ID";

    private ConditionalControlFlowNode createNode(final String id) {
        return new ConditionalControlFlowNode(id) {
            @Override
            public int getMinFlowsAllowed() {
                return 1;
            }

            @Override
            public NodeType[] getAllowedTypes() {
                return new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL };
            }

            @Override
            public NodeType getType() {
                return NodeType.DECISION;
            }
        };
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_create_Assign_Empty_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final ConditionalControlFlowNode node = this.createNode(" ");
    }

    @Test
    public void test_create_Assign_Null_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final ConditionalControlFlowNode node = this.createNode(null);
    }

    @Test
    public void test_getType_Is_The_Right_One() {
        final ConditionalControlFlowNode node = this.createNode(ID);
        Assert.assertEquals(NodeType.DECISION, node.getType());
    }

    @Test
    public void test_getId_Is_The_Same_As_Assigned_Id() {
        final ConditionalControlFlowNode node = this.createNode(ID);
        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_getAllowedTypes() {
        final ConditionalControlFlowNode node = this.createNode(ID);
        Assert.assertArrayEquals(new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL }, node.getAllowedTypes());
    }

}
