package com.implemica.calculator.view;

import com.implemica.calculator.Launcher;
import com.implemica.calculator.view.adapter.MenuAdapter;
import com.implemica.calculator.view.listener.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Create view of calculator
 *
 * @author Slavik Aleksey V.
 */
public class CalculatorView {

    /**
     * Path to fxml file
     */
    private static final String ROOT_PATH = "/fxml/root.fxml";

    /**
     * Path to style file
     */
    private static final String STYLE_PATH = "/css/root.css";

    /**
     * Path to app icon
     */
    private static final String APP_ICON_PATH = "/images/icon.png";

    /**
     * Path to history icon
     */
    private static final String HISTORY_ICON_PATH = "/images/history.png";

    /**
     * Path to menu icon
     */
    private static final String MENU_ICON_PATH = "/images/menu.png";

    /**
     * Path to about icon
     */
    private static final String ABOUT_ICON_PATH = "/images/about.png";

    /**
     * Calculator title
     */
    private static final String TITLE = "Calculator";

    /**
     * Id of exit button
     */
    private static final String EXIT_SELECTOR = "#exit";

    /**
     * Id of expand button
     */
    private static final String EXPAND_SELECTOR = "#expand";

    /**
     * Id of hide button
     */
    private static final String HIDE_SELECTOR = "#hide";

    /**
     * Id of menu button in main pane
     */
    private static final String MENU_OPEN_SELECTOR = "#menuShow";

    /**
     * Id of menu button in menu pane
     */
    private static final String MENU_CLOSE_SELECTOR = "#menuClose";

    /**
     * Id of history button
     */
    private static final String HISTORY_SELECTOR = "#historyShow";

    /**
     * Id of about button in menu pane
     */
    private static final String ABOUT_SELECTOR = "#about";

    /**
     * Id of title pane
     */
    private static final String TITLE_SELECTOR = "#title";

    /**
     * If of list in menu pane
     */
    private static final String LIST_SELECTOR = "#listView";

    /**
     * Id of numeric display
     */
    private static final String NUMERIC_FIELD_SELECTOR = "#numericField";

    /**
     * Setup view scheme and main view listeners
     *
     * @param primaryStage  window stage
     */
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
        Label title = (Label) scene.lookup(TITLE_SELECTOR);
        MoveWindowListener moveWindowListener = new MoveWindowListener(primaryStage);
        title.setOnMousePressed(moveWindowListener);
        title.setOnMouseDragged(moveWindowListener);

        //add icon for history button
        Button history = (Button) scene.lookup(HISTORY_SELECTOR);
        Image historyImage = new Image(getClass().getResourceAsStream(HISTORY_ICON_PATH));
        ImageView imageView = new ImageView(historyImage);
        history.setGraphic(imageView);

        //add icon for menuOpen button
        Image menuImage = new Image(Launcher.class.getResourceAsStream(MENU_ICON_PATH));
        imageView = new ImageView(menuImage);
        menuOpen.setGraphic(imageView);

        //add icon for menuClose button
        imageView = new ImageView(menuImage);
        menuClose.setGraphic(imageView);

        //add icon for about button
        ImageView about = (ImageView) scene.lookup(ABOUT_SELECTOR);
        Image aboutImage = new Image(Launcher.class.getResourceAsStream(ABOUT_ICON_PATH));
        about.setImage(aboutImage);

        //init menu list of items
        ListView<String> listView = (ListView<String>) scene.lookup(LIST_SELECTOR);
        listView.setItems(MenuAdapter.init());

        //add button font resize
        scene.heightProperty().addListener(new ButtonResizeListener(scene));

        //add numeric field font resize
        Label numericLabel = (Label) scene.lookup(NUMERIC_FIELD_SELECTOR);
        numericLabel.textProperty().addListener(new NumericResizeListener(scene));

        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(APP_ICON_PATH)));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
