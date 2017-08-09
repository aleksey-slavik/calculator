package com.implemica.calculator.controller;

import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.History;
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

    private static final int NUMERIC_FIELD_SIZE = 16;

    //private static final int HISTORY_FIELD_SIZE = 34;

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

    @FXML
    private void buttonDigitClick(ActionEvent event) {
        if (isLockedScreen) {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            disableButtons(false);
            disableMemoryButtons(isMemoryLocked);
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

    private void processBinaryOperator(Operator operator, String sign) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        if (isLockedScreen) {
            return;
        }

        String value = getNumericFieldText();

        if (isSequence) {
            if (isLastNumber) {
                history.replaceLastSign(operator.getText());
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
            setHistoryFieldText(history.surround(NEGATE_TEXT, value));
        } else if (isUnaryResult) {
            history.surround(NEGATE_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(NEGATE_TEXT, value));
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
            setHistoryFieldText(history.surround(SQR_TEXT, value));
        } else if (isUnaryResult) {
            history.surround(SQR_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(SQR_TEXT, value));
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
            setHistoryFieldText(history.surround(SQRT_TEXT, value));
        } else if (isUnaryResult) {
            history.surround(SQRT_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(SQRT_TEXT, value));
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
            setHistoryFieldText(history.surround(INVERSE_TEXT, value));
        } else if (isUnaryResult){
            history.surround(INVERSE_TEXT);
        } else {
            appendHistoryFieldText(SEPARATOR + history.surround(INVERSE_TEXT, value));
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
        if (value.length() > getLabelSize()) {
            left.setVisible(true);
            right.setVisible(false);
            history.setHistory(value);
            historyPos = history.getHistory().length();
            historyField.setText(value.substring(value.length() - getLabelSize()));
        } else {
            left.setVisible(false);
            //right.setVisible(false);
            historyField.setText(value);
            historyPos = history.getHistory().length();
        }
    }

    private void appendNumericFieldText(String value) {
        /*if (isLockedScreen) {
            return;
        }*/

        numericField.setText(getNumericFieldText() + value);
    }

    private void appendHistoryFieldText(String value) {
        history.appendHistory(value);
        setHistoryFieldText(history.getHistory());
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

    private int getLabelSize() {
        return (int) (historyField.getWidth() / 7.5);
    }
}