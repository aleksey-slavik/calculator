package com.implemica.calculator.controller.util;

import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.enums.UnaryOperator;
import com.implemica.calculator.model.util.Operation;

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
     * Surround given value using given function
     *
     * @param operator given function
     * @param value    given value
     * @return surrounded value using given function
     */
    private static String surround(UnaryOperator operator, String value) {
        return operator.getText() + LEFT_BRACKET + value + RIGHT_BRACKET;
    }

    /**
     * Return string representation of current history.
     * History is contained in given {@link Calculator} object
     *
     * @param calculator given statement of calculator
     * @return string representation of history
     */
    public static String parseHistory(Calculator calculator) {
        StringBuilder builder = new StringBuilder(DEFAULT_VALUE);

        for (Operation operation : calculator.getHistory()) {
            StringBuilder tmp = new StringBuilder(DEFAULT_VALUE);
            tmp.append(operation.getOperand());

            for (UnaryOperator unary : operation.getUnaryOperators()) {
                tmp.replace(0, tmp.length(), surround(unary, tmp.toString()));
            }

            tmp.append(SPACE);
            BinaryOperator binary = operation.getBinaryOperator();

            if (binary != null) {
                tmp.append(binary.getText());
                tmp.append(SPACE);
            }

            builder.append(tmp);
        }

        String res = builder.toString();

        if (res.endsWith(SPACE)) {
            res = res.substring(0, res.length() - 1);
        }

        return res;
    }
}
