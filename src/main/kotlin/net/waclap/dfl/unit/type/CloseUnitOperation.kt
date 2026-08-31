package net.waclap.dfl.unit.type

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.unit.UnitOperation

internal object CloseUnitOperation : UnitOperation() {
    override fun apply() {
        val writer = CompileEnvironment.fileWriter
        if (writer == null) {
            CompileEnvironment.logger.addGenerationError("error.generation.no_root_directory")
            return
        }
        writer.close()
    }
}