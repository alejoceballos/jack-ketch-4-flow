package somossuinos.jackketch.engine;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.workflow.Workflow;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

public class ExecutorTest {

    private final static String ID = "#ID";

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

}
