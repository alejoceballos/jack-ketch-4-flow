package somossuinos.jackketch.webclient;

import java.io.File;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import somossuinos.jackketch.engine.Jk4flowEngine;
import somossuinos.jackketch.transform.reader.Jk4flowReader;
import somossuinos.jackketch.transform.reader.VioletReader;
import somossuinos.jackketch.webclient.api.ApiResult;
import somossuinos.jackketch.webclient.api.ApiResultStatusType;
import somossuinos.jackketch.webclient.transformer.Transformers;
import somossuinos.jackketch.workflow.Jk4flowWorkflow;
import somossuinos.jackketch.workflow.context.WorkflowContext;
import somossuinos.jackketch.workflow.context.WorkflowContextFactory;

@Named
@Path("transform")
public class TransformerEntryPoint {

    private static final Jk4flowReader READER = new VioletReader();
    private static final Jk4flowEngine ENGINE = new Jk4flowEngine();
    private static Jk4flowWorkflow WORKFLOW;

    @PostConstruct
    public void init() {
        final String xml = READER.read(new File("tranform-violet-to-json.activity.violet.html"));
        WORKFLOW =
                Transformers.META_TO_WORKFLOW.transform(
                        Transformers.VIOLET_TO_META.transform(
                                Transformers.XML_TO_VIOLET.transform(xml)));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult transform(final String from) {
        final WorkflowContext context = WorkflowContextFactory.create();
        ApiResult result;

        try {
            ENGINE.run(WORKFLOW, context);
            result = ApiResult.build(ApiResultStatusType.SUCCESS).withData(context.get("META_JSON"));

        } catch (RuntimeException e) {
            result = ApiResult.build(ApiResultStatusType.EXCEPTION).withData(e);
        }

        return result;
    }

}
