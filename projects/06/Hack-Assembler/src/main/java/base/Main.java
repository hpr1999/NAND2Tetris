package base;

import parsing.Assembler;
import parsing.IterableFile;

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
        checkNotNull(asmFilePath);
        checkNotNull(hackFilePath);
        checkArgument(!Files.exists(hackFilePath),
                "The output file %s already exists.", hackFilePath.toAbsolutePath());
        checkArgument(Files.exists(asmFilePath),
                "The input file %s does not exist.", asmFilePath.toAbsolutePath());

        IterableFile assemblerFile = new IterableFile(asmFilePath);
        Files.createFile(hackFilePath);
        BufferedWriter writer = Files.newBufferedWriter(hackFilePath, StandardOpenOption.APPEND);
        Assembler assembler = new Assembler(assemblerFile, writer);
        assembler.assemble();
        writer.close();
    }
}
