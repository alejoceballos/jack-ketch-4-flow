package somossuinos.jackketch.engine;

import org.junit.Assert;
import org.junit.Test;
import somossuinos.jackketch.utils.ExecutableDummy;
import somossuinos.jackketch.workflow.NodeFactory;
import somossuinos.jackketch.workflow.Workflow;
import somossuinos.jackketch.workflow.executable.ContextExecutable;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

public class ExecutorIT {

    /**
     *    (>)
     *     |
     *     V
     *   [___]
     *     |
     *     V
     *    (*)
     */
    @Test
    public void testRun_Initial_Action_Final() {
        final Node fn = NodeFactory.createNode("#3", NodeType.FINAL);

        final Node an = NodeFactory.createNode("#2", NodeType.ACTION);
        ((SingleControlFlowNode) an).setFlow(fn);
        final ExecutableDummy obj = ExecutableDummy.create();
        ((ContextExecutable) an).setObject(obj);
        ((ContextExecutable) an).setMethod(obj.getExecutableMethod());

        final Node in = NodeFactory.createNode("#1", NodeType.INITIAL);
        ((SingleControlFlowNode) in).setFlow(an);

        final Workflow wf = Workflow.create(in);

        (new Executor(wf)).run();

        Assert.assertEquals(ExecutableDummy.VALUE, wf.getContext().get(ExecutableDummy.KEY));
    }

}
