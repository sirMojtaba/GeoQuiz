package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class CheatActivity extends AppCompatActivity {

    public static final String EXTRA_IS_CHEAT = "com.example.myapplication.isCheated";

    private TextView mTextViewAnswer;
    private Button mButtonCheat;
    private Button mButtonBack;

    private boolean mIsAnswerTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        Intent intent = getIntent();
        //initialization
        mIsAnswerTrue = intent.getBooleanExtra(QuizActivity.EXTRA_KEY_IS_ANSWER_TRUE, false);

        findAllViews();
        setClickListeners();
    }

    private void findAllViews() {
        mTextViewAnswer = findViewById(R.id.text_view_show_answer);
        mButtonCheat = findViewById(R.id.button_show_cheat);
        mButtonBack = findViewById(R.id.button_back);
    }

    private void setClickListeners() {
        mButtonCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAnswerTrue) {
                    mTextViewAnswer.setText(R.string.button_true);
                } else {
                    mTextViewAnswer.setText(R.string.button_false);
                }

                saveResult(true);
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveResult(boolean isCheated) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_CHEAT, isCheated);

        //3. save result in activity
        setResult(RESULT_OK, intent);

        //4. os will automatically send result back to parent.
    }
}