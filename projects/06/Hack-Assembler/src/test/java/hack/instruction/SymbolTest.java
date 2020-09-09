package hack.instruction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTest {
    @Test
    void equals() {
        Symbol s1 = new Symbol("abc");
        Symbol s2 = new Symbol("abc");
        Symbol s3 = new Symbol("xyz");

        assertEquals(s2, s1);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertEquals(s2.hashCode(), s1.hashCode());

        assertNotEquals(s1, s3);
        assertNotEquals(s3, s1);
        assertNotEquals(s1.hashCode(), s3.hashCode());
        assertNotEquals(s3.hashCode(), s1.hashCode());
    }

}