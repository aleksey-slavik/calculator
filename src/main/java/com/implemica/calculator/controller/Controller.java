package com.implemica.calculator.controller;

import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.enums.UnaryOperator;
import com.implemica.calculator.model.util.CalculationModel;
import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.exception.OverflowException;
import com.implemica.calculator.model.exception.SquareRootException;
import com.implemica.calculator.model.exception.ZeroByZeroDivideException;
import com.implemica.calculator.model.exception.ZeroDivideException;
import com.implemica.calculator.model.util.Operation;
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

import static com.implemica.calculator.controller.util.InputNumber.*;
import static com.implemica.calculator.controller.util.NumericFormatter.*;
import static com.implemica.calculator.controller.util.HistoryFormatter.*;

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
     * Pause duration when pressing buttons
     */
    private static final double DURATION = 0.1;

    /**
     * Width of character
     */
    private static final double CHAR_WIDTH = 7.5;

    /**
     * Decimal separator for numbers in numeric field
     */
    private static final String COMMA = ",";

    /**
     * String representation of history, which show in history field
     */
    private String history = DEFAULT_HISTORY_FIELD_VALUE;

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
     * {@link Calculator} object which consist set of methds of calculation methods
     */
    private Calculator calculator = new Calculator();

    /**
     * Numeric and history fields
     */
    @FXML
    private Label numericField, historyField;

    /**
     * Calculator buttons
     */
    @FXML
    private Button negate, comma, add, subtract, multiply, divide, inverse, sqr, sqrt, percent,
            memory_clear, memory_recall, memory_add, memory_minus, memory_store, left, right;

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
        updateHistoryField();

        //add resize history field listener
        historyField.widthProperty().addListener(observable -> {
            if (getLabelSize() > history.length()) {
                historyField.setText(history);
            } else {
                if (getLabelSize() < history.substring(0, historyPos).length()) {
                    historyField.setText(history.substring(0, historyPos));
                    left.setVisible(true);
                } else {
                    int newPos = historyPos - getLabelSize();
                    newPos = newPos < 0 ? 0 : newPos;
                    historyField.setText(history.substring(newPos, historyPos));
                    left.setVisible(false);
                }
            }
        });
    }

    /**
     * Click on digits buttons processing
     *
     * @param event digits event
     */
    @FXML
    private void buttonDigitClick(ActionEvent event) {
        if (isError) {
            normalStatement();
        }

        if (isEditable) {
            clearInput();
            isEditable = false;
        }

        String digit = ((Button) event.getSource()).getText();
        appendDigit(digit);
        numericField.setText(formatInput());
    }

    /**
     * Click on equals button processing
     */
    @FXML
    private void buttonEqualsClick() {
        if (isError) {
            normalStatement();
        }

        if (calculator.getOperator() == null) {
            calculator.clearHistory();
            updateHistoryField();
            isPreviousUnary = false;
            return;
        }

        try {
            if (isCalculateResult) {
                setNumericFieldNumber(calculator.calculateIntermediateResult(getNumericFieldNumber()));
            } else {
                setNumericFieldNumber(calculator.calculateResult(getNumericFieldNumber()));
                calculator.clearHistory();
                updateHistoryField();
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
        String number = formatInput();

        if (!isInputPointSet() && canInput()) {
            addPointToInput();
            number += COMMA;
        }

        numericField.setText(number);
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
        historyField.setText(history.substring(historyPos - getLabelSize(), historyPos));
    }

    /**
     * Processing of click on right arrow in history field
     */
    @FXML
    private void rightClick() {
        historyPos += getLabelSize();

        if (historyPos > history.length()) {
            historyPos = history.length();
            right.setVisible(false);
        }

        int delta = historyPos - getLabelSize();

        if (delta < 0) {
            left.setVisible(false);
            historyField.setText(history.substring(0, historyPos));
        } else {
            left.setVisible(true);
            historyField.setText(history.substring(delta, historyPos));
        }
    }

    /**
     * Processing of click on binary operator buttons
     *
     * @param event binary operator
     */
    @FXML
    private void binaryOperatorClick(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();

        try {
            if (id.equals(CalculatorButton.ADD.getId())) {
                processBinaryOperator(BinaryOperator.ADD);
            } else if (id.equals(CalculatorButton.SUBTRACT.getId())) {
                processBinaryOperator(BinaryOperator.SUBTRACT);
            } else if (id.equals(CalculatorButton.DIVIDE.getId())) {
                processBinaryOperator(BinaryOperator.DIVIDE);
            } else if (id.equals(CalculatorButton.MULTIPLY.getId())) {
                processBinaryOperator(BinaryOperator.MULTIPLY);
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
            Operation operation = new Operation();
            operation.setOperand(calculator.percent(getNumericFieldNumber()));
            setNumericFieldNumber(operation.getOperand());

            if (isEditable) {
                calculator.changeLast(getNumericFieldNumber());
            } else {
                calculator.appendOperation(operation);
            }

            updateHistoryField();
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
            calculator.appendUnary(UnaryOperator.NEGATE);
            updateHistoryField();
        }

        setNumericFieldNumber(calculator.negate(getNumericFieldNumber()));
    }

    /**
     * Processing of click on square button
     */
    @FXML
    private void sqrClick() {
        if (!isPreviousUnary) {
            Operation operation = new Operation();
            operation.setOperand(getNumericFieldNumber());
            calculator.appendOperation(operation);
        }

        calculator.appendUnary(UnaryOperator.SQR);
        updateHistoryField();

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
        if (!isPreviousUnary) {
            Operation operation = new Operation();
            operation.setOperand(getNumericFieldNumber());
            calculator.appendOperation(operation);
        }

        calculator.appendUnary(UnaryOperator.SQRT);
        updateHistoryField();

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

        backspaceInput();
        String number = formatInput();
        if (getInputScale() == 0 && isInputPointSet()) {
            number += COMMA;
        }

        numericField.setText(number);
    }

    /**
     * Processing of click on inverse button
     */
    @FXML
    private void inverseClick() {
        if (!isPreviousUnary) {
            Operation operation = new Operation();
            operation.setOperand(getNumericFieldNumber());
            calculator.appendOperation(operation);
        }

        calculator.appendUnary(UnaryOperator.INVERSE);
        updateHistoryField();

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
        clearInput();
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
     * @param operator operator
     * @throws OverflowException         throws when scale of result is bigger than MAX_SCALE, defined in {@link CalculationModel}
     * @throws ZeroByZeroDivideException throws when zero divided by zero
     * @throws ZeroDivideException       throws when not zero number divided by zero
     */
    private void processBinaryOperator(BinaryOperator operator) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        Operation operation = new Operation();
        operation.setOperand(getNumericFieldNumber());
        operation.setBinaryOperator(operator);

        if (isSequence) {
            if (isPreviousUnary) {
                calculator.changeBinary(operator);
                setNumericFieldNumber(calculator.calculateResult(getNumericFieldNumber()));
            } else if (isEditable) {
                calculator.changeBinary(operator);
            } else {
                calculator.appendOperation(operation);
                setNumericFieldNumber(calculator.calculateResult(getNumericFieldNumber()));
            }
        } else {
            if (isPreviousUnary) {
                calculator.changeBinary(operator);
            } else {
                calculator.appendOperation(operation);
            }
        }

        calculator.changeOperator(getNumericFieldNumber(), operator);
        updateHistoryField();
        isSequence = true;
        isEditable = true;
        isCalculateResult = false;
        isPreviousUnary = false;
    }

    /**
     * Return current statement of numeric field
     *
     * @return numeric field value
     */
    private String getNumericFieldText() {
        return numericField.getText();
    }

    /**
     * Write given value to numeric field
     *
     * @param value given value
     */
    private void setNumericFieldText(String value) {
        numericField.setText(value);
    }

    /**
     * Get current statement of numeric field and convert that into {@link BigDecimal}
     *
     * @return number from numeric field
     */
    private BigDecimal getNumericFieldNumber() {
        return parseInput(getNumericFieldText());
    }

    /**
     * Write given number into numeric field
     *
     * @param number given number
     */
    private void setNumericFieldNumber(BigDecimal number) {
        numericField.setText(display(number));
    }

    /**
     * Write given value into history field.
     */
    private void updateHistoryField() {
        history = parseHistory(calculator);

        if (history.length() > getLabelSize()) {
            left.setVisible(true);
            right.setVisible(false);
            historyField.setText(history.substring(history.length() - getLabelSize() + 1));
        } else {
            left.setVisible(false);
            right.setVisible(false);
            historyField.setText(history);
        }

        historyPos = history.length();
    }

    /**
     * Simulate pressing on given button for key events click
     *
     * @param button given button
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
     * @param disable true if buttons must be disabled, else otherwise
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
     * @param disable true if buttons must be disabled, else otherwise
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
     * @return count of chars
     */
    private int getLabelSize() {
        return (int) (historyField.getWidth() / CHAR_WIDTH);
    }

    /**
     * Return normal statement for calculator
     */
    private void normalStatement() {
        disableButtons(false);
        disableMemoryButtons(isMemoryLocked);
        calculator.resetOperator();
        calculator.clearHistory();
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        updateHistoryField();
    }

    /**
     * Set error statement for calculator with given error message in numeric field
     *
     * @param error error message
     */
    private void errorStatement(String error) {
        disableButtons(true);
        setNumericFieldText(error);
    }
}