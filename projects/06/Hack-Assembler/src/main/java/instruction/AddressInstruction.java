package instruction;

import base.Instruction;
import util.BinaryUtil;

public class AddressInstruction extends Instruction {

    public AddressInstruction(int integerRepresentation) {
        this(integerRepresentation,
                "@" + integerRepresentation);
    }

    public AddressInstruction(String stringRepresentation) {
        this(Integer.parseInt(stringRepresentation.substring(1)),
                stringRepresentation);
    }

    protected AddressInstruction(int integerRepresentation, String stringRepresentation) {
        super(integerRepresentation, stringRepresentation);
    }

    @Override
    public boolean hasMachineCode() {
        return true;
    }

    @Override
    public boolean hasMnemonic() {
        return true;
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
        return isValidMnemonic(stringRepresentation);
    }

    public static boolean isValidMnemonic(String stringRepresentation) {
        int value = Integer.parseInt(stringRepresentation.substring(1));
        return stringRepresentation.charAt(0) == '@' &&
                value <= (BinaryUtil.LARGEST_VALUE >> 1) &&
                value >= BinaryUtil.SMALLEST_VALUE;
    }

    @Override
    public boolean isValidMachineCode() {
//        First bit of 16 bit number is 0
        return super.isValidMachineCode() &&
                Integer.numberOfLeadingZeros(integerRepresentation) > BinaryUtil.BINARY_WORD_LENGTH;
    }

    @Override
    public String mnemonic() {
        return stringRepresentation;
    }

    @Override
    public int machineCode() {
        return integerRepresentation;
    }

    @Override
    public String machineCodeString() {
        return BinaryUtil.fixedLengthBinaryString(machineCode(), BinaryUtil.BINARY_WORD_LENGTH);
    }

    @Override
    public CommandType type() {
        return CommandType.ADDRESS;
    }
}
