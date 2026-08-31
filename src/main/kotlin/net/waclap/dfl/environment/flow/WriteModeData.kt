package net.waclap.dfl.environment.flow

import kotlin.math.max

internal class WriteModeData {
    var isInMode = false
    var defaultIndent = 0
    private val builder = StringBuilder()

    fun add(content: String, indent: Int) {
        builder.append(" ".repeat(max(0, indent - defaultIndent)))
        builder.appendLine(content)
    }

    fun start(defaultIndent: Int) {
        builder.clear()
        isInMode = true
        this.defaultIndent = defaultIndent
    }

    fun build(): String {
        val result = builder.toString()
        isInMode = false
        return result
    }
}