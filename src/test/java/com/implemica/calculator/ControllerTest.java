package com.implemica.calculator;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

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
        testExpression("0", "0");
        testExpression("1", "1");
        testExpression("2", "2");
        testExpression("3", "3");
        testExpression("4", "4");
        testExpression("5", "5");
        testExpression("6", "6");
        testExpression("7", "7");
        testExpression("8", "8");
        testExpression("9", "9");
        testExpression("1234567890", "1 234 567 890");
        testExpression("1234567890123456", "1 234 567 890 123 456");
        testExpression("1234567890123456 negate", "-1 234 567 890 123 456");
        testExpression("1234567890,123456", "1 234 567 890,123456");
        testExpression("1234567890,123456 negate", "-1 234 567 890,123456");
        testExpression("12345678901234567890", "1 234 567 890 123 456");
    }

    @Test
    public void scientificTest() throws Exception {
        testExpression("9999999999999999 + 1 =", "1,e+16");
        testExpression("9999999999999999 + 1 - 1 =", "9 999 999 999 999 999");
        testExpression("0,000000000000001 / 10 =", "1,e-16");
        //testExpression("0,00000000000001 / 10 = * 10 =", "0,00000000000001");
    }

    @Test
    public void plusTest() throws Exception {
        testExpression("2 + 2 =", "4", "");
        testExpression("2 + 2 +", "4", "2 + 2 +");
        testExpression("2 + 2 + + + +", "4", "2 + 2 +");
        testExpression("2 + + + +", "2", "2 +");
        testExpression("2 + 2 negate =", "0", "");
        testExpression("12 negate + 11 +", "-1", "negate(12) + 11 +");
        testExpression("32 negate + 32 =", "0", "");
        testExpression("+ 100 negate", "-100", "0 + negate(100)");
        testExpression("1 + = = = = = = = =", "9", "");
        testExpression("+ 5 = = = = = = = = = =", "50", "");
        testExpression("+ 5 negate =", "-5", "");
        testExpression("0,5 + 12 +", "12,5","0,5 + 12 +");
        testExpression("12345,6789 + 98765,4321 =", "111 111,111", "");
        testExpression("9999999999999999 + 1 =", "1,e+16", "");
        testExpression(",00000000000001 + ,00000000000001 =", "0,00000000000002", "");
        testExpression(",00000000000001 + ,00000000000001 negate =", "0", "");
        testExpression(",00000000000001 + ,00000000000009 =", "0,0000000000001", "");
        testExpression(",00000000000001 + ,00000000000009 negate =", "-0,00000000000008", "");
        testExpression("9999999999999999 + =", "2,e+16", "");
        testExpression("999,999999999999 + ,000000000001 =", "1 000", "");
        testExpression("9999999999999999 + ,1 =", "9 999 999 999 999 999", "");
        testExpression("9999999999999999 + ,1 = = = =", "9 999 999 999 999 999", "");
        testExpression("9999999999999999 + ,1 = = = =", "9 999 999 999 999 999", "");
    }

    @Test
    public void minusTest() throws Exception {
        testExpression("10 - 2 =", "8", "");
        testExpression("10 - 2 -", "8", "10 - 2 -");
        testExpression("2 - 0 -", "2", "2 - 0 -");
        testExpression("0 - 45 =", "-45", "");
        testExpression("18 - 5 = =", "8", "");
        testExpression("18 - 5 = = = = =", "-7", "");
        testExpression(",04 - 1,96 - - -", "-1,92", "0,04 - 1,96 -");
        testExpression("1000 - 0,00000000001 =", "999,99999999999", "");

        testExpression("0,00000000000001 - 0,00000000000001 =", "0", "");
        testExpression("0,00000000000001 - 0,00000000000001 negate =", "0,00000000000002", "");
        testExpression("0,00000000000001 - 0,00000000000002 =", "-0,00000000000001", "");
        testExpression("0,00000000000001 - 0,00000000000002 negate =", "0,00000000000003", "");

        testExpression("9999999999999999 - 9999999999999999 =", "0", "");
        testExpression("9999999999999999 - 9999999999999998 =", "1", "");
        testExpression("9999999999999998 - 9999999999999999 =", "-1", "");
        testExpression("9999999999999999 - 9999999999999999 negate =", "2,e+16", "");
        testExpression("9999999999999999 - 1 negate =", "1,e+16", "");
        testExpression("9999999999999999 - 1 negate - 1 =", "9 999 999 999 999 999", "");
    }

    @Test
    public void divideTest() throws Exception {
        testExpression("0 / 3 -", "0", "0 ÷ 3 -");
        testExpression("0 / 1 =", "0", "");
        testExpression("0 / 0,00000000000001 /", "0", "0 ÷ 0,00000000000001 ÷");
        testExpression("0 / 9999999999999999 =", "0", "");

        testExpression("1 / 1 /", "1", "1 ÷ 1 ÷");
        testExpression("3 / 1 =", "3", "");
        testExpression("0,00000000000001 / 1 /", "0,00000000000001", "0,00000000000001 ÷ 1 ÷");
        testExpression("9999999999999999 / 1 =", "9 999 999 999 999 999", "");

        testExpression("0,00000000000002 / 2 /", "0,00000000000001", "0,00000000000002 ÷ 2 ÷");
        testExpression("0,00000000000001 / 2 =", "0,000000000000005", "");
        testExpression("0,00000000000001 / 10 +", "0,000000000000001", "0,00000000000001 ÷ 10 +");
        testExpression("0,00000000000001 / 3 =", "3,333333333333333e-15", "");
        testExpression("0,00000000000001 / 0,00000000000001 =", "1", "");
        testExpression("0,00000000000001 / 0,00000000000002 =", "0,5", "");
        testExpression("0,00000000000002 / 0,00000000000001 -", "2", "0,00000000000002 ÷ 0,00000000000001 -");
        testExpression("0,00000000000001 / 9999999999999999 =", "1,e-30", "");

        testExpression("9999999999999999 / 2 +", "5 000 000 000 000 000", "9999999999999999 ÷ 2 +");
        testExpression("9999999999999999 / 9 =", "1 111 111 111 111 111", "");
        testExpression("9999999999999999 / 7 -", "1,428571428571428e+15", "9999999999999999 ÷ 7 -");
        testExpression("9999999999999999 / 0,1 =", "9,999999999999999e+16", "");
        testExpression("9999999999999999 / 0,00000000000001 =", "9,999999999999999e+29", "");
        testExpression("9999999999999999 / 9999999999999999 /", "1", "9999999999999999 ÷ 9999999999999999 ÷");
        testExpression("9999999999999999 / 8888888888888888 +", "1,125", "9999999999999999 ÷ 8888888888888888 +");
        testExpression("9999999999999999 negate / 8888888888888888 =", "-1,125", "");

        testExpression("10 / 2 /", "5", "10 ÷ 2 ÷");
        testExpression("10 negate / 2 =", "-5", "");
        testExpression("10 / 2 negate /", "-5", "10 ÷ negate(2) ÷");
        testExpression("1 / 3 =", "3,333333333333333e-1", "");
        testExpression("10 / 2 = =", "2,5", "");
        testExpression("10 / 2 = = =", "1,25", "");

        testExpression("10 / 5 / 2 /", "1", "10 ÷ 5 ÷ 2 ÷");
        testExpression("15 / 6 / 8 / 0,4 /", "0,78125", "15 ÷ 6 ÷ 8 ÷ 0,4 ÷");
    }

    @Test
    public void divideErrorTest() throws Exception{
        testExpression("0 / 0 =", "Result is undefined");
        testExpression("21328 / 0 =", "Cannot divide by zero");
        testExpression("3 / 0 =","Cannot divide by zero");
        testExpression("9999999999999999 / 0 =","Cannot divide by zero");
        testExpression("0,00000000000001 / 0 =","Cannot divide by zero");
        testExpression("3 negate / 0 =","Cannot divide by zero");
        testExpression("9999999999999999 negate / 0 =","Cannot divide by zero");
        testExpression("0,00000000000001 negate / 0 =","Cannot divide by zero");
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
        testExpression("* 0,00000000000001 =",  "0");
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
        testExpression("9999999999999999 * 1 =","9 999 999 999 999 999");
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
        testExpression("0,00000000000001 * 0,00000000000001 =", "1,e-28");
        testExpression("0,00000000000001 * 0,00000000000001 negate =", "-1,e-28");

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
        testExpression("0,00000000000001 negate negate =" ,"0,00000000000001");

        testExpression("5 + 0 negate =", "5");
        testExpression("1 + 1 negate =", "0");
        testExpression("1 - 1 negate =", "2");
        testExpression("0,1 + 0,1 negate =", "0");
        testExpression("0,1 - 0,1 negate =", "0,2");
        testExpression("1 + 9999999999999999 negate =", "-9 999 999 999 999 998");
        testExpression("1 + - 9999999999999999 negate =", "1,e+16");
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

        testExpression("9999999999999999 1/", "1,e-16");
        testExpression("9999999999999999 negate 1/", "-1,e-16");
        testExpression("0,00000000000001 1/", "100 000 000 000 000");
        testExpression("0,00000000000001 negate 1/", "-100 000 000 000 000");
        testExpression("0,00000000000001 1/ 1/", "0,00000000000001");

        testExpression("5 1/","0,2", "1/(5)");
        testExpression("5 1/ 1/", "5", "1/(1/(5))");
        testExpression("1 + 5 1/","0,2", "1 + 1/(5)");
        testExpression("1 + 5 1/ 1/","5", "1 + 1/(1/(5))");
    }

    @Test
    public void inverseErrorTest() throws Exception{
        testExpression("0 1/", "Cannot divide by zero", "1/(0)");
        testExpression("1 + 0 1/", "Cannot divide by zero", "1 + 1/(0)");
    }

    @Test
    public void sqrTest() throws Exception {
        testExpression("1 sqr", "1");
        testExpression("1 negate sqr", "1");
        testExpression("1 sqr sqr sqr", "1");
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
    }

    @Test
    public void sqrtTest() throws Exception {
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
        testExpression("9 sqrt =","3", "");
        testExpression("9 sqrt + 1","1", "√(9) +");
    }

    @Test
    public void sqrtErrorTest() throws Exception{
        testExpression("1 negate sqrt", "Invalid input", "√(negate(1))");
        testExpression("100 negate sqrt", "Invalid input", "√(negate(100))");
        testExpression("0,09 negate sqrt", "Invalid input", "√(negate(0,09))");
        testExpression("3 - 8 = sqrt","Invalid input", "√(-5)");
        testExpression("9999999999999999 negate sqrt", "Invalid input", "√(negate(9999999999999999))");
        testExpression("0,00000000000001 negate sqrt", "Invalid input", "√(negate(0,00000000000001))");
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
        testExpression("9999999999999999 + 0,00000000000001 % =", "1,e+16");

        testExpression("0,00000000000001 + 1 % =", "0,00000000000001");
        testExpression("0,00000000000001 - 1 % =", "9,9e-15");
        testExpression("0,00000000000001 - 100 % =", "0");
        testExpression("0,00000000000001 + 100 % =", "0,00000000000002");
        testExpression("0,00000000000001 + 0,00000000000001 % =", "1,e-14");

        testExpression("0 + 1 % =", "0");
        testExpression("0 + 10 % =", "0");
        testExpression("0 + 9999999999999999 % =", "0");
        testExpression("0 + 0,00000000000001 % =", "0");

        testExpression("0 % =", "0");
        testExpression("20 % =" ,"0");
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
        testExpression("10 / 3 * 3 =", "10");
        testExpression("2 / 3 * 3 =", "2");
        testExpression("1 / 3 * 3 =", "1");
        testExpression("9999999999999999 + 1 - 1 =", "9 999 999 999 999 999");
        testExpression("87599 sqr + = =", "23 020 754 403");

        testExpression("5 + 9 - 1 =", "13");
        testExpression("5 - 9 + 5 - 2 =", "-1");
        testExpression("2 * 3 / 4 =", "1,5");
        testExpression("5 negate * 2 / 4 =", "-2,5");
        testExpression("3 + 5 / 2 =", "4");
        testExpression("5 - 1 * 4 =", "16");
        testExpression("3 + 4 - 5 / 2 * 8 =", "8");

        testExpression("0,00000000000001 + 0,00000000000001 / 0,00000000000001 =", "2");
        testExpression("9999999999999999 + 5 - 4 / 10 =", "1 000 000 000 000 000");
        testExpression("9999999999999999 + 9999999999999999 / 9999999999999999 =", "2");

        testExpression("9 sqrt + 5 =", "8");
        testExpression("5 + 9 sqrt =", "8");
        testExpression("5 + 81 sqrt sqrt =", "8");
        testExpression("81 sqrt sqrt + 5 =", "8");

        testExpression("5 1/ + 1 =", "1,2");
        testExpression("1 + 5 1/ =", "1,2");
    }

    @Test
    public void memoryTest() throws Exception {
        testExpression("10 MS C MR", "10");
        testExpression("965 M+ + 123 MR", "965");
        testExpression("3765 M+ 78 M+ MR", "3 843");
        testExpression("4723399 M- 2377 M- MR", "-4 725 776");
        testExpression("98 M- C MR", "-98");
        testExpression("98 negate M- C MR", "98");
        testExpression("98 M- C 98 M+ C MR", "0");
    }

    @Test
    public void clearEntryTest() throws Exception{
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
        testExpression("34 + 0,00000000000001 CE 0,5","0,5", "34 +");
        testExpression("1 + 9999999999999999 + 1 + CE 1,5","1,5", "1 + 9999999999999999 + 1 +");

    }

    @Test
    public void clearTest() throws Exception {
        testExpression("5 C", "0", "" );
        testExpression("9999999999999999 C", "0", "");
        testExpression("0,00000000000001 C", "0", "");
        testExpression("9999999999999999 + 199879 = C", "0", "");

        testExpression("199 + 387 C", "0", "");
        testExpression("1 + 9999999999999999 C","0", "");
        testExpression("1 + 0,00000000000001 C", "0", "");
        testExpression("1 + 9999999999999999 + 1 + C", "0", "");

        testExpression("15 + 3555 C 5", "5", "");
        testExpression("1 + 9999999999999999 C 76543","76 543", "");
        testExpression("1 + 0,00000000000001 C 0,5", "0,5", "");
        testExpression("1 + 9999999999999999 + 1 + C 1,5", "1,5", "");
    }

    @Test
    public void moseClickTest() throws Exception {
        testMouseClick("32 - 4 =" ,"28", "");
        testMouseClick("1234 + 4321 -", "5 555", "1234 + 4321 -");
    }

    private void testExpression(String expression, String expected) throws Exception{
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);

        for (String item : expression.split(" ")) {
            pushButton(item, false);
        }

        Label numericDisplay = GuiTest.find("#numericField");
        String actualValue = numericDisplay.getText();
        assertEquals(expected, actualValue);

        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);

        for (String item : expression.split(" ")) {
            pushButton(item, true);
        }

        numericDisplay = GuiTest.find("#numericField");
        actualValue = numericDisplay.getText();
        assertEquals(expected, actualValue);
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

    private void pushButton(String item, boolean alternative) {
        switch (item) {
            case "=":
                controller.push(alternative ? KeyCode.EQUALS : KeyCode.ENTER);
                break;
            case "back":
                controller.push(KeyCode.BACK_SPACE);
                break;
            case "+":
                controller.push(alternative ? KeyCode.ADD : KeyCode.SHIFT, KeyCode.EQUALS);
                break;
            case "-":
                controller.push(alternative ? KeyCode.SUBTRACT : KeyCode.MINUS);
                break;
            case "/":
                controller.push(alternative ? KeyCode.DIVIDE : KeyCode.SLASH);
                break;
            case "*":
                controller.push(alternative ? KeyCode.MULTIPLY : KeyCode.SHIFT, KeyCode.DIGIT8);
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
            default:
                pushDigitButtons(item, alternative);
                break;
        }
    }

    private void pushDigitButtons(String number, boolean alternative) {
        char[] digits = number.toCharArray();

        for (char item : digits){
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

        for (char item : digits){
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
}
