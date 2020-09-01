package util;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class MnemonicUtilTest {

    private Function<String, Boolean> functionUnderTest;

    @Test
    void nulls() {
        assertThrows(NullPointerException.class, () -> MnemonicUtil.numeric(null));
        assertThrows(NullPointerException.class, () -> MnemonicUtil.alphanumeric(null));
        assertThrows(NullPointerException.class, () -> MnemonicUtil.letters(null));
        assertThrows(NullPointerException.class, () -> MnemonicUtil.hasLettersAndCanHaveDigits(null));
    }

    @Test
    void empties() {
        assertThrows(IllegalArgumentException.class, () -> MnemonicUtil.numeric(""));
        assertThrows(IllegalArgumentException.class, () -> MnemonicUtil.alphanumeric(""));
        assertThrows(IllegalArgumentException.class, () -> MnemonicUtil.letters(""));
        assertThrows(IllegalArgumentException.class, () -> MnemonicUtil.hasLettersAndCanHaveDigits(""));
    }

    @Test
    void numeric() {
        functionUnderTest = MnemonicUtil::numeric;

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
        functionUnderTest = MnemonicUtil::alphanumeric;

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
        functionUnderTest = MnemonicUtil::letters;

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
        functionUnderTest = MnemonicUtil::hasLettersAndCanHaveDigits;

        positive("a112312312312312");
        positive("122312312a");
        positive("awqerwfdhd");
        positive("WQERWQER");
        positive("WERWEQRWER23WEQRWER");
        positive("123213W123213");
        negative("123213213123");
        negative("|232423WAa");
    }

    private void positive(String testData) {
        assertTrue(functionUnderTest.apply(testData));
    }

    private void negative(String testData) {
        assertFalse(functionUnderTest.apply(testData));
    }

}