package somossuinos.jackketch.transform.reader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VioletReader implements Jk4flowReader {

    private static final String START_STRING = "<![CDATA[";
    private static final String END_STRING = "]]>";

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";

    @Override
    public String read(final File file) {
        final StringBuilder xml = new StringBuilder(XML_HEADER);

        try {
            FileReader reader = new FileReader(file);
            final byte[] byteContent = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            final String content = new String(byteContent);

            final int START_IX = content.indexOf(START_STRING) + START_STRING.length();
            final int END_IX = content.indexOf(END_STRING);

            xml.append(content.substring(START_IX, END_IX));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return xml.toString();

    }

}
