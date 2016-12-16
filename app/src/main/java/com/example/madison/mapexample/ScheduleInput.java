package com.example.madison.mapexample;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

/*
This activity has the user input their schedule
*/
public class ScheduleInput extends AppCompatActivity  implements RecurrencePickerDialogFragment.OnRecurrenceSetListener {

    // A gross number of variables need to be accessible across various methods
    static final int TIME_DIALOG_ID = 999;
    private int startHour = 0;
    private int startMinute = 0;
    private int endHour = 0;
    private int endMinute = 0;
    private int hour;
    private int minute;

    public String freqSelected;
    public String countSelected = "null";
    public String intervalSelected;
    public String untilSelected = "null";
    public String daysSelected;
    public String classDescriptor;
    private String locationSelected;

    private int sunday = 0;
    private int monday = 0;
    private int tuesday = 0;
    private int wednesday = 0;
    private int thursday = 0;
    private int friday = 0;
    private int saturday = 0;

    private int f1 = 0;
    private int f2 = 0;
    private int f3 = 0;
    private int f4 = 0;

    private int whichTime = 0;

    // These are the various textviews that will get updated
    private TextView sTime;
    private TextView eTime;
    private TextView days;
    private TextView loc;
    private TextView repetition;
    private TextView until;

    private EditText classField;

    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";
    private EventRecurrence mEventRecurrence = new EventRecurrence();
    private String mRrule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_input);

        // Setting the Views up so we can edit them in any method
        sTime = (TextView) findViewById(R.id.textView8);
        eTime = (TextView) findViewById(R.id.textView9);
        days = (TextView) findViewById(R.id.textView10);
        repetition = (TextView) findViewById(R.id.textView12);
        until = (TextView) findViewById(R.id.textView13);
        loc = (TextView) findViewById(R.id.textView11);
        classField = (EditText) findViewById(R.id.ClassField);
    }

    // Initiates the popup Start Time entry
    public void startTime(View view) {
        whichTime = 1;
        showDialog(TIME_DIALOG_ID);
    }

    // Initiates the popup End Time entry
    public void endTime(View view) {
        whichTime = 2;
        showDialog(TIME_DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {

                    // Start Time
                    if (whichTime == 1){
                        startHour = selectedHour;
                        startMinute = selectedMinute;
                        if (startMinute !=0) {
                            sTime.setText("Start Time: " + startHour + ":" + startMinute + " (24hr)");
                        }
                        else {
                            sTime.setText("Start Time: " + startHour + ":" + startMinute + "0 (24hr)");
                        }
                        f1 = 1;
                    }
                    // End Time
                    if (whichTime == 2){
                        endHour = selectedHour;
                        endMinute = selectedMinute;
                        if (endMinute != 0) {
                            eTime.setText("End Time: " + endHour + ":" + endMinute + " (24hr)");
                        }
                        else {
                            eTime.setText("End Time: " + endHour + ":" + endMinute + "0 (24hr)");
                        }
                        f2 = 1;
                    }

                    // Checking if every other field has been entered
                    if (f1 ==1 && f2 ==1 && f3 == 1 && f4 == 1 && (TextUtils.isEmpty(classField.getText().toString()) != true)){
                        new AlertDialog.Builder(ScheduleInput.this)
                                .setTitle("Add Entry")
                                .setMessage("Would you like to add " + locationSelected + " to your schedule?")
                                .setIcon(R.drawable.app_logo)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Add The Entry
                                        classDescriptor = classField.getText().toString();
                                        returnSuccess();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing
                                    }
                                })
                                .show();
                    }
                }
            };

    // Overkill day picker with waaaay too many options (All code from external library)
    public void pickDay(View view) {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        android.text.format.Time time = new android.text.format.Time();
        time.setToNow();
        bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);

        // may be more efficient to serialize and pass in EventRecurrence
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule);

        RecurrencePickerDialogFragment rpd = (RecurrencePickerDialogFragment) fm.findFragmentByTag(FRAG_TAG_RECUR_PICKER);
        if (rpd != null) {
            rpd.dismiss();
        }
        rpd = new RecurrencePickerDialogFragment();
        rpd.setArguments(bundle);
        rpd.setOnRecurrenceSetListener(ScheduleInput.this);
        rpd.show(fm, FRAG_TAG_RECUR_PICKER);
    }

    // Return Function from the DayPicker (More Voodoo magic that requires digging into the betterPickersLib)
    @Override
    public void onRecurrenceSet(String rrule) {

        sunday = 0;
        monday = 0;
        tuesday = 0;
        wednesday = 0;
        thursday = 0;
        friday = 0;
        saturday = 0;
        freqSelected = "";
        countSelected = "null";
        intervalSelected = "1";
        untilSelected = "null";
        daysSelected = "";

        String customS;
        String dayS[];
        int lengthArray;
        int lengthDay;
        int x = 0;
        int y = 0;
        mRrule = rrule;


        if (mRrule != null) {
            mEventRecurrence.parse(mRrule);
            Log.d("HJ", "" + mEventRecurrence.toString());
            customS = mEventRecurrence.toString();
            String[] r = customS.split("=|;");
            lengthArray = r.length;

            for (x = 0; x < lengthArray; x ++){

                // We know the next value is the freqeuncy
                if (r[x].equals("FREQ")){
                    freqSelected = r[x+1].toString();


                }

                // We know that the next position is the interval
                if (r[x].equals("INTERVAL")){
                    intervalSelected = r[x+1];

                }

                // We know that the following string are the days
                if (r[x].equals("BYDAY")){
                    dayS = r[x+1].split(",");
                    daysSelected = r[x+1];
                    lengthDay = dayS.length;

                    for (y = 0; y < lengthDay; y ++){

                        if (dayS[y].equals("SU")) {
                            sunday = 1;
                        }
                        if (dayS[y].equals("MO")) {
                            monday = 1;
                        }
                        if (dayS[y].equals("TU")) {
                            tuesday = 1;
                        }
                        if (dayS[y].equals("WE")) {
                            wednesday = 1;
                        }
                        if (dayS[y].equals("TH")) {
                            thursday = 1;
                        }
                        if (dayS[y].equals("FR")) {
                            friday = 1;
                        }
                        if (dayS[y].equals("SA")) {
                            saturday = 1;
                        }
                    }
                }

                // Check to see if the count is set
                if (r[x].equals("COUNT")){
                    countSelected = r[x+1];
                }

                // Check to see if the until method is set
                if (r[x].equals("UNTIL")){
                    untilSelected = r[x+1];
                    untilSelected = untilSelected.substring(0, 8);
                }

            }

            /* Good Debugging Logs
            Log.d("Sun", "" + sunday);
            Log.d("mon", "" + monday);
            Log.d("tue", "" + tuesday);
            Log.d("wed", "" + wednesday);
            Log.d("thu", "" + thursday);
            Log.d("fri", "" + friday);
            Log.d("Sat", "" + saturday);

            Log.d("freq", freqSelected);
            Log.d("until", untilSelected);
            Log.d("interval", intervalSelected);
            Log.d("count", countSelected);
            */


            // Setting the Textviews to appropriate strings
            days.setText("Days: " + daysSelected);

            if (intervalSelected.equals("1")) {
                repetition.setText("Every Week");
            }
            else {
                repetition.setText("Every " + intervalSelected + " Weeks");
            }

            if (untilSelected.equals("null")){

                if (countSelected.equals("")){
                    until.setText("Forever");
                }
                else
                until.setText("Complete Count: " + countSelected);

            }
            else {
                until.setText("Until: " + untilSelected.substring(0,4) + "/" + untilSelected.substring(4,6) + "/" + untilSelected.substring(6,8));
            }
            f3 = 1;

            // Checking to see if every has been completed on the form
            if (f1 ==1 && f2 ==1 && f3 == 1 && f4 == 1 && (TextUtils.isEmpty(classField.getText().toString()) != true)){
                new AlertDialog.Builder(ScheduleInput.this)
                        .setTitle("Add Entry")
                        .setMessage("Would you like to add " + locationSelected + " to your schedule?")
                        .setIcon(R.drawable.app_logo)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Add Entry
                                classDescriptor = classField.getText().toString();
                                returnSuccess();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })

                        .show();
            }

        }
        populateRepeats();
    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        RecurrencePickerDialogFragment rpd = (RecurrencePickerDialogFragment) getSupportFragmentManager().findFragmentByTag(
                FRAG_TAG_RECUR_PICKER);
        if (rpd != null) {
            rpd.setOnRecurrenceSetListener(this);
        }
    }

    // Related to the Picker Library
    private void populateRepeats() {
        Resources r = getResources();
        String repeatString = "";
        boolean enabled;
        if (!TextUtils.isEmpty(mRrule)) {
            repeatString = EventRecurrenceFormatter.getRepeatString(this, r, mEventRecurrence, true);

        }
    }

    // Calls the Map Activity with an expected return!
    public void selectLocation(View view) {
        Intent intent = new Intent (this, MapsActivity.class);
        intent.putExtra("WHICH_SENT", "1");
        startActivityForResult(intent, 10);
    }

    // Checks to see who responded and if they gave the correct response code (we decide the response code)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == 10) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                locationSelected = data.getStringExtra("result");
                loc.setText(locationSelected);
                f4 = 1;

                // Ask if the user is ready to save their data?
                if (f1 ==1 && f2 ==1 && f3 == 1 && f4 == 1 && (TextUtils.isEmpty(classField.getText().toString()) != true)){
                    new AlertDialog.Builder(ScheduleInput.this)
                            .setTitle("Add Entry")
                            .setMessage("Would you like to add " + locationSelected + " to your schedule?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Add Entry
                                    classDescriptor = classField.getText().toString();
                                    returnSuccess();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing
                                }
                            })

                            .show();
                }
            }
        }
    }

    // Manual button to add the data
    public void addEntry(View view) {

        if (TextUtils.isEmpty(classField.getText().toString())){
            Toast.makeText(ScheduleInput.this, "Please add a Class Descriptor",
                    Toast.LENGTH_SHORT).show();
        }

        else {

            // Check to see if we are ready to add the data
            if (f1 == 1 && f2 == 1 && f3 == 1 && f4 == 1) {
                new AlertDialog.Builder(ScheduleInput.this)
                        .setTitle("Add Entry")
                        .setMessage("Would you like to add " + locationSelected + " to your schedule?")
                        .setIcon(R.drawable.app_logo)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                classDescriptor = classField.getText().toString();
                                returnSuccess();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })

                        .show();
            } else {

                Toast.makeText(ScheduleInput.this, "Missing Some Entries",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Go Back button
    public void backShed(View view) {
        finish();
    }


    // This method stores the values into the Database. It is rather complicated to get the next time of an event!
    private void returnSuccess(){

        // Need to determine when this event will take place next!
        Calendar c = Calendar.getInstance();

        int currentDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDayMonth = c.get(Calendar.DAY_OF_MONTH);
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int currentHour = c.get(Calendar.HOUR) + c.get(Calendar.AM_PM)*12; // 24 hour clock
        int currentMinute = c.get(Calendar.MINUTE);
        int nextEvent = 0;
        int nextDayOfYear = 0;
        int isSet = 0;


        // Current Day is Sunday
        if (currentDay == Calendar.SUNDAY){
            // Sunday is an option, see if event passed yet
            if (sunday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == startHour){
                    // This is the soonest event!
                    if (currentMinute < startMinute){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < startHour){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (monday == 1 && isSet == 0) {
                // The hour is before, see if the minutes are before

                isSet = 1;
                nextDayOfYear = currentDayOfYear + 1;
            }
            if (tuesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (wednesday == 1&& isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (thursday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (friday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (saturday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+6;

            }
            // No next day works, Sunday next week has to be the day
            if (isSet == 0){

                isSet = 1;
                nextDayOfYear = currentDayOfYear+7;
            }
        }

        //***********************************************************************************************//
        // Current Day is Monday
        if (currentDay == Calendar.MONDAY){
            // Sunday is an option, see if event passed yet
            if (monday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == startHour){
                    // This is the soonest event!
                    if (currentMinute < startMinute){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < startHour){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (tuesday == 1 && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (wednesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (thursday == 1&& isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (friday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (saturday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (sunday == 1  && isSet == 0){

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+6;

            }
            // No next day works, Monday next week has to be the day
            if (isSet == 0){
                isSet = 1;
                nextDayOfYear = currentDayOfYear+7;
            }
        }
        //***********************************************************************************************//
        // Current Day is Tuesday
        if (currentDay == Calendar.TUESDAY){
            // Sunday is an option, see if event passed yet
            if (tuesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == startHour){
                    // This is the soonest event!
                    if (currentMinute < startMinute){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < startHour){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (wednesday == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (thursday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (friday == 1&& isSet == 0){

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (saturday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (sunday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (monday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+6;

            }
            // No next day works, Tuesday next week has to be the day
            if (isSet == 0){
                isSet = 1;
                nextDayOfYear = currentDayOfYear+7;
            }
        }
        //***********************************************************************************************//
        // Current Day is Wednesday
        if (currentDay == Calendar.WEDNESDAY){
            // Sunday is an option, see if event passed yet
            if (wednesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == startHour){
                    // This is the soonest event!
                    if (currentMinute < startMinute){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < startHour){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (thursday == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (friday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (saturday == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (sunday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (monday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (tuesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+6;

            }
            // No next day works, Wednesday next week has to be the day
            if (isSet == 0){
                isSet = 1;
                nextDayOfYear = currentDayOfYear+7;
            }
        }
        //***********************************************************************************************//
        // Current Day is Thursday
        if (currentDay == Calendar.THURSDAY){
            // Sunday is an option, see if event passed yet
            if (thursday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == startHour){
                    // This is the soonest event!
                    if (currentMinute < startMinute){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < startHour){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (friday == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (saturday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (sunday == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (monday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (tuesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (wednesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+6;

            }
            // No next day works, Thursday next week has to be the day
            if (isSet == 0){
                isSet = 1;
                nextDayOfYear = currentDayOfYear+7;
            }
        }
        //***********************************************************************************************//
        // Current Day is Friday
        if (currentDay == Calendar.FRIDAY){

            // Sunday is an option, see if event passed yet
            if (friday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == startHour){
                    // This is the soonest event!
                    if (currentMinute < startMinute){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < startHour){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (saturday == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (sunday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (monday == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (tuesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (wednesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (thursday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+6;

            }
            // No next day works, Friday next week has to be the day
            if (isSet == 0){
                isSet = 1;
                nextDayOfYear = currentDayOfYear+7;
            }
        }
        //***********************************************************************************************//
        // Current Day is Saturday
        if (currentDay == Calendar.SATURDAY){
            // Sunday is an option, see if event passed yet
            if (saturday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == startHour){
                    // This is the soonest event!
                    if (currentMinute < startMinute){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < startHour){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (sunday == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (monday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (tuesday == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (wednesday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (thursday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (friday == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+6;

            }
            // No next day works, Saturday next week has to be the day
            if (isSet == 0){
                isSet = 1;
                nextDayOfYear = currentDayOfYear+7;
            }
        }

        // Now that we know what the next day is, we need to check for rollovers for every day/month combiniation

      if (nextDayOfYear > 366){
          nextDayOfYear = nextDayOfYear - 366;
      }

        // Finalizing the values based on rollovers
        nextEvent = nextDayOfYear;

        // Add the newly created classType
        ArrayList<classType> ct = classType.addContact(classDescriptor,
                freqSelected,
                countSelected,
                "0",
                intervalSelected,
                intervalSelected,
                untilSelected,
                daysSelected,
                locationSelected,
                sunday,
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                0,
                0,
                0,
                startHour,
                startMinute,
                endHour,
                endMinute,
                nextEvent);

        // Open the Database to be used
        ClassDbAdapter classDbAdapter = new ClassDbAdapter(ScheduleInput.this);

        classDbAdapter.open();

        // Could add more than one entry at a time!
        for(classType con: ct){
            ContentValues newValues = new ContentValues();
            newValues.put(ClassDbAdapter.CLASSNAME, con.classname);
            newValues.put(ClassDbAdapter.FREQ, con.freq);
            newValues.put(ClassDbAdapter.COUNT, con.count);
            newValues.put(ClassDbAdapter.CURRENTCOUNT, con.currentcount);
            newValues.put(ClassDbAdapter.INTERVAL, con.interval);
            newValues.put(ClassDbAdapter.CURRENTINTERVAL, con.currentinterval);
            newValues.put(ClassDbAdapter.UNTIL, con.until);
            newValues.put(ClassDbAdapter.DAYS, con.days);
            newValues.put(ClassDbAdapter.LOCATION, con.location);
            newValues.put(ClassDbAdapter.SU, con.su);
            newValues.put(ClassDbAdapter.MO, con.mo);
            newValues.put(ClassDbAdapter.TU, con.tu);
            newValues.put(ClassDbAdapter.WE, con.we);
            newValues.put(ClassDbAdapter.TH, con.th);
            newValues.put(ClassDbAdapter.FR, con.fr);
            newValues.put(ClassDbAdapter.SA, con.sa);
            newValues.put(ClassDbAdapter.MISSED, con.missed);
            newValues.put(ClassDbAdapter.ATTENDED, con.attended);
            newValues.put(ClassDbAdapter.UNSURE, con.unsure);
            newValues.put(ClassDbAdapter.STARTH, con.starth);
            newValues.put(ClassDbAdapter.STARTM, con.startm);
            newValues.put(ClassDbAdapter.ENDH, con.endh);
            newValues.put(ClassDbAdapter.ENDM, con.endm);
            newValues.put(ClassDbAdapter.NEXTDATE, con.nextdate);
            classDbAdapter.insertClass(newValues);
        }

        // Iterates through the entire Class Table for Logging display purposes!
        Cursor classCursor = classDbAdapter.getClassType();
        StringBuilder results = new StringBuilder();
        if(classCursor.moveToFirst()){
            do{
                classType con = classDbAdapter.getClassFromCursor(classCursor);
                results.append(con.classname +" " + con.freq + " " + con.count + " " + con.currentcount + " " + con.interval + " " + con.currentinterval + " " + con.until + " " + con.days + " " + con.location + " " + con.su + " " + con.mo + " " + con.tu + " " + con.we + " " + con.th + " " + con.fr + " " + con.sa + " " + con.missed + " " + con.attended + " " + con.unsure + " " + con.starth + " " + con.startm + " " + con.endh + " " + con.endm + " " + con.nextdate + "\n");
            }while(classCursor.moveToNext());
        }


        Log.d("TAG", results.toString());

        // Return results!
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
