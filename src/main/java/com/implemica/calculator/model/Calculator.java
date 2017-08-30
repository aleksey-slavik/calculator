package com.implemica.calculator.model;

import com.implemica.calculator.model.exception.OverflowException;
import com.implemica.calculator.model.exception.SquareRootException;
import com.implemica.calculator.model.exception.ZeroByZeroDivideException;
import com.implemica.calculator.model.exception.ZeroDivideException;
import com.implemica.calculator.model.util.CalculationModel;
import com.implemica.calculator.model.util.MemoryModel;
import com.implemica.calculator.model.util.Operator;

import java.math.BigDecimal;

/**
 * Calculator model.
 * Consist methods of CalculationModel and MemoryModel classes
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
     * Change current operator and save given value as the left operand.
     * Usually called when binary operator are pressed.
     *
     * @param value     given value
     * @param operator  given operator
     */
    public void changeOperator(BigDecimal value, Operator operator) {
        calculation.changeOperator(value, operator);
    }

    /**
     * Return current statement of operator
     *
     * @return  current operator
     */
    public Operator getOperator() {
        return calculation.getOperator();
    }

    /**
     * Change operator to default
     */
    public void resetOperator() {
        calculation.resetOperator();
    }

    /**
     * Calculate current expression using current left operand and operator.
     * Right operand used if current operation was not already done
     *
     * @param value     given value
     * @return          result of calculation
     * @throws OverflowException            throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException    throws when zero divided by zero
     * @throws ZeroDivideException          throws when not zero number divided by zero
     */
    public BigDecimal calculateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        return calculation.calculateResult(value);
    }

    /**
     * Calculate current expression using current left operand, given value as right operand, and operator.
     *
     * @param value     given value
     * @return          result of calculation
     * @throws OverflowException            throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException    throws when zero divided by zero
     * @throws ZeroDivideException          throws when not zero number divided by zero
     */
    public BigDecimal calculateIntermediateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        return calculation.calculateIntermediateResult(value);
    }

    /**
     * Calculate current expression using given value as left operand, right operand and operator.
     *
     * @param value     given value
     * @return          result of calculation
     * @throws OverflowException            throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException    throws when zero divided by zero
     * @throws ZeroDivideException          throws when not zero number divided by zero
     */
    public BigDecimal calculateEqualsResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        return calculation.calculateEqualsResult(value);
    }

    /**
     * Return given percent value of left operand
     *
     * @param value     given percent value
     * @return          percent value of left operand
     */
    public BigDecimal percent(BigDecimal value) {
        return calculation.percent(value);
    }

    /**
     * Return negate number for given value.
     *
     * @param value     given value
     * @return          negate of given value
     */
    public BigDecimal negate(BigDecimal value) {
        return calculation.negate(value);
    }

    /**
     * Return inverse number for given value
     *
     * @param value     given value
     * @return          inverse number
     * @throws ZeroDivideException      throws when given number equals zero
     */
    public BigDecimal inverse(BigDecimal value) throws ZeroDivideException {
        return calculation.inverse(value);
    }

    /**
     * Return result of squaring operation for given value
     *
     * @param value     given value
     * @return          square of number
     * @throws OverflowException    throws when scale of result is bigger than MAX_SCALE
     */
    public BigDecimal sqr(BigDecimal value) throws OverflowException {
        return calculation.sqr(value);
    }

    /**
     * Return square root for given value
     *
     * @param value     given value
     * @return          square root of number
     * @throws SquareRootException      throws when given number is negative
     */
    public BigDecimal sqrt(BigDecimal value) throws SquareRootException {
        return calculation.sqrt(value);
    }

    /**
     * Return default statement of CalculationModel object
     */
    public void clearAll() {
        calculation.clearAll();
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
     * @param value     given value
     */
    public void memoryStore(BigDecimal value) {
        memory.memoryStore(value);
    }

    /**
     * Add given value to current memory value
     *
     * @param value     given value
     */
    public void memoryAdd(BigDecimal value) {
        memory.memoryAdd(value);
    }

    /**
     * Subtract given value to current memory value
     *
     * @param value     given value
     */
    public void memorySubtract(BigDecimal value) {
        memory.memorySubtract(value);
    }

    public BigDecimal memoryRecall() {
        return memory.memoryRecall();
    }
}
