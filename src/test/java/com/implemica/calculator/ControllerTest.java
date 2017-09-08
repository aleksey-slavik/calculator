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

/**
 * Tests work of calculator controller
 *
 * @author Slavik Aleksey V.
 */
public class ControllerTest {

    private static GuiTest controller;

    /**
     * Default value for memory buttons
     */
    private boolean isMemoryDisable = true;

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

    /**
     * Check valid input in numeric field.
     */
    @Test
    public void testDigits() throws Exception {
        //one digit
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

        //comma
        testExpression(",", "0,", "");
        testExpression("1,,", "1,", "");
        testExpression("1,,00", "1,00", "");
        testExpression("1,0,11", "1,011", "");
        testExpression("0000000000000001,", "1,", "");
        testExpression("0000000000000000000001,", "1,", "");

        //begin with zeros
        testExpression("01", "1", "");
        testExpression("00234", "234", "");
        testExpression("00100", "100", "");
        testExpression("000001024", "1 024", "");
        testExpression("9,0000", "9,0000", "");
        testExpression("67,1001000", "67,1001000", "");

        //check numeric field overflow
        testExpression("8589657657", "8 589 657 657", "");
        testExpression("4980009809,93024", "4 980 009 809,93024", "");
        testExpression("1874383290974918", "1 874 383 290 974 918", "");
        testExpression("9866764576557657 negate", "-9 866 764 576 557 657", "");
        testExpression("8398663054,46785 negate", "-8 398 663 054,46785", "");
        testExpression("927364827648237688886", "9 273 648 276 482 376", "");
        testExpression("980432987659269750670945760987 negate", "-9 804 329 876 592 697", "");
    }

    /**
     * Check valid transition to engineering form and back
     */
    @Test
    public void engineeringTest() throws Exception {
        //positive power
        testExpression("9999999999999999 + 1 =", "1e+16");
        testExpression("9999999999999999 + 1 - 1 =", "9 999 999 999 999 999");
        testExpression("9999999999999999 negate - 1 =", "-1e+16");
        testExpression("9999999999999999 negate - 1 + 1 =", "-9 999 999 999 999 999");
        testExpression("9999999999999999 + ,1 = = = =", "9 999 999 999 999 999");
        testExpression("9999999999999999 + ,1 = = = = =", "1e+16");
        testExpression("9999999999999999 + ,4999999999999999 =", "9 999 999 999 999 999");
        testExpression("9999999999999999 + ,4999999999999999 + ,000000000000001 =", "1e+16");
        //negative power
        testExpression("0,0000000000000001 / 10 =", "1e-17");
        testExpression("0,0000000000000001 / 10 * 10 =", "0,0000000000000001");
        testExpression("0,0000000000000001 negate / 10 =", "-1e-17");
        testExpression("0,0000000000000001 negate / 10 * 10 =", "-0,0000000000000001");
        testExpression("0,0000000000000001 / 10 = MS C 0,0000000000000001 - MR = = = = =", "0,0000000000000001");
        testExpression("0,0000000000000001 / 10 = MS C 0,0000000000000001 - MR = = = = = =", "4e-17");
        testExpression("0,0000000000000001 / 10 = MS C 0,0000000000000001 negate + MR = = = = =", "-0,0000000000000001");
        testExpression("0,0000000000000001 / 10 = MS C 0,0000000000000001 negate + MR = = = = = =", "-4e-17");
    }

    /**
     * Check correct work of add operation
     */
    @Test
    public void addTest() throws Exception {
        //simple
        testExpression("+", "0", "0 +");
        testExpression("+ 2", "2", "0 +");
        testExpression("+ 5 negate =", "-5", "");
        testExpression("2 + 2 =", "4", "");
        testExpression("2 + 2 +", "4", "2 + 2 +");
        testExpression("0,5 + 12 +", "12,5", "0,5 + 12 +");

        //several add
        testExpression("+ + +", "0", "0 +");
        testExpression("2 + 2 + + + +", "4", "2 + 2 +");
        testExpression("55 + + + +", "55", "55 +");
        testExpression("+ 100 negate + + +", "-100", "0 + -100 +");
        testExpression("98 + + 34 + +", "132", "98 + 34 +");
        testExpression("+ + 732 negate + + 901 + +", "169", "0 + -732 + 901 +");

        //add with zero
        testExpression("0 + 0 +", "0", "0 + 0 +");
        testExpression("+ 0 =", "0", "");
        testExpression("78 + 0 +", "78", "78 + 0 +");
        testExpression("93,4 negate + 0 +", "-93,4", "-93,4 + 0 +");
        testExpression("0 + ,5 =", "0,5", "");
        testExpression("0 + 9481 negate +", "-9 481", "0 + -9481 +");

        //add opposite numbers
        testExpression("2 + 2 negate =", "0", "");
        testExpression("2 negate + 2 +", "0", "-2 + 2 +");
        testExpression("32 negate + 32 =", "0", "");
        testExpression("9823 + 9823 negate +", "0", "9823 + -9823 +");
        testExpression(",00001 + ,00001 negate +", "0", "0,00001 + -0,00001 +");
        testExpression("36,6 negate + 36,6 negate +", "-73,2", "-36,6 + -36,6 +");

        //several equals
        testExpression("15 + 5 = =", "25", "");
        testExpression("36 + 4 = = = = = =", "60", "");
        testExpression("1 + = = = = = = = =", "9", "");
        testExpression("+ 5 = = = = = = = = = =", "50", "");
        testExpression("1 + = = = = = + 1 negate = = = = =", "1", "");
        testExpression(",1 negate + = = = + ,1 = = = =", "0", "");

        //add with lowest possible
        testExpression(",0000000000000001 + ,0000000000000001 =", "0,0000000000000002", "");
        testExpression(",0000000000000001 + ,0000000000000001 negate =", "0", "");
        testExpression(",0000000000000001 negate + ,0000000000000001 =", "0", "");
        testExpression(",0000000000000001 + ,0000000000000009 =", "0,000000000000001", "");
        testExpression(",0000000000000001 + ,0000000000000009 negate =", "-0,0000000000000008", "");
        testExpression(",0000000000000001 negate + ,0000000000000009 =", "0,0000000000000008", "");

        //add with highest possible
        testExpression("9999999999999999 + 1 =", "1e+16", "");
        testExpression("9999999999999999 + =", "2e+16", "");
        testExpression("9999999999999999 + 9999999999999999 negate =", "0", "");
        testExpression("9999999999999999 + 8888888888888888 negate =", "1 111 111 111 111 111", "");
        testExpression("1234567890123456 + 8765432109876543 =", "9 999 999 999 999 999", "");
        testExpression("9999999999999999 + ,00000000000001 =", "9 999 999 999 999 999", "");
    }

