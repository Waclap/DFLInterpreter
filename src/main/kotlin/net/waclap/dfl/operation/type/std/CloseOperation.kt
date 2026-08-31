package net.waclap.dfl.operation.type.std

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.CloseUnitOperation

internal object CloseOperation : OperationData("") {
    override fun read(): List<UnitOperation>? {
        if (CompileEnvironment.flow.fileDepth == 0) {
            CompileEnvironment.logger.addError("error.close_underflow")
            return null
        }
        CompileEnvironment.flow.popId()
        return listOf(CloseUnitOperation)
    }
}