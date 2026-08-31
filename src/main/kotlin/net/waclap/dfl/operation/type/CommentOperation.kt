package net.waclap.dfl.operation.type

import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation

internal object CommentOperation : OperationData("") {
    override fun read(): List<UnitOperation>? {
        return null
    }
}