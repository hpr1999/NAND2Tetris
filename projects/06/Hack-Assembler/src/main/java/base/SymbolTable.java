package base;

import instruction.Symbol;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, Symbol> symbolTable = new HashMap<>();


    public Symbol getSymbol(String symbolName) {
        return symbolTable.get(symbolName);
    }

}
