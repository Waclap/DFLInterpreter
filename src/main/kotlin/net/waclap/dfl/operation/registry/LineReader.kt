package net.waclap.dfl.operation.registry

internal class LineReader(private val line: String) {
    private var charIndex = 0

    fun readOperationAndOperand(): Pair<String, String> {
        if (line.isEmpty()) {
            return "" to ""
        }

        val builder = StringBuilder()
        var char = line[charIndex]

        while (char != ' ') {
            builder.append(char)
            if (++charIndex >= line.length) {
                break
            }
            char = line[charIndex]
        }

        if (char == ' ') {
            charIndex++
        }

        val operand = if (charIndex < line.length) line.substring(charIndex) else ""

        return builder.toString() to operand
    }
}