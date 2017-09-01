package com.implemica.calculator.model.util;

import java.util.ArrayList;

/**
 * Call history operations.
 *
 * @author Slavik Aleksey V.
 */
public class HistoryModel {

    private static ArrayList<Operator> history = new ArrayList<>();

    public static void append(Operator operator) {
        history.add(operator);
    }

    public static void replaceLast(Operator operator) {
        history.remove(history.size() - 1);
        history.add(operator);
    }

    public static void clear() {
        history.clear();
    }

    public static void main(String[] args) {
        append(Operator.DIVIDE);
        append(Operator.MULTIPLY);
        for (Operator operator: history) {
            System.out.println(operator.getText());
        }
        replaceLast(Operator.ADD);
        for (Operator operator: history) {
            System.out.println(operator.getText());
        }
        clear();
        for (Operator operator: history) {
            System.out.println(operator.getText());
        }
    }
}
