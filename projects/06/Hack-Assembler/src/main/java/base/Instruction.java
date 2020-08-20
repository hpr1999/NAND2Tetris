package base;

import instruction.Symbol;

public abstract class Instruction {

    protected int integerRepresentation;
    protected String stringRepresentation;

    public Instruction(int integerRepresentation) {
        this(integerRepresentation, Integer.toBinaryString(integerRepresentation));

    }

    public Instruction(String stringRepresentation) {
        this(Integer.valueOf(stringRepresentation, 2), stringRepresentation);
    }

    protected Instruction(int integerRepresentation, String stringRepresentation) {
        this.integerRepresentation = integerRepresentation;
        this.stringRepresentation = stringRepresentation;
        if (!isValid())
            throw new IllegalArgumentException(integerRepresentation + " and "
                    + stringRepresentation + " do not form a valid Instruction.");
    }

    public boolean isValid() {
        return isValidMachineCode() && isValidMnemonic();
    }

    public abstract boolean hasMachineCode();

    public abstract boolean hasMnemonic();

    public abstract boolean providesSymbol();

    public abstract Symbol getSymbol();

    public abstract boolean isValidMnemonic();

    public boolean isValidMachineCode() {
        // Binary Integers between 0 and 16 1's
        return integerRepresentation <= 0b1111111111111111 && integerRepresentation >= 0b0000000000000000;
    }

    public abstract String mnemonic();

    public abstract int machineCode();

    public abstract String machineCodeString();

    public abstract CommandType type();

    public enum CommandType {
        ADDRESS, COMPUTATION, LABEL
    }
}
