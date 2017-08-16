package com.implemica.calculator.view.listener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * Exit from application
 *
 * @author Slavik Aleksey V.
 */
public class ExitListener implements EventHandler<ActionEvent> {

    /**
     * Window stage
     */
    private Stage stage;

    public ExitListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        stage.close();
    }
}
