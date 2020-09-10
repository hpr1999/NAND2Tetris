package hack.parsing;

import hack.base.Instruction;
import hack.base.SymbolTable;
import hack.instruction.ComputationInstruction;
import hack.instruction.InstructionFactory;
import hack.instruction.LabelInstruction;
import hack.instruction.SymbolAccessInstruction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public class SmartDisassembler extends Disassembler {
    public SmartDisassembler(Path hackFilePath, Path assemblerFilePath) {
        super(hackFilePath, assemblerFilePath);
    }

    @Override
    public void translate(BufferedWriter writer) {
        List<Instruction> instructions = new ArrayList<>();
        machineCodeFile.forEach(s -> instructions.add(InstructionFactory.parseInstruction(Integer.parseInt(s, 2))));
        reconstructBuiltIns(instructions);
        reconstructLabels(instructions);

        for (Instruction instruction : instructions) {
            String s = instruction.mnemonic();
            String s1 = s + '\n';
            try {
                writer.write(s1);
            } catch (IOException e) {
                throw new RuntimeException("Could not write to output file!", e);
            }
        }

    }

    private void reconstructBuiltIns(List<Instruction> instructions) {
        findAddressInstructions(instructions).forEach((ins, line) -> {
            if (SymbolTable.builtInsPerLine.containsKey(ins.machineCode())) {
                instructions.set(line, builtinSymbolAccessForAdress(ins, new SymbolTable()));
            }
        });
    }

    private void reconstructLabels(List<Instruction> instructions) {
        Map<Instruction, Integer> addressInstructions = findAddressInstructions(instructions);
        SortedSet<Integer> linesWithLabel = findLabelCandidates(instructions, addressInstructions);
        Integer[] labelLinesArray = linesWithLabel.toArray(new Integer[0]);
        Map<Integer, LabelInstruction> labelsForOriginalLines = new HashMap<>();
        SymbolTable table = new SymbolTable();
        createLabels(instructions, labelLinesArray, labelsForOriginalLines, table);
        createLabelReferences(instructions, addressInstructions, labelsForOriginalLines, table);
    }

    private void createLabelReferences(List<Instruction> instructions, Map<Instruction, Integer> addressInstructions, Map<Integer, LabelInstruction> labelsForOriginalLines, SymbolTable table) {
        for (var entry : labelsForOriginalLines.entrySet()) {
            for (var addressIns : addressInstructions.keySet()) {
                if (entry.getKey().equals(addressIns.machineCode())) {
                    instructions.set(
                            instructions.indexOf(addressIns),
                            new SymbolAccessInstruction("@" + entry.getValue().getSymbol().getLabel(),
                                    table, true));
                }
            }
        }
    }

    private void createLabels(List<Instruction> instructions, Integer[] labelLinesArray, Map<Integer, LabelInstruction> labelsForOriginalLines, SymbolTable table) {
        for (int i = 0; i < labelLinesArray.length; i++) {
            int oldLineNumber = labelLinesArray[i];
            LabelInstruction labelInstruction = new LabelInstruction("(LABEL" + (i + 1) + ')');
            int newLineNumber = i + oldLineNumber;
            instructions.add(newLineNumber, labelInstruction);
            labelsForOriginalLines.put(oldLineNumber, labelInstruction);
            table.addSymbol(labelInstruction.getSymbol(), newLineNumber);
        }
    }

    private SortedSet<Integer> findLabelCandidates(List<Instruction> instructions, Map<Instruction, Integer> addressInstructions) {
        SortedSet<Integer> linesWithLabel = new TreeSet<>();
        for (var entry : addressInstructions.entrySet()) {
            Instruction ins = entry.getKey();
            int line = entry.getValue();

            if (line < instructions.size() - 1
                    && instructions.get(line + 1).type() == Instruction.CommandType.COMPUTATION
                    && ((ComputationInstruction) instructions.get(line + 1)).hasJump()) {
                linesWithLabel.add(ins.machineCode());
            }
        }
        return linesWithLabel;
    }

    private Map<Instruction, Integer> findAddressInstructions(List<Instruction> instructions) {
        Map<Instruction, Integer> addressInstructions = new HashMap<>();


        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i).type() == Instruction.CommandType.ADDRESS)
                addressInstructions.put(instructions.get(i), i);
        }
        return addressInstructions;
    }

    private SymbolAccessInstruction builtinSymbolAccessForAdress(Instruction instruction, SymbolTable table) {
        checkArgument(SymbolTable.builtInsPerLine.containsKey(instruction.machineCode()));
        String builtInSymbolLabel = SymbolTable.builtInsPerLine.get(instruction.machineCode()).iterator().next().getLabel();
        return new SymbolAccessInstruction("@" + builtInSymbolLabel, table, false);
    }
}
