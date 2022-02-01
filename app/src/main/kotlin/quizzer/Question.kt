package quizzer

import com.google.gson.Gson
import java.io.File

class Question() {
    companion object{
        @JvmStatic
        val fileExtension = "qson"
    }
    var questionText: String = ""
    var correctAnswer: String = ""
    var answers = mutableListOf<String>()

    private val questionFolder = "questions"

    override fun toString(): String {
        return toJson()
    }

    fun validate(): Boolean {
        return  (
            questionText.trim().isNotEmpty()
            || correctAnswer.trim().isNotEmpty()
            || answers.size > 0
        )
    }

    fun writeToDisk(name: String) {
        val directory = File("./$questionFolder")
        directory.mkdirs()
        File("$questionFolder/$name.$fileExtension").writeText(toJson())
    }

    fun getQuestion(name: Int): Question {
        val gson = Gson()
        return gson.fromJson(File("$questionFolder/$name.$fileExtension").readText(), Question::class.java)
    }

    private fun toJson(): String {
        val gson = Gson()
        val simpleJson = mapOf(
            "questionText" to questionText,
            "correctAnswer" to correctAnswer,
            "answers" to answers
        )
        return gson.toJson(simpleJson)
    }
}