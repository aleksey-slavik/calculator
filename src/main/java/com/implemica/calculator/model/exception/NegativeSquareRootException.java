package com.implemica.calculator.model.exception;

/**
 * Custom exception for calculations.
 * Thrown when the square root is taken from negative number.
 *
 * @author Slavik Aleksey V.
 */
public class NegativeSquareRootException extends Exception {

    public NegativeSquareRootException(String message) {
        super(message);
    }
}
