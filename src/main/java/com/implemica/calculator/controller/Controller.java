package com.implemica.calculator.controller;

import com.implemica.calculator.controller.util.NumericFormatter;
import com.implemica.calculator.controller.util.HistoryFormatter;
import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.util.CalculationModel;
import com.implemica.calculator.model.util.Operator;
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
 * Processing work of buttons of calculator
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

    /**
     * Maximum of numeric field size
     */
    private static final int NUMERIC_FIELD_SIZE = 16;

    /**
     * Pause duration when pressing buttons
     */
    private static final double DURATION = 0.1;

    /**
     * Width of character
     */
    private static final double CHAR_WIDTH = 7.5;

    /**
     * Decimal separator for {@link BigDecimal}
     */
    private static final String DOT = ".";

    /**
     * Decimal separator for numbers in numeric field
     */
    private static final String COMMA = ",";

    /**
     * String representation of zero with decimal separator
     */
    private static final String ZERO_WITH_COMMA = "0,";

    /**
     * Grouping separator
     */
    private static final String SEPARATOR = " ";

    /**
     * End point of shown history
     */
    private int historyPos;

    /**
     * True if can change value in numeric field
     */
    private boolean isEditable;

    /**
     * True if can calculate intermediate result
     */
    private boolean isCalculateResult;

    /**
     * True if previous operator belong to unary group
     */
    private boolean isPreviousUnary;

    /**
     * True if error is happens
     */
    private boolean isError;

    /**
     * True if several operations have been carried out before
     */
    private boolean isSequence;

    /**
     * True if memory is cleared
     */
    private boolean isMemoryLocked = true;

    /**
     * {@link Calculator} object which consist set of methods for calculation
     */
    private Calculator calculator = new Calculator();

    /**
     * {@link HistoryFormatter} object which consist set of methods for manage history
     */
    private HistoryFormatter history = new HistoryFormatter();

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

    /**
     * Array of buttons which are disabled when errors happens
     */
    private Button[] disabled;

    /**
     * Array of buttons which are disabled when memory cleared
     */
    private Button[] disabledMemory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //add buttons which are disabled when errors happens
        disabled = new Button[]{
                divide, multiply, subtract, add, comma, negate,
                memory_store, memory_recall, memory_minus, memory_add, memory_clear,
                percent, sqrt, sqr, inverse
        };

        //add buttons which are disabled when memory cleared
        disabledMemory = new Button[]{memory_clear, memory_recall};

        //setup default statement of calculator
        disableMemoryButtons(true);
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);

        //add resize history field listener
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

    /**
     * Click on digits buttons processing
     *
     * @param event     digits event
     */
    @FXML
    private void buttonDigitClick(ActionEvent event) {
        if (isError) {
            normalStatement();
        }

        if (checkSize() && !isEditable) {
            return;
        }

        String digit = ((Button) event.getSource()).getText();

        if (getNumericFieldText().equals(DEFAULT_NUMERIC_FIELD_VALUE) || isEditable) {
            setNumericFieldText(digit);
            isEditable = false;
        } else {
            appendNumericFieldText(digit);
        }
    }

    /**
     * Click on equals button processing
     */
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
            if (isCalculateResult) {
                setNumericFieldNumber(calculator.calculateEqualsResult(getNumericFieldNumber()));
            } else {
                setNumericFieldNumber(calculator.calculateResult(getNumericFieldNumber()));
                history.clearHistory();
                setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
            }
            isCalculateResult = true;
            isSequence = false;
            isEditable = true;
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        } catch (ZeroByZeroDivideException e) {
            errorStatement(MESSAGE_ZERO_DIVIDE_BY_ZERO);
        }
    }

    /**
     * Click on comma button processing
     */
    @FXML
    private void buttonCommaClick() {
        if (!getNumericFieldText().contains(COMMA) && !checkSize()) {
            appendNumericFieldText(COMMA);
        }

        if (isEditable || isCalculateResult) {
            isEditable = false;
            isCalculateResult = false;
            setNumericFieldText(ZERO_WITH_COMMA);
        }
    }

    /**
     * Processing keyboard events
     *
     * @param event keyboard event
     */
    @FXML
    private void operatorKeyClick(KeyEvent event) {
        CalculatorButton button = CalculatorButton.searchButtonByEvent(event);
        Parent parent = (Parent) event.getSource();

        if (button != null) {
            clickOnButton((Button) parent.lookup(button.getFXId()));
        }
    }

    /**
     * Processing of click on left arrow in history field
     */
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

    /**
     * Processing of click on right arrow in history field
     */
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

    /**
     * Processing of click on binary operator buttons
     *
     * @param event     binary operator
     */
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

    /**
     * Processing of click on percent button
     */
    @FXML
    private void percentClick() {
        if (!isSequence) {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        } else {
            setNumericFieldNumber(calculator.percent(getNumericFieldNumber()));
            String value = getNumericFieldText().replace(" ", "");

            if (isEditable) {
                history.replace(value);
                setHistoryFieldText(history.getHistory());
            } else {
                history.appendHistory(value);
                setHistoryFieldText(history.getHistory());
            }
        }

        isEditable = true;
        isPreviousUnary = true;
    }

    /**
     * Processing of click on negate button
     */
    @FXML
    private void negateClick() {

        if (isPreviousUnary) {
            history.surround(Operator.NEGATE);
            setHistoryFieldText(history.getHistory());
        }

        setNumericFieldNumber(calculator.negate(getNumericFieldNumber()));
    }

    /**
     * Processing of click on square button
     */
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
        isEditable = true;
    }

    /**
     * Processing of click on square root button
     */
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
        isEditable = true;
    }

    /**
     * Processing of click on backspace button
     */
    @FXML
    private void backspaceClick() {
        if (isError) {
            normalStatement();
        }

        if (isEditable) {
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

    /**
     * Processing of click on inverse button
     */
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
        isEditable = true;
    }

    /**
     * Processing of click on memory clear button
     */
    @FXML
    private void memoryClearClick() {
        disableMemoryButtons(true);
        calculator.memoryClear();
    }

    /**
     * Processing of click on memory recall button
     */
    @FXML
    private void memoryRecallClick() {
        setNumericFieldNumber(calculator.memoryRecall());
        isEditable = false;
    }

    /**
     * Processing of click on memory add button
     */
    @FXML
    private void memoryAddClick() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        calculator.memoryAdd(getNumericFieldNumber());
        isEditable = false;
    }

    /**
     * Processing of click on memory subtract button
     */
    @FXML
    private void memorySubtractClick() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        calculator.memorySubtract(getNumericFieldNumber());
        isEditable = false;
    }

    /**
     * Processing of click on memory store button
     */
    @FXML
    private void memoryStoreClick() {
        if (isMemoryLocked) {
            disableMemoryButtons(false);
        }

        calculator.memoryStore(getNumericFieldNumber());
        isEditable = false;
    }

    /**
     * Processing of click on clear button
     */
    @FXML
    private void clearClick() {
        isEditable = false;
        isCalculateResult = false;
        isPreviousUnary = false;
        isSequence = false;
        normalStatement();
        calculator.clearAll();
    }

    /**
     * Processing of click on clear entry button
     */
    @FXML
    private void clearEntryClick() {
        if (isError) {
            normalStatement();
        } else {
            setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
            calculator.clearEntry();
        }
    }

    /**
     * Processing of click on binary operator button.
     *
     * @param operator      operator
     * @throws OverflowException            throws when scale of result is bigger than MAX_SCALE, defined in {@link CalculationModel}
     * @throws ZeroByZeroDivideException    throws when zero divided by zero
     * @throws ZeroDivideException          throws when not zero number divided by zero
     */
    private void processBinaryOperator(Operator operator) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        String value = getNumericFieldText().replace(" ", "");

        if (isSequence) {
            if (isPreviousUnary) {
                history.appendHistory(operator.getText());
                setHistoryFieldText(history.getHistory());
                setNumericFieldNumber(calculator.calculateIntermediateResult(getNumericFieldNumber()));
                calculator.changeOperator(getNumericFieldNumber(), operator);
            } else if (isEditable) {
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
        isEditable = true;
        isCalculateResult = false;
        isPreviousUnary = false;
    }

    /**
     * Return current statement of numeric field
     *
     * @return  numeric field value
     */
    private String getNumericFieldText() {
        return numericField.getText();
    }

    /**
     * Write given value to numeric field
     *
     * @param value     given value
     */
    private void setNumericFieldText(String value) {
        if (!isError) {
            value = NumericFormatter.display(value);
        }

        numericField.setText(value);
    }

    /**
     * Get current statement of numeric field and convert that into {@link BigDecimal}
     *
     * @return  number from numeric field
     */
    private BigDecimal getNumericFieldNumber() {
        String value = getNumericFieldText().replace(" ", "").replace(COMMA, DOT);
        return new BigDecimal(value);
    }

    /**
     * Write given number into numeric field
     *
     * @param number    given number
     */
    private void setNumericFieldNumber(BigDecimal number) {
        numericField.setText(NumericFormatter.display(number));
    }

    /**
     * Write given value into history field.
     *
     * @param value     given history value
     */
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

    /**
     * Add given value to current history and save that in history field
     *
     * @param value     given history value
     */
    private void appendNumericFieldText(String value) {

        setNumericFieldText(getNumericFieldText() + value);
    }

    /**
     * Simulate pressing on given button for key events click
     *
     * @param button    given button
     */
    private void clickOnButton(Button button) {
        button.arm();
        button.fire();
        PauseTransition pause = new PauseTransition(Duration.seconds(DURATION));
        pause.setOnFinished(e -> button.disarm());
        pause.play();
    }

    /**
     * Change statement of buttons in list at entrance and exit from error statement
     *
     * @param disable   true if buttons must be disabled, else otherwise
     */
    private void disableButtons(boolean disable) {
        isError = disable;
        for (Button item : disabled) {
            item.setDisable(disable);
        }
    }

    /**
     * Change statement of buttons in list when cleaning and writing to memory
     *
     * @param disable   true if buttons must be disabled, else otherwise
     */
    private void disableMemoryButtons(boolean disable) {
        isMemoryLocked = disable;
        for (Button item : disabledMemory) {
            item.setDisable(disable);
        }
    }

    /**
     * Return maximum count of chars which can be placed in history label
     *
     * @return      count of chars
     */
    private int getLabelSize() {
        return (int) (historyField.getWidth() / CHAR_WIDTH);
    }

    /**
     * Check numeric field length
     *
     * @return      true if length of numeric field is bigger than max length, false otherwise
     */
    private boolean checkSize() {
        String str = getNumericFieldText().replace(COMMA, "").replace("-", "").replace(" ", "");
        if (str.startsWith("0")) {
            return str.length() >= NUMERIC_FIELD_SIZE + 1;
        } else {
            return str.length() >= NUMERIC_FIELD_SIZE;
        }
    }

    /**
     * Return normal statement for calculator
     */
    private void normalStatement() {
        disableButtons(false);
        disableMemoryButtons(isMemoryLocked);
        calculator.resetOperator();
        history.clearHistory();
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        setHistoryFieldText(DEFAULT_HISTORY_FIELD_VALUE);
    }

    /**
     * Set error statement for calculator with given error message in numeric field
     *
     * @param error     error message
     */
    private void errorStatement(String error) {
        disableButtons(true);
        setNumericFieldText(error);
    }
}