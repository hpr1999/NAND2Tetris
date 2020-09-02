package instruction;

import base.Instruction;
import util.BinaryUtil;

public class AddressInstruction extends Instruction {

    public static final Character ADDRESS_IDENTIFIER = '@';

    public AddressInstruction(int integerRepresentation) {
        this(integerRepresentation,
                translate(integerRepresentation));
    }

    public AddressInstruction(String stringRepresentation) {
        this(translate(stringRepresentation),
                stringRepresentation);
    }

    protected AddressInstruction(int integerRepresentation, String stringRepresentation) {
        super(integerRepresentation, stringRepresentation);
    }

    protected AddressInstruction() {
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
        int value = translate(stringRepresentation);
        return stringRepresentation.charAt(0) == ADDRESS_IDENTIFIER &&
                value <= (BinaryUtil.LARGEST_VALUE >> 1) &&
                value >= BinaryUtil.SMALLEST_VALUE;
    }

    @Override
    public boolean isValidMachineCode() {
//        First bit of 16 bit number is 0
        return super.isValidMachineCode() &&
                Integer.numberOfLeadingZeros(integerRepresentation) > BinaryUtil.BINARY_WORD_LENGTH;
    }

    public static String translate(int machineCode) {
        return ADDRESS_IDENTIFIER.toString() + machineCode;
    }

    @Override
    public String mnemonic() {
        return stringRepresentation;
    }

    public static int translate(String mnemonic) {
        try {
            return Integer.parseInt(mnemonic.substring(1));
        } catch (NumberFormatException ne) {
            return -1;
        }
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
