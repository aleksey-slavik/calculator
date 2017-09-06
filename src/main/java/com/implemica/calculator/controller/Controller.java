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
import java.util.HashMap;
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

    /**
     * One-to-one correspondence between {@link CalculatorButton) and {@link BinaryOperator}
     */
    private HashMap<CalculatorButton, BinaryOperator> binaryGroup = new HashMap<>();

    /**
     * One-to-one correspondence between {@link CalculatorButton) and {@link UnaryOperator}
     */
    private HashMap<CalculatorButton, UnaryOperator> unaryGroup = new HashMap<>();

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

        //add binary buttons to map
        binaryGroup.put(CalculatorButton.ADD, BinaryOperator.ADD);
        binaryGroup.put(CalculatorButton.SUBTRACT, BinaryOperator.SUBTRACT);
        binaryGroup.put(CalculatorButton.DIVIDE, BinaryOperator.DIVIDE);
        binaryGroup.put(CalculatorButton.MULTIPLY, BinaryOperator.MULTIPLY);

        //add unary buttons to map
        unaryGroup.put(CalculatorButton.NEGATE, UnaryOperator.NEGATE);
        unaryGroup.put(CalculatorButton.INVERSE, UnaryOperator.INVERSE);
        unaryGroup.put(CalculatorButton.SQR, UnaryOperator.SQR);
        unaryGroup.put(CalculatorButton.SQRT, UnaryOperator.SQRT);

        //setup default statement of calculator
        disableMemoryButtons(true);
        setNumericFieldText(DEFAULT_NUMERIC_FIELD_VALUE);
        updateHistoryField();

        //add resize history field listener
        historyField.widthProperty().addListener(observable -> {
            int label = getLabelSize();
            String subHistory = history.substring(0, historyPos);

            if (label > history.length()) {
                subHistory = history;
            } else {
                if (label < subHistory.length()) {
                    left.setVisible(true);
                } else {
                    int newPos = historyPos - label;
                    newPos = newPos < 0 ? 0 : newPos;
                    subHistory = history.substring(newPos, historyPos);
                    left.setVisible(false);
                }
            }

            historyField.setText(subHistory);
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
            BigDecimal res = getNumericFieldNumber();

            if (isCalculateResult) {
                res = calculator.calculateIntermediateResult(res);
            } else {
                res = calculator.calculateResult(res);
                calculator.clearHistory();
                updateHistoryField();
            }

            clearInput();
            setNumericFieldNumber(res);
            isCalculateResult = true;
            isSequence = false;
            isEditable = false;
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
        int size = getLabelSize();
        historyPos -= size;

        if (historyPos - size < 0) {
            historyPos = size;
            left.setVisible(false);
        }

        right.setVisible(true);
        historyField.setText(history.substring(historyPos - size, historyPos));
    }

    /**
     * Processing of click on right arrow in history field
     */
    @FXML
    private void rightClick() {
        int size = getLabelSize();
        int historyLength = history.length();
        historyPos += size;

        if (historyPos > historyLength) {
            historyPos = historyLength;
            right.setVisible(false);
        }

        int delta = historyPos - size;
        boolean isPositiveDelta = delta >= 0;
        int start = 0;

        if (isPositiveDelta) {
            start = delta;
        }

        left.setVisible(isPositiveDelta);
        historyField.setText(history.substring(start, historyPos));
    }

    /**
     * Processing of click on binary operator buttons
     *
     * @param event binary operator
     */
    @FXML
    private void binaryOperatorClick(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();
        CalculatorButton button = CalculatorButton.searchButtonById(id);

        try {
            processBinaryOperator(binaryGroup.get(button));
        } catch (ZeroByZeroDivideException e) {
            errorStatement(MESSAGE_ZERO_DIVIDE_BY_ZERO);
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        }
    }

    /**
     * Processing of click on unary operator buttons
     *
     * @param event unary operator
     */
    @FXML
    private void unaryOperatorClick(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();
        CalculatorButton button = CalculatorButton.searchButtonById(id);

        try {
            processUnaryOperator(unaryGroup.get(button));
        } catch (OverflowException e) {
            errorStatement(MESSAGE_OVERFLOW);
        } catch (ZeroDivideException e) {
            errorStatement(MESSAGE_DIVIDE_BY_ZERO);
        } catch (SquareRootException e) {
            errorStatement(MESSAGE_INVALID_INPUT);
        }
    }

    /**
     * Processing of click on memory operator buttons
     *
     * @param event memory operator
     */
    @FXML
    private void memoryOperatorClick(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();
        CalculatorButton button = CalculatorButton.searchButtonById(id);
        isMemoryLocked = false;

        if (button == CalculatorButton.MC) {
            calculator.memoryClear();
            isMemoryLocked = true;
        } else if (button == CalculatorButton.MR) {
            setNumericFieldNumber(calculator.memoryRecall());
        } else if (button == CalculatorButton.MS) {
            calculator.memoryStore(getNumericFieldNumber());
        } else if (button == CalculatorButton.M_PLUS) {
            calculator.memoryAdd(getNumericFieldNumber());
        } else if (button == CalculatorButton.M_MINUS) {
            calculator.memorySubtract(getNumericFieldNumber());
        }

        isEditable = false;
        disableMemoryButtons(isMemoryLocked);
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
            clearInput();
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
        BigDecimal operand = getNumericFieldNumber();
        Operation operation = new Operation();
        operation.setOperand(operand);
        operation.setBinaryOperator(operator);

        if (isPreviousUnary || isEditable) {
            calculator.changeBinary(operator);
        } else {
            calculator.appendOperation(operation);
            if (isSequence) {
                setNumericFieldNumber(calculator.calculateResult(operand));
            }
        }

        calculator.changeOperator(operand, operator);
        updateHistoryField();
        isSequence = true;
        isEditable = true;
        isCalculateResult = false;
        isPreviousUnary = false;
    }

    /**
     * Processing of click on binary operator button.
     *
     * @param operator operator
     * @throws OverflowException   throws when scale of result is bigger than MAX_SCALE, defined in {@link CalculationModel}
     * @throws ZeroDivideException throws when not zero number divided by zero
     * @throws SquareRootException throws when during square root operation of negative number
     */
    private void processUnaryOperator(UnaryOperator operator) throws OverflowException, ZeroDivideException, SquareRootException {
        BigDecimal operand = getNumericFieldNumber();

        if (!isPreviousUnary) {
            Operation operation = new Operation();
            operation.setOperand(operand);
            calculator.appendOperation(operation);
        }

        calculator.appendUnary(operator);
        updateHistoryField();
        setNumericFieldNumber(calculator.calculateUnary(operator, operand));
        isPreviousUnary = true;
        isEditable = true;
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
        int size = getLabelSize();
        history = parseHistory(calculator);
        int length = history.length();
        String subHistory = history;
        boolean checkLength = length > size;

        if (checkLength) {
            subHistory = history.substring(length - size + 1);
        }

        left.setVisible(checkLength);
        right.setVisible(false);
        historyPos = length;
        historyField.setText(subHistory);
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