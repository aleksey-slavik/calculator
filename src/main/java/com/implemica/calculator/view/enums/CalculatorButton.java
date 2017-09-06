package com.implemica.calculator.view.enums;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

/**
 * Buttons of calculator with corresponding fxId and keycode combinations
 *
 * @author Slavik Aleksey V.
 */
public enum CalculatorButton {

    ZERO("#zero"),
    ONE("#one"),
    TWO("#two"),
    THREE("#three"),
    FOUR("#four"),
    FIVE("#five"),
    SIX("#six"),
    SEVEN("#seven"),
    EIGHT("#eight"),
    NINE("#nine"),
    COMMA("#comma"),
    NEGATE("#negate"),
    EQUALS("#equals"),
    ADD("#add"),
    SUBTRACT("#subtract"),
    DIVIDE("#divide"),
    MULTIPLY("#multiply"),
    INVERSE("#inverse"),
    SQR("#sqr"),
    SQRT("#sqrt"),
    PERCENT("#percent"),
    C("#clear"),
    CE("#clear_expr"),
    BACK("#backspace"),
    MC("#memory_clear"),
    MR("#memory_recall"),
    M_MINUS("#memory_minus"),
    M_PLUS("#memory_add"),
    MS("#memory_store");

    /**
     * fx:fxId of given button
     */
    private String fxId;

    /**
     * One-to-one correspondence between keycode combinations and buttons
     */
    private static HashMap<KeyCodeCombination, CalculatorButton> keys = new HashMap<>();

    CalculatorButton(String fxId) {

        this.fxId = fxId;
    }

    /*
     * Setup keycode combinations
     */
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

    /**
     * Return fx:id of given button
     *
     * @return fx:id of given button
     */
    public String getFXId() {
        return fxId;
    }

    /**
     * Return id of given button.
     * Difference between fx:id and fxId is that fx:fxId started with '#'
     *
     * @return id without first char
     */
    public String getId() {
        return fxId.substring(1);
    }//todo remove

    /**
     * Search button with given keycode combination
     *
     * @param event given keycode combination
     * @return button with given keycode combination
     */
    public static CalculatorButton searchButtonByEvent(KeyEvent event) {
        for (KeyCodeCombination code : keys.keySet()) {
            if (code.match(event)) {
                return keys.get(code);
            }
        }

        return null;
    }

    /**
     * Search button with given fxId
     *
     * @param id given fxId
     * @return button with given fxId
     */
    public static CalculatorButton searchButtonById(String id) {
        for (CalculatorButton button : CalculatorButton.values()) {
            if (button.getId().equals(id)) {
                return button;
            }
        }

        return null;
    }
}
