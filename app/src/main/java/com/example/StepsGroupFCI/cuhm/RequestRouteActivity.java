package com.example.StepsGroupFCI.cuhm;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RequestRouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    final String uId = current_user.getUid();

    ///////////////////
    ArrayList markerPoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_route);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;

        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(3.0f);
        subscribeToUpdates();
        // Initializing

        if (mMap != null) {

            // Enable MyLocation Button in the Map
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }

    }
// Setting onclick event listener for the map
//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng point) {
//
//                    // Already two locations
////                    if (markerPoints.size() > 1) {
////                        markerPoints.clear();
////                        mMap.clear();
////                    }
//
//// Adding new item to the ArrayList
//                    markerPoints.add(point);
//
//// Creating MarkerOptions
//                    MarkerOptions options = new MarkerOptions();
//
//// Setting the position of the marker
//                    options.position(point);
//
///**
// * For the start location, the color of marker is GREEN and
// * for the end location, the color of marker is RED.
// */
//                    if (markerPoints.size() == 1) {
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    } else if (markerPoints.size() == 2) {
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                    }
//
//// Add new marker to the Google Map Android API V2
//                    mMap.addMarker(options);
//
//// Checks, whether start and end locations are captured
//                    if (markerPoints.size() >= 2) {
//                        LatLng origin = (LatLng) markerPoints.get(0);
//                        LatLng dest = (LatLng) markerPoints.get(1);
//
//// Getting URL to the Google Directions API
//                        String url = getDirectionsUrl(origin, dest);
//
//                        DownloadTask downloadTask = new DownloadTask();
//
//// Start downloading json data from Google Directions API
//                        downloadTask.execute(url);
//                    }
//
//                }
//            });
//        }
    private void subscribeToUpdates() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uId = current_user.getUid();
        DatabaseReference Curr_ref = FirebaseDatabase.getInstance().getReference().child("location");
        Curr_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double lat1=Double.parseDouble(  dataSnapshot.child(uId).child("latitude").getValue().toString());
                double lng1= Double.parseDouble(  dataSnapshot.child(uId).child("longitude").getValue().toString());
                LatLng curreLatLng = new LatLng(lat1, lng1);
                MarkerOptions options1 = new MarkerOptions();
// Setting the position of the marker
                options1.position(curreLatLng);
                options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(options1);

                double lat2=Double.parseDouble(  dataSnapshot.child("xo2FQ0VvrwbylboMGZ9sfUjdzv63").child("latitude").getValue().toString());
                double lng2= Double.parseDouble( dataSnapshot.child("xo2FQ0VvrwbylboMGZ9sfUjdzv63").child("longitude").getValue().toString());
                LatLng requesterLatLng =new LatLng(lat2,lng2);
                MarkerOptions options2 = new MarkerOptions();
                // Setting the position of the marker
                options2.position(requesterLatLng);
                options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(options2);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(curreLatLng, requesterLatLng);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(curreLatLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        DatabaseReference request_ref = FirebaseDatabase.getInstance().getReference().child("location");
//        request_ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }
    //    private void setMarker(DataSnapshot dataSnapshot) {
//        // When a location update is received, put or update
//        // its value in mMarkers, which contains all the markers
//        // for locations received, so that we can build the
//        // boundaries required to show them all on the map at once
//        // get user current location
//        String key = dataSnapshot.getKey();
//        //  String username = dataSnapshot.child("fname").getValue().toString();
//        // FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
//        // String uId = current_user.getDisplayName();
//        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
//        double lat1 = Double.parseDouble(value.get("latitude").toString());
//        double lng1 = Double.parseDouble(value.get("longitude").toString());
//        LatLng dest = new LatLng(lat1, lng1);
//////////////////////////////////
//
////        double lat2 = Current_Location.getLatitude();
////        double lng2 = Current_Location.getLongitude();
//   //     LatLng origin = new LatLng(lat2, lng2);
//
//        MarkerOptions options = new MarkerOptions();
//
//// Setting the position of the marker
//                    options.position(origin);
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                    mMap.addMarker(options);
//
//
//       // LatLng origin = (LatLng) markerPoints.get(0);
//       // LatLng dest = (LatLng) markerPoints.get(1);
//
//        // Getting URL to the Google Directions API
//        String url = getDirectionsUrl(origin, dest);
//
//        DownloadTask downloadTask = new DownloadTask();
//
//        // Start downloading json data from Google Directions API
//        downloadTask.execute(url);
//
//        //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//
//        if (!mMarkers.containsKey(key)) {
//            // DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
//            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(dest)));
//
//        } else {
//            mMarkers.get(key).setPosition(dest);
//        }
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        for (Marker marker : mMarkers.values()) {
//
//            builder.include(marker.getPosition());
//            // points.add(marker.getPosition());
//        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }
    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while ", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            RequestRouteActivity.ParserTask parserTask = new RequestRouteActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

}
