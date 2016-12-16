package com.example.madison.mapexample;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import static java.lang.Math.abs;

public class scanningService extends Service {

    private boolean isRunning = false;
    protected LocationListener locationListener;
    protected Context context;
    protected LocationManager locationManager;
    private double latt, lonn;

    private classType CT;


    // Setting up variables for the Broadcast servics
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.example.services.services_app.MyForegroundService.FOREGROUND2";
    static final String ACTION_FORGROUND = "com.example.services.services_app.MyForegroundService.FOREGROUND";

    private final Handler handler = new Handler();
    Intent intent2;


    public scanningService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate(){

        // Create Broadcast intent
        intent2 = new Intent(BROADCAST_ACTION);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {

            // Gets the lattitude and longitude
            @Override
            public void onLocationChanged(Location location) {

                latt = location.getLatitude();
                lonn = location.getLongitude();
            }


            // Goes to turn back on location if it is turned off
            @Override
            public void onProviderDisabled(String provider) {
                isRunning = false;
                Intent turnOn = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                turnOn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(turnOn);
            }

            // Enable the thread to run
            @Override
            public void onProviderEnabled(String provider) {
                isRunning = true;
                Log.d("Latitude", "enable");
            }

            // Change of Status
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        // Only ask for permissions if build level is 23 or higher!
        if (Build.VERSION.SDK_INT > 22) {
            // Needs to get the permissions to use fine and course location since API level is 23
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // We cant have a background service request for permissions, it can only check
            }
        }

        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);


