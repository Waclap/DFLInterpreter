package net.waclap.dfl.operation.type.std

import net.waclap.dfl.ResourceId
import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.FunctionCallAndWriteUnitOperation
import net.waclap.dfl.unit.type.OpenUnitOperation

internal class OpenOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation>? {
        val idString = readUntilSpace(operand)
        val id = ResourceId.parseOrNull(idString)
        if (id == null) {
            CompileEnvironment.logger.addError("error.invalid_resource_lcoation", operand)
            return null
        }
        CompileEnvironment.flow.pushId(id)

        val suffix = operand.substring(idString.length)
        if (suffix.isEmpty() || suffix.isBlank()) {
            return listOf(OpenUnitOperation(id))
        }

        if (suffix.length < SUFFIX_SYMBOL.length || suffix.substring(0, SUFFIX_SYMBOL.length) != SUFFIX_SYMBOL) {
            CompileEnvironment.logger.addError("error.expected", SUFFIX_SYMBOL)
            return null
        }

        val suffixLine = suffix.substring(SUFFIX_SYMBOL.length)

        val last = suffixLine.lastOrNull()
        val suffixResult = if (last != null && last == '|') {
            suffixLine.substring(0, suffixLine.length - 1)
        } else {
            suffixLine
        }
        val elements = StringParser.parse(suffixResult)
        return if (StringParser.checkVars(elements)) {
            listOf(FunctionCallAndWriteUnitOperation(id, elements))
        } else {
            null
        }
    }

    private companion object {
        const val SUFFIX_SYMBOL = " **"

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