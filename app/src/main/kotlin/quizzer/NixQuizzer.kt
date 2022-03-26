package quizzer

import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.FileNotFoundException
import javax.naming.directory.InvalidAttributesException

class NixQuizzer {
    fun run(args: Array<String>) {
        val parser = ArgParser("Quizzer")
        val file by parser.option(
            ArgType.String,
            shortName = "f",
            description = "Question file, will replace current questions with those in the file"
        )
        val questionCount by parser.option(
            ArgType.Int,
            shortName = "q",
            description = "Number of questions to ask, default: 10",
        )
        val timeLimit by parser.option(
            ArgType.Int,
            shortName = "tl",
            description = "the number of minutes the quiz is allowed to take, default: 10m"
        )
        parser.parse(args)
        val quiz = Quiz(questionCount ?: 10, timeLimit ?: 10)
        if (file?.isNotEmpty() == true) {
            try {
                quiz.createQuestions(file!!)
            } catch (e: IllegalArgumentException) {
                println(TextColors.red("\n\nUnable to parse Question file.\nErr: $e\n\n"))
                return
            } catch (ex: FileNotFoundException) {
                println(TextColors.red("Unable to import questions from $file.  File is not found."))
                return
            }
        }

        try {
            quiz.getQuestions()
            quiz.start();
        } catch (ex: InvalidAttributesException) {
            println(TextColors.red("An unrecoverable error occurred. Quiz has ended prematurely"))
            println(ex.message?.let { TextColors.red(it) })
        }
    }
}