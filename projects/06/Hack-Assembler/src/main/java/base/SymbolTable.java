package base;

import instruction.Symbol;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class SymbolTable {

    public SymbolTable() {
        symbolTable = new HashMap<>();
        initTable();
    }

    private final Map<Symbol, Integer> symbolTable;

    private int nextOpenLineForVariable = 16;

    public void addSymbol(Symbol symbol, int lineNumber) {
        checkArgument(lineNumber >= 0, "Can't assign %s since it is a line number smaller than 0.", lineNumber);
        checkNotNull(symbol);
        checkState(!symbolTable.containsKey(symbol), "An existing Symbol may not be overwritten.");
        symbolTable.put(symbol, lineNumber);
    }

    public void createVariable(Symbol symbol) {
        addSymbol(symbol, nextOpenLineForVariable);
        nextOpenLineForVariable++;
    }

    public boolean hasSymbol(Symbol symbol) {
        checkNotNull(symbol);
        return symbolTable.containsKey(symbol);
    }

    public int getLine(Symbol symbol) {
        checkArgument(hasSymbol(symbol));
        return symbolTable.get(symbol);
    }

    protected void initTable() {
//        maybe read from config in future
        for (int i = 0; i <= 15; i++) {
            addSymbol(new Symbol("R" + i), i);
        }
        addSymbol(new Symbol("SCREEN"), 16384);
        addSymbol(new Symbol("KBD"), 24576);
        addSymbol(new Symbol("SP"), 0);
        addSymbol(new Symbol("LCL"), 1);
        addSymbol(new Symbol("ARG"), 2);
        addSymbol(new Symbol("THIS"), 3);
        addSymbol(new Symbol("THAT"), 4);

    }
}
