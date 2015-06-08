package somossuinos.jackketch.transform.reader;

import java.io.File;
import org.junit.Test;
import somossuinos.jackketch.transform.violet.VltWorkflow;

public class VioletReaderTest {

    @Test
    public void testRead() {
        final Jk4flowReader<VltWorkflow> reader = new VioletReader();

        try {
            reader.read(new File("src/test/resources/activity-diagram.activity.violet.html"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
