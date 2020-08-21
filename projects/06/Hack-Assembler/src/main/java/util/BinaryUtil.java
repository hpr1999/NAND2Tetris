package util;

public class BinaryUtil {

    public static final int BINARY_WORD_LENGTH = 16;
    public static final int LARGEST_VALUE = setBinaryDigitToOne(0, BINARY_WORD_LENGTH) - 1;
    public static final int SMALLEST_VALUE = 0b0;

    /**
     * Outputs the given number as a String that is the binary
     * representation of that number, padded or cut to the outputDigitCount.
     */
    public static String fixedLengthBinaryString(int number, int outputDigitCount) {
        String paddedWithExtraOne = Integer.toBinaryString(setBinaryDigitToOne(number, outputDigitCount));
        int beginIndex = paddedWithExtraOne.length() - outputDigitCount;
        return paddedWithExtraOne.substring(beginIndex);
    }

    /**
     * Changes the given number by setting the provided binary digit to one.
     * Digit is between 0 and 31 and starts at the rightmost (least significant) bit.
     */
    public static int setBinaryDigitToOne(int numberToChange, int digit) {
        return numberToChange | (0b1 << digit);
    }
}
