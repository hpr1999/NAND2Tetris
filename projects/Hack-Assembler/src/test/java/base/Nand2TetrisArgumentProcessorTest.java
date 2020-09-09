package base;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static base.Nand2TetrisArgumentProcessor.INPUT_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Nand2TetrisArgumentProcessorTest {

    @Test
    void noArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> new Nand2TetrisArgumentProcessor(new String[]{}).get(INPUT_FILE));

    }

    @Test
    void relativePath() {
        Path path = new Nand2TetrisArgumentProcessor(new String[]{"assemble src/test/resources/test-assembler-file.asm"}).get(INPUT_FILE);
        assertEquals(Paths.get("src/test/resources/test-assembler-file.asm"), path);
    }

    @Test
    void absolutePath() {
        Path path = new Nand2TetrisArgumentProcessor(new String[]{"assemble C:\\Development\\Uni\\nand2tetris\\projects\\06\\Hack-Assembler\\src\\test\\resources\\test-assembler0file.asm"}).get(INPUT_FILE);
        assertEquals(Paths.get("C:\\Development\\Uni\\nand2tetris\\projects\\06\\Hack-Assembler\\src\\test\\resources\\test-assembler0file.asm"), path);
    }


}