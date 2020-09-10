package base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static base.GenericArgumentProcessor.*;
import static org.junit.jupiter.api.Assertions.*;
import static util.ArrayUtil.array;

class GenericArgumentProcessorTest {

    private GenericArgumentProcessor processor;
    private static final String[] args = array("1", "2");
    private static final Argument<String> ARG1 = new Argument<>(argument -> "argument" + argument, processor -> "default");
    private static final Argument<String> ARG2 = new Argument<>(argument -> "argument" + argument, processor -> "default");

    @BeforeEach
    void setUp() {
        processor = new GenericArgumentProcessor(args);
        processor.registerArgument(ARG1);
        processor.registerArgument(ARG2);
    }

    @Test
    void nullArgument() {
        assertThrows(NullPointerException.class, () -> new GenericArgumentProcessor(null));
        assertThrows(NullPointerException.class, () -> processor.registerArgument(null));
        assertThrows(NullPointerException.class, () -> processor.get(null));
        assertThrows(NullPointerException.class, () -> new Argument<>(null, null));
        assertThrows(NullPointerException.class, () -> processor.registerArgument(new Argument<>(null, null)));
    }

    @Test
    void registerExisting() {
        assertThrows(IllegalArgumentException.class, () -> processor.registerArgument(ARG1));
    }

    @Test
    void queryNonExisting() {
        assertThrows(IllegalArgumentException.class, () -> processor.get(new Argument<>(s -> s, Object::toString)));
    }

    @Test
    void mapper() {
        processor.process();
        assertEquals("argument1", processor.get(ARG1));
        assertEquals("argument2", processor.get(ARG2));
    }

    @Test
    void defaults() {
        processor = new GenericArgumentProcessor(array());
        processor.registerArgument(ARG1);
        processor.registerArgument(ARG2);
        processor.process();
        assertEquals("default", processor.get(ARG1));
        assertEquals("default", processor.get(ARG2));
    }
}