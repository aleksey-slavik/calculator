package com.implemica.calculator.model.exception;

/**
 * Custom exception for calculations.
 * Throws when zero divide by zero only.
 *
 * @author Slavik Aleksey V.
 */
public class ZeroByZeroDivideException extends Exception {

    public ZeroByZeroDivideException(String message) {
        super(message);
    }
}
