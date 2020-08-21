package instruction;

import base.Instruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressInstructionTest {

    private AddressInstruction instruction;

    @BeforeEach
    void setUp() {
        instruction = new AddressInstruction(0b000000000000000);
    }

    @Test
    void hasMachineCode() {
        assertTrue(instruction.hasMachineCode());
    }

    @Test
    void hasMnemonic() {
        assertTrue(instruction.hasMnemonic());
    }

    @Test
    void providesSymbol() {
        assertFalse(instruction.providesSymbol());
    }

    @Test
    void getSymbol() {
        assertNull(instruction.getSymbol());
    }

    @Test
    void type() {
        assertEquals(Instruction.CommandType.ADDRESS, instruction.type());
    }

    @Test
    void stringCtor() {
        AddressInstruction string = new AddressInstruction("@32767");
        assertEquals(0b0111111111111111, string.machineCode());
    }

    @Test
    void intCtor() {
        AddressInstruction integer = new AddressInstruction(0b0111111111111111);
        assertEquals("0111111111111111", integer.machineCodeString());
        assertEquals("@32767", integer.mnemonic());
    }

    @Test
    void addressTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> new AddressInstruction("@32768"));
    }

    @Test
    void addressTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> new AddressInstruction("@-1"));
    }

    @Test
    void addressNoNumber() {
        assertThrows(IllegalArgumentException.class, () -> new AddressInstruction("@asdsad"));
    }
}