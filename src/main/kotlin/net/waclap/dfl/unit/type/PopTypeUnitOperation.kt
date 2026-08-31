package net.waclap.dfl.unit.type

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.unit.UnitOperation

internal object PopTypeUnitOperation : UnitOperation() {
    override fun apply() {
        CompileEnvironment.typeSettings.pop()
    }
}