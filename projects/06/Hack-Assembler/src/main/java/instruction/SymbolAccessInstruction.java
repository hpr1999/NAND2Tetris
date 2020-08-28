package instruction;

import base.SymbolTable;
import util.BinaryUtil;

import static com.google.common.base.Preconditions.checkArgument;

public class SymbolAccessInstruction extends AddressInstruction {

//    TODO we need to deal with inbuilt Symbols, but also with variables,
//     that can be created BY an Access Instruction AND we need to deal
//      with the fact, that we might not supply a table or might not have one
//      in the super constructor that calls isValid

    private SymbolTable table = null;

    public SymbolAccessInstruction(int integerRepresentation) {
        this(integerRepresentation, translate(integerRepresentation), null);
    }

    public SymbolAccessInstruction(String stringRepresentation, SymbolTable table) {
        this(translate(stringRepresentation, table), stringRepresentation, table);
    }

    protected SymbolAccessInstruction(int integerRepresentation, String stringRepresentation, SymbolTable table) {
        this.integerRepresentation = integerRepresentation;
        this.stringRepresentation = stringRepresentation;
        this.table = table;
        if (table == null) {
            this.table = new SymbolTable() {
                @Override
                public boolean hasSymbol(Symbol symbol) {
                    return true;
                }
            };
        }
//        TODO create new Variable Symbols if necessary.
        checkArgument(!(stringRepresentation.isEmpty()) && isValid(),
                "%s and %s do not form a valid Instruction.",
                integerRepresentation, stringRepresentation);
    }

    @Override
    public boolean isValidMnemonic() {
        return isValidMnemonic(stringRepresentation, table);
    }

    public static boolean isValidMnemonic(String stringRepresentation, SymbolTable table) {
        return AddressInstruction.isValidMnemonic(stringRepresentation) ||
                AddressInstruction.ADDRESS_IDENTIFIER.equals(stringRepresentation.charAt(0)) &&
                        table.hasSymbol(extractSymbol(stringRepresentation));
    }

    public static int translate(String mnemonic, SymbolTable table) {
//        TODO if necessary, create new Variable Symbol.
        checkArgument(isValidMnemonic(mnemonic, table), "%s is not a valid mnemonic.", mnemonic);
        int referencedLine = table.getLine(extractSymbol(mnemonic));
        String stringOfLineNumber = BinaryUtil.fixedLengthBinaryString(referencedLine,
                BinaryUtil.BINARY_WORD_LENGTH - 1);
        return AddressInstruction.translate(AddressInstruction.ADDRESS_IDENTIFIER + stringOfLineNumber);
    }

    private static Symbol extractSymbol(String mnemonic) {
        return new Symbol(mnemonic.substring(1));
    }

}
