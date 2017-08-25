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

public class ModelTest {//todo add comments and tests for history

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
        binaryTest("0", Operator.DIVIDE, "1", "0");
        binaryTest("10", Operator.DIVIDE, "5", "2");
        binaryTest("81", Operator.DIVIDE, "-9", "-9");
        binaryTest("1", Operator.DIVIDE, "100", "0.01");
        binaryTest("10000", Operator.DIVIDE, "0.0001", "100000000");
        binaryTest("0.0003", Operator.DIVIDE, "0.0003", "1");
        binaryTest("9999999999999999", Operator.DIVIDE, "9", "1111111111111111");
        binaryTest("9999999999999999", Operator.DIVIDE, "-0.1", "-99999999999999990");
        binaryTest("0.0000000000000001", Operator.DIVIDE, "-10", "-0.00000000000000001");
        binaryTest("0.0000000000000001", Operator.DIVIDE, "0.0000000000000001", "1");
    }

    @Test
    public void multiplyTest() throws Exception {
        binaryTest("1", Operator.MULTIPLY, "1", "1");
        binaryTest("1", Operator.MULTIPLY, "0", "0");
        binaryTest("0", Operator.MULTIPLY, "-1", "0");
        binaryTest("0", Operator.MULTIPLY, "0", "0");
        binaryTest("0.0001", Operator.MULTIPLY, "10000", "1");
        binaryTest("12345679", Operator.MULTIPLY, "-9", "-111111111");
        binaryTest("20", Operator.MULTIPLY, "0.5", "10");
        binaryTest("9999999999999999", Operator.MULTIPLY, "1", "9999999999999999");
        binaryTest("9999999999999999", Operator.MULTIPLY, "-10", "-99999999999999990");
        binaryTest("0.0000000000000001", Operator.MULTIPLY, "-0.1", "-0.00000000000000001");
        binaryTest("0.0000000000000001", Operator.MULTIPLY, "1", "0.0000000000000001");
    }

    @Test
    public void binaryFail() {
        binaryFail("9.9e+9999", Operator.ADD, "0.1e+9999");
        binaryFail("-5.9e+9999", Operator.ADD, "-4.1e+9999");
        binaryFail("6.9e+9999", Operator.SUBTRACT, "-3.1e+9999");
        binaryFail("-7.9e+9999", Operator.SUBTRACT, "2.1e+9999");
        binaryFail("5.e+9999", Operator.MULTIPLY, "2");
        binaryFail("2.5e+9999", Operator.DIVIDE, "-0.25");
    }

    @Test
    public void percentTest() {
        percentTest("1000", Operator.ADD, "10", "100");
        percentTest("100", Operator.ADD, "1", "1");
        percentTest("25", Operator.MULTIPLY, "0", "0");
        percentTest("-25", Operator.DIVIDE, "20", "-5");
        percentTest("45", Operator.ADD, "-45", "-20.25");
        percentTest("-1024", Operator.MULTIPLY, "-200", "2048");
        percentTest("0", Operator.DIVIDE, "9999999999", "0");
        percentTest("9999999999999999", Operator.SUBTRACT, "0", "0");
        percentTest("9999999999999999", Operator.SUBTRACT, "100", "9999999999999999");
        percentTest("1000000000000000", Operator.SUBTRACT, "0.0000000000000001", "0.001");
        percentTest("0.0000000000000001", Operator.SUBTRACT, "1000000000000000000", "1");
    }

    @Test
    public void negateTest() {
        negateTest("-1", "1");
        negateTest("0", "0");
        negateTest("1", "-1");
        negateTest("23", "-23");
        negateTest("-23", "23");
        negateTest("-3982", "3982");
        negateTest("8849", "-8849");
        negateTest("-2.832578077688434e+600", "2.832578077688434e+600");
        negateTest("-5.822235463734427e-9608", "5.822235463734427e-9608");
        negateTest("2.832578077688434e+600", "-2.832578077688434e+600");
        negateTest("1.717553345667322e+9607", "-1.717553345667322e+9607");
    }

    @Test
    public void sqrTest() throws OverflowException {
        sqrTest("-22", "484");
        sqrTest("-1", "1");
        sqrTest("0", "0");
        sqrTest("1", "1");
        sqrTest("4", "16");
        sqrTest("-0.1", "0.01");
        sqrTest("0.09", "0.0081");
        sqrTest("-3.e-19", "9.e-38");
        sqrTest("-1.e+2000", "1.e+4000");
        sqrTest("2.e-1234", "4.e-2468");
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
        sqrtTest("81", "9");
        sqrtTest("100", "10");
        sqrtTest("10000", "100");
        sqrtTest("0.09", "0.3");
        sqrtTest("0.0081", "0.09");
        sqrtTest("0.00006561", "0.0081");
        sqrtTest("0.00000000000001", "0.0000001");
    }

    @Test
    public void sqrtFail() {
        sqrtFailTest("-1.1e-6433");
        sqrtFailTest("-1");
        sqrtFailTest("-954");
        sqrtFailTest("-1.45345e+147");
    }

    @Test
    public void inverseTest() throws ZeroDivideException {
        inverseTest("1", "1");
        inverseTest("-25", "-0.04");
        inverseTest("-0.04", "-25");
        inverseTest("1000", "0.001");
        inverseTest("0.001", "1000");
        inverseTest("1.e-9999", "1.e+9999");
        inverseTest("1.e+9999", "1.e-9999");
    }

    @Test
    public void inverseFail() {
        inverseFailTest("0");
        inverseFailTest("-0");
        inverseFailTest("0.");
        inverseFailTest("-0.");
    }

    @Test
    public void memoryStoreTest() {
        memoryStoreTest("1", "1");
        memoryStoreTest("23", "23");
        memoryStoreTest("-732", "-732");
        memoryStoreTest("9834", "9834");
        memoryStoreTest("9999999999999999", "9999999999999999");
        memoryStoreTest("0.0000000000000001", "0.0000000000000001");
    }

    @Test
    public void memoryAddTest() {
        memoryAddTest("12", "3", "15");
        memoryAddTest("23", "-23","0");
        memoryAddTest("987987","0","987987");
        memoryAddTest("1.e+9999","-1.e+9999", "0");
        memoryAddTest("999999999999999","1","1000000000000000");
        memoryAddTest("0.0000000000000001","1","1.0000000000000001");
    }

    @Test
    public void memorySubtractTest() {
        memorySubtractTest("12", "3", "9");
        memorySubtractTest("23", "23","0");
        memorySubtractTest("987987","0","987987");
        memorySubtractTest("1.e+9999","1.e+9999", "0");
        memorySubtractTest("999999999999999","-1","1000000000000000");
        memorySubtractTest("0.0000000000000001","-1","1.0000000000000001");
    }

    @Test
    public void memoryClearTest() {
        memoryClearTest("1");
        memoryClearTest("23");
        memoryClearTest("-732");
        memoryClearTest("9834");
        memoryClearTest("9999999999999999");
        memoryClearTest("0.0000000000000001");
    }

    private void sqrtTest(String value, String expected) throws SquareRootException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.sqrt(new BigDecimal(value));
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
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
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
    }

    private void sqrTest(String actual, String expected) throws OverflowException {
        Calculator calculator = new Calculator();
        BigDecimal expectedValue = new BigDecimal(expected);
        BigDecimal actualValue = calculator.sqr(new BigDecimal(actual));
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
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
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
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
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
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
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
    }

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
        BigDecimal actualValue = memory.memoryRecall();
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
    }

    private void memoryAddTest(String current, String value, String expected) {
        Memory memory = new Memory();
        BigDecimal memoryValue = new BigDecimal(current);
        BigDecimal pushValue = new BigDecimal(value);
        BigDecimal expectedValue = new BigDecimal(expected);
        memory.memoryStore(memoryValue);
        memory.memoryAdd(pushValue);
        BigDecimal actualValue = memory.memoryRecall();
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);
    }

    private void memorySubtractTest(String current, String value, String expected) {
        Memory memory = new Memory();
        BigDecimal memoryValue = new BigDecimal(current);
        BigDecimal pushValue = new BigDecimal(value);
        BigDecimal expectedValue = new BigDecimal(expected);
        memory.memoryStore(memoryValue);
        memory.memorySubtract(pushValue);
        BigDecimal actualValue = memory.memoryRecall();
        assertEquals("expected: " + expected + "\n actual: " + actualValue, true, expectedValue.compareTo(actualValue) == 0);

    }

    private void memoryClearTest(String current) {
        Memory memory = new Memory();
        BigDecimal memoryValue = new BigDecimal(current);
        memory.memoryStore(memoryValue);
        memory.memoryClear();
        BigDecimal actualValue = memory.memoryRecall();
        assertEquals("expected: " + BigDecimal.ZERO + "\n actual: " + actualValue, true, BigDecimal.ZERO.compareTo(actualValue) == 0);
    }
}