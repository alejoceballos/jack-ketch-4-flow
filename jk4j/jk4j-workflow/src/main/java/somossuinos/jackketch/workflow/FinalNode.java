package somossuinos.jackketch.workflow;

import somossuinos.jackketch.workflow.node.AbstractNode;
import somossuinos.jackketch.workflow.node.NodeType;

/**
 * Establishes the end of the flow.
 *
 * At first I thought this node wasn't really necessary, for example, if I just reach a last
 * Action Node (without outgoing control flow) the flow should be terminated too. But the Final
 * Node, besides establishing a formal end to our workflow, should allow the return of the
 * context flow object to the programming structure that started it. So I decided to make it
 * mandatory, and to simplify (my life, of course) I also insist that it shall be unique (there
 * can be only one!).
 *
 * Basic Rules: (1) Many as possible flows coming into; (2) No flow going out.
 */
public class FinalNode extends AbstractNode {

    public FinalNode(String id) {
        super(id);
    }

    @Override
    public NodeType getType() {
        return NodeType.FINAL;
    }

}
