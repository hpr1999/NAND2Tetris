package vm.command;

import hack.base.Instruction;
import hack.instruction.ComputationInstruction;
import hack.instruction.InstructionFactory;
import hack.instruction.SymbolAccessInstruction;
import util.ConfigUtil;
import vm.context.TranslationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static util.ArrayUtil.array;
import static util.ConfigUtil.*;
public class ComputationCommand implements VMCommand {

    private static Map<String, Integer> operationArity = ConfigUtil.mapFromConfig(
            "computation-command-arity",
            identityMapper(String.class),
            o -> Math.toIntExact((Long) o));
    private Map<String, List<Instruction>> operations ;
//TODO finish config

    public static boolean isValid(String command) {
        return operationArity.containsKey(command);
    }

    private final int arity;
    private final String command;
    private final TranslationContext context;

    public ComputationCommand(String command, TranslationContext context) {
        this.arity = operationArity.get(command);
        this.command = command;
        this.context = context;
        operations = ConfigUtil.mapFromConfig(
                "computation-commands",
                identityMapper(String.class),
                listMapper(castMapper(String.class, s -> InstructionFactory.parseInstruction(s,false,context.getSymbolTable()))));
    }


    @Override
    public List<Instruction> toAssembler(TranslationContext context) {
        List<Instruction> result = new ArrayList<>();
        result.add(new SymbolAccessInstruction("@SP", context.getSymbolTable(), false));
        result.addAll(
                Arrays.asList(switch (arity) {
                    case 1 -> array(new ComputationInstruction("A=M-1"));
                    case 2 -> array(
                            new ComputationInstruction("AM=M-1"),
                            new ComputationInstruction("D=M"),
                            new ComputationInstruction("A=A-1"));
                    default -> throw new IllegalStateException("Unexpected function arity: " + arity);
                }));
        result.addAll(operations.get(command));

        return result;
    }

}
