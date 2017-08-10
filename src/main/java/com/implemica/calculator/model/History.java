package com.implemica.calculator.model;

/**
 * History model
 *
 * @author Slavik Aleksey V.
 */
public class History {

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
     * History value
     */
    private String history;

    /**
     * Default constructor with initialization history value
     */
    public History() {
        history = DEFAULT_VALUE;
    }

    /**
     * Get current history value
     *
     * @return  history value
     */
    public String getHistory() {
        return history;
    }

    /**
     * Setup history value
     *
     * @param text  new history value
     */
    public void setHistory(String text) {
        history = text;
    }

    /**
     * Append value to current history
     *
     * @param text  append value
     */
    public void appendHistory(String text) {
        history += text;
    }

    /**
     * Surround given value using given function
     *
     * @param func  given function
     * @param value given value
     * @return      surrounded value using given function
     */
    public String surround(String func, String value){
        return func + LEFT_BRACKET + value + RIGHT_BRACKET;
    }

    /**
     * Surround last element of history using given function
     *
     * @param func  given function
     */
    public void surround(String func) {
        if (history.contains(SPACE)) {
            replace(SPACE + surround(func, getLastElement()));
        } else {
            replace(surround(func, getLastElement()));
        }
    }

    /**
     * Return last element of history
     *
     * @return  last element of history
     */
    private String getLastElement() {
        int start = history.lastIndexOf(SPACE) + 1;
        return history.substring(start);
    }

    /**
     * Replacement of the last element of history using given expression
     *
     * @param text  expression
     */
    private void replace(String text) {
        int lastSpace = history.lastIndexOf(SPACE);

        if (lastSpace == -1) {
            lastSpace = 0;
        }

        history = history.substring(0, lastSpace) + text;
    }

    /**
     * Replacement of the last sign history
     *
     * @param sign new sign
     */
    public void replaceLastSign(String sign) {
        replace(SPACE + sign);
    }

    /**
     * Set history to default
     */
    public void clearHistory() {
        setHistory(DEFAULT_VALUE);
    }

    /**
     * Return true if history is empty
     */
    public boolean isEmpty() {
        return history.equals(DEFAULT_VALUE);
    }
}
