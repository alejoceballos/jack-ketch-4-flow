package somossuinos.jackketch.workflow;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

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
    public void test_createNode_Initial_Succeeds() {
        final Node node = NodeFactory.createNode(ID, NodeType.INITIAL);

        Assert.assertEquals(InitialNode.class, node.getClass());
    }

    @Test
    public void test_createNode_Action_Succeeds() {
        final Node node = NodeFactory.createNode(ID, NodeType.ACTION);

        Assert.assertEquals(ActionNode.class, node.getClass());
    }

    @Test
    public void test_createNode_Join_Succeeds() {
        final Node node = NodeFactory.createNode(ID, NodeType.JOIN);

        Assert.assertEquals(JoinNode.class, node.getClass());
    }

    @Test
    public void test_createNode_Final_Succeeds() {
        final Node node = NodeFactory.createNode(ID, NodeType.FINAL);

        Assert.assertEquals(FinalNode.class, node.getClass());
    }

    @Test
    public void test_createNode_Fork_Succeeds() {
        final Node node = NodeFactory.createNode(ID, NodeType.FORK);

        Assert.assertEquals(ForkNode.class, node.getClass());
    }

    @Test
    public void test_createNode_Decision_Succeeds() {
        final Node node = NodeFactory.createNode(ID, NodeType.DECISION);

        Assert.assertEquals(DecisionNode.class, node.getClass());
    }

}
