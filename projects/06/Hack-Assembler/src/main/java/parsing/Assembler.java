package parsing;

import base.Instruction;
import base.SymbolTable;
import instruction.AddressInstruction;
import instruction.ComputationInstruction;
import instruction.LabelInstruction;
import instruction.SymbolAccessInstruction;
import util.MnemonicUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Assembler {

    private SymbolTable table;
    private AssemblerFile file;
    private BufferedWriter writer;

    public Assembler(AssemblerFile file, BufferedWriter writer) {
        table = new SymbolTable();
        checkNotNull(file);
        this.file = file;
        this.writer = writer;
    }

    public void assemble() {
        considerSymbols();
        translateToMachineCode();
    }

    private void considerSymbols() {
        passThroughFile((line, mnemonic) -> {
            Instruction ins = parseInstruction(mnemonic);
            if (ins.providesSymbol()) table.addSymbol(ins.getSymbol(), line);
            return ins.hasMachineCode();
        });
    }


    private void translateToMachineCode() {
        passThroughFile((line, mnemonic) -> {
            Instruction ins = parseInstruction(mnemonic);
            if (!ins.hasMachineCode()) return false;

            try {
                writer.write(ins.machineCodeString() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        });
    }

    private void passThroughFile(BiFunction<Integer, String, Boolean> increaseLineNumberBasedOnLineNumberAndMnemonic) {
        int lineNumber = 0;
        for (String line : file) {
            String relevant = MnemonicUtil.stripComments(line);
            String mnemonic = MnemonicUtil.stripAllWhiteSpace(relevant);
            try {
                if (!mnemonic.isBlank() &&
                        increaseLineNumberBasedOnLineNumberAndMnemonic.apply(lineNumber, mnemonic))
                    lineNumber++;
            } catch (Throwable t) {
                System.err.println("Current lineNumber: " + lineNumber);
                System.err.println("Current lineText: " + line);
                System.err.println("Current mnemonic: " + mnemonic);
                throw t;
            }
        }
    }

    // TODO ausgliedern, die dependency ist hier unnötig
    private Instruction parseInstruction(String mnemonic) {
        Instruction result = null;

        if (LabelInstruction.isValidMnemonic(mnemonic)) {
            result = new LabelInstruction(mnemonic);
        } else if (AddressInstruction.isValidMnemonic(mnemonic)) {
            result = new AddressInstruction(mnemonic);
        } else if (SymbolAccessInstruction.isValidMnemonic(mnemonic)) {
            result = new SymbolAccessInstruction(mnemonic, table);
        } else if (ComputationInstruction.isValidMnemonic(mnemonic)) {
            result = new ComputationInstruction(mnemonic);
        }

        checkArgument(result != null, "The mnemonic %s cannot be transformed into an instruction", mnemonic);
        return result;
    }
}
// Parsing:
//    start iterating
//    clean all whitespace in the process
//    calculate the current linenumber
//    instantly translate all label, computation, address instructions
//    memorize all SymbolAccessInstructions
//
// after all lines have been worked through:
//   translate all SymbolAccessInstructions in the order they were found


