package net.waclap.dfl.operation.type.fs

import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation
import java.nio.file.InvalidPathException
import java.nio.file.Paths

internal class IncludeOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation>? {
        val path = try {
            Paths.get(operand)
        } catch (_: InvalidPathException) {
            CompileEnvironment.logger.addError("error.path.invalid", operand)
            return null
        }
        CompileEnvironment.read(path)
        return null
    }
}