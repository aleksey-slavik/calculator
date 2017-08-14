package com.implemica.calculator;

import javafx.scene.Parent;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import static org.junit.Assert.*;

/**
 * View events test
 *
 * @author Slavik Aleksey V.
 */
public class ViewTest {

    private static GuiTest controller;

    @BeforeClass
    public static void init() {
        FXTestUtils.launchApp(Launcher.class);
        controller = new GuiTest() {
            @Override
            protected Parent getRootNode() {
                return stage.getScene().getRoot();
            }
        };
    }

    @Test
    public void moveTest() {
        moveTest(100,100);
        moveTest(200,200);
        moveTest(500,500);
    }

    private void moveTest(int x, int y) {
    }
}
