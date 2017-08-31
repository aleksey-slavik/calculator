package com.implemica.calculator.view.listener;

import javafx.event.EventHandler;
import javafx.event.EventType;
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
     * True, if besides resize window almost must be moved in horizontal projection.
     * Aries when resize have left direction
     */
    private boolean isMoveH;

    /**
     * True, if besides resize window almost must be moved in vertical projection.
     * Aries when resize have top direction
     */
    private boolean isMoveV;

    /**
     * True, if window must be resize in horizontal projection
     */
    private boolean isResizeH;

    /**
     * True, if window must be resize in vertical projection
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
        EventType type = event.getEventType();

        if (type.equals(MouseEvent.MOUSE_MOVED)) {
            changeCursor(event);
        } else if (type.equals(MouseEvent.MOUSE_PRESSED)) {
            dx = stage.getWidth() - event.getX();
            dy = stage.getHeight() - event.getY();
        } else if (type.equals(MouseEvent.MOUSE_DRAGGED)) {
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
     * @param event mouse event
     */
    private void changeCursor(MouseEvent event) {
        Cursor cursor;
        double eventX = event.getX();
        double eventY = event.getY();
        double width = scene.getWidth();
        double height = scene.getHeight();

        if (eventX < BORDER && eventY < BORDER) {
            cursor = Cursor.NW_RESIZE;
            isResizeH = true;
            isResizeV = true;
            isMoveH = true;
            isMoveV = true;
        } else if (eventX < BORDER && eventY > height - BORDER) {
            cursor = Cursor.SW_RESIZE;
            isResizeH = true;
            isResizeV = true;
            isMoveH = true;
            isMoveV = false;
        } else if (eventX > width - BORDER && eventY < BORDER) {
            cursor = Cursor.NE_RESIZE;
            isResizeH = true;
            isResizeV = true;
            isMoveH = false;
            isMoveV = true;
        } else if (eventX > width - BORDER && eventY > height - BORDER) {
            cursor = Cursor.SE_RESIZE;
            isResizeH = true;
            isResizeV = true;
            isMoveH = false;
            isMoveV = false;
        } else if (eventX < BORDER || eventX > width - BORDER) {
            cursor = Cursor.E_RESIZE;
            isResizeH = true;
            isResizeV = false;
            isMoveH = (eventX < BORDER);
            isMoveV = false;
        } else if (eventY < BORDER || eventY > height - BORDER) {
            cursor = Cursor.N_RESIZE;
            isResizeH = false;
            isResizeV = true;
            isMoveH = false;
            isMoveV = (eventY < BORDER);
        } else {
            cursor = Cursor.DEFAULT;
            isResizeH = false;
            isResizeV = false;
            isMoveH = false;
            isMoveV = false;
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
        double eventX = event.getX();
        double newWidth = stage.getWidth();
        double newX = stage.getX();

        if (width <= MIN_WIDTH) {
            if (isMoveH) {
                if (eventX < 0) {
                    newWidth = deltaX + width;
                    newX = event.getScreenX();
                }
            } else if (eventX + dx - width > 0) {
                newWidth = eventX + dx;
            }
        } else if (width > MIN_WIDTH) {
            if (isMoveH) {
                newWidth = deltaX + width;
                newX = event.getScreenX();
            } else {
                newWidth = eventX + dx;
            }
        }

        stage.setWidth(newWidth);
        stage.setX(newX);
    }

    /**
     * Change height of window
     *
     * @param event mouse event
     */
    private void changeHeight(MouseEvent event) {
        double deltaY = stage.getY() - event.getScreenY();
        double height = stage.getHeight();
        double eventY = event.getY();
        double newHeight = stage.getHeight();
        double newY = stage.getY();

        if (height <= MIN_HEIGHT) {
            if (isMoveV) {
                if (eventY < 0) {
                    newHeight = deltaY + height;
                    newY = event.getScreenY();
                }
            } else if (eventY + dy - height > 0) {
                newHeight = eventY + dy;
            }
        } else if (height > MIN_HEIGHT) {
            if (isMoveV) {
                newHeight = deltaY + height;
                newY = event.getScreenY();
            } else {
                newHeight = eventY + dy;
            }
        }

        stage.setHeight(newHeight);
        stage.setY(newY);
    }
}
