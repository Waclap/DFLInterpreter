package net.waclap.dfl.environment.flow

internal class FileSetting {
    var extension = "mcfunction"
    var type = "function"
    var defaultNamespace = "minecraft"

    fun reset() {
        extension = "mcfunction"
        type = "function"
        defaultNamespace = "minecraft"
    }
}