package net.waclap.dfl.operation.registry

import net.waclap.dfl.operation.type.DummyOperation
import net.waclap.dfl.operation.type.CommentOperation
import net.waclap.dfl.operation.type.fs.PopTypeOperation
import net.waclap.dfl.operation.type.fs.PushTypeOperation
import net.waclap.dfl.operation.type.function.CallOperation
import net.waclap.dfl.operation.type.function.RecurseOperation
import net.waclap.dfl.operation.type.std.*
import net.waclap.dfl.operation.type.value.SetOperation
import net.waclap.dfl.operation.type.value.def.DefOperation

internal object OperationRegistries {
    private val registry = hashMapOf<String, (String) -> OperationData>()

    init {
        register("///") { WriteModeOperation }
        register("/") { LineOperation(it) }
        register(">") { AppendOperation(it) }
        register("open") { OpenOperation(it) }
        register("close") { CloseOperation }

        register("pushType") { PushTypeOperation(it) }
        register("popType") { PopTypeOperation }

        register("recurse") { RecurseOperation(it) }
        register("call") { CallOperation(it) }

        register("*") { DummyOperation }
        register("***") { CommentOperation }

        register("set") { SetOperation(it) }
        register("def") { DefOperation(it) }
    }

    private fun register(op: String, factory: (String) -> OperationData) {
        registry[op] = factory
    }

    fun separate(line: String): Pair<String, String> {
        return LineReader(line).readOperationAndOperand()
    }

    fun generateOperation(operation: String, operand: String): OperationData? {
        val factory = registry[operation] ?: return null
        return factory(operand)
    }
}