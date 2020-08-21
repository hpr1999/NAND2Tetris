package instruction;

import base.Instruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LabelInstructionTest {

    private LabelInstruction instruction;

    @BeforeEach
    void setUp() {
        instruction = new LabelInstruction("(label)");
    }

    @Test
    void validMnemonic() {
        assertTrue(instruction.isValidMnemonic());
        assertEquals("(label)", instruction.mnemonic());
        assertEquals("label", instruction.getSymbol().getLabel());
    }

    @Test
    void numbersInLabel() {
        instruction = new LabelInstruction("(l4b3l)");
        assertTrue(instruction.isValidMnemonic());
        assertEquals("(l4b3l)", instruction.mnemonic());
        assertEquals("l4b3l", instruction.getSymbol().getLabel());
    }

    @Test
    void numericLabel() {
        assertThrows(IllegalArgumentException.class, () -> new LabelInstruction("(1111)"));
    }

    @Test
    void invalidMnemonic() {
        assertThrows(IllegalArgumentException.class, () -> new LabelInstruction("label"));
    }


    @Test
    void emptyMnemonic() {
        assertThrows(IllegalArgumentException.class, () -> new LabelInstruction(""));
    }

    @Test
    void hasMachineCode() {
        assertFalse(instruction.hasMachineCode());
    }

    @Test
    void hasMnemonic() {
        assertTrue(instruction.hasMnemonic());
    }

    @Test
    void providesSymbol() {
        assertTrue(instruction.providesSymbol());
    }


    @Test
    void isValidMachineCode() {
        assertTrue(instruction.isValidMachineCode());
    }


    @Test
    void machineCode() {
        assertEquals(-1, instruction.machineCode());
    }

    @Test
    void machineCodeString() {
        assertNull(instruction.machineCodeString());
    }

    @Test
    void type() {
        assertEquals(Instruction.CommandType.LABEL, instruction.type());
    }
}