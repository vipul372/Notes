package com.example.vipul.sahredpreferencenote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static final ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.list_view);
        notes.add("Example notes");

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.vipul.sahredpreferencenote", MODE_PRIVATE);

        //sharedpreferences can only use SET to store data.
        set = sharedPreferences.getStringSet("notes", null);
        notes.clear();

        if(set != null) {

            //insert content of set into Arraylist
            notes.addAll(set);

        }

        else {

            notes.add("example notes");
            set = new HashSet<String>();
            set.addAll(notes);
            sharedPreferences.edit().putStringSet("notes", set).apply();

        }


        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent= new Intent(getApplicationContext(),EditYourNote.class);
                intent.putExtra("noteId", position);
                startActivity(intent);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                notes.remove(position);

                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.example.vipul.sahredpreferencenote", Context.MODE_PRIVATE);

                                if(set==null) {

                                    set = new HashSet<String>();
                                }
                                else{
                                    set.clear();
                                }

                                set.addAll(notes);
                                //remove the saved note before edit
                                sharedPreferences.edit().remove("notes").apply();
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes.add("");
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.vipul.sahredpreferencenote", Context.MODE_PRIVATE);

                if(set==null) {

                    set = new HashSet<String>();
                }
                else{
                    set.clear();
                }

                set.addAll(notes);
                //remove the saved note before edit
                sharedPreferences.edit().remove("notes").apply();
                sharedPreferences.edit().putStringSet("notes", set).apply();
                arrayAdapter.notifyDataSetChanged();

                Intent intent= new Intent(getApplicationContext(),EditYourNote.class);
                intent.putExtra("noteId", notes.size()-1);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
