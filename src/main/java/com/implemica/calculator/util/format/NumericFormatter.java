package com.implemica.calculator.util.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * NumericFormatter
 *
 * @author Slavik Aleksey V.
 */
public class NumericFormatter {

    private static final int NUMERIC_FIELD_SIZE = 16;

    private static final BigDecimal MAX = new BigDecimal("1E" + NUMERIC_FIELD_SIZE);

    private static final BigDecimal MIN = new BigDecimal("1E-" + (NUMERIC_FIELD_SIZE - 2));

    private static final BigDecimal DELTA = new BigDecimal("1E-" + (NUMERIC_FIELD_SIZE - 2));

    private static final int MAX_SCALE = 29;

    private static final String SCIENCE_ERROR = "E0";

    private static final String DOT = ".";

    private static final String COMMA = ",";

    private static final String EXPONENT = "E";

    private static final String EXPONENT_POSITIVE = "E+";

    private static final String EXPONENT_NEGATIVE = "E-";

    private static DecimalFormat scienceForm = new DecimalFormat("0E0");

    private static DecimalFormat plainForm = new DecimalFormat("0");

    static {
        scienceForm.setMaximumIntegerDigits(1);
        scienceForm.setMaximumFractionDigits(NUMERIC_FIELD_SIZE - 1);
        scienceForm.setGroupingUsed(false);
        scienceForm.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));

        plainForm.setGroupingUsed(false);
        plainForm.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    }

    /**
     * Correcting presentation of given number
     * @param value     given value
     * @return          correct representation of given value
     */
    public static String format(BigDecimal value) {
        value = round(value);
        String result;

        if (value.compareTo(MAX) >= 0 || value.compareTo(MIN) < 0 || value.scale() > MAX_SCALE) {
            result = scienceForm.format(value);
            if (result.contains(SCIENCE_ERROR)) {
                result = result.substring(0, result.length() - 2);
            }
        } else {
            int digitCount = getCountOfIntegerDigits(value);
            plainForm.setMaximumFractionDigits(NUMERIC_FIELD_SIZE - digitCount);
            result = plainForm.format(value);
        }

        if (result.contains(EXPONENT)) {
            int index = result.indexOf(EXPONENT);
            if (index == 1) {
                result = result.replace(EXPONENT, DOT + EXPONENT);
            }
            if (!result.contains(EXPONENT_NEGATIVE)) {
                result = result.replace(EXPONENT, EXPONENT_POSITIVE);
            }
            result = result.toLowerCase();
        }

        if (result.contains(DOT)) {
            result = result.replace(DOT, COMMA);
        }

        return result;
    }

    public static BigDecimal formatValue(BigDecimal value) {
        String format = format(value);
        format = format.replace(COMMA, DOT);
        return new BigDecimal(format);
    }

    private static int getCountOfIntegerDigits(BigDecimal value) {
        return value.toPlainString().indexOf(DOT);
    }

    /**
     * Rounding for given number
     * @param number    given number
     * @return          rounded number
     */
    public static BigDecimal round(BigDecimal number) {
        if (isIntegerNumber(number) || number.compareTo(MIN) < 0) {
            return number;
        }

        BigDecimal near = number.setScale(0, BigDecimal.ROUND_HALF_UP);

        if (number.subtract(near).abs().compareTo(DELTA) < 0) {
            return near;
        }

        return number;
    }

    /**
     * Check contains only integer digits for given number
     * @param number    given number
     * @return          true, if given number is integer, false for otherwise
     */
    private static boolean isIntegerNumber(BigDecimal number) {
        if (number.scale() <= 0) {
            return true;
        }

        BigDecimal near = number.setScale(0, BigDecimal.ROUND_HALF_UP);
        return number.compareTo(near) == 0;
    }
}
