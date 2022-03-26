package quizzer

import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import quizzer.utility.getLogger
import java.io.FileNotFoundException
import javax.naming.directory.InvalidAttributesException

class NixQuizzer {
    private val uuid = getUUid()
    fun run(args: Array<String>) {
        getLogger().info("Quiz $uuid starting");
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
        getLogger().info("Quiz $uuid starting with file: $file")
        getLogger().info("Quiz $uuid starting with questionCount: $questionCount")
        getLogger().info("Quiz $uuid starting with timeLimit: $timeLimit")
        val quiz = Quiz(uuid, questionCount ?: 10, timeLimit ?: 10)
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
            getLogger().info("Quiz $uuid ending");
        } catch (ex: InvalidAttributesException) {
            println(TextColors.red("An unrecoverable error occurred. Quiz has ended prematurely"))
            println(ex.message?.let { TextColors.red(it) })
        }
    }
}