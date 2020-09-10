package base;

import hack.parsing.Assembler;
import hack.parsing.Disassembler;
import hack.parsing.SmartDisassembler;
import hack.parsing.Translator;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static base.Nand2TetrisArgumentProcessor.*;
import static org.junit.jupiter.api.Assertions.*;
import static util.ArrayUtil.array;

class Nand2TetrisArgumentProcessorTest {

    public static final String TEST_IN = "src/test/resources/test.in";
    public static final String TEST_OUT = "src/test/resources/test.out";
    private Nand2TetrisArgumentProcessor processor;

    @Test
    void assemble() {
        processor = defaultPathsWithOperation("assemble");
        Translator t = processor.get(OPERATION)
                .apply(processor.get(INPUT_FILE), processor.get(OUTPUT_FILE));
        assertTrue(t instanceof Assembler);
        assertEquals(Paths.get(TEST_IN), t.getInputFilePath());
        assertEquals(Paths.get(TEST_OUT), t.getOutputFilePath());
    }

    @Test
    void disassemble() {
        processor = defaultPathsWithOperation("disassemble");
        Translator t = processor.get(OPERATION)
                .apply(processor.get(INPUT_FILE), processor.get(OUTPUT_FILE));
        assertTrue(t instanceof Disassembler);
        assertEquals(Paths.get(TEST_IN), t.getInputFilePath());
        assertEquals(Paths.get(TEST_OUT), t.getOutputFilePath());
    }

    @Test
    void smartDisassemble() {
        processor = defaultPathsWithOperation("smart-disassemble");
        Translator t = processor.get(OPERATION)
                .apply(processor.get(INPUT_FILE), processor.get(OUTPUT_FILE));
        assertTrue(t instanceof SmartDisassembler);
        assertEquals(Paths.get(TEST_IN), t.getInputFilePath());
        assertEquals(Paths.get(TEST_OUT), t.getOutputFilePath());
    }

    @Test
    void invalidOperation() {
        assertThrows(IllegalArgumentException.class, () -> defaultPathsWithOperation("non-existing"));
    }

    @Test
    void noArguments() {
        assertThrows(IllegalStateException.class,
                () -> new Nand2TetrisArgumentProcessor(new String[]{}).get(INPUT_FILE));
    }

    @Test
    void relativePath() {
        Path path = new Nand2TetrisArgumentProcessor(array("assemble", "src/test/resources/test-assembler-file.asm")).get(INPUT_FILE);
        assertEquals(Paths.get("src/test/resources/test-assembler-file.asm"), path);
    }

    @Test
    void absolutePath() {
        Path path = new Nand2TetrisArgumentProcessor(array("assemble", "C:\\Development\\Uni\\nand2tetris\\projects\\06\\Hack-Assembler\\src\\test\\resources\\test-assembler0file.asm")).get(INPUT_FILE);
        assertEquals(Paths.get("C:\\Development\\Uni\\nand2tetris\\projects\\06\\Hack-Assembler\\src\\test\\resources\\test-assembler0file.asm"), path);
    }

    private Nand2TetrisArgumentProcessor defaultPathsWithOperation(String operation) {
        return new Nand2TetrisArgumentProcessor(array(operation, TEST_IN, TEST_OUT));
    }



}