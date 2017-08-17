package com.implemica.calculator.controller;

import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.History;
import com.implemica.calculator.model.Memory;
import com.implemica.calculator.util.enums.Operator;
import com.implemica.calculator.util.exception.OverflowException;
import com.implemica.calculator.util.exception.SquareRootException;
import com.implemica.calculator.util.exception.ZeroByZeroDivideException;
import com.implemica.calculator.util.exception.ZeroDivideException;
import com.implemica.calculator.util.format.NumericFormatter;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private static final String ADD_TEXT = "+";

    private static final String MINUS_TEXT = "-";

    private static final String MULTIPLY_TEXT = "×";

    private static final String DIVIDE_TEXT = "÷";

    private static final String SQR_TEXT = "sqr";

    private static final String SQRT_TEXT = "√";

    private static final String NEGATE_TEXT = "negate";

    private static final String INVERSE_TEXT = "1/";

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
    private void operatorEventClick(ActionEvent event) {

        String id = ((Button) event.getSource()).getId();
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
            errorStatement(MESSAGE_ZERO_DIVIDE_BY_ZERO);
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        } catch (SquareRootException e) {
            errorStatement(MESSAGE_INVALID_INPUT);
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

    private void processBinaryOperator(Operator operator, String sign) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        String value = getNumericFieldText();

        if (isSequence) {
            if (isLastNumber) {
                history.replaceLastSign(operator.getText());
                setHistoryFieldText(history.getHistory());
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
                history.setHistory(value + SEPARATOR + sign);
                setHistoryFieldText(value + SEPARATOR + sign);
            }
        }

        isSequence = true;
        isLastNumber = true;
        isResult = false;
        isUnaryResult = false;
    }

    private void processPercent() {
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
        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(NEGATE_TEXT, value));
        } else if (isUnaryResult) {
            history.surround(NEGATE_TEXT);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(NEGATE_TEXT, value));
        }

        setNumericFieldNumber(calculator.negate(getNumericFieldNumber()));
        isUnaryResult = true;
    }

    private void processSqr() {
        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(SQR_TEXT, value));
        } else if (isUnaryResult) {
            history.surround(SQR_TEXT);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(SQR_TEXT, value));
        }

        try {
            setNumericFieldNumber(calculator.sqr(getNumericFieldNumber()));
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        }

        isUnaryResult = true;
        isLastNumber = true;
    }

    private void processSqrt() throws SquareRootException {
        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(SQRT_TEXT, value));
        } else if (isUnaryResult) {
            history.surround(SQRT_TEXT);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(SQRT_TEXT, value));
        }

        setNumericFieldNumber(calculator.sqrt(getNumericFieldNumber()));

        isUnaryResult = true;
        isLastNumber = true;
    }

    private void processBackspace() {
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

    private void processInverse() throws ZeroDivideException {
        String value = getNumericFieldText();

        if (getHistoryFieldText().isEmpty()) {
            setHistoryFieldText(history.surround(INVERSE_TEXT, value));
        } else if (isUnaryResult) {
            history.surround(INVERSE_TEXT);
            setHistoryFieldText(history.getHistory());
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(INVERSE_TEXT, value));
        }

        setNumericFieldNumber(calculator.inverse(getNumericFieldNumber()));
        isUnaryResult = true;
        isLastNumber = true;
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

    private void processMemoryClear() {
        disableMemoryButtons(true);
        memory.memoryClear();
    }

    private void processMemoryRecall() {
        setNumericFieldNumber(memory.memoryRecall());
        isLastNumber = true;
    }

    private void processMemoryAdd() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        memory.memoryAdd(getNumericFieldNumber());
        isLastNumber = true;
    }

    private void processMemoryMinus() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        memory.memorySubtract(getNumericFieldNumber());
        isLastNumber = true;
    }

    private void processMemoryStore() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        memory.memoryStore(getNumericFieldNumber());
        isLastNumber = true;
    }

    private void processClearAll() {
        isLastNumber = false;
        isResult = false;
        isUnaryResult = false;
        isSequence = false;
        normalStatement();
        calculator.clearAll();
    }

    private void processClearExpr() {
        if (isLockedScreen) {
            normalStatement();
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