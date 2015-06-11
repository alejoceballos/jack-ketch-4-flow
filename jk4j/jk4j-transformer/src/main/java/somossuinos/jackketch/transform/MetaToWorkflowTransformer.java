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

package somossuinos.jackketch.transform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import somossuinos.jackketch.transform.meta.MetaActionNode;
import somossuinos.jackketch.transform.meta.MetaBaseNode;
import somossuinos.jackketch.transform.meta.MetaControlFlow;
import somossuinos.jackketch.transform.meta.MetaDecisionNode;
import somossuinos.jackketch.transform.meta.MetaWorkflow;
import somossuinos.jackketch.workflow.NodeFactory;
import somossuinos.jackketch.workflow.Workflow;
import somossuinos.jackketch.workflow.executable.ContextExecutable;
import somossuinos.jackketch.workflow.node.ConditionalControlFlowNode;
import somossuinos.jackketch.workflow.node.MultipleControlFlowNode;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;
import somossuinos.jackketch.workflow.node.SingleControlFlowNode;

public class MetaToWorkflowTransformer implements Jk4flowTransformer<MetaWorkflow, Workflow> {

    @Override
    public Workflow transform(final MetaWorkflow metaWorkflow) {
        final Map<String, Node> nodesById = new HashMap<>(0);

        final String initialNodeId = metaWorkflow.getInitialNodes().get(0).getId();
        nodesById.put(
                initialNodeId,
                NodeFactory.createNode(initialNodeId, NodeType.INITIAL));

        nodesById.put(
                metaWorkflow.getFinalNodes().get(0).getId(),
                NodeFactory.createNode(metaWorkflow.getFinalNodes().get(0).getId(), NodeType.FINAL));

        final Map<String, Set<Node>> targetsByFromId = new HashMap<>(0);

        for (final MetaBaseNode forkNode : metaWorkflow.getForkNodes()) {
            nodesById.put(
                    forkNode.getId(),
                    NodeFactory.createNode(forkNode.getId(), NodeType.FORK));
            targetsByFromId.put(
                    forkNode.getId(),
                    new HashSet<Node>(0));
        }

        for (final MetaBaseNode joinNode : metaWorkflow.getJoinNodes()) {
            nodesById.put(
                    joinNode.getId(),
                    NodeFactory.createNode(joinNode.getId(), NodeType.JOIN));
        }

        final Map<String, MetaActionNode> metaExecutableById = new HashMap<>(0);

        for (final MetaActionNode actionNode : metaWorkflow.getActionNodes()) {
            nodesById.put(
                    actionNode.getId(),
                    NodeFactory.createNode(actionNode.getId(), NodeType.ACTION));
            metaExecutableById.put(
                    actionNode.getId(),
                    actionNode);
        }

        for (final MetaDecisionNode decisionNode : metaWorkflow.getDecisionNodes()) {
            nodesById.put(
                    decisionNode.getId(),
                    NodeFactory.createNode(decisionNode.getId(), NodeType.DECISION));
        }

        final Map<String, Object> instancesByName = new HashMap<>(0);

        for (final MetaControlFlow controlFlow : metaWorkflow.getControlFlows()) {
            final Node from = nodesById.get(controlFlow.getFrom());

            if (from instanceof ContextExecutable) {
                final String callback = metaExecutableById.get(from.getId()).getCallback();
                final String[] binding = callback.split(BindingSeparator.CLASS_FROM_METHOD.getSeparator());

                try {
                    final Class clz = Class.forName(binding[0]);

                    if (instancesByName.get(binding[0]) == null) {
                        instancesByName.put(binding[0], clz.newInstance());
                        ((ContextExecutable) from).setObject(clz.newInstance());
                    }

                    ((ContextExecutable) from).setObject(instancesByName.get(binding[0]));
                    ((ContextExecutable) from).setMethod(clz.getMethod(binding[1]));

                } catch (ReflectiveOperationException e) {
                    throw  new RuntimeException(e);
                }
            }

            if (from instanceof SingleControlFlowNode) {
                ((SingleControlFlowNode) from).setFlow(nodesById.get(controlFlow.getTo()));

            } else if (from instanceof MultipleControlFlowNode) {
                targetsByFromId.get(from.getId()).add(nodesById.get(controlFlow.getTo()));

            } else if (from instanceof ConditionalControlFlowNode) {

            }
        }

        for (final String forkdId : targetsByFromId.keySet()) {
            ((MultipleControlFlowNode) nodesById.get(forkdId)).setFlows(targetsByFromId.get(forkdId));
        }

        return null;
    }
}
