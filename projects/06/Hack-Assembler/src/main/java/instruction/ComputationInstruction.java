package instruction;

import base.Instruction;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import util.BinaryUtil;

import static com.google.common.base.Preconditions.checkArgument;

public class ComputationInstruction extends Instruction {

    private static final BiMap<String, Integer> computations = HashBiMap.create();
    private static final BiMap<String, Integer> destinations = HashBiMap.create();
    private static final BiMap<String, Integer> jumps = HashBiMap.create();
    public static final int DESTINATION_BINARY_OFFSET = 3;
    public static final int COMPUTATION_BINARY_OFFSET = 6;
    public static final int MARKER_BINARY_OFFSET = 13;

//    FIXME config file?
    static {
        jumps.put(null, 0b000);
        jumps.put("JGT", 0b001);
        jumps.put("JEQ", 0b010);
        jumps.put("JGE", 0b011);
        jumps.put("JLT", 0b100);
        jumps.put("JNE", 0b101);
        jumps.put("JLE", 0b110);
        jumps.put("JMP", 0b111);

        destinations.put(null, 0b000);
        destinations.put("M", 0b001);
        destinations.put("D", 0b010);
        destinations.put("MD", 0b011);
        destinations.put("A", 0b100);
        destinations.put("AM", 0b101);
        destinations.put("AD", 0b110);
        destinations.put("AMD", 0b111);

        computations.put("0", 0b0101010);
        computations.put("1", 0b0111111);
        computations.put("-1", 0b0111010);
        computations.put("D", 0b0001100);
        computations.put("A", 0b0110000);
        computations.put("!D", 0b0001101);
        computations.put("!A", 0b0110001);
        computations.put("-D", 0b0001111);
        computations.put("-A", 0b0110011);
        computations.put("D+1", 0b0011111);
        computations.put("A+1", 0b0110111);
        computations.put("D-1", 0b0001110);
        computations.put("A-1", 0b0110010);
        computations.put("D+A", 0b0000010);
        computations.put("D-A", 0b0010011);
        computations.put("A-D", 0b0000111);
        computations.put("D&A", 0b0000000);
        computations.put("D|A", 0b0010101);
        computations.put("M", 0b1110000);
        computations.put("!M", 0b1110001);
        computations.put("-M", 0b1110011);
        computations.put("M+1", 0b1110111);
        computations.put("M-1", 0b1110010);
        computations.put("D+M", 0b1000010);
        computations.put("D-M", 0b1010011);
        computations.put("M-D", 0b1000111);
        computations.put("D&M", 0b1000000);
        computations.put("D|M", 0b1010101);
    }

    public ComputationInstruction(int integerRepresentation) {
        this(integerRepresentation, translate(integerRepresentation));
    }

    public ComputationInstruction(String stringRepresentation) {
        this(translate(stringRepresentation), stringRepresentation);
    }

    protected ComputationInstruction(int integerRepresentation, String stringRepresentation) {
        super(integerRepresentation, stringRepresentation);
    }

    public static String translate(int machineCode) {
        checkArgument(Instruction.isValidMachineCode(machineCode),
                "%s is not valid machineCode of any kind.",
                Integer.toBinaryString(machineCode));
        checkArgument(isValidMachineCode(machineCode), "%s is not a valid ComputationInstruction.",
                Integer.toBinaryString(machineCode));

        int[] machineCodeParts = splitMachineCode(machineCode);
        String destination = translatePart(machineCodeParts[0], destinations);
        String computation = translatePart(machineCodeParts[1], computations);

        String jump = translatePart(machineCodeParts[2], jumps);

        return (destination != null ? destination + "=" : "") +
                computation +
                (jump != null ? ";" + jump : "");
    }

    private static int[] splitMachineCode(int machineCode) {
        int[] result = new int[3];
        result[0] = BinaryUtil.extractBinaryDigits(machineCode, DESTINATION_BINARY_OFFSET, COMPUTATION_BINARY_OFFSET - DESTINATION_BINARY_OFFSET);
        result[1] = BinaryUtil.extractBinaryDigits(machineCode, COMPUTATION_BINARY_OFFSET, MARKER_BINARY_OFFSET - COMPUTATION_BINARY_OFFSET);
        result[2] = BinaryUtil.extractBinaryDigits(machineCode, 0, DESTINATION_BINARY_OFFSET);
        return result;
    }

