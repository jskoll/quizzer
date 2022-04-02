package quizzer

import com.github.kinquirer.KInquirer
import com.github.kinquirer.components.promptList
import quizzer.utility.getLogger

class WinQuizzer {
    private val uuid = getUUid()
    private val quiz = Quiz(uuid)
    private var questionsToAsk = 10;
    private var timeLimit = 10;
    suspend fun run() {
        getLogger().info("Quiz $uuid: starting")
        val mainMenuOptions = listOf(
            "Import Questions",
            "Set number of questions to ask (default: 10)",
            "Set a time limit in minutes (default: 10)",
            "Start Quiz",
            "Exit"
        )
        startQuizzer@  while (true) {
            println("Welcome to Windows Quizzer")
            val mainMenu: String = KInquirer.promptList(
                message = "Select one of the following options:",
                choices = mainMenuOptions,
                hint = "Use the arrow keys to navigate options and click Enter to select",
                pageSize = 5
            )
            when (mainMenuOptions.indexOf(mainMenu)) {
                0 -> importQuestions()
                1 -> setNumberOfQuestions()
                2 -> setTimeLimit()
                3 -> startQuiz()
                4 -> break@startQuizzer
                else -> continue@startQuizzer
            }

        }
        getLogger().info("Quiz $uuid: ending")
    }

    suspend private fun startQuiz() {
        quiz.getQuestions()
        quiz.run();
    }

    private fun setTimeLimit() {
        var time: Int? = null
        var input: String? = null
        var attempt = 0
        while (time == null && attempt < 5) {
            try {
                attempt++
                print("How many minutes should be given for this quiz?")
                input = readLine()
                if (input != null) {
                    time = input.toInt()
                    timeLimit = time;
                }
            } catch (exception: Exception) {
                getLogger().info("Quiz $uuid: time limit of quiz invalid input ($input)")
                time = null
            }
        }
    }

    private fun setNumberOfQuestions() {
        var questions: Int? = null
        var input: String? = null
        var attempt = 0
        while (questions == null && attempt < 5) {
            try {
                attempt++
                print("How many questions should this quiz include?")
                input = readLine()
                if (input != null) {
                    questions = input.toInt()
                    questionsToAsk = questions;
                }
            } catch (exception: Exception) {
                getLogger().info("Quiz $uuid: Number of questions to ask invalid input ($input)")
                questions = null
            }
        }
    }

    private fun importQuestions() {
        var file: String? = null
        var attempt = 0;
        while (file == null && attempt < 5) {
            try {
                attempt++
                println("Import Questions:")
                println("Enter an absolute or relative path to the question file to import:")
                file = readLine()
                if (file != null) {
                    quiz.createQuestions(file)
                } else {
                    println("Enter a path to the question file to import")
                }
            } catch (exception: Exception) {
                println("An Invalid file path provided unable to import questions")
                file = null
            }
        }
    }
}