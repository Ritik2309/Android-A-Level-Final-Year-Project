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

public class NotesFragement extends Fragment {

    NotesDBHelper MyNotesDB;

    EditText editDate, editSubject, editNote;
    Button AddNoteBtn, ViewNotesBtn, EditNotesBtn, DeleteNotesBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        MyNotesDB = new NotesDBHelper(getActivity());
//Initializing the different buttons on the XML file so they can be used in this java class and they will effect the app when interacting.
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        editDate = v.findViewById(R.id.editTextDate);
        editSubject = v.findViewById(R.id.editTextSubject);
        editNote= v.findViewById(R.id.editTextNote);
        AddNoteBtn = v.findViewById(R.id.AddNoteButton);
        ViewNotesBtn = v.findViewById(R.id.ViewNotesBtn);
        EditNotesBtn = v.findViewById(R.id.EditNoteBtn);
        DeleteNotesBtn = v.findViewById(R.id.DeleteNoteBtn);
        AddData();
        ViewData();
        UpdateData();
        DeleteNote();

        return v;
    }

    public void AddData()
    {
        //This method is called in the onCreateView so that when the add note button is pressed the user data
        //is taken and a method from the NotesDBHelper is called.
        AddNoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = MyNotesDB.insertData(editDate.getText().toString(),
                                editSubject.getText().toString(),
                                editNote.getText().toString());
                        if (isInserted == true)
                        {
                            Toast.makeText(getActivity(),"Note made!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Note not made, please check the Note ID!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }

    public void ViewData()
    {
        //This method is called in the onCreateView so that when the view notes button is pressed the user data
        //is displayed by calling a method from the NotesDBHelper class.
        ViewNotesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor result = MyNotesDB.getAllData();
//If the returned value is 0 then the app will display an appropriate message in the popup box of where the data should be displayed.
                        if (result.getCount() == 0)
                        {
                            showMessage("Error", "Nothing Found in Notes");
                            return;
                        }
                        //Here for as long as there are notes, the data for each row is displayed and the code below formats this.

                        StringBuffer buffer = new StringBuffer();

                        while(result.moveToNext())
                        {
                            buffer.append("Note ID: " + result.getString(0) + "\n");
                            buffer.append("Subject: " + result.getString(1) + "\n");
                            buffer.append("Note: " + result.getString(2) + "\n");
                            buffer.append("\n");
                        }
                        showMessage("Notes", buffer.toString());
                        //shows all the data in the database.
                    }
                }
        );
    }
    public void UpdateData()
    {
        //This method is called in the onCreateView so that when the edit note button is pressed the user data
        //is displayed by calling a method from the NotesDBHelper class.
        EditNotesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean hasUpdated = MyNotesDB.updateData(editDate.getText().toString(),
                                editSubject.getText().toString(),
                                editNote.getText().toString());


                        if (hasUpdated == true)
                        {
                            Toast.makeText(getActivity(),"Note changed!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Note not changed, please check the Note ID!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    public void DeleteNote()
    {
        //This method is called in the onCreateView so that when the delete note button is pressed the user data
        //is displayed by calling a method from the NotesDBHelper class.
        DeleteNotesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = MyNotesDB.deleteData(editDate.getText().toString());
                        //Depending on the return integer value the appropriate message is displayed below.
                        if (deletedRows > 0)
                        {
                            Toast.makeText(getActivity(),"Note Deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Note not Deleted, please check the Note ID!", Toast.LENGTH_SHORT).show();
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
