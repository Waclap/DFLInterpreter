package net.waclap.dfl.operation.type.value

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.operation.type.value.reader.ValueLexer
import net.waclap.dfl.operation.type.value.reader.ValueParser
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.SetUnitOperation

internal class ApplyOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation>? {
        val tokens = ValueLexer.lex(operand)
        if (tokens.hasError) {
            tokens.errorTokens.forEach { CompileEnvironment.logger.addError("error.invalid_symbol", it) }
            return null
        }

        val result = arrayListOf<UnitOperation>()
        val parser = ValueParser(tokens.tokens)
        with (parser) {
            while (true) {
                if (!consume(ValueLexer.TokenType.ID)) {
                    CompileEnvironment.logger.addError("error.jexl.expected_value_name")
                    return null
                }
                val name = previous().content

                if (!consume(ValueLexer.TokenType.COLON)) {
                    CompileEnvironment.logger.addError("error.expected", ":")
                    return null
                }

                val value = tryParseValue() ?: break

                result.add(SetUnitOperation(name, value))
                CompileEnvironment.values.set(name)

                if (!consume(ValueLexer.TokenType.COMMA)) {
                    break
                }
            }
        }

        return result
    }
}