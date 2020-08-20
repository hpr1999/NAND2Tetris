package base;

import instruction.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionTest {

    @Test
    void ctors() {
        TestInstruction tString = new TestInstruction("11");
        assertEquals(0b11, tString.integerRepresentation);
        TestInstruction tInt = new TestInstruction(0b11);
        assertEquals("11", tInt.stringRepresentation);
    }

    @Test
    void tooLargeNotValid() {
        assertThrows(IllegalArgumentException.class, () -> new TestInstruction(0b10000000000000000));
        assertTrue(new TestInstruction(0b1111111111111111).isValidMachineCode());
    }

    @Test
    void tooSmallNotValid() {
        assertTrue(new TestInstruction(0).isValidMachineCode());
        assertThrows(IllegalArgumentException.class, () -> new TestInstruction(-1));
    }

    @Test
    void betweenValid() {
        assertTrue(new TestInstruction(0b1111).isValidMachineCode());
        assertTrue(new TestInstruction(0b1111111).isValidMachineCode());
        assertTrue(new TestInstruction(0b100001101).isValidMachineCode());
    }

    class TestInstruction extends Instruction {

        public TestInstruction(int integerRepresentation) {
            super(integerRepresentation);
        }

        public TestInstruction(String stringRepresentation) {
            super(stringRepresentation);
        }

        public TestInstruction(int integerRepresentation, String stringRepresentation) {
            super(integerRepresentation, stringRepresentation);
        }

        @Override
        public boolean hasMachineCode() {
            return false;
        }

        @Override
        public boolean hasMnemonic() {
            return false;
        }

        @Override
        public boolean providesSymbol() {
            return false;
        }

        @Override
        public Symbol getSymbol() {
            return null;
        }

        @Override
        public boolean isValidMnemonic() {
            return true;
        }

        @Override
        public String mnemonic() {
            return null;
        }

        @Override
        public int machineCode() {
            return -1;
        }

        @Override
        public String machineCodeString() {
            return null;
        }

        @Override
        public CommandType type() {
            return null;
        }
    }
}