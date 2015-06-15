package somossuinos.jackketch.transform.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import somossuinos.jackketch.transform.Jk4flowTransformer;
import somossuinos.jackketch.transform.VioletToMetaTransformer;
import somossuinos.jackketch.transform.XmlToVioletTransformer;
import somossuinos.jackketch.transform.meta.MetaWorkflow;
import somossuinos.jackketch.transform.violet.VltWorkflow;

public class VioletToMetaTransformerTest {

    private static final Jk4flowTransformer<String, VltWorkflow> TRANSFORMER_FROM_XML = new XmlToVioletTransformer();
    private static final Jk4flowTransformer<VltWorkflow, MetaWorkflow> TRANSFORMER_FROM_VLT = new VioletToMetaTransformer();

    private MetaWorkflow workflow;

    @Before
    public void before() {
        final VltWorkflow vltWorkflow = TRANSFORMER_FROM_XML.transform(ActivityDiagramXml.getXml());
        workflow = TRANSFORMER_FROM_VLT.transform(vltWorkflow);
    }

    @Test
    public void testTransform() {
        Assert.assertNotNull(workflow);
    }

}
