package com.example.vipul.sahredpreferencenote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class EditYourNote extends AppCompatActivity implements TextWatcher {

    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText editText=  findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId",-1);

        if(noteId!=-1)
            editText.setText(MainActivity.notes.get(noteId));

        editText.addTextChangedListener(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence cs, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int before, int count) {

        MainActivity.notes.set(noteId, String.valueOf(cs));
        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.vipul.sahredpreferencenote", Context.MODE_PRIVATE);

        if(MainActivity.set==null) {

            MainActivity.set = new HashSet<String>();
        }
        else{
            MainActivity.set.clear();
        }

        MainActivity.set.addAll(MainActivity.notes);

        //remove the saved note before edit
        sharedPreferences.edit().remove("notes").apply();
        //save the notes after editing
        sharedPreferences.edit().putStringSet("notes", MainActivity.set).apply();


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
