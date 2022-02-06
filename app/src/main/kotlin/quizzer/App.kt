/******************************************************************************
 * The main file for Quizzer
 *
 * This file includes the entry point `main` function for the quiz
 * it is a CLI app that takes in a few optional cli options
 * Options:
 *        --file, -f -> Question file, will replace current questions with those in the file { String }
 *       --questionCount, -q -> Number of questions to ask, default: 10 { Int }
 *       --timeLimit, -tl -> the number of minutes the quiz is allowed to take, default: 10m { Int }
 *       --help, -h -> Usage info
 *
 * All options are optional.
 *      - If file is missing the quiz will attempt to run with saved questions
 *          if they exist otherwise the program will end
 *      - If questionCount is missing the quiz will default to 10 questions
 *      - If timeLimit is missing the quiz will be timed to 10 minutes
 *
 *  @author Jason Skollingsberg <js032@mix.wvu.edu>
 *  @since Feb 5, 2022
 *  @version 1.0.0-beta
 *****************************************************************************/

package quizzer

import com.github.ajalt.mordant.rendering.TextColors.red
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.FileNotFoundException
import javax.naming.directory.InvalidAttributesException

fun main(args: Array<String>) {
    val parser = ArgParser("Quizzer")
    val file by parser.option(ArgType.String, shortName = "f", description = "Question file, will replace current questions with those in the file")
    val questionCount by parser.option(ArgType.Int, shortName = "q", description = "Number of questions to ask, default: 10", )
    val timeLimit by parser.option(ArgType.Int, shortName = "tl", description = "the number of minutes the quiz is allowed to take, default: 10m")
    parser.parse(args)
    val quiz = Quiz(questionCount?: 10, timeLimit?: 10)
    if (file?.isNotEmpty() == true) {
        try {
            quiz.createQuestions(file!!)
        } catch (e: IllegalArgumentException) {
            println(red("\n\nUnable to parse Question file.\nErr: $e\n\n"))
            return
        } catch (ex: FileNotFoundException) {
            println(red("Unable to import questions from $file.  File is not found."))
            return
        }
    }

    try {
        quiz.getQuestions()
        quiz.start();
    } catch (ex: InvalidAttributesException) {
        println(red("An unrecoverable error occurred. Quiz has ended prematurely"))
        println(ex.message?.let { red(it) })
    }

}
