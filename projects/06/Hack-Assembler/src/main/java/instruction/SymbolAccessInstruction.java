package instruction;

import base.SymbolTable;
import util.MnemonicUtil;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

//FIXME was schöneres als create Variable
public class SymbolAccessInstruction extends AddressInstruction {

    public SymbolAccessInstruction(String stringRepresentation, SymbolTable table, boolean createVariable) {
        this(translate(stringRepresentation, table, createVariable), stringRepresentation, table, createVariable);
    }

    protected SymbolAccessInstruction(int integerRepresentation, String stringRepresentation, SymbolTable table, boolean createVariable) {

        checkNotNull(table);
        checkNotNull(stringRepresentation);

        this.integerRepresentation = integerRepresentation;
        this.stringRepresentation = stringRepresentation;
        Symbol symbol = extractSymbol(stringRepresentation);

        if (createVariable) createVariableIfNecessary(table, symbol);
        checkArgument(!(stringRepresentation.isEmpty()) && isValid(),
                "%s and %s do not form a valid Instruction.",
                integerRepresentation, stringRepresentation);
    }

    private static void createVariableIfNecessary(SymbolTable table, Symbol symbol) {
        if (!table.hasSymbol(symbol)) {
            table.createVariable(symbol);
        }
    }

    @Override
    public boolean isValidMnemonic() {
        return isValidMnemonic(stringRepresentation);
    }

    public static boolean isValidMnemonic(String stringRepresentation) {
        return '@' == stringRepresentation.charAt(0) && MnemonicUtil.validIdentifier(stringRepresentation.substring(1));
    }

    public static int translate(String mnemonic, SymbolTable table, boolean createVariable) {
//        FIXME
        if (createVariable) createVariableIfNecessary(table, extractSymbol(mnemonic));

        checkArgument(isValidMnemonic(mnemonic), "%s is not a valid mnemonic.", mnemonic);

//        FIXME
        if (!createVariable)
            return table.hasSymbol(extractSymbol(mnemonic)) ? table.getLine(extractSymbol(mnemonic)) : 0;

        return table.getLine(extractSymbol(mnemonic));
    }

    private static Symbol extractSymbol(String mnemonic) {
        return new Symbol(mnemonic.substring(1));
    }

}
