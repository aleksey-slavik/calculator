package com.implemica.calculator.controller;

import com.implemica.calculator.Launcher;
import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.util.enums.Operator;
import com.implemica.calculator.util.exception.OverflowException;
import com.implemica.calculator.util.exception.SquareRootException;
import com.implemica.calculator.util.exception.ZeroByZeroDivideException;
import com.implemica.calculator.util.exception.ZeroDivideException;
import com.implemica.calculator.util.format.NumericFormatter;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller
 *
 * @author Slavik Aleksey V.
 */
public class Controller implements Initializable{

    /**
     * Default value of numeric field
     */
    private static final String DEFAULT_NUMERIC_FIELD_VALUE = "0";

    /**
     * Default value of history field
     */
    private static final String DEFAULT_HISTORY_FIELD_VALUE = "";

    /**
     * Message, which obtained when scale of result is bigger than 10000
     */
    private static final String MESSAGE_OVERFLOW = "Overflow";

    /**
     * Message, which obtained when number divided by zero
     */
    private static final String MESSAGE_DIVIDE_BY_ZERO = "Cannot divide by zero";

    /**
     * Message, which obtained when zero divided by zero
     */
    private static final String MESSAGE_ZERO_DIVIDE_BY_ZERO = "Result is undefined";

    /**
     * Message, which obtained when find square root of negative number
     */
    private static final String MESSAGE_INVALID_INPUT = "Invalid input";

    /**
     * Big font size for numeric field
     */
    private static final int NUMERIC_FIELD_FONT_BIG = 30;

    /**
     * Medium font size for numeric field
     */
    private static final int NUMERIC_FIELD_FONT_MEDIUM = 24;

    /**
     * Small font size for numeric field
     */
    private static final int NUMERIC_FIELD_FONT_SMALL = 18;

    private static final int NUMERIC_FIELD_SIZE = 16;

    private static final int HISTORY_FIELD_SIZE = 34;

    private static final int BIG_FONT_COUNT = 12;

    private static final int MEDIUM_FONT_COUNT = 15;

    private static final String DOT = ".";

    private static final String COMMA = ",";

    private static final String ZERO_WITH_COMMA = "0,";

    private static final String ADD_TEXT = "+";

    private static final String MINUS_TEXT = "-";

    private static final String MULTIPLY_TEXT = "×";

    private static final String DIVIDE_TEXT = "÷";

    private static final String SQR_TEXT = "sqr";

    private static final String SQRT_TEXT = "√";

    private static final String NEGATE_TEXT = "negate";

    private static final String INVERSE_TEXT = "1/";

    private static final String FONT_STYLE_BIG = "-fx-font-size: " + NUMERIC_FIELD_FONT_BIG + "pt;";

    private static final String FONT_STYLE_MEDIUM = "-fx-font-size: " + NUMERIC_FIELD_FONT_MEDIUM + "pt;";

    private static final String FONT_STYLE_SMALL = "-fx-font-size: " + NUMERIC_FIELD_FONT_SMALL + "pt;";

    private static final String SEPARATOR = " ";

    private double xPos;

    private double yPos;

    private int historyPos;

    private String historyValue = DEFAULT_HISTORY_FIELD_VALUE;

    private boolean isLastNumber;

    private boolean isResult;

    /**
     * for calculateSqr, calculateSqrt, calculateNegate and calculateInverse
     */
    private boolean isUnaryResult;

    /**
     * if error is happens
     */
    private boolean isLockedScreen;

    private boolean isSequence;

    private boolean isMemoryLocked = true;

    private Calculator calculator = new Calculator();

    @FXML
    private Label numericField;

    @FXML
    private Label historyField;

    @FXML
    private Button zero;

    @FXML
    private Button one;

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button four;

    @FXML
    private Button five;

    @FXML
    private Button six;

    @FXML
    private Button seven;

    @FXML
    private Button eight;

    @FXML
    private Button nine;

    @FXML
    private Button negate;

    @FXML
    private Button comma;

    @FXML
    private Button equals;

    @FXML
    private Button add;

    @FXML
    private Button subtract;

    @FXML
    private Button multiply;

    @FXML
    private Button divide;

    @FXML
    private Button inverse;

    @FXML
    private Button sqr;

    @FXML
    private Button sqrt;

    @FXML
    private Button percent;

    @FXML
    private Button backspace;

    @FXML
    private Button clear;

    @FXML
    private Button clear_expr;

    @FXML
    private Button memory_clear;

    @FXML
    private Button memory_recall;

    @FXML
    private Button memory_add;

    @FXML
    private Button memory_minus;

