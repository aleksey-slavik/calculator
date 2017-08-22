package com.implemica.calculator;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * View events test
 *
 * @author Slavik Aleksey V.
 */
public class ViewTest {

    /**
     * Minimal window width
     */
    private static final int MIN_WINDOW_WIDTH = 320;

    /**
     * Minimal window height
     */
    private static final int MIN_WINDOW_HEIGHT = 500;

    /**
     * Maximal window width
     */
    private static final int MAX_WINDOW_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    /**
     * Maximal window height
     */
    private static final int MAX_WINDOW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    /**
     * Robot for test events
     */
    private FxRobot robot = new FxRobot();

    /**
     * Window stage
     */
    private static Stage stage;

    /**
     * Numeric field label
     */
    private static Label numericField;
    private static AnchorPane menuPane;

    @BeforeClass
    public static void initJFX() throws InterruptedException {
        new JFXPanel();
        Platform.runLater(() -> {
            stage = new Stage();
            try {
                new Launcher().start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Scene scene = stage.getScene();
            numericField = (Label) scene.lookup("#numericField");
            menuPane = (AnchorPane) scene.lookup("#navigator");
        });
        WaitForAsyncUtils.waitForFxEvents();
    }


    /**
     * Moving window tests
     */
    @Test
    public void moveTest() {
        moveTest(100, 100);
        moveTest(-200, -200);
        moveTest(500, 0);
        moveTest(0, 300);
        moveTest(120, 300);
        moveTest(-1500, 0);
        moveTest(534, -1000);
    }

    /**
     * Resize window tests which started from given position
     */
    @Test
    public void resizeTest() {
        resizeTest(0, 0, "E", 23, 22, 16);
        resizeTest(100, 0, "E", 23, 22, 16);
        resizeTest(0, 50, "E", 23, 22, 16);
        resizeTest(-123, 0, "E", 23, 22, 16);
        resizeTest(0, -65, "E", 23, 22, 16);
        resizeTest(943, 827, "E", 23, 22, 16);
        resizeTest(0, 1062, "E", 23, 22, 16);
        resizeTest(1000, 0, "E", 23, 22, 16);
        resizeTest(-1020, -3000, "E", 23, 22, 16);
        resizeTest(0, -2345, "E", 23, 22, 16);
        resizeTest(-9876, 0, "E", 23, 22, 16);
        resizeTest(-1765, 1399, "E", 23, 22, 16);
        resizeTest(999, -986, "E", 23, 22, 16);

        resizeTest(0, 0, "W", 23, 22, 16);
        resizeTest(-50, 0, "W", 23, 22, 16);
        resizeTest(0, 89, "W", 23, 22, 16);
        resizeTest(0, -152, "W", 23, 22, 16);
        resizeTest(58, 0, "W", 23, 22, 16);
        resizeTest(1043, 1027, "W", 23, 22, 16);
        resizeTest(0, 962, "W", 23, 22, 16);
        resizeTest(980, 0, "W", 23, 22, 16);
        resizeTest(-1024, -1321, "W", 23, 22, 16);
        resizeTest(0, -5432, "W", 23, 22, 16);
        resizeTest(-8888, 0, "W", 23, 22, 16);
        resizeTest(-1111, 1399, "W", 23, 22, 16);
        resizeTest(777, -666, "W", 23, 22, 16);

        resizeTest(0, 0, "N", 23, 22, 16);
        resizeTest(0, 52, "N", 23, 22, 16);
        resizeTest(65, 0, "N", 23, 22, 16);
        resizeTest(-10, 48, "N", 23, 22, 16);
        resizeTest(-1740, 0, "N", 23, 22, 16);
        resizeTest(-2111, 3399, "N", 23, 22, 16);
        resizeTest(1777, -1666, "N", 28, 26, 20);
        resizeTest(1243, 1327, "N", 23, 22, 16);
        resizeTest(0, 1962, "N", 23, 22, 16);
        resizeTest(2980, 0, "N", 23, 22, 16);
        resizeTest(-1924, -2321, "N", 28, 26, 20);
        resizeTest(0, -2432, "N", 28, 26, 20);

        resizeTest(0, 0, "S", 23, 22, 16);
        resizeTest(0, 101, "S", 28, 26, 20);
        resizeTest(10, 201, "S", 33, 30, 24);
        resizeTest(124, 200, "S", 28, 26, 20);
        resizeTest(0, 1962, "S", 33, 30, 24);
        resizeTest(2980, 0, "S", 23, 22, 16);
        resizeTest(-1924, -2321, "S", 23, 22, 16);
        resizeTest(0, -2432, "S", 23, 22, 16);
        resizeTest(-1320, 0, "S", 23, 22, 16);
        resizeTest(-2121, 1399, "S", 33, 30, 24);
        resizeTest(1777, -1446, "S", 23, 22, 16);
        resizeTest(1443, 1321, "S", 33, 30, 24);

        resizeTest(0, 0, "SE", 23, 22, 16);
        resizeTest(50, 50, "SE", 23, 22, 16);
        resizeTest(145, 199, "SE", 28, 26, 20);
        resizeTest(-250, 22, "SE", 23, 22, 16);
        resizeTest(1280, 0, "SE", 23, 22, 16);
        resizeTest(-3924, -1321, "SE", 23, 22, 16);
        resizeTest(1123, -1555, "SE", 23, 22, 16);
        resizeTest(1763, 1848, "SE", 33, 30, 24);
        resizeTest(0, -2974, "SE", 23, 22, 16);
        resizeTest(-1769, 0, "SE", 23, 22, 16);
        resizeTest(-1121, 1654, "SE", 33, 30, 24);

        resizeTest(0, 0, "SW", 23, 22, 16);
        resizeTest(-50, 50, "SW", 23, 22, 16);
        resizeTest(99, -87, "SW", 23, 22, 16);
        resizeTest(155, 120, "SW", 28, 26, 20);
        resizeTest(1763, 0, "SW", 23, 22, 16);
        resizeTest(-1993, 0, "SW", 23, 22, 16);
        resizeTest(-1762, 1621, "SW", 33, 30, 24);
        resizeTest(-2723, -1893, "SW", 23, 22, 16);
        resizeTest(1636, -1828, "SW", 23, 22, 16);
        resizeTest(2762, 1992, "SW", 33, 30, 24);
        resizeTest(0, -1904, "SW", 23, 22, 16);

        resizeTest(0, 0, "NE", 23, 22, 16);
        resizeTest(140, -122, "NE", 28, 26, 20);
        resizeTest(200, 199, "NE", 23, 22, 16);
        resizeTest(-98, 176, "NE", 23, 22, 16);
        resizeTest(2733, 0, "NE", 23, 22, 16);
        resizeTest(2873, 1873, "NE", 23, 22, 16);
        resizeTest(0, -8754, "NE", 28, 26, 20);
        resizeTest(-8363, 0, "NE", 23, 22, 16);
        resizeTest(-5261, 6271, "NE", 23, 22, 16);
        resizeTest(-6172, -3182, "NE", 28, 26, 20);
        resizeTest(7641, -1932, "NE", 28, 26, 20);

        resizeTest(0, 0, "NW", 23, 22, 16);
        resizeTest(177, 78, "NW", 23, 22, 16);
        resizeTest(155, -61, "NW", 23, 22, 16);
        resizeTest(127, 182, "NW", 23, 22, 16);
        resizeTest(2733, 0, "NW", 23, 22, 16);
        resizeTest(0, -8754, "NW", 28, 26, 20);
        resizeTest(-6172, -3182, "NW", 28, 26, 20);
        resizeTest(7641, -1932, "NW", 28, 26, 20);
        resizeTest(2873, 1873, "NW", 23, 22, 16);
        resizeTest(-8363, 0, "NW", 23, 22, 16);
        resizeTest(-5261, 6271, "NW", 23, 22, 16);
    }

    /**
     * Expand application test
     */
    @Test
    public void expandTest() {
        Button expand = GuiTest.find("#expand");
        assertEquals(false, stage.isMaximized());

        robot.clickOn(expand);
        assertEquals(true, stage.isMaximized());

        robot.clickOn(expand);
        assertEquals(false, stage.isMaximized());
    }

    /**
     * Hide application test
     */
    @Test
    public void hideTest() {
        Button hide = GuiTest.find("#hide");
        assertEquals(false, stage.isIconified());
        robot.clickOn(hide);
        assertEquals(true, stage.isIconified());
        Platform.runLater(() -> stage.setIconified(false));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(false, stage.isIconified());
    }

    /**
     * Open and close navigation menu test
     */
    @Test
    public void menuTest() throws Exception {
        Button menuShow = GuiTest.find("#menuShow");
        robot.clickOn(menuShow);
        Thread.sleep(350);
        assertEquals(0, menuPane.getTranslateX(), 0.1);

        Button menuClose = GuiTest.find("#menuClose");
        robot.clickOn(menuClose);
        Thread.sleep(350);
        assertEquals(-260, menuPane.getTranslateX(), 0.1);
    }

    /**
     * Check font size of numeric field depending on the count of chars in field
     */
    @Test
    public void numericFontResizeTest() {
        numericFontResizeTest("12345", 30);
        numericFontResizeTest("-98", 30);
    }

    /**
     * Move window and check position of window
     *
     * @param dx offset of X coordinate
     * @param dy offset of Y coordinate
     */
    private void moveTest(int dx, int dy) {
        double beforeX = stage.getX();
        double beforeY = stage.getY();
        double offsetX = Math.random() * 180 + 2;
        double offsetY = Math.random() * 32 + 2;

        robot.drag(new Point2D(beforeX + offsetX, beforeY + offsetY), MouseButton.PRIMARY);
        robot.moveBy(dx, dy);
        WaitForAsyncUtils.waitForFxEvents();

        double afterX = stage.getX();
        double afterY = stage.getY();

        double expectedX = beforeX + dx - offsetX;
        double expectedY = beforeY + dy - offsetX;
        if (expectedX < 0) {
            expectedX = dx - beforeX;
        } else if (expectedX > MAX_WINDOW_WIDTH) {
        }
        if (expectedY < 0) {
            expectedY = dy - beforeY;
        }
        assertEquals(expectedX, afterX, 0.1);
        assertEquals(expectedY, afterY, 0.1);
    }

    /**
     * Resize window in given window position and check window size.
     * Position NE, SE, SW, NW mean that resize started from corresponding corner of window.
     * Position E, W, N, S mean that resize started from random point in corresponding side of window.
     *
     * @param dx  offset of X coordinate
     * @param dy  offset of Y coordinate
     * @param pos resize position
     */
    private void resizeTest(int dx, int dy, String pos, int digitsFont, int binaryFont, int unaryFont) {
        returnNormalState();
        double beforeWidth = stage.getWidth();
        double beforeHeight = stage.getHeight();
        double beforeX = stage.getX();
        double beforeY = stage.getY();
        double expectedWidth = stage.getWidth();
        double expectedHeight = stage.getHeight();

        switch (pos) {
            case "E":
                robot.drag(stage.getX() + beforeWidth - 1, stage.getY() + Math.random() * beforeHeight, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + dx, beforeWidth, beforeX);
                break;
            case "W":
                robot.drag(stage.getX() + 1, stage.getY() + Math.random() * beforeHeight, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                WaitForAsyncUtils.waitForFxEvents();
                expectedWidth = checkWidth(beforeWidth - dx - 1, beforeWidth, beforeX);
                break;
            case "N":
                robot.drag(stage.getX() + Math.random() * beforeWidth, stage.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                expectedHeight = checkHeight(beforeHeight - dy - 1, beforeHeight, beforeY);
                break;
            case "S":
                robot.drag(stage.getX() + Math.random() * beforeWidth, stage.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                expectedHeight = checkHeight(beforeHeight + dy, beforeHeight, beforeY);
                break;
            case "NE":
                robot.drag(stage.getX() + beforeWidth - 1, stage.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + dx, beforeWidth, beforeX);
                expectedHeight = checkHeight(beforeHeight - dy - 1, beforeHeight, beforeY);
                break;
            case "SE":
                robot.drag(stage.getX() + beforeWidth - 1, stage.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + dx, beforeWidth, beforeX);
                expectedHeight = checkHeight(beforeHeight + dy, beforeHeight, beforeY);
                break;
            case "SW":
                robot.drag(stage.getX() + 1, stage.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - dx - 1, beforeWidth, beforeX);
                expectedHeight = checkHeight(beforeHeight + dy, beforeHeight, beforeY);
                break;
            case "NW":
                robot.drag(stage.getX() + 1, stage.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(dx, dy);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - dx - 1, beforeWidth, beforeX);
                expectedHeight = checkHeight(beforeHeight - dy - 1, beforeHeight, beforeY);
                break;
        }

        assertEquals(expectedWidth, stage.getWidth(), 0.1);
        assertEquals(expectedHeight, stage.getHeight(), 0.1);
        fontResizeTest(digitsFont, binaryFont, unaryFont);
    }

    /**
     * Return correct value of width.
     * Correct value is between MIN_WINDOW_WIDTH and MAX_WINDOW_WIDTH
     *
     * @param width given width
     * @return correct width
     */
    private double checkWidth(double width, double beforeWidth, double beforeX) {
        if (width < MIN_WINDOW_WIDTH) {
            width = MIN_WINDOW_WIDTH;
        } else if (stage.getX() == 0 && beforeWidth + beforeX < width) {
            width = beforeWidth + beforeX;
        } else if (stage.getX() + width > MAX_WINDOW_WIDTH) {
            width = MAX_WINDOW_WIDTH - stage.getX();
        }

        return width;
    }

    /**
     * Return correct value of height.
     * Correct value is between MIN_WINDOW_HEIGHT and MAX_WINDOW_HEIGHT
     *
     * @param height given height
     * @return correct height
     */
    private double checkHeight(double height, double beforeHeight, double beforeY) {
        if (height < MIN_WINDOW_HEIGHT) {
            height = MIN_WINDOW_HEIGHT;
        } else if (stage.getY() == 0 && beforeHeight + beforeY < height) {
            height = beforeHeight + beforeY;
        } else if (stage.getY() + height > MAX_WINDOW_HEIGHT) {
            height = MAX_WINDOW_HEIGHT - stage.getY();
        }

        return height;
    }

    /**
     * Check font size of groups of buttons during resize window
     *
     * @param digitsFont font size of digit group
     * @param binaryFont font size of binary group
     * @param unaryFont  font size of unary group
     */
    private void fontResizeTest(int digitsFont, int binaryFont, int unaryFont) {
        WaitForAsyncUtils.waitForFxEvents();
        for (Button button : digits()) {
            assertEquals(digitsFont, button.getFont().getSize(), 0.1);
        }
        for (Button button : binaries()) {
            assertEquals(binaryFont, button.getFont().getSize(), 0.1);
        }
        for (Button button : unaries()) {
            assertEquals(unaryFont, button.getFont().getSize(), 0.1);
        }
    }

    private ArrayList<Button> digits() {
        ArrayList<Button> list = new ArrayList<>();
        list.add(GuiTest.find("#one"));
        list.add(GuiTest.find("#two"));
        list.add(GuiTest.find("#three"));
        list.add(GuiTest.find("#four"));
        list.add(GuiTest.find("#five"));
        list.add(GuiTest.find("#six"));
        list.add(GuiTest.find("#seven"));
        list.add(GuiTest.find("#eight"));
        list.add(GuiTest.find("#nine"));
        list.add(GuiTest.find("#zero"));
        list.add(GuiTest.find("#comma"));
        return list;
    }

    private ArrayList<Button> binaries() {
        ArrayList<Button> list = new ArrayList<>();
        list.add(GuiTest.find("#equals"));
        list.add(GuiTest.find("#add"));
        list.add(GuiTest.find("#subtract"));
        list.add(GuiTest.find("#divide"));
        list.add(GuiTest.find("#multiply"));
        list.add(GuiTest.find("#backspace"));
        return list;
    }

    private ArrayList<Button> unaries() {
        ArrayList<Button> list = new ArrayList<>();
        list.add(GuiTest.find("#negate"));
        list.add(GuiTest.find("#percent"));
        list.add(GuiTest.find("#sqr"));
        list.add(GuiTest.find("#sqrt"));
        list.add(GuiTest.find("#inverse"));
        list.add(GuiTest.find("#clear"));
        list.add(GuiTest.find("#clear_expr"));
        return list;
    }

    /**
     * Check font size of numeric field depending on the count of chars in field.
     * Append string value to numeric field and check font size.
     *
     * @param value given numeric value
     * @param font  font size
     */
    private void numericFontResizeTest(String value, int font) {
        numericField.setText(value);
        assertEquals(font, numericField.getFont().getSize(), 0.1);
    }

    private void returnNormalState() {
        WaitForAsyncUtils.waitForFxEvents();
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setWidth(MIN_WINDOW_WIDTH);
        stage.setX(500);
        stage.setY(150);
    }
}