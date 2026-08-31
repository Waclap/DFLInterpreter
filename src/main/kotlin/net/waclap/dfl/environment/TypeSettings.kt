package net.waclap.dfl.environment

internal class TypeSettings {
    private val stack = arrayListOf<TypeSetting>()

    fun push(newSetting: TypeSetting) {
        stack.addLast(newSetting)
        CompileEnvironment.fileSetting.defaultNamespace = newSetting.namespace
        CompileEnvironment.fileSetting.type = newSetting.type
        CompileEnvironment.fileSetting.extension = newSetting.extension
    }

    fun pop() {
        if (stack.isEmpty()) {
            CompileEnvironment.logger.addError("error.type_setting_underflow")
        } else {
            val last = stack.last()
            CompileEnvironment.fileSetting.defaultNamespace = last.namespace
            CompileEnvironment.fileSetting.type = last.type
            CompileEnvironment.fileSetting.extension = last.extension
            stack.removeLast()
        }
    }

    fun reset() {
        stack.clear()
    }
}