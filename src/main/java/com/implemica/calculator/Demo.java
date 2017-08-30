package com.implemica.calculator;

import com.implemica.calculator.model.Calculator;
import com.implemica.calculator.model.Memory;
import com.implemica.calculator.model.Operator;
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
        BigDecimal res = BigDecimal.valueOf(2);

        try {
            calculator.changeOperator(res, Operator.ADD);
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
        Memory memory = new Memory();
        BigDecimal res = BigDecimal.valueOf(7);

        try {
            calculator.changeOperator(res, Operator.SUBTRACT);
            memory.memoryStore(BigDecimal.valueOf(10));
            res = calculator.sqrt(BigDecimal.valueOf(3));
            res = calculator.calculateIntermediateResult(res);
            calculator.changeOperator(res, Operator.ADD);
            res = memory.memoryRecall();
            res = calculator.calculateResult(res);
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
