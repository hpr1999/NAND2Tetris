package vm.command;

import hack.base.Instruction;
import vm.context.TranslationContext;

import java.util.List;

public class FunctionCallCommand implements VMCommand {
    @Override
    public List<Instruction> toAssembler(TranslationContext context) {
        return null;
    }
}
