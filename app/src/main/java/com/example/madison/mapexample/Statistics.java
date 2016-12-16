package com.example.madison.mapexample;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

import static lecho.lib.hellocharts.util.ChartUtils.COLOR_GREEN;
import static lecho.lib.hellocharts.util.ChartUtils.COLOR_ORANGE;
import static lecho.lib.hellocharts.util.ChartUtils.COLOR_RED;

/*
This activity is the stastic screen. It uses a special library for the pieChart
 */

public class Statistics extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ClassDbAdapter classDbAdapter;
    Cursor cursor1;
    SimpleCursorAdapter adapter;
    private String selected;
    private int first = 0;
    private int attended = 0;
    private int missed = 0;
    private int unsure = 0;
    private float ratio = 0;

    // My evil static variables that are frowned up
    // Needed since the PieChart Fragment is static
    private static int totalAttended;
    private static int totalMissed;
    private static int totalUnsure;

    // Some general Stat keep variables
    private String bestClass = "";
    private String worstClass = "";
    private float worstRatio = 101;
    private float bestRatio = -1;

    // The various views that will be updated
    RatingBar ratingBar;
    TextView attendedText;
    TextView missedText;
    TextView unsureText;
    TextView bestText;
    TextView worstText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Starts the PieChart fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        // Setting the views for external method usage
        ratingBar = (RatingBar) findViewById(R.id.ratingBar3);
        attendedText = (TextView) findViewById(R.id.attended);
        missedText = (TextView) findViewById(R.id.missed);
        unsureText = (TextView) findViewById(R.id.unsure);
        bestText = (TextView) findViewById(R.id.best);
        worstText = (TextView) findViewById(R.id.worst);

        // Creating an array for the Spinner (Dropdown menu)
        ArrayList<String> spinnerArray = new ArrayList<String>();

        // Need to access Database Informaiton
        ClassDbAdapter classDbAdapter = new ClassDbAdapter(Statistics.this);
        classDbAdapter.open();
        Cursor classCursor = classDbAdapter.getClassType();
        StringBuilder results = new StringBuilder();

        if(classCursor.moveToFirst()){
            do{
                classType con = classDbAdapter.getClassFromCursor(classCursor);
                spinnerArray.add(con.classname);

                // Need to get the first Class for viewing since the spinner defaults to the first item
                if (first == 0 ){
                    selected = con.classname;
                    Log.d("selected", selected);
                    first =1;
                }
                if (con.attended + con.missed > 0) {
                    ratio = (float) con.attended / ((float) con.missed + (float) con.attended);
                    ratio = ratio * 5;
                }
                else {
                    ratio = 0;
                }

                // New worst class ratio
                if (worstRatio > ratio){
                    worstClass = con.classname;
                    worstRatio = ratio;
                }
                // New best ratio
                if (bestRatio < ratio){
                    bestClass = con.classname;
                    bestRatio = ratio;
                }

                // Need to keep track of totals for every class
                totalAttended = totalAttended + con.attended;
                totalMissed = totalMissed + con.missed;
                totalUnsure = totalUnsure + con.unsure;
            }while(classCursor.moveToNext());
        }

        classCursor.close();
        classDbAdapter.close();

        // Setting up the spinner (Drop down menu)
        Spinner spinner = (Spinner) findViewById(R.id.class_spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

        // Specify the layout to use when the list of choices appears
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerArrayAdapter);

        // Create the listener
        spinner.setOnItemSelectedListener(this);


        // Reopen the Database and search for the firstClass that the spinner defaults to
        ClassDbAdapter classDbAdapter2 = new ClassDbAdapter(Statistics.this);
        classDbAdapter2.open();

        cursor1 = classDbAdapter2.getClassType();

        // Search the Database (Searches based on the classname)
        cursor1 = classDbAdapter2.searchClass(selected);

        // The cursor points to a table! We need to go to the first row in the table
        if(cursor1.moveToFirst()){
            classType con = classDbAdapter2.getClassFromCursor(cursor1);
            Log.d("attended", ""+ con.attended);
            Log.d("missed", ""+ con.missed);
            Log.d("unsure", ""+ con.unsure);
            attended = con.attended;
            missed = con.missed;
            unsure = con.unsure;
        }


        // Same issues with the Home Activity
        if (attended == 16843245){
            attended = 0;
            missed = 0;
            unsure = 0;
            ratingBar.setVisibility(View.INVISIBLE);
        }
        else {
            ratingBar.setVisibility(View.VISIBLE);
        }

        if (attended + missed > 0) {
            ratio = (float) attended / ((float) missed + (float) attended);
            ratio = ratio * 5;

        }
        else {
            ratio = 0;
        }

        // Same story as the Home Activity
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

        // Setting the views to the correct text
        attendedText.setText(""+ attended);
        missedText.setText(""+missed);
        unsureText.setText(""+unsure);
        bestText.setText(bestClass);
        worstText.setText(worstClass);

        // Clsing the Database
        cursor1.close();
        classDbAdapter2.close();

    }

    // Determines which spinner item was selected and updates the screen
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        ClassDbAdapter classDbAdapter2 = new ClassDbAdapter(Statistics.this);
        classDbAdapter2.open();

        cursor1 = classDbAdapter2.getClassType();

        // Same story as above
        cursor1 = classDbAdapter2.searchClass(parent.getItemAtPosition(pos).toString());

        if(cursor1.moveToFirst()){
            classType con = classDbAdapter2.getClassFromCursor(cursor1);
            attended = con.attended;
            missed = con.missed;
            unsure = con.unsure;
        }


        if (attended == 16843245){
            attended = 0;
            missed = 0;
            unsure = 0;
            ratingBar.setVisibility(View.INVISIBLE);
        }
        else {
            ratingBar.setVisibility(View.VISIBLE);
        }

        if (attended + missed > 0) {
            ratio = (float) attended / ((float) missed + (float) attended);
            ratio = ratio * 5;


        }
        else {
            ratio = 0;
        }

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

        attendedText.setText(""+ attended);
        missedText.setText(""+missed);
        unsureText.setText(""+unsure);


        cursor1.close();
        classDbAdapter2.close();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    // Since we are using Static variables, we need to hard reset them on closing the activity
    public void backHome(View view) {
        totalAttended = 0;
        totalUnsure = 0;
        totalMissed = 0;

        finish();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        totalAttended = 0;
        totalUnsure = 0;
        totalMissed = 0;
    }

    // This is the fragment for the PieChart
    public static class PlaceholderFragment extends Fragment {

        private PieChartView chart;
        private PieChartData data;

        private boolean hasLabels = false;
        private boolean hasLabelsOutside = false;
        private boolean hasCenterCircle = false;
        private boolean hasCenterText1 = false;
        private boolean hasCenterText2 = false;
        private boolean isExploded = false;
        private boolean hasLabelForSelected = false;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);

            chart = (PieChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            generateData();

            hasCenterCircle = !hasCenterCircle;
            if (!hasCenterCircle) {
                hasCenterText1 = false;
                hasCenterText2 = false;
            }

            generateData();
            toggleLabels();
            return rootView;
        }



        private void generateData() {

            List<SliceValue> values = new ArrayList<SliceValue>();

            SliceValue sliceValue = new SliceValue(totalAttended, COLOR_GREEN);
            values.add(sliceValue);
            sliceValue = new SliceValue(totalMissed, COLOR_RED);
            values.add(sliceValue);
            sliceValue = new SliceValue(totalUnsure, COLOR_ORANGE);
            values.add(sliceValue);

            data = new PieChartData(values);
            data.setHasLabels(hasLabels);
            data.setHasLabelsOnlyForSelected(hasLabelForSelected);
            data.setHasLabelsOutside(hasLabelsOutside);
            data.setHasCenterCircle(hasCenterCircle);

            if (isExploded) {
                data.setSlicesSpacing(24);
            }

            if (hasCenterText1) {
                data.setCenterText1("Hello!");

                // Get roboto-italic font.
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
                data.setCenterText1Typeface(tf);

                // Get font size from dimens.xml and convert it to sp(library uses sp values).
                data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
            }

            if (hasCenterText2) {
                data.setCenterText2("Charts (Roboto Italic)");

                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

                data.setCenterText2Typeface(tf);
                data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
            }

            chart.setPieChartData(data);
        }


        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);

                if (hasLabelsOutside) {
                    chart.setCircleFillRatio(0.7f);
                } else {
                    chart.setCircleFillRatio(1.0f);
                }
            }

            generateData();
        }


        /**
         *
         * method(don't confuse with View.animate()).
         */
        private void prepareDataAnimation() {
            for (SliceValue value : data.getValues()) {
                value.setTarget((float) Math.random() * 30 + 15);
            }
        }

        private class ValueTouchListener implements PieChartOnValueSelectListener {

            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                //Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }

}
