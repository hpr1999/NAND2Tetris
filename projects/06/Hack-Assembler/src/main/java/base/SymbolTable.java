package base;

import instruction.Symbol;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private Map<Symbol, Integer> symbolTable = new HashMap<>();

    public void addSymbol(Symbol symbol, int lineNumber) {
        if (symbolTable.containsKey(symbol))
            throw new IllegalStateException("An existing Symbol may not be overwritten.");
        symbolTable.put(symbol, lineNumber);
    }

    public int getLine(Symbol symbol) {
        return symbolTable.get(symbol);
    }


}
