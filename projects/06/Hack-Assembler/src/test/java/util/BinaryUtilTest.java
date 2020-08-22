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

    @Test
    void extractFirst() {
        assertEquals(0b1, BinaryUtil.extractBinaryDigits(0b001, 0, 1));
        assertEquals(0b0, BinaryUtil.extractBinaryDigits(0b110, 0, 1));
    }

    @Test
    void extractLast() {
        assertEquals(0b0, BinaryUtil.extractBinaryDigits(0b001, 30, 1));
        assertEquals(0b1, BinaryUtil.extractBinaryDigits(0b01000000000000000000000000000000, 30, 1));

    }

    @Test
    void extractMiddle() {
        assertEquals(0b1, BinaryUtil.extractBinaryDigits(0b1000000, 6, 1));
        assertEquals(0b0, BinaryUtil.extractBinaryDigits(0b1111111111111101111111111, 10, 1));
    }

    @Test
    void extractMultiple() {
        assertEquals(0b11, BinaryUtil.extractBinaryDigits(0b011, 0, 2));
        assertEquals(0b00, BinaryUtil.extractBinaryDigits(0b100, 0, 2));
        assertEquals(0b00, BinaryUtil.extractBinaryDigits(0b001, 29, 2));
        assertEquals(0b11, BinaryUtil.extractBinaryDigits(0b01100000000000000000000000000000, 29, 2));
        assertEquals(0b11, BinaryUtil.extractBinaryDigits(0b11000000, 6, 2));
        assertEquals(0b00, BinaryUtil.extractBinaryDigits(0b1111111111111001111111111, 10, 2));
        assertEquals(0b1111111111111001111111111, BinaryUtil.extractBinaryDigits(0b1111111111111001111111111, 0, 25));
    }
}