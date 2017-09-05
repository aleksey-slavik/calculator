package com.implemica.calculator.model.util;

import com.implemica.calculator.model.enums.BinaryOperator;
import com.implemica.calculator.model.enums.UnaryOperator;
import com.implemica.calculator.model.exception.*;

import java.math.BigDecimal;

/**
 * Consist main calculator operations such as add, subtract, multiply, divide, negate, percent, inverse, sqr, sqrt and equals
 *
 * @author Slavik Aleksey V.
 */
public class CalculationModel {

    /**
     * BigDecimal representation of 100
     */
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    /**
     * Divide operation scale.
     * For increase accuracy of operation need to increase
     */
    private static final int DIVIDE_SCALE = 11000;

    /**
     * Maximum scale of calculator.
     * Numbers with scale bigger than this value throws overflow
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
    private BinaryOperator operator;

    /**
     * Change current operator and save given value as the left operand.
     * Usually called when binary operator are pressed.
     *
     * @param value    given value
     * @param operator given operator
     */
    public void changeOperator(BigDecimal value, BinaryOperator operator) {
        if (left.equals(BigDecimal.ZERO)) {
            left = value;
        }

        this.operator = operator;
    }

    /**
     * Return current statement of operator
     *
     * @return current operator
     */
    public BinaryOperator getOperator() {
        return operator;
    }

    /**
     * Change operator to default
     */
    public void resetOperator() {
        operator = null;
    }

