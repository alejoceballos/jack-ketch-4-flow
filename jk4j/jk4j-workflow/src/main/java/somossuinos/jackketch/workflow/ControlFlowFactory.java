package somossuinos.jackketch.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import somossuinos.jackketch.workflow.controlflow.FlowCondition;
import somossuinos.jackketch.workflow.node.Node;
import somossuinos.jackketch.workflow.node.NodeType;

public class ControlFlowFactory {

    private ControlFlowFactory() {
    }

    private static void validateOrigin(final Node origin) {
        if (origin == null) {
            throw new RuntimeException("No origin was passed as argument");
        }
    }

    public static void validateTarget(final Node target) {
        if (target == null) {
            throw new RuntimeException("No target was passed as argument");
        }
    }

    private static void validateTarget(final Collection<Node> targets) {
        if (targets.contains(null)) {
            throw new RuntimeException("Null targets in target list are not accepted");
        }
    }

    private static void validateTarget(final Map<FlowCondition, Node> targets) {
        if (targets.containsKey(null)) {
            throw new RuntimeException("Null conditions in target list are not accepted");
        }

        if (targets.containsValue(null)) {
            throw new RuntimeException("Null targets in target list are not accepted");
        }
    }

    private static void validateSelfRedirection(final Node origin, final Node target) {
        if (target == origin) {
            throw new RuntimeException("Origin and target cannot be the same object");
        }
    }

    private static void validateSelfRedirection(final Node origin, final Collection<Node> targets) {
        if (targets.contains(origin)) {
            throw new RuntimeException("Repeated target instances are not allowed");
        }
    }

    private static void validateAllowedTypes(final NodeType[] allowedTypes) {
        if (ArrayUtils.isEmpty(allowedTypes)) {
            throw new RuntimeException("No allowed types were passed as argument");
        }

        // There cannot be null elements
        if (Arrays.asList(allowedTypes).contains(null)) {
            throw new RuntimeException("Null types in allowed types list are not accepted");
        }

        // Array cannot have repeated elements
        if (allowedTypes.length > 1) {
            Arrays.sort(allowedTypes);
            for(int i = 1; i < allowedTypes.length; i++) {
                if (Arrays.binarySearch(allowedTypes, i, allowedTypes.length - 1, allowedTypes[i - 1]) >= 0) {
                    throw new RuntimeException("Repeated types in allowed types list are not accepted");
                }
            }
        }
    }

    private static void validateTargetNodeType(final Node target, final NodeType[] allowedTypes) {
        // All elements must be of a valid node type defined by the allowed type set
        final List<NodeType> typesList = Arrays.asList(allowedTypes);
        if (!typesList.contains(target.getType())) {
            throw new RuntimeException(String.format("Node type %s is not accepted", target.getType().name()));
        }
    }

    private static void validateTargetNodeType(final Collection<Node> targets, final NodeType[] allowedTypes) {
        // All elements must be of a valid node type defined by the allowed type set
        final List<NodeType> typesList = Arrays.asList(allowedTypes);
        for (final Node target: targets) {
            if (!typesList.contains(target.getType())) {
                throw new RuntimeException(String.format("Node type %s is not accepted in targets list", target.getType().name()));
            }
        }
    }

    private static void validateTargetListSize(final Collection<Node> targets, final int minListSize) {
        if (targets.size() < minListSize) {
            throw new RuntimeException(String.format("Targets list must have at least %s element(s)", minListSize));
        }
    }

    private static void validateRepeatedTargets(final Collection<Node> targets) {
        final List<Node> nodes = new ArrayList<>(targets.size());
        nodes.addAll(targets);

        // There cannot be repeated instances of the same object
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (nodes.get(i) == nodes.get(j)) {
                    throw new RuntimeException(("Targets list must not repeat node instances"));
                }
            }
        }

    }

    public static Node create(final Node origin, final Node target, final NodeType[] allowedTypes) {
        validateOrigin(origin);
        validateTarget(target);
        validateSelfRedirection(origin, target);
        validateAllowedTypes(allowedTypes);
        validateTargetNodeType(target, allowedTypes);

        return target;
    }

    public static Set<Node> create(final Node origin, final Set<Node> targets, final NodeType[] allowedTypes, final int minListSize) {
        validateOrigin(origin);
        validateTarget(targets);
        validateSelfRedirection(origin, targets);
        validateAllowedTypes(allowedTypes);
        validateTargetNodeType(targets, allowedTypes);
        validateTargetListSize(targets, minListSize);

        final Set<Node> flows = new HashSet<>(targets.size());
        flows.addAll(targets);

        return flows;
    }

    public static Map<FlowCondition, Node> create(final Node origin, final Map<FlowCondition, Node> targets, final NodeType[] allowedTypes, final int minListSize) {
        validateOrigin(origin);
        validateTarget(targets);
        validateSelfRedirection(origin, targets.values());
        validateAllowedTypes(allowedTypes);
        validateTargetNodeType(targets.values(), allowedTypes);
        validateTargetListSize(targets.values(), minListSize);
        validateRepeatedTargets(targets.values());

        final Map<FlowCondition, Node> flows = new HashMap<>(targets.size());
        flows.putAll(targets);

        return flows;
    }
}
