package base;

import parsing.Assembler;
import parsing.AssemblerFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {

    public static void main(String[] args) throws IOException {
        ArgumentProcessor processor = new ArgumentProcessor(args);
        processor.process();
        assemble(processor.getAsmFilePath(), processor.getHackFilePath());
    }

    public static void assemble(Path asmFilePath, Path hackFilePath) throws IOException {
        AssemblerFile assemblerFile = new AssemblerFile(asmFilePath);
        BufferedWriter writer = Files.newBufferedWriter(hackFilePath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        Assembler assembler = new Assembler(assemblerFile, writer);
        assembler.assemble();
    }
}
