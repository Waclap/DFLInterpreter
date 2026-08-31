package net.waclap.dfl.operation.type.function

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.operation.type.std.StringParser
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.CallUnitOperation

internal class RecurseOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation>? {
        val id = CompileEnvironment.flow.currentId()
        if (id == null) {
            CompileEnvironment.logger.addError("error.unable_to_recurse", operand)
            return null
        }

        val suffix = operand

        if (suffix.substring(0, SUFFIX_SYMBOL.length) != SUFFIX_SYMBOL) {
            CompileEnvironment.logger.addError("error.expected", SUFFIX_SYMBOL)
            return null
        }

        val suffixLine = " " + suffix.substring(SUFFIX_SYMBOL.length)

        val last = suffixLine.lastOrNull()
        val suffixResult = if (last != null && last == '|') {
            suffixLine.substring(0, suffixLine.length - 1)
        } else {
            suffixLine
        }
        val elements = StringParser.parse(suffixResult)
        return if (StringParser.checkVars(elements)) {
            listOf(CallUnitOperation(id, elements))
        } else {
            null
        }
    }

    private companion object {
        const val SUFFIX_SYMBOL = ""
    }
}