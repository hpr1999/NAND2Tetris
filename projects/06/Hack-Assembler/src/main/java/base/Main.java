package base;

import parsing.Assembler;
import parsing.Disassembler;
import parsing.SmartDisassembler;
import parsing.Translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Main {

    public static void main(String[] args) throws IOException {
        ArgumentProcessor processor = new ArgumentProcessor(args);
        processor.process();
        assemble(processor.getAsmFilePath(), processor.getHackFilePath());
    }

    public static void assemble(Path asmFilePath, Path hackFilePath) throws IOException {
        Translator assembler = new Assembler(asmFilePath, hackFilePath);
        translate(assembler);
    }

    public static void disassemble(Path hackFilePath, Path asmFilePath) throws IOException {
        Translator disassembler = new Disassembler(hackFilePath, asmFilePath);
        translate(disassembler);
    }

    public static void smartDisassemble(Path hackFilePath, Path asmFilePath) throws IOException {
        Translator disassembler = new SmartDisassembler(hackFilePath, asmFilePath);
        translate(disassembler);
    }

    private static void translate(Translator translator) throws IOException {
        checkNotNull(translator.getInputFilePath());
        checkNotNull(translator.getOutputFilePath());
        checkArgument(!Files.exists(translator.getOutputFilePath()),
                "The output file %s already exists.", translator.getOutputFilePath().toAbsolutePath());
        checkArgument(Files.exists(translator.getInputFilePath()),
                "The input file %s does not exist.", translator.getInputFilePath().toAbsolutePath());

        Files.createFile(translator.getOutputFilePath());
        BufferedWriter writer = Files.newBufferedWriter(translator.getOutputFilePath(), StandardOpenOption.APPEND);
        translator.translate(writer);
        writer.close();
    }


}
