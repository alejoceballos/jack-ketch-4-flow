package somossuinos.jackketch.engine;

import somossuinos.jackketch.workflow.Workflow;
import somossuinos.jackketch.workflow.executable.ContextExecutable;
import somossuinos.jackketch.workflow.node.ConditionalControlFlowNode;
import somossuinos.jackketch.workflow.node.MultipleControlFlowNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

public class Executor {

    private Workflow workflow;

    public Executor(final Workflow workflow) {
        if (workflow == null) {
            throw new RuntimeException("Workflow cannot be null");
        }

        this.workflow = workflow;
    }

    public void run() {
        this.handle(this.workflow.getInitialNode());
    }

    protected void handle(final Node node) {
        Node currentNode = node;

        while(!NodeType.FINAL.equals(currentNode.getType())) {

            if (currentNode instanceof ContextExecutable) {
                ((ContextExecutable) currentNode).execute(this.workflow.getContext());
            }

            if (currentNode instanceof SingleControlFlowNode) {
                if (NodeType.JOIN.equals(currentNode.getType())) {
                    // TODO: Join threaded flows
                }

                currentNode = ((SingleControlFlowNode) currentNode).getFlow();

            } else if (currentNode instanceof ConditionalControlFlowNode) {
                currentNode = ((ConditionalControlFlowNode) currentNode).getFlow(this.workflow.getContext());

            } else if (currentNode instanceof MultipleControlFlowNode) {
                // TODO: Fork flows as threads
            }

        }
    }
}
