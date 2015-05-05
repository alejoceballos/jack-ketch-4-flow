package somossuinos.jackketch.workflow.node;

import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MultipleControlFlowNodeTest {

    private final static String ID = "#ID";
    private final static String OTHER_ID = "#OTHER_ID";
    private final static String ANOTHER_ID = "#ANOTHER_ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_create_Assign_Empty_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final MultipleControlFlowNode node = TestUtils.createMultipleCFNode(" ");
    }

    @Test
    public void test_create_Assign_Null_Id_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"id\" must not be empty");

        final MultipleControlFlowNode node = TestUtils.createMultipleCFNode(null);
    }

    @Test
    public void test_getType_Is_The_Right_One() {
        final MultipleControlFlowNode node = TestUtils.createMultipleCFNode(ID);
        Assert.assertEquals(NodeType.FORK, node.getType());
    }

    @Test
    public void test_getId_Is_The_Same_As_Assigned_Id() {
        final MultipleControlFlowNode node = TestUtils.createMultipleCFNode(ID);
        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_setFlows_getFlows() {
        final MultipleControlFlowNode node = TestUtils.createMultipleCFNode(ID);
        final SingleControlFlowNode flow1 = TestUtils.createSingleCFNode(OTHER_ID);
        final SingleControlFlowNode flow2 = TestUtils.createSingleCFNode(ANOTHER_ID);

        final Set<Node> flows = new HashSet<>(1);
        flows.add(flow1);
        flows.add(flow2);

        node.setFlows(flows);

        Assert.assertTrue(
                flows != node.getFlows() &&
                        flows.size() == node.getFlows().size() &&
                        node.getFlows().contains(flow1) &&
                        node.getFlows().contains(flow2));
    }

}
