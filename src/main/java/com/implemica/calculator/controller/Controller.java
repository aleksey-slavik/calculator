package com.implemica.calculator.controller;

import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.History;
import com.implemica.calculator.model.Memory;
import com.implemica.calculator.model.Operator;
import com.implemica.calculator.model.exception.OverflowException;
import com.implemica.calculator.model.exception.SquareRootException;
import com.implemica.calculator.model.exception.ZeroByZeroDivideException;
import com.implemica.calculator.model.exception.ZeroDivideException;
import com.implemica.calculator.view.enums.CalculatorButton;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller
 *
 * @author Slavik Aleksey V.
 */
public class Controller implements Initializable {

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

    private static final int NUMERIC_FIELD_SIZE = 16;

    private static final String DOT = ".";

    private static final String COMMA = ",";

    private static final String ZERO_WITH_COMMA = "0,";

    private static final String SEPARATOR = " ";

    /**
     * End point of shown history
     */
    private int historyPos;

    private boolean canChange;

    /**
     * true if can calculate intermediate result
     */
    private boolean canCalculateResult;

    /**
     * for calculateSqr, calculateSqrt, calculateNegate and calculateInverse
     */
    private boolean isPreviousUnary;

    /**
     * if error is happens
     */
    private boolean isError;

    private boolean isSequence;

    private boolean isMemoryAvailable = true;

    private Calculator calculator = new Calculator();

    private History history = new History();

    private Memory memory = new Memory();

    @FXML
    private Label numericField;

    @FXML
    private Label historyField;

    @FXML
    private Button negate;

    @FXML
    private Button comma;

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
    private Button left;

    @FXML
    private Button right;

    private Button[] disabled;

    private Button[] disabledMemory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        disabled = new Button[]{
                divide, multiply, subtract, add, comma, negate,
                memory_store, memory_recall, memory_minus, memory_add, memory_clear,
                percent, sqrt, sqr, inverse
        };

        disabledMemory = new Button[]{memory_clear, memory_recall};

