package base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class ArgumentProcessor {

    private static final String ASM_FILE_PATH = "asm-file-path";
    private static final String HACK_FILE_PATH = "hack-file-path";

    private Map<String, Object> argumentValues;
    private final Deque<String> arguments;

    public ArgumentProcessor(String[] arguments) {
        this.arguments = new ArrayDeque<>(Arrays.asList(arguments));
    }

    public boolean process() {
        argumentValues = new HashMap<>();

        if (tryParsingAnotherArgument(ASM_FILE_PATH, Paths::get,
                () -> {throw new IllegalArgumentException();})) {
            tryParsingAnotherArgument(HACK_FILE_PATH, Paths::get, this::hackFileFromAsmFile);
            return true;
        }
        return false;

    }

    private boolean tryParsingAnotherArgument(String argumentKey, Function<String, Object> transformer, Supplier defaultSupplier) {
        checkNotNull(argumentKey);
        checkNotNull(transformer);
        checkNotNull(defaultSupplier);
        if (!arguments.isEmpty()) {
            argumentValues.put(argumentKey, transformer.apply(arguments.pop()));
            return true;
        } else {
            argumentValues.put(argumentKey, defaultSupplier.get());
            return false;
        }
    }

    public Path getAsmFilePath() {
        return get(ASM_FILE_PATH, Path.class);
    }

    public Path getHackFilePath() {
        return get(HACK_FILE_PATH, Path.class);
    }

    private <T> T get(String argumentKey, Class<T> tClass) {
        if (argumentValues != null || this.process())
            return (T) argumentValues.get(argumentKey);
        throw new IllegalArgumentException();
    }

    private Path hackFileFromAsmFile() {
        return getAsmFilePath().resolveSibling(getAsmFilePath().getFileName() + ".hack");
    }
}
