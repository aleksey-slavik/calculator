package com.implemica.calculator;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ControllerTest {

    private static GuiTest controller;

    @BeforeClass
    public static void init() {
        FXTestUtils.launchApp(Launcher.class);
        controller = new GuiTest() {
            @Override
            protected Parent getRootNode() {
                return stage.getScene().getRoot();
            }
        };
    }

    @Test
    public void testDigits() throws Exception {
        testExpression("0", "0", "");
        testExpression("1", "1", "");
        testExpression("2", "2", "");
        testExpression("3", "3", "");
        testExpression("4", "4", "");
        testExpression("5", "5", "");
        testExpression("6", "6", "");
        testExpression("7", "7", "");
        testExpression("8", "8", "");
        testExpression("9", "9", "");

        testExpression(",", "0,", "");
        testExpression("1,,", "1,", "");
        testExpression("1,,00", "1,00", "");
        testExpression("1,0,11", "1,011", "");
        testExpression("0000000000000001,", "1,", "");
        testExpression("0000000000000000000001,", "1,", "");

        testExpression("01", "1", "");
        testExpression("00234", "234", "");
        testExpression("00100", "100", "");
        testExpression("000001024", "1 024", "");
        testExpression("1234567890123456", "1 234 567 890 123 456", "");

        testExpression("8589657657", "8 589 657 657", "");
        testExpression("1874383290974918", "1 874 383 290 974 918", "");
        testExpression("9866764576557657 negate", "-9 866 764 576 557 657", "");
        testExpression("4980009809,93024", "4 980 009 809,93024", "");
        testExpression("8398663054,46785 negate", "-8 398 663 054,46785", "");
        testExpression("927364827648237688886", "9 273 648 276 482 376", "");
        testExpression("980432987659269750670945760987 negate", "-9 804 329 876 592 697", "");
    }

    @Test
    public void scientificTest() throws Exception {
        testExpression("9999999999999999 + 1 =", "1e+16");
        testExpression("9999999999999999 + 1 - 1 =", "9 999 999 999 999 999");
        testExpression("9999999999999999 negate - 1 =", "-1e+16");
        testExpression("9999999999999999 negate - 1 + 1 =", "-9 999 999 999 999 999");
        testExpression("9999999999999999 + ,1 = = = =", "9 999 999 999 999 999");
        testExpression("9999999999999999 + ,1 = = = = =", "1e+16");
        testExpression("9999999999999999 + ,4999999999999999 =", "9 999 999 999 999 999");
        testExpression("9999999999999999 + ,4999999999999999 + ,000000000000001 =", "1e+16");

        testExpression("0,0000000000000001 / 10 =", "1e-17");
        testExpression("0,0000000000000001 / 10 * 10 =", "0,0000000000000001");
        testExpression("0,0000000000000001 negate / 10 =", "-1e-17");
        testExpression("0,0000000000000001 negate / 10 * 10 =", "-0,0000000000000001");
    }

    @Test
    public void addTest() throws Exception {
        testExpression("12 negate + 11 +", "-1", "-12 + 11 +");
        testExpression("+ 100 negate", "-100", "0 +");
        testExpression("+ 5 negate =", "-5", "");
        testExpression("0,5 + 12 +", "12,5", "0,5 + 12 +");
        testExpression("2 + 2 =", "4", "");
        testExpression("2 + 2 +", "4", "2 + 2 +");
        testExpression("2 + 2 + + + +", "4", "2 + 2 +");
        testExpression("2 + + + +", "2", "2 +");

        testExpression("0 + 0 +", "0", "0 + 0 +");
        testExpression("+ 0 =", "0", "");
        testExpression("78 + 0 +", "78", "78 + 0 +");
        testExpression("93,4 negate + 0 +", "-93,4", "-93,4 + 0 +");
        testExpression("0 + ,5 =", "0,5", "");
        testExpression("0 + 9481 negate +", "-9 481", "0 + -9481 +");

        testExpression("2 + 2 negate =", "0", "");
        testExpression("2 negate + 2 +", "0", "-2 + 2 +");
        testExpression("32 negate + 32 =", "0", "");
        testExpression(",00001 + ,00001 negate +", "0", "0,00001 + -0,00001 +");
        testExpression("36,6 negate + 36,6 negate +", "-73,2", "-36,6 + -36,6 +");

        testExpression("15 + 5 = =", "25", "");
        testExpression("36 + 4 = = = = = =", "60", "");
        testExpression("1 + = = = = = = = =", "9", "");
        testExpression("+ 5 = = = = = = = = = =", "50", "");
        testExpression("1 + = = = = = + 1 negate = = = = =", "1", "");
        testExpression(",1 negate + = = = + ,1 = = = =", "0", "");

        testExpression(",0000000000000001 + ,0000000000000001 =", "0,0000000000000002", "");
        testExpression(",0000000000000001 + ,0000000000000001 negate =", "0", "");
        testExpression(",0000000000000001 + ,0000000000000009 =", "0,000000000000001", "");
        testExpression(",0000000000000001 + ,0000000000000009 negate =", "-0,0000000000000008", "");

        testExpression("9999999999999999 + 1 =", "1e+16", "");
        testExpression("9999999999999999 + =", "2e+16", "");
        testExpression("999,999999999999 + ,000000000001 =", "1 000", "");
        testExpression("9999999999999999 + ,00000000000001 =", "9 999 999 999 999 999", "");
    }

    @Test
    public void minusTest() throws Exception {
        testExpression("1 - =", "0", "");
        testExpression("10 - 2 =", "8", "");
        testExpression("10 - 2 -", "8", "10 - 2 -");
        testExpression("- 5 -", "-5", "0 - 5 -");
        testExpression("1000 - 0,00000000001 =", "999,99999999999", "");
        testExpression("12345,6789 - 98765,4321 negate =", "111 111,111", "");
        testExpression("23 - - - - - -", "23", "23 -");
        testExpression("74 negate - - - -", "-74", "-74 -");
        testExpression(",04 - 1,96 - - -", "-1,92", "0,04 - 1,96 -");

        testExpression("0 - 0 =", "0", "");
        testExpression("- 0 -", "0", "0 - 0 -");
        testExpression("1 - 0 -", "1", "1 - 0 -");
        testExpression("36,6 negate - 0 -", "-36,6", "-36,6 - 0 -");
        testExpression("0 - 97 -", "-97", "0 - 97 -");
        testExpression("0 - 5392,783 negate -", "5 392,783", "0 - -5392,783 -");

        testExpression("108 - 108 =", "0", "");
        testExpression("234 - 234 negate -", "468", "234 - -234 -");
        testExpression("98,7 negate - 98,7 -", "-197,4", "-98,7 - 98,7 -");
        testExpression("528,99712 negate - 528,99712 negate =", "0", "");

        testExpression("18 - 5 = =", "8", "");
        testExpression("18 - 5 = = = = =", "-7", "");
        testExpression("1 - = = = = = = = =", "-7", "");
        testExpression("- 5 = = = = = = = = = =", "-50", "");
        testExpression("1 + = = = = = + 1 negate = = = = =", "1", "");
        testExpression(",1 negate + = = = + ,1 = = = =", "0", "");

        testExpression("0,00000000000001 - 0,00000000000001 =", "0", "");
        testExpression("0,00000000000001 - 0,00000000000001 negate =", "0,00000000000002", "");
        testExpression("0,00000000000001 - 0,00000000000002 =", "-0,00000000000001", "");
        testExpression("0,00000000000001 - 0,00000000000002 negate =", "0,00000000000003", "");

        testExpression("9999999999999999 - 9999999999999999 =", "0", "");
        testExpression("9999999999999999 - 9999999999999998 =", "1", "");
        testExpression("9999999999999998 - 9999999999999999 =", "-1", "");
        testExpression("9999999999999999 - 9999999999999999 negate =", "2e+16", "");
    }

    @Test
    public void divideTest() throws Exception {
        testExpression("0 / 3 -", "0", "0 ÷ 3 -");
        testExpression("0 / 1 =", "0", "");
        testExpression("0 / 0,00000000000001 /", "0", "0 ÷ 0,00000000000001 ÷");
        testExpression("0 / 9999999999999999 =", "0", "");

        testExpression("1 / 1 /", "1", "1 ÷ 1 ÷");
        testExpression("3 / 1 =", "3", "");
        testExpression(",0000000000000001 / 1 /", "0,0000000000000001", "0,0000000000000001 ÷ 1 ÷");
        testExpression("9999999999999999 / 1 =", "9 999 999 999 999 999", "");

        testExpression(",0000000000000001 / 10 +", "1e-17", "0,0000000000000001 ÷ 10 +");
        testExpression(",0000000000000001 / 3 =", "3,333333333333333e-17", "");
        testExpression(",0000000000000001 / ,0000000000000001 =", "1", "");
        testExpression(",0000000000000001 / ,0000000000000002 =", "0,5", "");
        testExpression(",0000000000000002 / ,0000000000000001 -", "2", "000000002 ÷ 0,0000000000000001 -");
        testExpression(",0000000000000001 / 9999999999999999 =", "1e-32", "");

        testExpression("9999999999999999 / 9 =", "1 111 111 111 111 111", "");
        testExpression("9999999999999999 / 7 -", "1 428 571 428 571 428", "9999999999999999 ÷ 7 -");
        testExpression("9999999999999999 / 0,1 =", "9,999999999999999e+16", "");
        testExpression("9999999999999999 / 0,00000000000001 =", "9,999999999999999e+29", "");
        testExpression("9999999999999999 / 9999999999999999 /", "1", "99999999999 ÷ 9999999999999999 ÷");
        testExpression("9999999999999999 / 8888888888888888 +", "1,125", "99999999999 ÷ 8888888888888888 +");
        testExpression("9999999999999999 negate / 8888888888888888 =", "-1,125", "");

        testExpression("10 / 2 /", "5", "10 ÷ 2 ÷");
        testExpression("10 negate / 2 =", "-5", "");
        testExpression("10 / 2 negate /", "-5", "10 ÷ -2 ÷");
        testExpression("1 / 3 =", "0,3333333333333333", "");
        testExpression("10 / 2 = =", "2,5", "");
        testExpression("10 / 2 = = =", "1,25", "");

        testExpression("10 / 5 / 2 /", "1", "10 ÷ 5 ÷ 2 ÷");
        testExpression("15 / 6 / 8 / 0,4 /", "0,78125", "15 ÷ 6 ÷ 8 ÷ 0,4 ÷");
    }

    @Test
    public void divideErrorTest() throws Exception {
        testErrorExpression("0 / 0 =", "Result is undefined", "0 ÷");
        testErrorExpression("21328 / 0 =", "Cannot divide by zero", "21328 ÷");
        testErrorExpression("3 / 0 =", "Cannot divide by zero", "3 ÷");
        testErrorExpression("9999999999999999 / 0 =", "Cannot divide by zero", "9999999999999999 ÷");
        testErrorExpression("0,00000000000001 / 0 =", "Cannot divide by zero", "0,00000000000001 ÷");
        testErrorExpression("3 negate / 0 =", "Cannot divide by zero", "-3 ÷");
        testErrorExpression("9999999999999999 negate / 0 =", "Cannot divide by zero", "-9999999999999999 ÷");
        testErrorExpression("0,00000000000001 negate / 0 =", "Cannot divide by zero", "-0,00000000000001 ÷");
    }

    @Test
    public void multiplyTest() throws Exception {
        testExpression("1 * 1 =", "1");
        testExpression("0 * 1 =", "0");
        testExpression(",0001 * 10000 =", "1");

        testExpression("0 * 0 =", "0");
        testExpression("0 * 1 =", "0");
        testExpression("0 * 1 negate =", "0");
        testExpression("* 0 =", "0");
        testExpression("* 1 =", "0");
        testExpression("* 5 =", "0");
        testExpression("* 9999999999999999 =", "0");
        testExpression("* 0,00000000000001 =", "0");
        testExpression("5 * 0 =", "0");
        testExpression("5 negate * 0 =", "0");
        testExpression("5 * 0 negate =", "0");
        testExpression("9999999999999999 * 0 =", "0");
        testExpression("0 * 9999999999999999 =", "0");
        testExpression("0,00000000000001 * 0 =", "0");
        testExpression("0 * 0,00000000000001 =", "0");

        testExpression("1 * 1 =", "1");
        testExpression("2 * 1 =", "2");
        testExpression("2 negate * 1 =", "-2");
        testExpression("2,25 * 1 =", "2,25");
        testExpression("2,25 negate * 1 =", "-2,25");
        testExpression("9999999999999999 * 1 =", "9 999 999 999 999 999");
        testExpression("9999999999999999 negate * 1 =", "-9 999 999 999 999 999");
        testExpression("0,00000000000001 * 1 =", "0,00000000000001");
        testExpression("0,00000000000001 negate * 1 =", "-0,00000000000001");
        testExpression("9999999999999999 * 1 negate =", "-9 999 999 999 999 999");
        testExpression("9999999999999999 negate * 1 negate =", "9 999 999 999 999 999");
        testExpression("0,00000000000001 * 1 negate =", "-0,00000000000001");
        testExpression("0,00000000000001 negate * 1 negate =", "0,00000000000001");

        testExpression("12345679 * 9 =", "111 111 111");
        testExpression("12345679 * 9 negate =", "-111 111 111");
        testExpression("20 * 0,5 =", "10");
        testExpression("20 * 0,5 negate =", "-10");
        testExpression("9999999999999999 * 9999999999999999 =", "9,999999999999998e+31");
        testExpression("9999999999999999 * 9999999999999999 negate =", "-9,999999999999998e+31");
        testExpression("0,00000000000001 * 0,00000000000001 =", "1e-28");
        testExpression("0,00000000000001 * 0,00000000000001 negate =", "-1e-28");

        testExpression("5 * =", "25");
        testExpression("5 * = =", "125");
        testExpression("3 * = * =", "81");
        testExpression("0,1 * = * 10 =", "0,1");
        testExpression("3 * = = =", "81");
    }

    @Test
    public void negateTest() throws Exception {
        testExpression("0 negate", "0");
        testExpression("1 negate", "-1");
        testExpression("1 negate negate", "1");
        testExpression("5 negate 8", "-58");

        testExpression("9999999999999999 negate =", "-9 999 999 999 999 999");
        testExpression("9999999999999999 negate negate =", "9 999 999 999 999 999");
        testExpression("0,00000000000001 negate =", "-0,00000000000001");
        testExpression("0,00000000000001 negate negate =", "0,00000000000001");

        testExpression("5 + 0 negate =", "5");
        testExpression("1 + 1 negate =", "0");
        testExpression("1 - 1 negate =", "2");
        testExpression("0,1 + 0,1 negate =", "0");
        testExpression("0,1 - 0,1 negate =", "0,2");
        testExpression("1 + 9999999999999999 negate =", "-9 999 999 999 999 998");
        testExpression("1 + - 9999999999999999 negate =", "1e+16");
        testExpression("1 + 0,00000000000001 negate =", "0,99999999999999");
        testExpression("1 - 0,00000000000001 negate =", "1,00000000000001");
        testExpression("3 + 5 negate =", "-2");
        testExpression("3 - 5 negate =", "8");

        testExpression("3 negate negate =", "3");
        testExpression("2 negate negate negate =", "-2");
    }

    @Test
    public void inverseTest() throws Exception {
        testExpression("1 1/", "1");
        testExpression("0 + 1 1/ =", "1");
        testExpression("0 + 1 negate 1/=", "-11");
        testExpression("0 + 2 1/ =", "0,5");
        testExpression("0 + 2 negate 1/ =", "-0,5");
        testExpression("0 + 0,1 1/ =", "10");
        testExpression("0 + 0,1 negate 1/ =", "-10");
        testExpression("1 + 2 1/ =", "1,5");
        testExpression("5 + 10 1/ 1/ =", "15");
        testExpression("5 + 10 1/ 1/ 1/ =", "5,1");

        testExpression("9999999999999999 1/", "0,0000000000000001");
        testExpression("9999999999999999 negate 1/", "-0,0000000000000001");
        testExpression("0,00000000000001 1/", "100 000 000 000 000");
        testExpression("0,00000000000001 negate 1/", "-100 000 000 000 000");
        testExpression("0,00000000000001 1/ 1/", "0,00000000000001");

        testExpression("5 1/", "0,2", "1/(5)");
        testExpression("5 1/ 1/", "5", "1/(1/(5))");
        testExpression("1 + 5 1/", "0,2", "1 + 1/(5)");
        testExpression("1 + 5 1/ 1/", "5", "1 + 1/(1/(5))");
    }

    @Test
    public void inverseErrorTest() throws Exception {
        testErrorExpression("0 1/", "Cannot divide by zero", "1/(0)");
        testErrorExpression("1 + 0 1/", "Cannot divide by zero", "1 + 1/(0)");
        testErrorExpression("0,00000000000001 + 0 1/", "Cannot divide by zero", "0,00000000000001 + 1/(0)");
        testErrorExpression("9999999999999999 + 0 1/", "Cannot divide by zero", "9999999999999999 + 1/(0)");
        testErrorExpression("2 - 2 = 1/", "Cannot divide by zero", "1/(0)");
        testErrorExpression("24 + 24 negate = 1/", "Cannot divide by zero", "1/(0)");
    }

    @Test
    public void sqrTest() throws Exception {
        testExpression("sqr", "0", "sqr(0)");
        testExpression("sqr sqr", "0", "sqr(sqr(0))");
        testExpression("sqr sqr sqr", "0", "sqr(sqr(sqr(0)))");

        testExpression("0 sqr", "0", "sqr(0)");
        testExpression("0 sqr sqr", "0", "sqr(sqr(0))");
        testExpression("0 sqr sqr sqr", "0", "sqr(sqr(sqr(0)))");

        testExpression("1 sqr", "1", "sqr(1)");
        testExpression("1 negate sqr", "1", "sqr(-1)");
        testExpression("1 sqr sqr sqr", "1", "sqr(sqr(sqr(1)))");

        testExpression("10 sqr", "100", "sqr(10)");
        testExpression("10 sqr sqr", "10 000", "sqr(sqr(10))");
        testExpression("10 sqr sqr sqr", "100 000 000", "sqr(sqr(sqr(10)))");

        testExpression("2 negate sqr", "4", "sqr(-2)");
        testExpression("5 negate negate sqr", "25", "sqr(5)");
        testExpression("125 negate sqr sqr", "244 140 625", "sqr(sqr(-125))");

        testExpression("0,0000000000000001 sqr", "1e-32", "sqr(0,0000000000000001)");
        testExpression("0,0000000000000001 negate sqr sqr", "1e-64", "sqr(sqr(-0,0000000000000001))");
        testExpression("9999999999999999 sqr", "9,999999999999998e+31", "sqr(9999999999999999)");
        testExpression("9999999999999999 negate sqr sqr sqr sqr", "9,999999999999984e+255", "qr(sqr(sqr(-9999999999999999))))");
    }

    @Test
    public void sqrtTest() throws Exception {
        testExpression("sqrt", "0", "√(0)");
        testExpression("sqrt sqrt", "0", "√(√(0))");
        testExpression("sqrt sqrt sqrt", "0", "√(√(√(0)))");

        testExpression("0 sqrt", "0");
        testExpression("1 sqrt", "1");
        testExpression("2 sqrt", "1,414213562373095");
        testExpression("4 sqrt", "2");
        testExpression("1 0 0 sqrt", "10");

        testExpression("9 sqrt =", "3");
        testExpression("81 sqrt sqrt =", "3");
        testExpression("6561 sqrt sqrt sqrt =", "3");
        testExpression("9 sqrt + 81 sqrt =", "12");

        testExpression("0,09 sqrt =", "0,3");
        testExpression("0,0081 sqrt sqrt =", "0,3");
        testExpression("0,00006561 sqrt sqrt sqrt =", "0,3");

        testExpression("0,00000000000001 sqrt =", "0,0000001");
        testExpression("9999999999999999 sqrt sqrt sqrt sqrt sqrt =", "3,162277660168379");

        testExpression("100 sqrt", "10", "√(100)");
        testExpression("5 + 9 sqrt", "3", "5 + √(9)");
        testExpression("81 sqrt sqrt", "3", "√(√(81))");
        testExpression("5 + 81 sqrt sqrt", "3", "5 + √(√(81))");
        testExpression("9 sqrt =", "3", "");
        testExpression("9 sqrt + 1", "1", "√(9) +");
    }

    @Test
    public void sqrtErrorTest() throws Exception {
        testExpression("1 negate sqrt", "Invalid input", "√(-1)");
        testExpression("100 negate sqrt", "Invalid input", "√(-100)");
        testExpression("0,09 negate sqrt", "Invalid input", "√(-0,09)");
        testExpression("3 - 8 = sqrt", "Invalid input", "√(-5)");
        testExpression("9999999999999999 negate sqrt", "Invalid input", "√(-9999999999999999)");
        testExpression("0,00000000000001 negate sqrt", "Invalid input", "√(-0,00000000000001)");
    }

    @Test
    public void percentTest() throws Exception {
        testExpression("100 + 10 %", "10");
        testExpression("98 - 34 %", "33,32");
        testExpression("9870 * 120 %", "11 844");
        testExpression("12365 / 99 %", "12 241,35");

        testExpression("100 + 0 % =", "100");
        testExpression("100 + 1 % =", "101");
        testExpression("100 + 1 negate % =", "99");
        testExpression("100 - 1 % =", "99");
        testExpression("100 + 0,1 % =", "100,1");
        testExpression("100 + 100 % =", "200");
        testExpression("100 - 100 % =", "0");
        testExpression("100 * 2 % =", "200");
        testExpression("100 / 20 % =", "5");
        testExpression("100 + 500 % =", "600");

        testExpression("100 negate + 10 % =", "-110");
        testExpression("100 negate * 10 % =", "1 000");

        testExpression("0,1 + 100 % =", "0,2");
        testExpression("0,1 - 100 % =", "0");
        testExpression("0,1 + 20 % =", "0,12");

        testExpression("9999999999999999 + 9999999999999999 % =", "1,00000000000001e+30");
        testExpression("9999999999999999 + 0 % =", "9 999 999 999 999 999");
        testExpression("9999999999999999 * 9999999999999999 % =", "9,999999999999997e+45");
        testExpression("9999999999999999 - 9999999999999999 % =", "-9,999999999999898e+29");
        testExpression("9999999999999999 + 0,00000000000001 % =", "1e+16");

        testExpression(",0000000000000001 + 1 % =", "0,0000000000000001");
        testExpression(",0000000000000001 - 1 % =", "0,0000000000000001");
        testExpression(",0000000000000001 - 100 % =", "0");
        testExpression(",0000000000000001 + 100 % =", "0,0000000000000002");
        testExpression(",0000000000000001 + ,0000000000000001 % =", "0,0000000000000001");

        testExpression("%", "0");
        testExpression("0 + 1 % =", "0");
        testExpression("0 + 10 % =", "0");
        testExpression("0 + 9999999999999999 % =", "0");
        testExpression("0 + 0,00000000000001 % =", "0");

        testExpression("0 % =", "0");
        testExpression("20 % =", "0");
        testExpression("9999999999999999 % =", "0");
        testExpression("0,00000000000001 % =", "0");

        testExpression("100 + 10 % % % =", "110");
        testExpression("200 + 10 % % =", "240");
        testExpression("200 + 10 % % % =", "280");

        testExpression("50 + 2 %", "1", "50 + 1");
        testExpression("200 + 10 % %", "40", "200 + 40");
        testExpression("20 %", "0", "");
    }

    @Test
    public void backspaceTest() throws Exception {
        testExpression(", back", "0");
        testExpression(",1 back", "0,");
        testExpression(",1 negate back", "-0,");
        testExpression(",1 negate back back", "0");

        testExpression("back", "0");
        testExpression("negate back", "0");
        testExpression("1 back", "0");
        testExpression("45 back back", "0");

        testExpression("12689,88 back", "12 689,8");
        testExpression("980,98 back back", "980,");
        testExpression("673,12 back back back", "673");
        testExpression("34, back", "34");

        testExpression("2 negate back", "0");
        testExpression("23 negate back", "-2");
        testExpression("843,44 negate back", "-843,4");
        testExpression("36,6 negate back back back back", "0");

        testExpression("99 - back back", "99");
        testExpression("2 + back", "2");
        testExpression("885,6 negate * back back back", "-885,6");
        testExpression("45 - 40 = back", "5");

        testExpression("100 + 10 % back", "10");
        testExpression("81 sqrt back", "9");
        testExpression("4 negate sqr back back back", "16");
        testExpression("10 1/ back back", "0,1");
    }

    @Test
    public void combinationTest() throws Exception {
        testExpression("5 + 9 - 1 =", "13");
        testExpression("2 * 3 / 4 =", "1,5");
        testExpression("5 negate * 2 / 4 =", "-2,5");
        testExpression("3 + 5 / 2 =", "4");
        testExpression("5 - 1 * 4 =", "16");
        testExpression("3 + 4 - 5 / 2 * 8 =", "8");

        testExpression("9 sqrt + 5 =", "8");
        testExpression("5 + 9 sqrt =", "8");
        testExpression("5 + 81 sqrt sqrt =", "8");
        testExpression("81 sqrt sqrt + 5 =", "8");
        testExpression("5 1/ + 1 =", "1,2");
        testExpression("1 + 5 1/ =", "1,2");

        testExpression("10 / 3 * 3 =", "10");
        testExpression("2 / 3 * 3 =", "2");
        testExpression("1 / 3 * 3 =", "1");
        testExpression("100 / 7 * 7 =", "100");
        testExpression("9999999999999999 + 1 - 1 =", "9 999 999 999 999 999");
        testExpression("87599 sqr + = =", "23 020 754 403");

        testExpression("76,4 + - ,4 +", "76", "76,4 - 0,4 +");
        testExpression("888 negate - / 111 - +", "-8", "-888 ÷ 111 +");
        testExpression("1024 * + - / 256 + - * * * /", "4", "1024 ÷ 256 ÷");
        testExpression("874,982 / * - 870 + /", "4,982", "874,982 - 870 ÷");
        testExpression("9879879,87876 - * 7654 - - - +", "75 620 600 592,02904", "9879879,87876 × 7654 +");
        testExpression("45 * - 56 + - - + 89,6 * / + * 45 - +", "3 537", "45 - 56 + 89,6 × 45 +");

        testExpression("81 + 3 sqr =", "90", "");
        testExpression("945 - 5 negate sqr +", "920", "945 - sqr(-5) +");
        testExpression("12,99 negate / 10 1/ *", "-129,9", "-12,99 ÷ 1/(10) ×");
        testExpression("6298 * 81 sqrt sqrt - 34 +", "18 860", "6298 × √(√(81)) - 34 +");
        testExpression("103,78 - 10 sqr - 79 = = =", "-233,22", "");
        testExpression("74,882 + 89 sqr sqrt negate * 3 / 2 =", "-21,177", "");

        testExpression("36,98 - 77 C 45 - 23 -", "22", "45 - 23 -");
        testExpression("46,99 * 5 sqr CE 69,9 /", "3 284,601", "46,99 × sqr(5) ÷");
        testExpression("98 + 3 CE 6 - 45 C 44 - 55 back 9 / +", "-15", "44 - 59 +");
        testExpression("67 negate / 98 sqrt C 53 + 89 sqrt 1/ -", "53,10599978800064", "53 + 1/(√(89)) -");
        testExpression("992 back back sqr + - 55 negate + 489 CE 6 -", "142", "sqr(9) - -55 + 6 -");
        testExpression("38,88 / 66 CE 12 - 98,92 + 44 back *", "-91,68", "38,88 ÷ 12 - 98,92 + 4 ×");

        testExpression("75 MS C 888 - 45 + MR -", "918", "888 - 45 + 75 -");
        testExpression("925,77 * 99999 sqr M+ * 99999 M- MR + 98765 sqr sqr -", "9,266752199568381e+22", "× 9999700002 + sqr(sqr(98765)) -");
        testExpression("987654321 sqr sqr M+ M+ C 98 back * MR =", "1,712743695476512e+37", "");
        testExpression("56 MS C 98 + 35,77 MR M+ M- M- =","154","");
        testExpression("81 M+ sqrt - 99 * 22 negate - M+ M+ MR","4 041","√(81) - 99 × -22 -");
        testExpression("225 sqr M- sqr + 66 / 8 M+ MR","-50 617","sqr(sqr(225)) + 66 ÷");

        testExpression(",000000000000001 + ,000000000000001 / ,000000000000001 =", "2");
        testExpression("9999999999999999 + 5 - 4 / 10 =", "1 000 000 000 000 000");
        testExpression("9999999999999999 + 9999999999999999 / 9999999999999999 =", "2");
        testExpression("9999999999999999 * sqr MS + 9876543210 + MR =","9,999999999999998e+47");
        testExpression(",000000000000001 sqrt / 800 sqr =","0,0000000000000494");
        testExpression(",000000000000001 * = / 99999999999999 + 34","34");
    }

    @Test
    public void overflowTest() throws Exception {
        testExpression("9999999999999999 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
        testExpression("9999999999999999 negate sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
        testExpression(",0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
        testExpression(",0000000000000001 negate sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");

        testExpression("1 / 9999999999999999 = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");
        testExpression("1 / 9999999999999999 negate = = = = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");
        testExpression("1 / ,0000000000000001 = = = = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");
        testExpression("1 / ,0000000000000001 negate = = = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");

        testExpression("9999999999999999 * = = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");
        testExpression("9999999999999999 negate * = = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");
        testExpression(",0000000000000001 * = = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");
        testExpression(",0000000000000001 negate * = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");

    }

    @Test
    public void memoryTest() throws Exception {
        testMemoryExpression("10 MS C MR", "10");
        testMemoryExpression(", MS C MR", "0");
        testMemoryExpression("0,000000000000001 MS C MR", "0,000000000000001");
        testMemoryExpression("9999999999999999 MS C MR", "9 999 999 999 999 999");
        testMemoryExpression("9999999999999999 + 1 = MS C MR", "1e+16");
        testMemoryExpression(",0000000000000001 / 10 = MS C MR", "1e-17");

        testMemoryExpression("965 M+ + 123 MR", "965");
        testMemoryExpression("50 M+ M+ M+ MR", "150");
        testMemoryExpression("23 M+ negate M+ MR", "0");
        testMemoryExpression("67 negate M+ negate M+ MR", "0");
        testMemoryExpression(",000000000000001 M+ sqr sqr MR", "0,000000000000001");
        testMemoryExpression("9999999999999999 negate M+ 1/ MR", "-9 999 999 999 999 999");

        testMemoryExpression("4723399 M- C 2377 M- MR", "-4 725 776");
        testMemoryExpression("98 M- C MR", "-98");
        testMemoryExpression("98 negate M- C MR", "98");
        testMemoryExpression("125 M- M- M- M- MR", "-500");
        testMemoryExpression(",000000000000001 negate M- * 85 MR", "0,000000000000001");
        testMemoryExpression("9999999999999999 M- sqrt + 67 MR", "-9 999 999 999 999 999");

        testMemoryExpression("98 M- C 98 M+ MR", "0");
        testMemoryExpression("9999999999999999 M+ M- M+ M- MR", "0");
        testMemoryExpression(",000000000000001 negate M- M+ M- M+ M- M+ MR", "0");
        testMemoryExpression("9999999999999999 sqr M+ C MR sqrt", "9 999 999 999 999 999");
        testMemoryExpression(",000000000000001 1/ M+ C MR 1/", "0,000000000000001");
        testMemoryExpression("10 M- / 3 * 3 = M+ MR", "0");
        testMemoryExpression("1234 M+ - 234 = M- MR", "234");
    }

    @Test
    public void clearEntryTest() throws Exception {
        testExpression("5 CE", "0", "");
        testExpression("9999999999999999 CE", "0", "");
        testExpression("0,00000000000001 CE", "0", "");
        testExpression("9999999999999999 + 1 = CE", "0", "");

        testExpression("1 + 3 CE", "0", "1 +");
        testExpression("28 + 9999999999999999 CE", "0", "28 +");
        testExpression("7 + 0,00000000000001 CE", "0", "7 +");
        testExpression("1 + 9999999999999999 + 1 + CE", "0", "1 + 9999999999999999 + 1 +");

        testExpression("8 + 3 CE 5", "5", "8 +");
        testExpression("10 + 9999999999999999 CE 15", "15", "10 +");
        testExpression("34 + 0,00000000000001 CE 0,5", "0,5", "34 +");
        testExpression("1 + 9999999999999999 + 1 + CE 1,5", "1,5", "1 + 9999999999999999 + 1 +");

    }

    @Test
    public void clearTest() throws Exception {
        testExpression("5 C", "0", "");
        testExpression("9999999999999999 C", "0", "");
        testExpression("0,00000000000001 C", "0", "");
        testExpression("9999999999999999 + 199879 = C", "0", "");

        testExpression("199 + 387 C", "0", "");
        testExpression("1 + 9999999999999999 C", "0", "");
        testExpression("1 + 0,00000000000001 C", "0", "");
        testExpression("1 + 9999999999999999 + 1 + C", "0", "");

        testExpression("15 + 3555 C 5", "5", "");
        testExpression("1 + 9999999999999999 C 76543", "76 543", "");
        testExpression("1 + 0,00000000000001 C 0,5", "0,5", "");
        testExpression("1 + 9999999999999999 + 1 + C 1,5", "1,5", "");
    }

    @Test
    public void mouseClickAndAlternativeTest() throws Exception {
        testAlternativeClick("32 - 4 =", "28", "");
        testAlternativeClick("1234 + 4321 -", "5 555", "1234 + 4321 -");
        testAlternativeClick("8,905 * 345 *", "3 072,225", "8,905 × 345 ×");
        testAlternativeClick("6555 / 30 /", "218,5", "6555 ÷ 30 ÷");

        testAlternativeClick("12345 back back", "123", "");
        testAlternativeClick("78 - 985 - 66 C", "0", "");
        testAlternativeClick("78 - 985 - 66 CE", "0", "78 - 985 -");

        testAlternativeClick("1000 + 55 %", "550", "1000 + 550");
        testAlternativeClick("81 sqrt sqrt", "3", "√(√(81))");
        testAlternativeClick("2 sqr sqr sqr", "256", "sqr(sqr(sqr(2)))");
        testAlternativeClick("777 negate negate negate", "-777", "");
        testAlternativeClick("100 1/ 1/ 1/", "0,01", "1/(1/(1/(100)))");

        testAlternativeClick("91 MS + 12345 MR", "91", "91 +");
        testAlternativeClick("48 negate M+ negate M+ MR", "0", "");
        testAlternativeClick("654,78 M- C MR", "-654,78", "");
        testAlternativeClick("653 MS C MC", "0", "");
    }

    @Test
    public void historyOverflowTest() throws Exception {
        testExpression("123 negate + 889 - 7643 / 2357 * sqr <", "8,512926497930231", "-123 + 889 - 7643 ÷ 2357 × sqr(-2");
        testExpression("87,999 sqrt + 65 sqr sqr - 89,0076 + ,0011 1/ - 849 * 382999 + < > < >","6 836 764 042 139,976", "076 + 1/(0,0011) - 849 × 382999 +");
        testExpression("14374,99 * 65438,01 - 34 1/ 1/ 1/ sqr + 88 < -","940 670 827,3690349",",01 - sqr(1/(1/(1/(34)))) + 88 -");

        testExpression("9999999999999999 + 9999999999999999 + 9999999999999999 +", "3e+16", "99999999999 + 9999999999999999 +");
        testExpression("9999999999999999 * 9999999999999999 * 9999999999999999 + <", "9,999999999999997e+47", "9999999999999999 × 99999999999999");
        testExpression("9999999999999999 / 9999999999999999 - 9999999999999999 + < >", "-9 999 999 999 999 998", "999999999999 - 9999999999999999 +");

        testExpression(",0000000000000001 + ,0000000000000001 - ,0000000000000001 + < > < > < >", "0,0000000000000001", "0000000001 - 0,0000000000000001 +");
        testExpression(",0000000000000001 * ,0000000000000001 + ,0000000000000001 / <", "0,0000000000000001", "0,0000000000000001 × 0,0000000000");
        testExpression(",0000000000000001 sqr - ,0000000000000001 sqrt * ,0000000000000001 1/ + < > < > <", "-100 000 000", "00000000000001) - √(0,00000000000");
    }

    private void testExpression(String expression, String expected) throws Exception {
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);

        for (String item : expression.split(" ")) {
            pushButton(item, false);
        }

        Label numericDisplay = GuiTest.find("#numericField");
        String actualValue = numericDisplay.getText();
        assertEquals(expected, actualValue);
    }

    private void testAlternativeClick(String expression, String numeric, String history) throws Exception {
        testAlternativeButtons(expression, numeric, history);
        testMouseClick(expression, numeric, history);
    }

    private void testAlternativeButtons(String expression, String expected, String history) throws Exception {
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);

        for (String item : expression.split(" ")) {
            pushButton(item, true);
        }

        Label numericDisplay = GuiTest.find("#numericField");
        String actualValue = numericDisplay.getText();
        assertEquals(expected, actualValue);

        Label historyDisplay = GuiTest.find("#historyField");
        actualValue = historyDisplay.getText();
        assertEquals(history, actualValue);
    }

    private void testExpression(String expression, String numeric, String history) throws Exception {
        testExpression(expression, numeric);
        Label historyDisplay = GuiTest.find("#historyField");
        String actualValue = historyDisplay.getText();
        assertEquals(history, actualValue);
    }

    private void testMouseClick(String expression, String numeric, String history) throws Exception {
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);

        for (String item : expression.split(" ")) {
            clickButton(item);
        }

        Label numericDisplay = GuiTest.find("#numericField");
        String actualValue = numericDisplay.getText();
        assertEquals(numeric, actualValue);

        Label historyDisplay = GuiTest.find("#historyField");
        actualValue = historyDisplay.getText();
        assertEquals(history, actualValue);

    }

    private void disableButtonTest(boolean disable) {
        for (Button button : disabled()) {
            assertEquals("Wrong flag for " + button.getId(), disable, button.isDisable());
        }
    }

    private void pushButton(String item, boolean alternative) {
        switch (item) {
            case "=":
                controller.push(alternative ? KeyCode.EQUALS : KeyCode.ENTER);
                break;
            case "back":
                controller.push(KeyCode.BACK_SPACE);
                break;
            case "+":
                if (alternative) {
                    controller.push(KeyCode.ADD);
                } else {
                    controller.push(KeyCode.SHIFT, KeyCode.EQUALS);
                }
                break;
            case "-":
                controller.push(alternative ? KeyCode.SUBTRACT : KeyCode.MINUS);
                break;
            case "/":
                controller.push(alternative ? KeyCode.DIVIDE : KeyCode.SLASH);
                break;
            case "*":
                if (alternative) {
                    controller.push(KeyCode.MULTIPLY);
                } else {
                    controller.push(KeyCode.SHIFT, KeyCode.DIGIT8);
                }
                break;
            case "negate":
                controller.push(KeyCode.F9);
                break;
            case "1/":
                controller.push(KeyCode.R);
                break;
            case "CE":
                controller.push(KeyCode.DELETE);
                break;
            case "C":
                controller.push(KeyCode.ESCAPE);
                break;
            case "sqr":
                controller.push(KeyCode.Q);
                break;
            case "sqrt":
                controller.push(KeyCode.SHIFT, KeyCode.DIGIT2);
                break;
            case "%":
                controller.push(KeyCode.SHIFT, KeyCode.DIGIT5);
                break;
            case "MS":
                controller.push(KeyCode.CONTROL, KeyCode.M);
                break;
            case "MR":
                controller.push(KeyCode.CONTROL, KeyCode.R);
                break;
            case "MC":
                controller.push(KeyCode.CONTROL, KeyCode.L);
                break;
            case "M+":
                controller.push(KeyCode.CONTROL, KeyCode.P);
                break;
            case "M-":
                controller.push(KeyCode.CONTROL, KeyCode.Q);
                break;
            case "<":
                controller.click((Button) GuiTest.find("#left"));
                break;
            case ">":
                controller.click((Button) GuiTest.find("#right"));
                break;
            default:
                pushDigitButtons(item, alternative);
                break;
        }
    }

    private void pushDigitButtons(String number, boolean alternative) {
        char[] digits = number.toCharArray();

        for (char item : digits) {
            switch (item) {
                case '0':
                    controller.push(alternative ? KeyCode.DIGIT0 : KeyCode.NUMPAD0);
                    break;
                case '1':
                    controller.push(alternative ? KeyCode.DIGIT1 : KeyCode.NUMPAD1);
                    break;
                case '2':
                    controller.push(alternative ? KeyCode.DIGIT2 : KeyCode.NUMPAD2);
                    break;
                case '3':
                    controller.push(alternative ? KeyCode.DIGIT3 : KeyCode.NUMPAD3);
                    break;
                case '4':
                    controller.push(alternative ? KeyCode.DIGIT4 : KeyCode.NUMPAD4);
                    break;
                case '5':
                    controller.push(alternative ? KeyCode.DIGIT5 : KeyCode.NUMPAD5);
                    break;
                case '6':
                    controller.push(alternative ? KeyCode.DIGIT6 : KeyCode.NUMPAD6);
                    break;
                case '7':
                    controller.push(alternative ? KeyCode.DIGIT7 : KeyCode.NUMPAD7);
                    break;
                case '8':
                    controller.push(alternative ? KeyCode.DIGIT8 : KeyCode.NUMPAD8);
                    break;
                case '9':
                    controller.push(alternative ? KeyCode.DIGIT9 : KeyCode.NUMPAD9);
                    break;
                case ',':
                    controller.push(KeyCode.COMMA);
                    break;
            }
        }
    }

    private void clickButton(String item) {
        switch (item) {
            case "=":
                controller.click((Button) GuiTest.find("#equals"));
                break;
            case "back":
                controller.click((Button) GuiTest.find("#backspace"));
                break;
            case "+":
                controller.click((Button) GuiTest.find("#add"));
                break;
            case "-":
                controller.click((Button) GuiTest.find("#subtract"));
                break;
            case "/":
                controller.click((Button) GuiTest.find("#divide"));
                break;
            case "*":
                controller.click((Button) GuiTest.find("#multiply"));
                break;
            case "negate":
                controller.click((Button) GuiTest.find("#negate"));
                break;
            case "1/":
                controller.click((Button) GuiTest.find("#inverse"));
                break;
            case "CE":
                controller.click((Button) GuiTest.find("#clear_expr"));
                break;
            case "C":
                controller.click((Button) GuiTest.find("#clear"));
                break;
            case "sqr":
                controller.click((Button) GuiTest.find("#sqr"));
                break;
            case "sqrt":
                controller.click((Button) GuiTest.find("#sqrt"));
                break;
            case "%":
                controller.click((Button) GuiTest.find("#percent"));
                break;
            case "MS":
                controller.click((Button) GuiTest.find("#memory_store"));
                break;
            case "MR":
                controller.click((Button) GuiTest.find("#memory_recall"));
                break;
            case "MC":
                controller.click((Button) GuiTest.find("#memory_clear"));
                break;
            case "M+":
                controller.click((Button) GuiTest.find("#memory_add"));
                break;
            case "M-":
                controller.click((Button) GuiTest.find("#memory_minus"));
                break;
            default:
                clickDigitButtons(item);
                break;
        }
    }

    private void clickDigitButtons(String number) {
        char[] digits = number.toCharArray();

        for (char item : digits) {
            switch (item) {
                case '0':
                    controller.click((Button) GuiTest.find("#zero"));
                    break;
                case '1':
                    controller.click((Button) GuiTest.find("#one"));
                    break;
                case '2':
                    controller.click((Button) GuiTest.find("#two"));
                    break;
                case '3':
                    controller.click((Button) GuiTest.find("#three"));
                    break;
                case '4':
                    controller.click((Button) GuiTest.find("#four"));
                    break;
                case '5':
                    controller.click((Button) GuiTest.find("#five"));
                    break;
                case '6':
                    controller.click((Button) GuiTest.find("#six"));
                    break;
                case '7':
                    controller.click((Button) GuiTest.find("#seven"));
                    break;
                case '8':
                    controller.click((Button) GuiTest.find("#eight"));
                    break;
                case '9':
                    controller.click((Button) GuiTest.find("#nine"));
                    break;
                case ',':
                    controller.click((Button) GuiTest.find("#comma"));
                    break;
            }
        }
    }

    private ArrayList<Button> disabled() {
        ArrayList<Button> disabled = new ArrayList<>();
        disabled.add(GuiTest.find("#divide"));
        disabled.add(GuiTest.find("#multiply"));
        disabled.add(GuiTest.find("#subtract"));
        disabled.add(GuiTest.find("#add"));
        disabled.add(GuiTest.find("#comma"));
        disabled.add(GuiTest.find("#negate"));
        disabled.add(GuiTest.find("#memory_store"));
        disabled.add(GuiTest.find("#memory_minus"));
        disabled.add(GuiTest.find("#memory_add"));
        disabled.add(GuiTest.find("#percent"));
        disabled.add(GuiTest.find("#sqrt"));
        disabled.add(GuiTest.find("#sqr"));
        disabled.add(GuiTest.find("#inverse"));
        return disabled;
    }

    private void testErrorExpression(String expression, String numeric, String history) throws Exception {
        controller.push(KeyCode.ESCAPE);
        disableButtonTest(false);
        testExpression(expression, numeric, history);
        disableButtonTest(true);
    }

    private void testMemoryExpression(String expression, String expected) throws Exception {
        controller.push(KeyCode.CONTROL, KeyCode.L);
        testExpression(expression, expected);
    }
}
