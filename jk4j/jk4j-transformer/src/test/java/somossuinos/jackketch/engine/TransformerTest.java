package somossuinos.jackketch.engine;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import somossuinos.jackketch.transform.model.json.JsonActionNode;
import somossuinos.jackketch.transform.model.json.JsonBaseNode;
import somossuinos.jackketch.transform.model.json.JsonWorkflow;

public class TransformerTest {

    private StringBuilder json = new StringBuilder()
        .append("{                                                                                       ")
        .append("     initialNodes: [                                                                    ")
        .append("         { id: '#IN:0001' }                                                             ")
        .append("     ],                                                                                 ")
        .append("     forkNodes: [                                                                       ")
        .append("         { id: '#FN:0002' }                                                             ")
        .append("     ],                                                                                 ")
        .append("     actionNodes: [                                                                     ")
        .append("         { id: '#AN:0003', callback: 'SimpleDiagram::sumOnePlusOne' },                  ")
        .append("         { id: '#AN:0004', callback: 'SimpleDiagram::sumTwoPlusTwo' },                  ")
        .append("         { id: '#AN:0006', callback: 'SimpleDiagram::sumXPlusY' },                      ")
        .append("         { id: '#AN:0008', callback: 'SimpleDiagram::showOk' },                         ")
        .append("         { id: '#AN:0009', callback: 'SimpleDiagram::showNotOk' }                       ")
        .append("     ],                                                                                 ")
        .append("     joinNodes: [                                                                       ")
        .append("         { id: '#JN:0005' }                                                             ")
        .append("     ],                                                                                 ")
        .append("     decisionNodes: [                                                                   ")
        .append("         {                                                                              ")
        .append("             id: '#DN:0007',                                                            ")
        .append("             context: [                                                                 ")
        .append("                 { attribute: 'v', condition: '=', value: '6', controlFlow: '#CF:0008' }")
        .append("             ],                                                                         ")
        .append("             otherwise: { controlFlow: '#CF:0009' }                                     ")
        .append("         }                                                                              ")
        .append("     ],                                                                                 ")
        .append("     finalNodes: [                                                                      ")
        .append("         { id: '#FN:0021' }                                                             ")
        .append("     ],                                                                                 ")
        .append("     controlFlows: [                                                                    ")
        .append("         { id: '#CF:0001', from: '#IF:0001', to: '#FN:0002' },                          ")
        .append("         { id: '#CF:0002', from: '#FN:0002', to: '#AN:0003' },                          ")
        .append("         { id: '#CF:0003', from: '#FN:0002', to: '#AN:0004' },                          ")
        .append("         { id: '#CF:0004', from: '#AN:0003', to: '#JN:0005' },                          ")
        .append("         { id: '#CF:0005', from: '#AN:0004', to: '#JN:0005' },                          ")
        .append("         { id: '#CF:0006', from: '#JN:0005', to: '#AN:0006' },                          ")
        .append("         { id: '#CF:0007', from: '#AN:0006', to: '#DN:0007' },                          ")
        .append("         { id: '#CF:0008', from: '#DN:0007', to: '#AN:0008' },                          ")
        .append("         { id: '#CF:0009', from: '#DN:0007', to: '#AN:0009' },                          ")
        .append("         { id: '#CF:0010', from: '#AN:0008', to: '#FN:0010' },                          ")
        .append("         { id: '#CF:0011', from: '#AN:0009', to: '#FN:0010' }                           ")
        .append("     ]                                                                                  ")
        .append("}                                                                                      ");

    private Gson gson = new Gson();

    @Test
    public void testGson_Workflow_Initial() {
        final List<JsonBaseNode> iNodes = new ArrayList<>(1);
        iNodes.add(new JsonBaseNode("#IN:0001"));

        final List<JsonBaseNode> fNodes = new ArrayList<>(1);
        fNodes.add(new JsonBaseNode("#IN:0002"));

        final List<JsonActionNode> aNodes = new ArrayList<>(2);
        aNodes.add(new JsonActionNode("#AN:0003", "SimpleDiagram::sumOnePlusOne"));
        aNodes.add(new JsonActionNode("#AN:0004", "SimpleDiagram::sumTwoPlusTwo"));
        aNodes.add(new JsonActionNode("#AN:0006", "SimpleDiagram::sumXPlusY"));
        aNodes.add(new JsonActionNode("#AN:0008", "SimpleDiagram::showOk"));
        aNodes.add(new JsonActionNode("#AN:0009", "SimpleDiagram::showNotOk"));

        final List<JsonBaseNode> jNodes = new ArrayList<>(1);
        jNodes.add(new JsonBaseNode("#IN:0005"));

        final JsonWorkflow wf = new JsonWorkflow();
        wf.setInitialNodes(iNodes);
        wf.setForkNodes(fNodes);
        wf.setActionNodes(aNodes);
        wf.setJoinNodes(jNodes);

        final String json = gson.toJson(wf);

        final JsonWorkflow wf2 = gson.fromJson(json, JsonWorkflow.class);
        final String json2 = gson.toJson(wf2);

        Assert.assertEquals(json, json2);
    }

}
