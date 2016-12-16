package com.example.madison.mapexample;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;


/*
Sexy Splash screen
 */

public class Splash extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        // Force enable the GPS by the user
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            new android.support.v7.app.AlertDialog.Builder(Splash.this)
                    .setTitle("Enable GPS")
                    .setMessage("GPS needs to be enabled!")
                    .setIcon(R.drawable.app_logo)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            finish();
                        }
                    })
                    .show();
            }


        // Creating a new thread that loads the application
        final Handler handler = new Handler();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int x = 0;
                    ProgressBar myProgress = (ProgressBar) findViewById(R.id.ProgressBar);

                    // 3 second load time
                    for (x = 0; x < 100; x ++){
                        myProgress.incrementProgressBy(1);
                        sleep(30);

                        // Check GPS is enabled, halt progress if it isn't
                        if (x == 30){
                            while (( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ));
                        }

                        if (x == 65){
                            // Only ask for permissions if build level is 23 or higher!
                            // This may need to actually be moved to the loading screen! Background service may actually try to run without permissions and break
                            if (Build.VERSION.SDK_INT > 22) {
                                // Needs to get the permissions to use fine and course location since API level is 23
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET

                                    }, 10);
                                    finish();
                                    return;

                                }
                            }

                        }
                    }

                    // Start the main Activity
                    Intent mainIntent = new Intent(Splash.this,HomeActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Force the user to enable their gps!
            private void buildAlertMessageNoGps() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                Log.d("s", "in altert");
                builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                //gps = true;
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        };

        thread.start();
    }
}