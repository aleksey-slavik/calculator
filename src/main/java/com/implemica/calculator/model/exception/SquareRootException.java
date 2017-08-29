package com.implemica.calculator.model.exception;

/**
 * Custom exception for calculations.
 * Throws when the square root is taken from negative number.
 *
 * @author Slavik Aleksey V.
 */
public class SquareRootException extends Exception {

    public SquareRootException(String message) {
        super(message);
    }
}
