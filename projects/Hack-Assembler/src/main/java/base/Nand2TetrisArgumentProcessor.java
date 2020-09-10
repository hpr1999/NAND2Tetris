package base;

import base.GenericArgumentProcessor.Argument;
import hack.parsing.Assembler;
import hack.parsing.Disassembler;
import hack.parsing.SmartDisassembler;
import hack.parsing.Translator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;

public class Nand2TetrisArgumentProcessor {

    public static final Argument<BiFunction<Path, Path, Translator>> OPERATION =
            new Argument<BiFunction<Path, Path, Translator>>(
            argument -> switch (argument) {
                case "assemble" -> Assembler::new;
                case "disassemble" -> Disassembler::new;
                case "smart-disassemble" -> SmartDisassembler::new;
                default -> throw new IllegalArgumentException();
            }, null);
    public static final Argument<Path> INPUT_FILE = new Argument<>(Paths::get, null);
    public static final Argument<Path> OUTPUT_FILE = new Argument<>(Paths::get, null);

    private final GenericArgumentProcessor processor;

    public Nand2TetrisArgumentProcessor(String[] incomingArguments) {
        processor = new GenericArgumentProcessor(incomingArguments);
        processor.registerArgument(OPERATION);
        processor.registerArgument(INPUT_FILE);
        processor.registerArgument(OUTPUT_FILE);
        processor.process();
    }

    public <T> T get(Argument<T> argument) {
        return processor.get(argument);
    }
}
