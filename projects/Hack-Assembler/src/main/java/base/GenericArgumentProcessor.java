package base;

import java.util.*;
import java.util.function.Function;

import static com.google.common.base.Preconditions.*;

// TODO TEST
public class GenericArgumentProcessor {

    private final Map<Argument, Object> valuesPerArgument = new HashMap<>();
    private final Queue<Argument<?>> arguments = new LinkedList<>();
    private final Queue<String> incomingArguments;

    public GenericArgumentProcessor(String[] argumentValues) {
        this.incomingArguments = new LinkedList<>(Arrays.asList(argumentValues));
    }

    public void registerArgument(Argument<?> argument) {
        checkArgument(!arguments.contains(argument), "This argument was already added.");
        arguments.add(argument);
    }

    public void process() {
        while (!arguments.isEmpty() && !incomingArguments.isEmpty()) {
            Argument argument = arguments.poll();
            Object value = argument.transformer.apply(incomingArguments.poll());
            valuesPerArgument.put(argument, value);
        }
    }

    public <T> T get(Argument<T> argument) {
        if (valuesPerArgument.containsKey(argument))
            return (T) valuesPerArgument.get(argument);
        else {
            checkState(argument.defaultSupplier != null, "This argument was not processed and has no default.");
            return argument.defaultSupplier.apply(this);
        }
    }

    public static class Argument<T> {
        private final Function<String, T> transformer;
        private final Function<GenericArgumentProcessor, T> defaultSupplier;

        public Argument(Function<String, T> transformer, Function<GenericArgumentProcessor, T> defaultSupplier) {
            checkNotNull(transformer);
            this.transformer = transformer;
            this.defaultSupplier = defaultSupplier;
        }
    }
}
