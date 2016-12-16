package com.example.madison.mapexample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import static android.R.attr.value;

/*
Full Screen Activity that utilizes a Map Fragment for Google Maps
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // A ton of variables required across the methods in this class
    private GoogleMap mMap;
    private Marker rich; // 1
    private Marker thrn; // 2
    private Marker thrn1006; // 3
    private Marker sci; // 4
    private Marker mcn; // 5
    private Marker macl; // 6
    private Marker crop; // 7
    private Marker ansci; // 8
    private Marker ovc; // 9
    private Marker jtp; // 10
    private Marker alex; // 11
    private Marker zav; // 12
    private Marker mackn; // 13
    private Marker macks; // 14
    private Marker grhm; // 15
    private Marker mac; // 16
    private Marker mins; // 17
    private Marker uc; // 18
    private Marker roz; // 19
    private Marker wmem; // 20
    private Marker mcl; // 21
    private Marker rey; // 22
    private Marker mass; // 23

    private int shown = 0;
    private String which = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Getting the extras passed to this activity. Finding out which activity called this activity to execute
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            which = extras.getString("WHICH_SENT");
        }
    }

    // Executes when a marker is selected on the map
    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(rey)) {
            Log.d("TAG", "Reynolds!");

            if (which.equals("1")) {

                if (shown == 22) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.REY_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.REY_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 22;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(mcl)) {
            Log.d("TAG", "MCL!");

            if (which.equals("1")) {

                if (shown == 21) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MCL_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MCL_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 21;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }

        if (marker.equals(wmem)) {
            Log.d("TAG", "WMEM!");

            if (which.equals("1")) {

                if (shown == 20) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.WMEM_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.WMEM_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 20;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(roz)) {
            Log.d("TAG", "ROZ!");

            if (which.equals("1")) {

                if (shown == 19) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.ROZ_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.ROZ_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 19;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(uc)) {
            Log.d("TAG", "UC!");

            if (which.equals("1")) {

                if (shown == 18) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.UC_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.UC_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 18;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(mins)) {
            Log.d("TAG", "MINS!");

            if (which.equals("1")) {

                if (shown == 17) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MINS_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MINS_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 17;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(mac)) {
            Log.d("TAG", "MAC!");

            if (which.equals("1")) {

                if (shown == 16) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MAC_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MAC_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 16;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(mass)) {
            Log.d("TAG", "MASS!");

            if (which.equals("1")) {

                if (shown == 15) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MASS_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MASS_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 15;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(grhm)) {
            Log.d("TAG", "GRHM!");

            if (which.equals("1")) {

                if (shown == 23) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.GRHM_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.GRHM_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 23;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(macks)) {
            Log.d("TAG", "MACKS!");

            if (which.equals("1")) {

                if (shown == 14) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MACK_S_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MACK_S_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 14;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(mackn)) {
            Log.d("TAG", "MACKN!");

            if (which.equals("1")) {

                if (shown == 13) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MACK_N_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MACK_N_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 13;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(zav)) {
            Log.d("TAG", "ZAV!");

            if (which.equals("1")) {

                if (shown == 12) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.ZAV_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.ZAV_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 12;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(alex)) {
            Log.d("TAG", "ALEX!");

            if (which.equals("1")) {

                if (shown == 11) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.ALEX_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.ALEX_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 11;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(jtp)) {
            Log.d("TAG", "JTP!");

            if (which.equals("1")) {

                if (shown == 10) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.JTP_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.JTP_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 10;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(ovc)) {
            Log.d("TAG", "OVC!");

            if (which.equals("1")) {

                if (shown == 9) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.OVC_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.OVC_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 9;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(ansci)) {
            Log.d("TAG", "ANSCI!");

            if (which.equals("1")) {

                if (shown == 8) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.ANSCI_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.ANSCI_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 8;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(crop)) {
            Log.d("TAG", "CROP!");

            if (which.equals("1")) {

                if (shown == 7) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.CROP_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.CROP_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 7;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(macl)) {
            Log.d("TAG", "MACL!");

            if (which.equals("1")) {

                if (shown == 6) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MACL_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MACL_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 6;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(mcn)) {
            Log.d("TAG", "MCN!");

            if (which.equals("1")) {

                if (shown == 5) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.MCN_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.MCN_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 5;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(sci)) {
            Log.d("TAG", "SCI!");

            if (which.equals("1")) {

                if (shown == 4) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.SCI_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.SCI_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 4;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(thrn1006)) {
            Log.d("TAG", "THRN1006!");

            if (which.equals("1")) {

                if (shown == 3) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.THRN_1006_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.THRN_1006_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 3;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(thrn)) {
            Log.d("TAG", "THRN!");

            if (which.equals("1")) {

                if (shown == 2) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.THRN_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.THRN_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 2;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (marker.equals(rich)) {
            Log.d("TAG", "RICH!");

            if (which.equals("1")) {

                if (shown == 1) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.RICH_Name))
                            .setMessage("Select this building?")
                            .setIcon(R.drawable.app_logo)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", getString(R.string.RICH_Name));

                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .show();

                }
                else {
                    shown = 1;
                    Toast.makeText(MapsActivity.this, "Click Again to Select",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        return false;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        // Adding General Buildings
        uc = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.UC_Lat)), Double.parseDouble(getString(R.string.UC_Lon))))
                .title((getString(R.string.UC_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        roz = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.ROZ_Lat)), Double.parseDouble(getString(R.string.ROZ_Lon))))
                .title((getString(R.string.ROZ_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        wmem = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.WMEM_Lat)), Double.parseDouble(getString(R.string.WMEM_Lon))))
                .title((getString(R.string.WMEM_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mcl = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MCL_Lat)), Double.parseDouble(getString(R.string.MCL_Lon))))
                .title((getString(R.string.MCL_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Adding Engineering Buildings
        rich = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.RICH_Lat)), Double.parseDouble(getString(R.string.RICH_Lon))))
                .title((getString(R.string.RICH_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        thrn1006 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.THRN_1006_Lat)), Double.parseDouble(getString(R.string.THRN_1006_Lon))))
                .title((getString(R.string.THRN_1006_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        thrn = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.THRN_Lat)), Double.parseDouble(getString(R.string.THRN_Lon))))
                .title((getString(R.string.THRN_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        // Adding Science Buildings
        sci = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.SCI_Lat)), Double.parseDouble(getString(R.string.SCI_Lon))))
                .title((getString(R.string.SCI_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mcn = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MCN_Lat)), Double.parseDouble(getString(R.string.MCN_Lon))))
                .title((getString(R.string.MCN_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        macl = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MACL_Lat)), Double.parseDouble(getString(R.string.MACL_Lon))))
                .title((getString(R.string.MACL_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Adding Aggie & Animal Buildings
        crop = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.CROP_Lat)), Double.parseDouble(getString(R.string.CROP_Lon))))
                .title((getString(R.string.CROP_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        ansci = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.ANSCI_Lat)), Double.parseDouble(getString(R.string.ANSCI_Lon))))
                .title((getString(R.string.ANSCI_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        ovc = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.OVC_Lat)), Double.parseDouble(getString(R.string.OVC_Lon))))
                .title((getString(R.string.OVC_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        // Adding Arts Buildings
        jtp = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.JTP_Lat)), Double.parseDouble(getString(R.string.JTP_Lon))))
                .title((getString(R.string.JTP_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        alex = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.ALEX_Lat)), Double.parseDouble(getString(R.string.ALEX_Lon))))
                .title((getString(R.string.ALEX_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        zav = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.ZAV_Lat)), Double.parseDouble(getString(R.string.ZAV_Lon))))
                .title((getString(R.string.ZAV_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        mackn = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MACK_N_Lat)), Double.parseDouble(getString(R.string.MACK_N_Lon))))
                .title((getString(R.string.MACK_N_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        macks = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MACK_S_Lat)), Double.parseDouble(getString(R.string.MACK_S_Lon))))
                .title((getString(R.string.MACK_S_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        grhm = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.GRHM_Lat)), Double.parseDouble(getString(R.string.GRHM_Lon))))
                .title((getString(R.string.GRHM_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        mass = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MASS_Lat)), Double.parseDouble(getString(R.string.MASS_Lon))))
                .title((getString(R.string.MASS_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        // Adding the Business Building
        mac = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MAC_Lat)), Double.parseDouble(getString(R.string.MAC_Lon))))
                .title((getString(R.string.MAC_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mins = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.MINS_Lat)), Double.parseDouble(getString(R.string.MINS_Lon))))
                .title((getString(R.string.MINS_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Adding the CompSci Building
        rey = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(getString(R.string.REY_Lat)), Double.parseDouble(getString(R.string.REY_Lon))))
                .title((getString(R.string.REY_Name))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


        // Moving the Camera to the University of Guelph
        LatLng UoG = new LatLng(43.53239641384581, -80.22454977035522);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UoG));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }
}
