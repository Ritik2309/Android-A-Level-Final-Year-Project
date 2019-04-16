package com.ritik.finalproject;

        import android.app.AlertDialog;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
        import java.util.ArrayList;

public class CreateQuizFragement extends Fragment {

    QuizDBHelper MyQuizDB;
    EditText editQuestionNum, editQuestion, editAnswer, editAnswer2, editAnswer3, correctAnsNum;
    Button AddQuestion_Btn, ViewQuestion_Btn, EditQuestion_Btn, DeleteQuestion_Btn;
    public ArrayList<Quiz> quizInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        MyQuizDB = new QuizDBHelper(getActivity());
        quizInfo = new ArrayList<>();
//Initializing the different buttons on the XML file so they can be used in this java class and they will effect the app when interacting.
        View v = inflater.inflate(R.layout.fragment_createquiz, container, false);
        editQuestionNum = v.findViewById(R.id.QuestionNumTextView);
        editQuestion = v.findViewById(R.id.QuestionTextView);
        editAnswer = v.findViewById(R.id.AnswerTextView);
        editAnswer2 = v.findViewById(R.id.AnswerTextView2);
        editAnswer3 = v.findViewById(R.id.AnswerTextView3);
        correctAnsNum = v.findViewById(R.id.correctAnswerTextView);
        AddQuestion_Btn = v.findViewById(R.id.AddQuestionBtn);
        ViewQuestion_Btn = v.findViewById(R.id.ViewQuestionBtn);
        EditQuestion_Btn = v.findViewById(R.id.EditQuestionBtn);
        DeleteQuestion_Btn = v.findViewById(R.id.DelQuestionBtn);
        AddData();
        ViewData();
        UpdateData();
        DeleteNote();

        return v;
    }

    public void AddData()
    {
        //This method is called in the onCreateView so that when the add question button is pressed the user data
        //is taken and a method from the QuizDBHelper is called.
        AddQuestion_Btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = MyQuizDB.insertData(editQuestionNum.getText().toString(),
                                editQuestion.getText().toString(),
                                editAnswer.getText().toString(),
                                editAnswer2.getText().toString(),
                                editAnswer3.getText().toString(),
                                correctAnsNum.getText().toString());

                        if (isInserted == true)
                        {
                            Toast.makeText(getActivity(),"Question made!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Question not made!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }

    public void ViewData()
    {//This method is called in the onCreateView so that when the view button is pressed the user data
        //is displayed by calling a method from the QuizDBHelper class.
        ViewQuestion_Btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor result = MyQuizDB.getAllData();
//If the returned value is 0 then the app will display an appropriate message in the popup box of where the data should be displayed.
                        if (result.getCount() == 0)
                        {
                            showMessage("Error", "Nothing Found in Quiz");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
//Here for as long as there are questions, the data for each row is displayed and the code below formats this.
                        while(result.moveToNext())
                        {
                            buffer.append("QuestionNumber: " + result.getString(0) + "\n");
                            buffer.append("Question: " + result.getString(1) + "\n");
                            buffer.append("Option 1: " + result.getString(2) + "\n");
                            buffer.append("Option 2: " + result.getString(3) + "\n");
                            buffer.append("Option 3: " + result.getString(4) + "\n");
                            buffer.append("Correct Ans Num: " + result.getString(5) + "\n");
                            buffer.append("\n");

                        }
                        showMessage("Quiz", buffer.toString());
                        //shows all the data in the database by calling showMessage.
                    }
                }
        );
    }
    public void UpdateData()
    {
        //This method is called in the onCreateView so that when the edit button is pressed the user data
        //is updated for a particular question.
        EditQuestion_Btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean hasUpdated = MyQuizDB.updateData(editQuestionNum.getText().toString(),
                                editQuestion.getText().toString(),
                                editAnswer.getText().toString(),
                                editAnswer2.getText().toString(),
                                editAnswer3.getText().toString(),
                                correctAnsNum.getText().toString());
//The return value of the QuizDBHelper method is set to a boolean as this is what it returns, therefore if it is positive then we respond with an appropriate message.
                        if (hasUpdated == true)
                        {
                            Toast.makeText(getActivity(),"Question changed!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Question not changed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    public void DeleteNote()
    {
        DeleteQuestion_Btn.setOnClickListener(
                //This method is called in the onCreateView so that when the delete button is pressed the user data
                //is deleted for a particular question.

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = MyQuizDB.deleteData(editQuestionNum.getText().toString());

                        quizInfo.remove(editQuestionNum.getText());
                         //Depending on the return value the appropriate message is displayed below.
                        if (deletedRows > 0)
                        {
                            Toast.makeText(getActivity(),"Question Deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Question not Deleted, please check the Question Number entered!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
        );
    }

    public void showMessage(String title, String Message)
    {
        //This method creates a popup box that can be used to display whatever is needed, it formats how the box is formatted.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}
