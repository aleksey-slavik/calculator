package com.implemica.calculator.view.listener;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Show or close navigation menu
 *
 * @author Slavik Aleksey V.
 */
public class NavigatorListener implements EventHandler<ActionEvent> {

    /**
     * Id of menu pane
     */
    private static final String MENU_PANE_SELECTOR = "#navigator";

    /**
     * Duration of menu move
     */
    private static final int MOVE_DURATION = 350;

    /**
     * Position of pane, when it hide
     */
    private static final int HIDE_TRANSLATE = -260;

    /**
     * Main window scene
     */
    private Scene scene;

    public NavigatorListener(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void handle(ActionEvent event) {
        AnchorPane navigator = (AnchorPane) scene.lookup(MENU_PANE_SELECTOR);
        TranslateTransition openMenu = new TranslateTransition(new Duration(MOVE_DURATION), navigator);
        openMenu.setToX(0);
        TranslateTransition closeMenu = new TranslateTransition(new Duration(MOVE_DURATION), navigator);

        if (navigator.getTranslateX() != 0) {
            openMenu.play();
        } else {
            closeMenu.setToX(HIDE_TRANSLATE);
            closeMenu.play();
        }
    }
}
