package somossuinos.jackketch.workflow.node;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.NodeFactory;
import somossuinos.jackketch.workflow.executable.ActionNode;

public class NodeFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String ID = "#ID";

    @Test
    public void test_createNode_Without_Node_Type_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Must a node type to create a node");

        NodeFactory.createNode(ID, null);
    }

    @Test
    public void test_createNode_Initial_Is_Right_Class() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);

        Assert.assertTrue(node instanceof SingleControlFlowNode);
    }

    @Test
    public void test_createNode_Initial_Has_Right_Type() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);

        Assert.assertEquals(NodeType.INITIAL, node.getType());
    }

    @Test
    public void test_createNode_Initial_Has_Right_Id() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);

        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_createNode_Initial_Allows_Right_Types() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);

        Assert.assertArrayEquals(new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK }, ((ControlFlowNode) node).getAllowedTypes());
    }

    @Test
    public void test_createNode_Join_Is_Right_Class() {
        final Node node = NodeFactory.createNode(ID, NodeType.JOIN);

        Assert.assertTrue(node instanceof SingleControlFlowNode);
    }

    @Test
    public void test_createNode_Join_Has_Right_Type() {
        final Node node = NodeFactory.createNode(ID, NodeType.JOIN);

        Assert.assertEquals(NodeType.JOIN, node.getType());
    }

    @Test
    public void test_createNode_Join_Has_Right_Id() {
        final Node node = NodeFactory.createNode(ID, NodeType.JOIN);

        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_createNode_Join_Allows_Right_Types() {
        final Node node = NodeFactory.createNode(ID, NodeType.JOIN);

        Assert.assertArrayEquals(new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL }, ((ControlFlowNode) node).getAllowedTypes());
    }
    @Test
    public void test_createNode_Action_Succeeds() {
        final Node node = NodeFactory.createNode(ID, NodeType.ACTION);

        Assert.assertEquals(ActionNode.class, node.getClass());
    }

    @Test
    public void test_createNode_Final_Is_Right_Class() {
        final Node node = NodeFactory.createNode(ID, NodeType.FINAL);

        Assert.assertTrue(node instanceof Node);
    }

    @Test
    public void test_createNode_Final_Has_Right_Type() {
        final Node node = NodeFactory.createNode(ID, NodeType.FINAL);

        Assert.assertEquals(NodeType.FINAL, node.getType());
    }

    @Test
    public void test_createNode_Final_Has_Right_Id() {
        final Node node = NodeFactory.createNode(ID, NodeType.FINAL);

        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_createNode_Fork_Is_Right_Class() {
        final Node node = NodeFactory.createNode(ID, NodeType.FORK);

        Assert.assertTrue(node instanceof MultipleControlFlowNode);
    }

    @Test
    public void test_createNode_Fork_Has_Right_Type() {
        final Node node = NodeFactory.createNode(ID, NodeType.FORK);

        Assert.assertEquals(NodeType.FORK, node.getType());
    }

    @Test
    public void test_createNode_Fork_Has_Right_Id() {
        final Node node = NodeFactory.createNode(ID, NodeType.FORK);

        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_createNode_Fork_Allows_Right_Types() {
        final Node node = NodeFactory.createNode(ID, NodeType.FORK);

        Assert.assertArrayEquals(new NodeType[] { NodeType.ACTION, NodeType.DECISION }, ((ControlFlowNode) node).getAllowedTypes());
    }

    @Test
    public void test_createNode_Fork_getMinFlowsAlloweds() {
        final Node node = NodeFactory.createNode(ID, NodeType.FORK);

        Assert.assertEquals(2, ((MultipleControlFlowNode) node).getMinFlowsAllowed());
    }

    @Test
    public void test_createNode_Decision_Is_Right_Class() {
        final Node node = NodeFactory.createNode(ID, NodeType.DECISION);

        Assert.assertTrue(node instanceof ConditionalControlFlowNode);
    }

    @Test
    public void test_createNode_Decision_Has_Right_Type() {
        final Node node = NodeFactory.createNode(ID, NodeType.DECISION);

        Assert.assertEquals(NodeType.DECISION, node.getType());
    }

    @Test
    public void test_createNode_Decision_Has_Right_Id() {
        final Node node = NodeFactory.createNode(ID, NodeType.DECISION);

        Assert.assertEquals(ID, node.getId());
    }

    @Test
    public void test_createNode_Decision_Allows_Right_Types() {
        final Node node = NodeFactory.createNode(ID, NodeType.DECISION);

        Assert.assertArrayEquals(new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL }, ((ControlFlowNode) node).getAllowedTypes());
    }

    @Test
    public void test_createNode_Decision_getMinFlowsAlloweds() {
        final Node node = NodeFactory.createNode(ID, NodeType.DECISION);

        Assert.assertEquals(1, ((ConditionalControlFlowNode) node).getMinFlowsAllowed());
    }

}
