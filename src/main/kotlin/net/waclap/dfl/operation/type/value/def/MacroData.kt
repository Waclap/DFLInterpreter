package net.waclap.dfl.operation.type.value.def

import org.apache.commons.jexl3.JexlBuilder
import org.apache.commons.jexl3.JexlEngine
import org.apache.commons.jexl3.MapContext

internal data class MacroData(val name: String, val args: List<String>, val jexl: String) {
    fun call(dataArgs: List<Any>): Any {
        val script = JEXL.createScript(jexl)
        val ctx = MapContext()

        var i = 0
        for (arg in args) {
            ctx.set(arg, dataArgs[i++])
        }

        return script.execute(ctx)
    }

    fun matchArgs(dataArgs: List<Any>): Boolean = args.size == dataArgs.size

    private companion object {
        val JEXL: JexlEngine = JexlBuilder().create()
    }
}