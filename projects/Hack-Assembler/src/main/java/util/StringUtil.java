package util;

import com.google.common.base.CharMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class StringUtil {

    private static final CharMatcher UPPER_CASE_LETTERS = CharMatcher.inRange('A', 'Z');
    private static final CharMatcher LOWER_CASE_LETTERS = CharMatcher.inRange('a', 'z');
    private static final CharMatcher LETTERS = UPPER_CASE_LETTERS.or(LOWER_CASE_LETTERS);
    private static final CharMatcher DIGITS = CharMatcher.inRange('0', '9');
    private static final CharMatcher ALLOWED_SPECIAL_CHARS = CharMatcher.anyOf("-_.#$");

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

    public static boolean validIdentifier(String string) {
        checkNotNull(string);
        checkArgument(!string.isEmpty());
        return LETTERS.matchesAnyOf(string) && LETTERS.or(DIGITS).or(ALLOWED_SPECIAL_CHARS).matchesAllOf(string);
    }

    public static boolean hasLettersAndCanHaveDigits(String string) {
        checkNotNull(string);
        checkArgument(!string.isEmpty());
        return LETTERS.matchesAnyOf(string) && alphanumeric(string);
    }

    public static String stripAllWhiteSpace(String string) {
        return CharMatcher.whitespace().removeFrom(string);
    }

    public static String stripComments(String line) {
        if (line.contains("//")) {
            return line.substring(0, line.indexOf("//"));
        }
        return line;
    }

    //    TODO TEST
    public static String cutDownWhiteSpace(String string) {
        String onlySimpleWhiteSpace = CharMatcher.whitespace().or(CharMatcher.is(' ').negate()).removeFrom(string);
        return CharMatcher.is(' ').collapseFrom(onlySimpleWhiteSpace, ' ');
    }

}
