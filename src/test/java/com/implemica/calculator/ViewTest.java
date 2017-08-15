package com.implemica.calculator;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;
import org.testfx.api.FxRobot;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * View events test
 *
 * @author Slavik Aleksey V.
 */
public class ViewTest {

    private static final int MIN_WINDOW_WIDTH = 320;
    private static final int MIN_WINDOW_HEIGHT = 500;
    private static final int MAX_WINDOW_WIDTH = 1600;
    private static final int MAX_WINDOW_HEIGHT = 900;

    private FxRobot robot = new FxRobot();
    private AnchorPane root;
    private static GuiTest controller;

    @BeforeClass
    public static void init() throws InterruptedException {
        FXTestUtils.launchApp(Launcher.class);
        controller = new GuiTest() {
            @Override
            protected Parent getRootNode() {
                return stage.getScene().getRoot();
            }
        };

        Thread.sleep(3000);
    }

    @Before
    public void setUp() throws InterruptedException {
        root = GuiTest.find("#root");
    }

    @Test
    public void moveTest() {
        moveTest(100, 100);
        moveTest(-200, -200);
        moveTest(500, 0);
        moveTest(0, 300);
    }

    @Test
    public void resizeTest() {
        resizeTest(50, 0, "E");
        resizeTest(-50, 0, "W");
        //resizeTest(0, -50, "N");
        resizeTest(0, 50, "S");
        resizeTest(50, 50, "SE");
        resizeTest(-50, 50, "SW");
    }

    @Test
    public void exitTest() {
    }

    @Test
    public void expandTest() {
        Button expand = GuiTest.find("#expand");
        double beforeWidth = expand.getScene().getWidth();
        double beforeHeight = expand.getScene().getHeight();

        robot.clickOn(expand);
        assertEquals(MAX_WINDOW_WIDTH, root.getWidth(), 0.1);
        assertEquals(MAX_WINDOW_HEIGHT, root.getHeight(), 0.1);

        robot.clickOn(expand);
        assertEquals(beforeWidth, root.getWidth(), 0.1);
        assertEquals(beforeHeight, root.getHeight(), 0.1);
    }

    @Test
    public void hideTest() throws Throwable {
        Button hide = GuiTest.find("#hide");
        robot.clickOn(hide);
        assertEquals(true, root.isManaged());
    }

    @Test
    public void menuTest() {
        Button menuShow = GuiTest.find("#menuShow");
        AnchorPane menu = GuiTest.find("#navigator");

        robot.clickOn(menuShow);
        controller.sleep(350);
        assertEquals(0, menu.getTranslateX(), 0.1);

        Button menuClose = GuiTest.find("#menuClose");
        robot.clickOn(menuClose);
        controller.sleep(350);
        assertEquals(-250, menu.getTranslateX(), 0.1);
    }

    @Test
    public void fontResizeTest() {
        fontResizeTest(0,16);
        fontResizeTest(10,16);
        fontResizeTest(100,20);
    }

    @Test
    public void numericFontResizeTest() {
    }

    @Test
    public void disableButtonsTest() {
    }

    @Test
    public void disableMemoryButtonsTest() {
    }

    @Test
    public void historyOverflowTest() {
    }

    private void moveTest(int x, int y) {
        AnchorPane title = GuiTest.find("#title");
        Window window = title.getScene().getWindow();
        double beforeX = window.getX();
        double beforeY = window.getY();
        robot.drag(title, MouseButton.PRIMARY);
        robot.moveBy(x, y);
        double afterX = window.getX();
        double afterY = window.getY();
        assertEquals(beforeX + x, afterX, 0.1);
        assertEquals(beforeY + y, afterY, 0.1);
    }

    private void resizeTest(int x, int y, String direction) {
        //AnchorPane root = GuiTest.find("#root");
        Window window = root.getScene().getWindow();
        double beforeWidth = window.getWidth();
        double beforeHeight = window.getHeight();
        double expectedWidth = window.getWidth();
        double expectedHeight = window.getHeight();

        switch (direction) {
            case "E":
                robot.drag(window.getX() + beforeWidth - 1, window.getY() + Math.random() * beforeHeight, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + x);
                break;
            case "W":
                robot.drag(window.getX() + 1, window.getY() + Math.random() * beforeHeight, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - x - 1);
                break;
            case "N":
                robot.drag(window.getX() + Math.random() * beforeWidth, window.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedHeight = checkHeight(beforeHeight - y - 1);
                break;
            case "S":
                robot.drag(window.getX() + Math.random() * beforeWidth, window.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedHeight = checkHeight(beforeHeight + y);
                break;
            case "NE":
                robot.drag(window.getX() + beforeWidth - 1, window.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + x);
                expectedHeight = checkHeight(beforeHeight - y - 1);
                break;
            case "SE":
                robot.drag(window.getX() + beforeWidth - 1, window.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth + x);
                expectedHeight = checkHeight(beforeHeight + y);
                break;
            case "SW":
                robot.drag(window.getX() + 1, window.getY() + beforeHeight - 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - x - 1);
                expectedHeight = checkHeight(beforeHeight + y);
                break;
            case "NW":
                robot.drag(window.getX() + 1, window.getY() + 1, MouseButton.PRIMARY);
                robot.moveBy(x, y);
                robot.drop();
                expectedWidth = checkWidth(beforeWidth - x - 1);
                expectedHeight = checkHeight(beforeHeight - y - 1);
                break;
        }

        assertEquals(expectedWidth, window.getWidth(), 0.1);
        assertEquals(expectedHeight, window.getHeight(), 0.1);
    }

    private double checkWidth(double width) {
        if (width < MIN_WINDOW_WIDTH) {
            width = MIN_WINDOW_WIDTH;
        } else if (width > MAX_WINDOW_WIDTH) {
            width = MAX_WINDOW_WIDTH;
        }

        return width;
    }

    private double checkHeight(double height) {
        if (height < MIN_WINDOW_HEIGHT) {
            height = MIN_WINDOW_HEIGHT;
        } else if (height > MAX_WINDOW_HEIGHT) {
            height = MAX_WINDOW_HEIGHT;
        }

        return height;
    }

    private void fontResizeTest(int dy, int font) {
        resizeTest(0, dy, "S");
        for (Button button : digits()) {
            System.out.println(button.getFont().getSize());
            assertEquals(font, button.getFont().getSize(), 0.1);
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
}
