package quizzer

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue

class QuizTest {
    @Test fun validFileImport() {
        val quiz = Quiz()
        assertTrue { quiz.createQuestions("src/test/kotlin/quizzer/valid_test.q") }
    }

    @Test fun invalidFileImport() {
        val quiz = Quiz()
        assertFails { quiz.createQuestions("badFile.q") }
    }

//    @Test fun testGetQuestions() {
//        val quiz = Quiz(numberOfQuestions=1)
//        assertNotNull(quiz.getQuestions())
//    }

    @Test fun testMoreQuestionsRequestedThanQuestionsAvail(){
        val quiz = Quiz(numberOfQuestions=10)
        assertFails { quiz.getQuestions() }
    }
}