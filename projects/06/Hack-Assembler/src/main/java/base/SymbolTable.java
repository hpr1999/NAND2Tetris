package base;

import instruction.Symbol;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class SymbolTable {

    private final Map<Symbol, Integer> symbolTable = new HashMap<>();

    public void addSymbol(Symbol symbol, int lineNumber) {
        checkState(symbolTable.containsKey(symbol), "An existing Symbol may not be overwritten.");
        symbolTable.put(symbol, lineNumber);
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
