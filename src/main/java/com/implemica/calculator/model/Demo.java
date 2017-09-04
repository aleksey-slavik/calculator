package com.implemica.calculator.model;

import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.exception.OverflowException;
import com.implemica.calculator.model.exception.SquareRootException;
import com.implemica.calculator.model.exception.ZeroByZeroDivideException;
import com.implemica.calculator.model.exception.ZeroDivideException;

import java.math.BigDecimal;

/**
 * Demo of directly using model
 *
 * @author Slavik Aleksey V.
 */
public class Demo {

    /**
     * Simple example of using model.
     * 2 + 2 =
     *
     * @return result of calculation
     */
    private static BigDecimal simpleExample() {
        Calculator calculator = new Calculator();
        BigDecimal res = null;
        BigDecimal two = new BigDecimal(2);

        try {
            calculator.changeOperator(two, BinaryOperator.ADD);
            res = calculator.calculateResult(two);
        } catch (OverflowException | ZeroByZeroDivideException | ZeroDivideException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Complex example of using model.
     * 7 - 10 = M+ / 3 = M+ M+ 3 SQRT + MR =
     *
     * @return result of calculation
     */
    private static BigDecimal complexExample() {
        Calculator calculator = new Calculator();
        BigDecimal res = null;
        BigDecimal seven = new BigDecimal(7);
        BigDecimal ten = new BigDecimal(10);
        BigDecimal three = new BigDecimal(3);

        try {
            BigDecimal tmp = seven;
            calculator.changeOperator(tmp, BinaryOperator.SUBTRACT); // 7 -
            tmp = calculator.calculateResult(ten); // 10 =
            calculator.memoryAdd(tmp); // M+
            calculator.changeOperator(tmp, BinaryOperator.DIVIDE); // tmp /
            tmp = calculator.calculateResult(three); // 3 =
            calculator.memoryAdd(tmp); // M+
            calculator.memoryAdd(tmp); // M+
            tmp = calculator.sqrt(three); // SQRT 3
            calculator.changeOperator(tmp, BinaryOperator.ADD); // tmp +
            tmp = calculator.memoryRecall(); // MR
            res = calculator.calculateResult(tmp); // tmp =
        } catch (OverflowException | ZeroByZeroDivideException | ZeroDivideException | SquareRootException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println("2 + 2 = " + simpleExample());
        System.out.println("7 - 10 = M+ / 3 = M+ M+ 3 SQRT + MR = " + complexExample());
    }
}
