package com.implemica.calculator.model.util;

import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.enums.UnaryOperator;

import java.util.ArrayList;

/**
 * Call history operations.
 *
 * @author Slavik Aleksey V.
 */
public class HistoryModel {

    private ArrayList<Operation> history = new ArrayList<>();

    public void append(Operation operation) {
        history.add(operation);
    }

    public void changeLast(Operation operation) {
        getLastOperation();
        history.add(operation);
    }

    public void changeBinary(BinaryOperator operator) {
        getLastOperation().setBinaryOperator(operator);
    }

    public void appendUnary(UnaryOperator operator) {
        getLastOperation().addUnaryOperator(operator);
    }

    public void clear() {
        history.clear();
    }

    public ArrayList<Operation> getHistory() {
        return history;
    }

    private Operation getLastOperation() {
        return history.get(history.size() - 1);
    }
}
