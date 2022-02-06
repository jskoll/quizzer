/******************************************************************************
 * Question class for Quizzer
 *
 * This file is a very simple class that represents a question that can be
 * asked during a quiz
 *
 * The class contains:
 *      1. The text (String) of the question
 *      2. A list<String> of possible answers
 *      3. The index (as a String) of the correct answer
 *
 *  The class also includes a few member methods:
 *      1. toString that returns a JSON formatted string of the question
 *      2. validate that validates that the questions provided are valid
 *          - has question text
 *          - has at least 1 answer in the answer list
 *          - has a correctAnswer index that exists in the answer list
 *
 *  @author Jason Skollingsberg <js032@mix.wvu.edu>
 *  @since Feb 5, 2022
 *  @version 1.0.0-beta
 ****************************************************************************/

package quizzer

import com.google.gson.Gson

class Question() {
    var questionText: String = ""
    var correctAnswer: String = ""
    var answers = mutableListOf<String>()

    /**
     * @return a JSON formated string
     */
    override fun toString(): String {
        val gson = Gson()
        val simpleJson = mapOf(
            "questionText" to questionText,
            "correctAnswer" to correctAnswer,
            "answers" to answers
        )
        return gson.toJson(simpleJson)
    }

    /**
     * @return Boolean indicating the question is valid or not
     */
    fun validate(): Boolean = (
        questionText.trim().isNotEmpty()
        || correctAnswer.trim().isNotEmpty()
        || answers.size > 0
        || (correctAnswer.toInt() > 0 && correctAnswer.toInt() in 0..answers.size)
    )
}