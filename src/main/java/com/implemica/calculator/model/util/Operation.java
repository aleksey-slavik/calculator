package com.implemica.calculator.model.util;

import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.enums.UnaryOperator;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Simple element of sequence of operations in calculator.
 * Consist operand, lot of unary operator for operand and binary operator.
 *
 * @author Slavik Aleksey V.
 */
public class Operation {

    /**
     * Binary operator
     */
    private BinaryOperator binaryOperator;

    /**
     * List of unary operators
     */
    private ArrayList<UnaryOperator> unaryOperators = new ArrayList<>();

    /**
     * Operand
     */
    private BigDecimal operand = BigDecimal.ZERO;

    /**
     * Setup binary operator
     *
     * @param binaryOperator new binary operator
     */
    public void setBinaryOperator(BinaryOperator binaryOperator) {
        this.binaryOperator = binaryOperator;
    }

    /**
     * Return current binary operator
     *
     * @return binary operator
     */
    public BinaryOperator getBinaryOperator() {
        return binaryOperator;
    }

    /**
     * Append given unary operator to list of operators
     *
     * @param unaryOperator given unary operators
     */
    public void addUnaryOperator(UnaryOperator unaryOperator) {
        unaryOperators.add(unaryOperator);
    }

    /**
     * Return current list of unary operators
     *
     * @return list of unary operators
     */
    public ArrayList<UnaryOperator> getUnaryOperators() {
        return unaryOperators;
    }

    /**
     * Setup operand
     *
     * @param operand new operand
     */
    public void setOperand(BigDecimal operand) {
        this.operand = operand;
    }

    /**
     * Return current operand
     *
     * @return operand
     */
    public BigDecimal getOperand() {
        return operand;
    }
}
