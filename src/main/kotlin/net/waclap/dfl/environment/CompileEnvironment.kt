package net.waclap.dfl.environment

import net.waclap.dfl.environment.flow.CompileFlow
import net.waclap.dfl.environment.flow.FileSetting
import net.waclap.dfl.environment.flow.FileWriter
import net.waclap.dfl.environment.write.WriteTimeValueData
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

internal object CompileEnvironment {
    val flow = CompileFlow()
    val logger = CompileLogger()
    val fileSetting = FileSetting()
    var fileWriter: FileWriter? = null
        private set
    val values = ValueMap()
    val typeSettings = TypeSettings()
    val macros = MacroMap()
    val writeTimeValues = WriteTimeValueData()

    fun setRootDirectory(path: Path) {
        if (!path.exists() || !path.isDirectory()) {
            Files.createDirectories(path)
        }
        fileWriter = FileWriter(path)
    }

    fun reset() {
        logger.reset()
        fileSetting.reset()
        fileWriter = null
        values.reset()
        typeSettings.reset()
        macros.reset()
        writeTimeValues.reset()
    }
}