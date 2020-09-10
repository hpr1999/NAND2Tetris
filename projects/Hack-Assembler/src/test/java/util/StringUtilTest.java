package util;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    private Function<String, Boolean> functionUnderTest;

    @Test
    void nulls() {
        assertThrows(NullPointerException.class, () -> StringUtil.numeric(null));
        assertThrows(NullPointerException.class, () -> StringUtil.alphanumeric(null));
        assertThrows(NullPointerException.class, () -> StringUtil.letters(null));
        assertThrows(NullPointerException.class, () -> StringUtil.hasLettersAndCanHaveDigits(null));
        assertThrows(NullPointerException.class, () -> StringUtil.cutDownWhiteSpace(null));
        assertThrows(NullPointerException.class, () -> StringUtil.stripAllWhiteSpace(null));
        assertThrows(NullPointerException.class, () -> StringUtil.stripComments(null));
        assertThrows(NullPointerException.class, () -> StringUtil.validIdentifier(null));
    }

    @Test
    void empties() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.numeric(""));
        assertThrows(IllegalArgumentException.class, () -> StringUtil.alphanumeric(""));
        assertThrows(IllegalArgumentException.class, () -> StringUtil.letters(""));
        assertThrows(IllegalArgumentException.class, () -> StringUtil.hasLettersAndCanHaveDigits(""));
        assertThrows(IllegalArgumentException.class, () -> StringUtil.validIdentifier(""));

        assertEquals("", StringUtil.cutDownWhiteSpace(""));
        assertEquals("", StringUtil.stripAllWhiteSpace(""));
        assertEquals("", StringUtil.stripComments(""));
    }

    @Test
    void numeric() {
        functionUnderTest = StringUtil::numeric;

        positive("11111");
        positive("1223524257752726");
        positive("1578667");
        negative("213213213a");
        negative("aasdsadasdsad");
        negative("adasdad1");
        negative("AAADSDADSADA");
        negative("11111|");
    }

    @Test
    void alphanumeric() {
        functionUnderTest = StringUtil::alphanumeric;

        positive("112312321313");
        positive("adasadasdsada");
        positive("ASDSADADADS");
        positive("quyzOISUPGHSN");
        positive("QWErzer1236AERr1");
        negative("?,?>");
        negative("12321313|");
        negative("asdsadasdsad123.");
    }

    @Test
    void letters() {
        functionUnderTest = StringUtil::letters;

        positive("ASDSAD");
        positive("assdwqeqwe");
        positive("Z");
        negative("ä");
        negative("asdasd1");
        negative("|");
        negative("lhlhkjhk|");
    }

    @Test
    void hasLettersAndCanHaveDigits() {
        functionUnderTest = StringUtil::hasLettersAndCanHaveDigits;

        positive("a112312312312312");
        positive("122312312a");
        positive("awqerwfdhd");
        positive("WQERWQER");
        positive("WERWEQRWER23WEQRWER");
        positive("123213W123213");
        negative("123213213123");
        negative("|232423WAa");
    }

    @Test
    void stripWhitespace() {
        assertEquals("123456789", StringUtil.stripAllWhiteSpace("1 2 3  456    7\n8\t9"));
    }

    @Test
    void stripComments() {
        assertEquals("1 2 3  456    7\n8\t9", StringUtil.stripComments("1 2 3  456    7\n8\t9//sdfsdfsdf"));
    }

    @Test
    void cutDownWhiteSpace() {
        assertEquals("1 2 3 456 7 8 9 ", StringUtil.cutDownWhiteSpace("1 2 3  456    7\n\n8\t9\n"));    }

    private void positive(String testData) {
        assertTrue(functionUnderTest.apply(testData));
    }

    private void negative(String testData) {
        assertFalse(functionUnderTest.apply(testData));
    }

}