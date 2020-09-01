package util;

import com.google.common.base.CharMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class MnemonicUtil {

    private static final CharMatcher UPPER_CASE_LETTERS = CharMatcher.inRange('A', 'Z');
    private static final CharMatcher LOWER_CASE_LETTERS = CharMatcher.inRange('a', 'z');
    public static final CharMatcher LETTERS = UPPER_CASE_LETTERS.or(LOWER_CASE_LETTERS);

    private static final CharMatcher DIGITS = CharMatcher.inRange('0', '9');


    public static boolean numeric(String string) {
        checkNotNull(string);
        checkArgument(!string.isEmpty());
        return DIGITS.matchesAllOf(string);
    }

    public static boolean alphanumeric(String string) {
        checkNotNull(string);
        checkArgument(!string.isEmpty());
        return LETTERS.or(DIGITS).matchesAllOf(string);
    }

    public static boolean letters(String string) {
        checkNotNull(string);
        checkArgument(!string.isEmpty());
        return LETTERS.matchesAllOf(string);
    }

    public static boolean hasLettersAndCanHaveDigits(String string) {
        checkNotNull(string);
        checkArgument(!string.isEmpty());
        return LETTERS.matchesAnyOf(string) && alphanumeric(string);
    }
}