    @FXML
    private Button memory_store;

    @FXML
    private Button history;

    @FXML
    private Button exit;

    @FXML
    private Button expand;

    @FXML
    private Button hide;

    @FXML
    private AnchorPane navigator;

    @FXML
    private AnchorPane title;

    @FXML
    private Button menuShow;

    @FXML
    private Button menuClose;

    @FXML
    private ImageView about;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button left;

    @FXML
    private Button right;

    private Button[] disabled;

    private Button[] disabledMemory;

    private ObservableList<String> observableList;

    public Controller() {
        observableList = FXCollections.observableArrayList();
        observableList.addAll(
                "Standard", "Scientific", "Programmer", "Date Calculation",
                "Currency", "Volume", "Length", "Weight and Mass", "Temperature", "Energy",
                "Area", "Speed", "Time", "Power", "Data", "Pressure", "Angle");
    }



    @FXML
    private void buttonMenuClick() {

        TranslateTransition openMenu = new TranslateTransition(new Duration(350), navigator);
        openMenu.setToX(0);
        TranslateTransition closeMenu = new TranslateTransition(new Duration(350), navigator);

        if (navigator.getTranslateX() != 0) {
            openMenu.play();
        } else {
            closeMenu.setToX(-(navigator.getWidth()));
            closeMenu.play();
        }
    }

    @FXML
    private void buttonDigitClick(ActionEvent event) {
        if (isLockedScreen) {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            disableButtons(false);
        }

        if (getNumericFieldText().length() == NUMERIC_FIELD_SIZE && !isLastNumber) {
            return;
        }

        String digit = ((Button)event.getSource()).getText();

        if (getNumericFieldText().equals(DEFAULT_NUMERIC_FIELD_VALUE) || isLastNumber) {
            setNumericFieldText(digit);
            isLastNumber = false;
        } else {
            appendNumericFieldText(digit);
        }
    }

    private void replaceLastSign(String sign) {
        replace(SEPARATOR + sign);
    }

    private void processBinaryOperator(Operator operator, String sign) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        if (isLockedScreen) {
            return;
        }

        String value = getNumericFieldText();

        if (isSequence) {
            if (isLastNumber) {
                replaceLastSign(operator.getText());
                calculator.changeOperator(getNumericFieldNumber(), operator);
            } else {
                appendHistoryFieldText(SEPARATOR + value + SEPARATOR + sign);
                setNumericFieldNumber(calculator.calculateIntermediateResult(getNumericFieldNumber()));
                calculator.changeOperator(getNumericFieldNumber(), operator);
            }
        } else {
            calculator.changeOperator(getNumericFieldNumber(), operator);
            if (isUnaryResult) {
                appendHistoryFieldText(SEPARATOR + sign);
            } else {
                historyValue = value + SEPARATOR + sign;
                setHistoryFieldText(value + SEPARATOR + sign);
            }
        }

