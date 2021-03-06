package hack;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static base.Main.main;
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
        String prefix = "src\\test\\resources\\testfiles\\" + name;
        testFile(
                Paths.get(prefix + ".asm"),
                Paths.get(prefix + ".hack"),
                Paths.get(prefix + ".cmp"),
                Paths.get(prefix + ".hack.asm"),
                Paths.get(prefix + ".hack.smartasm"),
                Paths.get(prefix + ".hack.smartasm.hack")
        );
    }

    private void testFile(Path asmFile, Path hackFile, Path cmpFile, Path disassemblyFile, Path smartDisassemblyFile, Path smartHackFile) throws IOException {
        Files.deleteIfExists(hackFile);
        Files.deleteIfExists(disassemblyFile);
        Files.deleteIfExists(smartDisassemblyFile);
        Files.deleteIfExists(smartHackFile);

        translate("assemble", asmFile, hackFile);
        translate("disassemble", hackFile, disassemblyFile);
        translate("smart-disassemble", hackFile, smartDisassemblyFile);
        translate("assemble", smartDisassemblyFile, smartHackFile);

        assertEquals(Files.lines(cmpFile).collect(Collectors.toList()), Files.lines(hackFile).collect(Collectors.toList()));
        assertEquals(Files.lines(cmpFile).collect(Collectors.toList()), Files.lines(smartHackFile).collect(Collectors.toList()));
    }

    private void translate(String operation, Path input, Path output) throws IOException {
        main(new String[]{operation, input.toAbsolutePath().toString(), output.toAbsolutePath().toString()});
    }

}