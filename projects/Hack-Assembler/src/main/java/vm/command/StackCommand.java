package vm.command;

import com.google.common.collect.Lists;
import hack.base.Instruction;
import hack.instruction.AddressInstruction;
import hack.instruction.ComputationInstruction;
import hack.instruction.SymbolAccessInstruction;
import util.ArrayUtil;
import util.ConfigUtil;
import util.StringUtil;
import vm.context.TranslationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.addAll;

public class StackCommand implements VMCommand {

    public static boolean isValid(String command) {
        String[] parts = getCommandParts(command);
        return ArrayUtil.in(parts[0], "pop", "push")
                && Segment.isValidSegment(parts[1])
                && StringUtil.numeric(parts[2]);
//        TODO check if push / pop valid for segment type
//        TODO check range for temp
    }

    private final Function<TranslationContext, List<Instruction>> operation;

    public StackCommand(String command) {
        String[] parts = getCommandParts(command);
        Segment segment = Segment.parseSegment(parts[1], parts[2]);
        operation = switch (parts[0]) {
            case "pop" -> segment::pop;
            case "push" -> segment::push;
            default -> throw new IllegalArgumentException(command + " is not a valid StackCommand");//TODO
        };
    }

    private static String[] getCommandParts(String command) {
        String[] parts = command.split(" ");
        return parts;
    }

    @Override
    public List<Instruction> toAssembler(TranslationContext context) {
        return operation.apply(context);
    }

    private static abstract class Segment {

        private static Map<String, BiFunction<String, Integer, Segment>> segmentTypes = new HashMap<>();

        static {
//            TODO FROM CONFIG VIA Reflection of Types
            segmentTypes.put("this", MemorySegment::new);
            segmentTypes.put("that", MemorySegment::new);
            segmentTypes.put("local", MemorySegment::new);
            segmentTypes.put("argument", MemorySegment::new);
            segmentTypes.put("temp", TempSegment::new);
            segmentTypes.put("constant", ConstantSegment::new);
            segmentTypes.put("static", StaticSegment::new);
            segmentTypes.put("pointer", PointerSegment::new);
        }

        private static boolean isValidSegment(String segment) {
            return segmentTypes.containsKey(segment);
        }

        private static Segment parseSegment(String segment, String offset) {
            int memoryOffset = Integer.parseInt(offset);
            return segmentTypes.get(segment).apply(segment, memoryOffset);
        }

        final String memorySegment;
        final int memoryOffset;

        Segment(String memorySegment, int memoryOffset) {
            this.memorySegment = memorySegment;
            this.memoryOffset = memoryOffset;
        }

        List<Instruction> saveDOnStack(TranslationContext context) {
            return Lists.newArrayList(getStackPointerIntoAddressRegister(context),
                    new ComputationInstruction("A=M"),
                    new ComputationInstruction("M=D"));
        }

        Instruction getStackPointerIntoAddressRegister(TranslationContext context) {
            return new SymbolAccessInstruction("@SP", context.getSymbolTable(), false);
        }

        abstract List<Instruction> getValueToPushOnStackIntoD(TranslationContext context);

        List<Instruction> push(TranslationContext context) {
            // Get Value from Memory
            List<Instruction> result = getValueToPushOnStackIntoD(context);

            // Push Value onto Stack
            result.addAll(saveDOnStack(context));

            // Increment Stack Pointer
            addAll(result,
                    getStackPointerIntoAddressRegister(context),
                    new ComputationInstruction("M=M+1"));

            return result;
        }

        abstract List<Instruction> getAddressToPopStackValueIntoInD(TranslationContext context);


        List<Instruction> pop(TranslationContext context) {
            // Calc Address
            List<Instruction> result = getAddressToPopStackValueIntoInD(context);
            // Save Address on stack
            result.addAll(saveDOnStack(context));

            addAll(result,
                    // Get Stack Value and Decrement Pointer
                    getStackPointerIntoAddressRegister(context),
                    new ComputationInstruction("AM=M-1"),
                    new ComputationInstruction("D=M"),

                    // Get Address from Stack
                    new ComputationInstruction("A=A+1"),
                    new ComputationInstruction("A=M"),

                    // Save Stack Value To Memory
                    new ComputationInstruction("M=D"));

            return result;
        }

    }

    private static class ConstantSegment extends StackCommand.Segment {

        public ConstantSegment(String memorySegment, int memoryOffset) {
            super(memorySegment, memoryOffset);
        }

