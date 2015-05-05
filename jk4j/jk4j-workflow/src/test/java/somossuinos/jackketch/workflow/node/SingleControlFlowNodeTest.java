package somossuinos.jackketch.workflow.node;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SingleControlFlowNodeTest {

    private final static String ID = "#ID";
    private final static String ANOTHER_ID = "#ANOTHER_ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_create_Assign_Empty_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final SingleControlFlowNode node = TestUtils.createSingleCFNode(" ");
    }

    @Test
    public void test_create_Assign_Null_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final SingleControlFlowNode node = TestUtils.createSingleCFNode(null);
    }

    @Test
    public void test_getType_Is_The_Right_One() {
        final SingleControlFlowNode node = TestUtils.createSingleCFNode(ID);
        Assert.assertEquals(NodeType.ACTION, node.getType());
    }

    @Test
    public void test_getId_Is_The_Same_As_Assigned_Id() {
        final SingleControlFlowNode node = TestUtils.createSingleCFNode(ID);
        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_getAllowedTypes() {
        final SingleControlFlowNode node = TestUtils.createSingleCFNode(ID);
        Assert.assertArrayEquals(new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL }, node.getAllowedTypes());
    }

    @Test
    public void test_setFlow_getFlow() {
        final SingleControlFlowNode node = TestUtils.createSingleCFNode(ID);
        final SingleControlFlowNode flow = TestUtils.createSingleCFNode(ANOTHER_ID);

        node.setFlow(flow);

        Assert.assertEquals(flow, node.getFlow());
    }

}
