package com.implemica.calculator.model;

import com.implemica.calculator.model.enums.UnaryOperator;
import com.implemica.calculator.model.exception.OverflowException;
import com.implemica.calculator.model.exception.NegativeSquareRootException;
import com.implemica.calculator.model.exception.ZeroByZeroDivideException;
import com.implemica.calculator.model.exception.DivideByZeroException;
import com.implemica.calculator.model.util.CalculationModel;
import com.implemica.calculator.model.util.HistoryModel;
import com.implemica.calculator.model.util.MemoryModel;
import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.util.Operation;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Calculator model.
 * Consist methods of CalculationModel, MemoryModel and HistoryModel classes
 *
 * @author Slavik Aleksey V.
 */
public class Calculator {

    /**
     * {@link CalculationModel} object
     */
    private CalculationModel calculation = new CalculationModel();

    /**
     * {@link MemoryModel} object
     */
    private MemoryModel memory = new MemoryModel();

    /**
     * {@link HistoryModel} object
     */
    private HistoryModel history = new HistoryModel();

    /**
     * Change current operator and save given value as the left operand.
     * Usually called when binary operator are pressed.
     *
     * @param value    given value
     * @param operator given operator
     */
    public void changeOperator(BigDecimal value, BinaryOperator operator) {
        calculation.changeOperator(value, operator);
    }

    /**
     * Return current statement of operator
     *
     * @return current operator
     */
    public BinaryOperator getOperator() {
        return calculation.getOperator();
    }

    /**
     * Change operator to default
     */
    public void resetOperator() {
        calculation.resetOperator();
    }

    /**
     * Calculate current expression using given value as left operand, right operand and operator.
     *
     * @param value given value
     * @return result of calculation
     * @throws OverflowException         throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException throws when zero divided by zero
     * @throws DivideByZeroException       throws when not zero number divided by zero
     */
    public BigDecimal calculateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, DivideByZeroException {
        return calculation.calculateResult(value);
    }

    /**
     * Calculate current expression using current left operand, given value as right operand, and operator.
     *
     * @param value given value
     * @return result of calculation
     * @throws OverflowException         throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException throws when zero divided by zero
     * @throws DivideByZeroException       throws when not zero number divided by zero
     */
    public BigDecimal calculateIntermediateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, DivideByZeroException {
        return calculation.calculateIntermediateResult(value);
    }

    /**
     * Return result of given unary operation for given value
     *
     * @param operator given operator
     * @param value    given value
     * @return result of given unary operation for given value
     * @throws OverflowException   throws when scale of result is bigger than MAX_SCALE
     * @throws NegativeSquareRootException throws when during square root operation of negative number
     * @throws DivideByZeroException throws when not zero number divided by zero
     */
    public BigDecimal calculateUnary(UnaryOperator operator, BigDecimal value) throws OverflowException, NegativeSquareRootException, DivideByZeroException {
        return calculation.calculateUnary(operator, value);
    }

    /**
     * Return given percent value of left operand
     *
     * @param value given percent value
     * @return percent value of left operand
     */
    public BigDecimal percent(BigDecimal value) {
        return calculation.percent(value);
    }

    /**
     * Return default statement of CalculationModel object
     */
    public void clearAll() {
        calculation.clearAll();
        clearHistory();
    }

    /**
     * Set right operand to default
     */
    public void clearEntry() {
        calculation.clearEntry();
    }

    /**
     * Set memory value to default
     */
    public void memoryClear() {
        memory.memoryClear();
    }

    /**
     * Save given value in memory
     *
     * @param value given value
     */
    public void memoryStore(BigDecimal value) {
        memory.memoryStore(value);
    }

    /**
     * Add given value to current memory value
     *
     * @param value given value
     */
    public void memoryAdd(BigDecimal value) {
        memory.memoryAdd(value);
    }

    /**
     * Subtract given value to current memory value
     *
     * @param value given value
     */
    public void memorySubtract(BigDecimal value) {
        memory.memorySubtract(value);
    }

    /**
     * Return current memory value
     *
     * @return current memory value
     */
    public BigDecimal memoryRecall() {
        return memory.memoryRecall();
    }

    /**
     * Append given {@link Operation} object to history
     *
     * @param operator given operation
     */
    public void appendOperation(Operation operator) {
        history.append(operator);
    }

    /**
     * Replace last operation by given number.
     * Using for percent operation, for example
     *
     * @param number given number
     */
    public void changeLast(BigDecimal number) {
        history.changeLast(number);
    }

    /**
     * Replace binary operator in last operation by given operator.
     * Using for binary operations, for example.
     *
     * @param operator given operator
     */
    public void changeBinary(BinaryOperator operator) {
        history.changeBinary(operator);
    }

    /**
     * Append given unary operator to current operation.
     *
     * @param operator given operator
     */
    public void appendUnary(UnaryOperator operator) {
        history.appendUnary(operator);
    }

    /**
     * Clear history
     */
    public void clearHistory() {
        history.clear();
    }

    /**
     * Remove last operation in sequence
     */
    public void removeLastOperation() {
        history.removeLastOperation();
    }

    /**
     * Return current statement of history
     *
     * @return current statement of history
     */
    public ArrayList<Operation> getHistory() {
        return history.getHistory();
    }
}