    /**
     * Check correct work of subtract operation
     */
    @Test
    public void subtractTest() throws Exception {
        //simple
        testExpression("-", "0", "0 -");
        testExpression("1 - =", "0", "");
        testExpression("10 - 2 =", "8", "");
        testExpression("10 - 2 -", "8", "10 - 2 -");
        testExpression("- 5 -", "-5", "0 - 5 -");
        testExpression("1000 - 0,00000000001 =", "999,99999999999", "");

        //several subtracts
        testExpression("- - -", "0", "0 -");
        testExpression("23 - - - - - -", "23", "23 -");
        testExpression("74 negate - - - -", "-74", "-74 -");
        testExpression(",04 - 1,96 - - -", "-1,92", "0,04 - 1,96 -");
        testExpression("78 - - 88 - -", "-10", "78 - 88 -");
        testExpression("- - - 176 - - - 873 - - -", "-1 049", "0 - 176 - 873 -");

        //subtract with zero
        testExpression("0 - 0 =", "0", "");
        testExpression("- 0 -", "0", "0 - 0 -");
        testExpression("1 - 0 -", "1", "1 - 0 -");
        testExpression("36,6 negate - 0 -", "-36,6", "-36,6 - 0 -");
        testExpression("0 - 97 -", "-97", "0 - 97 -");
        testExpression("0 - 5392,783 negate -", "5 392,783", "0 - -5392,783 -");

        //subtract same numbers
        testExpression("108 - 108 =", "0", "");
        testExpression("0,001 - 0,001 =", "0", "");
        testExpression("34 negate - 34 negate =", "0", "");
        testExpression("9873 - 9873 =", "0", "");
        testExpression("45 negate - 45 negate =", "0", "");
        testExpression("91263 - 91263 =", "0", "");

        //subtract opposite numbers
        testExpression("1 - 1 negate =", "2", "");
        testExpression("234 - 234 negate -", "468", "234 - -234 -");
        testExpression("98,7 negate - 98,7 -", "-197,4", "-98,7 - 98,7 -");
        testExpression("528,99712 - 528,99712 negate =", "1 057,99424", "");
        testExpression(",0001 negate - ,0001 =", "-0,0002", "");
        testExpression("9879872 - 9879872 negate =", "19 759 744", "");

        //several equals
        testExpression("18 - 5 = =", "8", "");
        testExpression("18 - 5 = = = = =", "-7", "");
        testExpression("1 - = = = = = = = =", "-7", "");
        testExpression("- 5 = = = = = = = = = =", "-50", "");
        testExpression("1 - = = = = = - 1 negate = = = = =", "1", "");
        testExpression(",1 negate - = = = - ,1 = = = =", "-0,2", "");

        //subtract with lowest possible
        testExpression("0,00000000000001 - 0,00000000000001 =", "0", "");
        testExpression("0,00000000000001 - 0,00000000000001 negate =", "0,00000000000002", "");
        testExpression("0,00000000000001 - 0,00000000000002 =", "-0,00000000000001", "");
        testExpression("0,00000000000001 - 0,00000000000002 negate =", "0,00000000000003", "");
        testExpression(",0000000000000001 negate - ,0000000000000009 =", "-0,000000000000001", "");
        testExpression(",0000000000000001 - ,0000000000000009 =", "-0,0000000000000008", "");

        //subtract with highest possible
        testExpression("9999999999999999 - 9999999999999999 =", "0", "");
        testExpression("9999999999999999 - 9999999999999998 =", "1", "");
        testExpression("9999999999999998 - 9999999999999999 =", "-1", "");
        testExpression("9999999999999999 - 9999999999999999 negate =", "2e+16", "");
        testExpression("1234567890123456 negate - 8765432109876543 =", "-9 999 999 999 999 999", "");
        testExpression("9999999999999999 - ,00000000000001 negate =", "9 999 999 999 999 999", "");
    }

    /**
     * Check correct work of divide operation
     */
    @Test
    public void divideTest() throws Exception {
        //simple
        testExpression("/", "0", "0 ÷");
        testExpression("10 / 2 /", "5", "10 ÷ 2 ÷");
        testExpression("10 negate / 2 =", "-5", "");
        testExpression("10 / 2 negate /", "-5", "10 ÷ -2 ÷");
        testExpression("1 / 3 =", "0,3333333333333333", "");
        testExpression("999 / 3 /", "333", "999 ÷ 3 ÷");

        //divide with zero
        testExpression("0 / 3 -", "0", "0 ÷ 3 -");
        testExpression("0 / 1 =", "0", "");
        testExpression("0 / 178 negate =", "0", "");
        testExpression("0 / 89657757 =", "0", "");
        testExpression("0 / 0,00000000000001 /", "0", "0 ÷ 0,00000000000001 ÷");
        testExpression("0 / 9999999999999999 =", "0", "");

        //divide by one
        testExpression("1 / 1 /", "1", "1 ÷ 1 ÷");
        testExpression("3 / 1 =", "3", "");
        testExpression("1768 / 1 =", "1 768", "");
        testExpression("987342 negate / 1 =", "-987 342", "");
        testExpression(",0000000000000001 / 1 /", "0,0000000000000001", "0,0000000000000001 ÷ 1 ÷");
        testExpression("9999999999999999 / 1 =", "9 999 999 999 999 999", "");

        //divide same numbers
        testExpression("2 / 2 /", "1", "2 ÷ 2 ÷");
        testExpression("987 / 987 =", "1", "");
        testExpression("81 negate / 81 negate /", "1", "-81 ÷ -81 ÷");
        testExpression("981,001234 / 981,001234 =", "1", "");
        testExpression(",90877070701 / ,90877070701 /", "1", "0,90877070701 ÷ 0,90877070701 ÷");
        testExpression("9814309834701987 / 9814309834701987 =", "1", "");

        //several equals
        testExpression("1 / = = = = =", "1", "");
        testExpression("100 / =", "1", "");
        testExpression("81 / 3 = = =", "3", "");
        testExpression("10 / 2 = =", "2,5", "");
        testExpression("10 / 2 = = =", "1,25", "");
        testExpression("625 / 5 = = = = = / ,2 = = = = =", "625", "");

        //several divide
        testExpression("/ / /", "0", "0 ÷");
        testExpression("23 / / / / / /", "23", "23 ÷");
        testExpression("74 negate / / / /", "-74", "-74 ÷");
        testExpression(",04 / 1,96 / / /", "0,0204081632653061", "0,04 ÷ 1,96 ÷");
        testExpression("78 / / 88 / /", "0,8863636363636364", "78 ÷ 88 ÷");
        testExpression("/ / / 176 / / / 873 / / /", "0", "0 ÷ 176 ÷ 873 ÷");

        //divide with lower possible
        testExpression(",0000000000000001 / 10 +", "1e-17", "0,0000000000000001 ÷ 10 +");
        testExpression(",0000000000000001 / 3 =", "3,333333333333333e-17", "");
        testExpression(",0000000000000001 / ,0000000000000001 =", "1", "");
        testExpression(",0000000000000001 / ,0000000000000002 =", "0,5", "");
        testExpression(",0000000000000002 / ,0000000000000001 -", "2", "000000002 ÷ 0,0000000000000001 -");
        testExpression(",0000000000000001 / 9999999999999999 =", "1e-32", "");

        //divide with higher possible
        testExpression("9999999999999999 / 9 =", "1 111 111 111 111 111", "");
        testExpression("9999999999999999 / 7 -", "1 428 571 428 571 428", "9999999999999999 ÷ 7 -");
        testExpression("9999999999999999 / 0,1 =", "9,999999999999999e+16", "");
        testExpression("9999999999999999 / 0,00000000000001 =", "9,999999999999999e+29", "");
        testExpression("9999999999999999 / 9999999999999999 /", "1", "99999999999 ÷ 9999999999999999 ÷");
        testExpression("9999999999999999 / 8888888888888888 +", "1,125", "99999999999 ÷ 8888888888888888 +");
    }

    /**
     * Check correct work of divide operation with errors
     */
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

