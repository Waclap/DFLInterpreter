package net.waclap.dfl.app

import net.waclap.dfl.Messages
import net.waclap.dfl.environment.CompileEnvironment
import java.nio.file.Path

class DflCompiler(language: DflCompilerLanguage) {
    init {
        Messages.set(language.map)
    }

    fun compile(rootDirectory: Path, sourcePath: Path): String {
        val errorBuilder = StringBuilder()
        var hasError = false

        CompileEnvironment.reset()
        CompileEnvironment.setRootDirectory(rootDirectory)
        CompileEnvironment.read(sourcePath)

        if (CompileEnvironment.resultLog.isNotEmpty()) {
            errorBuilder.appendLine(CompileEnvironment.resultLog.toString())
            hasError = true
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

        for (op in CompileEnvironment.resultList) {
            if (writer.isStopped) {
                break
            }
            op.apply()
        }

        if (CompileEnvironment.generationErrors.isNotEmpty()) {
            errorBuilder.appendLine("Generation errors: ")
            CompileEnvironment.generationErrors.forEach { errorBuilder.appendLine(it) }
        }

        return errorBuilder.toString()
    }
}