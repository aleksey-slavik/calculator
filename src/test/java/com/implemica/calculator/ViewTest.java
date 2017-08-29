package com.implemica.calculator;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
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
import java.util.Random;

import static org.junit.Assert.*;

/**
 * View events test for calculator
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
     * Title label
     */
    private static Label title;

    /**
     * Pane of navigation menu
     */
    private static AnchorPane menuPane;

    /**
     * Hide button
     */
    private static Button hide;
    private static Button expand;
    private static Button menuShow;
    private static Button menuClose;

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
            menuPane = (AnchorPane) scene.lookup("#navigator");
            title = (Label) scene.lookup("#title");
            hide = (Button) scene.lookup("#hide");
            expand = (Button) scene.lookup("#expand");
            menuShow = (Button) scene.lookup("menuShow");
            menuClose = (Button) scene.lookup("#menuClose");
        });

        WaitForAsyncUtils.waitForFxEvents();
    }


    /**
     * Moving window tests
     */
    @Test
    public void moveTest() {
        //handle tests
        moveTest(100, 100);
        moveTest(274, -182);
        moveTest(-530, 0);
        moveTest(0, 387);
        moveTest(123, -298);
        moveTest(-430, 257);
        moveTest(534, 341);

        //random tests
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            moveTest(random.nextInt(MAX_WINDOW_WIDTH) - MAX_WINDOW_WIDTH / 2,
                    random.nextInt(MAX_WINDOW_HEIGHT) - MAX_WINDOW_HEIGHT / 2);
        }
    }

    /**
     * Resize window tests which started from given position
     */
    @Test
    public void resizeTest() {
        //resize from left border of window
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

        //resize from right border of window
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

        //resize from top border of window
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

        //resize from bottom border of window
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

        // resize from right bottom corner of window
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

        //resize from left bottom corner of window
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

        //resize from right top corner of window
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

        //resize from left top corner of window
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
        //check that stage is not maximized
        assertEquals(false, stage.isMaximized());
        //click on expand button and check that stage is maximized
        robot.clickOn(expand);
        assertEquals(true, stage.isMaximized());
        //click on expand button once more  and check that stage is not maximized
        robot.clickOn(expand);
        assertEquals(false, stage.isMaximized());
    }

    /**
     * Hide application test
     */
    @Test
    public void hideTest() {
        //check that stage is not iconified
        assertEquals(false, stage.isIconified());
        //click on hide button and check that stage is iconified
        robot.clickOn(hide);
        assertEquals(true, stage.isIconified());
        //return normal statement and check that stage is not iconified
        Platform.runLater(() -> stage.setIconified(false));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(false, stage.isIconified());
    }

    /**
     * Open and close navigation menu test
     */
    @Test
    public void menuTest() throws Exception {
        //check than navigation menu is outside main pane
        assertEquals(-260, menuPane.getTranslateX(), 0.1);
        //click on open menu button, wait until it showed and check that menu is inside main pane
        robot.clickOn(menuShow);
        Thread.sleep(350);
        assertEquals(0, menuPane.getTranslateX(), 0.1);
        //click on close menu button, wait until it closed and check that menu is outside main pane
        robot.clickOn(menuClose);
        Thread.sleep(350);
        assertEquals(-260, menuPane.getTranslateX(), 0.1);
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

        robot.drag(title, MouseButton.PRIMARY);

        Point point = MouseInfo.getPointerInfo().getLocation();
        double offsetX = Math.abs(stage.getX() - point.getX());
        double offsetY = Math.abs(stage.getY() - point.getY());

        robot.moveBy(dx, dy);
        robot.drop();

        double afterX = stage.getX();
        double afterY = stage.getY();

        assertEquals(checkX(beforeX, dx, offsetX), afterX, 0.1);
        assertEquals(checkY(beforeY, dy, offsetY), afterY, 0.1);
    }

    /**
     * Return correct value of position of window.
     * Correct value if window is outside of the screen
     *
     * @param beforeX X coordinate of window before move
     * @param dx      offset of X coordinate of window move
     * @param offsetX offset of X coordinate of mouse relatively of window
     * @return correct X coordinate
     */
    private double checkX(double beforeX, int dx, double offsetX) {
        double res = beforeX + dx;

        if (res > MAX_WINDOW_WIDTH) {
            res = MAX_WINDOW_WIDTH - offsetX - 1;
        } else if (res < 0) {
            res = Math.abs(offsetX);
        }

        return res;
    }

    /**
     * Return correct value of position of window.
     * Correct value if window is outside of the screen
     *
     * @param beforeY Y coordinate of window before move
     * @param dy      offset of Y coordinate of window move
     * @param offsetY offset of Y coordinate of mouse relatively of window
     * @return correct Y coordinate
     */
    private double checkY(double beforeY, int dy, double offsetY) {
        double res = beforeY + dy;

        if (res > MAX_WINDOW_HEIGHT) {
            res = MAX_WINDOW_HEIGHT - offsetY - 1;
        } else if (res < 0) {
            res = Math.abs(offsetY);
        }

        return res;
    }

    /**
     * Resize window in given window position and check window size.
     * Position NE, SE, SW, NW mean that resize started from corresponding corner of window.
     * Position E, W, N, S mean that resize started from random point in corresponding side of window.
     *
     * @param dx         offset of X coordinate
     * @param dy         offset of Y coordinate
     * @param pos        resize position
     * @param digitsFont font size for digits group
     * @param binaryFont font size for binary group
     * @param unaryFont  font size for unary group
     */
    private void resizeTest(int dx, int dy, String pos, int digitsFont, int binaryFont, int unaryFont) {
        returnNormalState();
        double beforeWidth = stage.getWidth();
        double beforeHeight = stage.getHeight();
        double beforeX = stage.getX();
        double beforeY = stage.getY();
        //initial approach of expected width and height
        double expectedWidth = stage.getWidth();
        double expectedHeight = stage.getHeight();
        //initial approach for drag
        double x = stage.getX();
        double y = stage.getY();

        if (pos.equals("E")) {
            x += beforeWidth - 1;
            y += Math.random() * beforeHeight;
            expectedWidth = beforeWidth + dx;
        } else if (pos.equals("W")) {
            y += Math.random() * beforeHeight;
            expectedWidth = beforeWidth - dx;
        } else if (pos.equals("N")) {
            x += Math.random() * beforeWidth;
            expectedHeight = beforeHeight - dy;
        } else if (pos.equals("S")) {
            x += Math.random() * beforeWidth;
            y += beforeHeight - 1;
            expectedHeight = beforeHeight + dy;
        } else if (pos.equals("NE")) {
            x += beforeWidth - 1;
            expectedWidth = beforeWidth + dx;
            expectedHeight = beforeHeight - dy;
        } else if (pos.equals("SE")) {
            x += beforeWidth - 1;
            y += beforeHeight - 1;
            expectedWidth = beforeWidth + dx;
            expectedHeight = beforeHeight + dy;
        } else if (pos.equals("SW")) {
            y += beforeHeight - 1;
            expectedWidth = beforeWidth - dx;
            expectedHeight = beforeHeight + dy;
        } else if (pos.equals("NW")) {
            expectedWidth = beforeWidth - dx;
            expectedHeight = beforeHeight - dy;
        }

        robot.drag(x, y, MouseButton.PRIMARY);
        robot.moveBy(dx, dy);
        robot.drop();
        expectedWidth = checkWidth(expectedWidth, beforeWidth, beforeX);
        expectedHeight = checkHeight(expectedHeight, beforeHeight, beforeY);

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

    /**
     * Create list of digits buttons group
     *
     * @return digits buttons group
     */
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

    /**
     * Create list of binary buttons group
     *
     * @return binary buttons group
     */
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

    /**
     * Create list of unary buttons group
     *
     * @return unary buttons group
     */
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
     * Return standard statement of window
     */
    private void returnNormalState() {
        WaitForAsyncUtils.waitForFxEvents();
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setWidth(MIN_WINDOW_WIDTH);
        stage.setX(500);
        stage.setY(150);
    }
}