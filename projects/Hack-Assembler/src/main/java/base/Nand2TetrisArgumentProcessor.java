package base;

import hack.parsing.Assembler;
import hack.parsing.Disassembler;
import hack.parsing.SmartDisassembler;
import hack.parsing.Translator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;

//TODO TEST
public class Nand2TetrisArgumentProcessor extends GenericArgumentProcessor {

    public static final Argument<BiFunction<Path, Path, Translator>> OPERATION = new Argument<>(
            argument -> {
                switch (argument) {
                    case "assemble":
                        return Assembler::new;
                    case "disassemble":
                        return Disassembler::new;
                    case "smart-disassemble":
                        return SmartDisassembler::new;
                    default:
                        return null;
                }
            }, null);
    public static final Argument<Path> INPUT_FILE = new Argument<>(Paths::get, null);
    public static final Argument<Path> OUTPUT_FILE = new Argument<>(Paths::get, null);

    public Nand2TetrisArgumentProcessor(String[] incomingArguments) {
        super(incomingArguments);
        registerArgument(OPERATION);
        registerArgument(INPUT_FILE);
        registerArgument(OUTPUT_FILE);
        process();
    }

}
