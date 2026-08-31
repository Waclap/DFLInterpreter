package net.waclap.dfl.operation.type.std

import net.waclap.dfl.environment.CompileEnvironment

internal object StringParser {
    data class Element(val isText: Boolean, val content: String) {
        override fun toString(): String {
            return if (isText) "String($content)" else "Value($content)"
        }
    }

    fun parse(input: String): Array<Element> {
        val result = arrayListOf<Element>()
        var lastIndex = 0

        for (match in GETTER_REGEX.findAll(input)) {
            val before = input.getOrNull(match.range.first - 1) ?: ' '
            val value = match.value

            if (before != '%') {
                result.addLast(Element(true, input.substring(lastIndex, match.range.first)))
                result.add(Element(false, value.substring(2, value.length - 1)))
            } else {
                val str = input.substring(lastIndex, match.range.first - 1) +  value.substring(0, value.length)
                result.addLast(Element(true, str))
            }
            lastIndex = match.range.last + 1
        }

        if (lastIndex < input.length) {
            result.addLast(Element(true, input.substring(lastIndex)))
        }

        return result.toTypedArray()
    }

    fun checkVars(array: Array<Element>): Boolean {
        val illegalVars = arrayListOf<String>()
        for ((isText, content) in array) {
            if (!isText && !CompileEnvironment.values.exist(content)) {
                illegalVars.addLast(content)
            }
        }
        illegalVars.forEach { CompileEnvironment.logger.addError("error.var_not_exist", it) }
        return illegalVars.isEmpty()
    }

    fun convertToString(array: Array<Element>): String {
        val builder = StringBuilder()
        for ((isText, content) in array) {
            if (isText) {
                builder.append(content)
            } else {
                builder.append(CompileEnvironment.writeTimeValues.get(content))
            }
        }
        return builder.toString()
    }

    private val GETTER_REGEX = """%\{[^}]*}""".toRegex()
}