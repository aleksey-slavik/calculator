package com.implemica.calculator.view.listener;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Resize window
 *
 * @author Slavik Aleksey V.
 */
public class ResizeListener implements EventHandler<MouseEvent> {

    /**
     * Border width
     */
    private static final double BORDER = 2;

    /**
     * Minimal window width
     */
    private static final double MIN_WIDTH = 320;

    /**
     * Minimal window height
     */
    private static final double MIN_HEIGHT = 500;

    /**
     * Offset of X coordinate
     */
    private double dx;

    /**
     * Offset of Y coordinate
     */
    private double dy;

    /**
     *
     */
    private boolean isMoveH;

    /**
     *
     */
    private boolean isMoveV;

    /**
     *
     */
    private boolean isResizeH;

    /**
     *
     */
    private boolean isResizeV;

    /**
     * Main window scene
     */
    private Scene scene;

    /**
     * Main window stage
     */
    private Stage stage;

    public ResizeListener(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    @Override
    public void handle(MouseEvent event) {
        if (MouseEvent.MOUSE_MOVED.equals(event.getEventType())) {
            changeCursor(event);
        } else if (MouseEvent.MOUSE_PRESSED.equals(event.getEventType())) {
            dx = stage.getWidth() - event.getX();
            dy = stage.getHeight() - event.getY();
        } else if (MouseEvent.MOUSE_DRAGGED.equals(event.getEventType())) {
            if (isResizeH) {
                changeWidth(event);
            }
            if (isResizeV) {
                changeHeight(event);
            }
        }
    }

    /**
     * Change cursor type and setup flags for further processing
     *
     * @param event     mouse event
     */
    private void changeCursor(MouseEvent event) {
        if (event.getX() < BORDER && event.getY() < BORDER) {
            scene.setCursor(Cursor.NW_RESIZE);
            isResizeH = true;
            isResizeV = true;
            isMoveH = true;
            isMoveV = true;
        } else if (event.getX() < BORDER && event.getY() > scene.getHeight() - BORDER) {
            scene.setCursor(Cursor.SW_RESIZE);
            isResizeH = true;
            isResizeV = true;
            isMoveH = true;
            isMoveV = false;
        } else if (event.getX() > scene.getWidth() - BORDER && event.getY() < BORDER) {
            scene.setCursor(Cursor.NE_RESIZE);
            isResizeH = true;
            isResizeV = true;
            isMoveH = false;
            isMoveV = true;
        } else if (event.getX() > scene.getWidth() - BORDER && event.getY() > scene.getHeight() - BORDER) {
            scene.setCursor(Cursor.SE_RESIZE);
            isResizeH = true;
            isResizeV = true;
            isMoveH = false;
            isMoveV = false;
        } else if (event.getX() < BORDER || event.getX() > scene.getWidth() - BORDER) {
            scene.setCursor(Cursor.E_RESIZE);
            isResizeH = true;
            isResizeV = false;
            isMoveH = (event.getX() < BORDER);
            isMoveV = false;
        } else if (event.getY() < BORDER || event.getY() > scene.getHeight() - BORDER) {
            scene.setCursor(Cursor.N_RESIZE);
            isResizeH = false;
            isResizeV = true;
            isMoveH = false;
            isMoveV = (event.getY() < BORDER);
        } else {
            scene.setCursor(Cursor.DEFAULT);
            isResizeH = false;
            isResizeV = false;
            isMoveH = false;
            isMoveV = false;
        }
    }

    /**
     * Change width of window
     *
     * @param event     mouse event
     */
    private void changeWidth(MouseEvent event) {
        double deltaX;

        if (stage.getWidth() <= MIN_WIDTH) {
            if (isMoveH) {
                deltaX = stage.getX() - event.getScreenX();
                if (event.getX() < 0) {
                    stage.setWidth(deltaX + stage.getWidth());
                    stage.setX(event.getScreenX());
                }
            } else {
                if (event.getX() + dx - stage.getWidth() > 0) {
                    stage.setWidth(event.getX() + dx);
                }
            }
        } else if (stage.getWidth() > MIN_WIDTH) {
            if (isMoveH) {
                deltaX = stage.getX() - event.getScreenX();
                stage.setWidth(deltaX + stage.getWidth());
                stage.setX(event.getScreenX());
            } else {
                stage.setWidth(event.getX() + dx);
            }
        }
    }

    /**
     * Change height of window
     * @param event     mouse event
     */
    private void changeHeight(MouseEvent event) {
        double deltaY;

        if (stage.getHeight() <= MIN_HEIGHT) {
            if (isMoveV) {
                deltaY = stage.getY() - event.getScreenY();
                if (event.getY() < 0) {
                    stage.setHeight(deltaY + stage.getHeight());
                    stage.setY(event.getScreenY());
                }
            } else {
                if (event.getY() + dy - stage.getHeight() > 0) {
                    stage.setHeight(event.getY() + dy);
                }
            }
        } else if (stage.getHeight() > MIN_HEIGHT) {
            if (isMoveV) {
                deltaY = stage.getY() - event.getScreenY();
                stage.setHeight(deltaY + stage.getHeight());
                stage.setY(event.getScreenY());
            } else {
                stage.setHeight(event.getY() + dy);
            }
        }
    }
}
