package net.waclap.dfl.operation.type.value

import net.waclap.dfl.environment.CompileEnvironment
import org.apache.commons.jexl3.JexlException

internal sealed class DflValue {
    class Literal(val value: Any) : DflValue() {
        override fun toString(): String {
            return "Literal($value)"
        }

        override fun asValue(): Any {
            return value
        }
    }

    class Value(val name: String) : DflValue() {
        override fun toString(): String {
            return "Var($name)"
        }

        override fun asValue(): Any {
            if (!CompileEnvironment.writeTimeValues.exist(name)) {
                CompileEnvironment.fileWriter?.stop()
                return "???"
            }
            return CompileEnvironment.writeTimeValues.get(name)
        }
    }

    class MacroCall(val name: String, val args: List<DflValue>) : DflValue() {
        override fun toString(): String {
            return "$name(${args.joinToString(", ")})"
        }

        override fun asValue(): Any {
            if (!CompileEnvironment.macros.test(name, args)) {
                CompileEnvironment.fileWriter?.stop()
                return "???"
            }
            try {
                return CompileEnvironment.macros.call(name, args.map { it.asValue() })
            } catch (_: JexlException) {
                CompileEnvironment.fileWriter?.stop()
                CompileEnvironment.logger.addGenerationError("error.jexl.execution")
                return "???"
            }
        }
    }

    object Error : DflValue() {
        override fun toString(): String {
            return "Nil"
        }

        override fun asValue(): Any {
            return "???"
        }
    }

    abstract fun asValue(): Any
}