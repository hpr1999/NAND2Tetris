package hack.base;

import com.google.common.collect.Multimap;
import hack.instruction.Symbol;
import util.ConfigUtil;

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

    public static Multimap<Integer, Symbol> builtInsPerLine =
            ConfigUtil.multimapFromConfig("symbol-builtins",
            Integer::parseInt, ConfigUtil.listMapper(o -> new Symbol(o.toString())));
}
