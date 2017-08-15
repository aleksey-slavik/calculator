package com.implemica.calculator;

import com.implemica.calculator.view.CalculatorView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launcher
 *
 * @author Slavik Aleksey V.
 */
public class Launcher extends Application {

    private CalculatorView view = new CalculatorView();

    @Override
    public void start(Stage primaryStage) throws Exception {
        view.initStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
