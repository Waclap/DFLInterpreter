package net.waclap.dfl.environment.flow

import net.waclap.dfl.ResourceId
import net.waclap.dfl.environment.CompileEnvironment
import net.waclap.dfl.environment.write.FileData
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal class FileWriter(val rootPath: Path) {
    var isStopped = false
        private set
    private var hasWritePackMcmeta = false
    private val fileStack = ArrayList<FileData>()
    private val idStack = ArrayList<ResourceId>()

    init {
        openPath("pack.mcmeta", false)
    }

    private fun openPath(path: String, overrideOriginal: Boolean): Boolean {
        val pathData = child(path)
        try {
            Files.createDirectories(pathData.parent)
            if (overrideOriginal) {
                if (Files.exists(pathData)) {
                    Files.delete(pathData)
                }
                Files.createFile(pathData)
            }
            fileStack.addLast(FileData(pathData))
            return true
        } catch (_: IOException) {
            CompileEnvironment.logger.addGenerationError("error.generation.could_not_open", pathData.toString())
            stop()
            return false
        }
    }

    fun open(id: ResourceId) {
        if (openPath(id.toFileName(), true)) {
            idStack.addLast(id)
        }
    }

    fun write(content: String) {
        if (idStack.isEmpty() && !hasWritePackMcmeta) {
            hasWritePackMcmeta = true
            val mcmeta = child("pack.mcmeta")
            if (Files.exists(mcmeta)) {
                Files.delete(mcmeta)
            }
            Files.createFile(mcmeta)
        }
        val data = fileStack.last()
        if (!data.write(content)) {
            stop()
        }
    }

    fun close() {
        if (fileStack.size <= 1) {
            CompileEnvironment.logger.addGenerationError("error.generation.file_stack_underflow")
            stop()
        } else {
            fileStack.removeLast()
            idStack.removeLast()
        }
    }

    fun stop() {
        isStopped = true
    }

    private fun child(child: String): Path {
        return rootPath.resolve(Paths.get(child))
    }

    override fun toString(): String {
        return fileStack.toString()
    }
}