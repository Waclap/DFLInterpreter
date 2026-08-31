package net.waclap.dfl.operation.type.fs

import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.PopTypeUnitOperation

internal object PopTypeOperation : OperationData("") {
    override fun read(): List<UnitOperation> {
        return listOf(PopTypeUnitOperation)
    }
}