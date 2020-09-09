package base;

import hack.parsing.Translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class AlternativeMain {

    public static void main(String[] args) throws IOException {
        Nand2TetrisArgumentProcessor argumentProcessor = new Nand2TetrisArgumentProcessor(args);
        Path input = argumentProcessor.get(Nand2TetrisArgumentProcessor.INPUT_FILE);
        Path output = argumentProcessor.get(Nand2TetrisArgumentProcessor.OUTPUT_FILE);
        Translator translator = argumentProcessor.get(Nand2TetrisArgumentProcessor.OPERATION).apply(input, output);
        translate(translator);
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
