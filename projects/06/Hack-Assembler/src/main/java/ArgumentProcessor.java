import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

public class ArgumentProcessor {

    private String[] arguments;
    private Map<String, String> argumentValues;

    public ArgumentProcessor(String[] arguments) {
        this.arguments = arguments;
    }

    public boolean process() {
        argumentValues = new HashMap<>();
        // TODO: Implement processing and return if it's valid.
        return true;
    }

    public Path getAsmFilePath() {
        if (argumentValues != null)
            this.process();
        return Paths.get(argumentValues.get("asm-file-path"));
    }
}
