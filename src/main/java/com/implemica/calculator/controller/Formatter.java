package com.implemica.calculator.controller;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.math.BigDecimal;
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

       return formatMathView(number.toString());
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

        if (!value.contains(",") && value.startsWith("-")) {
            return value.length() <= PLAIN_LENGTH;
        }

        if (value.contains(",") && !value.startsWith("-")) {
            return value.length() <= PLAIN_WITH_SEPARATOR_LENGTH;
        }

        if (!value.contains(",") && value.startsWith("-")) {
            return value.length() <= NEGATE_PLAIN_LENGTH;
        }

        if (value.startsWith("0,")) {
            return value.length() <= PLAIN_WITH_ZERO_AND_SEPARATOR_LENGTH;
        }

        return value.contains(",") && value.startsWith("-") && value.length() <= NEGATE_PLAIN_WITH_SEPARATOR_LENGTH;
    }

    private static boolean isEngineeringValue(BigDecimal number) {
        if (number.abs().compareTo(MIN_PLAIN_VALUE) < 0 || number.abs().compareTo(MAX_PLAIN_VALUE) > 0) {
            return true;
        }

        String value = number.toPlainString();

        return number.abs().compareTo(CRITERIA) < 0 && number.stripTrailingZeros().scale() > MAX_PLAIN_SCALE || checkLength(value);

    }

    private static String formatMathView(String numberStr) {
        BigDecimal number = new BigDecimal(numberStr);

        if (numberStr.contains(".") && !numberStr.toLowerCase().contains("e") && numberStr.length() == PLAIN_WITH_SEPARATOR_LENGTH) {
            int fractionDigitsCount = number.scale();
            BigDecimal fractionalPart = number.remainder(BigDecimal.ONE);
            BigDecimal correlation = new BigDecimal("1.e-" + fractionDigitsCount).multiply(new BigDecimal(5));
            if (fractionalPart.compareTo(correlation) < 0) {
                number = number.setScale(fractionDigitsCount - 1, BigDecimal.ROUND_HALF_UP);
            }
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
