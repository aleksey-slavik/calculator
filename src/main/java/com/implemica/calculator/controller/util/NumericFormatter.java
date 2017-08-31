package com.implemica.calculator.controller.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Number formatter for calculator numbers.
 *
 * @author Slavik Aleksey V.
 */
public class NumericFormatter {
    /**
     * Maximum of length of number in plain form
     */
    private static final int MAX_PLAIN_SCALE = 16;

    /**
     * Maximum of length of number with decimal separator or negative number in plain form
     */
    private static final int PLAIN_WITH_SEPARATOR_LENGTH = 17;

    /**
     * Maximum of length of negative number with decimal separator in plain form
     */
    private static final int NEGATE_PLAIN_WITH_SEPARATOR_LENGTH = 18;

    /**
     * Grouping separator for decimal format
     */
    private static final char GROUPING_SEPARATOR = ' ';

    /**
     * Decimal separator for decimal format
     */
    private static final char DECIMAL_SEPARATOR = ',';

    /**
     * Maximal value which can represent in calculator without scientific form
     */
    private static final BigDecimal MAX_PLAIN_VALUE = new BigDecimal("9999999999999999");

    /**
     * Minimal value which can represent in calculator without scientific form
     */
    private static final BigDecimal MIN_PLAIN_VALUE = new BigDecimal("0.0000000000000001");

    /**
     * Decimal separator for {@link BigDecimal}
     */
    private static final String DOT = ".";

    /**
     * Decimal separator for numbers in calculator
     */
    private static final String COMMA = ",";

    /**
     * Empty string
     */
    private static final String EMPTY = "";

    /**
     * Grouping separator
     */
    private static final String SPACE = " ";

    /**
     * Pattern for numbers which end with comma
     */
    private static final String COMMA_PATTERN = "(,$)";

    /**
     * Pattern for numbers which consist fractional part and end with zeros
     */
    private static final String COMMA_WITH_ZERO_PATTERN = "(,[0-9]*0$)";

    /**
     * Return correct string representation of given number
     *
     * @param number given number
     * @return string representation of number
     */
    public static String display(BigDecimal number) {
        return formatMathView(number.toPlainString());
    }

    /**
     * Return correct string representation of given string
     *
     * @param number given string
     * @return correct string representation
     */
    public static String display(String number) {
        Pattern commaReg = Pattern.compile(COMMA_PATTERN);
        Matcher commaMatch = commaReg.matcher(number);

        if (commaMatch.find()) {
            return number;
        }

        Pattern commaWithZerosReg = Pattern.compile(COMMA_WITH_ZERO_PATTERN);
        Matcher commaWithZerosMatch = commaWithZerosReg.matcher(number);

        if (commaWithZerosMatch.find()) {
            return number;
        }

        String currStr = number.replaceAll(SPACE, EMPTY).replace(COMMA, DOT);
        BigDecimal currNum = new BigDecimal(currStr);
        int scale = 0;

        if (BigDecimal.ZERO.compareTo(currNum) == 0) {
            return currNum.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }

        if (currStr.contains(".")) {
            if (currStr.toLowerCase().contains("e")) {
                scale = new BigDecimal(currStr).scale();
            } else {
                scale = currStr.length() - currStr.indexOf(".") - 1;
            }
        }

        return display(currNum.setScale(scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * Choose plain or engineering form of given number
     *
     * @param numberStr input number
     * @return output representation of number
     */
    private static String formatMathView(String numberStr) {
        BigDecimal number = new BigDecimal(numberStr);

        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        String stringValue;

        BigDecimal plain = new BigDecimal(formatPlainView(number).replace(" ", "").replace(",", "."));

        if (plain.abs().compareTo(MIN_PLAIN_VALUE) < 0 || plain.abs().compareTo(MAX_PLAIN_VALUE) > 0) {
            stringValue = formatEngineeringView(number);
        } else if (Math.abs(number.stripTrailingZeros().scale() - number.stripTrailingZeros().precision()) > MAX_PLAIN_SCALE) {
            stringValue = formatEngineeringView(number);
        } else {
            stringValue = formatPlainView(number);
        }

        return stringValue;
    }

    /**
     * Formatting for engineering representation of given number
     *
     * @param number given number
     * @return string representation of given number
     */
    private static String formatEngineeringView(BigDecimal number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        symbols.setGroupingSeparator(GROUPING_SEPARATOR);

        DecimalFormat df = new DecimalFormat("0.###############E0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setDecimalFormatSymbols(symbols);
        String formattedNumber = df.format(number).toLowerCase();
        return checkEngineeringView(formattedNumber);
    }

    /**
     * Check correct form for engineering representation of number
     *
     * @param number given number
     * @return correct representation of given number
     */
    private static String checkEngineeringView(String number) {
        String format = number.toLowerCase();

        if (format.contains("e") && !format.contains("e-")) {
            format = format.replace("e", "e+");
        }

        return format;
    }

    /**
     * Formatting for plain representation of given number
     *
     * @param number given number
     * @return string representation of given number
     */
    private static String formatPlainView(BigDecimal number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        symbols.setGroupingSeparator(GROUPING_SEPARATOR);

        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(getFractionDigitsCount(number.toPlainString()));
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setDecimalFormatSymbols(symbols);
        return format.format(number).toLowerCase();
    }

    /**
     * return count of digits in fractional part of number in plain form
     *
     * @param number given number
     * @return count of digits in fractional part
     */
    private static int getFractionDigitsCount(String number) {
        if (!number.contains(".")) {
            return 0;
        }

        if (number.startsWith("-0.")) {
            return NEGATE_PLAIN_WITH_SEPARATOR_LENGTH - number.indexOf(".");
        } else if (number.startsWith("-")) {
            return PLAIN_WITH_SEPARATOR_LENGTH - number.indexOf(".");
        } else if (number.startsWith("0.")) {
            return PLAIN_WITH_SEPARATOR_LENGTH - number.indexOf(".");
        }

        return MAX_PLAIN_SCALE - number.indexOf(".");
    }
}
