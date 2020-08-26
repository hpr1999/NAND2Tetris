package instruction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComputationInstructionTest {

    private int machineCode;
    private Integer expectedMachineCode;
    private String mnemonic;
    private String expectedMnemonic;

    @Test
    void validMachineCode() {
        init(0b111_0_111111_001_110);
        expect("M=1;JLE");
        testMachineCode();
    }

    @Test
    void machineCodeInvalidLength() {
        init(0b111__001101_110_101);
        expectFail();
        testMachineCode();
    }

    @Test
    void machineCodeInvalidWrongA() {
        init(0b111_1_001101_110_101);
        expectFail();
        testMachineCode();
    }

    @Test
    void validMnemonics() {
        init("AD=!D;JNE");
        expect(0b111_0_001101_110_101);
        testMnemonic();
    }

    @Test
    void validMnemonicsNoDest() {
        init("D;JMP");
        expect(0b111_0_001100_000_111);
        testMnemonic();
    }

    @Test
    void validMnemonicsNoJump() {
        init("AMD=D|M");
        expect(0b111_1_010101_111_000);
        testMnemonic();
    }

    @Test
    void validMnemonicsOnlyComp() {
        init("D+A");
        expect(0b111_0_000010_000_000);
        testMnemonic();
    }

    @Test
    void invalidJump() {
        init("A=D;JAP");
        expectFail();
        testMnemonic();
    }

    @Test
    void invalidDest() {
        init("C=D;JMP");
        expectFail();
        testMnemonic();
    }

    @Test
    void invalidComp() {
        init("D=F;JMP");
        expectFail();
        testMnemonic();
    }

    @Test
    void invalidMnemonicsWhiteSpace() {
        init("A =1; JMP");
        expectFail();
        testMnemonic();
    }


    private void init(int machineCode) {
        this.machineCode = machineCode;
    }

    private void init(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    private void expect(int expectedMachineCode) {
        this.expectedMachineCode = expectedMachineCode;
    }

    private void expectFail() {
        this.expectedMnemonic = null;
        this.expectedMachineCode = null;
    }

    private void expect(String expectedMnemonic) {
        this.expectedMnemonic = expectedMnemonic;
    }

    private void testMnemonic() {
        boolean valid = expectedMachineCode != null;
        assertEquals(valid, ComputationInstruction.isValidMnemonic(mnemonic));

        if (valid) {
            assertEquals(expectedMachineCode, new ComputationInstruction(mnemonic).machineCode());
            assertEquals(expectedMachineCode, ComputationInstruction.translate(mnemonic));
        } else {
            assertThrows(IllegalArgumentException.class, () -> ComputationInstruction.translate(mnemonic));
        }
    }

    private void testMachineCode() {
        boolean valid = expectedMnemonic != null;
        assertEquals(valid, ComputationInstruction.isValidMachineCode(machineCode));

        if (valid) {
            assertEquals(expectedMnemonic, new ComputationInstruction(machineCode).mnemonic());
            assertEquals(expectedMnemonic, ComputationInstruction.translate(machineCode));
        } else {
            assertThrows(IllegalArgumentException.class, () -> ComputationInstruction.translate(machineCode));
        }
    }


}