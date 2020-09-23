package vm.context;

import hack.base.SymbolTable;

public class TranslationContext {

    private SymbolTable table = new SymbolTable();

    public SymbolTable getSymbolTable(){
        return table;
    }

    public String getFileName(){
        return "TODO";
    }
}
