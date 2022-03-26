/******************************************************************************
 * The main class for Quizzer.
 *
 * This file represents the quiz itself.
 *
 *  @author Jason Skollingsberg <js032@mix.wvu.edu>
 *  @since Feb 5, 2022
 *  @version 1.0.0-beta
 *****************************************************************************/
package quizzer

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.kinquirer.KInquirer
import com.github.kinquirer.components.*
import com.google.gson.Gson
import quizzer.utility.getLogger
import java.io.File
import java.util.*
import javax.naming.directory.InvalidAttributesException
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.math.round

class Quiz  (
    private val uuid: UUID,
    private val numberOfQuestions: Int? = 10,
    timeLimit: Int? = 10
) {
    private val selectedQuestions  = mutableListOf<Question>()
    private val savedQuestionFile = "questions.qson"
    private val millisecondsInSecond = 1000
    private val secondsInMinute = 60
    private val  maxTime = timeLimit?.times(millisecondsInSecond * secondsInMinute)
        ?: ((secondsInMinute * 10) * millisecondsInSecond)
    private val gson = Gson()

    /**
     * Adds random questions from `savedQuestionFile` to `selectedQuestions`
     * Ensures that no question is added more than once to a quiz
     */
    fun getQuestions() {
        val path = Path(savedQuestionFile).absolutePathString()
        val qList = gson.fromJson(File(path).readText(), Array<Question>::class.java).asList()
        val totalQuestions = qList.size
        if (totalQuestions < numberOfQuestions!!) {
            getLogger().error("Quiz $uuid: unable to get questions, more questions ($numberOfQuestions) requested" +
                    "than available ($totalQuestions)")
            throw InvalidAttributesException("More questions than exist in the file requested")
        }
        while (selectedQuestions.count() < numberOfQuestions) {
            val questionNum = (0..totalQuestions).random()
            if (qList[questionNum] !in selectedQuestions) {
                getLogger().debug("Quiz $uuid: adding question $questionNum to quiz")
                selectedQuestions.add(qList[questionNum])
            }
        }
    }

    /**
     * Reads a file line by line and converts it into a Set of Questions
     * It then saves the questions in `savedQuestionFile` which holds all questions
     * as json objects
     *
     * @param questionFile the relative path the question file being imported
     * @return Boolean the status of the question file.  True if created or false otherwise
     */
    fun createQuestions(questionFile: String): Boolean {
        clearConsole()
        var isQuestion = false
        var isAnswer = false
        var question = Question()
        val questionList = mutableSetOf<Question>()
        var qCount = 0
        val startTime = System.currentTimeMillis()
        val path = Path(questionFile).absolutePathString()
        getLogger().debug("Quiz $uuid: Importing questions from $path")
        File(path).forEachLine {
            if (it.isNotEmpty()) { // ignore empty lines
                val firstCar: String = it.substring(0, 1)
                if (firstCar != "*") { // ignore comment lines
                    if (it[0] =='@') {
                        when (it.trim().substring(0, 2).uppercase()) {
                            "@Q" -> {
                                qCount++
                                isQuestion = true
                                question = Question()
                            }
                            "@A" -> isAnswer = true
                            "@E" -> {
                                isQuestion = false
                                isAnswer = false
                                if (question.validate()) {
                                    questionList.add(question)
                                } else {
                                    getLogger().debug("Quiz $uuid: Question $qCount from file $path is invalid")
                                    throw IllegalArgumentException("Question #$qCount is invalid")
                                }
                            }
                        }
                    } else {
                        handleLine(it, isQuestion, isAnswer, question)
                    }

                }
            }
        }
        if (questionList.size == 0) {
            getLogger().debug("Quiz $uuid: $path had no questions")
            return false
        }
        File(savedQuestionFile).delete()
        File(savedQuestionFile).writeText(gson.toJson(questionList))
        val elapseTime = "${System.currentTimeMillis() - startTime}ms"
        getLogger().info("Quiz $uuid: Added ${questionList.size} questions in $elapseTime")
        println("Added ${questionList.size} questions in $elapseTime")
        println()
        return true
    }

    /**
     * Present and handle user input for this quiz.
     * This method:
     *      - Displays questions
     *      - List of possible answers
     *      - Reads input from user
     *      - Keeps track of the number of answers correct
     *      - Keeps track of how long this quiz has taken
     *      - Displays a score and grade at the end of the quiz
     */
    fun start() {
        var correctAnswers = 0
        var preText = ""
        val startTime = System.currentTimeMillis()
        println("\nStarting Quiz of ${selectedQuestions.size} questions\n")
        quiz@ for (q in 0 until selectedQuestions.size) {
            val text = selectedQuestions[q]
            val correctAns = text.correctAnswer.toInt()
            do {
                if (preText.isNotEmpty()) {
                    println(preText)
                    Thread.sleep(1000)
                }
                val questionDisplay: String = KInquirer.promptList(
                    message = "${q+1}: ${text.questionText}",
                    choices = text.answers,
                    hint = "Use the arrow keys to navigate options and click Enter to select",
                    pageSize = 6
                )
                val ans = text.answers.indexOf(questionDisplay) + 1
                if (ans < 1 || ans > text.answers.size)
                    throw NumberFormatException()

                getLogger().info("Quiz $uuid: Asking question ${q+1}: `${text.questionText}`")
                preText = if (ans == correctAns) {
                    getLogger().info("Quiz $uuid: Question ${q+1}: answered correctly")
                    correctAnswers++
                    "Congratulations! Your answer is correct."
                } else {
                    getLogger().info("Quiz $uuid: Question ${q+1}: answered incorrectly")
                    "Sorry, your answer was not correct"
                }



                val timeLeft = maxTime - (System.currentTimeMillis() - startTime)
                if (timeLeft < 0)
                    break@quiz
            } while (true)
        }

        println(preText)
        Thread.sleep(1000)
        clearConsole()
        println("Quiz has finished you got $correctAnswers out of ${selectedQuestions.size} " +
                "answers correct.")
        getLogger().info("Quiz $uuid: finished $correctAnswers out of ${selectedQuestions.size} correct")
        val percent = round(correctAnswers.toFloat() / selectedQuestions.size * 100).toInt()
        val letterGrade = when {
            percent >= 90 -> "A"
            percent >= 80 -> "B"
            percent >= 70 -> "C"
            percent >= 60 -> "D"
            else -> "F"

        }
        println("Your final score is $percent%")
        print("Your grade is ")
        println(letterGrade)
        val quizTime = formatTimeFromMilliseconds(System.currentTimeMillis() - startTime)
        getLogger().info("Quiz $uuid: Score $percent: Grade $letterGrade in $quizTime")
        println("\nThis quiz took $quizTime to complete")
    }

    /**
     * helper function used to create the questions
     */
    private fun handleLine(it: String, question: Boolean, answer: Boolean, q: Question) {
        var hasAns = q.correctAnswer.isNotEmpty()
        if (question && !answer) {
            q.questionText += "$it \n"
        } else if (question && answer) {
            if (!hasAns) {
                hasAns = true
                q.correctAnswer = (it.toInt() - 1).toString()
            } else {
                q.answers.add(it)
            }
        }
    }

    /**
     * Helper function to provide a friendly formatted string of mins and seconds from an elapsedTime
     *
     * @param elapsedTime: Long the time elapsed
     * @return String
     */
    private fun formatTimeFromMilliseconds(elapsedTime: Long): String {
        val minutes = elapsedTime / millisecondsInSecond / secondsInMinute
        val seconds = elapsedTime / millisecondsInSecond % secondsInMinute

        return "$minutes minutes and $seconds seconds"
    }

    /**
     * Simple method to clear the console for a cleaner appearance on the cli
     */
    private fun clearConsole() {
        if (System.getProperty("os.name").contains("Windows")) {
            ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        } else {
            print("\u001b\u0063")
        }
    }
}