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
        if (isLockedScreen) {
            normalStatement();
        }

        if (checkSize() && !isLastNumber) {
            return;
        }

        String digit = ((Button) event.getSource()).getText();

        if (getNumericFieldText().equals(DEFAULT_NUMERIC_FIELD_VALUE) || isLastNumber) {
            setNumericFieldText(digit);
            isLastNumber = false;
        } else {
            appendNumericFieldText(digit);
        }
    }

    @FXML
    private void buttonEqualsClick() {
        if (isLockedScreen) {
            normalStatement();
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
                history.clearHistory();
                setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            }
            isResult = true;
            isSequence = false;
            isLastNumber = true;
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

        if (isLastNumber || isResult) {
            isLastNumber = false;
            isResult = false;
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

    @FXML
    private void negateClick() {
        //String value = getNumericFieldText();

        if(isUnaryResult) {
            history.surround(Operator.NEGATE);
            setHistoryFieldText(history.getHistory());
        }
        /*
        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(Operator.NEGATE, value));
        } else if (isUnaryResult) {
            history.surround(Operator.NEGATE);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(Operator.NEGATE, value));
        }
        */

        setNumericFieldNumber(calculator.negate(getNumericFieldNumber()));
        //isUnaryResult = true;
    }

    @FXML
    private void sqrClick() {
        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(Operator.SQR, value));
        } else if (isUnaryResult) {
            history.surround(Operator.SQR);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(Operator.SQR, value));
        }

        try {
            setNumericFieldNumber(calculator.sqr(getNumericFieldNumber()));
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        }

        isUnaryResult = true;
        isLastNumber = true;
    }

    @FXML
    private void sqrtClick() {
        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(Operator.SQRT, value));
        } else if (isUnaryResult) {
            history.surround(Operator.SQRT);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(Operator.SQRT, value));
        }

        try {
            setNumericFieldNumber(calculator.sqrt(getNumericFieldNumber()));
        } catch (SquareRootException e) {
            errorStatement(MESSAGE_INVALID_INPUT);
        }

        isUnaryResult = true;
        isLastNumber = true;
    }

    @FXML
    private void backspaceClick() {
        if (isLockedScreen) {
            normalStatement();
        }

        if (isLastNumber) {
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
        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(Operator.INVERSE, value));
        } else if (isUnaryResult) {
            history.surround(Operator.INVERSE);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(Operator.INVERSE, value));
        }

        try {
            setNumericFieldNumber(calculator.inverse(getNumericFieldNumber()));
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        }

        isUnaryResult = true;
        isLastNumber = true;
    }

    @FXML
    private void memoryClearClick() {
        disableMemoryButtons(true);
        memory.memoryClear();
    }

    @FXML
    private void memoryRecallClick() {
        setNumericFieldNumber(memory.memoryRecall());
        isLastNumber = true;
    }

    @FXML
    private void memoryAddClick() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        memory.memoryAdd(getNumericFieldNumber());
        isLastNumber = true;
    }

    @FXML
    private void memorySubtractClick() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        memory.memorySubtract(getNumericFieldNumber());
        isLastNumber = true;
    }

    @FXML
    private void memoryStoreClick() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        memory.memoryStore(getNumericFieldNumber());
        isLastNumber = true;
    }

    @FXML
    private void clearClick() {
        isLastNumber = false;
        isResult = false;
        isUnaryResult = false;
        isSequence = false;
        normalStatement();
        calculator.clearAll();
    }

    @FXML
    private void clearEntryClick() {
        if (isLockedScreen) {
            normalStatement();
        } else {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            calculator.clearEntry();
        }
    }

    private void processBinaryOperator(Operator operator) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        String value = getNumericFieldText();

        if (isSequence) {
            if (isLastNumber) {
                history.replaceLastSign(operator);
                setHistoryFieldText(history.getHistory());
                calculator.changeOperator(getNumericFieldNumber(), operator);
            } else {
                appendHistoryFieldText(SEPARATOR + value + SEPARATOR + operator.getText());
                setNumericFieldNumber(calculator.calculateIntermediateResult(getNumericFieldNumber()));
                calculator.changeOperator(getNumericFieldNumber(), operator);
            }
        } else {
            calculator.changeOperator(getNumericFieldNumber(), operator);
            if (isUnaryResult) {
                appendHistoryFieldText(SEPARATOR + operator.getText());
            } else {
                history.setHistory(value + SEPARATOR + operator.getText());
                setHistoryFieldText(value + SEPARATOR + operator.getText());
            }
        }

        isSequence = true;
        isLastNumber = true;
        isResult = false;
        isUnaryResult = false;
    }

    private String getNumericFieldText() {

        return numericField.getText().replace(" ", "");
    }

    private void setNumericFieldText(String value) {
        if (!isLockedScreen) {
            value = value.replace(DOT, COMMA);
            value = NumericFormatter.format(value);
        }

        numericField.setText(value);
    }

    private BigDecimal getNumericFieldNumber() {
        String value = getNumericFieldText().replace(" ", "").replace(COMMA, DOT);
        return new BigDecimal(value);
    }

    private void setNumericFieldNumber(BigDecimal number) {
        number = number.stripTrailingZeros();
        if (number.toPlainString().replace(DOT, "").replace("-", "").replace(" ", "").length() <= NUMERIC_FIELD_SIZE) {
            setNumericFieldText(number.toPlainString());
        } else {
            number = NumericFormatter.round(number);
            setNumericFieldText(NumericFormatter.format(number));
        }
    }

    private String getHistoryFieldText() {

        return historyField.getText();
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

    private void appendHistoryFieldText(String value) {
        history.appendHistory(value);
        setHistoryFieldText(history.getHistory());
    }

    private void clickOnButton(Button button) {
        button.arm();
        button.fire();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> button.disarm());
        pause.play();
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

    private int getLabelSize() {

        return (int) (historyField.getWidth() / 7.5);
    }

    private boolean checkSize() {

        return getNumericFieldText().replace(COMMA, "").replace("-", "").replace(" ", "").length() >= NUMERIC_FIELD_SIZE;
    }

    private void normalStatement() {
        disableButtons(false);
        disableMemoryButtons(isMemoryLocked);
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