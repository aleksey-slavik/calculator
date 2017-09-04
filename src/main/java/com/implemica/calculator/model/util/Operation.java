package com.implemica.calculator.model.util;

import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.enums.UnaryOperator;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Slavik Aleksey V.
 */
public class Operation {

    private BinaryOperator binaryOperator;
    private ArrayList<UnaryOperator> unaryOperators;
    private BigDecimal operand = BigDecimal.ZERO;

    public void setBinaryOperator(BinaryOperator binaryOperator) {
        this.binaryOperator = binaryOperator;
    }

    public BinaryOperator getBinaryOperator() {
        return binaryOperator;
    }

    public void addUnaryOperator(UnaryOperator unaryOperator) {
        unaryOperators.add(unaryOperator);
    }

    public ArrayList<UnaryOperator> getUnaryOperators() {
        return unaryOperators;
    }

    public void setOperand(BigDecimal operand) {
        this.operand = operand;
    }

    public BigDecimal getOperand() {
        return operand;
    }

    public void clearUnaryOperators() {
        unaryOperators.clear();
    }
}
