package com.implemica.calculator.model.exception;

/**
 * Custom exception for calculations.
 * Thrown when not zero number divide by zero
 *
 * @author Slavik Aleksey V.
 */
public class ZeroDivideException extends Exception {

    public ZeroDivideException(String message) {
        super(message);
    }
}