    /**
     * Calculate current expression using given value as left operand, right operand and operator.
     *
     * @param value given value
     * @return result of calculation
     * @throws OverflowException         throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException throws when zero divided by zero
     * @throws ZeroDivideException       throws when not zero number divided by zero
     */
    public BigDecimal calculateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        right = value;
        left = calculate();
        return left;
    }

    /**
     * Calculate current expression using current left operand, given value as right operand, and operator.
     *
     * @param value given value
     * @return result of calculation
     * @throws OverflowException         throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException throws when zero divided by zero
     * @throws ZeroDivideException       throws when not zero number divided by zero
     */
    public BigDecimal calculateIntermediateResult(BigDecimal value) throws OverflowException, ZeroByZeroDivideException, ZeroDivideException {
        left = value;
        left = calculate();
        return left;
    }

    /**
     * Return given percent value of left operand
     *
     * @param value given percent value
     * @return percent value of left operand
     */
    public BigDecimal percent(BigDecimal value) {
        return left.multiply(value.divide(HUNDRED, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP)).stripTrailingZeros();
    }

    /**
     * Return result of given unary operation for given value
     *
     * @param operator given operator
     * @param value    given value
     * @return result of given unary operation for given value
     * @throws OverflowException   throws when scale of result is bigger than MAX_SCALE
     * @throws SquareRootException throws when given number is negative
     * @throws ZeroDivideException throws when not zero number divided by zero
     */
    public BigDecimal calculateUnary(UnaryOperator operator, BigDecimal value) throws OverflowException, SquareRootException, ZeroDivideException {
        BigDecimal res = BigDecimal.ZERO;

        if (operator == UnaryOperator.SQR) {
            res = sqr(value);
        } else if (operator == UnaryOperator.SQRT) {
            res = sqrt(value);
        } else if (operator == UnaryOperator.INVERSE) {
            res = inverse(value);
        } else if (operator == UnaryOperator.NEGATE) {
            res = negate(value);
        }

        return res;
    }

    /**
     * Return default statement of CalculationModel object
     */
    public void clearAll() {
        left = BigDecimal.ZERO;
        right = BigDecimal.ZERO;
        resetOperator();
    }

    /**
     * Set right operand to default
     */
    public void clearEntry() {
        right = BigDecimal.ZERO;
    }

    /**
     * Calculate expression using left and right operands and operator from current statement of CalculationModel object.
     *
     * @return result of calculation
     * @throws OverflowException         throws when scale of result is bigger than MAX_SCALE
     * @throws ZeroByZeroDivideException throws when zero divided by zero
     * @throws ZeroDivideException       throws when not zero number divided by zero
     */
    private BigDecimal calculate() throws OverflowException, ZeroDivideException, ZeroByZeroDivideException {
        if (operator == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal res = BigDecimal.ZERO;

        if (operator == BinaryOperator.ADD) {
            res = add();
        } else if (operator == BinaryOperator.SUBTRACT) {
            res = subtract();
        } else if (operator == BinaryOperator.MULTIPLY) {
            res = multiply();
        } else if (operator == BinaryOperator.DIVIDE) {
            res = divide();
        }

        if (Math.abs(res.scale() - res.precision()) >= MAX_SCALE) {
            throw new OverflowException("Scale of result is bigger than max scale value");
        }

        return res;
    }

    /**
     * Add left operand to right
     *
     * @return result of add
     */
    private BigDecimal add() {
        return left.add(right).stripTrailingZeros();
    }

    /**
     * Subtract left operand from right
     *
     * @return result of subtract
     */
    private BigDecimal subtract() {
        return left.subtract(right).stripTrailingZeros();
    }

    /**
     * Multiply left and right operands
     *
     * @return result of multiply
     */
    private BigDecimal multiply() {
        return left.multiply(right).stripTrailingZeros();
    }

    /**
     * Return negate number for given value.
     *
     * @param value given value
     * @return negate of given value
     */
    private BigDecimal negate(BigDecimal value) {
        return value.negate();
    }

    /**
     * Return inverse number for given value
     *
     * @param value given value
     * @return inverse number
     * @throws ZeroDivideException throws when given number equals zero
     */
    private BigDecimal inverse(BigDecimal value) throws ZeroDivideException {
        if (value.equals(BigDecimal.ZERO)) {
            throw new ZeroDivideException("Divide by zero in inverse method");
        }

        return BigDecimal.ONE.divide(value, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    /**
     * Return result of squaring operation for given value
     *
     * @param value given value
     * @return square of number
     * @throws OverflowException throws when scale of result is bigger than MAX_SCALE
     */
    private BigDecimal sqr(BigDecimal value) throws OverflowException {
        BigDecimal res = value.pow(2);

        if (Math.abs(res.scale()) >= MAX_SCALE) {
            throw new OverflowException("Scale of result of sqr is bigger than max scale value");
        }

        return res;
    }

    /**
     * Return square root for given value
     *
     * @param value given value
     * @return square root of number
     * @throws SquareRootException throws when given number is negative
     */
    private BigDecimal sqrt(BigDecimal value) throws SquareRootException {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new SquareRootException("Can't get square root of " + value.toPlainString() + ". Non negative number is needed");
        }

        if (value.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        return sqrt(value, BigDecimal.ONE);
    }

    /**
     * Divide left operand to right
     *
     * @return result of divide
     * @throws ZeroByZeroDivideException throws when zero divided by zero
     * @throws ZeroDivideException       throws when not zero number divided by zero
     */
    private BigDecimal divide() throws ZeroByZeroDivideException, ZeroDivideException {
        if (left.equals(BigDecimal.ZERO) && right.equals(BigDecimal.ZERO)) {
            throw new ZeroByZeroDivideException("Quotient of two zeros are not defined");
        }

        if (right.equals(BigDecimal.ZERO)) {
            throw new ZeroDivideException("Divide by zero in expression: " + left + " / " + right);
        }

        return left.divide(right, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    /**
     * Returns the square root calculated for the given number. Source of square root for {@link BigDecimal} is
     * <a href="http://www.java2s.com/Code/Java/Data-Type/BigDecimalandBigIntegersqareroot.htm">square rooting a BigDecimal</a>
     *
     * @param number given number
     * @param guess  first approach
     * @return square root of number
     */
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

            result = number.divide(guess, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP)
                    .add(guess)
                    .divide(two, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);

            if (result.equals(flipB))
                return flipA;

            flipB = flipA;
            flipA = result;
        }

        return result;
    }
}
