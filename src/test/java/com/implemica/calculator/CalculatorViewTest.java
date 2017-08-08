package com.implemica.calculator;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import static org.junit.Assert.*;

public class CalculatorViewTest {

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
        testExpression("1 2 3 4 5 6 7 8 9 0", "1234567890");
        testExpression("1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6", "1234567890123456");
        testExpression("1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0", "1234567890123456");
        testExpression("1 2 3 4 5 6 7 8 9 0 , 1 2 3 4 5 6", "1234567890,123456");
        testExpression("1 2 3 4 5 6 7 8 9 0 , 1 2 3 4 5 6 NG", "-1234567890,123456");
    }

    @Test
    public void plusTest() throws Exception {
        testExpression("2 + 2 =", "4");
        testExpression("2 + 2 +", "4");
        testExpression("2 + 2 + + + +", "4");
        testExpression("2 + + + +", "2");
        testExpression("2 + 2 NG =", "0");
        testExpression("1 2 NG + 1 1 +", "-1");
        testExpression("3 2 NG + 3 2 =", "0");
        testExpression("+ 1 0 0 NG =", "-100");
        testExpression("1 + = = = = = = = =", "9");
        //testExpression("1 6 + 3 NG = = = = = = = ", "-5", ""); //todo
        testExpression("+ 5 = = = = = = = = = =", "50");
        testExpression("+ 5 NG =", "-5");
        //testExpression("+ 5 NG = = = = =", "-25", ""); //todo
        testExpression("0 , 5 + 1 2 +", "12,5");
        testExpression("1 2 3 4 5 , 6 7 8 9 + 9 8 7 6 5 , 4 3 2 1 =", "111111,111");
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 + 1 =", "1,e+16");
        testExpression(", 0 0 0 0 0 0 0 0 0 0 0 0 0 1 + , 0 0 0 0 0 0 0 0 0 0 0 0 0 1 =", "0,00000000000002");
        testExpression(", 0 0 0 0 0 0 0 0 0 0 0 0 0 1 + , 0 0 0 0 0 0 0 0 0 0 0 0 0 1 NG =", "0");
        testExpression(", 0 0 0 0 0 0 0 0 0 0 0 0 0 1 + , 0 0 0 0 0 0 0 0 0 0 0 0 0 9 =", "0,0000000000001");
        //testExpression(", 0 0 0 0 0 0 0 0 0 0 0 0 0 1 + , 0 0 0 0 0 0 0 0 0 0 0 0 0 9 NG =", "-0,00000000000008", ""); //todo
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 + =", "2,e+16");
        testExpression("9 9 9 , 9 9 9 9 9 9 9 9 9 9 9 9 + , 0 0 0 0 0 0 0 0 0 0 0 1 =", "1000");
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 + , 1 =", "9999999999999999");
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 + , 1 = = = =", "9999999999999999");
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 + , 1 = = = =", "9999999999999999");
        //testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 + , 1 = = = = =", "1,e+16", ""); //todo
    }

    @Test
    public void minusTest() throws Exception {
        testExpression("1 0 - 2 =", "8");
        testExpression("1 0 - 2 -", "8");
        testExpression("2 - 0 -", "2");
        testExpression("0 - 4 5 =", "-45");
        testExpression("1 8 - 5 = =", "8");
        testExpression("1 8 - 5 = = = = =", "-7");
        testExpression(", 0 4 - 1 , 9 6 - - -", "-1,92");
        testExpression("0 , 0 0 0 0 0 0 0 0 0 0 0 0 1 - 0 , 0 0 0 0 0 0 0 0 0 0 0 0 1 =", "0");
    }

    @Test
    public void divideTest() throws Exception {
        testExpression("1 / 1 =", "1");
        testExpression("0 / 1 =", "0");
        testExpression("0 / 5 6 7 8 =", "0");
        testExpression("1 2 / 1 =", "12");
        testExpression("0 / 0 =", "Result is undefined");
        testExpression("2 1 3 2 8 / 0 =", "Cannot divide by zero");
    }

    @Test
    public void multiplyTest() throws Exception {
        testExpression("1 * 1 =", "1");
        testExpression("0 * 1 =", "0");
        testExpression(", 0 0 0 1 * 1 0 0 0 0 =", "1");
    }

    @Test
    public void negateTest() throws Exception {
        testExpression("0 NG", "0");
        testExpression("1 NG", "-1");
        testExpression("1 NG NG", "1");
        testExpression("5 NG 8", "-58");
    }

    @Test
    public void inverseTest() throws Exception {
        testExpression("1 1/", "1");
        testExpression("0 1/", "Cannot divide by zero");
    }

    @Test
    public void sqrTest() throws Exception {
        testExpression("1 sqr", "1");
        testExpression("1 NG sqr", "1");
        testExpression("1 sqr sqr sqr", "1");
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow");
    }

    @Test
    public void sqrtTest() throws Exception {
        testExpression("0 sqrt", "0");
        testExpression("1 sqrt", "1");
        testExpression("2 sqrt", "1,414213562373095");
        testExpression("4 sqrt", "2");
        testExpression("1 NG sqrt", "Invalid input");
        testExpression("1 0 0 sqrt", "10");
    }

    @Test
    public void percentTest() throws Exception {
        testExpression("1 0 0 + 1 0 %", "10");
        testExpression("9 8 - 3 4 %", "33,32");
        testExpression("9 8 7 0 * 1 2 0 %", "11844");
        testExpression("1 2 3 6 5 / 9 9 %", "12241,35");
    }

    @Test
    public void clearTest() throws Exception {
        testExpression("1 8 3 2 5 7 , 9 7 7 C" , "0");
        testExpression("1 8 3 2 5 7 , 9 7 7 CE" , "0");
    }

    @Test
    public void backspaceTest() throws Exception {
        testExpression("1 2 6 8 9 , 8 8 BS", "12689,8");
        testExpression("9 8 0 , 9 8 BS BS", "980,");
        testExpression("6 7 3 , 1 2 BS BS BS", "673");
    }

    @Test
    public void combinationTest() throws Exception {
        testExpression("1 0 / 3 * 3 =", "10");
        testExpression("2 / 3 * 3 =", "2");
        testExpression("1 / 3 * 3 =", "1");
        testExpression("9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 + 1 - 1 =", "9999999999999999");
        testExpression("8 7 5 9 9 sqr + = =", "23020754403");
    }

    @Test
    public void memoryTest() throws Exception {
        testExpression("1 0 MS C MR", "10");
        testExpression("9 6 5 M+ + 1 2 3 MR", "965");
        testExpression("3 7 6 5 M+ 7 8 M+ MR", "3843");
        testExpression("4 7 2 3 3 9 9 M- 2 3 7 7 M- MR", "-4725776");
    }

    /*
    //todo
    @Test
    public void testClearEntryButton() {
        testExpression("0", "", "5(CE)");
        testExpression("0", "", "9999999999999999(CE)");
        testExpression("0", "", "0.00000000000001(CE)");
        testExpression("0", "", "9999999999999999+1=(CE)");

        testExpression("0", "1 +", "1+3(CE)");
        testExpression("0", "1 +", "1+9999999999999999(CE)");
        testExpression("0", "1 +", "1+0.00000000000001(CE)");
        testExpression("0", "1 + 9999999999999999 + 1 +", "1+9999999999999999+1+(CE)");

        testExpression("5", "1 +", "1+3(CE)5");
        testExpression("15", "1 +", "1+9999999999999999(CE)15");
        testExpression("0.5", "1 +", "1+0.00000000000001(CE)0.5");
        testExpression("1.5", "1 + 9999999999999999 + 1 +", "1+9999999999999999+1+(CE)1.5");

    }


    @Test
    public void testClearButton() {
        testExpression("0", "", "5C");
        testExpression("0", "", "9999999999999999C");
        testExpression("0", "", "0.00000000000001C");
        testExpression("0", "", "9999999999999999+1=C");

        testExpression("0", "", "1+3C");
        testExpression("0", "", "1+9999999999999999C");
        testExpression("0", "", "1+0.00000000000001C");
        testExpression("0", "", "1+9999999999999999+1+C");

        testExpression("5", "", "1+3C5");
        testExpression("15", "", "1+9999999999999999C15");
        testExpression("0.5", "", "1+0.00000000000001C0.5");
        testExpression("1.5", "", "1+9999999999999999+1+C1.5");
    }

    @Test
    public void testPlus() {
        testExpression("-1+1=0");
        testExpression("-1+2=1");
        testExpression("1+1(+/-)=0");
        testExpression("0+1(+/-)=-1");
        testExpression("1+2(+/-)=-1");

        testExpression("8+5(+/-)=3");
        testExpression("8+5(+/-)===-7");
        testExpression("3+5=8");
        testExpression("0+3=3");
        testExpression("3+==9");
        testExpression("+5=5");
        testExpression("+5(+/-)=-5");
        testExpression("+5=====25");

        testExpression("0+0=0");
        testExpression("1+0=1");
        testExpression("1+0(+/-)=1");

        testExpression("0.5+14=14.5");
        testExpression("12345.6789+98765.4321=111111.111");

        testExpression("0.00000000000001+0.00000000000001=0.00000000000002");
        testExpression("0.00000000000001+0.00000000000001(+/-)=0");
        testExpression("0.00000000000001+0.00000000000002(+/-)=-0.00000000000001");

        testExpression("9999999999999999+9999999999999999(+/-)=0");
        testExpression("9999999999999999+9999999999999998(+/-)=1");
        testExpression("9999999999999998+9999999999999999(+/-)=-1");
        testExpression("9999999999999999+9999999999999999=2e+16");
        testExpression("9999999999999999+1=1e+16");
        testExpression("9999999999999999+1=+1(+/-)=9999999999999999");

        testExpression("999.99999999999+0.00000000001=1000");
    }

    @Test
    public void testMinus() {
        testExpression("-1-1=-2");
        testExpression("1-2=-1");
        testExpression("0-1=-1");
        testExpression("1-1(+/-)=2");
        testExpression("0-1(+/-)=1");
        testExpression("-1-1(+/-)=0");
        testExpression("-1-2(+/-)=1");

        testExpression("8-5=3");
        testExpression("18-5==8");
        testExpression("8-5===-7");
        testExpression("0-3=-3");
        testExpression("3-==-3");
        testExpression("-5=-5");
        testExpression("-5(+/-)=5");
        testExpression("-5=====-25");
        testExpression("0-0=0");
        testExpression("1-0=1");
        testExpression("1-0(+/-)=1");

        testExpression("0.5-14=-13.5");
        testExpression("12345.6789-98765.4321=-86419.7532");

        testExpression("0.00000000000001-0.00000000000001=0");
        testExpression("0.00000000000001-0.00000000000001(+/-)=0.00000000000002");
        testExpression("0.00000000000001-0.00000000000002=-0.00000000000001");
        testExpression("-0.00000000000001-0.00000000000002(+/-)=0.00000000000001");

        testExpression("9999999999999999-9999999999999999=0");
        testExpression("9999999999999999-9999999999999998=1");
        testExpression("9999999999999998-9999999999999999=-1");
        testExpression("9999999999999999-9999999999999999(+/-)=2e+16");
        testExpression("9999999999999999-1(+/-)=1e+16");
        testExpression("9999999999999999-1(+/-)=-1=9999999999999999");

        testExpression("1000-0.00000000001=999.99999999999");
    }

    @Test
    public void testDivide() {
        testExpression("0/3=0");
        testExpression("0/1=0");
        testExpression("0/0.00000000000001=0");
        testExpression("0/9999999999999999=0");

        testExpression("1/1=1");
        testExpression("3/1=3");
        testExpression("0.00000000000001/1=0.00000000000001");
        testExpression("9999999999999999/1=9999999999999999");

        testExpression("0.00000000000002/2=0.00000000000001");
        testExpression("0.00000000000001/2=0.000000000000005");
        testExpression("0.00000000000001/10=0.000000000000001");
        testExpression("0.00000000000001/3=3.333333333333333e-15");
        testExpression("0.00000000000001/0.00000000000001=1");
        testExpression("0.00000000000001/0.00000000000002=0.5");
        testExpression("0.00000000000002/0.00000000000001=2");
        testExpression("0.00000000000001/9999999999999999=1e-30");

        testExpression("9999999999999999/2=5000000000000000");
        testExpression("9999999999999999/9=1111111111111111");
        testExpression("9999999999999999/7=1.428571428571428e+15");
        testExpression("9999999999999999/0.1=9.999999999999999e+16");
        testExpression("9999999999999999/0.00000000000001=9.999999999999999e+29");
        testExpression("9999999999999999/9999999999999999=1");
        testExpression("9999999999999999/8888888888888888=1.125");
        testExpression("-9999999999999999/8888888888888888=-1.125");

        testExpression("10/2=5");
        testExpression("-10/2=-5");
        testExpression("10/2(+/-)=-5");
        //0.3333333333333333
        testExpression("1/3=3.333333333333333e-1");
        testExpression("10/2==2.5");
        testExpression("10/2===1.25");

        testExpression("10/5/2=1");
        testExpression("15/6/8/0.4=0.78125");

        testExpression("2.5", "5/2=");
        testExpression("0", "0/2=");
        testExpression("0.2", "5/==");
    }

    @Test
    public void testDivideByZero() {
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "0/0=");
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "3/0=");
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "9999999999999999/0=");
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "0.00000000000001/0=");
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "-3/0=");
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "-9999999999999999/0=");
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "-0.00000000000001/0=");
    }

    @Test
    public void testMultiply() {
        testExpression("0*0=0");
        testExpression("0*1=0");
        testExpression("0*1(+/-)=0");
        testExpression("*0=0");
        testExpression("*1=0");
        testExpression("*5=0");
        testExpression("*9999999999999999=0");
        testExpression("*0.00000000000001=0");
        testExpression("5*0=0");
        testExpression("-5*0=0");
        testExpression("5*0(+/-)=0");
        testExpression("9999999999999999*0=0");
        testExpression("0*9999999999999999=0");
        testExpression("0.00000000000001*0=0");
        testExpression("0*0.00000000000001=0");

        testExpression("1*1=1");
        testExpression("2*1=2");
        testExpression("-2*1=-2");
        testExpression("2.25*1=2.25");
        testExpression("-2.25*1=-2.25");
        testExpression("9999999999999999*1=9999999999999999");
        testExpression("-9999999999999999*1=-9999999999999999");
        testExpression("0.00000000000001*1=0.00000000000001");
        testExpression("-0.00000000000001*1=-0.00000000000001");
        testExpression("1*1(+/-)=-1");
        testExpression("2*1(+/-)=-2");
        testExpression("-2*1(+/-)=2");
        testExpression("2.25*1(+/-)=-2.25");
        testExpression("-2.25*1(+/-)=2.25");
        testExpression("9999999999999999*1(+/-)=-9999999999999999");
        testExpression("-9999999999999999*1(+/-)=9999999999999999");
        testExpression("0.00000000000001*1(+/-)=-0.00000000000001");
        testExpression("-0.00000000000001*1(+/-)=0.00000000000001");

        testExpression("1*2=2");
        testExpression("1*2(+/-)=-2");
        testExpression("1*2.25=2.25");
        testExpression("1*2.25(+/-)=-2.25");
        testExpression("1*9999999999999999=9999999999999999");
        testExpression("1*9999999999999999(+/-)=-9999999999999999");
        testExpression("1*0.00000000000001=0.00000000000001");
        testExpression("1*0.00000000000001(+/-)=-0.00000000000001");

        testExpression("12345679*9=111111111");
        testExpression("12345679*9(+/-)=-111111111");
        testExpression("20*0.5=10");
        testExpression("20*0.5(+/-)=-10");
        testExpression("3*0.3=0.9");
        testExpression("3*0.3(+/-)=-0.9");
        testExpression("9999999999999999*9999999999999999=9.999999999999998e+31");
        testExpression("9999999999999999*9999999999999999(+/-)=-9.999999999999998e+31");
        testExpression("0.00000000000001*0.00000000000001=1e-28");
        testExpression("0.00000000000001*0.00000000000001(+/-)=-1e-28");

        testExpression("5*=25");
        testExpression("5*==125");
        testExpression("3*=*=81");
        testExpression("0.1*=*10=0.1");
        testExpression("3*===81");
    }

    @Test
    public void testPercent() {
        testExpression("100+0%=100");
        testExpression("100+1%=101");
        testExpression("100+1(+/-)%=99");
        testExpression("100-1%=99");
        testExpression("100+0.1%=100.1");
        testExpression("100+100%=200");
        testExpression("100-100%=0");
        testExpression("100*2%=200");
        testExpression("100/20%=5");
        testExpression("100+500%=600");

        testExpression("-100+10%=-110");
        testExpression("-100*10%=1000");

        testExpression("0.1+100%=0.2");
        testExpression("0.1-100%=0");
        testExpression("0.1+20%=0.12");

        testExpression("9999999999999999+9999999999999999%=1.00000000000001e+30");
        testExpression("9999999999999999+0%=9999999999999999");
        testExpression("9999999999999999*9999999999999999%=9.999999999999997e+45");
        testExpression("9999999999999999-9999999999999999%=-9.999999999999898e+29");
        testExpression("9999999999999999+0.00000000000001%=1e+16");

        testExpression("0.00000000000001+1%=0.00000000000001");
        testExpression("0.00000000000001-1%=9.9e-15");
        testExpression("0.00000000000001-100%=0");
        testExpression("0.00000000000001+100%=0.00000000000002");
        testExpression("0.00000000000001+9999999999999999%=1");
        testExpression("0.00000000000001+9999999999999999%=1");
        testExpression("0.00000000000001+0.00000000000001%=1e-14");

        testExpression("0+1%=0");
        testExpression("0+10%=0");
        testExpression("0+9999999999999999%=0");
        testExpression("0+0.00000000000001%=0");

        testExpression("0%=0");
        testExpression("20%=0");
        testExpression("9999999999999999%=0");
        testExpression("0.00000000000001%=0");

        testExpression("100+10%%%=110");
        testExpression("200+10%%=240");
        testExpression("200+10%%%=280");

        //for second screen assertion
        testExpression("1", "50 + 1", "50+2%");
        testExpression("40", "200 + 40", "200+10%%");
        testExpression("0", "0", "20%");
    }

    @Test
    public void testInvert() {
        testExpression("0(+/-)=0");
        testExpression("1(+/-)=-1");
        testExpression("-1(+/-)=1");
        testExpression("0.1(+/-)=-0.1");
        testExpression("-0.1(+/-)=0.1");
        testExpression("9999999999999999(+/-)=-9999999999999999");
        testExpression("-9999999999999999(+/-)=9999999999999999");
        testExpression("0.00000000000001(+/-)=-0.00000000000001");
        testExpression("-0.00000000000001(+/-)=0.00000000000001");
        testExpression("5(+/-)=-5");
        testExpression("-5(+/-)=5");

        testExpression("5+0(+/-)=5");
        testExpression("1+1(+/-)=0");
        testExpression("1-1(+/-)=2");
        testExpression("0.1+0.1(+/-)=0");
        testExpression("0.1-0.1(+/-)=0.2");
        testExpression("1+9999999999999999(+/-)=-9999999999999998");
        testExpression("1+-9999999999999999(+/-)=1e+16");
        testExpression("1+0.00000000000001(+/-)=0.99999999999999");
        testExpression("1-0.00000000000001(+/-)=1.00000000000001");
        testExpression("3+5(+/-)=-2");
        testExpression("3-5(+/-)=8");

        testExpression("3(+/-)(+/-)=3");
        testExpression("-2(+/-)(+/-)=-2");
        testExpression("3(+/-)(+/-)(+/-)=-3");
        testExpression("-2(+/-)(+/-)(+/-)=2");
    }

    @Test
    public void testReverse() {
        testExpression("0+1(1/x)=1");
        testExpression("0+1(+/-)(1/x)=-1");
        testExpression("0+2(1/x)=0.5");
        testExpression("0+2(+/-)(1/x)=-0.5");
        testExpression("0+0.1(1/x)=10");
        testExpression("0+0.1(+/-)(1/x)=-10");
        testExpression("1+2(1/x)=1.5");
        testExpression("5+10(1/x)(1/x)=15");
        testExpression("5+10(1/x)(1/x)(1/x)=5.1");

        testExpression("0+9999999999999999(1/x)=1e-16");
        testExpression("0+9999999999999999(+/-)(1/x)=-1e-16");
        testExpression("0+9999999999999999(1/x)(1/x)=9999999999999999");
        testExpression("0+0.00000000000001(1/x)=100000000000000");
        testExpression("0+0.00000000000001(+/-)(1/x)=-100000000000000");
        testExpression("0+0.00000000000001(1/x)(1/x)=0.00000000000001");


        //for second screen
        testExpression("0.2", "reciproc(5)", "5(1/x)");
        testExpression("5", "reciproc(reciproc(5))", "5(1/x)(1/x)");
        testExpression("0.2", "1 + reciproc(5)", "1+5(1/x)");
        testExpression("5", "1 + reciproc(reciproc(5))", "1+5(1/x)(1/x)");

        //for divide by zero
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "reciproc(0)", "0(1/x)");
        testExpression(DIVIDE_BY_ZERO_MESSAGE, "1 + reciproc(0)", "1+0(1/x)");
    }

    @Test
    public void testSqrt() {
        testExpression("9s=3");
        testExpression("81ss=3");
        testExpression("6561sss=3");
        testExpression("9s+81s=12");

        testExpression("0.09s=0.3");
        testExpression("0.0081ss=0.3");
        testExpression("0.00006561sss=0.3");

        testExpression("0.00000000000001s=0.0000001");
        testExpression("0.00000000000001ss=0.000316227766017");

        testExpression("9999999999999999s=100000000");
        testExpression("9999999999999999ss=10000");
        testExpression("9999999999999999sssss=3.16227766016838");


        //for second screen
        testExpression("10", "sqrt(100)", "100s");
        testExpression("3", "5 + sqrt(9)", "5+9s");
        testExpression("3", "sqrt(sqrt(81))", "81ss");
        testExpression("3", "5 + sqrt(sqrt(81))", "5+81ss");
        testExpression("3", "", "9s=");
        testExpression("1", "sqrt(9) +", "9s+1");

        //for bad input
        testExpression(INVALID_INPUT_MESSAGE, "sqrt(-100)", "100(+/-)s");
        testExpression(INVALID_INPUT_MESSAGE, "sqrt(-0.09)", "0.09(+/-)s");
        testExpression(INVALID_INPUT_MESSAGE, "sqrt(-5)", "3-8=s");
        testExpression(INVALID_INPUT_MESSAGE, "sqrt(-9999999999999999)", "9999999999999999(+/-)s");
        testExpression(INVALID_INPUT_MESSAGE, "sqrt(-0.00000000000001)", "0.00000000000001(+/-)s");

    }

    @Test
    public void testOperatorCombinations() {
        testExpression("5+9-1=13");
        testExpression("5-9+5-2=-1");
        testExpression("2*3/4=1.5");
        testExpression("-5*2/4=-2.5");
        testExpression("3+5/2=4");
        testExpression("5-1*4=16");
        testExpression("3+4-5/2*8=8");

        testExpression("0.00000000000001+0.00000000000001/0.00000000000001=2");
        testExpression("9999999999999999+5-4/10=1000000000000000");
        testExpression("9999999999999999+9999999999999999/9999999999999999=2");

        testExpression("9s+5=8");
        testExpression("5+9s=8");
        testExpression("5+81ss=8");
        testExpression("81ss+5=8");

        testExpression("5(1/x)+1=1.2");
        testExpression("1+5(1/x)=1.2");


        //for second screen
        //5 + 9 * 2 - 1 / 3 =
        assertSequence("27", "5 + 9 * 2 - 1 /", "5+9*2-1/");

        clickSequence("3=");

        assertFirstScreen("9");
        assertSecondScreen("");

        //3 + 5 + + -
        assertSequence("8", "3 + 5 -", "3+5++-");

        click("*");

        assertFirstScreen("8");
        assertSecondScreen("3 + 5 *");

        click("/");

        assertFirstScreen("8");
        assertSecondScreen("3 + 5 /");

        click("2");
        click("=");

        assertFirstScreen("4");
        assertSecondScreen("");

        //100+10%=
        assertSequence("110", "100+10%=");

        assertSequence("5", "10 +", "10+9s5");
        assertSequence("5", "10 +", "10+10%5");

    }

    @Test
    public void testBackSpace() {
        //one symbol
        testExpression("0", "", "1<");

        //many symbols
        testExpression("0", "", "123<<<");

        //for second operand
        testExpression("12", "12 +", "12+");

        clickSequence("<");
        assertFirstScreen("1");

        clickSequence("<");
        assertFirstScreen("0");

        clickSequence("34");
        assertFirstScreen("34");

        clickSequence("<");
        assertFirstScreen("3");

        clickSequence("<");
        assertFirstScreen("0");

        testExpression("0.", "", "0.00000000000001<<<<<<<<<<<<<<");
        clickSequence("<");
        assertFirstScreen("0");
    }

    @Test
    //@Ignore
    public void testFirstScreenOverflow() {
        testExpression("9999999999999999", "9999999999999999");
        String text = firstScreen.getText();
        click("1");
        assertFirstScreen(text);

        click("+");
        click("5");

        assertFirstScreen("5");

    }

    @Test
    //@Ignore
    public void testSecondScreenOverflow() {
        testExpression("1.00000001e+16", SCREEN_OVERFLOW_SYMBOL + "9999999999999 + 99999999 + 1 +", "9999999999999999+99999999+1+");
        testExpression("987654321", SCREEN_OVERFLOW_SYMBOL + "6789 * 987654321 / 123456789 -", "123456789*987654321/123456789-");
    }

    @Test
    public void testMemoryButtonsOnZero() {
        click("MS");

        assertMemoryScreen("");

        click("M+");

        assertMemoryScreen("");

        click("M-");

        assertMemoryScreen("");

    }

    @Test
    //@Ignore
    public void testMemoryStoreAndRecall() {
        // for single number
        assertSequence("5", "", "5(MS)");
        assertMemoryScreen("M");

        click("C");

        assertFirstScreen("0");
        assertSecondScreen("");

        assertMemoryScreen("M");

        click("MR");

        assertFirstScreen("5");
        assertSecondScreen("");
        assertMemoryScreen("M");

        formatter.pressClearButton();
        click("MC");

        //in expression
        assertSequence("8", "5 +", "5+8(MS)");
        assertMemoryScreen("M");

        click("C");

        assertFirstScreen("0");
        assertSecondScreen("");
        assertMemoryScreen("M");

        click("MR");

        assertFirstScreen("8");
        assertSecondScreen("");
        assertMemoryScreen("M");

        formatter.pressClearButton();
        click("MC");

        //for result
        assertSequence("13", "", "5+8=(MS)");
        assertMemoryScreen("M");

        click("C");

        assertFirstScreen("0");
        assertSecondScreen("");
        assertMemoryScreen("M");

        click("MR");

        assertFirstScreen("13");
        assertSecondScreen("");
        assertMemoryScreen("M");
    }

    @Test
    //@Ignore
    public void testMemoryPlus() {
        assertSequence("5", "", "5(M+)");
        assertMemoryScreen("M");

        clickSequence("C(MR)");

        assertFirstScreen("5");
        assertSecondScreen("");
        assertMemoryScreen("M");

        clickSequence("C3(M+)C(MR)");


        assertFirstScreen("8");
        assertSecondScreen("");
        assertMemoryScreen("M");

        click("1");

        assertFirstScreen("1");
        assertSecondScreen("");
        assertMemoryScreen("M");

        click("MR");

        assertFirstScreen("8");
        assertSecondScreen("");
        assertMemoryScreen("M");

    }

    @Test
    //@Ignore
    public void testMemoryMinus() {
        assertSequence("5", "", "5(M-)");
        assertMemoryScreen("M");

        clickSequence("C(MR)");

        assertFirstScreen("-5");
        assertSecondScreen("");
        assertMemoryScreen("M");

        clickSequence("C3(M-)C(MR)");

        assertFirstScreen("-8");
        assertSecondScreen("");
        assertMemoryScreen("M");

        click("1");

        assertFirstScreen("1");
        assertSecondScreen("");
        assertMemoryScreen("M");

        click("MR");

        assertFirstScreen("-8");
        assertSecondScreen("");
        assertMemoryScreen("M");

    }

    @Test
    //@Ignore
    public void testMemoryClear() {
        assertSequence("5", "", "5(MS)");
        assertMemoryScreen("M");

        click("C");

        assertFirstScreen("0");
        assertSecondScreen("");
        assertMemoryScreen("M");

        click("MR");

        assertFirstScreen("5");
        assertSecondScreen("");
        assertMemoryScreen("M");

        clickSequence("(MC)C(MR)");

        assertFirstScreen("0");
        assertSecondScreen("");
        assertMemoryScreen("");
    }

    @Test
    public void testInputAfterResult() {
        assertSequence("0.", "", "5+8=.");

        assertSequence("58", "55+3=");

        click("7");
        assertFirstScreen("7");
        assertSecondScreen("");

        click("=");
        assertFirstScreen("10");
        assertSecondScreen("");

        //after sqrt
        assertSequence("1", "50 + 1", "50+2%");

        click("5");
        assertFirstScreen("5");
        assertSecondScreen("50 +");


    }

    @Test
    //@Ignore
    public void testScienceRepresentation() {

        assertExpression("9999999999999999+1=1e+16");
        assertExpression("9999999999999999+1=-1=9999999999999999");
        assertExpression("9999999999999999*999=9.989999999999999e+18");
        assertExpression("0.00000000000001/10==1e-16");
        assertExpression("0.00000000000001/10==*10=0.000000000000001");

        assertExpression("1/365==7.506098705197973e-6");
        assertExpression("9999999999*=9.999999998e+19");

        //for second screen
        assertSequence("9.999999998e+19", "9.999999998e+19 +", "9999999999*=+");
        assertSequence("7.506098705197973e-6", "7.506098705197973e-6 +", "1/365==+");


    }

    @Test
    //@Ignore
    public void testTextSizing() {
        clickSequence("999999999999");

        assertEquals(FIRST_SCREEN_BIG_FONT_SIZE, firstScreen.getFont().getSize(), 0.1);

        click("9");

        assertEquals(FIRST_SCREEN_MEDIUM_FONT_SIZE, firstScreen.getFont().getSize(), 0.1);

        click("1/x");

        assertEquals(FIRST_SCREEN_SMALL_FONT_SIZE, firstScreen.getFont().getSize(), 0.1);
    }

    @Test
    public void testOperationAfterResult() {
        assertExpression("55-5=50-7=43");
        assertExpression("10+5=12-3=9");
        assertExpression("10+5=12-9s=9");
        assertExpression("1+9s=12-3=9");
        assertExpression("9999999999999999-1=+1=9999999999999999");
        assertExpression("0.00000000000001+0.00000000000001=-1=-0.99999999999998");
    }

    @Test
    //@Ignore
    public void testRounding() {
        assertExpression("3s*=3");
        assertExpression("7s*=7");
        assertExpression("9999999999999999/568=1.76056338028169e+13");
        assertExpression("1-0.00000000000001=0.99999999999999");
        assertExpression("1000-0.00000000000001=1000");
    }

    @Test
    //@Ignore
    public void testNumberOverflow() {
        assertSequence(OVERFLOW_MESSAGE, "1*0.0000000001==========*=========*=========*0.1=");
        assertSequence(OVERFLOW_MESSAGE, "1*10000000000==========*=========*=========*10==");
    }

    //todo
    */

    private void testExpression(String expression, String expected) throws Exception{
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);

        for (String item : expression.split(" ")) {
            pushButton(item);
        }

        Label numericDisplay = GuiTest.find("#numericField");
        String actualValue = numericDisplay.getText();
        assertEquals(expected, actualValue);
    }

    private void testHistory(String expression, String expected) {
        controller.push(KeyCode.ESCAPE);
        controller.push(KeyCode.CONTROL, KeyCode.L);

        for (String item : expression.split(" ")) {
            pushButton(item);
        }

        Label historyDisplay = GuiTest.find("#historyField");
        String actualValue = historyDisplay.getText();
        assertEquals(expected, actualValue);
    }

    private void pushButton(String item) {
        switch (item) {
            case "0":
                controller.push(KeyCode.DIGIT0);
                break;
            case "1":
                controller.push(KeyCode.DIGIT1);
                break;
            case "2":
                controller.push(KeyCode.DIGIT2);
                break;
            case "3":
                controller.push(KeyCode.DIGIT3);
                break;
            case "4":
                controller.push(KeyCode.DIGIT4);
                break;
            case "5":
                controller.push(KeyCode.DIGIT5);
                break;
            case "6":
                controller.push(KeyCode.DIGIT6);
                break;
            case "7":
                controller.push(KeyCode.DIGIT7);
                break;
            case "8":
                controller.push(KeyCode.DIGIT8);
                break;
            case "9":
                controller.push(KeyCode.DIGIT9);
                break;
            case ",":
                controller.push(KeyCode.COMMA);
                break;
            case "=":
                controller.push(KeyCode.EQUALS);
                break;
            case "BS":
                controller.push(KeyCode.BACK_SPACE);
                break;
            case "+":
                controller.push(KeyCode.ADD);
                break;
            case "-":
                controller.push(KeyCode.SUBTRACT);
                break;
            case "/":
                controller.push(KeyCode.DIVIDE);
                break;
            case "*":
                controller.push(KeyCode.MULTIPLY);
                break;
            case "NG":
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
        }
    }
}
