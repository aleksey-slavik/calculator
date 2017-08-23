package com.implemica.calculator;

import com.implemica.calculator.model.Memory;
import com.implemica.calculator.model.Operator;
import com.implemica.calculator.model.exception.SquareRootException;
import com.implemica.calculator.model.exception.OverflowException;
import com.implemica.calculator.model.exception.ZeroByZeroDivideException;
import com.implemica.calculator.model.exception.ZeroDivideException;
import com.implemica.calculator.model.Calculator;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void addTest() throws Exception {
        binaryTest("1", Operator.ADD, "1", "2");
        binaryTest("28", Operator.ADD, "-82", "-54");
        binaryTest("-739", Operator.ADD, "-55", "-794");
        binaryTest("0", Operator.ADD, "23", "23");
        binaryTest("-4", Operator.ADD, "0", "-4");
        binaryTest("-89", Operator.ADD, "89", "0");
        binaryTest("1.39", Operator.ADD, "0.98766", "2.37766");
        binaryTest("-0.1", Operator.ADD, "5987.999", "5987.899");
        binaryTest("-2133.9", Operator.ADD, "-8799.38833", "-10933.28833");
        binaryTest("-9999999999999999", Operator.ADD, "-1", "-10000000000000000");
        binaryTest("9999999999999999", Operator.ADD, "1", "10000000000000000");
    }

    @Test
    public void subtractTest() throws Exception {
        binaryTest("1", Operator.SUBTRACT, "1", "0");
        binaryTest("1", Operator.SUBTRACT, "-1", "2");
        binaryTest("-1", Operator.SUBTRACT, "1", "-2");
        binaryTest("23", Operator.SUBTRACT, "23", "0");
        binaryTest("1961", Operator.SUBTRACT, "-49", "2010");
        binaryTest("0.1", Operator.SUBTRACT, "0.1", "0");
        binaryTest("0.0000000000000001", Operator.SUBTRACT, "0.0000000000000001", "0");
        binaryTest("0.0000000000000001", Operator.SUBTRACT, "-0.0000000000000001", "0.0000000000000002");
        binaryTest("-0.0000000000000001", Operator.SUBTRACT, "0.0000000000000001", "-0.0000000000000002");
        binaryTest("-0.0000000000000001", Operator.SUBTRACT, "-0.0000000000000001", "0");
        binaryTest("9999999999999999", Operator.SUBTRACT, "9999999999999999", "0");
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
        binaryFail("1.e+10001", Operator.ADD, "1.e+10001");
    }

    @Test
    public void percentTest() {
        percentTest("1000", Operator.ADD, "10", "100");
        percentTest("100", Operator.ADD, "1", "1");
    }

    @Test
    public void negateTest() {
        negateTest("-1", "1");
        negateTest("0", "0");
        negateTest("1", "-1");
        negateTest("23", "-23");
        negateTest("-23", "23");
        negateTest("-3982", "3982");
        negateTest("8849", "-8849"); negateTest("-1.717553345667322e+9607", "1.717553345667322e+9607");
        negateTest("-2.832578077688434e+600", "2.832578077688434e+600");
        negateTest("-5.822235463734427e-9608", "5.822235463734427e-9608");
        negateTest("2.832578077688434e+600", "-2.832578077688434e+600");
        negateTest("1.717553345667322e+9607", "-1.717553345667322e+9607");
    }

    @Test
    public void sqrTest() throws OverflowException {
        sqrTest("-22", "484");
        sqrTest("-1", "1");
        sqrTest("-3.e-19", "9.e-38");
        sqrTest("0", "0");
        sqrTest("1", "1");
        sqrTest("4", "16");
        sqrTest("87909.888", "7728148408.172544");
    }

    @Test
    public void sqrFail() {
        sqrFailTest("-1.e+5000");
        sqrFailTest("-1.e-5000");
        sqrFailTest("-2.38476e+7349");
        sqrFailTest("2.38476e-7349");
    }

    @Test
    public void sqrtTest() throws SquareRootException {
        sqrtTest("0", "0");
        sqrtTest("1", "1");
        sqrtTest("9", "3");
        sqrtTest("4", "2");
        sqrtTest("100", "10");
        sqrtTest("10000", "100");

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
        inverseTest("-0.04","-25");
        inverseTest("1000","0.001");
        inverseTest("0.001","1000");
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

    private void sqrtTest(String value, String expected) throws SquareRootException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.sqrt(new BigDecimal(value));
        assertEquals("expected: " + expected + "\n actual: " + actualValue,true, expectedValue.compareTo(actualValue) == 0);
    }

    private void sqrtFailTest(String value) {
        Calculator calculator = new Calculator();
        try {
            calculator.sqrt(new BigDecimal(value));
            fail("Current value " + value + " is valid for square root");
        } catch (SquareRootException e) {
            //expected
        }
    }

    private void negateTest(String actual, String expected) {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.negate(new BigDecimal(actual));
        assertEquals("expected: " + expected + "\n actual: " + actualValue,true, expectedValue.compareTo(actualValue) == 0);
    }

    private void sqrTest(String actual, String expected) throws OverflowException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.sqr(new BigDecimal(actual));
        assertEquals("expected: " + expected + "\n actual: " + actualValue,true, expectedValue.compareTo(actualValue) == 0);
    }

    private void sqrFailTest(String value) {
        Calculator calculator = new Calculator();
        try {
            calculator.sqr(new BigDecimal(value));
            fail("Scale of SQR for current value " + value + " isn't bigger than 10000");
        } catch (OverflowException e) {
            //expected
        }
    }

    private void inverseTest(String actual, String expected) throws ZeroDivideException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.inverse(new BigDecimal(actual));
        assertEquals("expected: " + expected + "\n actual: " + actualValue,true, expectedValue.compareTo(actualValue) == 0);
    }

    private void inverseFailTest(String value) {
        Calculator calculator = new Calculator();
        try {
            calculator.inverse(new BigDecimal(value));
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
        BigDecimal actualValue = calculator.calculateResult(rightValue);
        assertEquals("expected: " + expected + "\n actual: " + actualValue,true, expectedValue.compareTo(actualValue) == 0);
    }

    private void binaryFail(String left, Operator operator, String right) {
        Calculator calculator = new Calculator();
        BigDecimal leftValue = new BigDecimal(left);
        BigDecimal rightValue = new BigDecimal(right);
        calculator.changeOperator(leftValue, operator);
        try {
            calculator.calculateResult(rightValue);
            fail("Values " + left + " and " + right + " don't throw exceptions for " + operator);
        } catch (ZeroByZeroDivideException | ZeroDivideException | OverflowException e) {
            //expected
        }
    }

    private void percentTest(String left, Operator operator, String right, String expected) {
        Calculator calculator = new Calculator();
        BigDecimal leftValue = new BigDecimal(left);
        BigDecimal rightValue = new BigDecimal(right);
        BigDecimal expectedValue = new BigDecimal(expected);
        calculator.changeOperator(leftValue, operator);
        BigDecimal actualValue = calculator.percent(rightValue);
        assertEquals("expected: " + expected + "\n actual: " + actualValue,true, expectedValue.compareTo(actualValue) == 0);
    }

    /**
     * Check add some value to memory using button MS, M+ and M-
     * @param value
     * @param expected
     */
    private void memoryStoreTest(String value, String expected) {
        Memory memory = new Memory();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal pushValue = new BigDecimal(value);
        memory.memoryStore(pushValue);
        assertEquals(expectedValue, memory.memoryRecall());

        memory = new Memory();
        memory.memoryAdd(pushValue);
        assertEquals(expectedValue, memory.memoryRecall());

        memory = new Memory();
        expectedValue = expectedValue.negate();
        memory.memorySubtract(pushValue);
        assertEquals(expectedValue, memory.memoryRecall());
    }

    private void memoryAddTest(String current, String value, String expected) {
        Memory memory = new Memory();
        BigDecimal memoryValue = new BigDecimal(current);
        BigDecimal pushValue = new BigDecimal(value);
        BigDecimal expectedValue = new BigDecimal(expected);
        memory.memoryStore(memoryValue);
        memory.memoryAdd(pushValue);
        assertEquals(expectedValue, memory.memoryRecall());
    }

    private void memorySubtractTest(String current, String value, String expected) {
        Memory memory = new Memory();
        BigDecimal memoryValue = new BigDecimal(current);
        BigDecimal pushValue = new BigDecimal(value);
        BigDecimal expectedValue = new BigDecimal(expected);
        memory.memoryStore(memoryValue);
        memory.memorySubtract(pushValue);
        assertEquals(expectedValue, memory.memoryRecall());
    }

    private void memoryClearTest(String current) {
        Memory memory = new Memory();
        BigDecimal memoryValue = new BigDecimal(current);
        memory.memoryStore(memoryValue);
        memory.memoryClear();
        assertEquals(BigDecimal.ZERO, memory.memoryRecall());
    }
}