        // Thread creation
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Runs on an infinite Loop (i is not increased ever)
                for (int i = 0; i < 60; ) {

                    if(isRunning){
                        Log.i("TAG", "Service running: " + i);
                        Log.i("TAG", "Lat: " + latt);
                        Log.i("TAG", "Long: " + lonn);

                        // Gets the current Time and Date using the Calandar
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time = " +c.get(Calendar.DAY_OF_WEEK) + " " +c.get(Calendar.HOUR) + ":" + +c.get(Calendar.MINUTE) + ":" +c.get(Calendar.SECOND) + " " + +c.get(Calendar.AM_PM));

                        // Need to open the SQLite database
                        ClassDbAdapter classDbAdapter = new ClassDbAdapter(scanningService.this);
                        classDbAdapter.open();

                        // Setting a curose to the Class Table
                        Cursor classCursor = classDbAdapter.getClassType();
                        StringBuilder results = new StringBuilder();

                        // Moving to the first row in the Table
                        if(classCursor.moveToFirst()){
                            do{
                                // extracting the row information in a usable datatype
                                classType con = classDbAdapter.getClassFromCursor(classCursor);

                                // The class is outdated (time & day has passed)
                                if (c.get(Calendar.DAY_OF_YEAR) > con.nextdate) {

                                    // 3 equals unsure flag (we weren't present when this occured)
                                    updateEventTime(con, 3);
                                }

                                // Chcking if today the event day for this class
                                if (c.get(Calendar.DAY_OF_YEAR) == con.nextdate) {

                                    // Check to see if we haven't reached that time yet
                                    if (con.starth == (c.get(Calendar.HOUR) + 12 * c.get(Calendar.AM_PM))) {

                                        // Check to see if the minute has passed.
                                        if (con.startm > (c.get(Calendar.MINUTE))) {

                                            // This is the entry we want to select as the next soonest event!
                                            if (con.startm < 10) {
                                                results.append("Next Class: " + con.classname + " in " + con.location + " at " + con.starth + ":0" + con.startm + "\nAttended: " + con.attended + " -- Missed: " + con.missed + " -- Unsure: " + con.unsure);

                                            } else {
                                                results.append("Next Class: " + con.classname + " in " + con.location + " at " + con.starth + ":" + con.startm + "\nAttended: " + con.attended + " -- Missed: " + con.missed + " -- Unsure: " + con.unsure);
                                            }
                                            CT = con;
                                            break;
                                        }
                                        else {

                                            // Same day, same hour and minute
                                            if (con.startm == (c.get(Calendar.MINUTE))){

                                                // We want to check if the GPS coordiantes match up!
                                                updateEventTime(con, 1);
                                            }
                                            else {

                                                // Minute has passed, update (unsure if class was attended)
                                                updateEventTime(con, 3);
                                            }
                                        }
                                    }

                                    // If the hour is greater than the current hour, this is our next class
                                    else if (con.starth > (c.get(Calendar.HOUR) + 12 * c.get(Calendar.AM_PM))) {
                                        Log.d("found", "found next class");

                                        if (con.startm < 10) {
                                            results.append("Next Class: " + con.classname + " in " + con.location + " at " + con.starth + ":0" + con.startm + "\nAttended: " + con.attended + " -- Missed: " + con.missed + " -- Unsure: " + con.unsure);

                                        } else {
                                            results.append("Next Class: " + con.classname + " in " + con.location + " at " + con.starth + ":" + con.startm + "\nAttended: " + con.attended + " -- Missed: " + con.missed + " -- Unsure: " + con.unsure);

                                        }
                                        CT = con;
                                        break;
                                    }

                                    // The class that we just looked at is outdated. We need to recalculate the next class event trigger
                                    else {
                                        updateEventTime(con, 3);
                                    }
                                }
                                else {
                                    // Class is the next day
                                    if (con.startm < 10) {
                                        results.append("Next Class: " + con.classname + " in " + con.location + " at " + con.starth + ":0" + con.startm + "\nAttended: " + con.attended + " -- Missed: " + con.missed + " -- Unsure: " + con.unsure);

                                    } else {
                                        results.append("Next Class: " + con.classname + " in " + con.location + " at " + con.starth + ":" + con.startm + "\nAttended: " + con.attended + " -- Missed: " + con.missed + " -- Unsure: " + con.unsure);

                                    }
                                    CT = con;
                                    break;
                                }
                            }while(classCursor.moveToNext());
                        }

                        // Close the Database
                        classCursor.close();
                        classDbAdapter.close();


                        // Broadcast this information to other activites and to the AppWidget
                        DisplayLoggingInfo(results.toString());

                    }

                    // We want to sleep for 10 seconds
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                    }
                }
                Log.d("kill", "killing service");
                stopSelf();
            }
        }).start();



    }


    // This method is rather complicated. It determines if we attended or missed a class, and updates the
    // Next event occurance for each class. I could treat it as magic and ignore it!

    public void updateEventTime(classType con, int flag){

        Calendar c = Calendar.getInstance();

        // Variables for the current Year, Day, Month, Day of Week, Hour, Minute
        int currentYear = c.get(Calendar.YEAR);
        int currentDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDayMonth = c.get(Calendar.DAY_OF_MONTH);
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int currentHour = c.get(Calendar.HOUR) + c.get(Calendar.AM_PM)*12; // 24 hour clock
        int currentMinute = c.get(Calendar.MINUTE);

        // Similar variables, except these are extracted from the ClassType passed in
        int nextEvent = 0;
        int nextDayOfYear = 0;
        int isSet = 0;
        int untilYear;
        int untilMonth;
        int untilDay;

        // Latitude and Longitude to check against
        double latCheck;
        double longCheck;

        int currentCount;

        // Unsure if the class was attended or missed
        if (flag == 3){
            con.unsure++;
        }

        // We need to compare the current location to the actual location
        if (flag == 1){

            // GPS can't find out location :(
            if (latt == 0.0 || lonn == 0.0){
                con.unsure++;
            }

            // GPS found our location, we need to check which class we are supposed to be in!
            else {

                if (con.location.equals(getString(R.string.ALEX_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.ALEX_Lat));
                    longCheck = Double.parseDouble(getString(R.string.ALEX_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.RICH_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.RICH_Lat));
                    longCheck = Double.parseDouble(getString(R.string.RICH_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.THRN_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.THRN_Lat));
                    longCheck = Double.parseDouble(getString(R.string.THRN_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.THRN_1006_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.THRN_1006_Lat));
                    longCheck = Double.parseDouble(getString(R.string.THRN_1006_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MAC_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MAC_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MAC_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MCN_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MCN_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MCN_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.SCI_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.SCI_Lat));
                    longCheck = Double.parseDouble(getString(R.string.SCI_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MINS_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MINS_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MINS_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MACL_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MACL_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MACL_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.CROP_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.CROP_Lat));
                    longCheck = Double.parseDouble(getString(R.string.CROP_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.ANSCI_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.ANSCI_Lat));
                    longCheck = Double.parseDouble(getString(R.string.ANSCI_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.OVC_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.OVC_Lat));
                    longCheck = Double.parseDouble(getString(R.string.OVC_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.JTP_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.JTP_Lat));
                    longCheck = Double.parseDouble(getString(R.string.JTP_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.ZAV_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.ZAV_Lat));
                    longCheck = Double.parseDouble(getString(R.string.ZAV_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MACK_N_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MACK_N_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MACK_N_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MACK_S_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MACK_S_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MACK_S_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.GRHM_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.GRHM_Lat));
                    longCheck = Double.parseDouble(getString(R.string.GRHM_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MASS_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MASS_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MASS_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.REY_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.REY_Lat));
                    longCheck = Double.parseDouble(getString(R.string.REY_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.UC_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.UC_Lat));
                    longCheck = Double.parseDouble(getString(R.string.UC_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.ROZ_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.ROZ_Lat));
                    longCheck = Double.parseDouble(getString(R.string.ROZ_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.WMEM_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.WMEM_Lat));
                    longCheck = Double.parseDouble(getString(R.string.WMEM_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
                if (con.location.equals(getString(R.string.MCL_Name))) {
                    latCheck = Double.parseDouble(getString(R.string.MCL_Lat));
                    longCheck = Double.parseDouble(getString(R.string.MCL_Lon));

                    // Checking if we are in class
                    if ((abs(latCheck - latt) < 0.001) && (abs(longCheck - lonn) < 0.001)) {
                        con.attended++;
                    } else {
                        con.missed++;
                    }
                }
            }
        }



        // Update the count variable
        if (con.count.equals("null")){
            // Do nothing, count is not enabled
        }

        else {
            currentCount = Integer.parseInt(con.currentcount);
            currentCount++;

            // Don't let the class occur again if we have reached the count
            if (currentCount == Integer.parseInt(con.count)) {
                Log.d("COUNTMATCH", "setting to 999");
                nextDayOfYear = 999;
                isSet = 1;
            }

            con.currentcount = Integer.toString(currentCount);
        }

        // Check to see if the final day has passed for the event
        if (con.until.equals("null")){
            // Do nothing
        }
        else {
            untilYear = Integer.parseInt(con.until.substring(0,4));
            untilMonth = Integer.parseInt(con.until.substring(4,6));
            untilDay = Integer.parseInt(con.until.substring(6,8));

            if (untilYear <  currentYear){
                // Do not repeat again!
                Log.d("YEAR", "has passed");
                nextDayOfYear = 999;
                isSet = 1;
            }
            else if (untilYear == currentYear){
                if (untilMonth < currentMonth+1){
                    // Do not repeat again!
                    Log.d("MONTH", "has passed");
                    nextDayOfYear = 999;
                    isSet = 1;
                }
                else if (untilMonth == currentMonth+1){

                    if (untilDay <= currentDayMonth){
                        // Do not repeat again! (if its the same day, it must have passed to be in this function!)
                        Log.d("DAY", "has passed");
                        nextDayOfYear = 999;
                        isSet = 1;
                    }
                    else{
                        //Day is greater than current day
                    }
                }
                else {
                    // Month is greater than current month
                }
            }
            else {
                // Year is greater than current year
            }
        }


        // Now we need to check which day it is today, and find the next occurance of the class
        // Here is the voodoo magic!
        if (currentDay == Calendar.SUNDAY){
            // Sunday is an option, see if event passed yet
            if (con.su == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == con.starth){
                    // This is the soonest event!
                    if (currentMinute < con.startm){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < con.starth){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (con.mo == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (con.tu == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (con.we == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (con.th == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (con.fr == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (con.sa == 1  && isSet == 0){
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
            if (con.mo == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == con.starth){
                    // This is the soonest event!
                    if (currentMinute < con.startm){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < con.starth){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (con.tu == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (con.we == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (con.th == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (con.fr == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (con.sa == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (con.su == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
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
            if (con.tu == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == con.starth){
                    // This is the soonest event!
                    if (currentMinute < con.startm){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < con.starth){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (con.we == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (con.th == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (con.fr == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (con.sa == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (con.su == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (con.mo == 1  && isSet == 0){
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
            if (con.we == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == con.starth){
                    // This is the soonest event!
                    if (currentMinute < con.startm){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < con.starth){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (con.th == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (con.fr == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (con.sa == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (con.su == 1  && isSet == 0){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (con.mo == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (con.tu == 1  && isSet == 0){
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
            if (con.th == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == con.starth){
                    // This is the soonest event!
                    if (currentMinute < con.startm){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < con.starth){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (con.fr == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (con.sa == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (con.su == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (con.mo == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (con.tu == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (con.we == 1  && isSet == 0){
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
            if (con.fr == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == con.starth){
                    // This is the soonest event!
                    if (currentMinute < con.startm){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < con.starth){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (con.sa == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (con.su == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (con.mo == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (con.tu == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (con.we == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (con.th == 1  && isSet == 0){
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
            if (con.sa == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                if (currentHour == con.starth){
                    // This is the soonest event!
                    if (currentMinute < con.startm){
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear;
                    }
                }
                else if (currentHour < con.starth){
                    isSet = 1;
                    nextDayOfYear = currentDayOfYear;
                }
            }
            if (con.su == 1 && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+1;

            }
            if (con.mo == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+2;

            }
            if (con.tu == 1&& isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+3;

            }
            if (con.we == 1  && isSet == 0){
                // The hour is before, see if the minutes are before
                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+4;

            }
            if (con.th == 1  && isSet == 0){
                // The hour is before, see if the minutes are before

                        isSet = 1;
                        nextDayOfYear = currentDayOfYear+5;

            }
            if (con.fr == 1  && isSet == 0){
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

        // Now that we know what the next day is, we need to check for rollovers for every day/month combination
        if ((nextDayOfYear > 366) && (nextDayOfYear != 999)){
            nextDayOfYear = nextDayOfYear - 366;
        }


        // Finalizing the values based on rollovers
        nextEvent = nextDayOfYear;

        // Open the Database for updating of the classType
        ClassDbAdapter classDbAdapter2 = new ClassDbAdapter(scanningService.this);
        classDbAdapter2.open();

        // Need to update the class here
        con.nextdate = nextEvent;

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

        // Updating the class
        classDbAdapter2.updateClass(con.id, newValues);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){


        if (ACTION_FORGROUND.equals(intent.getAction())){
            startForeground(1002, getCompatNotification());
        }

        if (BROADCAST_ACTION.equals(intent.getAction())){
            startForeground(1003, getCompatNotification());
        }

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        return START_NOT_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);

    }

        // This method updates the AppWidget and broadcasts class informaiton to the Home Activity
        private void DisplayLoggingInfo(String results) {

            int dayOfWeek = 0;

            // Storing the Latitude and Longitude
            intent2.putExtra("lat", latt);
            intent2.putExtra("lon", lonn);


            // Can never be too careful! (we may not have any classes in the list!)
            try {
                intent2.putExtra("results", results);
                intent2.putExtra("className", CT.classname);
                intent2.putExtra("startH", CT.starth);
                intent2.putExtra("startM", CT.startm);
                intent2.putExtra("attended", CT.attended);
                intent2.putExtra("missed", CT.missed);

                Calendar cc = Calendar.getInstance();

                cc.set(Calendar.DAY_OF_YEAR, CT.nextdate);
                dayOfWeek = cc.get(Calendar.DAY_OF_WEEK);


                if (dayOfWeek == Calendar.SUNDAY){
                    intent2.putExtra("Day", "Sunday");
                }
                if (dayOfWeek == Calendar.SATURDAY){
                    intent2.putExtra("Day", "Saturday");
                }
                if (dayOfWeek == Calendar.MONDAY){
                    intent2.putExtra("Day", "Monday");
                }
                if (dayOfWeek == Calendar.WEDNESDAY){
                    intent2.putExtra("Day", "Wednesday");
                }
                if (dayOfWeek == Calendar.THURSDAY){
                    intent2.putExtra("Day", "Thursday");
                }
                if (dayOfWeek == Calendar.FRIDAY){
                    intent2.putExtra("Day", "friday");
                }
                if (dayOfWeek == Calendar.SATURDAY){
                    intent2.putExtra("Day", "Saturday");
                }
            }
            catch (Exception e){

            }

            // Be careful with what you broadcast, you never know who is listening!
            sendBroadcast(intent2);

            // This is how we update an Appwidget in an activity of a thread
            Context context = this;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_app_widget_provider);
            ComponentName thisWidget = new ComponentName(context, customAppWidgetProvider.class);
            remoteViews.setTextViewText(R.id.textViewWidget, results);
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }

        @Override
    public void onDestroy(){
        super.onDestroy();
    }

    // Any background thread needs to have a notification in the notification tray!
    private Notification getCompatNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(getNotificationIcon())
                .setContentTitle("Gryphon Class Tracker")
                .setTicker("Here for You!")
                .setColor(0xb70223)
                .setWhen(System.currentTimeMillis());
        Intent startIntent = new Intent(getApplicationContext(),
                HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 10201, startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        return notification;

    }

    // Setting the Icon to all white
    private int getNotificationIcon(){
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.gryphons_logo_no_background : R.drawable.gryphons_logo_no_background;
    }
}
