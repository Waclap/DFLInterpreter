package net.waclap.dfl.operation.type.value.reader

import net.waclap.craftylex.TokenData
import net.waclap.craftylex.TokenType
import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.type.value.DflValue

internal class ValueParser(private val tokens: List<TokenData>) {
    private var i = 0

    private fun consumeSetter(): Boolean {
        if (!consume(ValueLexer.TokenType.ID)) {
            CompileEnvironment.logger.addError("error.jexl.expected_value_name")
            return false
        }
        if (!consume(ValueLexer.TokenType.EQUAL)) {
            CompileEnvironment.logger.addError("error.expected", "=")
            return false
        }
        return true
    }

    private fun tryParseValue(): DflValue? {
        tryParseMacroCall()?.also { return it }
        if (i < tokens.size) {
            tryParseLiteral()?.also { return it }
        }

        return null
    }

    private fun tryParseMacroCall(): DflValue? {
        val originalIndex = i
        if (!consume(ValueLexer.TokenType.ID)) return null
        val name = tokens[originalIndex].content
        if (!consume(ValueLexer.TokenType.LEFT_PAREN)) {
            i = originalIndex
            return null
        }

        if (consume(ValueLexer.TokenType.RIGHT_PAREN)) {
            return DflValue.MacroCall(name, listOf())
        }

        val firstArg = tryParseValue() ?: return null
        val args = arrayListOf(firstArg)

        while (true) {
            if (i >= tokens.size) {
                CompileEnvironment.logger.addError("error.expected", ")")
                return null
            } else if (consume(ValueLexer.TokenType.RIGHT_PAREN)) {
                break
            } else if (consume(ValueLexer.TokenType.COMMA)) {
                tryParseValue()?.also {
                    args.addLast(it)
                    continue
                }
            } else {
                return null
            }
            i++
        }

        if (!CompileEnvironment.macros.test(name, args)) {
            return null
        }
        return DflValue.MacroCall(name, args)
    }

    private fun tryParseLiteral(): DflValue? {
        val originalIndex = i

        val current = tokens[i]
        val currentStr = current.content.trim()

        if (consume(ValueLexer.TokenType.STRING)) {
            val data = if (currentStr.length <= 2) {
                ""
            } else {
                currentStr.substring(1, currentStr.length - 1).replace("\\\"", "\"")
            }
            return DflValue.Literal(data)

        } else if (consume(ValueLexer.TokenType.INT)) {
            return DflValue.Literal(currentStr.toIntOrNull() ?: 0)

        } else if (consume(ValueLexer.TokenType.HEX)) {
            return DflValue.Literal(currentStr.toIntOrNull(16) ?: 0)

        } else if (consume(ValueLexer.TokenType.FLOAT)) {
            return DflValue.Literal(currentStr.toFloatOrNull() ?: 0.0F)

        } else if (consume(ValueLexer.TokenType.ID)) {
            if (!CompileEnvironment.values.exist(currentStr)) {
                CompileEnvironment.logger.addError("error.var_not_exist", currentStr)
                return null
            }
            return DflValue.Value(currentStr)

        } else {
            i = originalIndex
            return null
        }

    }

    private fun consume(type: TokenType): Boolean {
        if (i < tokens.size && tokens[i].type == type) {
            i++
            return true
        }
        return false
    }

    companion object {
        fun parse(input: List<TokenData>): DflValue {
            val parser = ValueParser(input)
            return parser.tryParseValue() ?: DflValue.Error
        }

        fun parseSet(input: List<TokenData>): DflValue {
            val parser = ValueParser(input)
            if (!parser.consumeSetter()) {
                return DflValue.Error
            }
            return parser.tryParseValue() ?: DflValue.Error
        }
    }
}