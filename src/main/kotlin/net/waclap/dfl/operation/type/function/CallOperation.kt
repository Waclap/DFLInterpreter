package net.waclap.dfl.operation.type.function

import net.waclap.dfl.ResourceId
import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.operation.type.std.StringParser
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.CallUnitOperation

internal class CallOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation>? {
        val idString = readUntilSpace(operand)
        val id = ResourceId.parseOrNull(idString)
        if (id == null) {
            CompileEnvironment.logger.addError("error.invalid_resource_lcoation", operand)
            return null
        }

        val suffixLine = operand.substring(idString.length)

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
        fun readUntilSpace(line: String): String {
            var i = 0
            while (i < line.length) {
                if (line[i] == ' ') {
                    break
                }
                i++
            }
            return line.substring(0, i)
        }
    }
}