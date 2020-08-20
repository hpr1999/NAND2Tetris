package instruction;

import base.Instruction;

public class AddressInstruction extends Instruction {

    public AddressInstruction(int integerRepresentation) {
        this(integerRepresentation,
                "@" + integerRepresentation);
    }

    public AddressInstruction(String stringRepresentation) {
        this(Integer.valueOf(stringRepresentation.substring(1)),
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
        int value = Integer.parseInt(stringRepresentation.substring(1));
        return stringRepresentation.charAt(0) == '@' &&
                value <= 0b111111111111111 && value >= 0;
    }

    @Override
    public boolean isValidMachineCode() {
//        First bit of 16 bit number is 0
        return super.isValidMachineCode() && Integer.numberOfLeadingZeros(integerRepresentation) >= 17;
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

        return Integer.toBinaryString(machineCode() | 0b10000000000000000).substring(1);
    }

    @Override
    public CommandType type() {
        return CommandType.ADDRESS;
    }
}
