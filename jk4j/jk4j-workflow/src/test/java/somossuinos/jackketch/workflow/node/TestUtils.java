package somossuinos.jackketch.workflow.node;

public class TestUtils {

    public static SingleControlFlowNode createSingleCFNode(final String id) {
        return new SingleControlFlowNode(id) {
            @Override
            public NodeType getType() {
                return NodeType.ACTION;
            }

            @Override
            public NodeType[] getAllowedTypes() {
                return new NodeType[] { NodeType.ACTION, NodeType.DECISION, NodeType.FORK, NodeType.FINAL };
            }
        };
    }

    public static MultipleControlFlowNode createMultipleCFNode(final String id) {
        return new MultipleControlFlowNode(id) {
            @Override
            public NodeType getType() {
                return NodeType.FORK;
            }

            @Override
            public int getMinFlowsAllowed() {
                return 2;
            }

            @Override
            public NodeType[] getAllowedTypes() {
                return new NodeType[] { NodeType.ACTION, NodeType.DECISION };
            }
        };
    }

}
