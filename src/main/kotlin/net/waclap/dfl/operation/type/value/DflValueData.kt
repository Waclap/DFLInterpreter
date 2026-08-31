package net.waclap.dfl.operation.type.value

import net.waclap.dfl.environment.CompileEnvironment

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
                return "???"
            }
            return CompileEnvironment.macros.call(name, args.map { it.asValue() })
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