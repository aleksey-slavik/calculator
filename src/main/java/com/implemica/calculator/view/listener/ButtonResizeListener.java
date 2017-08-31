package com.implemica.calculator.view.listener;

import com.implemica.calculator.view.enums.CalculatorButton;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.ArrayList;

import static com.implemica.calculator.view.enums.CalculatorButton.*;

/**
 * Resize font of button during window resize
 *
 * @author Slavik Aleksey V.
 */
public class ButtonResizeListener implements InvalidationListener {

    /**
     * Big font size of digits group
     */
    private static final int DIGIT_BIG_FONT_SIZE = 33;

    /**
     * Medium font size of digits group
     */
    private static final int DIGIT_MEDIUM_FONT_SIZE = 28;

    /**
     * Small font size of digits group
     */
    private static final int DIGIT_SMALL_FONT_SIZE = 23;

    /**
     * Big font size of binary group
     */
    private static final int BINARY_BIG_FONT_SIZE = 30;

    /**
     * Medium font size of binary group
     */
    private static final int BINARY_MEDIUM_FONT_SIZE = 26;

    /**
     * Small font size of binary group
     */
    private static final int BINARY_SMALL_FONT_SIZE = 22;

    /**
     * Big font size of unary group
     */
    private static final int UNARY_BIG_FONT_SIZE = 24;

    /**
     * Medium font size of unary group
     */
    private static final int UNARY_MEDIUM_FONT_SIZE = 20;

    /**
     * Small font size of unary group
     */
    private static final int UNARY_SMALL_FONT_SIZE = 16;

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
    private static ArrayList<CalculatorButton> digits = new ArrayList<>();

    /**
     * Binary group of buttons
     */
    private static ArrayList<CalculatorButton> binary = new ArrayList<>();

    /**
     * Unary group of buttons
     */
    private static ArrayList<CalculatorButton> unary = new ArrayList<>();

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
        digits.add(ZERO);
        digits.add(ONE);
        digits.add(TWO);
        digits.add(THREE);
        digits.add(FOUR);
        digits.add(FIVE);
        digits.add(SIX);
        digits.add(SEVEN);
        digits.add(EIGHT);
        digits.add(NINE);
        digits.add(COMMA);

        binary.add(EQUALS);
        binary.add(ADD);
        binary.add(SUBTRACT);
        binary.add(DIVIDE);
        binary.add(MULTIPLY);
        binary.add(BACK);

        unary.add(NEGATE);
        unary.add(PERCENT);
        unary.add(SQR);
        unary.add(SQRT);
        unary.add(INVERSE);
        unary.add(C);
        unary.add(CE);
    }

    @Override
    public void invalidated(Observable observable) {
        double height = scene.getHeight();
        int digit, binary, unary;

        if (height > BIG_HEIGHT) {
            digit = DIGIT_BIG_FONT_SIZE;
            binary = BINARY_BIG_FONT_SIZE;
            unary = UNARY_BIG_FONT_SIZE;
        } else if (height > MEDIUM_HEIGHT) {
            digit = DIGIT_MEDIUM_FONT_SIZE;
            binary = BINARY_MEDIUM_FONT_SIZE;
            unary = UNARY_MEDIUM_FONT_SIZE;
        } else {
            digit = DIGIT_SMALL_FONT_SIZE;
            binary = BINARY_SMALL_FONT_SIZE;
            unary = UNARY_SMALL_FONT_SIZE;
        }

        resize(digit, binary, unary);
    }

    /**
     * Change font size for all groups
     *
     * @param digitSize  size of digit group buttons
     * @param binarySize size of binary group buttons
     * @param unarySize  size of unary group buttons
     */
    private void resize(int digitSize, int binarySize, int unarySize) {
        for (CalculatorButton button : digits) {
            Button bt = (Button) scene.lookup(button.getFXId());
            bt.setStyle(getFontString(digitSize));
        }

        for (CalculatorButton button : binary) {
            Button digit = (Button) scene.lookup(button.getFXId());
            digit.setStyle(getFontString(binarySize));
        }

        for (CalculatorButton button : unary) {
            Button bt = (Button) scene.lookup(button.getFXId());
            bt.setStyle(getFontString(unarySize));
        }
    }

    /**
     * Create style string with given font size
     *
     * @param size font size
     * @return style with new font size
     */
    private String getFontString(int size) {
        return "-fx-font-size: " + size + "px;";
    }
}
