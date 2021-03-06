/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Alejo Ceballos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package somossuinos.jackketch.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import somossuinos.jackketch.engine.exception.Jk4flowEngineException;
import somossuinos.jackketch.workflow.Jk4flowWorkflow;
import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.executable.ContextExecutable;
import somossuinos.jackketch.workflow.node.ConditionalControlFlowNode;
import somossuinos.jackketch.workflow.node.MultipleControlFlowNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

/**
 * The engine is reponsible for for running a workflow execution and attaching it (whenever
 * possible) to an execution context.
 */
public class Jk4flowEngine {

    public Jk4flowEngine() { }

    public void run(final Jk4flowWorkflow workflow) {
        if (workflow == null) {
            throw new Jk4flowEngineException("Workflow cannot be null");
        }

        this.run(workflow, null);
    }

    public void run(final Jk4flowWorkflow workflow, final WorkflowContext context) {
        if (workflow == null) {
            throw new Jk4flowEngineException("Workflow cannot be null");
        }

        this.handle(workflow.getInitialNode(), context);
    }

    private Node handle(final Node node, final WorkflowContext context) {
        Node currentNode = node;

        while(!NodeType.FINAL.equals(currentNode.getType())) {
            if (currentNode instanceof ContextExecutable) {
                ((ContextExecutable) currentNode).execute(context);
            }

            if (currentNode instanceof SingleControlFlowNode) {
                currentNode = ((SingleControlFlowNode) currentNode).getFlow();

                if (NodeType.JOIN.equals(currentNode.getType())) {
                    break; // Exits while
                }

            } else if (currentNode instanceof ConditionalControlFlowNode) {
                currentNode = ((ConditionalControlFlowNode) currentNode).getFlow(context);

            } else if (currentNode instanceof MultipleControlFlowNode) {
                currentNode = this.fork((MultipleControlFlowNode) currentNode, context);
            }
        }

        return currentNode;
    }

    private Node fork(final MultipleControlFlowNode fork, final WorkflowContext context) {
        final ExecutorService pool = Executors.newFixedThreadPool(fork.getFlows().size());
        final List<Callable<Node>> listToBeExecuted = new ArrayList<>(fork.getFlows().size());

        for (final Node node : fork.getFlows()) {
            listToBeExecuted.add(
                    new Callable<Node>() {
                        @Override
                        public Node call() throws Exception {
                            return handle(node, context);
                        }
                    }
            );
        }

        try {
            final List<Future<Node>> resultList = pool.invokeAll(listToBeExecuted);

            for (int i = 0; i < resultList.size(); i++) {
                final Node current = resultList.get(i).get();

                if (!NodeType.JOIN.equals(current.getType())) {
                    throw new Jk4flowEngineException(String.format("Fork executions should end on join nodes, but ended on a %. Node id: %", current.getType().name(), current.getId()));
                }

                if (i > 0) {
                    final Node previous = resultList.get(i - 1).get();

                    if (current != previous) {
                        throw new Jk4flowEngineException(String.format("Not all forked flows ended in the same join node. Nodes' id: % and %", previous.getId(), current.getId()));
                    }
                }
            }

            return resultList.get(0).get();

        } catch (InterruptedException e) {
            throw new Jk4flowEngineException(e);

        } catch (ExecutionException e) {
            throw new Jk4flowEngineException(e);

        } finally {
            pool.shutdown();
        }

    }
}
