package base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ArgumentProcessor {

    private static final String asmFileKey = "asm-file-path";
    private String[] arguments;
    private Map<String, String> argumentValues;

    public ArgumentProcessor(String[] arguments) {
        this.arguments = arguments;
    }

    public boolean process() {
        argumentValues = new HashMap<>();
        if (arguments.length > 0 && arguments[0] != null) {
            argumentValues.put(asmFileKey, arguments[0]);
            return true;
        } else return false;
    }

    public Path getAsmFilePath() {
        if (argumentValues != null || this.process())
            return Paths.get(argumentValues.get(asmFileKey));

        return null;
    }
}
