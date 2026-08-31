package net.waclap.dfl.unit.type

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.type.std.StringParser
import net.waclap.dfl.unit.UnitOperation

internal class WriteUnitOperation(private val content: Array<StringParser.Element>) : UnitOperation() {
    override fun apply() {
        val writer = CompileEnvironment.fileWriter
        if (writer == null) {
            CompileEnvironment.logger.addGenerationError("error.generation.no_root_directory")
            return
        }
        writer.write(StringParser.convertToString(content))
    }
}