package net.waclap.dfl.environment.flow

import net.waclap.dfl.ResourceId
import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.operation.registry.OperationRegistries
import net.waclap.dfl.operation.type.CommentOperation
import net.waclap.dfl.operation.type.fs.PopTypeOperation
import net.waclap.dfl.operation.type.fs.PushTypeOperation
import net.waclap.dfl.operation.type.std.CloseOperation
import net.waclap.dfl.operation.type.std.OpenOperation
import net.waclap.dfl.operation.type.std.StringParser
import net.waclap.dfl.operation.type.std.WriteModeOperation
import net.waclap.dfl.operation.registry.OperationData
import net.waclap.dfl.unit.UnitOperation
import net.waclap.dfl.unit.type.WriteUnitOperation
import java.util.*

internal class CompileFlow {
    private var lineIndex = 0
    private val lines = LinkedList<String>()
    var writingMode = WriteModeData()
    var commentMode = false
        private set
    var fileDepth = 0
        private set
    var typeSettingDepth = 0
        private set
    private var idStack = ArrayList<ResourceId>()

    val line: Int
        get() = lineIndex

    fun start(input: String): List<UnitOperation> {
        val result = ArrayList<UnitOperation>()
        lineIndex = 0
        lines.addAll(input.lines())
        writingMode = WriteModeData()
        commentMode = false
        fileDepth = 0
        typeSettingDepth = 0
        idStack.clear()

        while (lineIndex < lines.size) {
            val line = lines[lineIndex++]
            if (line.isEmpty() || line.isBlank()) {
                continue
            }

            val trimmed = line.trimStart()
            val indent = line.length - trimmed.length
            val (op, operand) = OperationRegistries.separate(trimmed)
            val operation = OperationRegistries.generateOperation(op, operand)

            if (handleCommentMode(line, operation)) continue
            if (handleWriteMode(result, op, operation, indent, trimmed)) continue
            if (operation == null) continue

            when (operation) {
                is OpenOperation -> {
                    fileDepth++
                }
                is CloseOperation -> {
                    fileDepth--
                }
                is PushTypeOperation -> {
                    typeSettingDepth++
                }
                is PopTypeOperation -> {
                    if (typeSettingDepth <= 0) {
                        CompileEnvironment.logger.addError("error.type_setting_underflow")
                    }
                    typeSettingDepth--
                }
            }
        }

        if (typeSettingDepth > 0) {
            repeat(typeSettingDepth) {
                CompileEnvironment.logger.addError("error.type_need_pop")
            }
        }

        return result
    }

    private fun handleWriteMode(result: ArrayList<UnitOperation>, op: String, operation: OperationData?, indent: Int, trimmed: String): Boolean {
        if (operation is WriteModeOperation) {
            if (!writingMode.isInMode) {
                writingMode.start(indent)
            } else {
                val formatted = StringParser.parse(writingMode.build())
                if (StringParser.checkVars(formatted)) {
                    result.addLast(WriteUnitOperation(formatted))
                }
            }
            return true
        }

        if (writingMode.isInMode) {
            writingMode.add(trimmed, indent)
        } else {
            if (operation == null) {
                CompileEnvironment.logger.addError("error.reader.unknown_operation", op)
                return true
            }
            operation.read()?.let { result.addAll(it) }
        }
        return false
    }

    private fun handleCommentMode(line: String, operation: OperationData?): Boolean {
        if (commentMode && line.isNotEmpty() && line[0] == '|') {
            return true
        }
        if (operation is CommentOperation) {
            commentMode = !commentMode
            return true
        } else if (commentMode) {
            return true
        }
        return false
    }

    fun pushId(id: ResourceId) {
        idStack.addLast(id)
    }

    fun currentId(): ResourceId? {
        return idStack.lastOrNull()
    }

    fun popId() {
        idStack.removeLast()
    }
}