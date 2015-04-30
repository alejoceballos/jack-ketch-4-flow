package somossuinos.jackketch.workflow;

import java.util.HashSet;
import java.util.Set;
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
    private static final String YET_ANOTHER_ID = "#YET_ANOTHER_ID";

    private static final Node ORIGIN_INITIAL = NodeFactory.createNode(ID, NodeType.INITIAL);
    private static final Node TARGET_ACTION = NodeFactory.createNode(OTHER_ID, NodeType.ACTION);
    private static final Node TARGET_JOIN = NodeFactory.createNode(ANOTHER_ID, NodeType.JOIN);
    private static final Node TARGET_FORK = NodeFactory.createNode(YET_ANOTHER_ID, NodeType.FORK);

    @Test
    public void test_create_Node_Without_Origin_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No origin was passed as argument");

        ControlFlowFactory.create(null, null, null);
    }

    @Test
    public void test_create_Node_Set_Without_Origin_Fails() {
        final Set<Node> targets = new HashSet<>(1);
        targets.add(TARGET_ACTION);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No origin was passed as argument");

        ControlFlowFactory.create(null, targets, null, 0);
    }

    @Test
    public void test_create_Node_Without_Target_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No target was passed as argument");

        ControlFlowFactory.create(ORIGIN_INITIAL, null, null);
    }

    @Test
    public void test_create_Node_Set_With_Targets_Empty_Fails() {
        final Set<Node> targets = new HashSet<>();

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No targets were passed as argument");

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, null, 0);
    }

    @Test
    public void test_create_Node_Set_With_Null_Element_In_Targets_Fails() {
        final Set<Node> targets = new HashSet<>(3);
        targets.add(TARGET_ACTION);
        targets.add(null);
        targets.add(TARGET_FORK);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Null targets in target list are not accepted");

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, null, 0);
    }

    @Test
    public void test_create_Node_With_Self_Redirection_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Origin and target cannot be the same object");

        ControlFlowFactory.create(TARGET_ACTION, TARGET_ACTION, null);
    }

    @Test
    public void test_create_Node_Set_With_Self_Redirection_Fails() {
        final Set<Node> targets = new HashSet<>(3);
        targets.add(TARGET_ACTION);
        targets.add(ORIGIN_INITIAL);
        targets.add(TARGET_FORK);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Origin and targets cannot be the same object");

        ControlFlowFactory.create(TARGET_ACTION, targets, null, 0);
    }

    @Test
    public void test_create_Node_With_Null_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No allowed types were passed as argument");

        ControlFlowFactory.create(ORIGIN_INITIAL, TARGET_ACTION, null);
    }

    @Test
    public void test_create_Node_Set_With_Null_Allowed_Types_Fails() {
        final Set<Node> targets = new HashSet<>(2);
        targets.add(TARGET_ACTION);
        targets.add(TARGET_JOIN);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No allowed types were passed as argument");

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, null, 0);
    }

    @Test
    public void test_create_Node_With_Empty_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No allowed types were passed as argument");

        ControlFlowFactory.create(ORIGIN_INITIAL, TARGET_ACTION, new NodeType[0]);
    }

    @Test
    public void test_create_Node_Set_With_Empty_Allowed_Types_Fails() {
        final Set<Node> targets = new HashSet<>(2);
        targets.add(TARGET_ACTION);
        targets.add(TARGET_JOIN);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No allowed types were passed as argument");

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, new NodeType[0], 0);
    }

    @Test
    public void test_create_Node_With_Null_Elements_In_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Null types in allowed types list are not accepted");

        ControlFlowFactory.create(ORIGIN_INITIAL, TARGET_ACTION, new NodeType[] { NodeType.ACTION, null, NodeType.FORK });
    }

    @Test
    public void test_create_Node_Set_With_Null_Elements_In_Allowed_Types_Fails() {
        final Set<Node> targets = new HashSet<>(2);
        targets.add(TARGET_ACTION);
        targets.add(TARGET_JOIN);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Null types in allowed types list are not accepted");

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, new NodeType[] { NodeType.ACTION, null, NodeType.FORK }, 0);
    }

    @Test
    public void test_create_Node_With_Repeated_Elements_In_Allowed_Types_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Repeated types in allowed types list are not accepted");

        ControlFlowFactory.create(ORIGIN_INITIAL, TARGET_ACTION, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.ACTION });
    }

    @Test
    public void test_create_Node_Set_With_Repeated_Elements_In_Allowed_Types_Fails() {
        final Set<Node> targets = new HashSet<>(2);
        targets.add(TARGET_ACTION);
        targets.add(TARGET_JOIN);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Repeated types in allowed types list are not accepted");

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.ACTION }, 0);
    }

    @Test
    public void test_create_Node_With_Wrong_Node_Type_Target_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Node type %s is not accepted", TARGET_JOIN.getType().name()));

        ControlFlowFactory.create(ORIGIN_INITIAL, TARGET_JOIN, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.DECISION });
    }

    @Test
    public void test_create_Node_Set_With_Wrong_Node_Type_Target_Fails() {
        final Set<Node> targets = new HashSet<>(3);
        targets.add(TARGET_ACTION);
        targets.add(TARGET_JOIN);
        targets.add(TARGET_FORK);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Node type %s is not accepted", TARGET_JOIN.getType().name()));

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.DECISION }, 0);
    }

    @Test
    public void test_create_Node_Set_With_Wrong_Min_List_Size_Fails() {
        final Set<Node> targets = new HashSet<>(2);
        targets.add(TARGET_ACTION);
        targets.add(TARGET_JOIN);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Targets list must have at least %s element(s)", 3));

        ControlFlowFactory.create(ORIGIN_INITIAL, targets, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.JOIN }, 3);
    }

    @Test
    public void test_create_Node_Succeeds() {
        final Node node = ControlFlowFactory.create(ORIGIN_INITIAL, TARGET_ACTION, new NodeType[] { NodeType.ACTION, NodeType.FORK, NodeType.DECISION });
        Assert.assertEquals(TARGET_ACTION, node);
    }

    @Test
    public void test_create_Node_Set_Succeeds() {
        final Set<Node> targets = new HashSet<>(3);
        targets.add(TARGET_ACTION);
        targets.add(TARGET_JOIN);
        targets.add(TARGET_FORK);

        final Set<Node> nodes = ControlFlowFactory.create(ORIGIN_INITIAL, targets, new NodeType[] { NodeType.ACTION, NodeType.JOIN, NodeType.FORK}, 2);

        Assert.assertEquals(targets, nodes);
    }

}
