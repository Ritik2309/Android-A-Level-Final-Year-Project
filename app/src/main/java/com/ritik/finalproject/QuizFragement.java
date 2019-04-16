package com.ritik.finalproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class QuizFragement extends Fragment {


    Button startQuizBtn, addPhotoBTN;
    TextView questionNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
//Initializing the different buttons on the XML file so they can be used in this java class and they will effect the app when interacting.
        startQuizBtn = v.findViewById(R.id.StartQuizBtn);
        questionNumber = v.findViewById(R.id.editQuestionNum);
        addPhotoBTN = v.findViewById(R.id.addPhotoBtn);
        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        return v;
    }

    private void startQuiz()
    {

        //Use of "FragmentTranscation" to call another fragment though a button.
        FragmentTransaction fragment = getFragmentManager().beginTransaction();
        fragment.replace(R.id.fragment_container, new QuizDisplayFragement()); //replaces what the user sees on the app.
        fragment.commit();

    }


}
