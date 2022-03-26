package quizzer

import com.github.kinquirer.KInquirer
import com.github.kinquirer.components.promptList

class WinQuizzer {
    fun run() {
        var cont = true
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
    }

    private fun startQuiz() {
        TODO("Not yet implemented")
    }

    private fun setTimeLimit() {
        TODO("Not yet implemented")
    }

    private fun setNumberOfQuestions() {
        TODO("Not yet implemented")
    }

    private fun importQuestions() {
        TODO("Not yet implemented")
    }
}