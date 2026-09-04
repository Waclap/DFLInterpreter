package net.waclap.dfl.operation.type.value.reader

import net.waclap.craftylex.CraftyLex
import net.waclap.craftylex.LexerResult

internal object ValueLexer {
    private val lexer = CraftyLex()

    fun lex(input: String): LexerResult {
        return lexer.getTokens(input, TokenType.entries)
    }

    enum class TokenType(private val regex: Regex) : net.waclap.craftylex.TokenType {
        STRING(""""([^"\\]|\\.)*"""".toRegex()), INT("""[0-9]+""".toRegex()), HEX("""0x[0-9a-fA-F]+]""".toRegex()), FLOAT("""[0-9]+\.[0-9]*""".toRegex()),
        ID("""[a-zA-Z][a-zA-Z0-9_]*""".toRegex()), LEFT_PAREN("""\(""".toRegex()), RIGHT_PAREN("""\)""".toRegex()), COMMA(",".toRegex()),
        EQUAL("=".toRegex()), SPACE("""[ \t\r\n]+""".toRegex());

        override fun matches(line: String): Boolean = regex.matches(line)
        override fun isSkipped(): Boolean = this == SPACE
    }
}