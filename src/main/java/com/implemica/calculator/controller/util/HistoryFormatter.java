package com.implemica.calculator.controller.util;

import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.enums.UnaryOperator;
import com.implemica.calculator.model.util.Operation;

import java.util.ArrayList;
import java.util.HashMap;

import static com.implemica.calculator.controller.util.NumericFormatter.*;

/**
 * HistoryFormatter model.
 * Consist set of method for work with history
 *
 * @author Slavik Aleksey V.
 */
public class HistoryFormatter {

    /**
     * Default history value
     */
    private static final String DEFAULT_VALUE = "";

    /**
     * Separator between expressions
     */
    private static final String SPACE = " ";

    /**
     * Left bracket
     */
    private static final String LEFT_BRACKET = "(";

    /**
     * Right bracket
     */
    private static final String RIGHT_BRACKET = ")";

    /**
     * History representation of binary operations
     */
    private static HashMap<BinaryOperator, String> binaryMap = new HashMap<>();

    /**
     * History representation of unary operations
     */
    private static HashMap<UnaryOperator, String> unaryMap = new HashMap<>();

    static {
        binaryMap.put(BinaryOperator.ADD, "+");
        binaryMap.put(BinaryOperator.SUBTRACT, "-");
        binaryMap.put(BinaryOperator.DIVIDE, "÷");
        binaryMap.put(BinaryOperator.MULTIPLY, "×");

        unaryMap.put(UnaryOperator.INVERSE, "1/");
        unaryMap.put(UnaryOperator.NEGATE, "negate");
        unaryMap.put(UnaryOperator.SQR, "sqr");
        unaryMap.put(UnaryOperator.SQRT, "√");
    }

    /**
     * Surround given value using given function
     *
     * @param operator given function
     * @param value    given value
     * @return surrounded value using given function
     */
    private static String surround(UnaryOperator operator, String value) {
        return unaryMap.get(operator) + LEFT_BRACKET + value + RIGHT_BRACKET;
    }

    /**
     * Return string representation of current history.
     * History is contained in given {@link Calculator} object
     *
     * @param calculator given statement of calculator
     * @return string representation of history
     */
    public static String formatHistory(Calculator calculator) {
        StringBuilder builder = new StringBuilder(DEFAULT_VALUE);

        for (Operation operation : calculator.getHistory()) {
            StringBuilder tmp = new StringBuilder(DEFAULT_VALUE);
            tmp.append(formatNumber(operation.getOperand()).replace(" ", ""));

            for (UnaryOperator unary : operation.getUnaryOperators()) {
                tmp.replace(0, tmp.length(), surround(unary, tmp.toString()));
            }

            tmp.append(SPACE);
            BinaryOperator binary = operation.getBinaryOperator();

            if (binary != null) {
                tmp.append(binaryMap.get(binary));
                tmp.append(SPACE);
            }

            builder.append(tmp);
        }

        String res = builder.toString();

        return res.trim();
    }
}
