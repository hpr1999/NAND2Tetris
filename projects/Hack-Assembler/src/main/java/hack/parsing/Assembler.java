package hack.parsing;

import base.IterableFile;
import hack.base.Instruction;
import hack.base.SymbolTable;
import hack.instruction.InstructionFactory;
import util.StringUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiFunction;

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
            Instruction ins = InstructionFactory.parseInstruction(mnemonic, false, table);
            if (ins.providesSymbol()) table.addSymbol(ins.getSymbol(), line);
            return ins.hasMachineCode();
        });
    }


    private void translateToMachineCode(BufferedWriter writer) {
        passThroughFile((line, mnemonic) -> {
            Instruction ins = InstructionFactory.parseInstruction(mnemonic, true, table);
            if (!ins.hasMachineCode()) return false;

            try {
                writer.write(ins.machineCodeString() + '\n');
            } catch (IOException e) {
                throw new RuntimeException("Could not write to output file!", e);
            }
            return true;
        });
    }

    private void passThroughFile(BiFunction<Integer, String, Boolean> increaseLineNumberBasedOnLineNumberAndMnemonic) {
        int lineNumber = 0;
        for (String line : assemblerFile) {
            String relevant = StringUtil.stripComments(line);
            String mnemonic = StringUtil.stripAllWhiteSpace(relevant);
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


    @Override
    public Path getInputFilePath() {
        return this.assemblerFilePath;
    }

    @Override
    public Path getOutputFilePath() {
        return this.hackFilePath;
    }
}