package instruction;

import base.SymbolTable;
import util.MnemonicUtil;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SymbolAccessInstruction extends AddressInstruction {

    public SymbolAccessInstruction(String stringRepresentation, SymbolTable table) {
        this(translate(stringRepresentation, table), stringRepresentation, table);
    }

    protected SymbolAccessInstruction(int integerRepresentation, String stringRepresentation, SymbolTable table) {

        checkNotNull(table);
        checkNotNull(stringRepresentation);

        this.integerRepresentation = integerRepresentation;
        this.stringRepresentation = stringRepresentation;
        Symbol symbol = extractSymbol(stringRepresentation);

        createVariableIfNecessary(table, symbol);
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
        return '@' == stringRepresentation.charAt(0) && MnemonicUtil.hasLettersAndCanHaveDigits(stringRepresentation.substring(1));
    }

    public static int translate(String mnemonic, SymbolTable table) {
        createVariableIfNecessary(table, extractSymbol(mnemonic));
        checkArgument(isValidMnemonic(mnemonic), "%s is not a valid mnemonic.", mnemonic);
        return table.getLine(extractSymbol(mnemonic));
    }

    private static Symbol extractSymbol(String mnemonic) {
        return new Symbol(mnemonic.substring(1));
    }

}
