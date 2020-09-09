package hack.parsing;

import java.io.BufferedWriter;
import java.nio.file.Path;

public interface Translator {
    /**
     * Translate and write to a file
     */
    void translate(BufferedWriter writer);

    Path getInputFilePath();

    Path getOutputFilePath();
}
