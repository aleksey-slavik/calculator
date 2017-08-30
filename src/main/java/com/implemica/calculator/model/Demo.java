package com.implemica.calculator.model;

import com.implemica.calculator.model.util.Operator;
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
     * @return      result of calculation
     */
    private static BigDecimal simpleExample() {
        Calculator calculator = new Calculator();
        BigDecimal res = null;

        try {
            BigDecimal tmp = BigDecimal.valueOf(2);
            calculator.changeOperator(tmp, Operator.ADD);
            res = calculator.calculateResult(BigDecimal.valueOf(2));
        } catch (OverflowException | ZeroByZeroDivideException | ZeroDivideException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Complex example of using model.
     * 7 - 10 MS 3 SQRT + MR =
     *
     * @return      result of calculation
     */
    private static BigDecimal complexExample() {
        Calculator calculator = new Calculator();
        BigDecimal res = null;

        try {
            BigDecimal tmp = BigDecimal.valueOf(7);
            calculator.changeOperator(tmp, Operator.SUBTRACT);
            calculator.memoryStore(BigDecimal.valueOf(10));
            tmp = calculator.sqrt(BigDecimal.valueOf(3));
            tmp = calculator.calculateIntermediateResult(tmp);
            calculator.changeOperator(tmp, Operator.ADD);
            tmp = calculator.memoryRecall();
            res = calculator.calculateResult(tmp);
        } catch (OverflowException | ZeroByZeroDivideException | ZeroDivideException | SquareRootException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println("2 + 2 = " + simpleExample());
        System.out.println("7 - 10 MS 3 SQRT + MR = " + complexExample());
    }
}
