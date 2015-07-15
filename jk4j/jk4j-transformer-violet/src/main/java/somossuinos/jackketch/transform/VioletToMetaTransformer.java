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

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import somossuinos.jackketch.transform.exception.Jk4flowTranformerException;
import somossuinos.jackketch.transform.meta.MetaActionBinding;
import somossuinos.jackketch.transform.meta.MetaActionNode;
import somossuinos.jackketch.transform.meta.MetaBaseNode;
import somossuinos.jackketch.transform.meta.MetaControlFlow;
import somossuinos.jackketch.transform.meta.MetaDecisionNode;
import somossuinos.jackketch.transform.meta.MetaFlowCondition;
import somossuinos.jackketch.transform.meta.MetaOtherwiseFlow;
import somossuinos.jackketch.transform.meta.MetaWorkflow;
import somossuinos.jackketch.transform.violet.VltEdge;
import somossuinos.jackketch.transform.violet.VltNodeItem;
import somossuinos.jackketch.transform.violet.VltNoteNode;
import somossuinos.jackketch.transform.violet.VltWorkflow;
import somossuinos.jackketch.workflow.conditional.ConditionType;

public class VioletToMetaTransformer implements Jk4flowTransformer<VltWorkflow, MetaWorkflow> {

    private static final Gson GSON = new Gson();

    @Override
    public MetaWorkflow transform(final VltWorkflow workflow) {
        final MetaWorkflow metaWorkflow = new MetaWorkflow();

        this.setInitialNodes(workflow, metaWorkflow);
        this.setForkAndJoinNodes(workflow, metaWorkflow);
        this.setActionNodes(workflow, metaWorkflow);
        this.setDecisionNodes(workflow, metaWorkflow);
        this.setFinalNodes(workflow, metaWorkflow);
        this.setControlFLows(workflow, metaWorkflow);

        return metaWorkflow;
    }

    private void setInitialNodes(final VltWorkflow workflow, final MetaWorkflow metaWorkflow) {
        for (final VltNodeItem nodeItem : workflow.getNodes().getScenarioStartNodes()) {
            metaWorkflow.getInitialNodes().add(new MetaBaseNode(nodeItem.getId()));
        }
    }

    private void setFinalNodes(final VltWorkflow workflow, final MetaWorkflow metaWorkflow) {
        for (final VltNodeItem nodeItem : workflow.getNodes().getScenarioEndNodes()) {
            metaWorkflow.getFinalNodes().add(new MetaBaseNode(nodeItem.getId()));
        }
    }

    private void setControlFLows(final VltWorkflow workflow, final MetaWorkflow metaWorkflow) {
        for (final VltEdge edge : workflow.getEdges().getTransitionEdges()) {
            metaWorkflow.getControlFlows().add(
                    new MetaControlFlow(
                            edge.getId(),
                            edge.getStart().getReference(),
                            edge.getEnd().getReference()));
        }
    }

    private void setForkAndJoinNodes(final VltWorkflow workflow, final MetaWorkflow metaWorkflow) {
        for (final VltNodeItem nodeItem : workflow.getNodes().getSynchronizationBarNodes()) {
            int nodeOutgoings = 0;
            int nodeIncomings = 0;

            for (final VltEdge transitionEdge : workflow.getEdges().getTransitionEdges()) {
                if (transitionEdge.getStart().getReference().equals(nodeItem.getId())) {
                    nodeOutgoings++;

                } else if (transitionEdge.getEnd().getReference().equals(nodeItem.getId())) {
                    nodeIncomings++;
                }
            }

            if (nodeOutgoings > 1 && nodeIncomings == 1) {
                metaWorkflow.getForkNodes().add(new MetaBaseNode(nodeItem.getId()));

            } else if (nodeOutgoings == 1 && nodeIncomings > 1) {
                metaWorkflow.getJoinNodes().add(new MetaBaseNode(nodeItem.getId()));

            } else {
                throw new Jk4flowTranformerException(
                        "SynchronizationBarNodes may be Forks or Joins. No Fork or Join can have " +
                                nodeOutgoings + " outgoing flows and " +
                                nodeIncomings + " incoming flows.");
            }
        }
    }

    private void setActionNodes(final VltWorkflow workflow, final MetaWorkflow metaWorkflow) {
        for (final VltNodeItem activityNode : workflow.getNodes().getActivityNodes()) {
            MetaActionBinding binding = null;

            for (final VltEdge noteEdge : workflow.getEdges().getNoteEdges()) {
                if (noteEdge.getEnd().getReference().equals(activityNode.getId())) {
                    for (final VltNoteNode noteNode : workflow.getNodes().getNoteNodes()) {
                        if (noteNode.getId().equals(noteEdge.getStart().getReference())) {
                            binding = GSON.fromJson(noteNode.getTextContainer().getText(), MetaActionBinding.class);
                            break;
                        }
                    }

                    break;
                }
            }

            if (binding == null) throw new Jk4flowTranformerException("An action cannot be unbound to an executable class and method");

            metaWorkflow.getActionNodes().add(new MetaActionNode(activityNode.getId(), binding.toBinding()));
        }
    }

    private void setDecisionNodes(final VltWorkflow workflow, final MetaWorkflow metaWorkflow) {
        for (final VltNodeItem nodeItem : workflow.getNodes().getDecisionNodes()) {
            MetaOtherwiseFlow otherwise = null;
            final List<MetaFlowCondition> flowConditions = new ArrayList<>(0);

            for (final VltEdge transitionEdge : workflow.getEdges().getTransitionEdges()) {
                if (transitionEdge.getStart().getReference().equals(nodeItem.getId())) {
                    final String label = transitionEdge.getMiddleLabel();

                    if (StringUtils.isBlank(label) || "otherwise".equalsIgnoreCase(label)) {
                        if (otherwise != null) throw new Jk4flowTranformerException("There cannot be two otherwise flows for the same decision. Note: empty named and \"otherwise\" named flows are both considered otherwise flows.");
                        otherwise = new MetaOtherwiseFlow(transitionEdge.getId());

                    } else {
                        MetaFlowCondition fc = null;

                        for (final ConditionType condition : ConditionType.values()) {
                            final String conditionSeparator = " " + condition.name() + " ";

                            if (label.indexOf(conditionSeparator) > 0) {
                                String[] conditionParts = label.split(conditionSeparator);

                                if (conditionParts.length == 0) {
                                    throw new Jk4flowTranformerException("No context attribute and value found in the expression.");

                                } else if (conditionParts.length == 1) {
                                    throw new Jk4flowTranformerException("Only the context attribute or the value was found in the expression. Must be both.");

                                } else if (conditionParts.length > 2) {
                                    throw new Jk4flowTranformerException("More than one condition was found in the expression. There can be only one.");
                                }

                                fc = new MetaFlowCondition(
                                        conditionParts[0].trim(),
                                        condition.name(),
                                        conditionParts[1].trim(),
                                        transitionEdge.getId());

                                break;
                            }
                        }

                        if (fc == null) {
                            throw new Jk4flowTranformerException("The condition separator must conform " + ConditionType.class + " enumerators. It also must be between the context attribute and the value, separeted by at least one blank space character.");
                        }

                        flowConditions.add(fc);
                    }
                }
            }

            metaWorkflow.getDecisionNodes().add(
                    new MetaDecisionNode(
                            nodeItem.getId(),
                            flowConditions,
                            otherwise)
            );
        }
    }

}
