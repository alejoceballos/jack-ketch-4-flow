package somossuinos.jackketch.engine;

import somossuinos.jackketch.workflow.node.ActionNode;
import somossuinos.jackketch.workflow.Workflow;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

public class Executor {

    public Executor(final Workflow workflow) {
    }

    public void run() {

    }

    public void handle(final Node node) {
        Node currentNode = node;

        while(!NodeType.FINAL.equals(currentNode.getType())) {
            switch(currentNode.getType()) {
                case INITIAL:
                    currentNode = ((SingleControlFlowNode) currentNode).getFlow();
                    break;
                case JOIN:
                    currentNode = ((SingleControlFlowNode) currentNode).getFlow();
                    break;
                case ACTION:
                    final ActionNode an = (ActionNode) currentNode;
                    // TODO: Do something regarding the action

            }
        }
    }
}