    /**
     * Check correct work of multiply operation
     */
    @Test
    public void multiplyTest() throws Exception {
        //simple
        testExpression("*", "0", "0 ×");
        testExpression("1 * 1 =", "1", "");
        testExpression("0 * 1 =", "0", "");
        testExpression(",0001 * 10000 =", "1", "");
        testExpression("12345679 * 9 =", "111 111 111", "");
        testExpression("20 * 0,5 negate =", "-10", "");

        //with zero
        testExpression("0 * 0 =", "0", "");
        testExpression("0 * 1 =", "0", "");
        testExpression("0 * 1 negate =", "0", "");
        testExpression("* 0 =", "0", "");
        testExpression("* 5 =", "0", "");
        testExpression("5 * 0 =", "0", "");

        //with one
        testExpression("* 1 =", "0", "");
        testExpression("1 * 1 =", "1", "");
        testExpression("2 * 1 =", "2", "");
        testExpression("2 negate * 1 =", "-2", "");
        testExpression("2,25 * 1 =", "2,25", "");
        testExpression("2,25 negate * 1 =", "-2,25", "");

        //with reverse numbers
        testExpression("2 * 0,5 *", "1", "2 × 0,5 ×");
        testExpression("10 * 0,1 =", "1", "");
        testExpression("25 negate * 0,04 negate *", "1", "-25 × -0,04 ×");
        testExpression("0,0009765625 * 1024 =", "1", "");
        testExpression("1000000 * ,000001 *", "1", "1000000 × 0,000001 ×");
        testExpression("0,000064 * 15625 =", "1", "");

        //several multiply
        testExpression("* * *", "0", "0 ×");
        testExpression("23 * * * * * *", "23", "23 ×");
        testExpression("74 negate * * *", "-74", "-74 ×");
        testExpression(",04 * 1,96 * * *", "0,0784", "0,04 × 1,96 ×");
        testExpression("78 * * 88 * *", "6 864", "78 × 88 ×");
        testExpression("* * * 176 * * * 873 * * *", "0", "0 × 176 × 873 ×");

        //several equals
        testExpression("5 * =", "25", "");
        testExpression("5 * = =", "125", "");
        testExpression("3 * = * =", "81", "");
        testExpression("0,1 * = * 10 =", "0,1", "");
        testExpression("3 * = = =", "81", "");
        testExpression("10 * = = * ,1 = =", "10", "");

        //with highest possible
        testExpression("9999999999999999 * 9999999999999999 =", "9,999999999999998e+31", "");
        testExpression("9999999999999999 * 9999999999999999 negate =", "-9,999999999999998e+31", "");
        testExpression("9999999999999999 * 0,1 =", "999 999 999 999 999,9", "");
        testExpression("9999999999999999 * 10 =", "9,999999999999999e+16", "");
        testExpression("9999999999999999 * 0,00000000000001 =", "99,99999999999999", "");
        testExpression("9999999999999999 * 8888888888888888 *", "8,888888888888887e+31", "99999999999 × 8888888888888888 ×");

        //with lowest possible
        testExpression("0,00000000000001 * 0,00000000000001 =", "1e-28", "");
        testExpression("0,00000000000001 * 0,00000000000001 negate =", "-1e-28", "");
        testExpression(",0000000000000001 * 10 *", "0,000000000000001", "0,0000000000000001 × 10 ×");
        testExpression(",0000000000000001 * 3 =", "0,0000000000000003", "");
        testExpression(",0000000000000001 * ,0000000000000002 =", "2e-32", "");
        testExpression(",0000000000000001 * 9999999999999999 =", "0,9999999999999999", "");
    }

    /**
     * Check correct work of negate operation
     */
    @Test
    public void negateTest() throws Exception {
        //simple
        testExpression("negate", "0");
        testExpression("0 negate", "0");
        testExpression("1 negate", "-1");
        testExpression("1 negate negate", "1");
        testExpression("5 negate 8", "-58");
        testExpression("87,08 negate", "-87,08");

        //several negate
        testExpression("3 negate negate", "3");
        testExpression("2 negate negate negate", "-2");
        testExpression(",01 negate negate", "0,01");
        testExpression("9 negate 7 negate", "97");
        testExpression("negate 3 negate negate 1 negate", "-31");
        testExpression("negate 98 negate negate 12 negate negate negate", "-9 812");

        //with highest possible
        testExpression("9999999999999999 negate", "-9 999 999 999 999 999");
        testExpression("9999999999999999 negate negate", "9 999 999 999 999 999");
        testExpression("9999999999999999 negate negate negate", "-9 999 999 999 999 999");
        testExpression("9999999999999999 negate negate negate negate", "9 999 999 999 999 999");
        testExpression("9999999999999999 negate negate negate negate negate", "-9 999 999 999 999 999");
        testExpression("9999999999999999 negate negate negate negate negate negate", "9 999 999 999 999 999");

        //with lowest possible
        testExpression("0,00000000000001 negate", "-0,00000000000001");
        testExpression("0,00000000000001 negate negate", "0,00000000000001");
        testExpression("0,00000000000001 negate negate negate", "-0,00000000000001");
        testExpression("0,00000000000001 negate negate negate negate", "0,00000000000001");
        testExpression("0,00000000000001 negate negate negate negate negate", "-0,00000000000001");
        testExpression("0,00000000000001 negate negate negate negate negate negate", "0,00000000000001");
    }

    /**
     * Check correct work of inverse operation
     */
    @Test
    public void inverseTest() throws Exception {
        //simple
        testExpression("1 1/", "1", "1/(1)");
        testExpression("1 negate 1/", "-1", "1/(-1)");
        testExpression("2 1/", "0,5", "1/(2)");
        testExpression("2 negate 1/", "-0,5", "1/(-2)");
        testExpression("0,1 negate 1/", "-10", "1/(-0,1)");
        testExpression("0,1 1/", "10", "1/(0,1)");

        //several inverse
        testExpression("10 1/ 1/", "10");
        testExpression("10 1/ 1/ 1/", "0,1");
        testExpression("1 + 5 1/ 1/", "5", "1 + 1/(1/(5))");
        testExpression("77 - 1024 1/ 1/", "1 024", "77 - 1/(1/(1024))");
        testExpression("1000000 1/ 1/ 1/ 1/", "1 000 000", "1/(1/(1/(1/(1000000))))");
        testExpression("98 1/ 1/ - 12 1/ 1/", "12", "1/(1/(98)) - 1/(1/(12))");

        //with lowest possible
        testExpression("0,00000000000001 1/", "100 000 000 000 000", "1/(0,00000000000001)");
        testExpression("0,00000000000001 negate 1/", "-100 000 000 000 000", "1/(-0,00000000000001)");
        testExpression("0,00000000000001 1/ 1/", "0,00000000000001", "1/(1/(0,00000000000001))");
        testExpression("0,00000000000001 1/ 1/ 1/", "100 000 000 000 000", "1/(1/(1/(0,00000000000001)))");
        testExpression("0,00000000000001 negate 1/ 1/ 1/", "-100 000 000 000 000", "1/(1/(1/(-0,00000000000001)))");
        testExpression("0,00000000000001 1/ 1/ 1/ 1/", "0,00000000000001", "1/(1/(1/(1/(0,00000000000001))))");

        //with highest possible
        testExpression("9999999999999999 1/", "0,0000000000000001", "1/(9999999999999999)");
        testExpression("9999999999999999 negate 1/", "-0,0000000000000001", "1/(-9999999999999999)");
        testExpression("9999999999999999 1/ 1/", "1e+16", "1/(1/(9999999999999999))");
        testExpression("9999999999999999 negate 1/ 1/", "-1e+16", "1/(1/(-9999999999999999))");
        testExpression("9999999999999999 1/ 1/ 1/", "0,0000000000000001", "1/(1/(1/(9999999999999999)))");
        testExpression("9999999999999999 negate 1/ 1/ 1/ 1/", "-1e+16", "1/(1/(1/(1/(-9999999999999999))))");
    }

    /**
     * Check correct work of inverse operation with errors
     */
    @Test
    public void inverseErrorTest() throws Exception {
        testErrorExpression("1/", "Cannot divide by zero", "1/(0)");
        testErrorExpression("0 1/", "Cannot divide by zero", "1/(0)");
        testErrorExpression("1 + 0 1/", "Cannot divide by zero", "1 + 1/(0)");
        testErrorExpression("0,00000000000001 + 0 1/", "Cannot divide by zero", "0,00000000000001 + 1/(0)");
        testErrorExpression("9999999999999999 + 0 1/", "Cannot divide by zero", "9999999999999999 + 1/(0)");
        testErrorExpression("2 - 2 = 1/", "Cannot divide by zero", "1/(0)");
        testErrorExpression("24 + 24 negate = 1/", "Cannot divide by zero", "1/(0)");
    }

