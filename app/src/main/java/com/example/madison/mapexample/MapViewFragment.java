package com.example.madison.mapexample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
This is the embedded map fragment, located on the Home Activity
 */

public class MapViewFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflates and returns the layout
        View v = inflater.inflate(R.layout.fragment_map_view, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        // Move the Map to the University of Guelph as we wait to find your location
        LatLng UoG = new LatLng(43.53239641384581, -80.22454977035522);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(UoG));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

        return v;
    }

    // The home Activty periodically calls this method to update the fragment
    public void updateLocation(double la, double lo){

        Marker myMarker = null;

        // Only update the location if we are not at 0.0, 0.0
        if (la != 0 && lo != 0){

            // Prevents duplicate markers from being placed
            googleMap.clear();
            myMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(la, lo))
                    .title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            LatLng myLoc = new LatLng(la, lo);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

            HomeActivity activity = (HomeActivity) getActivity();
            TextView theText = (TextView) activity.findViewById(R.id.textView);
            theText.setText("Current Location!");
       }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}