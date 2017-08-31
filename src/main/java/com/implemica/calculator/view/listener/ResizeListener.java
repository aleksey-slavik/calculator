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
     * True, if window must be resize to right direction
     */
    private boolean isRightResize;

    /**
     * True, if window must be resize to bottom direction
     */
    private boolean isBottomResize;

    /**
     * True, if window must be resize to left direction
     */
    private boolean isLeftResize;

    /**
     * True, if window must be resize to top direction
     */
    private boolean isTopResize;

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
            if (isLeftResize) {
                changeWidth(event);
            }
            if (isTopResize) {
                changeHeight(event);
            }
        }
    }

    /**
     * Change cursor type and setup flags for further processing
     *
     * @param event mouse event
     */
    private void changeCursor(MouseEvent event) {
        Cursor cursor;

        if (event.getX() < BORDER && event.getY() < BORDER) {
            cursor = Cursor.NW_RESIZE;
            isLeftResize = true;
            isTopResize = true;
            isRightResize = true;
            isBottomResize = true;
        } else if (event.getX() < BORDER && event.getY() > scene.getHeight() - BORDER) {
            cursor = Cursor.SW_RESIZE;
            isLeftResize = true;
            isTopResize = true;
            isRightResize = true;
            isBottomResize = false;
        } else if (event.getX() > scene.getWidth() - BORDER && event.getY() < BORDER) {
            cursor = Cursor.NE_RESIZE;
            isLeftResize = true;
            isTopResize = true;
            isRightResize = false;
            isBottomResize = true;
        } else if (event.getX() > scene.getWidth() - BORDER && event.getY() > scene.getHeight() - BORDER) {
            cursor = Cursor.SE_RESIZE;
            isLeftResize = true;
            isTopResize = true;
            isRightResize = false;
            isBottomResize = false;
        } else if (event.getX() < BORDER || event.getX() > scene.getWidth() - BORDER) {
            cursor = Cursor.E_RESIZE;
            isLeftResize = true;
            isTopResize = false;
            isRightResize = (event.getX() < BORDER);
            isBottomResize = false;
        } else if (event.getY() < BORDER || event.getY() > scene.getHeight() - BORDER) {
            cursor = Cursor.N_RESIZE;
            isLeftResize = false;
            isTopResize = true;
            isRightResize = false;
            isBottomResize = (event.getY() < BORDER);
        } else {
            cursor = Cursor.DEFAULT;
            isLeftResize = false;
            isTopResize = false;
            isRightResize = false;
            isBottomResize = false;
        }

        scene.setCursor(cursor);
    }

    /**
     * Change width of window
     *
     * @param event mouse event
     */
    private void changeWidth(MouseEvent event) {
        double deltaX = stage.getX() - event.getScreenX();
        double width = stage.getWidth();

        if (width <= MIN_WIDTH) {
            if (isRightResize) {
                if (event.getX() < 0) {
                    stage.setWidth(deltaX + width);
                    stage.setX(event.getScreenX());
                }
            } else {
                if (event.getX() + dx - width > 0) {
                    stage.setWidth(event.getX() + dx);
                }
            }
        } else if (width > MIN_WIDTH) {
            if (isRightResize) {
                stage.setWidth(deltaX + width);
                stage.setX(event.getScreenX());
            } else {
                stage.setWidth(event.getX() + dx);
            }
        }
    }

    /**
     * Change height of window
     *
     * @param event mouse event
     */
    private void changeHeight(MouseEvent event) {
        double deltaY = stage.getY() - event.getScreenY();
        double height = stage.getHeight();

        if (height <= MIN_HEIGHT) {
            if (isBottomResize) {
                if (event.getY() < 0) {
                    stage.setHeight(deltaY + height);
                    stage.setY(event.getScreenY());
                }
            } else {
                if (event.getY() + dy - height > 0) {
                    stage.setHeight(event.getY() + dy);
                }
            }
        } else if (height > MIN_HEIGHT) {
            if (isBottomResize) {
                stage.setHeight(deltaY + height);
                stage.setY(event.getScreenY());
            } else {
                stage.setHeight(event.getY() + dy);
            }
        }
    }
}
