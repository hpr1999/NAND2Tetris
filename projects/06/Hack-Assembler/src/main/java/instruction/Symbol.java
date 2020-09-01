package instruction;

import java.util.Objects;

public class Symbol {
    private String label;

    public Symbol(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(label, symbol.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

}
