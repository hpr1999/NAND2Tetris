package hack.base;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import hack.instruction.Symbol;

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
        builtInsPerLine.entries().forEach(entry ->
                addSymbol(entry.getValue(), entry.getKey()));
    }

    public static Multimap<Integer, Symbol> builtInsPerLine = MultimapBuilder.treeKeys().arrayListValues().build();

    //    TODO read from config
    static {
        for (int i = 0; i <= 15; i++) {
            builtInsPerLine.put(i, new Symbol("R" + i));
        }
        builtInsPerLine.put(16384, new Symbol("SCREEN"));
        builtInsPerLine.put(24576, new Symbol("KBD"));
        builtInsPerLine.put(0, new Symbol("SP"));
        builtInsPerLine.put(1, new Symbol("LCL"));
        builtInsPerLine.put(2, new Symbol("ARG"));
        builtInsPerLine.put(3, new Symbol("THIS"));
        builtInsPerLine.put(4, new Symbol("THAT"));
    }

}
