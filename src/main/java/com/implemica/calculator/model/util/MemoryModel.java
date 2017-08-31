package com.implemica.calculator.model.util;

import java.math.BigDecimal;

/**
 * CalculationModel memory.
 * Consist main memory operations such as MC, MR, M+, M- and MS
 *
 * @author Slavik Aleksey V.
 */
public class MemoryModel {

    /**
     * Default memory value
     */
    private BigDecimal memory = BigDecimal.ZERO;

    /**
     * Set memory value to default
     */
    public void memoryClear() {
        memory = BigDecimal.ZERO;
    }

    /**
     * Save given value in memory
     *
     * @param value     given value
     */
    public void memoryStore(BigDecimal value) {
        memory = value;
    }

    /**
     * Add given value to current memory value
     *
     * @param value     given value
     */
    public void memoryAdd(BigDecimal value) {
        memory = memory.add(value);
    }

    /**
     * Subtract given value to current memory value
     *
     * @param value     given value
     */
    public void memorySubtract(BigDecimal value) {
        memory = memory.subtract(value);
    }

    /**
     * Get current memory value
     *
     * @return  current memory value
     */
    public BigDecimal memoryRecall() {
        return memory;
    }
}
