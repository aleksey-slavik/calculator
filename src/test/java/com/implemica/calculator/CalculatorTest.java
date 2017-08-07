package com.implemica.calculator;

import com.implemica.calculator.util.enums.Operator;
import com.implemica.calculator.util.exception.SquareRootException;
import com.implemica.calculator.util.exception.OverflowException;
import com.implemica.calculator.util.exception.ZeroByZeroDivideException;
import com.implemica.calculator.util.exception.ZeroDivideException;
import com.implemica.calculator.model.Calculator;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CalculatorTest {

    @Test
    public void addTest() throws Exception {
        binaryTest("1", Operator.PLUS, "1", "2");
        binaryTest("28", Operator.PLUS, "-82", "-54");
        binaryTest("-739", Operator.PLUS, "-55", "-794");
        binaryTest("0", Operator.PLUS, "23", "23");
        binaryTest("-4", Operator.PLUS, "0", "-4");
        binaryTest("-89", Operator.PLUS, "89", "0");
        binaryTest("462", Operator.PLUS, "-462", "0");
        binaryTest("1.39", Operator.PLUS, "0.98766", "2.37766");
        binaryTest("-0.1", Operator.PLUS, "5987.999", "5987.899");
        binaryTest("-2133.9", Operator.PLUS, "-8799.38833", "-10933.28833");
        binaryTest("9999999999999999", Operator.PLUS, "1", "1.e+16");
    }

    @Test
    public void subtractTest() throws Exception {
        binaryTest("1", Operator.MINUS, "1", "0");
    }

    @Test
    public void divideTest() throws Exception {
        binaryTest("1", Operator.DIVIDE, "1", "1");
    }

    @Test
    public void multiplyTest() throws Exception {
        binaryTest("1", Operator.MULTIPLY, "1", "1");
    }

    @Test
    public void binaryFail() {
        binaryFail("1.e+10001", Operator.PLUS, "1.e+10001");
    }

    @Test
    public void percentTest() {
        //percentTest("1000", Operator.PLUS, "10", "100");
        percentTest("100", Operator.PLUS, "1", "1");
    }

    @Test
    public void negateTest() {
        negateTest("-1.717553345667322e+9607", "1.717553345667322e+9607");
        negateTest("-2.832578077688434e+600", "2.832578077688434e+600");
        negateTest("-3982", "3982");
        negateTest("-1", "1");
        negateTest("-5.822235463734427e-9608", "5.822235463734427e-9608");
        negateTest("0", "0");
        negateTest("5.822235463734427e-9608", "-5.822235463734427e-9608");
        negateTest("1", "-1");
        negateTest("23", "-23");
        negateTest("8849", "-8849");
        negateTest("2.832578077688434e+600", "-2.832578077688434e+600");
        negateTest("1.717553345667322e+9607", "-1.717553345667322e+9607");
    }

    @Test
    public void sqrTest() throws OverflowException {
        sqrTest("-1.e+5000", "1.e+10000");
        sqrTest("-4.716914975207485e+1265", "2.224928688333663e+2531");
        sqrTest("-22", "484");
        sqrTest("-1", "1");
        sqrTest("-3.e-19", "9.e-38");
        sqrTest("0", "0");
        sqrTest("2.497384811270314e-4712", "6.236930895563662e-9424");
        sqrTest("2.405529628524835e-74", "5.786572793710831e-148");
        sqrTest("1", "1");
        sqrTest("4", "16");
        sqrTest("87909.888", "7728148408.172544");
        sqrTest("5.972427781873983e+19", "3.566989360970018e+39");
        sqrTest("4.716914975207485e+1265", "2.224928688333663e+2531");
        sqrTest("1.e+5000", "1.e+10000");
    }

    @Test
    public void sqrFail() {
        sqrFailTest("-9.764584e+12345");
        sqrFailTest("-8.876999e+10000");
        sqrFailTest("-2.38476e+7349");
        sqrFailTest("-1.e+5001");
        sqrFailTest("1.e+5001");
        sqrFailTest("2.38476e+7349");
        sqrFailTest("8.876999e+10000");
        sqrFailTest("9.764584e+12345");
    }

    @Test
    public void sqrtTest() throws InvalidSqrtException {
        sqrtTest("0", "0");
        sqrtTest("1.935438063020253e-29", "4.399361388906637e-15");
        sqrtTest("1", "1");
        sqrtTest("2", "1.414213562373095");
        sqrtTest("4", "2");
        sqrtTest("100", "10");
        sqrtTest("10000", "100");
        sqrtTest("5.809392023064086e+18", "2410268039.671954");
        //sqrtTest("6.437652924183111e+2401", "8.0234985662011e+1200");
        //sqrtTest("1.717553345667319e+9607", "4.144337517224338e+4803");
    }

    @Test
    public void sqrtFail() {
        sqrtFailTest("-1.1E-6433");
        sqrtFailTest("-1");
        sqrtFailTest("-954");
        sqrtFailTest("-889663227");
        sqrtFailTest("-1.45345e+147");
    }

    @Test
    public void inverseTest() throws ZeroDivideException {
        inverseTest("1", "1");
        inverseTest("-25", "-0.04");
        inverseTest("-0.04", "-25");
        inverseTest("55.33", "0.01807337791433219");
        inverseTest("0.01807337791433219","55.33");
        inverseTest("-3.e-33", "-3.333333333333333e+32");
        inverseTest("-3.333333333333333e+32", "-3.e-33");
        inverseTest("4995", "2.002002002002002e-4");
        inverseTest("2.002002002002002e-4", "4995");
        inverseTest("1.482904830437566e+1694", "6.743521090998985e-1695");
        inverseTest("6.743521090998985e-1695", "1.482904830437566e+1694");
        inverseTest("7.170780012865725e+8470", "1.394548428770388e-8471");
        inverseTest("1.394548428770388e-8471", "7.170780012865725e+8470");
        inverseTest("-3.734365312567033e-4236", "-2.677831214409476e+4235");
        inverseTest("-2.677831214409476e+4235", "-3.734365312567033e-4236");
        inverseTest("1.e-9999", "1.e+9999");
        inverseTest("1.e+9999", "1.e-9999");
    }

    @Test
    public void inverseFail() {
        inverseFailTest("0");
    }

    @Test
    public void memoryStoreTest() {
        memoryStoreTest("1", "1");
    }

    @Test
    public void memoryAddTest() {
        memoryAddTest("12", "3", "15");
    }

    @Test
    public void memorySubtractTest() {
        memorySubtractTest("12", "3", "9");
    }

    @Test
    public void memoryClearTest() {
        memoryClearTest("23");
    }

    private void sqrtTest(String value, String expected) throws InvalidSqrtException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.calculateSqrt(new BigDecimal(value));
        assertEquals(expectedValue, actualValue);
    }

    private void sqrtFailTest(String value) {
        Calculator calculator = new Calculator();
        try {
            calculator.calculateSqrt(new BigDecimal(value));
            fail("Current value " + value + " is valid for square root");
        } catch (InvalidSqrtException e) {
            //expected
        }
    }

    private void negateTest(String actual, String expected) {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.calculateNegate(new BigDecimal(actual));
        assertEquals(expectedValue, actualValue);
    }

    private void sqrTest(String actual, String expected) throws OverflowException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.calculateSqr(new BigDecimal(actual));
        assertEquals(expectedValue, actualValue);
    }

    private void sqrFailTest(String value) {
        Calculator calculator = new Calculator();
        try {
            calculator.calculateSqr(new BigDecimal(value));
            fail("Scale of SQR for current value " + value + " isn't bigger than 10000");
        } catch (OverflowException e) {
            //expected
        }
    }

    private void inverseTest(String actual, String expected) throws ZeroDivideException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.calculateInverse(new BigDecimal(actual));
        assertEquals(expectedValue, actualValue);
    }

    private void inverseFailTest(String value) {
        Calculator calculator = new Calculator();
        try {
            calculator.calculateInverse(new BigDecimal(value));
            fail("Inverse of " + value + " doesn't call exception. Exception called only when value equals zero");
        } catch (ZeroDivideException e) {
            //expected
        }
    }

    private void binaryTest(String left, Operator operator, String right, String expected) throws Exception {
        Calculator calculator = new Calculator();
        BigDecimal leftValue = new BigDecimal(left);
        BigDecimal rightValue = new BigDecimal(right);
        BigDecimal expectedValue = new BigDecimal(expected);
        calculator.changeOperator(leftValue, operator);
        assertEquals(expectedValue, calculator.calculateResult(rightValue));
    }

    private void binaryFail(String left, Operator operator, String right) {
        Calculator calculator = new Calculator();
        BigDecimal leftValue = new BigDecimal(left);
        BigDecimal rightValue = new BigDecimal(right);
        calculator.changeOperator(leftValue, operator);
        try {
            calculator.calculateResult(rightValue);
            fail("Values " + left + " and " + right + " don't throw exceptions for " + operator);
        } catch (ZeroByZeroDivideException e) {
            //expected
        } catch (OverflowException e) {
            //expected
        } catch (ZeroDivideException e) {
            //expected
        }
    }

    private void percentTest(String left, Operator operator, String right, String expected) {
        Calculator calculator = new Calculator();
        BigDecimal leftValue = new BigDecimal(left);
        BigDecimal rightValue = new BigDecimal(right);
        BigDecimal expectedValue = new BigDecimal(expected);
        calculator.changeOperator(leftValue, operator);
        assertEquals(expectedValue, calculator.calculatePercent(rightValue));
    }

    /**
     * Check add some value to memory using buttons MS, M+ and M-
     * @param value
     * @param expected
     */
    private void memoryStoreTest(String value, String expected) {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal pushValue = new BigDecimal(value);
        calculator.memoryStore(pushValue);
        assertEquals(expectedValue, calculator.memoryRecall());

        calculator = new Calculator();
        calculator.memoryAdd(pushValue);
        assertEquals(expectedValue, calculator.memoryRecall());

        calculator = new Calculator();
        expectedValue = expectedValue.negate();
        calculator.memorySubtract(pushValue);
        assertEquals(expectedValue, calculator.memoryRecall());
    }

    private void memoryAddTest(String memory, String value, String expected) {
        Calculator calculator = new Calculator();
        BigDecimal memoryValue = new BigDecimal(memory);
        BigDecimal pushValue = new BigDecimal(value);
        BigDecimal expectedValue = new BigDecimal(expected);
        calculator.memoryStore(memoryValue);
        calculator.memoryAdd(pushValue);
        assertEquals(expectedValue, calculator.memoryRecall());
    }

    private void memorySubtractTest(String memory, String value, String expected) {
        Calculator calculator = new Calculator();
        BigDecimal memoryValue = new BigDecimal(memory);
        BigDecimal pushValue = new BigDecimal(value);
        BigDecimal expectedValue = new BigDecimal(expected);
        calculator.memoryStore(memoryValue);
        calculator.memorySubtract(pushValue);
        assertEquals(expectedValue, calculator.memoryRecall());
    }

    private void memoryClearTest(String memory) {
        Calculator calculator = new Calculator();
        BigDecimal memoryValue = new BigDecimal(memory);
        calculator.memoryStore(memoryValue);
        calculator.memoryClear();
        assertEquals(BigDecimal.ZERO, calculator.memoryRecall());
    }

    private BigDecimal convert(String value) {
        return new BigDecimal(value);
    }
}