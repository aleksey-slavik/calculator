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
public class NumericResizeListener implements InvalidationListener{

    private static final int FONT_BIG = 35;

    private static final int FONT_MEDIUM = 24;

    private static final int FONT_SMALL = 18;

    private static final int BIG_FONT_COUNT = 12;

    private static final int MEDIUM_FONT_COUNT = 15;

    private static final String FIELD_SELECTOR = "#numericField";

    private Scene scene;

    public NumericResizeListener(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void invalidated(Observable observable) {
        Label field = (Label) scene.lookup(FIELD_SELECTOR);
        int length = field.getText().length();

        if (length < BIG_FONT_COUNT) {
            field.setStyle(getFontString(FONT_BIG));
        } else if (length < MEDIUM_FONT_COUNT) {
            field.setStyle(getFontString(FONT_MEDIUM));
        } else {
            field.setStyle(getFontString(FONT_SMALL));
        }
    }

    private String getFontString(int size) {
        return "-fx-font-size: " + size + "pt;";
    }
}
