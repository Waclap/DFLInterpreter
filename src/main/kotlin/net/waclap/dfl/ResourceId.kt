package net.waclap.dfl

import net.waclap.dfl.environment.CompileEnvironment

internal data class ResourceId(val namespace: String?, val path: String) {
    fun toFileName(): String {
        val fs = CompileEnvironment.fileSetting
        val extension = fs.extension
        val namespace = namespace ?: CompileEnvironment.fileSetting.defaultNamespace
        return if (extension.isNotEmpty()) {
            "data/$namespace/${fs.type}/$path.$extension"
        } else {
            "data/$namespace/${fs.type}/$path"
        }
    }

    override fun toString(): String {
        val namespace = namespace ?: CompileEnvironment.fileSetting.defaultNamespace
        return "$namespace:$path"
    }

    companion object {
        fun parseOrNull(input: String): ResourceId? {
            if (input.isEmpty() || input.isBlank()) {
                return null
            }
            return Parser(input).parse()
        }

        fun Char.isIdChar(allowDot: Boolean): Boolean {
            return isIn(0x61, 0x7A) || isIn(0x30, 0x39) || this == '_' || (allowDot && this == '.')
        }
        val String.isIdString: Boolean
            get() {
                if (isEmpty() || isBlank()) {
                    return false
                }
                for (char in this) {
                    if (!char.isIdChar(true)) {
                        return false
                    }
                }
                return true
            }
        val String.isNamespaceString: Boolean
            get() {
                if (isEmpty() || isBlank()) {
                    return false
                }
                for (char in this) {
                    if (!char.isIdChar(false)) {
                        return false
                    }
                }
                return true
            }

        private fun Char.isIn(from: Int, to: Int): Boolean = code in from..to
    }

    private class Parser(val input: String) {
        var i = 0
        private val isInProgress: Boolean
            get() = i < input.length
        private val current: Char
            get() = input[i]

        fun parse(): ResourceId? {
            val nameSpace = parseId()
            if (!isInProgress) {
                return ResourceId(null, nameSpace)
            }

            val paths = ArrayList<String>()

            val isDefaultNamespace = !consume(':')
            if (!isDefaultNamespace) {
                val id = parseId()
                if (id.isEmpty() || id.isBlank()) {
                    return null
                }
                paths.addLast(id)
            }

            while (isInProgress) {
                if (!consume('/')) {
                    break
                }
                val id = parseId()
                if (id.isEmpty() || id.isBlank()) {
                    return null
                }
                paths.addLast(id)
            }

            if (isInProgress) return null

            return if (isDefaultNamespace) {
                ResourceId(null, "$nameSpace/${paths.joinToString("/")}")
            } else {
                ResourceId(nameSpace, paths.joinToString("/"))
            }
        }

        fun parseId(): String {
            val builder = StringBuilder()
            while (isInProgress && current.isIdChar(true)) {
                builder.append(current)
                i++
            }
            return builder.toString()
        }

        fun consume(char: Char): Boolean {
            if (current == char) {
                i++
                return true
            }
            return false
        }
    }
}
