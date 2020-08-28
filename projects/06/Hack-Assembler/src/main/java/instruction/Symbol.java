package instruction;

import com.google.common.base.CharMatcher;

import java.util.Objects;

public class Symbol {
    private static final CharMatcher UPPER_CASE_LETTERS = CharMatcher.inRange('A', 'Z');
    private static final CharMatcher DIGITS = CharMatcher.inRange('0', '9');
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

//    TODO TEST
    public static boolean isJumpLabelOrBuiltIn(CharSequence label) {
        return UPPER_CASE_LETTERS.or(DIGITS).matchesAllOf(label)
                && UPPER_CASE_LETTERS.matchesAnyOf(label);
    }
}
