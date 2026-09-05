import net.waclap.dfl.app.DflCompiler
import net.waclap.dfl.app.DflCompilerLanguage
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Paths
import kotlin.io.path.exists

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        val builder = StringBuilder("\u001b[31m")

        builder.appendLine("Usage: ")
        builder.appendLine("    [source file] [generation location]")

        builder.appendLine("\u001b[0m")
        print(builder.toString())
        return
    }
    val sourceFile = args[0]
    val generationPathStr = args.getOrNull(1)
    if (generationPathStr == null) {
        val builder = StringBuilder("\u001b[31m")

        builder.appendLine("Usage: ")
        builder.appendLine("    [source file] [generation location]")

        builder.appendLine("\u001b[0m")
        print(builder.toString())
        return
    }
    val generationPath = Paths.get(generationPathStr)

    val programPath = try {
        Paths.get(sourceFile)
    } catch (_: InvalidPathException) {
        println("\u001b[31mInvalid path '$sourceFile'\u001b[0m")
        return
    }

    if (!programPath.exists() || !Files.isRegularFile(programPath)) {
        val builder = StringBuilder("\u001b[31m")

        builder.append("File '$sourceFile' does not exist")

        builder.appendLine("\u001b[0m")
        print(builder.toString())
        return
    }

    val compiler = DflCompiler(DflCompilerLanguage.EN_US)

    val errors = compiler.compile(generationPath, programPath)
    if (errors.isNotEmpty() && errors.isNotBlank()) {
        print(errors)
    }
}