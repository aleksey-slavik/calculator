package com.implemica.calculator.view.listener;

import javafx.event.EventHandler;
import javafx.event.EventType;
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
        double screenX = event.getScreenX();
        double screenY = event.getScreenY();
        EventType type = event.getEventType();

        if (type.equals(MouseEvent.MOUSE_PRESSED)) {
            xPos = stage.getX() - screenX;
            yPos = stage.getY() - screenY;
        } else if (type.equals(MouseEvent.MOUSE_DRAGGED)) {
            stage.setX(screenX + xPos);
            stage.setY(screenY + yPos);
        }
    }
}
