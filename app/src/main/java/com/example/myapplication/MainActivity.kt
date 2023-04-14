package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProviders

private const val TAG = "\nMain activity: "
private const val KEY_INDEX = "KEY_INDEX"
private const val REQUEST_CODE_CHEAT = 0
private const val EXTRA_ANSWER_SHOWN = "com.example.android.myapplication.answer_shown"


class MainActivity : AppCompatActivity() {

    private lateinit var trueBtn: Button
    private lateinit var falseBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var prevBtn: Button
    private lateinit var cheatBtn: Button

    private lateinit var questionTextView: TextView

    private val questionViewModel: QuestionViewModel by lazy {
        ViewModelProviders.of(this)[QuestionViewModel::class.java]
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            Log.d(TAG, "Answer is ${it.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false}")

            if (it.resultCode == REQUEST_CODE_CHEAT) {
                questionViewModel.isCheater =
                    it.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "on create called")
        setContentView(R.layout.activity_main)

        Log.i(TAG, savedInstanceState?.getInt(KEY_INDEX, 0).toString())
        val currentI = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        questionViewModel.currentIndex = currentI

        trueBtn = findViewById(R.id.true_btn)
        falseBtn = findViewById(R.id.false_btn)

        nextBtn = findViewById(R.id.next_btn)
        prevBtn = findViewById(R.id.prev_btn)

        cheatBtn = findViewById(R.id.cheat_btn)

        questionTextView = findViewById(R.id.question_text)

        trueBtn.setOnClickListener {
            checkAnswer(true)
            updateUI()
        }
        falseBtn.setOnClickListener {
            checkAnswer(false)
            updateUI()
        }

        nextBtn.setOnClickListener {
            updateNextQuestion()
            updateUI()
        }
        prevBtn.setOnClickListener {
            updatePrevQuestion()
            updateUI()
        }

        cheatBtn.setOnClickListener {
            val answerIsTrue = questionViewModel.currentQuestionAnswer
            val intent = ChaeteActivity.newIntent(this@MainActivity, answerIsTrue)
            getResult.launch(intent)
        }

        updateUI()
    }

    private fun updateUI() {
        questionTextView.setText(questionViewModel.currentQuestionText)
        if (questionViewModel.isAnswered) {
            trueBtn.visibility = View.GONE
            falseBtn.visibility = View.GONE
        } else {
            trueBtn.visibility = View.VISIBLE
            falseBtn.visibility = View.VISIBLE
        }
    }


    private fun checkAnswer(userAnswer: Boolean) {
        val res = questionViewModel.saveUserAnswer(userAnswer)

        val messageID = if (res) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(
            applicationContext,
            messageID,
            Toast.LENGTH_SHORT
        ).show()

        if (questionViewModel.theEnd) {
            Toast.makeText(
                applicationContext,
                "Правильно ${questionViewModel.correctAnswers} / ${questionViewModel.countQuestions}",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun updateNextQuestion() {
        questionViewModel.moveToNext()
    }

    private fun updatePrevQuestion() {
        questionViewModel.moveToPrev()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, questionViewModel.currentIndex)
        Log.i(TAG, outState.getInt(KEY_INDEX, 0).toString())
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}