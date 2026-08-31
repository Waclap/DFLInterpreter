package net.waclap.dfl.environment

import net.waclap.dfl.operation.type.value.def.MacroData

internal class MacroMap {
    private val map = hashMapOf<String, MacroData>()

    fun def(name: String, data: MacroData) {
        map[name] = data
    }

    fun exist(name: String): Boolean {
        return map.containsKey(name)
    }

    fun call(name: String, args: List<Any>): Any {
        val data = map[name] ?: return ""
        return data.call(args)
    }

    fun test(name: String, args: List<Any>): Boolean {
        val data = map[name]
        if (data == null) {
            CompileEnvironment.logger.addError("error.jexl.macro_not_exist", name)
            return false
        }
        if (!data.matchArgs(args)) {
            CompileEnvironment.logger.addError("error.jexl.unmatch_args", data.args.size.toString(), args.size.toString())
            return false
        }
        return true
    }

    fun reset() {
        map.clear()
    }
}