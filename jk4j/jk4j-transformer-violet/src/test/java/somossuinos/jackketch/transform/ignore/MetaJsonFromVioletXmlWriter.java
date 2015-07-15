package somossuinos.jackketch.transform.ignore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Ignore;
import somossuinos.jackketch.transform.Jk4flowTransformer;
import somossuinos.jackketch.transform.MetaToJsonTransformer;
import somossuinos.jackketch.transform.VioletToMetaTransformer;
import somossuinos.jackketch.transform.XmlToVioletTransformer;
import somossuinos.jackketch.transform.exception.Jk4flowTranformerException;
import somossuinos.jackketch.transform.meta.MetaWorkflow;
import somossuinos.jackketch.transform.reader.Jk4flowReader;
import somossuinos.jackketch.transform.reader.VioletReader;
import somossuinos.jackketch.transform.violet.VltWorkflow;

@Ignore
public class MetaJsonFromVioletXmlWriter {

    public static final void main(final String... args) {
        final String path = MetaJsonFromVioletXmlWriter.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        final String[] fileNames = {
            "activity-diagram"
        };

        final Jk4flowReader reader = new VioletReader();
        final Jk4flowTransformer<String, VltWorkflow> xmlToVioletTransformer = new XmlToVioletTransformer();
        final Jk4flowTransformer<VltWorkflow, MetaWorkflow> violetToMetaTransformer = new VioletToMetaTransformer();
        final Jk4flowTransformer<MetaWorkflow, String> metaToJsonTransformer = new MetaToJsonTransformer();

        for (final String fileName : fileNames) {
            final File fileToRead = new File(path + "/" + fileName + ".activity.violet.html");
            final File fieToWrite = new File(path + "/" + fileName + ".json");

            final String xml = reader.read(fileToRead);

            FileWriter writer = null;

            try {
                writer = new FileWriter(fieToWrite);

            } catch (IOException e) {
                throw new Jk4flowTranformerException("An exception was raised while creating a FileWriter", e);
            }

            final VltWorkflow vltWorkflow = xmlToVioletTransformer.transform(xml);
            final MetaWorkflow metaWorkflow = violetToMetaTransformer.transform(vltWorkflow);
            final String json = metaToJsonTransformer.transform(metaWorkflow);

            try {
                writer.write(json);

            } catch (IOException e) {
                throw new Jk4flowTranformerException("An exception was raised while writing to the file", e);

            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        throw new Jk4flowTranformerException("An exception was raised while trying to close the writer", e);
                    }
                }
            }

        }
    }

}