    /**
     * Check correct work of squaring operation
     */
    @Test
    public void sqrTest() throws Exception {
        //simple
        testExpression("sqr", "0", "sqr(0)");
        testExpression("2 sqr", "4", "sqr(2)");
        testExpression("5 sqr", "25", "sqr(5)");
        testExpression("9 sqr", "81", "sqr(9)");
        testExpression("10 sqr", "100", "sqr(10)");
        testExpression("34 sqr", "1 156", "sqr(34)");

        //with zero
        testExpression("0 sqr", "0", "sqr(0)");
        testExpression("0 sqr sqr", "0", "sqr(sqr(0))");
        testExpression("0 sqr sqr sqr", "0", "sqr(sqr(sqr(0)))");
        testExpression("1 - 1 = sqr", "0", "sqr(0)");
        testExpression("12 - 12 = sqr", "0", "sqr(0)");
        testExpression("976 negate + 976 = sqr", "0", "sqr(0)");

        //with one
        testExpression("1 sqr", "1", "sqr(1)");
        testExpression("1 negate sqr", "1", "sqr(negate(1))");
        testExpression("1 sqr sqr sqr", "1", "sqr(sqr(sqr(1)))");
        testExpression("21 - 20 = sqr", "1", "sqr(1)");
        testExpression("10 / 10 = sqr", "1", "sqr(1)");
        testExpression("5 * 0,2 = sqr", "1", "sqr(1)");

        //several sqr
        testExpression("sqr sqr", "0", "sqr(sqr(0))");
        testExpression("sqr sqr sqr", "0", "sqr(sqr(sqr(0)))");
        testExpression("10 sqr sqr", "10 000", "sqr(sqr(10))");
        testExpression(",001 sqr sqr", "0,000000000001", "sqr(sqr(0,001))");
        testExpression("10 sqr sqr sqr", "100 000 000", "sqr(sqr(sqr(10)))");
        testExpression("125 sqr sqr", "244 140 625", "sqr(sqr(125))");

        //with negative numbers
        testExpression("2 negate sqr", "4", "sqr(negate(2))");
        testExpression("5 negate sqr", "25", "sqr(negate(5))");
        testExpression(",002 negate sqr", "0,000004", "sqr(negate(0,002))");
        testExpression("125 negate sqr sqr", "244 140 625", "sqr(sqr(negate(125)))");
        testExpression("20 - 25 = sqr", "25", "sqr(-5)");
        testExpression("7890 - 66122 = sqr", "3 390 965 824", "sqr(-58232)");

        //with lowest possible
        testExpression("0,0000000000000001 sqr", "1e-32", "sqr(0,0000000000000001)");
        testExpression("0,0000000000000001 negate sqr", "1e-32", "sqr(negate(0,0000000000000001))");
        testExpression("0,0000000000000001 negate sqr sqr", "1e-64", "qr(sqr(negate(0,0000000000000001)))");
        testExpression("0,0000000000000001 sqr sqr", "1e-64", "sqr(sqr(0,0000000000000001))");
        testExpression("0,0000000000000001 negate sqr sqr sqr", "1e-128", "r(sqr(sqr(negate(0,0000000000000001))))");
        testExpression("0,0000000000000001 sqr sqr sqr", "1e-128", "sqr(sqr(sqr(0,0000000000000001)))");

        //with highest possible
        testExpression("9999999999999999 sqr", "9,999999999999998e+31", "sqr(9999999999999999)");
        testExpression("9999999999999999 negate sqr", "9,999999999999998e+31", "sqr(negate(9999999999999999))");
        testExpression("9999999999999999 sqr sqr", "9,999999999999996e+63", "sqr(sqr(9999999999999999))");
        testExpression("9999999999999999 negate sqr sqr", "9,999999999999996e+63", "sqr(sqr(negate(99999999999999990))");
        testExpression("9999999999999999 sqr sqr sqr sqr", "9,999999999999984e+255", "sqr(sqr(sqr(9999999999999999))))");
        testExpression("9999999999999999 negate sqr sqr sqr sqr", "9,999999999999984e+255", "qr(sqr(sqr(negate(9999999999999999)))))");
    }

    /**
     * Check correct work of square root operation
     */
    @Test
    public void sqrtTest() throws Exception {
        //simple
        testExpression("sqrt", "0", "√(0)");
        testExpression("2 sqrt", "1,414213562373095", "√(2)");
        testExpression("4 sqrt", "2", "√(4)");
        testExpression("9 sqrt", "3", "√(9)");
        testExpression("81 sqrt", "9", "√(81)");
        testExpression("100 sqrt", "10", "√(100)");

        //with zero
        testExpression("0 sqrt", "0", "√(0)");
        testExpression("1 - 1 = sqrt", "0", "√(0)");
        testExpression("24 + 24 negate = sqrt", "0", "√(0)");
        testExpression("334 negate + 334 = sqrt", "0", "√(0)");
        testExpression("0 / 23 = sqrt", "0", "√(0)");
        testExpression("55 * 0 = sqrt", "0", "√(0)");

        //with one
        testExpression("1 sqrt", "1", "√(1)");
        testExpression("+ 1 sqrt", "1", "0 + √(1)");
        testExpression("46 + 45 negate = sqrt", "1", "√(1)");
        testExpression("21 - 20 = sqrt", "1", "√(1)");
        testExpression("10 / 10 = sqrt", "1", "√(1)");
        testExpression("5 * ,2 = sqrt", "1", "√(1)");

        //several sqrt
        testExpression("sqrt sqrt", "0", "√(√(0))");
        testExpression("sqrt sqrt sqrt", "0", "√(√(√(0)))");
        testExpression("81 sqrt sqrt", "3", "√(√(81))");
        testExpression("6561 sqrt sqrt sqrt", "3", "√(√(√(6561)))");
        testExpression("0,0081 sqrt sqrt", "0,3", "√(√(0,0081))");
        testExpression("0,00006561 sqrt sqrt sqrt", "0,3", "√(√(√(0,00006561)))");

        //with lowest possible
        testExpression("0,0000000000000001 sqrt", "0,00000001", "√(0,0000000000000001)");
        testExpression("0,0000000000000004 sqrt", "0,00000002", "√(0,0000000000000004)");
        testExpression("0,0000000000000009 sqrt", "0,00000003", "√(0,0000000000000009)");
        testExpression("0,0000000000000001 sqrt sqrt", "0,0001", "√(√(0,0000000000000001))");
        testExpression("0,0000000000000001 sqrt sqrt sqrt", "0,01", "√(√(√(0,0000000000000001)))");
        testExpression("0,0000000000000001 sqrt sqrt sqrt sqrt", "0,1", "√(√(√(√(0,0000000000000001))))");

        //with highest possible
        testExpression("9999999999999999 sqrt", "99 999 999,99999999", "√(9999999999999999)");
        testExpression("8888888888888888 sqrt", "94 280 904,15820633", "√(8888888888888888)");
        testExpression("1111111111111111 sqrt", "33 333 333,33333333", "√(1111111111111111)");
        testExpression("9999999999999999 sqrt sqrt", "9 999,999999999999", "√(√(9999999999999999))");
        testExpression("9999999999999999 sqrt sqrt sqrt", "99,99999999999999", "√(√(√(9999999999999999)))");
        testExpression("9999999999999999 sqrt sqrt sqrt sqrt", "9,999999999999999", "√(√(√(√(9999999999999999))))");
    }

