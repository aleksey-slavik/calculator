package com.implemica.calculator.model;

import com.implemica.calculator.util.enums.Operator;
import com.implemica.calculator.util.exception.OverflowException;
import com.implemica.calculator.util.exception.SquareRootException;
import com.implemica.calculator.util.exception.ZeroByZeroDivideException;
import com.implemica.calculator.util.exception.ZeroDivideException;

import java.math.BigDecimal;

/**
 * Calculator operations.
 * Consist main calculator operations such as add, subtract, multiply, divide, negate, percent, inverse, sqr, sqrt and equals
 *
 * @author Slavik Aleksey V.
 */
public class Calculator {

    /**
     * BigDecimal representation of 100
     */
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    /**
     * divide operation scale
     */
    private static final int DIVIDE_SCALE = 10000;

    /**
     * maximum scale of calculator.
     * numbers with scale bigger than this value throws overflow
     */
    private static final int MAX_SCALE = 10000;

    /**
     * Left operand of equation
     */
    private BigDecimal left = BigDecimal.ZERO;

    /**
     * Right operand of equation
     */
    private BigDecimal right = BigDecimal.ZERO;

    /**
     * Operation of equation
     */
    private Operator operator = Operator.EMPTY;

    /**
     * Is true, if some operation was already done.
     */
    private boolean isNextOperator;

    public void changeOperator(BigDecimal value, Operator operator) {
        isNextOperator = false;

        if (left.equals(BigDecimal.ZERO)) {
            left = value;
        }

        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    public void resetOperator() {
        operator = Operator.EMPTY;
    }

    public BigDecimal calculateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        if (!isNextOperator) {
            right = value;
        }

        isNextOperator = true;
        left = calculate();
        return left;
    }

    public BigDecimal calculateIntermediateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        isNextOperator = false;
        right = value;
        left = calculate();
        return left;
    }

    public BigDecimal calculateEqualsResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        if (left.equals(BigDecimal.ZERO)) {
            left = value;
        }

        left = calculate();
        return left;
    }

    private BigDecimal calculate() throws OverflowException, ZeroDivideException, ZeroByZeroDivideException {
        if (operator == Operator.EMPTY) {
            return BigDecimal.ZERO;
        }

        BigDecimal res = BigDecimal.ZERO;

        if (operator == Operator.ADD) {
            res = add();
        } else if (operator == Operator.SUBTRACT) {
            res = subtract();
        } else if (operator == Operator.MULTIPLY) {
            res = multiply();
        } else if (operator == Operator.DIVIDE) {
            res = divide();
        }

        if (Math.abs(res.scale()) > MAX_SCALE) {
            throw new OverflowException("Scale of result is bigger than max scale value");
        }

        return res;
    }

    private BigDecimal add() {

        return left.add(right);
    }

    private BigDecimal subtract() {

        return left.subtract(right);
    }

    private BigDecimal multiply() {

        return left.multiply(right);
    }

    private BigDecimal divide() throws ZeroByZeroDivideException, ZeroDivideException {
        if (left.equals(BigDecimal.ZERO) && right.equals(BigDecimal.ZERO)) {
            throw new ZeroByZeroDivideException("Quotient of two zeros are not defined");
        }

        if (right.equals(BigDecimal.ZERO)) {
            throw new ZeroDivideException("Divide by zero in expression: " + left + " / " + right);
        }

        return left.divide(right, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    public BigDecimal percent(BigDecimal value) {

        return left.multiply(value.divide(HUNDRED, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP)).stripTrailingZeros();
    }

    public BigDecimal negate(BigDecimal value) {

        return value.negate();
    }

    public BigDecimal inverse(BigDecimal value) throws ZeroDivideException {
        if (value.equals(BigDecimal.ZERO)) {
            throw new ZeroDivideException("Divide by zero in inverse method");
        }

        return BigDecimal.ONE.divide(value, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    public BigDecimal sqr(BigDecimal value) throws OverflowException {
        BigDecimal res = value.pow(2);

        if (Math.abs(res.scale()) > MAX_SCALE) {
            throw new OverflowException("Scale of result of sqr is bigger than max scale value");
        }

        return res;
    }

    public BigDecimal sqrt(BigDecimal value) throws SquareRootException {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new SquareRootException("Can't get square root of " + value.toPlainString() + ". Non negative number is needed");
        }

        if (value.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        return sqrt(value, BigDecimal.ONE);
    }

    public void clearAll() {
        left = BigDecimal.ZERO;
        right = BigDecimal.ZERO;
        resetOperator();
    }

    public void clearEntry() {

        right = BigDecimal.ZERO;
    }

    private static BigDecimal sqrt(BigDecimal number, BigDecimal guess) {
        BigDecimal two = BigDecimal.valueOf(2);
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal flipA = result;
        BigDecimal flipB = result;
        boolean first = true;
        while (result.compareTo(guess) != 0) {
            if (!first)
                guess = result;
            else
                first = false;

            result = number.divide(guess, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP).add(guess).divide(two, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
            // handle flip flops
            if (result.equals(flipB))
                return flipA;

            flipB = flipA;
            flipA = result;
        }
        return result;
    }
}
