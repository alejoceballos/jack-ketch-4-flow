package somossuinos.jackketch.workflow.context;

import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class WorkflowContextFactoryTest {

    @Test
    public void testCreate() {
        Assert.assertNotNull(WorkflowContextFactory.create());
    }

    @Test
    public void testCreateBasic() {
        Assert.assertNotNull(WorkflowContextFactory.create());
    }

    @Test
    public void testSetAndGet_Workflow() {
        final WorkflowContext wfc = WorkflowContextFactory.create();
        wfc.set("KEY", "VALUE");

        Assert.assertEquals("VALUE", wfc.get("KEY"));
    }

    @Test
    public void testSetAndGet_Workflow_With_Null_Value() {
        final WorkflowContext wfc = WorkflowContextFactory.create();
        wfc.set("KEY", "VALUE");
        wfc.set("KEY", null);

        Assert.assertEquals("VALUE", wfc.get("KEY"));
    }

    @Test
    public void testGetMap_Workflow_Is_A_Clone() {
        final WorkflowContext wfc = WorkflowContextFactory.create();
        wfc.set("KEY", "VALUE");

        final Map map1 = wfc.getMap();
        map1.put("KEY", "VALUE2");

        Assert.assertEquals("VALUE", wfc.get("KEY"));
    }

    @Test
    public void testClear_Workflow() {
        final WorkflowContext wfc = WorkflowContextFactory.create();
        wfc.set("KEY", "VALUE");
        wfc.clear();

        final Map map = wfc.getMap();


        Assert.assertTrue(map.size() == 0);
    }

}
