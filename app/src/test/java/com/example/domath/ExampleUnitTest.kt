package com.example.domath

import com.example.domath.calculator.ExpressionParser
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun evaluatesParentheses() {
        val result = ExpressionParser("(2 + 3) * 4").evaluate()

        assertEquals(20.0, result, 0.000001)
    }

    @Test
    fun multiplicationHasHigherPrecedenceThanAddition() {
        val result = ExpressionParser("2 + 3 * 4").evaluate()

        assertEquals(14.0, result, 0.000001)
    }

    @Test
    fun supportsNegativeNumbers() {
        val result = ExpressionParser("-5 + 2").evaluate()

        assertEquals(-3.0, result, 0.000001)
    }

    @Test
    fun rejectsDivisionByZero() {
        try {
            ExpressionParser("10 / 0").evaluate()
            fail("Expected an error for division by zero")
        } catch (error: IllegalArgumentException) {
            // Это ожидаемый результат: деление на ноль запрещено.
        }
    }

    @Test
    fun rejectsMissingClosingParenthesis() {
        try {
            ExpressionParser("(2 + 3").evaluate()
            fail("Expected an error for a missing closing parenthesis")
        } catch (error: IllegalArgumentException) {
            // Это ожидаемый результат: закрывающая скобка отсутствует.
        }
    }
}