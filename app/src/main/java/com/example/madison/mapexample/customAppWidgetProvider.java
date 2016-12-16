package com.example.madison.mapexample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;


/**
 * Creates an App Widget that can be used outside of the internal Applicaiton
 */

public class customAppWidgetProvider extends AppWidgetProvider {

    // Default String for the widget
    public String widgetString = "No classes are scheduled!";
    public static final String ACTION_TEXT_CHANGED = "com.example.madison.mapexample.TEXT_CHANGED";



    // Class to update the widget with base values. Called periodically by the application.
    // Note, the real assignment of the app widget's strings occur within the scanningService

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Log.d("updat:", "widget updated");

        CharSequence widgetText = "Gryphon Class Tracker";
        CharSequence nextText = widgetString;

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.custom_app_widget_provider);

        // Setting the views to be the base strings
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.textViewWidget, nextText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_TEXT_CHANGED)) {

            // Unused code but kept for future functionialty.
            // App Widgets naturally have a broadcast reciever
            // Any broadcasts can be recieved here
            widgetString = intent.getStringExtra("NewString");
            //Log.d("widgetString", widgetString);
        }
    }
}

