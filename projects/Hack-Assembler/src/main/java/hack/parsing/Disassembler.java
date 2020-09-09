package hack.parsing;

import base.IterableFile;
import hack.base.Instruction;
import hack.instruction.AddressInstruction;
import hack.instruction.ComputationInstruction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

public class Disassembler implements Translator {
    protected IterableFile machineCodeFile;
    protected Path hackFilePath;
    protected Path assemblerFilePath;


    public Disassembler(Path hackFilePath, Path assemblerFilePath) {
        checkNotNull(hackFilePath);
        checkNotNull(assemblerFilePath);

        this.hackFilePath = hackFilePath;
        this.assemblerFilePath = assemblerFilePath;
        this.machineCodeFile = new IterableFile(hackFilePath);
    }


    protected Instruction parseInstruction(int machineCode) {
        if (AddressInstruction.isValidMachineCode(machineCode)) {
            return new AddressInstruction(machineCode);
        } else if (ComputationInstruction.isValidMachineCode(machineCode)) {
            return new ComputationInstruction(machineCode);
        } else {
            throw new IllegalArgumentException(Integer.toBinaryString(machineCode) + " is invalid Machine Code.");
        }

    }

    @Override
    public void translate(BufferedWriter writer) {
        int line = 0;
        for (String s : machineCodeFile) {
            try {
                int machineCode = Integer.parseInt(s, 2);
                Instruction ins = parseInstruction(machineCode);
//            TODO
                writer.write(ins.mnemonic() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Current lineNumber: " + line);
                System.err.println("Current lineText: " + s);
                throw t;
            }
            line++;
        }
    }

    @Override
    public Path getInputFilePath() {
        return hackFilePath;
    }

    @Override
    public Path getOutputFilePath() {
        return assemblerFilePath;
    }

}
