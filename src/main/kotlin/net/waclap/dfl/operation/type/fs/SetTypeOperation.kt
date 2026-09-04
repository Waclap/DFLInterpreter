package net.waclap.dfl.operation.type.fs

import net.waclap.dfl.ResourceId.Companion.isIdChar
import net.waclap.dfl.ResourceId.Companion.isIdString
import net.waclap.dfl.ResourceId.Companion.isNamespaceString
import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.environment.TypeSetting
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.PushTypeUnitOperation

internal class SetTypeOperation(operand: String) : OperationData(operand.trim()) {
    override fun read(): List<UnitOperation> {
        return listOf(PushTypeUnitOperation(Parser(operand).parse()))
    }

    private class Parser(val input: String) {
        var i = 0

        fun parse(): TypeSetting {
            val namespace = load(false)
            if (namespace.isEmpty()) {
                CompileEnvironment.logger.addError("error.expected_flat", "namespace")
            } else if (!namespace.isNamespaceString) {
                CompileEnvironment.logger.addError("error.invalid_namespace_name", namespace)
            }
            skipSpace()

            val type = load(false)
            if (type.isEmpty()) {
                CompileEnvironment.logger.addError("error.expected_flat", "data type name")
            } else if (!namespace.isNamespaceString) {
                CompileEnvironment.logger.addError("error.invalid_data_type_name", type)
            }
            skipSpace()

            val extension = load(true)
            if (extension.isEmpty()) {
                CompileEnvironment.logger.addError("error.expected_flat", "extension")
            } else if (!namespace.isIdString) {
                CompileEnvironment.logger.addError("error.invalid_file_extension", extension)
            }
            if (i < input.length) {
                CompileEnvironment.logger.addError("error.invalid_symbol", input.substring(i))
            }
            return TypeSetting(namespace, type, extension)
        }

        private fun skipSpace() {
            while (i < input.length) {
                if (input[i] != ' ') {
                    break
                } else {
                    i++
                }
            }
        }

        private fun load(allowDot: Boolean): String {
            val builder = StringBuilder()
            while (i < input.length) {
                if (input[i].isIdChar(allowDot)) {
                    builder.append(input[i])
                    i++
                } else {
                    break
                }
            }
            return builder.toString()
        }
    }
}