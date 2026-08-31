package net.waclap.dfl.operation.type.std

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation

internal object WriteModeOperation : OperationData("") {
    override fun read(): List<UnitOperation>? {
        CompileEnvironment.flow
        return null
    }
}