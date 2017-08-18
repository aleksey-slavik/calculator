package com.implemica.calculator.view.listener;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * Change font size for numeric field value
 *
 * @author Slavik Aleksey V.
 */
public class NumericResizeListener implements InvalidationListener {

    /**
     * Value of big font size for numeric field
     */
    private static final int FONT_BIG = 35;

    /**
     * Value of medium font size for numeric field
     */
    private static final int FONT_MEDIUM = 24;

    /**
     * Value of small font size fir numeric field
     */
    private static final int FONT_SMALL = 17;

    /**
     * Count of chars.
     * If current count of chars in numeric field bigger than given value font size of field are changed to big font.
     * If current count of chars in numeric field less than given value font size of field are changed to medium font.
     */
    private static final int BIG_FONT_COUNT = 11;

    /**
     * Count of chars.
     * If current count of chars in numeric field bigger than given value font size of field are changed to medium font.
     * If current count of chars in numeric field less than given value font size of field are changed to small font.
     */
    private static final int MEDIUM_FONT_COUNT = 15;

    /**
     * Id of numeric field Label
     */
    private static final String FIELD_SELECTOR = "#numericField";

    /**
     * Main window scene
     */
    private Scene scene;

    public NumericResizeListener(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void invalidated(Observable observable) {
        Label field = (Label) scene.lookup(FIELD_SELECTOR);
        int length = field.getText().length();

        int size = FONT_BIG;
        if (length > BIG_FONT_COUNT) {
            size = FONT_BIG * BIG_FONT_COUNT / length;
        }
        field.setStyle(getFontString(size));


        /*
        if (length < BIG_FONT_COUNT) {
            field.setStyle(getFontString(FONT_BIG));
        } else if (length < MEDIUM_FONT_COUNT) {
            field.setStyle(getFontString(FONT_MEDIUM));
        } else {
            field.setStyle(getFontString(FONT_SMALL));
        }
        */
    }

    /**
     * Create style string with given font size
     *
     * @param size  font size
     * @return      style with new font size
     */
    private String getFontString(int size) {
        return "-fx-font-size: " + size + "pt;";
    }
}
