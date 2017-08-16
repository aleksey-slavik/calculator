package com.implemica.calculator.view.listener;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Move window
 *
 * @author Slavik Aleksey V.
 */
public class MoveWindowListener implements EventHandler<MouseEvent> {

    /**
     * X value of current window position
     */
    private double xPos;

    /**
     * Y value of current window position
     */
    private double yPos;

    /**
     * Main window stage
     */
    private Stage stage;

    public MoveWindowListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(MouseEvent event) {
        if (MouseEvent.MOUSE_PRESSED.equals(event.getEventType())) {
            xPos = stage.getX() - event.getScreenX();
            yPos = stage.getY() - event.getScreenY();
        } else if (MouseEvent.MOUSE_DRAGGED.equals(event.getEventType())) {
            stage.setX(event.getScreenX() + xPos);
            stage.setY(event.getScreenY() + yPos);
        }
    }
}
