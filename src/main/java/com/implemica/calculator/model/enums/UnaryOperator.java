package com.implemica.calculator.model.enums;

/**
 * Created by Joker on 03.09.2017.
 */
public enum  UnaryOperator {

    NEGATE("negate"),
    INVERSE("1/"),
    SQR("sqr"),
    SQRT("√");

    /**
     * Value which display in history when given operator is used
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
