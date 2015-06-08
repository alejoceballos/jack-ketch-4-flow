package somossuinos.jackketch.transform;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import somossuinos.jackketch.transform.meta.MetaActionNode;
import somossuinos.jackketch.transform.meta.MetaBaseNode;
import somossuinos.jackketch.transform.meta.MetaControlFlow;
import somossuinos.jackketch.transform.meta.MetaDecisionNode;
import somossuinos.jackketch.transform.meta.MetaFlowCondition;
import somossuinos.jackketch.transform.meta.MetaOtherwiseFlow;
import somossuinos.jackketch.transform.meta.MetaWorkflow;

public class Jk4flowTransformerTest {

    private StringBuilder json = new StringBuilder()
        .append("{                                                                                        ")
        .append("     initialNodes: [                                                                     ")
        .append("         { id: '#IN:0001' }                                                              ")
        .append("     ],                                                                                  ")
        .append("     forkNodes: [                                                                        ")
        .append("         { id: '#FN:0002' }                                                              ")
        .append("     ],                                                                                  ")
        .append("     actionNodes: [                                                                      ")
        .append("         { id: '#AN:0003', callback: 'SimpleDiagram::sumOnePlusOne' },                   ")
        .append("         { id: '#AN:0004', callback: 'SimpleDiagram::sumTwoPlusTwo' },                   ")
        .append("         { id: '#AN:0006', callback: 'SimpleDiagram::sumXPlusY' },                       ")
        .append("         { id: '#AN:0008', callback: 'SimpleDiagram::showOk' },                          ")
        .append("         { id: '#AN:0009', callback: 'SimpleDiagram::showNotOk' }                        ")
        .append("     ],                                                                                  ")
        .append("     joinNodes: [                                                                        ")
        .append("         { id: '#JN:0005' }                                                              ")
        .append("     ],                                                                                  ")
        .append("     decisionNodes: [                                                                    ")
        .append("         {                                                                               ")
        .append("             id: '#DN:0007',                                                             ")
        .append("             flowConditions: [                                                           ")
        .append("                 { attribute: 'v', condition: 'EQ', value: '6', controlFlow: '#CF:0008' }")
        .append("             ],                                                                          ")
        .append("             otherwise: { controlFlow: '#CF:0009' }                                      ")
        .append("         }                                                                               ")
        .append("     ],                                                                                  ")
        .append("     finalNodes: [                                                                       ")
        .append("         { id: '#FN:0021' }                                                              ")
        .append("     ],                                                                                  ")
        .append("     controlFlows: [                                                                     ")
        .append("         { id: '#CF:0001', from: '#IF:0001', to: '#FN:0002' },                           ")
        .append("         { id: '#CF:0002', from: '#FN:0002', to: '#AN:0003' },                           ")
        .append("         { id: '#CF:0003', from: '#FN:0002', to: '#AN:0004' },                           ")
        .append("         { id: '#CF:0004', from: '#AN:0003', to: '#JN:0005' },                           ")
        .append("         { id: '#CF:0005', from: '#AN:0004', to: '#JN:0005' },                           ")
        .append("         { id: '#CF:0006', from: '#JN:0005', to: '#AN:0006' },                           ")
        .append("         { id: '#CF:0007', from: '#AN:0006', to: '#DN:0007' },                           ")
        .append("         { id: '#CF:0008', from: '#DN:0007', to: '#AN:0008' },                           ")
        .append("         { id: '#CF:0009', from: '#DN:0007', to: '#AN:0009' },                           ")
        .append("         { id: '#CF:0010', from: '#AN:0008', to: '#FN:0010' },                           ")
        .append("         { id: '#CF:0011', from: '#AN:0009', to: '#FN:0010' }                            ")
        .append("     ]                                                                                   ")
        .append("}                                                                                        ");

    private Gson gson = new Gson();

    @Test
    public void testGson_TransformationBetweenStringAndObjectRepresentation() {
        final List<MetaBaseNode> iNodes = new ArrayList<>(1);
        iNodes.add(new MetaBaseNode("#IN:0001"));

        final List<MetaBaseNode> fNodes = new ArrayList<>(1);
        fNodes.add(new MetaBaseNode("#FN:0002"));

        final List<MetaActionNode> aNodes = new ArrayList<>(5);
        aNodes.add(new MetaActionNode("#AN:0003", "SimpleDiagram::sumOnePlusOne"));
        aNodes.add(new MetaActionNode("#AN:0004", "SimpleDiagram::sumTwoPlusTwo"));
        aNodes.add(new MetaActionNode("#AN:0006", "SimpleDiagram::sumXPlusY"));
        aNodes.add(new MetaActionNode("#AN:0008", "SimpleDiagram::showOk"));
        aNodes.add(new MetaActionNode("#AN:0009", "SimpleDiagram::showNotOk"));

        final List<MetaBaseNode> jNodes = new ArrayList<>(1);
        jNodes.add(new MetaBaseNode("#JN:0005"));

        final List<MetaFlowCondition> fc = new ArrayList<>();
        fc.add(new MetaFlowCondition("v", "EQ", "6", "#CF:0008"));

        final List<MetaDecisionNode> dNodes = new ArrayList<>(1);
        dNodes.add(new MetaDecisionNode("#DN:0007", fc, new MetaOtherwiseFlow("#CF:0009")));

        final List<MetaBaseNode> finNodes = new ArrayList<>(1);
        finNodes.add(new MetaBaseNode("#FN:0021"));

        final List<MetaControlFlow> cfNodes = new ArrayList<>(1);
        cfNodes.add(new MetaControlFlow("#CF:0001", "#IF:0001", "#FN:0002"));
        cfNodes.add(new MetaControlFlow("#CF:0002", "#FN:0002", "#AN:0003"));
        cfNodes.add(new MetaControlFlow("#CF:0003", "#FN:0002", "#AN:0004"));
        cfNodes.add(new MetaControlFlow("#CF:0004", "#AN:0003", "#JN:0005"));
        cfNodes.add(new MetaControlFlow("#CF:0005", "#AN:0004", "#JN:0005"));
        cfNodes.add(new MetaControlFlow("#CF:0006", "#JN:0005", "#AN:0006"));
        cfNodes.add(new MetaControlFlow("#CF:0007", "#AN:0006", "#DN:0007"));
        cfNodes.add(new MetaControlFlow("#CF:0008", "#DN:0007", "#AN:0008"));
        cfNodes.add(new MetaControlFlow("#CF:0009", "#DN:0007", "#AN:0009"));
        cfNodes.add(new MetaControlFlow("#CF:0010", "#AN:0008", "#FN:0010"));
        cfNodes.add(new MetaControlFlow("#CF:0011", "#AN:0009", "#FN:0010"));

        final MetaWorkflow wf = new MetaWorkflow();
        wf.setInitialNodes(iNodes);
        wf.setForkNodes(fNodes);
        wf.setActionNodes(aNodes);
        wf.setJoinNodes(jNodes);
        wf.setDecisionNodes(dNodes);
        wf.setFinalNodes(finNodes);
        wf.setControlFlows(cfNodes);

        final String jsonFromObj = gson.toJson(wf);

        final MetaWorkflow wf2 = gson.fromJson(this.json.toString(), MetaWorkflow.class);
        final String jsonFromStr = gson.toJson(wf2);

        Assert.assertEquals(jsonFromStr, jsonFromObj);
    }

}
