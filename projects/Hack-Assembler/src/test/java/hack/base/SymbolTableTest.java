package hack.base;

import hack.instruction.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTableTest {

    private SymbolTable table;

    @BeforeEach
    void setUp() {
        table = new SymbolTable();
    }

    @Test
    void testIllegalLineNumber() {
        assertThrows(IllegalArgumentException.class, () -> table.addSymbol(new Symbol(""), -1));
        assertThrows(IllegalArgumentException.class, () -> table.addSymbol(new Symbol(""), -100));
        assertDoesNotThrow(() -> table.addSymbol(new Symbol(""), 0));
    }

    @Test
    void testOverwrite() {
        Symbol s = new Symbol("abc");
        table.addSymbol(s, 13);
        assertThrows(IllegalStateException.class, () -> table.addSymbol(s, 123));
        assertThrows(IllegalStateException.class, () -> table.addSymbol(s, 13));
        assertThrows(IllegalStateException.class, () -> table.addSymbol(new Symbol("abc"), 13));
        assertThrows(IllegalStateException.class, () -> table.addSymbol(new Symbol("abc"), 123));
    }

    @Test
    void testVariables() {
        expectNoSymbol("16");
        expectNoSymbol("17");
        expectNoSymbol("18");

        table.createVariable(new Symbol("16"));
        table.createVariable(new Symbol("17"));
        table.createVariable(new Symbol("18"));

        expectSymbol("16", 16);
        expectSymbol("17", 17);
        expectSymbol("18", 18);
    }

    @Test
    void testNull() {
        assertThrows(NullPointerException.class, () -> table.addSymbol(null, 2));
        assertThrows(NullPointerException.class, () -> table.hasSymbol(null));

        assertThrows(NullPointerException.class, () -> table.createVariable(null));
        Symbol s = new Symbol("");
        table.createVariable(s);
        assertEquals(16, table.getLine(s));
    }

    @Test
    void testInit() {
        for (int i = 0; i <= 15; i++) {
            expectSymbol("R" + i, i);
        }

        expectSymbol("SCREEN", 16384);
        expectSymbol("KBD", 24576);
        expectSymbol("SP", 0);
        expectSymbol("LCL", 1);
        expectSymbol("ARG", 2);
        expectSymbol("THIS", 3);
        expectSymbol("THAT", 4);
    }


    private void expectSymbol(String expectedName, int expectedLine) {
        Symbol symbol = new Symbol(expectedName);
        assertTrue(table.hasSymbol(symbol));
        assertEquals(expectedLine, table.getLine(symbol));
    }

    private void expectNoSymbol(String name) {
        Symbol symbol = new Symbol(name);
        assertFalse(table.hasSymbol(symbol));
        assertThrows(IllegalArgumentException.class, () -> table.getLine(symbol));
    }
}