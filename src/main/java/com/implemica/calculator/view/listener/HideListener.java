package com.implemica.calculator.view.listener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * Hide application
 *
 * @author Slavik Aleksey V.
 */
public class HideListener implements EventHandler<ActionEvent> {

    /**
     * Main window stage
     */
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
