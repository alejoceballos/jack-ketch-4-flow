package somossuinos.jackketch.transform.reader;

import java.io.File;

/**
 * The main contract that a file reader must implement.
 * <p>
 * Some file formats may not need a specific reader, but some of the diagram
 * files have structures that need to be be cleaned before starting the
 * transformation process.
 * </p>
 * <p>
 * For example, the Violet HTML/XML format is invalid if tried to be transformed
 * directly with no cleaning. So its reader must first get rid of the invalid
 * structures and return a clean, possible of transforming, XML.
 * </p>
 */
public interface Jk4flowReader {

    /**
     * Reads a file and returns a clean string to be transformed.
     *
     * @param file The file that holds the diagram data.
     * @return A clean structured string with a valid format possible of transformation.
     */
    String read(final File file);

}
