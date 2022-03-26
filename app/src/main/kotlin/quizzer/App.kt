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
 *  @author Jason Skollingsberg <js0327@mix.wvu.edu>
 *  @since Mar 24, 2022
 *  @version 1.2.0-beta
 *****************************************************************************/

package quizzer

import mu.KotlinLogging
import quizzer.utility.OS
import quizzer.utility.getLogger
import quizzer.utility.getOS

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val os = getOS()
    getLogger().debug("Hello, Logging")
    if (os == OS.MAC || os == OS.LINUX) {
        val nixQuizzer = NixQuizzer()
        nixQuizzer.run(args)
    }

    if (os == OS.WINDOWS) {
        val winQuizzer = WinQuizzer()
        winQuizzer.run()
    }
}
