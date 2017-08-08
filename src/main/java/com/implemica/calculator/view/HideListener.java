package com.implemica.calculator.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * Hide
 *
 * @author Slavik Aleksey V.
 */
public class HideListener implements EventHandler<ActionEvent>{

    private Stage stage;

    public HideListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        if (stage.isIconified()) {
            stage.setIconified(false);
        } else {
            stage.setIconified(true);
        }
    }
}
