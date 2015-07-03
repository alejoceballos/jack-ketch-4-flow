package somossuinos.jackketch.webclient.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;
import somossuinos.jackketch.transform.MetaToJsonTransformer;
import somossuinos.jackketch.transform.VioletToMetaTransformer;
import somossuinos.jackketch.transform.XmlToVioletTransformer;
import somossuinos.jackketch.transform.meta.MetaWorkflow;
import somossuinos.jackketch.transform.violet.VltWorkflow;

@Component
@Path("transform")
public class TransformerController {

    private static final XmlToVioletTransformer XML_TO_VIOLET = new XmlToVioletTransformer();
    private static final VioletToMetaTransformer VIOLET_TO_META = new VioletToMetaTransformer();
    private static final MetaToJsonTransformer META_TO_JSON_TRANSFORMER = new MetaToJsonTransformer();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String transform(final String from) {
        final VltWorkflow vWf = XML_TO_VIOLET.transform(from);
        final MetaWorkflow mWf = VIOLET_TO_META.transform(vWf);

        return META_TO_JSON_TRANSFORMER.transform(mWf);
    }

}
