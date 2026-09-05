package net.waclap.dfl.environment.write

import net.waclap.dfl.environment.CompileEnvironment
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.absolute

internal class FileData(val file: Path) {
    fun write(content: String): Boolean {
        try {
            Files.newBufferedWriter(
                file,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            ).use { writer ->
                writer.write(content)
            }
            return true
        } catch (_: IOException) {
            CompileEnvironment.logger.addGenerationError("error.generation.could_not_write", file.toString())
            return false
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is FileData && other.file.absolute() == file.absolute()
    }

    override fun hashCode(): Int {
        return file.absolute().hashCode()
    }
}