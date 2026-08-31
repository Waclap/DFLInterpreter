package net.waclap.dfl.operation.type.value.def

import net.waclap.craftylex.CraftyLex
import net.waclap.craftylex.TokenData
import net.waclap.craftylex.TokenType
import net.waclap.dfl.environment.CompileEnvironment

internal class DefParser(private val input: String) {
    private var i = 0

    fun parse(): MacroData? {
        if (input.isEmpty() || input.isBlank()) {
            CompileEnvironment.logger.addError("error.jexl.expected_macro")
            return null
        }

        skipUntilEqual()
        val defPart = input.substring(0, i - 1).trim()
        val jexlPart = input.substring(i).trim()

        val lexerResult = LEXER.getTokens(defPart, DefineToken.entries)
        val tokens = lexerResult.tokens
        if (lexerResult.hasError) {
            lexerResult.errorTokens.forEach { CompileEnvironment.logger.addError("error.invalid_symbol", it) }
            return null
        }
        val tokenParser = TokenParser(tokens)

        val macroName = if (!tokenParser.consume(DefineToken.ID)) {
            CompileEnvironment.logger.addError("error.jexl.need_macro_name")
            return null
        } else {
            tokenParser.before().content
        }
        if (!tokenParser.consume(DefineToken.LEFT_PAREN)) {
            CompileEnvironment.logger.addError("error.expected", "(")
            return null
        }

        val args = arrayListOf<String>()
        with (tokenParser) {
            while (hasCurrent) {
                val current = current()
                if (consume(DefineToken.RIGHT_PAREN)) {
                    break
                } else if (consume(DefineToken.COMMA)) {
                    continue
                } else if (consume(DefineToken.ID)) {
                    args.addLast(current.content)
                } else {
                    CompileEnvironment.logger.addError("error.invalid_symbol", current.content)
                    return null
                }
            }
        }

        if (tokenParser.hasCurrent) {
            tokens.subList(tokenParser.i, tokens.size).forEach {
                CompileEnvironment.logger.addError("error.invalid_symbol", it.content)
            }
            return null
        }

        return MacroData(macroName, args, jexlPart)
    }

    private fun skipUntilEqual(): Boolean {
        while (i < input.length) {
            if (input[i] == '=') {
                i++
                return true
            }
            i++
        }
        return false
    }

    private class TokenParser(private val tokens: List<TokenData>) {
        var i = 0
            private set
        val hasCurrent: Boolean
            get() = i < tokens.size

        fun current(): TokenData {
            return tokens[i]
        }

        fun before(): TokenData {
            return tokens[i - 1]
        }

        fun consume(type: DefineToken): Boolean {
            if (i < tokens.size && tokens[i].type == type) {
                i++
                return true
            }
            return false
        }
    }

    private enum class DefineToken(private val regex: Regex) : TokenType {
        ID("""[a-zA-Z][a-zA-Z0-9_]*""".toRegex()), LEFT_PAREN("""\(""".toRegex()), RIGHT_PAREN("""\)""".toRegex()),
        COMMA(""",""".toRegex()), SPACE(""" +""".toRegex());

        override fun matches(line: String): Boolean = regex.matches(line)

        override fun isSkipped(): Boolean = this == SPACE
    }

    private companion object {
        val LEXER = CraftyLex()
    }
}