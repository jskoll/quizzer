package quizzer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QuestionTest {
    @Test fun validQuestion() {
        val question = Question()
        question.questionText = "ABC123"
        question.answers = mutableListOf("0", "1", "2")
        question.correctAnswer = "2"
        assertTrue { question.validate() }
    }

    @Test fun  emptyQuestionValidateTest() {
        val question = Question();
        assertFalse { question.validate() }
    }

    @Test fun invalidQuestionAnswer() {
        val question = Question()
        question.questionText = "ABC123"
        question.answers = mutableListOf("0", "1")
        question.correctAnswer = "2"
        assertFalse { question.validate() }
    }

    @Test fun invalidQuestionAnswers(){
        val question = Question()
        question.questionText = "ABC123"
        question.answers = mutableListOf()
        question.correctAnswer = "2"
        assertFalse { question.validate() }
    }
}