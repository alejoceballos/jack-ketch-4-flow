package somossuinos.jackketch.transform.integration;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.transform.Jk4flowTransformer;
import somossuinos.jackketch.transform.XmlToVioletTransformer;
import somossuinos.jackketch.transform.reader.Jk4flowReader;
import somossuinos.jackketch.transform.reader.VioletReader;
import somossuinos.jackketch.transform.violet.VltWorkflow;

public class VioletToMetaIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValidateEmptyDiagramFails() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/empty-diagram.activity.violet.html"));

        final Jk4flowTransformer<String, VltWorkflow> transformer = new XmlToVioletTransformer();
        final VltWorkflow workflow = transformer.transform(text);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Scenario START node rule is \"there must be one and only one in the whole diagram\".");

        workflow.validate();
    }

    @Test
    public void testValidateNoStartNodeFails() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/no-start.activity.violet.html"));

        final Jk4flowTransformer<String, VltWorkflow> transformer = new XmlToVioletTransformer();
        final VltWorkflow workflow = transformer.transform(text);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Scenario START node rule is \"there must be one and only one in the whole diagram\".");

        workflow.validate();
    }

    @Test
    public void testValidateMoreThanOneStartNodeFails() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/multiple-start.activity.violet.html"));

        final Jk4flowTransformer<String, VltWorkflow> transformer = new XmlToVioletTransformer();
        final VltWorkflow workflow = transformer.transform(text);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Scenario START node rule is \"there must be one and only one in the whole diagram\".");

        workflow.validate();
    }

    @Test
    public void testValidateNoEndNodeFails() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/no-end.activity.violet.html"));

        final Jk4flowTransformer<String, VltWorkflow> transformer = new XmlToVioletTransformer();
        final VltWorkflow workflow = transformer.transform(text);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Scenario END node rule is \"there must be one and only one in the whole diagram\".");

        workflow.validate();
    }

    @Test
    public void testValidateMoreThanOneEndNodeFails() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/multiple-end.activity.violet.html"));

        final Jk4flowTransformer<String, VltWorkflow> transformer = new XmlToVioletTransformer();
        final VltWorkflow workflow = transformer.transform(text);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Scenario END node rule is \"there must be one and only one in the whole diagram\".");

        workflow.validate();
    }

    @Test
    public void testValidateOddNumberOfSynchronizationBarFails() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/odd-number-synch-bar.activity.violet.html"));

        final Jk4flowTransformer<String, VltWorkflow> transformer = new XmlToVioletTransformer();
        final VltWorkflow workflow = transformer.transform(text);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Synchronization bar node represent both Forks and Joins, there must be pairs of them.");

        workflow.validate();
    }

    @Test
    public void testValidateActivityNodeFails() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/only-start-end-node.activity.violet.html"));

        final Jk4flowTransformer<String, VltWorkflow> transformer = new XmlToVioletTransformer();
        final VltWorkflow workflow = transformer.transform(text);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("For a workflow to make sense, it should have at least one START node, one ACTION node bound to an executable object and one END node.");

        workflow.validate();
    }
}
