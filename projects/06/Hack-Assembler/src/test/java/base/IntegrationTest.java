package base;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTest {

    @Test
    void Add() throws IOException {
        testFile("Add");
    }

    @Test
    void Max() throws IOException {
        testFile("Max");
    }

    @Test
    void MaxL() throws IOException {
        testFile("MaxL");
    }

    @Test
    void Pong() throws IOException {
        testFile("Pong");
    }

    @Test
    void PongL() throws IOException {
        testFile("PongL");
    }

    @Test
    void PredefinedSymbols() throws IOException {
        testFile("PredefinedSymbols");
    }

    @Test
    void Rect() throws IOException {
        testFile("Rect");
    }

    @Test
    void RectL() throws IOException {
        testFile("RectL");
    }

    @Test
    void Simple() throws IOException {
        testFile("Simple");
    }

    @Test
    void SimpleL() throws IOException {
        testFile("SimpleL");
    }

    private void testFile(String name) throws IOException {
        String prefix = "C:\\Development\\Uni\\nand2tetris\\projects\\06\\Hack-Assembler\\src\\test\\resources\\testfiles\\" + name;
        testFile(
                Paths.get(prefix + ".asm"),
                Paths.get(prefix + ".hack"),
                Paths.get(prefix + ".cmp")
        );
    }

    private void testFile(Path asmFile, Path hackFile, Path cmpFile) throws IOException {
        Path disassemblyPath = hackFile.resolveSibling(hackFile.getFileName() + ".asm");
        Files.deleteIfExists(hackFile);
        Files.deleteIfExists(disassemblyPath);
        Main.assemble(asmFile, hackFile);
        Main.disassemble(hackFile, disassemblyPath);
        assertEquals(Files.lines(cmpFile).collect(Collectors.toList()), Files.lines(hackFile).collect(Collectors.toList()));
    }
}