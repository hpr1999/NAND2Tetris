package instruction;

import base.Instruction;

public class LabelInstruction extends Instruction {

    private Symbol symbol;

    public LabelInstruction(String stringRepresentation) {
        super(-1, stringRepresentation);
        this.symbol = new Symbol(stringRepresentation.substring(1, stringRepresentation.length() - 1));
    }

    @Override
    public boolean hasMachineCode() {
        return false;
    }

    @Override
    public boolean hasMnemonic() {
        return true;
    }

    @Override
    public boolean providesSymbol() {
        return true;
    }

    @Override
    public Symbol getSymbol() {
        return symbol;
    }

    public static boolean isValidMnemonic(String stringRepresentation) {
        char first = stringRepresentation.charAt(0);
        int lastIndex = stringRepresentation.length() - 1;
        char lastChar = stringRepresentation.charAt(lastIndex);
        return first == '(' && lastChar == ')' && !stringRepresentation.substring(1, lastIndex).chars().allMatch(Character::isDigit);
    }

    @Override
    public boolean isValidMnemonic() {
        return isValidMnemonic(stringRepresentation);
    }

    @Override
    public boolean isValidMachineCode() {
        return true;
    }

    @Override
    public String mnemonic() {
        return stringRepresentation;
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
        return CommandType.LABEL;
    }
}
