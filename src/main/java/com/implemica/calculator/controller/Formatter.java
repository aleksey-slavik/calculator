package com.implemica.calculator.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Number formatter.
 *
 * @author Slavik Aleksey V.
 */
public class Formatter {

    private static final int MAX_PLAIN_SCALE = 16;
    private static final int PLAIN_LENGTH = 16;
    private static final int PLAIN_WITH_SEPARATOR_LENGTH = 17;
    private static final int NEGATE_PLAIN_LENGTH = 17;
    private static final int PLAIN_WITH_ZERO_AND_SEPARATOR_LENGTH = 18;
    private static final int NEGATE_PLAIN_WITH_SEPARATOR_LENGTH = 19;
    private static final char GROUPING_SEPARATOR = ' ';
    private static final char DECIMAL_SEPARATOR = ',';
    private static final BigDecimal MAX_PLAIN_VALUE = new BigDecimal("9999999999999999");
    private static final BigDecimal MIN_PLAIN_VALUE = new BigDecimal("0.0000000000000001");
    /**
     * Criteria for switch to engineering mode
     */
    private static final BigDecimal CRITERIA = new BigDecimal("0.001");

    public static String display(BigDecimal number) {
        return formatMathView(number.toPlainString());
    }

    public static String display(String number) {
        Pattern commaReg = Pattern.compile("(,$)");
        Matcher commaMatch = commaReg.matcher(number);

        if (commaMatch.find()) {
            return number;
        }

        Pattern commaWithZerosReg = Pattern.compile("(,0*$)");
        Matcher commaWithZerosMatch = commaWithZerosReg.matcher(number);

        if (commaWithZerosMatch.find()) {
            return number;
        }

        String currStr = number.replaceAll(" ", "").replace(",", ".");
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

    private static boolean checkLength(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        if (value.startsWith("0.") && !value.startsWith("-")) {
            return value.length() <= PLAIN_WITH_ZERO_AND_SEPARATOR_LENGTH;
        }

        if (!value.contains(".") && !value.startsWith("-")) {
            return value.length() <= PLAIN_LENGTH;
        }

        if (value.contains(".") && !value.startsWith("-")) {
            return value.length() <= PLAIN_WITH_SEPARATOR_LENGTH;
        }

        if (!value.contains(".") && value.startsWith("-")) {
            return value.length() <= NEGATE_PLAIN_LENGTH;
        }

        return value.contains(".") && value.startsWith("-") && value.length() <= NEGATE_PLAIN_WITH_SEPARATOR_LENGTH;
    }

    private static boolean isEngineeringValue(BigDecimal number) {
        if (number.abs().compareTo(MIN_PLAIN_VALUE) < 0 || number.abs().compareTo(MAX_PLAIN_VALUE) > 0) {
            return number.setScale(0, BigDecimal.ROUND_HALF_UP).toString().length() != MAX_PLAIN_SCALE;
        }

        String value = number.toPlainString();
        return number.stripTrailingZeros().scale() > MAX_PLAIN_SCALE || !checkLength(value);
    }

    private static String formatMathView(String numberStr) {
        BigDecimal number = new BigDecimal(numberStr);

        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        String stringValue;

        if (isEngineeringValue(number)) {
            stringValue = formatEngineeringView(number);
        } else {
            stringValue = formatPlainView(number);
        }
        return stringValue;
    }

    private static String formatEngineeringView(BigDecimal number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        symbols.setGroupingSeparator(GROUPING_SEPARATOR);

        DecimalFormat format = new DecimalFormat("0.###############E0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setDecimalFormatSymbols(symbols);
        String formattedNumber = format.format(number).toLowerCase();
        return checkEngineeringView(formattedNumber);
    }

    private static String checkEngineeringView(String number) {
        String format = number.toLowerCase();

        if (format.contains("e") && !format.contains("e-")) {
            format = format.replace("e", "e+");
        }

        if (format.contains("e+0")) {
            format = format.replace("e+0","");
        }

        return format;
    }

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

    private static int getFractionDigitsCount(String number) {
        if (!number.contains(".")) {
            return 0;
        }
        if (number.startsWith("-0.")) {
            return NEGATE_PLAIN_WITH_SEPARATOR_LENGTH - number.indexOf(".") - 1;
        } else if (number.startsWith("-")) {
            return NEGATE_PLAIN_WITH_SEPARATOR_LENGTH - number.indexOf(".") - 2;
        } else if (number.startsWith("0.")) {
            return PLAIN_WITH_SEPARATOR_LENGTH - number.indexOf(".");
        }
        return PLAIN_WITH_SEPARATOR_LENGTH - number.indexOf(".") - 1;
    }
}
