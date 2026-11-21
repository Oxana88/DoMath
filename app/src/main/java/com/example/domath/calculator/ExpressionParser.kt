package com.example.domath.calculator

class ExpressionParser(private val input: String) {
    private var position = 0

    fun evaluate(): Double {
        val result = parseExpression()
        skipSpaces()

        require(position == input.length) {
            "Unexpected character at position $position"
        }
        require(result.isFinite()) {
            "Result is not finite"
        }

        return result
    }

    // Сложение и вычитание
    private fun parseExpression(): Double {
        var value = parseTerm()

        while (true) {
            skipSpaces()
            value = when {
                consume('+') -> value + parseTerm()
                consume('-') -> value - parseTerm()
                else -> return value
            }
        }
    }

    // Умножение и деление имеют более высокий приоритет
    private fun parseTerm(): Double {
        var value = parseFactor()

        while (true) {
            skipSpaces()
            value = when {
                consume('*') -> value * parseFactor()

                consume('/') -> {
                    val divisor = parseFactor()
                    require(divisor != 0.0) { "Division by zero" }
                    value / divisor
                }

                else -> return value
            }
        }
    }

    // Число, знак или выражение в скобках
    private fun parseFactor(): Double {
        skipSpaces()

        if (consume('+')) return parseFactor()
        if (consume('-')) return -parseFactor()

        if (consume('(')) {
            val value = parseExpression()
            skipSpaces()
            require(consume(')')) { "Missing closing parenthesis" }
            return value
        }

        return parseNumber()
    }

    private fun parseNumber(): Double {
        skipSpaces()
        val start = position
        var decimalSeparatorFound = false

        while (position < input.length) {
            val char = input[position]

            when {
                char.isDigit() -> position++

                (char == '.' || char == ',') && !decimalSeparatorFound -> {
                    decimalSeparatorFound = true
                    position++
                }

                else -> break
            }
        }

        require(position > start) { "Number expected at position $position" }

        val number = input.substring(start, position).replace(',', '.')
        return number.toDoubleOrNull()
            ?: throw IllegalArgumentException("Invalid number")
    }

    private fun consume(expected: Char): Boolean {
        if (position < input.length && input[position] == expected) {
            position++
            return true
        }
        return false
    }

    private fun skipSpaces() {
        while (position < input.length && input[position].isWhitespace()) {
            position++
        }
    }
}