package net.waclap.dfl.app

import net.waclap.dfl.Messages
import net.waclap.dfl.environment.CompileEnvironment
import java.nio.file.Path

class DflCompiler(language: DflCompilerLanguage) {
    init {
        Messages.set(language.map)
    }

    fun compile(rootDirectory: Path, program: String): String {
        val errorBuilder = StringBuilder()
        var hasError = false

        CompileEnvironment.reset()
        CompileEnvironment.setRootDirectory(rootDirectory)
        val result = CompileEnvironment.flow.start(program)

        if (CompileEnvironment.logger.hasError) {
            errorBuilder.appendLine(CompileEnvironment.logger.errors())
            hasError = true
        }

        if (CompileEnvironment.flow.commentMode) {
            hasError = true
            errorBuilder.append("\u001b[31m")
            errorBuilder.append(Messages.get("error.comment_unclosed"))
            errorBuilder.appendLine("\u001b[0m")
        }

        val writer = CompileEnvironment.fileWriter
        if (writer == null) {
            errorBuilder.append("\u001b[31m")
            errorBuilder.append(Messages.get("error.generation.file_writer_error"))
            errorBuilder.appendLine("\u001b[0m")

            return errorBuilder.toString()
        }

        if (hasError) {
            return errorBuilder.toString()
        }

        for (op in result) {
            if (writer.isStopped) {
                break
            }
            op.apply()
        }

        if (CompileEnvironment.logger.hasError) {
            errorBuilder.appendLine(CompileEnvironment.logger.generationErrors())
        }

        return errorBuilder.toString()
    }
}