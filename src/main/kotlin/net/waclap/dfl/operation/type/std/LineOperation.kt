package net.waclap.dfl.operation.type.std

import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.WriteUnitOperation

internal class LineOperation(operand: String) : OperationData(operand) {
    override fun read(): List<UnitOperation>? {
        val last = operand.lastOrNull()
        val line = if (last != null && last == '|') {
            "${operand.substring(0, operand.length - 1)}\n"
        } else {
            "$operand\n"
        }
        val elements = StringParser.parse(line)
        return if (StringParser.checkVars(elements)) {
            listOf(WriteUnitOperation(elements))
        } else {
            null
        }
    }
}