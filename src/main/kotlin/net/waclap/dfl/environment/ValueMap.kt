package net.waclap.dfl.environment

internal class ValueMap {
    private val data = hashSetOf<String>()

    fun set(key: String) {
        data.add(key)
    }

    fun exist(key: String): Boolean = data.contains(key)

    fun reset() {
        data.clear()
    }
}