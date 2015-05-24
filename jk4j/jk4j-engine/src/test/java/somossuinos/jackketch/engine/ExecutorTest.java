package somossuinos.jackketch.engine;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.engine.mock.DelayableActionMock;
import somossuinos.jackketch.engine.mock.ExecutableActionMock;
import somossuinos.jackketch.workflow.NodeFactory;
import somossuinos.jackketch.workflow.Workflow;
import somossuinos.jackketch.workflow.conditional.ConditionType;
import somossuinos.jackketch.workflow.conditional.FlowCondition;
import somossuinos.jackketch.workflow.executable.ContextExecutable;
import somossuinos.jackketch.workflow.node.ConditionalControlFlowNode;
import somossuinos.jackketch.workflow.node.MultipleControlFlowNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

public class ExecutorTest {

    private final static String ID = "#ID";
    private final static String KEY = "KEY";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Node getInitialNode() {
        return new Node(ID) {
            @Override
            public NodeType getType() {
                return NodeType.INITIAL;
            }
        };
    }

    @Test
    public void testCreate_Without_Workflow_Parameter_Fails() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Workflow cannot be null");

        final Executor executor = new Executor(null);
    }

    @Test
    public void testCreate_Success() {
        final Workflow wc = Workflow.create(this.getInitialNode());
        Assert.assertNotNull(new Executor(wc));
    }

    /*
     *    (IN)   #1
     *     |
     *     V
     *    (A)   #2
     *     |
     *     V
     *    (FN)   #3
     */
    @Test
    public void testRun_SimpleActionFlow_IN_A_FN() {
        // Final node creation
        final Node fNode = NodeFactory.createNode("#3", NodeType.FINAL);

        // Action node (id #2) creation and flow set to final node
        final String ID = "#2";
        final Node aNode = NodeFactory.createNode(ID, NodeType.ACTION);
        ((SingleControlFlowNode) aNode).setFlow(fNode);
        final ExecutableActionMock obj = ExecutableActionMock.create(KEY, ID);
        ((ContextExecutable) aNode).setObject(obj);
        ((ContextExecutable) aNode).setMethod(obj.getPutIdInContextMethod());

        // Initial node (id #1) creation and flow set to action node
        final Node iNode = NodeFactory.createNode("#1", NodeType.INITIAL);
        ((SingleControlFlowNode) iNode).setFlow(aNode);

        final Workflow wf = Workflow.create(iNode);

        (new Executor(wf)).run();

        Assert.assertEquals(ID, wf.getContext().get(KEY));
    }

    /*
     *     (IN)   #1
     *      |
     *      V
     *     (A1)   #2
     *      |
     *      V
     *     <D>    #3
     *     /  \
     *   |/   \/
     * (A2)  (A3) #4,5
     *   \    /
     *    \  /
     *     V
     *   (FN)     #6
     */
    @Test
    public void testRun_DecisionWithEquals_IN_A1_D_A2_FN() {
        // Final node creation
        final Node fn = NodeFactory.createNode("#6", NodeType.FINAL);

        // Action node 3 (id #5) creation and flow set to final node
        final String A3_ID = "#5";
        final Node an3 = NodeFactory.createNode(A3_ID, NodeType.ACTION);
        final ExecutableActionMock objAn3 = ExecutableActionMock.create(KEY, A3_ID);
        ((ContextExecutable) an3).setObject(objAn3);
        ((ContextExecutable) an3).setMethod(objAn3.getPutIdInContextMethod());
        ((SingleControlFlowNode) an3).setFlow(fn);

        // Action node 2 (id #4) creation and flow set to final node
        final String A2_ID = "#4";
        final Node an2 = NodeFactory.createNode(A2_ID, NodeType.ACTION);
        final ExecutableActionMock objAn2 = ExecutableActionMock.create(KEY, A2_ID);
        ((ContextExecutable) an2).setObject(objAn2);
        ((ContextExecutable) an2).setMethod(objAn2.getPutIdInContextMethod());
        ((SingleControlFlowNode) an2).setFlow(fn);

        final String A1_KEY = "a1_key";
        final String A1_ID = "#2";

        // Decision node (id #3) creation
        final Node dn = NodeFactory.createNode("#3", NodeType.DECISION);
        // Flow control from decision to action node 2 (if KEY == #2 goto A2)
        Map<FlowCondition, Node> flows = new HashMap<>(1);
        final FlowCondition fc2a2 = new FlowCondition(A1_KEY, ConditionType.EQ, A1_ID);
        flows.put(fc2a2, an2);
        // Otherwise, goto A3
        ((ConditionalControlFlowNode) dn).setFlows(flows, an3);

        // Action node 1 (id #2) creation. Sets the context to be used by the decision
        final Node an1 = NodeFactory.createNode(A1_ID, NodeType.ACTION);
        final ExecutableActionMock objAn1 = ExecutableActionMock.create(A1_KEY, A1_ID);
        ((ContextExecutable) an1).setObject(objAn1);
        ((ContextExecutable) an1).setMethod(objAn1.getPutIdInContextMethod());
        ((SingleControlFlowNode) an1).setFlow(dn);

        // Initial node (id #1) creation and flow set to action node 1
        final Node iNode = NodeFactory.createNode("#1", NodeType.INITIAL);
        ((SingleControlFlowNode) iNode).setFlow(an1);

        final Workflow wf = Workflow.create(iNode);

        (new Executor(wf)).run();

        Assert.assertEquals(A2_ID, wf.getContext().get(KEY));
    }

    /*
     *     (IN)   #1
     *      |
     *      V
     *     (A1)   #2
     *      |
     *      V
     *     <D>    #3
     *     /  \
     *   |/   \/
     * (A2)  (A3) #4,5
     *   \    /
     *    \  /
     *     V
     *   (FN)     #6
     */
    @Test
    public void testRun_DecisionOtherwise_IN_A1_D_A3_FN() {
        // Final node creation
        final Node fn = NodeFactory.createNode("#6", NodeType.FINAL);

        // Action node 3 (id #5) creation and flow to final node
        final String A3_ID = "#5";
        final Node an3 = NodeFactory.createNode(A3_ID, NodeType.ACTION);
        final ExecutableActionMock objAn3 = ExecutableActionMock.create(KEY, A3_ID);
        ((ContextExecutable) an3).setObject(objAn3);
        ((ContextExecutable) an3).setMethod(objAn3.getPutIdInContextMethod());
        ((SingleControlFlowNode) an3).setFlow(fn);

        // Action node 2 (id #4) creation and flow to final node
        final String A2_ID = "#4";
        final Node an2 = NodeFactory.createNode(A2_ID, NodeType.ACTION);
        final ExecutableActionMock objAn2 = ExecutableActionMock.create(KEY, A2_ID);
        ((ContextExecutable) an2).setObject(objAn2);
        ((ContextExecutable) an2).setMethod(objAn2.getPutIdInContextMethod());
        ((SingleControlFlowNode) an2).setFlow(fn);

        final String A1_KEY = "a1_key";
        final String A1_ID = "#2";

        // Decision node (id #3) creation
        final Node dNode = NodeFactory.createNode("#3", NodeType.DECISION);
        // Flow control from decision to action node 2 (if KEY == #2 goto A2)
        Map<FlowCondition, Node> flows = new HashMap<>(1);
        final FlowCondition fc2a2 = new FlowCondition(A1_KEY, ConditionType.EQ, "ANYTHING WRONG");
        flows.put(fc2a2, an2);
        // Otherwise, goto A3
        ((ConditionalControlFlowNode) dNode).setFlows(flows, an3);

        // Action node 1 (id #2) creation. Sets the context to be used by the decision
        final Node an1 = NodeFactory.createNode(A1_ID, NodeType.ACTION);
        final ExecutableActionMock objAn1 = ExecutableActionMock.create(A1_KEY, A1_ID);
        ((ContextExecutable) an1).setObject(objAn1);
        ((ContextExecutable) an1).setMethod(objAn1.getPutIdInContextMethod());
        ((SingleControlFlowNode) an1).setFlow(dNode);

        // Initial node (id #1) creation and flow to action node 1
        final Node iNode = NodeFactory.createNode("#1", NodeType.INITIAL);
        ((SingleControlFlowNode) iNode).setFlow(an1);

        final Workflow wf = Workflow.create(iNode);

        (new Executor(wf)).run();

        Assert.assertEquals(A3_ID, wf.getContext().get(KEY));
    }

    /*
     *     (IN)   #1
     *      |
     *      V
     *    -----   #2
     *     /  \
     *   |/   \/
     * (A1)  (A2) #3,4
     *   \    /
     *    \  /
     *     V
     *   -----    #5
     *     |
     *     V
     *   (FN)     #6
     */
    @Test
    public void testRun_Fork_IN_FN_A1_A2_JN_FN() {
        // Final node creation (id #6)
        final Node finNode = NodeFactory.createNode("#6", NodeType.FINAL);

        // Join node creation (id #5) and flow to final node
        final Node jNode = NodeFactory.createNode("#5", NodeType.JOIN);
        ((SingleControlFlowNode) jNode).setFlow(finNode);

        // Action node 2 creation (id #4) and flow to join node
        final String A2_KEY = "a2_key";
        final String A2_ID = "#4";
        final Node an2 = NodeFactory.createNode(A2_ID, NodeType.ACTION);
        final ExecutableActionMock objAn2 = ExecutableActionMock.create(A2_KEY, A2_ID);
        ((ContextExecutable) an2).setObject(objAn2);
        ((ContextExecutable) an2).setMethod(objAn2.getPutIdInContextMethod());
        ((SingleControlFlowNode) an2).setFlow(jNode);

        // Action node 1 creation (id #3) and flow to join node
        final String A1_KEY = "a1_key";
        final String A1_ID = "#3";
        final Node an1 = NodeFactory.createNode(A1_ID, NodeType.ACTION);
        final ExecutableActionMock objAn1 = ExecutableActionMock.create(A1_KEY, A1_ID);
        ((ContextExecutable) an1).setObject(objAn1);
        ((ContextExecutable) an1).setMethod(objAn1.getPutIdInContextMethod());
        ((SingleControlFlowNode) an1).setFlow(jNode);

        // Fork creation (id #2) and flow to actions node
        final Node fNode = NodeFactory.createNode("#2", NodeType.FORK);
        final Set<Node> flows = new HashSet<>(2);
        flows.add(an1);
        flows.add(an2);
        ((MultipleControlFlowNode) fNode).setFlows(flows);

        // Initial node (id #1) creation and flow to action node 1
        final Node iNode = NodeFactory.createNode("#1", NodeType.INITIAL);
        ((SingleControlFlowNode) iNode).setFlow(fNode);

        final Workflow wf = Workflow.create(iNode);

        (new Executor(wf)).run();

        Assert.assertTrue(
                wf.getContext().get(A1_KEY).equals(A1_ID) &&
                wf.getContext().get(A2_KEY).equals(A2_ID)
        );
    }

    /*
     *     (IN)   #1
     *      |
     *      V
     *    -----   #2
     *     /  \
     *   |/   \/
     * (A1)  (A2) #3,4
     *   \    /
     *    \  /
     *     V
     *   -----    #5
     *     |
     *     V
     *   (FN)     #6
     */
    @Test
    public void testRun_Fork_With_Actions_With_Different_Delays_IN_FN_A1_A2_JN_FN() {
        // Final node creation (id #6)
        final Node fn = NodeFactory.createNode("#6", NodeType.FINAL);

        // Join node creation (id #5) and flow to final node
        final Node jn = NodeFactory.createNode("#5", NodeType.JOIN);
        ((SingleControlFlowNode) jn).setFlow(fn);

        // Action node 2 creation (id #4) and flow to join node
        final String A2_ID = "#4";
        final Node an2 = NodeFactory.createNode(A2_ID, NodeType.ACTION);
        final DelayableActionMock objAn2 = DelayableActionMock.create(A2_ID, 20);
        ((ContextExecutable) an2).setObject(objAn2);
        ((ContextExecutable) an2).setMethod(objAn2.getDelayExecutionMethod());
        ((SingleControlFlowNode) an2).setFlow(jn);

        // Action node 1 creation (id #3) and flow to join node
        final String A1_ID = "#3";
        final Node an1 = NodeFactory.createNode(A1_ID, NodeType.ACTION);
        final DelayableActionMock objAn1 = DelayableActionMock.create(A1_ID, 100);
        ((ContextExecutable) an1).setObject(objAn1);
        ((ContextExecutable) an1).setMethod(objAn1.getDelayExecutionMethod());
        ((SingleControlFlowNode) an1).setFlow(jn);

        // Fork creation (id #2) and flow to actions node
        final Node fk = NodeFactory.createNode("#2", NodeType.FORK);
        final Set<Node> flows = new HashSet<>(2);
        flows.add(an1);
        flows.add(an2);
        ((MultipleControlFlowNode) fk).setFlows(flows);

        // Initial node (id #1) creation and flow to action node 1
        final Node in = NodeFactory.createNode("#1", NodeType.INITIAL);
        ((SingleControlFlowNode) in).setFlow(fk);

        final Workflow wf = Workflow.create(in);

        (new Executor(wf)).run();

        final Date a1Date = (Date) wf.getContext().get(A1_ID);
        final Date a2Date = (Date) wf.getContext().get(A2_ID);

        Assert.assertTrue(a1Date.after(a2Date));
    }


    /*
            (INI)       #1
              |
             \|/
            -----       #2
            /   \
           V    V
        (ACT)   (ACT)   #3/#4
           \     /
           V    V
            -----       #5
              |
              V
            (ACT)       #6
              |
              V
            (DEC)       #7
            /   \
           V    V
        (ACT)   (ACT)   #8/#9
           \     /
           V    V
          ( FIN )       #10

     */
    @Test
    public void testRun_Complex_Execution() {
        // TODO: TBD - Exactly as done in Javascript
    }
}
