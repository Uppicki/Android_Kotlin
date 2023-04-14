package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "\nQuestion viewModel: "

class QuestionViewModel : ViewModel() {

    init {
        Log.d(TAG, "View model instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "View model instance about to be destroyed")
    }

    var currentIndex = 0
    var isCheater = false
    private val userAnswers = mutableListOf<Question>()

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val isAnswered: Boolean
        get() = userAnswers.firstOrNull {
            it.textResId == questionBank[currentIndex].textResId
        } != null

    val theEnd: Boolean
        get() = userAnswers.size == questionBank.size

    val correctAnswers: Int
        get() {
            val res = userAnswers.filter { el ->
                questionBank.first { inner ->
                    inner.textResId == el.textResId
                }.answer == el.answer
            }.size
            userAnswers.clear()
            return res
        }

    val countQuestions: Int
        get() = questionBank.size


    private val questionBank = listOf(
        Question(R.string.question_text, true),
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )


    fun saveUserAnswer(answer: Boolean): Boolean {
        userAnswers.add(Question(questionBank[currentIndex].textResId,answer))
        return answer == questionBank[currentIndex].answer
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if ((currentIndex - 1) >= 0)
            currentIndex - 1
        else
            questionBank.size - 1
    }
}