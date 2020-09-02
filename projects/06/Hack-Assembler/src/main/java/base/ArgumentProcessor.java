package base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ArgumentProcessor {

    private static final String asmFileKey = "asm-file-path";
    private static final String hackFileKey = "hack-file-path";
    private String[] arguments;
    private Map<String, String> argumentValues;

    public ArgumentProcessor(String[] arguments) {
        this.arguments = arguments;
    }

    public boolean process() {
        argumentValues = new HashMap<>();
        if (arguments.length > 0 && arguments[0] != null) {
            argumentValues.put(asmFileKey, arguments[0]);

            if (arguments.length > 1 && arguments[1] != null)
                argumentValues.put(hackFileKey, arguments[1]);
            else argumentValues.put(hackFileKey, hackFileFromAsmFile());

            return true;
        } else return false;
    }

    public Path getAsmFilePath() {
        if (argumentValues != null || this.process())
            return Paths.get(argumentValues.get(asmFileKey));

        return null;
    }

    public Path getHackFilePath() {
        if (argumentValues != null || this.process())
            return Paths.get(argumentValues.get(hackFileKey));

        return null;
    }

    private String hackFileFromAsmFile() {
        String asm = argumentValues.get(asmFileKey);
        int index = asm.lastIndexOf('.');
        index = index != -1 ? index : asm.length() - 1;
        return asm.substring(0, index) + ".hack";
    }
}
