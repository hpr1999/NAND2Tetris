package hack.instruction;

import com.google.common.collect.BiMap;
import hack.base.Instruction;
import util.BinaryUtil;
import util.ConfigUtil;

import static com.google.common.base.Preconditions.checkArgument;

public class ComputationInstruction extends Instruction {

    private static final BiMap<String, Integer> computations = ConfigUtil.biMapFromConfig("computation-comp");
    private static final BiMap<String, Integer> destinations = ConfigUtil.biMapFromConfig("computation-dest");
    private static final BiMap<String, Integer> jumps = ConfigUtil.biMapFromConfig("computation-jump");
    public static final int DESTINATION_BINARY_OFFSET = 3;
    public static final int COMPUTATION_BINARY_OFFSET = 6;
    public static final int MARKER_BINARY_OFFSET = 13;

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

    public boolean hasJump() {
        String jmpMnemonic = splitMnemonic(mnemonic())[2];
        return jmpMnemonic != null && jumps.containsKey(jmpMnemonic);
    }
}
