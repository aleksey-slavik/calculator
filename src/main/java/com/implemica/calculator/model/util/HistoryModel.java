package com.implemica.calculator.model.util;

import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.enums.UnaryOperator;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Calculator history model.
 * Consist main history operations.
 *
 * @author Slavik Aleksey V.
 */
public class HistoryModel {

    /**
     * Call sequence
     */
    private ArrayList<Operation> history = new ArrayList<>();

    /**
     * Append to current sequence new operation
     *
     * @param operation new operation
     */
    public void append(Operation operation) {
        history.add(operation);
    }

    /**
     * Replace last operation by given number.
     * Using for percent operation, for example
     *
     * @param operand given number
     */
    public void changeLast(BigDecimal operand) {
        removeLastOperation();
        append(new Operation());
        changeOperand(operand);
    }

    /**
     * Replace binary operator in last operation by given operator.
     * Using for binary operations, for example.
     *
     * @param operator given operator
     */
    public void changeBinary(BinaryOperator operator) {
        getLastOperation().setBinaryOperator(operator);
    }

    /**
     * Append given unary operator to current operation.
     *
     * @param operator given operator
     */
    public void appendUnary(UnaryOperator operator) {
        getLastOperation().addUnaryOperator(operator);
    }

    /**
     * Clear history
     */
    public void clear() {
        history.clear();
    }

    /**
     * Return current statement of history
     *
     * @return current statement of history
     */
    public ArrayList<Operation> getHistory() {
        return history;
    }

    /**
     * Return last operation in sequence
     *
     * @return last operation
     */
    private Operation getLastOperation() {
        return history.get(history.size() - 1);
    }

    /**
     * Remove last operation in sequence
     */
    public void removeLastOperation() {
        history.remove(history.size() - 1);
    }

    /**
     * Replace current operand in operation by given operand
     *
     * @param operand given operand
     */
    private void changeOperand(BigDecimal operand) {
        getLastOperation().setOperand(operand);
    }
}