    /**
     * Check correct work of square root operation for negative numbers
     */
    @Test
    public void sqrtErrorTest() throws Exception {
        testExpression("1 negate sqrt", "Invalid input", "√(-1)");
        testExpression("100 negate sqrt", "Invalid input", "√(-100)");
        testExpression("0,09 negate sqrt", "Invalid input", "√(-0,09)");
        testExpression("3 - 8 = sqrt", "Invalid input", "√(-5)");
        testExpression("9999999999999999 negate sqrt", "Invalid input", "√(-9999999999999999)");
        testExpression("0,00000000000001 negate sqrt", "Invalid input", "√(-0,00000000000001)");
    }

    /**
     * Check correct work of percent operation
     */
    @Test
    public void percentTest() throws Exception {
        //simple
        testExpression("%", "0", "");
        testExpression("20 % =", "0", "");
        testExpression("100 + 10 %", "10", "100 + 10");
        testExpression("98 - 34 %", "33,32", "98 - 33,32");
        testExpression("9870 * 120 %", "11 844", "9870 × 11844");
        testExpression("12365 / 99 %", "12 241,35", "12365 ÷ 12241,35");

        //with zero
        testExpression("0 % =", "0", "");
        testExpression("0 + 1 % =", "0", "");
        testExpression("0 + 10 % =", "0", "");
        testExpression("100 + 0 % =", "100", "");
        testExpression("987,88 + 0 % =", "987,88", "");
        testExpression(",0001 negate + 0 % =", "-0,0001", "");

        //with hundred
        testExpression("100 + 100 % =", "200", "");
        testExpression("100 - 100 % =", "0", "");
        testExpression("0,1 + 100 % =", "0,2", "");
        testExpression("0,1 - 100 % =", "0", "");
        testExpression("875453 / 100 % =", "1", "");
        testExpression("75 * 100 % =", "5 625", "");

        //with highest possible
        testExpression("0 + 9999999999999999 % =", "0", "");
        testExpression("9999999999999999 % =", "0", "");
        testExpression("9999999999999999 + 9999999999999999 % =", "1,00000000000001e+30", "");
        testExpression("9999999999999999 + 0 % =", "9 999 999 999 999 999", "");
        testExpression("9999999999999999 * 9999999999999999 % =", "9,999999999999997e+45", "");
        testExpression("9999999999999999 - 9999999999999999 % =", "-9,999999999999898e+29", "");

        //with lowest possible
        testExpression("0 + 0,00000000000001 % =", "0", "");
        testExpression("0,00000000000001 % =", "0", "");
        testExpression(",0000000000000001 + 1 % =", "0,0000000000000001", "");
        testExpression(",0000000000000001 - 1 % =", "0,0000000000000001", "");
        testExpression(",0000000000000001 + 100 % =", "0,0000000000000002", "");
        testExpression(",0000000000000001 + ,0000000000000001 % =", "0,0000000000000001", "");

        //several percent
        testExpression("100 + 10 % % % =", "110", "");
        testExpression("200 + 10 % % =", "240", "");
        testExpression("200 + 10 % % % =", "280", "");
        testExpression("45 + 45 % % =", "54,1125", "");
        testExpression("196 + 240 % % % =", "2 003,08864", "");
        testExpression(",0001 + ,0001 % % =", "0,0001000000000001", "");
    }

    /**
     * Check correct work of backspace operation
     */
    @Test
    public void backspaceTest() throws Exception {
        //simple
        testExpression("back", "0");
        testExpression(", back", "0");
        testExpression(",1 back", "0,");
        testExpression(",1 negate back", "-0,");

        //delete all digits
        testExpression("back", "0");
        testExpression("negate back", "0");
        testExpression("1 back", "0");
        testExpression("45 back back", "0");

        //delete digits with comma
        testExpression("12689,88 back", "12 689,8");
        testExpression("980,98 back back", "980,");
        testExpression("673,12 back back back", "673");
        testExpression("34, back", "34");

        //several backspace
        testExpression("2 negate back", "0");
        testExpression("23 negate back", "-2");
        testExpression("843,44 negate back", "-843,4");
        testExpression("36,6 negate back back back back", "0");

        //after binary
        testExpression("99 - back back", "99");
        testExpression("2 + back", "2");
        testExpression("885,6 negate * back back back", "-885,6");
        testExpression("45 - 40 = back", "5");

        //after unary
        testExpression("100 + 10 % back", "10");
        testExpression("81 sqrt back", "9");
        testExpression("4 negate sqr back back back", "16");
        testExpression("10 1/ back back", "0,1");
    }

    /**
     * Check font font size
     */
    @Test
    public void resizeFontTest() throws Exception {
        //big font size
        testExpression("1", "1", "", 47);
        testExpression("12345678", "12 345 678", "", 47);
        testExpression("797797,55", "797 797,55", "", 47);
        testExpression("822,787687", "822,787687", "", 47);
        testExpression("9999999999999999 + 1 =", "1e+16", "", 47);
        testExpression("9999999999999999 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow", "r(sqr(9999999999999999))))))))))", 47);

        //medium font size
        testExpression("123456789", "123 456 789", "", 35);
        testExpression("12692387928", "12 692 387 928", "", 35);
        testExpression("987927,876868", "987 927,876868", "", 35);
        testExpression("827,8768768768", "827,8768768768", "", 35);
        testExpression("9000 sqr sqr sqr", "4,3046721e+31", "sqr(sqr(sqr(9000)))", 35);
        testExpression("25 negate sqrt", "Invalid input", "√(-25)", 35);

        //low font size
        testExpression("123456789012", "123 456 789 012", "", 21);
        testExpression("9876454687656551", "9 876 454 687 656 551", "", 21);
        testExpression("9,876876876868768", "9,876876876868768", "", 21);
        testExpression("777777777 sqr", "6,049382703950617e+17", "sqr(777777777)", 21);
        testExpression("22 / 0 =", "Cannot divide by zero", "22 ÷", 21);
        testExpression("0 / 0 =", "Result is undefined", "0 ÷", 21);
    }

