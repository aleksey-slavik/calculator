package com.implemica.calculator.controller.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import static com.implemica.calculator.controller.util.InputNumber.*;

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
     * Minimal value which can represent in calculator without scientific form
     */
    private static final BigDecimal MIN_PLAIN_VALUE = new BigDecimal("0.0000000000000001");

    /**
     * Input formatter
     */
    private static final DecimalFormat format = new DecimalFormat();

    /*
     * Setup "e" as exponent separator
     */
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setExponentSeparator("e");
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');
        format.setDecimalFormatSymbols(symbols);
        format.setParseBigDecimal(true);
    }

    /**
     * Return correct string representation of given number
     *
     * @param input given number
     * @return string representation of number
     */
    public static String formatNumber(BigDecimal input) {
        input = input.stripTrailingZeros();
        int inputScale = input.scale();
        int inputPrecision = input.precision();

        StringBuilder pattern;

        if (input.abs().compareTo(MIN_PLAIN_VALUE) == -1 && inputScale > MAX_PLAIN_SCALE) {
            pattern = new StringBuilder("0.###############E0");
        } else {
            int digitsIntegerPart = inputPrecision - inputScale;

            if (digitsIntegerPart > MAX_PLAIN_SCALE) {
                pattern = new StringBuilder("0.");

                if (inputScale > 0) {
                    for (int i = 0; i < inputScale; i++) {
                        pattern.append("0");
                    }
                } else {
                    pattern.append("###############");
                }

                pattern.append("E0");
            } else {
                pattern = new StringBuilder("###,###.#");

                for (int i = 0; i < MAX_PLAIN_SCALE - digitsIntegerPart; i++) {
                    pattern.append("#");
                }

                input = input.setScale(MAX_PLAIN_SCALE, BigDecimal.ROUND_HALF_UP);
            }
        }

        format.applyPattern(pattern.toString());
        return format.format(input);
    }

    /**
     * Formats current input number, which saved in {@link InputNumber} object
     *
     * @return formatted input number to formatNumber
     */
    public static String formatNumber() {
        int scale = getInputScale();
        StringBuilder pattern = new StringBuilder("###,##0.");

        if (scale != 0) {
            for (int i = 0; i < scale; i++) {
                pattern.append("0");
            }
        } else {
            pattern = new StringBuilder("###,###");
        }

        format.applyPattern(pattern.toString());
        return format.format(getInput());
    }

    /**
     * Parse {@link BigDecimal} number from given string
     *
     * @param number given string
     * @return number
     */
    public static BigDecimal parseInput(String number) throws ParseException {
        return (BigDecimal) format.parse(number);
    }
}
