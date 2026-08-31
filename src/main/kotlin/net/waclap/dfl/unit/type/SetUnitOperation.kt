package net.waclap.dfl.unit.type

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.type.value.DflValue
import net.waclap.dfl.unit.UnitOperation

internal class SetUnitOperation(val name: String, val data: DflValue) : UnitOperation() {
    override fun apply() {
        CompileEnvironment.writeTimeValues.set(name, data.asValue())
    }
}