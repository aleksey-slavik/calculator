package com.implemica.calculator.view.enums;

/**
 * Buttons
 *
 * @author Slavik Aleksey V.
 */
public enum CalculatorButton {

    ZERO    ("zero"),
    ONE     ("one"),
    TWO     ("two"),
    THREE   ("three"),
    FOUR    ("four"),
    FIVE    ("five"),
    SIX     ("six"),
    SEVEN   ("seven"),
    EIGHT   ("eight"),
    NINE    ("nine"),
    COMMA   ("comma"),
    NEGATE  ("negate"),
    EQUALS  ("equals"),
    ADD     ("add"),
    SUBTRACT("subtract"),
    DIVIDE  ("divide"),
    MULTIPLY("multiply"),
    INVERSE ("inverse"),
    SQR     ("sqr"),
    SQRT    ("sqrt"),
    PERCENT ("percent"),
    C       ("clear"),
    CE      ("clear_expr"),
    BACK    ("backspace"),
    MC      ("memory_clear"),
    MR      ("memory_recall"),
    M_MINUS ("memory_minus"),
    M_PLUS  ("memory_add"),
    MS      ("memory_store"),
    M       ("memory"),
    HISTORY (),
    MENU    (),
    MINIMIZE(),
    EXPAND  (),
    CLOSE   ();

    private String id;

    CalculatorButton(){}

    CalculatorButton(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
