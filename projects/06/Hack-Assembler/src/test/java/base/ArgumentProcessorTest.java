package base;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentProcessorTest {

    @Test
    void noArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> new ArgumentProcessor(new String[]{}).getAsmFilePath());

    }

    @Test
    void relativePath() {
        Path path = new ArgumentProcessor(new String[]{"src/test/resources/test-assembler-file.asm"}).getAsmFilePath();
        assertEquals(Paths.get("src/test/resources/test-assembler-file.asm"), path);
    }

    @Test
    void absolutePath() {
        Path path = new ArgumentProcessor(new String[]{"C:\\Development\\Uni\\nand2tetris\\projects\\06\\Hack-Assembler\\src\\test\\resources\\test-assembler0file.asm"}).getAsmFilePath();
        assertEquals(Paths.get("C:\\Development\\Uni\\nand2tetris\\projects\\06\\Hack-Assembler\\src\\test\\resources\\test-assembler0file.asm"), path);
    }
}