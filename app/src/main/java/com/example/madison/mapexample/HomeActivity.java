package com.example.madison.mapexample;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.attr.defaultValue;

/*
This activity is visited right after the splash screen.
 */

public class HomeActivity extends AppCompatActivity  {

    // Activity context for varying use across activity
    protected Context context;

    // Lattitude and longitude variables
    private double latt, lonn;

    private static final String TAG = "BroadcastTest";
    private Intent intent2;

    // These are the variables that become assigned in the broadcast reciever
    public String stringForWidget = "No classes are scheduled!";
    public String cName;
    public String day;
    public int startH = 0;
    public int startM = 0;
    public int attended = 0;
    public int missed = 0;
    public float ratio = 0;

    // Executes when a broadcast is recieved
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    // Update the user interface with the next class values
    private void updateUI(Intent intent) {

        // Lattitude and Longitude are passed in by the scanningService
        latt = intent.getDoubleExtra("lat",defaultValue);
        lonn = intent.getDoubleExtra("lon",defaultValue);

        // Broadcast recievers pass information similar to how activites can pass information
        stringForWidget = intent.getStringExtra("results");
        day = intent.getStringExtra("Day");
        cName = intent.getStringExtra("className");
        startH = intent.getIntExtra("startH", defaultValue);
        startM = intent.getIntExtra("startM", defaultValue);
        attended = intent.getIntExtra("attended", defaultValue);
        missed = intent.getIntExtra("missed", defaultValue);


        // Finding the various views on the activity to update the informaiton
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        TextView dayText = (TextView) findViewById(R.id.dayText);
        TextView classText = (TextView) findViewById(R.id.CName);
        TextView timeText = (TextView) findViewById(R.id.timeText);


        // If there are no values being bundled by the scanningService, variables default to 16843245
        // Resetting variables to 0
        if (attended == 16843245){
            attended = 0;
            missed = 0;
            startH = 0;
            startM = 0;
            // Hiding the RatingBar (Stars)
            ratingBar.setVisibility(View.INVISIBLE);
        }
        else {
            // We have recieved correct values, show the ratingBar (Stars)
            ratingBar.setVisibility(View.VISIBLE);
        }


        // Determining the Ratio of classes attended (avoiding divide my 0 scenario)
        if (attended + missed > 0) {
            ratio = (float) attended / ((float) missed + (float) attended);
            ratio = ratio * 5;
        }
        else {
            ratio = 0;
        }

        // Some uesful log commands for debugging
        /*
        Log.d("startH", "" + startH);
        Log.d("startM", "" + startM);
        Log.d("attended", ""+attended);
        Log.d("missed", "" + missed);
        Log.d("ratio", "" + ratio);
        Log.d("lat", "" + latt);
        Log.d("lat", "" + lonn);
        Log.d("results", "" + stringForWidget);
        */


        // Here is an example of how to have varying functionailty depending on the Build Version
        // Older models will have different SDK versions which will break certain code
        if (Build.VERSION.SDK_INT > 20) {
            Drawable stars = ratingBar.getProgressDrawable();
            stars.setTint(Color.RED);
            ratingBar.setRating((int) ratio);
        }

        else  {
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(0).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            ratingBar.setRating((int) ratio);
        }


        // Setting the classname and the day of the week text on the Activty UI
        dayText.setText(day);
        classText.setText(cName);


        // Correctly displaying the time.
        if (startH == 0) {
            timeText.setText("00:00");
        }
        else {
            // Forcing a double 00 if the minutes is less than 10 minutes (eg. 12:08)
             if (startM < 10){
                 timeText.setText(startH+":0"+ startM);
             }
             else {
                   timeText.setText(startH+":"+ startM);
             }
        }

        // Learning how to send a broadcast to the appWidgetProvider
        // Although I am broadcasting a String, I never use it in the App Widget
        Intent intentWidget = new Intent(customAppWidgetProvider.ACTION_TEXT_CHANGED);
        intentWidget.putExtra("NewString", stringForWidget);
        getApplicationContext().sendBroadcast(intentWidget);


        // Very useful: Finding the ID of a fragment so we can access all of its methods!
        MapViewFragment rf =  (MapViewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment0);

        // If we haven't connected with all 3 GPS, we recieved lat:0.0, long:0.0
        if(rf!=null){
            rf.updateLocation(latt, lonn);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Creating background Service (Called a Foreground Service weirdly)
        intent2 = new Intent(this, scanningService.class);
        Intent fgrService = new Intent(scanningService.ACTION_FORGROUND);
        fgrService.setClass(HomeActivity.this, scanningService.class);
        startService(fgrService);

        // Assigning the ratingBar (Start) some initial values
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // Discussed above why we need two different possibilities
        if (Build.VERSION.SDK_INT > 20) {
            Drawable stars = ratingBar.getProgressDrawable();
            stars.setTint(Color.RED);
            ratingBar.setRating(0);
        }

        else  {
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(0).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            ratingBar.setRating(0);
        }



    }

    // OnClick listener for a button
    public void campusMap(View view) {
        Intent intent = new Intent (this, MapsActivity.class);

        // If you want to stop a backgound service, you can call this intent!
        //Intent stopIntent = new Intent(HomeActivity.this, scanningService.class);
        //stopService(stopIntent);

        // Lets the map Activity know to disable location selection
        intent.putExtra("WHICH_SENT", "0");
        startActivity(intent);
    }

    // OnClick listener to switch activity
    public void modifySchedule(View view) {
        Intent modify = new Intent(this, viewSchedule.class);
        startActivity(modify);
    }

    @Override
    public void onResume() {
        super.onResume();
        //startService(intent2);
        registerReceiver(broadcastReceiver, new IntentFilter(scanningService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        //stopService(intent2);
    }

    // OnClick listener for the Stats activity
    public void goStats(View view) {
        Intent statScreen = new Intent(this, Statistics.class);
        startActivity(statScreen);
    }
}
