package com.example.madison.mapexample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/*
This activiy displays the classes and allows them to be deleted
 */

public class viewSchedule extends AppCompatActivity {

    ListView listView;
    ClassDbAdapter classDbAdapter;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    int passId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        listView = (ListView)findViewById(R.id.listView2);

        classDbAdapter = new ClassDbAdapter(this);
        classDbAdapter.open();
        cursor = classDbAdapter.getClassType();


        // Special listview, adapter combination which populates a listview
        adapter = new SimpleCursorAdapter(this,
                R.layout.customlist,
                cursor,
                new String[] {ClassDbAdapter.CLASSNAME, ClassDbAdapter.LOCATION},
                new int[]{R.id.fN,R.id.lN});

        listView.setAdapter(adapter);

        // delete the Entry if clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                passId = (int)id;

                new AlertDialog.Builder(viewSchedule.this)
                        .setTitle("Delete Entry?")
                        .setMessage("Would you like to permanantly remove this entry?")
                        .setIcon(R.drawable.app_logo)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                classDbAdapter.deleteClass((int) passId);
                                finish();
                                startActivity(getIntent());

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

    }

    // Return to the home screen
    public void goHome(View view) {
        finish();
    }

    // Go to the class add activity
    public void goAdd(View view) {
        Intent modifyIntent = new Intent(this, ScheduleInput.class);
        startActivityForResult(modifyIntent, 101);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        listView.setAdapter(null);

        if(cursor !=null){
            cursor.close();
        }

        if(classDbAdapter!=null){
            classDbAdapter.close();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 101) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());

            }
        }
    }
}
