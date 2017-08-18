package com.implemica.calculator;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @AfterClass
    public static void closeJFX() throws InterruptedException {
        Platform.exit();
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
    }

    /**
     * Resize window tests which started from given position
     */
    @Test
    public void resizeTest() {
        resizeTest(50, 0, "E");
        resizeTest(-50, 0, "W");
        resizeTest(0, -50, "N");
        resizeTest(0, 50, "S");
        resizeTest(50, 50, "SE");
        resizeTest(-50, 50, "SW");
    }

    /**
     * Close application test
     */
    @Test
    public void exitTest() {
        //todo
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
    @Ignore
    @Test
    public void hideTest() {
        Button hide = GuiTest.find("#hide");
        assertEquals(false, stage.isIconified());
        robot.clickOn(hide);
        assertEquals(true, stage.isIconified());
        //stage.setIconified(false);
    }

    /**
     * Open and close navigation menu test
     */
    @Test
    public void menuTest() throws Exception {
        Button menuShow = GuiTest.find("#menuShow");

        robot.clickOn(menuShow);
        Thread.sleep(350);
        AnchorPane menu = GuiTest.find("#navigator");
        assertEquals(0, menu.getTranslateX(), 0.1);

        Button menuClose = GuiTest.find("#menuClose");
        robot.clickOn(menuClose);
        Thread.sleep(350);
        assertEquals(-260, menu.getTranslateX(), 0.1);
    }

    /**
     * Check font size of buttons during resize window
     */
    @Test
    public void fontResizeTest() {
        fontResizeTest(0, 16, 16, 14, 12);
        fontResizeTest(10, 16, 16, 14, 12);
        fontResizeTest(110, 20, 20, 17, 20);
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
     * @param x offset of X coordinate
     * @param y offset of Y coordinate
     */
    private void moveTest(int x, int y) {
        AnchorPane title = GuiTest.find("#title");
        double beforeX = stage.getX();
        double beforeY = stage.getY();
        robot.drag(title, MouseButton.PRIMARY);
        robot.moveBy(x, y);
        double afterX = stage.getX();
        double afterY = stage.getY();
        assertEquals(beforeX + x, afterX, 0.1);
        assertEquals(beforeY + y, afterY, 0.1);
    }

    /**
     * Resize window in given window position and check window size.
     * Position NE, SE, SW, NW mean that resize started from corresponding corner of window.
     * Position E, W, N, S mean that resize started from random point in corresponding side of window.
     *
     * @param x   offset of X coordinate
     * @param y   offset of Y coordinate
     * @param pos resize position
     */
    private void resizeTest(int x, int y, String pos) {
        double beforeWidth = stage.getWidth();
        double beforeHeight = stage.getHeight();
        double expectedWidth = stage.getWidth();
        double expectedHeight = stage.getHeight();

        switch (pos) {
            case "E":
                robot.drag(stage.getX() + beforeWidth - 1, stage.getY() + Math.random() * beforeHeight, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + x);
                break;
            case "W":
                robot.drag(stage.getX() + 1, stage.getY() + Math.random() * beforeHeight, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - x - 1);
                break;
            case "N":
                robot.drag(stage.getX() + Math.random() * beforeWidth, stage.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedHeight = checkHeight(beforeHeight - y - 1);
                break;
            case "S":
                robot.drag(stage.getX() + Math.random() * beforeWidth, stage.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedHeight = checkHeight(beforeHeight + y);
                break;
            case "NE":
                robot.drag(stage.getX() + beforeWidth - 1, stage.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + x);
                expectedHeight = checkHeight(beforeHeight - y - 1);
                break;
            case "SE":
                robot.drag(stage.getX() + beforeWidth - 1, stage.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + x);
                expectedHeight = checkHeight(beforeHeight + y);
                break;
            case "SW":
                robot.drag(stage.getX() + 1, stage.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - x - 1);
                expectedHeight = checkHeight(beforeHeight + y);
                break;
            case "NW":
                robot.drag(stage.getX() + 1, stage.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - x - 1);
                expectedHeight = checkHeight(beforeHeight - y - 1);
                break;
        }

        assertEquals(expectedWidth, stage.getWidth(), 0.1);
        assertEquals(expectedHeight, stage.getHeight(), 0.1);
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setWidth(MIN_WINDOW_WIDTH);
    }

    /**
     * Return correct value of width.
     * Correct value is between MIN_WINDOW_WIDTH and MAX_WINDOW_WIDTH
     *
     * @param width given width
     * @return correct width
     */
    private double checkWidth(double width) {
        if (width < MIN_WINDOW_WIDTH) {
            width = MIN_WINDOW_WIDTH;
        } else if (width > MAX_WINDOW_WIDTH) {
            width = MAX_WINDOW_WIDTH;
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
    private double checkHeight(double height) {
        if (height < MIN_WINDOW_HEIGHT) {
            height = MIN_WINDOW_HEIGHT;
        } else if (height > MAX_WINDOW_HEIGHT) {
            height = MAX_WINDOW_HEIGHT;
        }

        return height;
    }

    /**
     * Check font size of groups of buttons during resize window
     *
     * @param dy         offset of Y coordinate
     * @param digitsFont font size of digit group
     * @param binaryFont font size of binary group
     * @param unaryFont  font size of unary group
     * @param clearFont  font size of clear group
     */
    private void fontResizeTest(int dy, int digitsFont, int binaryFont, int unaryFont, int clearFont) {
        resizeTest(0, dy, "S");
        for (Button button : digits()) {
            assertEquals(digitsFont, button.getFont().getSize(), 0.1);
        }
        for (Button button : binaries()) {
            assertEquals(binaryFont, button.getFont().getSize(), 0.1);
        }
        for (Button button : unaries()) {
            assertEquals(unaryFont, button.getFont().getSize(), 0.1);
        }
        for (Button button : clear()) {
            assertEquals(clearFont, button.getFont().getSize(), 0.1);
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
        return list;
    }

    private ArrayList<Button> clear() {
        ArrayList<Button> list = new ArrayList<>();
        list.add(GuiTest.find("#clear"));
        list.add(GuiTest.find("#clear_expr"));
        return list;
    }

    private ArrayList<Button> other() {
        ArrayList<Button> list = new ArrayList<>();
        list.add(GuiTest.find("#exit"));
        list.add(GuiTest.find("#expaand"));
        list.add(GuiTest.find("#hide"));
        list.add(GuiTest.find("#memory_clear"));
        list.add(GuiTest.find("#memory_recall"));
        list.add(GuiTest.find("#memory_add"));
        list.add(GuiTest.find("#memory_minus"));
        list.add(GuiTest.find("#memory_store"));
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
}