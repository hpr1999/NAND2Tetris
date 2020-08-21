package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.BinaryUtil.fixedLengthBinaryString;
import static util.BinaryUtil.setBinaryDigitToOne;

class BinaryUtilTest {

    private int testInt = 0b10000;

    @Test
    void setLeftMostDigit() {
        assertEquals(0b10000, setBinaryDigitToOne(testInt, 4));
    }

    @Test
    void setRightMostDigit() {
        assertEquals(0b10001, setBinaryDigitToOne(testInt, 0));
    }

    @Test
    void setMiddleDigit() {
        assertEquals(0b10100, setBinaryDigitToOne(testInt, 2));
    }

    @Test
    void setMultiple() {
        testInt = setBinaryDigitToOne(testInt, 0);
        testInt = setBinaryDigitToOne(testInt, 1);
        testInt = setBinaryDigitToOne(testInt, 2);
        testInt = setBinaryDigitToOne(testInt, 3);
        testInt = setBinaryDigitToOne(testInt, 4);
        assertEquals(0b11111, testInt);
    }

    @Test
    void setOutOfRange() {
        testInt = setBinaryDigitToOne(testInt, 10);
        assertEquals(0b10000010000, testInt);
    }

    @Test
    void fixedLengthSmaller() {
        assertEquals("00", fixedLengthBinaryString(testInt, 2));
    }

    @Test
    void fixedLengthLarger() {
        assertEquals("00000000000010000", fixedLengthBinaryString(testInt, 17));
    }

    @Test
    void fixedLengthSame() {
        assertEquals("10000", fixedLengthBinaryString(testInt, 5));
    }
}