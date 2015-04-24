package somossuinos.jackketch.workflow;

import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

public class NodeFactory {
    private NodeFactory() {
    }

    public static Node createNode(final String id, final NodeType type) {
        if (type == null) {
            throw new RuntimeException("Must a node type to create a node");
        }

        final Node node;

        switch (type) {
            case INITIAL:
                node = new InitialNode(id);
                break;
            case ACTION:
                node = new ActionNode(id);
                break;
            case JOIN:
                node = new JoinNode(id);
                break;
            case FORK:
                node = new ForkNode(id);
                break;
            case DECISION:
                node = new DecisionNode(id);
                break;
            default:
                node = new FinalNode(id);
                break;
        }

        return node;
    }
}
