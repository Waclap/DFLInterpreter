package net.waclap.dfl.operation.type.value.def

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation

internal class DefOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation>? {
        val parser = DefParser(operand)
        val data = parser.parse() ?: return null

        if (CompileEnvironment.macros.exist(data.name)) {
            CompileEnvironment.logger.addError("error.jexl.macro_already_exist", data.name)
            return null
        }

        CompileEnvironment.macros.def(data.name, data)

        return null
    }
}