    private static String translatePart(int machineCode, BiMap<String, Integer> partMap) {
        checkArgument(partMap.inverse().containsKey(machineCode),
                "%s is neither a valid computation, destination nor jump machineCode fragment.",
                                    machineCode);
        return partMap.inverse().get(machineCode);
    }

    public static int translate(String mnemonic) {
        String[] mnemonicParts = splitMnemonic(mnemonic);
        return translate(mnemonicParts[0], mnemonicParts[1], mnemonicParts[2]);
    }

    private static String[] splitMnemonic(String mnemonic) {
        String[] returnParts = new String[3];

        int equalsIndex = mnemonic.indexOf("=");
        int semicolonIndex = mnemonic.indexOf(";");

        if (equalsIndex != -1) {
            returnParts[0] = mnemonic.substring(0, equalsIndex);
        }
        if (semicolonIndex != -1) {
            returnParts[2] = mnemonic.substring(semicolonIndex + 1);
        }

        returnParts[1] = mnemonic.substring(
                equalsIndex != -1 ? equalsIndex + 1 : 0,
                semicolonIndex != -1 ? semicolonIndex : mnemonic.length());

        return returnParts;
    }

    private static int translate(String destMnemonic, String compMnemonic, String jmpMnemonic) {
        return (0b111 << MARKER_BINARY_OFFSET)
                | (translatePart(destMnemonic, destinations) << DESTINATION_BINARY_OFFSET)
                | (translatePart(compMnemonic, computations) << COMPUTATION_BINARY_OFFSET)
                | translatePart(jmpMnemonic, jumps);
    }

    private static int translatePart(String mnemonic, BiMap<String, Integer> partMap) {
        checkArgument(partMap.containsKey(mnemonic), "The mnemonic %s is invalid.", mnemonic);
        return partMap.get(mnemonic);
    }


    @Override
    public boolean hasMachineCode() {
        return true;
    }

    @Override
    public boolean hasMnemonic() {
        return true;
    }

    @Override
    public boolean providesSymbol() {
        return false;
    }

    @Override
    public Symbol getSymbol() {
        return null;
    }


    private static boolean isValidMachineCodePart(int machineCode, BiMap<String, Integer> partMap) {
        return partMap.inverse().containsKey(machineCode);
    }

    public static boolean isValidMachineCode(int machineCode) {
        int[] machineCodeParts = splitMachineCode(machineCode);
        return Instruction.isValidMachineCode(machineCode) &&
                isValidMachineCodePart(machineCodeParts[0], destinations) &&
                isValidMachineCodePart(machineCodeParts[1], computations) &&
                isValidMachineCodePart(machineCodeParts[2], jumps);
    }

    @Override
    public boolean isValidMachineCode() {
        return isValidMachineCode(integerRepresentation);
    }

    private static boolean isValidMnemonicPart(String mnemonic, BiMap<String, Integer> partMap) {
        return partMap.containsKey(mnemonic);
    }

    public static boolean isValidMnemonic(String mnemonic) {
        String[] mnemonicParts = splitMnemonic(mnemonic);
        return
                (mnemonicParts[0] == null || isValidMnemonicPart(mnemonicParts[0], destinations)) &&
                        (isValidMnemonicPart(mnemonicParts[1], computations)) &&
                        (mnemonicParts[2] == null || isValidMnemonicPart(mnemonicParts[2], jumps));
    }

    @Override
    public boolean isValidMnemonic() {
        return isValidMnemonic(stringRepresentation);
    }

    @Override
    public String mnemonic() {
        return stringRepresentation;
    }

    @Override
    public int machineCode() {
        return integerRepresentation;
    }

    @Override
    public String machineCodeString() {
        return Integer.toBinaryString(machineCode());
    }

    @Override
    public CommandType type() {
        return CommandType.COMPUTATION;
    }

}
