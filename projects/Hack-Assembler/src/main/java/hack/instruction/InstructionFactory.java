package hack.instruction;

import hack.base.Instruction;
import hack.base.SymbolTable;

import static com.google.common.base.Preconditions.checkArgument;

public class InstructionFactory {

    public static Instruction parseInstruction(String mnemonic, boolean createVariables, SymbolTable table) {
        Instruction result = null;

        if (LabelInstruction.isValidMnemonic(mnemonic)) {
            result = new LabelInstruction(mnemonic);
        } else if (AddressInstruction.isValidMnemonic(mnemonic)) {
            result = new AddressInstruction(mnemonic);
        } else if (SymbolAccessInstruction.isValidMnemonic(mnemonic)) {
            result = new SymbolAccessInstruction(mnemonic, table, createVariables);
        } else if (ComputationInstruction.isValidMnemonic(mnemonic)) {
            result = new ComputationInstruction(mnemonic);
        }

        checkArgument(result != null, "The mnemonic %s cannot be transformed into an hack.instruction", mnemonic);
        return result;
    }

    public static Instruction parseInstruction(int machineCode) {
        if (AddressInstruction.isValidMachineCode(machineCode)) {
            return new AddressInstruction(machineCode);
        } else if (ComputationInstruction.isValidMachineCode(machineCode)) {
            return new ComputationInstruction(machineCode);
        } else {
            throw new IllegalArgumentException(Integer.toBinaryString(machineCode) + " is invalid Machine Code.");
        }

    }

}
