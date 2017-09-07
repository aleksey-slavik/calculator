package com.implemica.calculator.model.enums;

/**
 * Enumeration of unary calculator operations.
 *
 * @author Slavik Aleksey V.
 */
public enum  UnaryOperator {

    NEGATE("negate"),
    INVERSE("1/"),
    SQR("sqr"),
    SQRT("√");

    /**
     * Value which formatNumber in history when given operator is used
     */
    private String text;

    UnaryOperator(String text) {
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
