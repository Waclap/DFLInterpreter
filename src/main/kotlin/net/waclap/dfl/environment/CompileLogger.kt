package net.waclap.dfl.environment

import net.waclap.dfl.Messages

internal class CompileLogger {
    private val errors = HashMap<Line, ArrayList<String>>()
    private val generationErrors = ArrayList<String>()
    val hasError: Boolean
        get() = errors.isNotEmpty() || generationErrors.isNotEmpty()
    var artifact = ""
        private set

    fun reset() {
        errors.clear()
        generationErrors.clear()
        artifact = ""
    }

    fun addError(key: String, vararg args: String) {
        val message = Messages.get(key, *args)
        val list = errors.computeIfAbsent(Line(artifact, CompileEnvironment.flow.line)) { arrayListOf() }
        list.addLast(message)
    }

    fun addGenerationError(key: String, vararg args: String) {
        val message = Messages.get(key, *args)
        generationErrors.addLast(message)
    }

    fun errors(): String {
        val builder = StringBuilder("\u001b[31m")

        for ((line, messages) in errors) {
            builder.appendLine("At ${line.artifact}(${line.line}):")
            messages.forEach { builder.appendLine("    $it") }
        }

        builder.append("\u001b[0m")
        return builder.toString()
    }

    fun generationErrors(): String {
        val builder = StringBuilder("\u001b[31m")

        for (message in generationErrors) {
            builder.appendLine("- $message")
        }

        builder.append("\u001b[0m")
        return builder.toString()
    }

    private data class Line(val artifact: String, val line: Int)
}