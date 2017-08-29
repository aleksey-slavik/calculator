package com.implemica.calculator.model.exception;

import com.implemica.calculator.model.Calculator;

/**
 * Custom exception for calculations.
 * Throws when scale of result of calculation is bigger than maximum scale.
 * Maximum scale determined in {@link Calculator}.
 *
 * @author Slavik Aleksey V.
 */
public class OverflowException extends Exception {

    public OverflowException(String message) {
        super(message);
    }
}
