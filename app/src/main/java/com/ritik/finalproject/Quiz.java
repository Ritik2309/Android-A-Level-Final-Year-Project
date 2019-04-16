package com.ritik.finalproject;

import android.os.Parcel;
import android.os.Parcelable;


public class Quiz implements Parcelable {
    private String questionNumber;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private Integer correct_Ans_Num;

    public Quiz() {}


    public Quiz(String questionNumber, String question, String option1, String option2, String option3, Integer correct_Ans_Num) {
        this.questionNumber = questionNumber;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.correct_Ans_Num = correct_Ans_Num;
    }

    protected Quiz(Parcel in) {
        questionNumber = in.readString();
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        if (in.readByte() == 0) {
            correct_Ans_Num = null;
        } else {
            correct_Ans_Num = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionNumber);
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        if (correct_Ans_Num == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(correct_Ans_Num);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public String getQuestionNumber() {
        return questionNumber;
    }

//Below are get and set methods that use the QuizDBHelper database data to set the current questions and different answer options.


    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public Integer getCorrect_Ans_Num() {
        return correct_Ans_Num;
    }

    public void setCorrect_Ans_Num(Integer correct_Ans_Num) {
        this.correct_Ans_Num = correct_Ans_Num;
    }
}
