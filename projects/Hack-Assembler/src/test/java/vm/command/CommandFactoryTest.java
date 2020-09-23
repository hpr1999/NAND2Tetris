package vm.command;

import org.junit.jupiter.api.Test;
import vm.context.TranslationContext;

class CommandFactoryTest {

    @Test
    void test() {
        System.out.println(new CommandFactory().parseCommand("gt", new TranslationContext()).toAssembler(new TranslationContext()));
    }
}