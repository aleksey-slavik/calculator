package com.implemica.calculator.model.util;

/**
 * Enumeration of calculator operations, which need to work with history of calculator and binary operations.
 *
 * @author Slavik Aleksey V.
 */
public enum  Operator {

    EMPTY,
    NEGATE("negate"),
    ADD("+"),
    SUBTRACT("-"),
    DIVIDE("÷"),
    MULTIPLY("×"),
    INVERSE("1/"),
    SQR("sqr"),
    SQRT("√");

    /**
     * Value which display in history when given operator is used
     */
    private String text;

    Operator() {
        this.text = "";
    }

    Operator(String text) {
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
