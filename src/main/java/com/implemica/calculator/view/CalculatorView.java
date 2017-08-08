package com.implemica.calculator.view;

import com.implemica.calculator.view.listener.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * View
 *
 * @author Slavik Aleksey V.
 */
public class CalculatorView {

    private static final String ROOT_PATH = "/fxml/root.fxml";

    private static final String STYLE_PATH = "/css/root.css";

    private static final String APP_ICON_PATH = "/images/icon.png";

    private static final String HISTORY_ICON_PATH = "/images/history.png";

    private static final String MENU_ICON_PATH = "/images/menu.png";

    private static final String ABOUT_ICON_PATH = "/images/about.png";

    private static final String TITLE = "Calculator";

    private static final String EXIT_SELECTOR = "#exit";

    private static final String EXPAND_SELECTOR = "#expand";

    private static final String HIDE_SELECTOR = "#hide";

    private static final String MENU_OPEN_SELECTOR = "#menuShow";

    private static final String MENU_CLOSE_SELECTOR = "#menuClose";

    private static final String HISTORY_SELECTOR = "#history";

    private static final String ABOUT_SELECTOR = "#about";

    public void initStage(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(ROOT_PATH));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(STYLE_PATH).toExternalForm());

        //add resize listener to main scene
        ResizeListener resizeListener = new ResizeListener(primaryStage, scene);
        scene.setOnMouseMoved(resizeListener);
        scene.setOnMousePressed(resizeListener);
        scene.setOnMouseDragged(resizeListener);

        //add close listener
        Button exit = (Button) scene.lookup(EXIT_SELECTOR);
        exit.setOnAction(new ExitListener(primaryStage));

        //add expand listener
        Button expand = (Button) scene.lookup(EXPAND_SELECTOR);
        expand.setOnAction(new ExpandListener(primaryStage));

        //add hide listener
        Button hide = (Button) scene.lookup(HIDE_SELECTOR);
        hide.setOnAction(new HideListener(primaryStage));

        //add menu listener
        Button menuOpen = (Button) scene.lookup(MENU_OPEN_SELECTOR);
        Button menuClose = (Button) scene.lookup(MENU_CLOSE_SELECTOR);
        NavigatorListener navigatorListener = new NavigatorListener(scene);
        menuOpen.setOnAction(navigatorListener);
        menuClose.setOnAction(navigatorListener);

        //add move listener

        //add icon for history button

        //add icon for menu buttons

        //add icon for about button

        //init listview

        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(APP_ICON_PATH)));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
