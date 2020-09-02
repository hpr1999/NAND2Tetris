package instruction;

import base.SymbolTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolAccessInstructionTest {


    private SymbolTable table;
    private SymbolAccessInstruction instruction;

    @BeforeEach
    void setUp() {
        table = new SymbolTable();
    }

    @Test
    void inbuiltSymbols() {
        test("@KBD", 24576);
        test("@SCREEN", 16384);
        test("@SP", 0);
        test("@LCL", 1);
        test("@ARG", 2);
        test("@THIS", 3);
        test("@THAT", 4);
        for (int i = 0; i <= 15; i++) {
            test("@R" + i, i);
        }
    }

    @Test
    void variables() {
        test("@v1", 16);
        test("@v2", 17);
        test("@v3", 18);
        test("@v4", 19);

        test("@v1", 16);
        test("@v2", 17);
        test("@v3", 18);
        test("@v4", 19);

        table.createVariable(new Symbol("v5"));
        test("@v5", 20);
        test("@v5", 20);
    }

    @Test
    void symbols() {
        table.addSymbol(new Symbol("S101"), 101);
        table.addSymbol(new Symbol("S102"), 102);
        table.addSymbol(new Symbol("S103"), 103);

        test("@S101", 101);
        test("@S102", 102);
        test("@S103", 103);
    }

    @Test
    void mix() {
        table.createVariable(new Symbol("v5"));
        table.addSymbol(new Symbol("S15"), 15);
        table.addSymbol(new Symbol("S16"), 16);
        test("@S15", 15);
        test("@S16", 16);
        test("@R15", 15);
        test("@v5", 16);
    }

    @Test
    void invalid() {
        assertThrows(IllegalArgumentException.class, () -> test("@123", 123));
        assertThrows(IllegalArgumentException.class, () -> test("@Ä123", 123));
    }

    private void test(String mnemonic, int machineCode) {
        instruction = new SymbolAccessInstruction(mnemonic, table,true);
        assertEquals(machineCode, instruction.machineCode());
    }
}