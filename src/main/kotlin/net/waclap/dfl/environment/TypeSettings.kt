package net.waclap.dfl.environment

internal class TypeSettings {
    private var currentType = TypeSetting()

    fun set(newSetting: TypeSetting) {
        CompileEnvironment.fileSetting.defaultNamespace = newSetting.namespace
        CompileEnvironment.fileSetting.type = newSetting.type
        CompileEnvironment.fileSetting.extension = newSetting.extension
    }

    fun reset() {
        currentType = TypeSetting()
    }
}