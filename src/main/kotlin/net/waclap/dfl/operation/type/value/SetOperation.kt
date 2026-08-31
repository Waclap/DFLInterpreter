package net.waclap.dfl.operation.type.value

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.operation.type.value.reader.ValueLexer
import net.waclap.dfl.operation.type.value.reader.ValueParser
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.SetUnitOperation

internal class SetOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation>? {
        val tokens = ValueLexer.lex(operand)
        if (tokens.hasError) {
            tokens.errorTokens.forEach { CompileEnvironment.logger.addError("error.invalid_symbol", it) }
            return null
        }

        val value = ValueParser.parseSet(tokens.tokens)
        if (value is DflValue.Error) {
            return null
        }

        val name = tokens.tokens[0].content
        CompileEnvironment.values.set(name)
        return listOf(SetUnitOperation(name, value))
    }
}