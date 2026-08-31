package net.waclap.dfl

import java.util.*

internal object Messages {
    private val messages = hashMapOf<String, String>()

    fun get(key: String, vararg args: String): String {
        val base = messages[key] ?: throw IllegalArgumentException("Undefined message key '$key'")
        return try {
            base.format(*args)
        } catch (_: IllegalFormatException) {
            key
        } catch (_: UnknownFormatConversionException) {
            key
        } catch (_: IllegalFormatConversionException) {
            key
        } catch (_: MissingFormatArgumentException) {
            key
        }
    }

    fun set(messageMap: Map<String, String>) {
        messages.clear()
        messages.putAll(messageMap)
    }
}