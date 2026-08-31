package net.waclap.dfl.operation.registry

import net.waclap.dfl.unit.UnitOperation

internal abstract class OperationData(val operand: String) {
    abstract fun read(): List<UnitOperation>?
}