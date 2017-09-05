package com.implemica.calculator.model.exception;

import com.implemica.calculator.model.util.CalculationModel;

/**
 * Custom exception for calculations.
 * Thrown when scale of result of calculation is bigger than maximum scale.
 * Maximum scale determined in {@link CalculationModel}.
 *
 * @author Slavik Aleksey V.
 */
public class OverflowException extends Exception {

    public OverflowException(String message) {
        super(message);
    }
}
