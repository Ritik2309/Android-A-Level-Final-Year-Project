package com.ritik.finalproject;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class QuizDisplayFragement extends Fragment {
    public static final long countdown_in_milisec = 30000;

    private static final String key_Score = "Keyscore";
    private static final String key_Question_Count = "KeyQuestionCount";
    private static final String key_Millis_left = "KeyMilliesLeft";
    private static final String key_Answered = "KeyAnswered";
    private static final String key_Question_List = "KeyQuestionList";

    private TextView textViewQuestion, textViewScore, textViewQuestionCount, textViewQuestionCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3;
    private  Button confirmNextBtn, displayHelpImageBtn;
    private ImageView displayImage;

    private ColorStateList textColorDefaultRB;
    private ColorStateList textColorDefaultCD;

    private CountDownTimer countDownTimer;
    private long timeLeftinMiliSec;

    private ArrayList<Quiz> questionList;
    private int questionsCounter;
    private int questionCountTotal;
    private Quiz currentQuestion;
    private int score;

    private Boolean answered;

    PhotosDBHelper MyPhotoDb;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_quizdisplay, container, false);

        MyPhotoDb = new PhotosDBHelper(getActivity());

        textViewQuestion = v.findViewById(R.id.textView_Question);
        textViewScore = v.findViewById(R.id.textViewScore);
        textViewQuestionCount = v.findViewById(R.id.textViewQuestionCount);
        textViewQuestionCountDown = v.findViewById(R.id.textView_CountDown);
        rbGroup = v.findViewById(R.id.radioGroup);
        rb1 = v.findViewById(R.id.radioButton1);
        rb2 = v.findViewById(R.id.radioButton2);
        rb3 = v.findViewById(R.id.radioButton3);
        confirmNextBtn = v.findViewById(R.id.buttonConfirmAnswer);
        displayHelpImageBtn = v.findViewById(R.id.helpBtn);
        displayImage = v.findViewById(R.id.helpImageView);


        textColorDefaultCD = textViewQuestionCountDown.getTextColors();
        textColorDefaultRB = rb1.getTextColors();

        displayHelpImageBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            displayImage.setImageBitmap(MyPhotoDb.getImage(questionsCounter));
                            if (displayImage == null)
                            {
                                Toast.makeText(getActivity(), "Sorry no image found.", Toast.LENGTH_SHORT).show();
                            }

                    }
                }
        );

        if (savedInstanceState == null)
        {
            QuizDBHelper MyQuizDB = new QuizDBHelper(getActivity());
            questionList = MyQuizDB.getAllQuestions();
            questionCountTotal = questionList.size();


            showNextQuestion();
        }
        else
        {
            questionList = savedInstanceState.getParcelableArrayList(key_Question_List);
            if (questionList == null){
                finishQuiz();
            }
            questionCountTotal = questionList.size();
            questionsCounter = savedInstanceState.getInt(key_Question_Count);
            currentQuestion = questionList.get(questionsCounter-1);
            score = savedInstanceState.getInt(key_Score);
            timeLeftinMiliSec = savedInstanceState.getLong(key_Millis_left);
            answered = savedInstanceState.getBoolean(key_Answered);

            if (!answered)
            {
                startCountDown();
            }else{
                updateCountDown();
                showSolution();
            }
        }

        confirmNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the confirm button is pressed then there is a check done to see which option they have selected
                if (!answered)
                {
                    //checks if answer options are checked or not.
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked())
                    {
                        checkAnswer();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    displayImage.setImageBitmap(null); //If they have answered a question then this line of code makes sure that any images in the ImageView are set to null.
                    showNextQuestion();
                }
            }
        });

        return v;
    }

    private  void showNextQuestion()
    {
        //When a new question is asked the colors of the options are set to their default, neither red or green.
        rb1.setTextColor(textColorDefaultRB);
        rb2.setTextColor(textColorDefaultRB);
        rb3.setTextColor(textColorDefaultRB);
        rbGroup.clearCheck();

        if(questionsCounter < questionCountTotal)
        {
            currentQuestion = questionList.get(questionsCounter); //Getting the current question

            textViewQuestion.setText(currentQuestion.getQuestion()); //getting the different question options and setting them.
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionsCounter++;
           //The code below sets up some of hte formatting for the interface.
            textViewQuestionCount.setText("Question: " + questionsCounter + "/" + questionCountTotal);

            answered = false;
            confirmNextBtn.setText("Confirm");

            timeLeftinMiliSec = countdown_in_milisec;
            startCountDown();
        }
        else
        {
            finishQuiz();
        }
    }
    private void startCountDown()
    {
        countDownTimer = new CountDownTimer(timeLeftinMiliSec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMiliSec = millisUntilFinished;
                updateCountDown();
            }

            @Override
            public void onFinish() {
                timeLeftinMiliSec = 0; //To make sure zero seconds are displayed when the countdown time is done.
                updateCountDown();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDown()
    {
        int minutes = (int) (timeLeftinMiliSec/1000)/ 60; //To get the time left in minutes
        int seconds = (int) (timeLeftinMiliSec/1000) % 60; //Use modulus function to get the seconds only and not the minutes

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds); // Formats the minutes and seconds


        textViewQuestionCountDown.setText(timeFormatted);

        if (timeLeftinMiliSec < 10000)
        {
            textViewQuestionCountDown.setTextColor(Color.RED); //sets the color of the timer red when it is less than 10 seconds
        }
        else
        {
            textViewQuestionCountDown.setTextColor(textColorDefaultCD);

        }
    }

    private void checkAnswer()
    {
        answered = true;

        countDownTimer.cancel(); //stops timer for that question

        RadioButton rbselected = getView().findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) + 1;
        if (answerNr == currentQuestion.getCorrect_Ans_Num())
        {
            score++;
            textViewScore.setText("Score: " + score );
        }
        showSolution();
    }
    private void showSolution()
    {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getCorrect_Ans_Num())
        {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("option 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("option 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("option 3 is correct");
                break;
        }

        if(questionsCounter < questionCountTotal)
        {
            confirmNextBtn.setText("Next");
        }
        else
        {
            confirmNextBtn.setText("Finish");
        }

    }

    private void finishQuiz()
    {
        //This method swaps the quiz fragment container to the start quiz fragment when the questions have run out.
        FragmentTransaction fragment = getFragmentManager().beginTransaction();
        fragment.replace(R.id.fragment_container, new QuizFragement());
        fragment.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
        {
            countDownTimer.cancel(); //We will get a crash if we don't cancel the timer when the fragment is closed.
        }
    }


}
