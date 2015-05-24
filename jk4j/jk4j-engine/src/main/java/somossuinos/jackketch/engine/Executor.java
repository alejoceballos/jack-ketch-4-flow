package somossuinos.jackketch.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    protected Node handle(final Node node) {
        Node currentNode = node;

        while(!NodeType.FINAL.equals(currentNode.getType())) {
            if (currentNode instanceof ContextExecutable) {
                ((ContextExecutable) currentNode).execute(this.workflow.getContext());
            }

            if (currentNode instanceof SingleControlFlowNode) {
                currentNode = ((SingleControlFlowNode) currentNode).getFlow();

                if (NodeType.JOIN.equals(currentNode.getType())) {
                    break; // Exits while
                }

            } else if (currentNode instanceof ConditionalControlFlowNode) {
                currentNode = ((ConditionalControlFlowNode) currentNode).getFlow(this.workflow.getContext());

            } else if (currentNode instanceof MultipleControlFlowNode) {
                currentNode = this.fork((MultipleControlFlowNode) currentNode);
            }
        }

        return currentNode;
    }

    private Node fork(final MultipleControlFlowNode fork) {
        final ExecutorService executorService = Executors.newFixedThreadPool(fork.getFlows().size());
        final List<Callable<Node>> listToBeExecuted = new ArrayList<>(fork.getFlows().size());

        for (final Node node : fork.getFlows()) {
            listToBeExecuted.add(
                    new Callable<Node>() {
                        @Override
                        public Node call() throws Exception {
                            return handle(node);
                        }
                    }
            );
        }

        try {
            final List<Future<Node>> resultList = executorService.invokeAll(listToBeExecuted);

            for (int i = 0; i < resultList.size(); i++) {
                final Node current = resultList.get(i).get();

                if (!NodeType.JOIN.equals(current.getType())) {
                    throw new RuntimeException(String.format("Fork executions should end on join nodes, but ended on a %. Node id: %", current.getType().name(), current.getId()));
                }

                if (i > 0) {
                    final Node previous = resultList.get(i - 1).get();

                    if (current != previous) {
                        throw new RuntimeException(String.format("Not all forked flows ended in the same join node. Nodes' id: % and %", previous.getId(), current.getId()));
                    }
                }
            }

            return resultList.get(0).get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
