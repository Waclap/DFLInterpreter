package net.waclap.dfl.environment

import net.waclap.dfl.Messages

internal class CompileLogger {
    private val errors = HashMap<Int, ArrayList<String>>()
    private val errorLines = ArrayList<Int>()
    val hasError: Boolean
        get() = errors.isNotEmpty()

    fun addError(key: String, vararg args: String) {
        val message = Messages.get(key, *args)
        val list = errors.computeIfAbsent(CompileEnvironment.flow.line) { arrayListOf() }
        errorLines.addLast(CompileEnvironment.flow.line)
        list.addLast(message)
    }

    fun addGenerationError(key: String, vararg args: String) {
        val message = Messages.get(key, *args)
        CompileEnvironment.generationErrors.addLast(message)
    }

    fun errors(): String {
        val builder = StringBuilder("\u001b[31m")

        for (line in errorLines) {
            val entry = errors[line] ?: arrayListOf()
            builder.appendLine("L$line:")
            entry.forEach { builder.appendLine("- $it") }
        }

        builder.append("\u001b[0m")
        return builder.toString()
    }
}