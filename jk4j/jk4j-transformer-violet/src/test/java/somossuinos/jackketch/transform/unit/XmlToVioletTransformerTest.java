package somossuinos.jackketch.transform.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.transform.Jk4flowTransformer;
import somossuinos.jackketch.transform.XmlToVioletTransformer;
import somossuinos.jackketch.transform.violet.VltWorkflow;

public class XmlToVioletTransformerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final Jk4flowTransformer<String, VltWorkflow> TRANSFORMER = new XmlToVioletTransformer();

    private VltWorkflow workflow;

    @Before
    public void before() {
        this.workflow = TRANSFORMER.transform(ActivityDiagramXml.getXml());
    }

    @Test
    public void testTransform_Null_Xml_Fail() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("XML should not be blank");

        TRANSFORMER.transform(null);
    }

    @Test
    public void testTransform_Blank_Xml_Fail() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("XML should not be blank");

        TRANSFORMER.transform(" ");
    }

    @Test
    public void testTransform_Not_Null() {
        Assert.assertNotNull(workflow);
    }

    @Test
    public void testTransform_Validate_Workflow() {
        workflow.validate();
    }

    @Test
    public void testTransform_Validate_Nodes() {
        workflow.getNodes();
    }

    @Test
    public void testTransform_Validate_Edges() {
        workflow.getEdges().validate();
    }

    @Test
    public void testTransform_ScenarioStartNode() {
        Assert.assertEquals(1, workflow.getNodes().getScenarioStartNodes().size());
    }

    @Test
    public void testTransform_Validate_ScenarioStartNode_Without_Id() {
        Assert.fail("Not implemented yet");
    }
    @Test
    public void testTransform_SynchronizationBarNode() {
        Assert.assertEquals(2, workflow.getNodes().getSynchronizationBarNodes().size());
    }

    @Test
    public void testTransform_ActivityNode() {
        Assert.assertEquals(5, workflow.getNodes().getActivityNodes().size());
    }

    @Test
    public void testTransform_DecisionNode() {
        Assert.assertEquals(1, workflow.getNodes().getDecisionNodes().size());
    }

    @Test
    public void testTransform_NoteNode() {
        Assert.assertEquals(5, workflow.getNodes().getNoteNodes().size());
    }

    @Test
    public void testTransform_ScenarioEndNode() {
        Assert.assertEquals(1, workflow.getNodes().getScenarioEndNodes().size());
    }

}
