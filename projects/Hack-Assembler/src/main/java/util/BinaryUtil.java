package util;

import static com.google.common.base.Preconditions.checkArgument;

public class BinaryUtil {

    public static final int BINARY_WORD_LENGTH = 16;
    public static final int LARGEST_VALUE = setBinaryDigitToOne(0, BINARY_WORD_LENGTH) - 1;
    public static final int SMALLEST_VALUE = 0b0;

    /**
     * Outputs the given number as a String that is the binary
     * representation of that number, padded or cut to the outputDigitCount, to a maximum of 31.
     */
    public static String fixedLengthBinaryString(int number, int outputDigitCount) {
        checkArgument(outputDigitCount >= 0 && outputDigitCount <= 31);
        String paddedWithExtraOne = Integer.toBinaryString(setBinaryDigitToOne(number, outputDigitCount));
        int beginIndex = paddedWithExtraOne.length() - outputDigitCount;
        return paddedWithExtraOne.substring(beginIndex);
    }

    /**
     * Changes the given number by setting the provided binary digit to one.
     * Digit is between 0 and 31 and starts at the rightmost (least significant) bit.
     */
    public static int setBinaryDigitToOne(int numberToChange, int digit) {
        checkArgument(digit >= 0 && digit <= 31);
        return numberToChange | (0b1 << digit);
    }

    /**
     * Extract the supplied number of binary digits,
     * starting from the right at the firstRightBit from the given number.
     * The firstRightBit starts at 0. The maximum accessible digit is bit 30,
     * the sign bit cannot be accessed.
     */
    public static int extractBinaryDigits(int fromNumber, int firstRightBit, int numberOfBits) {
        checkArgument(firstRightBit >= 0);
        checkArgument(numberOfBits >= 1);
        checkArgument(firstRightBit + numberOfBits <= 31);
        int bitmask = 0b0;
        for (int i = 1; i <= numberOfBits; i++) {
            bitmask = setBinaryDigitToOne(bitmask, firstRightBit + i - 1);
        }
        return (fromNumber & bitmask) >> firstRightBit;
    }
}
