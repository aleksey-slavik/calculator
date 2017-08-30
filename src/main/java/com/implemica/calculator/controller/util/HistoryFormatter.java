package com.implemica.calculator.controller.util;

import com.implemica.calculator.model.util.Operator;

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
     * HistoryFormatter value
     */
    private String history = DEFAULT_VALUE;

    /**
     * Get current history value
     *
     * @return history value
     */
    public String getHistory() {
        return history;
    }

    /**
     * Setup history value
     *
     * @param text new history value
     */
    public void setHistory(String text) {

        history = text;
    }

    /**
     * Append value to current history
     *
     * @param text append value
     */
    public void appendHistory(String text) {
        history += SPACE + text;
    }

    /**
     * Surround given value using given function
     *
     * @param operator  given function
     * @param value     given value
     * @return surrounded value using given function
     */
    public String surround(Operator operator, String value) {

        return operator.getText() + LEFT_BRACKET + value + RIGHT_BRACKET;
    }

    /**
     * Surround last element of history using given function
     *
     * @param operator given function
     */
    public void surround(Operator operator) {

        replace(surround(operator, getLastElement()));
    }

    /**
     * Return last element of history
     *
     * @return last element of history
     */
    private String getLastElement() {
        int start = history.lastIndexOf(SPACE) + 1;
        return history.substring(start);
    }

    /**
     * Replacement of the last element of history using given expression
     *
     * @param text expression
     */
    public void replace(String text) {
        int lastSpace = history.lastIndexOf(SPACE) + 1;

        if (lastSpace == -1) {
            lastSpace = 0;
        }

        history = history.substring(0, lastSpace) + text;
    }

    /**
     * Replacement of the last sign history
     *
     * @param operator new sign
     */
    public void replaceLastSign(Operator operator) {
        replace(operator.getText());
    }

    /**
     * Set history to default
     */
    public void clearHistory() {
        setHistory(DEFAULT_VALUE);
    }

    /**
     * Return true if current history value is default
     *
     * @return  true if history is default, false otherwise
     */
    public boolean isEmpty() {
        return history.equals(DEFAULT_VALUE);
    }
}