    /**
     * Check correct work for given expression with combination of different operations
     */
    @Test
    public void combinationTest() throws Exception {
        //binary
        testExpression("5 + 9 - 1 =", "13");
        testExpression("2 * 3 / 4 =", "1,5");
        testExpression("5 negate * 2 / 4 =", "-2,5");
        testExpression("3 + 5 / 2 =", "4");
        testExpression("5 - 1 * 4 =", "16");
        testExpression("3 + 4 - 5 / 2 * 8 =", "8");

        //unary
        testExpression("81 + 3 sqr =", "90", "");
        testExpression("945 - 5 negate sqr +", "920", "945 - sqr(-5) +");
        testExpression("12,99 negate / 10 1/ *", "-129,9", "-12,99 ÷ 1/(10) ×");
        testExpression("6298 * 81 sqrt sqrt - 34 +", "18 860", "6298 × √(√(81)) - 34 +");
        testExpression("103,78 - 10 sqr - 79 = = =", "-233,22", "");
        testExpression("74,882 + 89 sqr sqrt negate * 3 / 2 =", "-21,177", "");

        //mutually inverse
        testExpression("10 / 3 * 3 =", "10");
        testExpression("2 / 3 * 3 =", "2");
        testExpression("1 / 3 * 3 =", "1");
        testExpression("100 / 7 * 7 =", "100");
        testExpression("9999999999999999 + 1 - 1 =", "9 999 999 999 999 999");
        testExpression("87599 sqr + = =", "23 020 754 403");

        //overwriting
        testExpression("76,4 + - ,4 +", "76", "76,4 - 0,4 +");
        testExpression("888 negate - / 111 - +", "-8", "-888 ÷ 111 +");
        testExpression("1024 * + - / 256 + - * * * /", "4", "1024 ÷ 256 ÷");
        testExpression("874,982 / * - 870 + /", "4,982", "874,982 - 870 ÷");
        testExpression("9879879,87876 - * 7654 - - - +", "75 620 600 592,02904", "9879879,87876 × 7654 +");
        testExpression("45 * - 56 + - - + 89,6 * / + * 45 - +", "3 537", "45 - 56 + 89,6 × 45 +");

        // with clear
        testExpression("36,98 - 77 C 45 - 23 -", "22", "45 - 23 -");
        testExpression("46,99 * 5 sqr CE 69,9 /", "3 284,601", "46,99 × sqr(5) ÷");
        testExpression("98 + 3 CE 6 - 45 C 44 - 55 back 9 / +", "-15", "44 - 59 +");
        testExpression("67 negate / 98 sqrt C 53 + 89 sqrt 1/ -", "53,10599978800064", "53 + 1/(√(89)) -");
        testExpression("992 back back sqr + - 55 negate + 489 CE 6 -", "142", "sqr(9) - -55 + 6 -");
        testExpression("38,88 / 66 CE 12 - 98,92 + 44 back *", "-91,68", "38,88 ÷ 12 - 98,92 + 4 ×");

        //with memory operations
        testExpression("75 MS C 888 - 45 + MR -", "918", "888 - 45 + 75 -");
        testExpression("925,77 * 99999 sqr M+ * 99999 M- MR + 98765 sqr sqr -", "9,266752199568381e+22", "× 9999700002 + sqr(sqr(98765)) -");
        testExpression("987654321 sqr sqr M+ M+ C 98 back * MR =", "1,712743695476512e+37", "");
        testExpression("56 MS C 98 + 35,77 MR M+ M- M- =", "154", "");
        testExpression("81 M+ sqrt - 99 * 22 negate - M+ M+ MR", "4 041", "√(81) - 99 × -22 -");
        testExpression("225 sqr M- sqr + 66 / 8 M+ MR", "-50 617", "sqr(sqr(225)) + 66 ÷");

        //with lowest and highest possible
        testExpression(",000000000000001 + ,000000000000001 / ,000000000000001 =", "2");
        testExpression("9999999999999999 + 5 - 4 / 10 =", "1 000 000 000 000 000");
        testExpression("9999999999999999 + 9999999999999999 / 9999999999999999 =", "2");
        testExpression("9999999999999999 * sqr MS + 9876543210 + MR =", "9,999999999999998e+47");
        testExpression(",000000000000001 sqrt / 800 sqr =", "0,0000000000000494");
        testExpression(",000000000000001 * = / 99999999999999 + 34", "34");
    }

