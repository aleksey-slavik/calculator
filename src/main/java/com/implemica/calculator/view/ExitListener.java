package com.implemica.calculator.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * Exit
 *
 * @author Slavik Aleksey V.
 */
public class ExitListener implements EventHandler<ActionEvent>{

    private Stage stage;

    public ExitListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        stage.close();
    }
}
