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
import java.nio.file.Path;
import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Assembler implements Translator {

    private SymbolTable table;
    private Path assemblerFilePath;
    private Path hackFilePath;
    private IterableFile assemblerFile;

    public Assembler(Path assemblerFilePath, Path hackFilePath) {
        table = new SymbolTable();
        checkNotNull(assemblerFilePath);
        checkNotNull(hackFilePath);

        this.assemblerFilePath = assemblerFilePath;
        this.hackFilePath = hackFilePath;
        this.assemblerFile = new IterableFile(assemblerFilePath);
    }

    @Override
    public void translate(BufferedWriter writer) {
        considerSymbols();
        translateToMachineCode(writer);
    }

    private void considerSymbols() {
        passThroughFile((line, mnemonic) -> {
            Instruction ins = parseInstruction(mnemonic, false);
            if (ins.providesSymbol()) table.addSymbol(ins.getSymbol(), line);
            return ins.hasMachineCode();
        });
    }


    private void translateToMachineCode(BufferedWriter writer) {
        passThroughFile((line, mnemonic) -> {
            Instruction ins = parseInstruction(mnemonic, true);
            if (!ins.hasMachineCode()) return false;
//TODO
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
        for (String line : assemblerFile) {
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
    private Instruction parseInstruction(String mnemonic, boolean createVariables) {
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

        checkArgument(result != null, "The mnemonic %s cannot be transformed into an instruction", mnemonic);
        return result;
    }

    @Override
    public Path getInputFilePath() {
        return this.assemblerFilePath;
    }

    @Override
    public Path getOutputFilePath() {
        return this.hackFilePath;
    }
}