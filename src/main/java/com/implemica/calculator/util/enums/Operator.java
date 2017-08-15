package com.implemica.calculator.util.enums;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

/**
 * Operators
 *
 * @author Slavik Aleksey V.
 */
public enum Operator {

    EMPTY,
    ZERO("zero", "0"),
    ONE("one", "1"),
    TWO("two", "2"),
    THREE("three", "3"),
    FOUR("four", "4"),
    FIVE("five", "5"),
    SIX("six", "6"),
    SEVEN("seven", "7"),
    EIGHT("eight", "8"),
    NINE("nine", "9"),
    COMMA("comma", ","),
    NEGATE("negate", "±"),
    EQUALS("equals", "="),
    ADD("add", "+"),
    SUBTRACT("subtract", "-"),
    DIVIDE("divide", "÷"),
    MULTIPLY("multiply", "×"),
    INVERSE("inverse", "1/"),
    SQR("sqr", "sqr"),
    SQRT("sqrt", "√"),
    PERCENT("percent", "%"),
    C("clear", "C"),
    CE("clear_expr", "CE"),
    BACK("backspace", "⌫"),
    MC("memory_clear", "MC"),
    MR("memory_recall", "MR"),
    M_MINUS("memory_minus", "M-"),
    M_PLUS("memory_add", "M+"),
    MS("memory_store", "MS"),
    M("memory", "M"),
    HISTORY(),
    MENU(),
    MINIMIZE(),
    EXPAND(),
    CLOSE();

    private String id;
    private String text;
    private static HashMap<KeyCodeCombination, Operator> keys = new HashMap<>();

    static {
        keys.put(new KeyCodeCombination(KeyCode.DIGIT0), ZERO);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD0), ZERO);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT1), ONE);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD1), ONE);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT2), TWO);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD2), TWO);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT3), THREE);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD3), THREE);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT4), FOUR);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD4), FOUR);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT5), FIVE);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD5), FIVE);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT6), SIX);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD6), SIX);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT7), SEVEN);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD7), SEVEN);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT8), EIGHT);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD8), EIGHT);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT9), NINE);
        keys.put(new KeyCodeCombination(KeyCode.NUMPAD9), NINE);
        keys.put(new KeyCodeCombination(KeyCode.COMMA), COMMA);
        keys.put(new KeyCodeCombination(KeyCode.F9), NEGATE);
        keys.put(new KeyCodeCombination(KeyCode.EQUALS), EQUALS);
        keys.put(new KeyCodeCombination(KeyCode.ENTER), EQUALS);
        keys.put(new KeyCodeCombination(KeyCode.ADD), ADD);
        keys.put(new KeyCodeCombination(KeyCode.SUBTRACT), SUBTRACT);
        keys.put(new KeyCodeCombination(KeyCode.MINUS), SUBTRACT);
        keys.put(new KeyCodeCombination(KeyCode.DIVIDE), DIVIDE);
        keys.put(new KeyCodeCombination(KeyCode.SLASH), DIVIDE);
        keys.put(new KeyCodeCombination(KeyCode.MULTIPLY), MULTIPLY);
        keys.put(new KeyCodeCombination(KeyCode.R), INVERSE);
        keys.put(new KeyCodeCombination(KeyCode.Q), SQR);
        keys.put(new KeyCodeCombination(KeyCode.ESCAPE), C);
        keys.put(new KeyCodeCombination(KeyCode.DELETE), CE);
        keys.put(new KeyCodeCombination(KeyCode.BACK_SPACE), BACK);
        keys.put(new KeyCodeCombination(KeyCode.EQUALS, KeyCodeCombination.SHIFT_DOWN), ADD);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT8, KeyCodeCombination.SHIFT_DOWN), MULTIPLY);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT2, KeyCodeCombination.SHIFT_DOWN), SQRT);
        keys.put(new KeyCodeCombination(KeyCode.DIGIT5, KeyCodeCombination.SHIFT_DOWN), PERCENT);
        keys.put(new KeyCodeCombination(KeyCode.L, KeyCodeCombination.CONTROL_DOWN), MC);
        keys.put(new KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN), MR);
        keys.put(new KeyCodeCombination(KeyCode.Q, KeyCodeCombination.CONTROL_DOWN), M_MINUS);
        keys.put(new KeyCodeCombination(KeyCode.P, KeyCodeCombination.CONTROL_DOWN), M_PLUS);
        keys.put(new KeyCodeCombination(KeyCode.M, KeyCodeCombination.CONTROL_DOWN), MS);
    }

    Operator() {
        id = "";
        text = "";
    }

    Operator(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public static Operator searchById(String id) {
        for (Operator item : values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }

        throw new IllegalStateException("Can't find button with id " + id);
    }

    public static Operator searchButtonByEvent(KeyEvent event) {
        for (KeyCodeCombination code : keys.keySet()) {
            if (code.match(event)) {
                return keys.get(code);
            }
        }

        return null;
    }
}