        @Override
        List<Instruction> getValueToPushOnStackIntoD(TranslationContext context) {
            return Lists.newArrayList(
                    new AddressInstruction(memoryOffset),
                    new ComputationInstruction("D=A")
            );
        }

        @Override
        List<Instruction> getAddressToPopStackValueIntoInD(TranslationContext context) {
            return null;
        }

        @Override
        List<Instruction> pop(TranslationContext context) {
            throw new RuntimeException(
                    new NoSuchMethodException("Cannot pop stack value into constant."));
        }

    }

    private static class MemorySegment extends Segment {
        private static final Map<String, String> segmentLabels =
                ConfigUtil.mapFromConfig("memory-segments",
                        ConfigUtil.identityMapper(String.class),
                        ConfigUtil.castMapper(ConfigUtil.identityMapper(String.class)));

        public MemorySegment(String memorySegment, int memoryOffset) {
            super(memorySegment, memoryOffset);
        }

        @Override
        List<Instruction> getValueToPushOnStackIntoD(TranslationContext context) {
            // Get Value from Memory
            List<Instruction> result =
                    offsetInDAndBaseInA(context);
            addAll(result,
                    new ComputationInstruction("A=D+A"),
                    new ComputationInstruction("D=M"));
            return result;
        }

        @Override
        List<Instruction> getAddressToPopStackValueIntoInD(TranslationContext context) {
            List<Instruction> result =
                    // Calc Address
                    offsetInDAndBaseInA(context);
            result.add(new ComputationInstruction("D=D+A"));
            return result;
        }

        private List<Instruction> offsetInDAndBaseInA(TranslationContext context) {
            return Lists.newArrayList(new AddressInstruction(memoryOffset),
                    new ComputationInstruction("D=A"),
                    new SymbolAccessInstruction(
                            segmentLabels.get(memorySegment),
                            context.getSymbolTable(),
                            false));
        }
    }

    private static class StaticSegment extends Segment {

        public StaticSegment(String memorySegment, int memoryOffset) {
            super(memorySegment, memoryOffset);
        }

        @Override
        List<Instruction> getValueToPushOnStackIntoD(TranslationContext context) {
            return Lists.newArrayList(
                    new SymbolAccessInstruction("@" + context.getFileName() + "." + memoryOffset, context.getSymbolTable(), true),
                    new ComputationInstruction("D=M")
            );
        }

        @Override
        List<Instruction> getAddressToPopStackValueIntoInD(TranslationContext context) {
            return Lists.newArrayList(
                    new SymbolAccessInstruction("@" + context.getFileName() + "." + memoryOffset, context.getSymbolTable(), true),
                    new ComputationInstruction("D=A")
            );
        }
    }

    private static class TempSegment extends Segment {

        private static final int BASE_ADDRESS = 5;

        public TempSegment(String memorySegment, int memoryOffset) {
            super(memorySegment, memoryOffset);
            checkArgument(memoryOffset < 8,
                    "%s must be smaller than 8 to be a temp variable!",
                    memoryOffset);
        }

        @Override
        List<Instruction> getValueToPushOnStackIntoD(TranslationContext context) {
            return Lists.newArrayList(
                    new AddressInstruction(5 + memoryOffset),
                    new ComputationInstruction("D=M")
            );
        }

        @Override
        List<Instruction> getAddressToPopStackValueIntoInD(TranslationContext context) {
            return Lists.newArrayList(
                    new AddressInstruction(5 + memoryOffset),
                    new ComputationInstruction("D=A")
            );
        }
    }

    private static class PointerSegment extends Segment {

        public PointerSegment(String memorySegment, int memoryOffset) {
            super(memorySegment, memoryOffset);
        }

        @Override
        List<Instruction> getValueToPushOnStackIntoD(TranslationContext context) {
            return Lists.newArrayList(
                    new SymbolAccessInstruction("@" + thisOrThat(), context.getSymbolTable(), false),
                    new ComputationInstruction("D=M")
            );
        }

        @Override
        List<Instruction> getAddressToPopStackValueIntoInD(TranslationContext context) {
            return Lists.newArrayList(
                    new SymbolAccessInstruction("@" + thisOrThat(), context.getSymbolTable(), false),
                    new ComputationInstruction("D=A")
            );
        }

        private String thisOrThat() {
            if (memoryOffset == 0) return "THIS";
            if (memoryOffset == 1) return "THAT";
            throw new IllegalStateException("Accessed pointer segment with offset " + memoryOffset + ", but must be 0 or 1.");
        }
    }
}
