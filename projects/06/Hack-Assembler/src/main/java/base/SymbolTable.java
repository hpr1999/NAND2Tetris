package base;

import instruction.Symbol;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class SymbolTable {

    private final Map<Symbol, Integer> symbolTable = new HashMap<>();
    //    FIXME TEST
    //    TODO verify 16 is the correct initial value
    private int nextOpenLineForVariable = 16;

    public void addSymbol(Symbol symbol, int lineNumber) {
        checkState(symbolTable.containsKey(symbol), "An existing Symbol may not be overwritten.");
        symbolTable.put(symbol, lineNumber);
    }

    public void createVariable(Symbol symbol) {
        addSymbol(symbol, nextOpenLineForVariable++);
    }

    public boolean hasSymbol(Symbol symbol) {
        checkNotNull(symbol);
        return symbolTable.containsKey(symbol);
    }

    public int getLine(Symbol symbol) {
        checkArgument(hasSymbol(symbol));
        return symbolTable.get(symbol);
    }
}
