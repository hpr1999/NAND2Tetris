package base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IterableFileTest {

    private IterableFile assemblerFile;

    @BeforeEach
    private void setup() {
        assemblerFile = new IterableFile(getTestFile());
    }

    @Test
    void invalidPath() {
        assemblerFile = new IterableFile(Paths.get(""));
        Iterator<String> iterator = assemblerFile.iterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        assemblerFile.forEach(s -> fail());
    }

    @Test
    void iterator() {
        Iterator<String> it = assemblerFile.iterator();
        assertTrue(it.hasNext());
        assertEquals("Abc", it.next());
        assertTrue(it.hasNext());
        assertEquals("Bca", it.next());
        assertTrue(it.hasNext());
        assertEquals("Cba", it.next());
        assertFalse(it.hasNext());
        assertNull(it.next());
    }

    @Test
    void forEach() {
        List<String> results = new ArrayList<>();
        assemblerFile.forEach(results::add);
        assertEquals(Arrays.asList("Abc", "Bca", "Cba"), results);
    }

    @Test
    void testResources() {
        Path testFilePath = getTestFile();
        assertEquals("test-assembler-file.asm", testFilePath.getFileName().toString());
        assertTrue(Files.exists(testFilePath));
    }

    private Path getTestFile() {
        return Paths.get("").toAbsolutePath()
                .resolve(Paths.get("src/test/resources/test-assembler-file.asm"));
    }
}
