package com.example.myapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Question;

public class QuizActivity extends AppCompatActivity {

    public static final String TAG = "QuizActivity";
    public static final String BUNDLE_KEY_CURRENT_INDEX = "currentIndex";
    public static final String EXTRA_KEY_IS_ANSWER_TRUE = "com.example.myapplication.isAnswerTrue";

    public static final int REQUEST_CODE_CHEAT_ACTIVITY = 0;

    private Button mButtonTrue;
    private Button mButtonFalse;
    private Button mButtonNext;
    private Button mButtonPrevious;
    private Button mButtonCheat;
    private TextView mTextViewQuestion;

    private boolean mIsCheated = false;
    private int mCurrentIndex = 0;
    private Question[] mQuestionBank = {
            new Question(R.string.question_australia, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, false),
            new Question(R.string.question_asia, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        Log.d(TAG, "Bundle: " + savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(BUNDLE_KEY_CURRENT_INDEX);
        }

        //inflate: convert layout xml to actual java objects to be displayed
        setContentView(R.layout.activity_quiz);

        //it must be the first task we do after inflate
        findAllViews();
        setClickListeners();

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");

        outState.putInt(BUNDLE_KEY_CURRENT_INDEX, mCurrentIndex);

    }

    //5. when child activity is finished this method will be called automatically by OS.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null)
            return;

        //result backed from cheate
        if (requestCode == REQUEST_CODE_CHEAT_ACTIVITY) {
            mIsCheated = data.getBooleanExtra(CheatActivity.EXTRA_IS_CHEAT, false);
        }
    }

    //always used mandatory
    private void findAllViews() {
        mButtonTrue = findViewById(R.id.button_true);
        mButtonFalse = findViewById(R.id.button_false);
        mButtonNext = findViewById(R.id.button_next);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonCheat = findViewById(R.id.button_cheat);
        mTextViewQuestion = findViewById(R.id.text_view_question);
    }

    //change question in textview using current question in bank
    private void updateQuestion() {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        mTextViewQuestion.setText(currentQuestion.getTextResId());
    }

    private void setClickListeners() {
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (++mCurrentIndex) % mQuestionBank.length;
                updateQuestion();
                mIsCheated = false;
            }
        });

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (--mCurrentIndex + mQuestionBank.length) % mQuestionBank.length;
                updateQuestion();
                mIsCheated = false;
            }
        });

        mButtonCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheatActivity();
            }
        });
    }

    private void startCheatActivity() {
        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        Intent intent = new Intent(QuizActivity.this, CheatActivity.class);

        //2. send request parameters to child
        intent.putExtra(EXTRA_KEY_IS_ANSWER_TRUE, isAnswerTrue);

        //1. create parent child relations between quiz and cheat activity
        startActivityForResult(intent, REQUEST_CODE_CHEAT_ACTIVITY);
    }

    //check current question answer and show toast.
    private void checkAnswer(boolean userPressed) {
        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        if (mIsCheated) {
            Toast.makeText(this, R.string.toast_judgment, Toast.LENGTH_SHORT).show();
        } else {
            if (userPressed == isAnswerTrue) {
                Toast.makeText(this, R.string.toast_correct, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.toast_incorrect, Toast.LENGTH_LONG).show();
            }
        }
    }
}