        isSequence = true;
        isLastNumber = true;
        isResult = false;
        isUnaryResult = false;
    }

    @FXML
    private void buttonEqualsClick() {
        if (isLockedScreen) {
            setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            disableButtons(false);
        }

        if (isUnaryResult && !isSequence) {
            calculator.clearEntry();
            setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            isUnaryResult = false;
            return;
        }

        if (calculator.getOperator().equals(Operator.EMPTY)) {
            return;
        }

        try {
            if (isResult) {
                setNumericFieldNumber(calculator.calculateEqualsResult(getNumericFieldNumber()));
            } else {
                setNumericFieldNumber(calculator.calculateResult(getNumericFieldNumber()));
                setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            }
            isResult = true;
            isSequence = false;
            isLastNumber = true;
        } catch (ZeroDivideException e) {
            setNumericFieldText(MESSAGE_DIVIDE_BY_ZERO);
            disableButtons(true);
        } catch (OverflowException e) {
            setNumericFieldText(MESSAGE_OVERFLOW);
            disableButtons(true);
        } catch (ZeroByZeroDivideException e) {
            setNumericFieldText(MESSAGE_ZERO_DIVIDE_BY_ZERO);
            disableButtons(true);
        }
    }

    @FXML
    private void buttonCommaClick() {
        if (isLockedScreen) {
            return;
        }

        if (!getNumericFieldText().contains(COMMA)) {
            appendNumericFieldText(COMMA);
        }

        if (isLastNumber || isResult) {
            isLastNumber = false;
            isResult = false;
            setNumericFieldText(ZERO_WITH_COMMA);
        }
    }

    private void processPercent() {
        if (isLockedScreen) {
            return;
        }

        if (!isSequence) {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        } else {
            setNumericFieldNumber(calculator.percent(getNumericFieldNumber()));
            String value = getNumericFieldText();
            if (isLastNumber) {
                String history = getHistoryFieldText();
                int begin = history.lastIndexOf(SEPARATOR);
                setHistoryFieldText(history.substring(0, begin) + SEPARATOR + value);
            } else {
                appendHistoryFieldText(SEPARATOR + value);
            }
        }

        isLastNumber = true;
    }

    private void processNegate() {
        if (isLockedScreen) {
            return;
        }

        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(functionSurround(NEGATE_TEXT, value));
        } else if (isUnaryResult) {
            functionSurround(NEGATE_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + functionSurround(NEGATE_TEXT, value));
        }

        setNumericFieldNumber(calculator.negate(getNumericFieldNumber()));
        isUnaryResult = true;
        //isLastNumber = true;
    }

    private void processSqr() {
        if (isLockedScreen) {
            return;
        }

        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(functionSurround(SQR_TEXT, value));
        } else if (isUnaryResult) {
            functionSurround(SQR_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + functionSurround(SQR_TEXT, value));
        }

        try {
            setNumericFieldNumber(calculator.sqr(getNumericFieldNumber()));
        } catch (OverflowException e) {
            setNumericFieldText(MESSAGE_OVERFLOW);
        }

        isUnaryResult = true;
        isLastNumber = true;
    }

    private void processSqrt() throws SquareRootException {
        if (isLockedScreen) {
            return;
        }

        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(functionSurround(SQRT_TEXT, value));
        } else if (isUnaryResult) {
            functionSurround(SQRT_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + functionSurround(SQRT_TEXT, value));
        }

        setNumericFieldNumber(calculator.sqrt(getNumericFieldNumber()));

        isUnaryResult = true;
        isLastNumber = true;
    }

    private void processBackspace() {
        if (isLockedScreen) {
            setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            disableButtons(false);
        }

        String value = getNumericFieldText();

        if (value.length() < 2) {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        } else {
            setNumericFieldText(value.substring(0, value.length() - 1));
        }
    }

    private void processInverse() throws ZeroDivideException {
        if (isLockedScreen) {
            return;
        }

        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(functionSurround(INVERSE_TEXT, value));
        } else if (isUnaryResult){
            functionSurround(INVERSE_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + functionSurround(INVERSE_TEXT, value));
        }

        setNumericFieldNumber(calculator.inverse(getNumericFieldNumber()));
        isLastNumber = true;
        isUnaryResult = true;
    }

    private String getNumericFieldText() {
        return numericField.getText();
    }

    private void setNumericFieldText(String value) {
        /*if (isLockedScreen) {
            return;
        }*/
        numericField.setText(value);
        checkSize();
    }

    private BigDecimal getNumericFieldNumber() {
        String value = getNumericFieldText();
        value = value.replace(COMMA, DOT);
        return new BigDecimal(value);
    }

    private void setNumericFieldNumber(BigDecimal number) {
        number = number.stripTrailingZeros();
        //setNumericFieldText(NumericFormatter.format(number));
        if (number.toPlainString().replace(DOT, "").replace("-", "").length() <= NUMERIC_FIELD_SIZE) {
            setNumericFieldText(number.toPlainString().replace(DOT, COMMA));
        } else {
            number = NumericFormatter.round(number);
            setNumericFieldText(NumericFormatter.format(number));
        }
    }

    private String getHistoryFieldText() {
        return historyField.getText();
    }

    private void setHistoryFieldText(String value) {
        if (value.length() > HISTORY_FIELD_SIZE) {
            left.setVisible(true);
            right.setVisible(false);
            historyValue = value;
            historyField.setText(value.substring(value.length() - HISTORY_FIELD_SIZE));
        } else {
            left.setVisible(false);
            right.setVisible(false);
            historyField.setText(value);
        }
    }

    /**
     * Check font size of numeric field
     */
    private void checkSize() {
        if (getNumericFieldText().length() <= BIG_FONT_COUNT) {
            numericField.setStyle(FONT_STYLE_BIG);
        } else if (getNumericFieldText().length() < MEDIUM_FONT_COUNT) {
            numericField.setStyle(FONT_STYLE_MEDIUM);
        } else {
            numericField.setStyle(FONT_STYLE_SMALL);
        }
    }

    /**
     * Replace last element in history
     * @param expr
     */
    private void replace(String expr) {
        String str = getHistoryFieldText();
        int last = str.lastIndexOf(SEPARATOR);
        last = last == -1 ? 0 : last;
        setHistoryFieldText(str.substring(0, last) + expr);
    }

    /**
     * Surround given value using given function
     * @param func
     * @param value
     * @return
     */
    private String functionSurround(String func, String value) {
        return func + "(" + value + ")";
    }

    /**
     * Surround last element
     * @return
     */
    private void functionSurround(String func) {
        if (getHistoryFieldText().contains(SEPARATOR)) {
            replace(SEPARATOR + functionSurround(func, getLastElement()));
        } else {
            replace(functionSurround(func, getLastElement()));
        }
    }

    private String getLastElement() {
        String history = getHistoryFieldText();
        int begin = history.lastIndexOf(SEPARATOR) + 1;
        return history.substring(begin, history.length());
    }

    private void appendNumericFieldText(String value) {
        /*if (isLockedScreen) {
            return;
        }*/

        numericField.setText(getNumericFieldText() + value);
        checkSize();
    }

    private void appendHistoryFieldText(String value) {
        historyValue += value;
        setHistoryFieldText(historyValue);
    }

    @FXML
    private void operatorEventClick(ActionEvent event) {

        String id = ((Button)event.getSource()).getId();
        Operator button = Operator.searchById(id);

        try {
            switch (button) {
                case ADD:
                    processBinaryOperator(Operator.ADD, ADD_TEXT);
                    break;
                case SUBTRACT:
                    processBinaryOperator(Operator.SUBTRACT, MINUS_TEXT);
                    break;
                case MULTIPLY:
                    processBinaryOperator(Operator.MULTIPLY, MULTIPLY_TEXT);
                    break;
                case DIVIDE:
                    processBinaryOperator(Operator.DIVIDE, DIVIDE_TEXT);
                    break;
                case NEGATE:
                    processNegate();
                    break;
                case SQRT:
                    processSqrt();
                    break;
                case SQR:
                    processSqr();
                    break;
                case INVERSE:
                    processInverse();
                    break;
                case PERCENT:
                    processPercent();
                    break;
                case BACK:
                    processBackspace();
                    break;
                case C:
                    processClearAll();
                    break;
                case CE:
                    processClearExpr();
                    break;
                case MC:
                    processMemoryClear();
                    break;
                case MR:
                    processMemoryRecall();
                    break;
                case M_PLUS:
                    processMemoryAdd();
                    break;
                case M_MINUS:
                    processMemoryMinus();
                    break;
                case MS:
                    processMemoryStore();
                    break;
            }
        } catch (ZeroByZeroDivideException e) {
            setNumericFieldText(MESSAGE_ZERO_DIVIDE_BY_ZERO);
            disableButtons(true);
        } catch (ZeroDivideException e) {
            setNumericFieldText(MESSAGE_DIVIDE_BY_ZERO);
            disableButtons(true);
        } catch (OverflowException e) {
            setNumericFieldText(MESSAGE_OVERFLOW);
            disableButtons(true);
        } catch (SquareRootException e) {
            setNumericFieldText(MESSAGE_INVALID_INPUT);
            disableButtons(true);
        }
    }

    @FXML
    private void operatorKeyClick(KeyEvent event) {
        Operator button = Operator.searchButtonByEvent(event);
        if (button != null) {
            switch (button) {
                case ZERO:
                    clickOnButton(zero);
                    break;
                case ONE:
                    clickOnButton(one);
                    break;
                case TWO:
                    clickOnButton(two);
                    break;
                case THREE:
                    clickOnButton(three);
                    break;
                case FOUR:
                    clickOnButton(four);
                    break;
                case FIVE:
                    clickOnButton(five);
                    break;
                case SIX:
                    clickOnButton(six);
                    break;
                case SEVEN:
                    clickOnButton(seven);
                    break;
                case EIGHT:
                    clickOnButton(eight);
                    break;
                case NINE:
                    clickOnButton(nine);
                    break;
                case COMMA:
                    clickOnButton(comma);
                    break;
                case EQUALS:
                    clickOnButton(equals);
                    break;
                case BACK:
                    clickOnButton(backspace);
                    break;
                case ADD:
                    clickOnButton(add);
                    break;
                case SUBTRACT:
                    clickOnButton(subtract);
                    break;
                case DIVIDE:
                    clickOnButton(divide);
                    break;
                case MULTIPLY:
                    clickOnButton(multiply);
                    break;
                case NEGATE:
                    clickOnButton(negate);
                    break;
                case INVERSE:
                    clickOnButton(inverse);
                    break;
                case CE:
                    clickOnButton(clear_expr);
                    break;
                case C:
                    clickOnButton(clear);
                    break;
                case SQR:
                    clickOnButton(sqr);
                    break;
                case SQRT:
                    clickOnButton(sqrt);
                    break;
                case PERCENT:
                    clickOnButton(percent);
                    break;
                case MR:
                    clickOnButton(memory_recall);
                    break;
                case MS:
                    clickOnButton(memory_store);
                    break;
                case MC:
                    clickOnButton(memory_clear);
                    break;
                case M_PLUS:
                    clickOnButton(memory_add);
                    break;
                case M_MINUS:
                    clickOnButton(memory_minus);
                    break;
            }
        }
    }

    private void processMemoryClear() {
        if (isLockedScreen) {
            return;
        }

        disableMemoryButtons(true);
        calculator.memoryClear();
    }

    private void processMemoryRecall() {
        if (isLockedScreen) {
            return;
        }

        setNumericFieldNumber(calculator.memoryRecall());
        isLastNumber = true;
    }

    private void processMemoryAdd() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        calculator.memoryAdd(getNumericFieldNumber());
        isLastNumber = true;
    }

    private void processMemoryMinus() {
        if (isLockedScreen) {
            return;
        }

        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        calculator.memorySubtract(getNumericFieldNumber());
        isLastNumber = true;
    }

    private void processMemoryStore() {
        if (isLockedScreen) {
            return;
        }

        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        calculator.memoryStore(getNumericFieldNumber());
        isLastNumber = true;
    }

    private void processClearAll() {
        isLastNumber = false;
        isResult = false;
        isUnaryResult = false;
        isSequence = false;
        disableButtons(false);
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
        calculator.clearAll();
    }

    private void processClearExpr() {
        if (isLockedScreen) {
            disableButtons(false);
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
        } else {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            calculator.clearEntry();
        }
    }

    private void clickOnButton(Button button) {
        button.arm();
        button.fire();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> button.disarm());
        pause.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disabled = new Button[] {
                divide, multiply, subtract, add, comma, negate,
                memory_store, memory_recall, memory_minus, memory_add, memory_clear,
                percent, sqrt, sqr, inverse
        };

        disabledMemory = new Button[] {memory_clear, memory_recall};

        disableMemoryButtons(true);
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);

        Image historyImage = new Image(Launcher.class.getResourceAsStream("/images/history.png"));
        ImageView imageView = new ImageView(historyImage);
        history.setGraphic(imageView);

        Image menuImage = new Image(Launcher.class.getResourceAsStream("/images/menu.png"));
        imageView = new ImageView(menuImage);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        menuShow.setGraphic(imageView);

        imageView = new ImageView(menuImage);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        menuClose.setGraphic(imageView);

        Image aboutImage = new Image(Launcher.class.getResourceAsStream("/images/about.png"));
        about.setImage(aboutImage);

        listView.setItems(observableList);
    }

    private void disableButtons(boolean disable) {
        isLockedScreen = disable;
        for (Button item : disabled) {
            item.setDisable(disable);
        }
    }

    private void disableMemoryButtons(boolean disable) {
        isMemoryLocked = disable;
        for (Button item : disabledMemory) {
            item.setDisable(disable);
        }
    }

    @FXML
    private void processExit() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void processExpand() {
        Stage stage = (Stage) expand.getScene().getWindow();

        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }

    @FXML
    private void processHide() {
        Stage stage = (Stage) hide.getScene().getWindow();

        if (stage.isIconified()) {
            stage.setIconified(false);
        } else {
            stage.setIconified(true);
        }
    }

    @FXML
    private void moveWindow() {
        Stage stage = (Stage) title.getScene().getWindow();

        title.setOnMousePressed(mouseEvent -> {
            xPos = stage.getX() - mouseEvent.getScreenX();
            yPos = stage.getY() - mouseEvent.getScreenY();
        });

        title.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + xPos);
            stage.setY(mouseEvent.getScreenY() + yPos);
        });
    }

    @FXML
    private void leftClick() {
        historyPos -= HISTORY_FIELD_SIZE;

        if (historyPos < 0) {
            historyPos = 0;
            left.setVisible(false);
        }

        right.setVisible(true);
        historyField.setText(historyValue.substring(historyPos, historyPos + HISTORY_FIELD_SIZE));
    }

    @FXML
    private void rightClick() {
        historyPos += HISTORY_FIELD_SIZE;

        if (historyPos + HISTORY_FIELD_SIZE > historyValue.length()) {
            historyPos = historyValue.length() - HISTORY_FIELD_SIZE;
            right.setVisible(false);
        }

        left.setVisible(true);
        historyField.setText(historyValue.substring(historyPos, historyPos + HISTORY_FIELD_SIZE));
    }
}
