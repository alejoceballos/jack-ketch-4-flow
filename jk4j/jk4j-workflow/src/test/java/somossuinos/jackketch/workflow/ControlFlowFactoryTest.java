package somossuinos.jackketch.workflow;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

public class ControlFlowFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String ID = "#ID";
    private static final String OTHER_ID = "#OTHER_ID";
    private static final String ANOTHER_ID = "#ANOTHER_ID";

    private static final Node origin = NodeFactory.createNode(ID, NodeType.INITIAL);
    private static final Node target = NodeFactory.createNode(OTHER_ID, NodeType.ACTION);
    private static final Node untarget = NodeFactory.createNode(ANOTHER_ID, NodeType.JOIN);

    @Test
    public void test_create_Node_Without_Origin_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No origin was passed as argument");

        ControlFlowFactory.create(null, null, null);
    }

    @Test
    public void test_create_Node_Without_Target_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No target was passed as argument");

        ControlFlowFactory.create(origin, null, null);
    }

    @Test
    public void test_create_Node_With_Origin_Equals_Target_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Origin and target cannot be the same object");

        ControlFlowFactory.create(target, target, null);
    }

    @Test
    public void test_create_Node_With_Null_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No allowed types were passed as argument");

        ControlFlowFactory.create(origin, target, null);
    }

    @Test
    public void test_create_Node_With_Empty_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No allowed types were passed as argument");

        ControlFlowFactory.create(origin, target, new NodeType[0]);
    }

    @Test
    public void test_create_Node_With_Null_Elements_In_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Null types in allowed types list are not accepted");

        ControlFlowFactory.create(origin, target, new NodeType[] { NodeType.ACTION, null, NodeType.FORK });
    }

    @Test
    public void test_create_Node_With_Repeated_Elements_In_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Repeated types in allowed types list are not accepted");

        ControlFlowFactory.create(origin, target, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.ACTION });
    }

    @Test
    public void test_create_Node_With_Wrong_Node_Type_Target_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Node type %s is not accepted", untarget.getType().name()));

        ControlFlowFactory.create(origin, untarget, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.DECISION });
    }

    @Test
    public void test_create_Node_Succeeds() {
        final Node node = ControlFlowFactory.create(origin, target, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.DECISION });
        Assert.assertEquals(target, node);
    }

}
