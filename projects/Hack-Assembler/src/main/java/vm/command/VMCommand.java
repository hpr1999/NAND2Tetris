package vm.command;

import hack.base.Instruction;
import vm.context.TranslationContext;

import java.util.List;

public interface VMCommand {
    List<Instruction> toAssembler(TranslationContext context);
}
