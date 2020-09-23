package vm.command;

import vm.context.TranslationContext;

public class CommandFactory {

    public VMCommand parseCommand(String command, TranslationContext context) {
        if(StackCommand.isValid(command)) return new StackCommand(command);
        if(ComputationCommand.isValid(command)) return new ComputationCommand(command, context);
        throw new IllegalArgumentException(command + " is not a valid command.");
    }
}
