package net.waclap.dfl.environment

import net.waclap.dfl.Messages
import net.waclap.dfl.environment.flow.CompileFlow
import net.waclap.dfl.environment.flow.FileSetting
import net.waclap.dfl.environment.flow.FileWriter
import net.waclap.dfl.environment.write.FileData
import net.waclap.dfl.environment.write.WriteTimeValueData
import net.waclap.dfl.unit.UnitOperation
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

internal object CompileEnvironment {
    var flow = CompileFlow()
        private set
    var logger = CompileLogger()
        private set
    val fileSetting = FileSetting()
    var fileWriter: FileWriter? = null
        private set
    val values = ValueMap()
    val typeSettings = TypeSettings()
    val macros = MacroMap()
    val writeTimeValues = WriteTimeValueData()
    val resultList = arrayListOf<UnitOperation>()
    val resultLog = StringBuilder()
    val generationErrors = ArrayList<String>()
    val successfullyLoadedFiles = hashSetOf<FileData>()

    fun setRootDirectory(path: Path) {
        if (!path.exists() || !path.isDirectory()) {
            Files.createDirectories(path)
        }
        fileWriter = FileWriter(path)
    }

    fun read(path: Path) {
        if (successfullyLoadedFiles.contains(FileData(path))) {
            return
        }
        val beforeFlow = flow
        val beforeLogger = logger

        flow = CompileFlow()
        logger = CompileLogger()

        successfullyLoadedFiles.add(FileData(path))
        flow.start(path)
        if (flow.commentMode) {
            logger.addError(Messages.get("error.comment_unclosed"))
        }
        if (logger.hasError) {
            resultLog.appendLine("\u001b[31m>> At $path:\u001b[0m")
            resultLog.appendLine(logger.errors())
        }

        flow = beforeFlow
        logger = beforeLogger
    }

    fun reset() {
        flow = CompileFlow()
        logger = CompileLogger()
        fileSetting.reset()
        fileWriter = null
        values.reset()
        typeSettings.reset()
        macros.reset()
        writeTimeValues.reset()
        resultList.clear()
        resultLog.clear()
        generationErrors.clear()
        successfullyLoadedFiles.clear()
    }
}