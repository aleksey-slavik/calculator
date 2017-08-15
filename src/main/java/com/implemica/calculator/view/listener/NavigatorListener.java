package com.implemica.calculator.view.listener;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Navigator
 *
 * @author Slavik Aleksey V.
 */
public class NavigatorListener implements EventHandler<ActionEvent> {

    private static final String MENU_PANE_SELECTOR = "#navigator";

    private Scene scene;

    public NavigatorListener(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void handle(ActionEvent event) {
        AnchorPane navigator = (AnchorPane) scene.lookup(MENU_PANE_SELECTOR);
        TranslateTransition openMenu = new TranslateTransition(new Duration(350), navigator);
        openMenu.setToX(0);
        TranslateTransition closeMenu = new TranslateTransition(new Duration(350), navigator);

        if (navigator.getTranslateX() != 0) {
            openMenu.play();
        } else {
            closeMenu.setToX(-(navigator.getWidth()));
            closeMenu.play();
        }
    }
}
