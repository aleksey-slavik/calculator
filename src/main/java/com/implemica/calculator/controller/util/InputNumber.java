package com.implemica.calculator.controller.util;

import java.math.BigDecimal;

/**
 * Representation of number which is entered in numeric field
 *
 * @author Slavik Aleksey V.
 */
public class InputNumber {
    /**
     * Maximum digits count
     */
    private static final int MAX_DIGITS = 16;

    /**
     * Decimal part
     */
    private static int scale;

    /**
     * Shows if point is present
     */
    private static boolean isPointSet = false;

    /**
     * Input number
     */
    private static BigDecimal input = BigDecimal.ZERO;

    /**
     * Add digit to input number
     *
     * @param digit input digit
     */
    public static void appendDigit(String digit) {
        if (canInput()) {
            if (isPointSet) {
                scale++;
            }

            input = input.multiply(BigDecimal.TEN).add(new BigDecimal(digit));
        }
    }

    /**
     * Returns input number
     *
     * @return input number
     */
    public static BigDecimal getInput() {
        return input.movePointLeft(scale);
    }

    /**
     * Adds point to input number
     */
    public static void addPointToInput() {
        isPointSet = true;
    }

    /**
     * Verify is point set
     *
     * @return bool point set
     */
    public static boolean isInputPointSet() {
        return isPointSet;
    }

    /**
     * Get scale of input number
     *
     * @return scale
     */
    public static int getInputScale() {
        return scale;
    }

    /**
     * Backspace operation on input number
     */
    public static void backspaceInput() {
        if (isPointSet) {
            if (scale > 0) {
                input = input.divideToIntegralValue(BigDecimal.TEN);
                scale--;
            } else {
                isPointSet = false;
            }
        } else {
            input = input.divideToIntegralValue(BigDecimal.TEN);
        }
    }

    /**
     * Clear input number
     */
    public static void clearInput() {
        scale = 0;
        input = BigDecimal.ZERO;
        isPointSet = false;
    }

    /**
     * Checks if input number reach limit
     *
     * @return permission for input
     */
    public static boolean canInput() {
        return input.precision() < MAX_DIGITS && scale < MAX_DIGITS;
    }
}
