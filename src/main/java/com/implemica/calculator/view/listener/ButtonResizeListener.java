package com.implemica.calculator.view.listener;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * Resize font of button during window resize
 *
 * @author Slavik Aleksey V.
 */
public class ButtonResizeListener implements InvalidationListener {

    /**
     * Big font size of digits group
     */
    private static final int DIGIT_BIG_FONT_SIZE = 24;

    /**
     * Medium font size of digits group
     */
    private static final int DIGIT_MEDIUM_FONT_SIZE = 20;

    /**
     * Small font size of digits group
     */
    private static final int DIGIT_SMALL_FONT_SIZE = 16;

    /**
     * Big font size of binary group
     */
    private static final int BINARY_BIG_FONT_SIZE = 24;

    /**
     * Medium font size of binary group
     */
    private static final int BINARY_MEDIUM_FONT_SIZE = 20;

    /**
     * Small font size of binary group
     */
    private static final int BINARY_SMALL_FONT_SIZE = 16;

    /**
     * Big font size of unary group
     */
    private static final int UNARY_BIG_FONT_SIZE = 20;

    /**
     * Medium font size of unary group
     */
    private static final int UNARY_MEDIUM_FONT_SIZE = 17;

    /**
     * Small font size of unary group
     */
    private static final int UNARY_SMALL_FONT_SIZE = 16;

    /**
     * Big font size of clear group
     */
    private static final int CLEAR_BIG_FONT_SIZE = 24;

    /**
     * Medium font size of clear group
     */
    private static final int CLEAR_MEDIUM_FONT_SIZE = 20;

    /**
     * Small font size of clear group
     */
    private static final int CLEAR_SMALL_FONT_SIZE = 12;

    /**
     * Value of height of window.
     * If current window height bigger than given value font size of button are changed to big size.
     * If current window height less than given value font size of button are changed to medium size.
     */
    private static final double BIG_HEIGHT = 700;

    /**
     * Value of height of window.
     * If current window height bigger than given value font size of button are changed to medium size.
     * If current window height less than given value font size of button are changed to small size.
     */
    private static final double MEDIUM_HEIGHT = 600;

    /**
     * Digit group of buttons
     */
    private static ArrayList<String> digits = new ArrayList<>();

    /**
     * Binary group of buttons
     */
    private static ArrayList<String> binary = new ArrayList<>();

    /**
     * Unary group of buttons
     */
    private static ArrayList<String> unary = new ArrayList<>();

    /**
     * Clear group of buttons
     */
    private static ArrayList<String> clear = new ArrayList<>();

    /**
     * Scene of window
     */
    private Scene scene;

    public ButtonResizeListener(Scene scene) {
        this.scene = scene;
    }

    /*
     * Add buttons to groups
     */
    static {
        digits.add("#zero");
        digits.add("#one");
        digits.add("#two");
        digits.add("#three");
        digits.add("#four");
        digits.add("#five");
        digits.add("#six");
        digits.add("#seven");
        digits.add("#eight");
        digits.add("#nine");
        digits.add("#comma");

        binary.add("#equals");
        binary.add("#add");
        binary.add("#subtract");
        binary.add("#divide");
        binary.add("#multiply");
        binary.add("#backspace");

        unary.add("#negate");
        unary.add("#percent");
        unary.add("#sqr");
        unary.add("#sqrt");
        unary.add("#inverse");

        clear.add("#clear");
        clear.add("#clear_expr");
    }

    @Override
    public void invalidated(Observable observable) {
        double height = scene.getHeight();

        if (height > BIG_HEIGHT) {
            resize(DIGIT_BIG_FONT_SIZE, BINARY_BIG_FONT_SIZE, UNARY_BIG_FONT_SIZE, CLEAR_BIG_FONT_SIZE);
        } else if (height > MEDIUM_HEIGHT) {
            resize(DIGIT_MEDIUM_FONT_SIZE, BINARY_MEDIUM_FONT_SIZE, UNARY_MEDIUM_FONT_SIZE, CLEAR_MEDIUM_FONT_SIZE);
        } else {
            resize(DIGIT_SMALL_FONT_SIZE, BINARY_SMALL_FONT_SIZE, UNARY_SMALL_FONT_SIZE, CLEAR_SMALL_FONT_SIZE);
        }
    }

    /**
     * Change font size for all groups
     *
     * @param digitSize     size of digit group buttons
     * @param binarySize    size of binary group buttons
     * @param unarySize     size of unary group buttons
     * @param clearSize     size of clear group buttons
     */
    private void resize(int digitSize, int binarySize, int unarySize, int clearSize) {
        for (String id : digits) {
            Button bt = (Button) scene.lookup(id);
            bt.setStyle(getFontString(digitSize));
        }

        for (String id : binary) {
            Button digit = (Button) scene.lookup(id);
            digit.setStyle(getFontString(binarySize));
        }

        for (String id : unary) {
            Button bt = (Button) scene.lookup(id);
            bt.setStyle(getFontString(unarySize));
        }

        for (String id : clear) {
            Button bt = (Button) scene.lookup(id);
            bt.setStyle(getFontString(clearSize));
        }
    }

    /**
     * Create style string with given font size
     *
     * @param size  font size
     * @return      style with new font size
     */
    private String getFontString(int size) {
        return "-fx-font-size: " + size + "px;";
    }
}
