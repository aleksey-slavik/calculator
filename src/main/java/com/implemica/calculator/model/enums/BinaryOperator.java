package com.implemica.calculator.model.enums;

/**
 * Enumeration of calculator operations, which need to work with history of calculator and binary operations.
 *
 * @author Slavik Aleksey V.
 */
public enum BinaryOperator {

    ADD("+"),
    SUBTRACT("-"),
    DIVIDE("÷"),
    MULTIPLY("×"),
    EQUALS("=");

    /**
     * Value which display in history when given operator is used
     */
    private String text;

    BinaryOperator(String text) {
        this.text = text;
    }

    /**
     * Get text representation of given operator
     *
     * @return text representation
     */
    public String getText() {
        return text;
    }
}
