package net.waclap.dfl.unit.type

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.environment.TypeSetting
import net.waclap.dfl.environment.flow.FileSetting
import net.waclap.dfl.unit.UnitOperation

internal class PushTypeUnitOperation(private val setting: TypeSetting) : UnitOperation() {
    override fun apply() {
        CompileEnvironment.typeSettings.push(setting)
    }
}