package com.implemica.calculator.model.exception;

/**
 * Custom exception for calculations.
 * Thrown when zero divide by zero only.
 *
 * @author Slavik Aleksey V.
 */
public class ZeroDivideByZeroException extends Exception {

    public ZeroDivideByZeroException(String message) {
        super(message);
    }
}
