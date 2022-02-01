package quizzer
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.round

class Quiz  {
    // the subfolder where the questions will be stored
    private val questionFolder = "questions"
    private val selectedQuestions  = mutableListOf<Int>()

    fun getQuestions(questionCount: Int?) {
        val totalQuestions = count()
        while (selectedQuestions.count() < questionCount!!) {
            val questionNum = (1..totalQuestions).random().toInt()
            if (questionNum !in selectedQuestions) {
                selectedQuestions.add(questionNum)
            }
        }
    }

    fun createQuestions(questionFile: String): Boolean {
        var isQuestion = false;
        var isAnswer = false;
        var q = Question()
        var questionCount = 0
        File("./$questionFolder").deleteRecursively()
        File("./$questionFile").forEachLine {
            if (it.isNotEmpty()) { // ignore empty lines
                val firstCar: String = it.substring(0, 1);
                if (firstCar != "*") { // ignore comment lines
                    if (it[0] =='@') {
                        when (it.trim().substring(0, 2).uppercase()) {
                            "@Q" -> {
                                questionCount++
                                isQuestion = true
                                q = Question()
                            }
                            "@A" -> isAnswer = true
                            "@E" -> {
                                isQuestion = false
                                isAnswer = false
                                if (q.validate()) {
                                    q.writeToDisk(questionCount.toString())
                                } else {
                                    questionCount--
                                }
                            }
                        }
                    } else {
                        handleLine(it, isQuestion, isAnswer, q)
                    }

                }
            }
        }
        println("Added $questionCount questions\n\n")
        return true
    }

    private fun handleLine(it: String, question: Boolean, answer: Boolean, q: Question) {
        var hasAns = q.correctAnswer.isNotEmpty()
        if (question && !answer) {
            q.questionText += "$it \n"
        } else if (question && answer) {
            if (!hasAns) {
                hasAns = true
                q.correctAnswer = it
            } else {
                q.answers.add(""""$it"""")
            }
        }
    }

    private fun count(): Long
    {
        val resourcesPath = Paths.get(questionFolder)
        val q = Question()
        return Files.walk(resourcesPath)
            .filter { item -> Files.isRegularFile(item) }
            .filter { item -> item.toString().endsWith(Question.fileExtension) }
            .count()

    }

    fun start() {
        println(selectedQuestions)
        var correctAnswers = 0
        val startTime = System.currentTimeMillis()
        for (q in 0 until selectedQuestions.size) {
            val question = Question()
            val text = question.getQuestion(selectedQuestions[q])
            val correctAns = text.correctAnswer.toInt()
            do {
                println("${q + 1}: ${text.questionText}")
                for (x in 0 until text.answers.size) {
                    println("${x + 1}. ${text.answers[x]}")
                }
                print("Which is the correct answer? [$correctAns] ")
                var validResponse = false
                val ans = readLine();
                try {
                    val i = ans?.toInt()
                    if (i != null) {
                        if (i < 1 || i > text.answers.size)
                            throw NumberFormatException()
                    } else {
                        throw NumberFormatException()
                    }
                    validResponse = true

                    if (i == correctAns) {
                        correctAnswers++
                        println("\nCongratulations you got the answer correct!!\n\n")
                    } else {
                        println("\nSorry, that is not the correct answer\n\n")
                    }
                } catch (ne: NumberFormatException) {
                    println("\nPlease pick a number from 1 - ${text.answers.size} \n")
                    validResponse = false
                }
            } while (!validResponse)
        }
        println("Quiz has finished you got $correctAnswers/${selectedQuestions.size} answers correct.")
        val percent = round(correctAnswers.toFloat() / selectedQuestions.size * 100).toInt()
        val letterGrade = when {
            percent >= 90 -> "A"
            percent >= 80 -> "B"
            percent >= 70 -> "C"
            percent >= 60 -> "D"
            else -> "F"

        }
        println("Your final score is $percent%")
        println("Your grade is $letterGrade")
        println("This quiz took ${(System.currentTimeMillis() - startTime)/1000.0}s to complete")
    }
}