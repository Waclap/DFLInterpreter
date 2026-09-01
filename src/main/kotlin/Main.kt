import net.waclap.dfl.app.DflCompiler
import net.waclap.dfl.app.DflCompilerLanguage
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        val builder = StringBuilder("\u001b[31m")

        builder.appendLine("Usage: ")
        builder.appendLine("    [source file] [generation location]?")

        builder.appendLine("\u001b[0m")
        print(builder.toString())
        return
    }
    val sourceFile = args[0]
    val generationPath = args.getOrNull(1)?.let { Paths.get(it) } ?: Paths.get("")

    val programPath = Paths.get(sourceFile)
    if (!programPath.exists() || !Files.isRegularFile(programPath)) {
        val builder = StringBuilder("\u001b[31m")

        builder.append("File '$sourceFile' does not exist")

        builder.appendLine("\u001b[0m")
        print(builder.toString())
        return
    }
    val program = Files.readString(programPath)

    val compiler = DflCompiler(DflCompilerLanguage.EN_US)

    val errors = compiler.compile(generationPath, program)
    if (errors.isNotEmpty() && errors.isNotBlank()) {
        println("\u001b[31mAt $programPath:\u001b[0m")
        print(errors)
    }
}