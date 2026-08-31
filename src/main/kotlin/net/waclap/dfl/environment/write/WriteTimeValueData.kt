package net.waclap.dfl.environment.write

internal class WriteTimeValueData {
    private val map = hashMapOf<String, Any>()

    fun set(name: String, value: Any) {
        map[name] = value
    }

    fun exist(name: String): Boolean = map.containsKey(name)

    fun get(name: String): Any {
        return map[name] ?: "???"
    }

    fun reset() {
        map.clear()
    }
}