        disableMemoryButtons(true);
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);

        historyField.widthProperty().addListener(observable -> {
            if (getLabelSize() > history.getHistory().length()) {
                historyField.setText(history.getHistory());
            } else {
                if (getLabelSize() < history.getHistory().substring(0, historyPos).length()) {
                    historyField.setText(history.getHistory().substring(0, historyPos));
                    left.setVisible(true);
                } else {
                    int newPos = historyPos - getLabelSize();
                    newPos = newPos < 0 ? 0 : newPos;
                    historyField.setText(history.getHistory().substring(newPos, historyPos));
                    left.setVisible(false);
                }
            }
        });
    }

    @FXML
    private void buttonDigitClick(ActionEvent event) {
        if (isError) {
            normalStatement();
        }

        if (checkSize() && !canChange) {
            return;
        }

        String digit = ((Button) event.getSource()).getText();

        if (getNumericFieldText().equals(DEFAULT_NUMERIC_FIELD_VALUE) || canChange) {
            setNumericFieldText(digit);
            canChange = false;
        } else {
            appendNumericFieldText(digit);
        }
    }

    @FXML
    private void buttonEqualsClick() {
        if (isError) {
            normalStatement();
        }

        if (calculator.getOperator().equals(Operator.EMPTY)) {
            history.clearHistory();
            setHistoryFieldText(history.getHistory());
            isPreviousUnary = false;
            return;
        }

        try {
            if (canCalculateResult) {
                setNumericFieldNumber(calculator.calculateEqualsResult(getNumericFieldNumber()));
            } else {
                setNumericFieldNumber(calculator.calculateResult(getNumericFieldNumber()));
                history.clearHistory();
                setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            }
            canCalculateResult = true;
            isSequence = false;
            canChange = true;
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        } catch (ZeroByZeroDivideException e) {
            errorStatement(MESSAGE_ZERO_DIVIDE_BY_ZERO);
        }
    }

    @FXML
    private void buttonCommaClick() {
        if (!getNumericFieldText().contains(COMMA) && !checkSize()) {
            appendNumericFieldText(COMMA);
        }

        if (canChange || canCalculateResult) {
            canChange = false;
            canCalculateResult = false;
            setNumericFieldText(ZERO_WITH_COMMA);
        }
    }

    @FXML
    private void operatorKeyClick(KeyEvent event) {
        CalculatorButton button = CalculatorButton.searchButtonByEvent(event);
        Parent parent = (Parent) event.getSource();

        if (button != null) {
            clickOnButton((Button) parent.lookup(button.getFXId()));
        }
    }

    @FXML
    private void leftClick() {
        historyPos -= getLabelSize();

        if (historyPos - getLabelSize() < 0) {
            historyPos = getLabelSize();
            left.setVisible(false);
        }

        right.setVisible(true);
        historyField.setText(history.getHistory().substring(historyPos - getLabelSize(), historyPos));
    }

    @FXML
    private void rightClick() {
        historyPos += getLabelSize();

        if (historyPos > history.getHistory().length()) {
            historyPos = history.getHistory().length();
            right.setVisible(false);
        }

        int delta = historyPos - getLabelSize();

        if (delta < 0) {
            left.setVisible(false);
            historyField.setText(history.getHistory().substring(0, historyPos));
        } else {
            left.setVisible(true);
            historyField.setText(history.getHistory().substring(delta, historyPos));
        }
    }

    @FXML
    private void binaryOperatorClick(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();

        try {
            if (id.equals(CalculatorButton.ADD.getId())) {
                processBinaryOperator(Operator.ADD);
            } else if (id.equals(CalculatorButton.SUBTRACT.getId())) {
                processBinaryOperator(Operator.SUBTRACT);
            } else if (id.equals(CalculatorButton.DIVIDE.getId())) {
                processBinaryOperator(Operator.DIVIDE);
            } else if (id.equals(CalculatorButton.MULTIPLY.getId())) {
                processBinaryOperator(Operator.MULTIPLY);
            }
        } catch (ZeroByZeroDivideException e) {
            errorStatement(MESSAGE_ZERO_DIVIDE_BY_ZERO);
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        }
    }

    @FXML
    private void percentClick() {
        if (!isSequence) {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        } else {
            setNumericFieldNumber(calculator.percent(getNumericFieldNumber()));
            String value = getNumericFieldText().replace(" ", "");

            if (canChange) {
                history.replace(value);
                setHistoryFieldText(history.getHistory());
            } else {
                history.appendHistory(value);
                setHistoryFieldText(history.getHistory());
            }
        }

        canChange = true;
    }

    @FXML
    private void negateClick() {

        if (isPreviousUnary) {
            history.surround(Operator.NEGATE);
            setHistoryFieldText(history.getHistory());
        }

        setNumericFieldNumber(calculator.negate(getNumericFieldNumber()));
    }

    @FXML
    private void sqrClick() {
        String value = getNumericFieldText().replace(" ", "");

        if (history.isEmpty()) {
            history.setHistory(history.surround(Operator.SQR, value));
            setHistoryFieldText(history.getHistory());
        } else if (isPreviousUnary) {
            history.surround(Operator.SQR);
            setHistoryFieldText(history.getHistory());
        } else {
            history.appendHistory(history.surround(Operator.SQR, value));
            setHistoryFieldText(history.getHistory());
        }

        try {
            setNumericFieldNumber(calculator.sqr(getNumericFieldNumber()));
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        }

        isPreviousUnary = true;
        canChange = true;
    }

    @FXML
    private void sqrtClick() {
        String value = getNumericFieldText().replace(" ", "");

        if (history.isEmpty()) {
            history.setHistory(history.surround(Operator.SQRT, value));
            setHistoryFieldText(history.getHistory());
        } else if (isPreviousUnary) {
            history.surround(Operator.SQRT);
            setHistoryFieldText(history.getHistory());
        } else {
            history.appendHistory(history.surround(Operator.SQRT, value));
            setHistoryFieldText(history.getHistory());
        }

        try {
            setNumericFieldNumber(calculator.sqrt(getNumericFieldNumber()));
        } catch (SquareRootException e) {
            errorStatement(MESSAGE_INVALID_INPUT);
        }

        isPreviousUnary = true;
        canChange = true;
    }

    @FXML
    private void backspaceClick() {
        if (isError) {
            normalStatement();
        }

        if (canChange) {
            return;
        }

        String value = getNumericFieldText().replace(" ", "");
        int minLength = value.contains("-") ? 3 : 2;

        if (value.length() < minLength) {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        } else {
            setNumericFieldText(value.substring(0, value.length() - 1));
        }
    }

    @FXML
    private void inverseClick() {
        String value = getNumericFieldText().replace(" ", "");

        if (history.isEmpty()) {
            history.setHistory(history.surround(Operator.INVERSE, value));
            setHistoryFieldText(history.getHistory());
        } else if (isPreviousUnary) {
            history.surround(Operator.INVERSE);
            setHistoryFieldText(history.getHistory());
        } else {
            history.appendHistory(history.surround(Operator.INVERSE, value));
            setHistoryFieldText(history.getHistory());
        }

        try {
            setNumericFieldNumber(calculator.inverse(getNumericFieldNumber()));
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        }

        isPreviousUnary = true;
        canChange = true;
    }

    @FXML
    private void memoryClearClick() {
        disableMemoryButtons(true);
        memory.memoryClear();
    }

    @FXML
    private void memoryRecallClick() {
        setNumericFieldNumber(memory.memoryRecall());
        canChange = false;
    }

    @FXML
    private void memoryAddClick() {
        if (isMemoryAvailable) {
            disableMemoryButtons(false);
        }

        memory.memoryAdd(getNumericFieldNumber());
        canChange = false;
    }

    @FXML
    private void memorySubtractClick() {
        if (isMemoryAvailable) {
            disableMemoryButtons(false);
        }

        memory.memorySubtract(getNumericFieldNumber());
        canChange = false;
    }

    @FXML
    private void memoryStoreClick() {
        if (isMemoryAvailable) {
            disableMemoryButtons(false);
        }

        memory.memoryStore(getNumericFieldNumber());
        canChange = false;
    }

    @FXML
    private void clearClick() {
        canChange = false;
        canCalculateResult = false;
        isPreviousUnary = false;
        isSequence = false;
        normalStatement();
        calculator.clearAll();
    }

    @FXML
    private void clearEntryClick() {
        if (isError) {
            normalStatement();
        } else {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            calculator.clearEntry();
        }
    }

    private void processBinaryOperator(Operator operator) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        String value = getNumericFieldText().replace(" ", "");

        if (isSequence) {
            if (isPreviousUnary) {
                history.appendHistory(operator.getText());
                setHistoryFieldText(history.getHistory());
                setNumericFieldNumber(calculator.calculateIntermediateResult(getNumericFieldNumber()));
                calculator.changeOperator(getNumericFieldNumber(), operator);
            } else if (canChange) {
                history.replaceLastSign(operator);
                setHistoryFieldText(history.getHistory());
                calculator.changeOperator(getNumericFieldNumber(), operator);
            } else {
                history.appendHistory(value + SEPARATOR + operator.getText());
                setHistoryFieldText(history.getHistory());
                setNumericFieldNumber(calculator.calculateIntermediateResult(getNumericFieldNumber()));
                calculator.changeOperator(getNumericFieldNumber(), operator);
            }
        } else {
            calculator.changeOperator(getNumericFieldNumber(), operator);
            if (isPreviousUnary) {
                history.appendHistory(operator.getText());
                setHistoryFieldText(history.getHistory());
            } else {
                history.setHistory(value + SEPARATOR + operator.getText());
                setHistoryFieldText(value + SEPARATOR + operator.getText());
            }
        }

        isSequence = true;
        canChange = true;
        canCalculateResult = false;
        isPreviousUnary = false;
    }

    private String getNumericFieldText() {

        return numericField.getText();
    }

    private void setNumericFieldText(String value) {
        if (!isError) {
            value = Formatter.display(value);
        }

        numericField.setText(value);
    }

    private BigDecimal getNumericFieldNumber() {
        String value = getNumericFieldText().replace(" ", "").replace(COMMA, DOT);
        return new BigDecimal(value);
    }

    private void setNumericFieldNumber(BigDecimal number) {

        numericField.setText(Formatter.display(number));
    }

    private void setHistoryFieldText(String value) {
        if (value.length() > getLabelSize()) {
            left.setVisible(true);
            right.setVisible(false);
            history.setHistory(value);
            historyPos = history.getHistory().length();
            historyField.setText(history.getHistory().substring(value.length() - getLabelSize() + 1));
        } else {
            left.setVisible(false);
            right.setVisible(false);
            historyField.setText(value);
            history.setHistory(value);
            historyPos = history.getHistory().length();
        }
    }

    private void appendNumericFieldText(String value) {

        setNumericFieldText(getNumericFieldText() + value);
    }

    private void clickOnButton(Button button) {
        button.arm();
        button.fire();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> button.disarm());
        pause.play();
    }

    private void disableButtons(boolean disable) {
        isError = disable;
        for (Button item : disabled) {
            item.setDisable(disable);
        }
    }

    private void disableMemoryButtons(boolean disable) {
        isMemoryAvailable = disable;
        for (Button item : disabledMemory) {
            item.setDisable(disable);
        }
    }

    private int getLabelSize() {

        return (int) (historyField.getWidth() / 7.5);
    }

    private boolean checkSize() {
        String str = getNumericFieldText().replace(COMMA, "").replace("-", "").replace(" ", "");
        if (str.startsWith("0")) {
            return str.length() >= NUMERIC_FIELD_SIZE + 1;
        } else {
            return str.length() >= NUMERIC_FIELD_SIZE;
        }
    }

    private void normalStatement() {
        disableButtons(false);
        disableMemoryButtons(isMemoryAvailable);
        calculator.resetOperator();
        history.clearHistory();
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
    }

    private void errorStatement(String error) {
        disableButtons(true);
        setNumericFieldText(error);
    }
}