    /**
     * Check correct work of operations, the scale of result of which is bigger than 10000
     */
    @Test
    public void overflowTest() throws Exception {
        //sqr
        testExpression("9999999999999999 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
        testExpression("9999999999999999 negate sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
        testExpression(",0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
        testExpression(",0000000000000001 negate sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");

        //divide
        testExpression("1 / 9999999999999999 = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");
        testExpression("1 / 9999999999999999 negate = = = = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");
        testExpression("1 / ,0000000000000001 = = = = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");
        testExpression("1 / ,0000000000000001 negate = = = = = = = = = = = = = = = = = = = MS / MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Overflow");

        //multiply
        testExpression("9999999999999999 * = = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");
        testExpression("9999999999999999 negate * = = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");
        testExpression(",0000000000000001 * = = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");
        testExpression(",0000000000000001 negate * = = = = = = = = = = = = = = = = = = MS * MR = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = ", "Overflow");

        //add
        testExpression("9999999999999999 sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * 9999999999999999 = = = = = = = = = = = = = = = = = +", "Overflow");
        testExpression("9999999999999999 negate sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * 9999999999999999 = = = = = = = = = = = = = = = = = +", "Overflow");
        testExpression(",0000000000000001 sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * ,0000000000000001 = = = = = = = = = = = = = = = = = +", "Overflow");
        testExpression(",0000000000000001 negate sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * ,0000000000000001 = = = = = = = = = = = = = = = = = +", "Overflow");

        //subtract
        testExpression("9999999999999999 sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * 9999999999999999 = = = = = = = = = = = = = = = = = negate - ", "Overflow");
        testExpression("9999999999999999 negate sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * 9999999999999999 = = = = = = = = = = = = = = = = = negate -", "Overflow");
        testExpression(",0000000000000001 sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * ,0000000000000001 = = = = = = = = = = = = = = = = = negate-", "Overflow");
        testExpression(",0000000000000001 negate sqr sqr sqr sqr sqr MS sqr sqr sqr sqr * MR = = = * ,0000000000000001 = = = = = = = = = = = = = = = = = negate -", "Overflow");

    }

    /**
     * Check correct work of memory operations
     */
    @Test
    public void memoryTest() throws Exception {
        //simple
        testExpression("10 MS C MR", "10");
        testExpression(", MS C MR", "0");
        testExpression("0,000000000000001 MS C MR", "0,000000000000001");
        testExpression("9999999999999999 MS C MR", "9 999 999 999 999 999");
        testExpression("9999999999999999 + 1 = MS C MR", "1e+16");
        testExpression(",0000000000000001 / 10 = MS C MR", "1e-17");

        //add
        testExpression("965 M+ + 123 MR", "965");
        testExpression("50 M+ M+ M+ MR", "150");
        testExpression("23 M+ negate M+ MR", "0");
        testExpression("67 negate M+ negate M+ MR", "0");
        testExpression(",000000000000001 M+ sqr sqr MR", "0,000000000000001");
        testExpression("9999999999999999 negate M+ 1/ MR", "-9 999 999 999 999 999");

        //subtract
        testExpression("4723399 M- C 2377 M- MR", "-4 725 776");
        testExpression("98 M- C MR", "-98");
        testExpression("98 negate M- C MR", "98");
        testExpression("125 M- M- M- M- MR", "-500");
        testExpression(",000000000000001 negate M- * 85 MR", "0,000000000000001");
        testExpression("9999999999999999 M- sqrt + 67 MR", "-9 999 999 999 999 999");

        //clear
        testExpression("32453 MS C MC 2377 M- MR", "-2 377");
        testExpression("98 M- C MC 23 MS MR", "23");
        testExpression("4524 negate M- C MC 22 M- MR", "-22");
        testExpression("125 M- M- M- M- MC MS MR", "125");
        testExpression(",000000000000001 negate M+ C 85 M+ MR", "85");
        testExpression("9999999999999999 M- sqrt MC CE 67 M- MR", "-67");

        //with other operations
        testExpression("98 M- C 98 M+ MR", "0");
        testExpression("9999999999999999 M+ M- M+ M- MR", "0");
        testExpression(",000000000000001 negate M- M+ M- M+ M- M+ MR", "0");
        testExpression("9999999999999999 sqr M+ C MR sqrt", "9 999 999 999 999 999");
        testExpression(",000000000000001 1/ M+ C MR 1/", "0,000000000000001");
        testExpression("10 M- / 3 * 3 = M+ MR", "0");
        testExpression("1234 M+ - 234 = M- MR", "234");
    }

    /**
     * Check correct work of clear entry operation
     */
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

    /**
     * Check correct work of clear operation
     */
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

    /**
     * Check correct work of alternative buttons and mouse click
     */
    @Test
    public void mouseClickAndAlternativeTest() throws Exception {
        //binary
        testAlternativeClick("32 - 4 =", "28", "");
        testAlternativeClick("1234 + 4321 -", "5 555", "1234 + 4321 -");
        testAlternativeClick("8,905 * 345 *", "3 072,225", "8,905 × 345 ×");
        testAlternativeClick("6555 / 30 /", "218,5", "6555 ÷ 30 ÷");

        //clear
        testAlternativeClick("12345 back back", "123", "");
        testAlternativeClick("78 - 985 - 66 C", "0", "");
        testAlternativeClick("78 - 985 - 66 CE", "0", "78 - 985 -");

        //unary
        testAlternativeClick("1000 + 55 %", "550", "1000 + 550");
        testAlternativeClick("81 sqrt sqrt", "3", "√(√(81))");
        testAlternativeClick("2 sqr sqr sqr", "256", "sqr(sqr(sqr(2)))");
        testAlternativeClick("777 negate negate negate", "-777", "");
        testAlternativeClick("100 1/ 1/ 1/", "0,01", "1/(1/(1/(100)))");

        //memory
        testAlternativeClick("91 MS + 12345 MR", "91", "91 +");
        testAlternativeClick("48 negate M+ negate M+ MR", "0", "");
        testAlternativeClick("654,78 M- C MR", "-654,78", "");
        testAlternativeClick("653 MS C MC", "0", "");
    }

    /**
     * Check correct display of history field, when history length is bigger than length of history field
     */
    @Test
    public void historyOverflowTest() throws Exception {
        //simple
        testExpression("123 negate + 889 - 7643 / 2357 * sqr <", "8,512926497930231", "-123 + 889 - 7643 ÷ 2357 × sqr(-2");
        testExpression("87,999 sqrt + 65 sqr sqr - 89,0076 + ,0011 1/ - 849 * 382999 + < > < >", "6 836 764 042 139,976", "076 + 1/(0,0011) - 849 × 382999 +");
        testExpression("14374,99 * 65438,01 - 34 1/ 1/ 1/ sqr + 88 < -", "940 670 827,3690349", ",01 - sqr(1/(1/(1/(34)))) + 88 -");

        //with highest possible
        testExpression("9999999999999999 + 9999999999999999 + 9999999999999999 +", "3e+16", "99999999999 + 9999999999999999 +");
        testExpression("9999999999999999 * 9999999999999999 * 9999999999999999 + <", "9,999999999999997e+47", "9999999999999999 × 99999999999999");
        testExpression("9999999999999999 / 9999999999999999 - 9999999999999999 + < >", "-9 999 999 999 999 998", "999999999999 - 9999999999999999 +");

        //with lowest possible
        testExpression(",0000000000000001 + ,0000000000000001 - ,0000000000000001 + < > < > < >", "0,0000000000000001", "0000000001 - 0,0000000000000001 +");
        testExpression(",0000000000000001 * ,0000000000000001 + ,0000000000000001 / <", "0,0000000000000001", "0,0000000000000001 × 0,0000000000");
        testExpression(",0000000000000001 sqr - ,0000000000000001 sqrt * ,0000000000000001 1/ + < > < > <", "-100 000 000", "00000000000001) - √(0,00000000000");
    }

    /**
     * Parsing given expression to commands array and push given commands.
     * Check of result of pushing commands for numeric field
     *
     * @param expression given expression with commands
     * @param expected   expected numeric field value
     */
    private void testExpression(String expression, String expected) throws Exception {
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);
        isMemoryDisable = true;

        for (String item : expression.split(" ")) {
            pushButton(item, false);
        }

        Label numericDisplay = GuiTest.find("#numericField");
        String actualValue = numericDisplay.getText();
        assertEquals(expected, actualValue);
    }

    /**
     * Parsing given expression for alternative buttons and mouse commands array and push given commands.
     * Check of result of pushing commands for numeric and history fields
     *
     * @param expression given expression with commands
     * @param numeric    expected numeric field value
     * @param history    expected history field value
     */
    private void testAlternativeClick(String expression, String numeric, String history) throws Exception {
        testAlternativeButtons(expression, numeric, history);
        testMouseClick(expression, numeric, history);
    }

    /**
     * Parsing given expression for alternative buttons commands array and push given commands.
     * Check of result of pushing commands for numeric and history fields
     *
     * @param expression given expression with commands
     * @param numeric    expected numeric field value
     * @param history    expected history field value
     */
    private void testAlternativeButtons(String expression, String numeric, String history) throws Exception {
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);
        isMemoryDisable = true;

        for (String item : expression.split(" ")) {
            pushButton(item, true);
        }

        Label numericDisplay = GuiTest.find("#numericField");
        String actualValue = numericDisplay.getText();
        assertEquals(numeric, actualValue);

        Label historyDisplay = GuiTest.find("#historyField");
        actualValue = historyDisplay.getText();
        assertEquals(history, actualValue);
    }

    /**
     * Parsing given expression to commands array and push given commands.
     * Check of result of pushing commands for numeric and history fields
     *
     * @param expression given expression with commands
     * @param numeric    expected numeric field value
     * @param history    expected history field value
     */
    private void testExpression(String expression, String numeric, String history) throws Exception {
        testExpression(expression, numeric);
        Label historyDisplay = GuiTest.find("#historyField");
        String actualValue = historyDisplay.getText();
        assertEquals(history, actualValue);
    }

    /**
     * Parsing given expression for mouse commands array and push given commands.
     * Check of result of pushing commands for numeric and history fields
     *
     * @param expression given expression with commands
     * @param numeric    expected numeric field value
     * @param history    expected history field value
     */
    private void testMouseClick(String expression, String numeric, String history) throws Exception {
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);
        isMemoryDisable = true;

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

    /**
     * Check is enable or disable all buttons in array
     *
     * @param disable true if buttons must be disable, false otherwise
     */
    private void disableButtonTest(boolean disable) {
        for (Button button : disabled()) {
            assertEquals("Wrong flag for " + button.getId(), disable, button.isDisable());
        }
    }

    /**
     * Check is enable or disable memory buttons in array
     *
     * @param disable true if buttons must be disable, false otherwise
     */
    private void disableMemoryButtonTest(boolean disable) {
        for (Button button : memoryDisabled()) {
            assertEquals("Wrong flag for " + button.getId(), disable, button.isDisable());
        }
    }

    /**
     * Create list of memory buttons which are mus be disabled after MC operation
     *
     * @return  list of buttons
     */
    private ArrayList<Button> memoryDisabled() {
        ArrayList<Button> disabled = new ArrayList<>();
        disabled.add(GuiTest.find("#memory_clear"));
        disabled.add(GuiTest.find("#memory_recall"));
        return disabled;
    }

    /**
     * Push button using item key.
     * If alternative flag is true for push used alternative combinations of buttons if they exist.
     * Check statement of memory buttons.
     *
     * @param item        button key
     * @param alternative true if used alternative buttons combination, false otherwise
     */
    private void pushButton(String item, boolean alternative) {
        disableMemoryButtonTest(isMemoryDisable);
        KeyCode first = KeyCode.UNDEFINED;
        KeyCode second = KeyCode.UNDEFINED;
        String fxId = "";

        if (item.equals("=")) {
            first = alternative ? KeyCode.EQUALS : KeyCode.ENTER;
        } else if (item.equals("back")) {
            first = KeyCode.BACK_SPACE;
        } else if (item.equals("+")) {
            if (alternative) {
                first = KeyCode.ADD;
            } else {
                first = KeyCode.SHIFT;
                second = KeyCode.EQUALS;
            }
        } else if (item.equals("-")) {
            first = alternative ? KeyCode.SUBTRACT : KeyCode.MINUS;
        } else if (item.equals("/")) {
            first = alternative ? KeyCode.DIVIDE : KeyCode.SLASH;
        } else if (item.equals("*")) {
            if (alternative) {
                first = KeyCode.MULTIPLY;
            } else {
                first = KeyCode.SHIFT;
                second = KeyCode.DIGIT8;
            }
        } else if (item.equals("negate")) {
            first = KeyCode.F9;
        } else if (item.equals("1/")) {
            first = KeyCode.R;
        } else if (item.equals("CE")) {
            first = KeyCode.DELETE;
        } else if (item.equals("C")) {
            first = KeyCode.ESCAPE;
        } else if (item.equals("sqr")) {
            first = KeyCode.Q;
        } else if (item.equals("sqrt")) {
            first = KeyCode.SHIFT;
            second = KeyCode.DIGIT2;
        } else if (item.equals("%")) {
            first = KeyCode.SHIFT;
            second = KeyCode.DIGIT5;
        } else if (item.equals("MS")) {
            first = KeyCode.CONTROL;
            second = KeyCode.M;
            isMemoryDisable = false;
        } else if (item.equals("MR")) {
            first = KeyCode.CONTROL;
            second = KeyCode.R;
        } else if (item.equals("MC")) {
            first = KeyCode.CONTROL;
            second = KeyCode.L;
            isMemoryDisable = true;
        } else if (item.equals("M+")) {
            first = KeyCode.CONTROL;
            second = KeyCode.P;
            isMemoryDisable = false;
        } else if (item.equals("M-")) {
            first = KeyCode.CONTROL;
            second = KeyCode.Q;
            isMemoryDisable = false;
        } else if (item.equals("<")) {
            fxId = "#left";
        } else if (item.equals(">")) {
            fxId = "#right";
        }

        if (first != KeyCode.UNDEFINED) {
            if (second != KeyCode.UNDEFINED) {
                controller.push(first, second);
            } else {
                controller.push(first);
            }
        } else if (!fxId.isEmpty()) {
            controller.click((Button) GuiTest.find(fxId));
        } else {
            pushDigitButtons(item, alternative);
            disableMemoryButtonTest(isMemoryDisable);
        }
    }

    /**
     * Push digits buttons using number key.
     * If alternative flag is true for push used alternative combinations of buttons if they exist.
     * Check statement of memory buttons.
     *
     * @param number      number key
     * @param alternative true if used alternative buttons combination, false otherwise
     */
    private void pushDigitButtons(String number, boolean alternative) {
        char[] digits = number.toCharArray();

        for (char item : digits) {
            disableMemoryButtonTest(isMemoryDisable);
            KeyCode code = KeyCode.UNDEFINED;

            if (item == '0') {
                code = alternative ? KeyCode.DIGIT0 : KeyCode.NUMPAD0;
            } else if (item == '1') {
                code = alternative ? KeyCode.DIGIT1 : KeyCode.NUMPAD1;
            } else if (item == '2') {
                code = alternative ? KeyCode.DIGIT2 : KeyCode.NUMPAD2;
            } else if (item == '3') {
                code = alternative ? KeyCode.DIGIT3 : KeyCode.NUMPAD3;
            } else if (item == '4') {
                code = alternative ? KeyCode.DIGIT4 : KeyCode.NUMPAD4;
            } else if (item == '5') {
                code = alternative ? KeyCode.DIGIT5 : KeyCode.NUMPAD5;
            } else if (item == '6') {
                code = alternative ? KeyCode.DIGIT6 : KeyCode.NUMPAD6;
            } else if (item == '7') {
                code = alternative ? KeyCode.DIGIT7 : KeyCode.NUMPAD7;
            } else if (item == '8') {
                code = alternative ? KeyCode.DIGIT8 : KeyCode.NUMPAD8;
            } else if (item == '9') {
                code = alternative ? KeyCode.DIGIT9 : KeyCode.NUMPAD9;
            } else if (item == ',') {
                code = KeyCode.COMMA;
            }

            controller.push(code);
            disableMemoryButtonTest(isMemoryDisable);
        }
    }

    /**
     * Click on buttons using mouse and button key.
     * Check statement of memory buttons.
     *
     * @param item button key
     */
    private void clickButton(String item) {
        disableMemoryButtonTest(isMemoryDisable);
        String fxId = "";

        if (item.equals("=")) {
            fxId = "#equals";
        } else if (item.equals("back")) {
            fxId = "#backspace";
        } else if (item.equals("+")) {
            fxId = "#add";
        } else if (item.equals("-")) {
            fxId = "#subtract";
        } else if (item.equals("/")) {
            fxId = "#divide";
        } else if (item.equals("*")) {
            fxId = "#multiply";
        } else if (item.equals("negate")) {
            fxId = "#negate";
        } else if (item.equals("1/")) {
            fxId = "#inverse";
        } else if (item.equals("CE")) {
            fxId = "#clear_expr";
        } else if (item.equals("C")) {
            fxId = "#clear";
        } else if (item.equals("sqr")) {
            fxId = "#sqr";
        } else if (item.equals("sqrt")) {
            fxId = "#sqrt";
        } else if (item.equals("%")) {
            fxId = "#percent";
        } else if (item.equals("MS")) {
            fxId = "#memory_store";
            isMemoryDisable = false;
        } else if (item.equals("MR")) {
            fxId = "#memory_recall";
            disableMemoryButtonTest(false);
        } else if (item.equals("MC")) {
            fxId = "#memory_clear";
            isMemoryDisable = true;
        } else if (item.equals("M+")) {
            fxId = "#memory_add";
            isMemoryDisable = false;
        } else if (item.equals("M-")) {
            fxId = "#memory_minus";
            isMemoryDisable = false;
        }

        if (fxId.isEmpty()) {
            clickDigitButtons(item);
        } else {
            controller.click((Button) GuiTest.find(fxId));
            disableMemoryButtonTest(isMemoryDisable);
        }
    }

    /**
     * Push digits buttons using mouse and number key.
     * Check statement of memory buttons.
     *
     * @param number number key
     */
    private void clickDigitButtons(String number) {
        char[] digits = number.toCharArray();

        for (char item : digits) {
            disableMemoryButtonTest(isMemoryDisable);
            String fxId = "";

            if (item == '0') {
                fxId = "#zero";
            } else if (item == '1') {
                fxId = "#one";
            } else if (item == '2') {
                fxId = "#two";
            } else if (item == '3') {
                fxId = "#three";
            } else if (item == '4') {
                fxId = "#four";
            } else if (item == '5') {
                fxId = "#five";
            } else if (item == '6') {
                fxId = "#six";
            } else if (item == '7') {
                fxId = "#seven";
            } else if (item == '8') {
                fxId = "#eight";
            } else if (item == '9') {
                fxId = "#nine";
            } else if (item == ',') {
                fxId = "#comma";
            }

            controller.click((Button) GuiTest.find(fxId));
            disableMemoryButtonTest(isMemoryDisable);
        }
    }

    /**
     * Create list of buttons which are must be disabled when errors occur
     *
     * @return list of buttons
     */
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

    /**
     * Check numeric field, history field and buttons statement when error was occur
     *
     * @param expression given expression with commands
     * @param numeric    expected numeric field value
     * @param history    expected history field value
     */
    private void testErrorExpression(String expression, String numeric, String history) throws Exception {
        controller.push(KeyCode.ESCAPE);
        disableButtonTest(false);
        testExpression(expression, numeric, history);
        disableButtonTest(true);
    }

    /**
     * Parsing given expression to commands array and push given commands.
     * Check of result of pushing commands for numeric and history fields.
     * Check font size of numeric field depending on the count of chars in field
     *
     * @param expression given expression with commands
     * @param numeric    expected numeric field value
     * @param history    expected history field value
     * @param font       expected numeric field font size
     */
    private void testExpression(String expression, String numeric, String history, int font) throws Exception {
        testExpression(expression, numeric, history);
        Label numericDisplay = GuiTest.find("#numericField");
        assertEquals(font, numericDisplay.getFont().getSize(), 0.1);
    }
}
