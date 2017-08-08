package com.implemica.calculator.view.listener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * Expand
 *
 * @author Slavik Aleksey V.
 */
public class ExpandListener implements EventHandler<ActionEvent>{

    private Stage stage;

    public ExpandListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }
}
