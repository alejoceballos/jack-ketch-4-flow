package somossuinos.jackketch.workflow;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.exception.Jk4flowWorkflowException;
import somossuinos.jackketch.workflow.executable.ActionNode;
import somossuinos.jackketch.workflow.node.ConditionalControlFlowNode;
import somossuinos.jackketch.workflow.node.MultipleControlFlowNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

public class NodeFactoryTest {

    private static final String ID = "#ID";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreateNode_Node_Type_Null_Fails() {
        thrown.expect(Jk4flowWorkflowException.class);
        thrown.expectMessage("Must have a node type to create a node");

        NodeFactory.createNode("", null);
    }

    @Test
    public void testCreateNode_Initial() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);

        Assert.assertTrue(node instanceof SingleControlFlowNode);
    }

    @Test
    public void testCreateNode_Action() {
        final Node node = NodeFactory.createNode(ID, NodeType.ACTION);

        Assert.assertTrue(node instanceof ActionNode);
    }

    @Test
    public void testCreateNode_Join() {
        final Node node = NodeFactory.createNode(ID, NodeType.JOIN);

        Assert.assertTrue(node instanceof SingleControlFlowNode);
    }

    @Test
    public void testCreateNode_Fork() {
        final Node node = NodeFactory.createNode(ID, NodeType.FORK);

        Assert.assertTrue(node instanceof MultipleControlFlowNode);
    }

    @Test
    public void testCreateNode_Decision() {
        final Node node = NodeFactory.createNode(ID, NodeType.DECISION);

        Assert.assertTrue(node instanceof ConditionalControlFlowNode);
    }

    @Test
    public void testCreateNode_Final() {
        final Node node = NodeFactory.createNode(ID, NodeType.FINAL);

        Assert.assertTrue(node instanceof Node);
    }
}
