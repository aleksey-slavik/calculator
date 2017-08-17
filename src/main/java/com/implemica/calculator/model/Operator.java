package com.implemica.calculator.model;

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

    private String text;

    Operator() {

        this.text = "";
    }

    Operator(String text) {

        this.text = text;
    }

    public String getText() {

        return text;
    }
}
