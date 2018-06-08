package com.fci.steps.cuhm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    //the URL having the json data
    private static final String JSON_URL = "http://cuhm.000webhostapp.com/FCMExample/getnotif.php";
//    private static final String JSON_URL = "https://simplifiedcoding.net/demos/view-flipper/heroes.php";

    //listView object
    ListView listView;

    //the notification list where we will store all the notification objects after parsing json
    List<Notifications> notification_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Mono = inflater.inflate(R.layout.fragment_notification, container, false);


        //initializing listView and Notification list
        listView = (ListView) Mono.findViewById(R.id.listView);
        notification_list = new ArrayList<>();

        //this method will fetch and parse the data
        loadNotificationList();
        return Mono;
    }

    private void loadNotificationList() {
//        //getting the progressbar
//        final ProgressBar  progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
//
//        //making the progressbar visible
//        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
//                        progressBar.setVisibility(View.INVISIBLE);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named notificationArray inside the object
                            //so here we are getting that json array
                            JSONArray notificationArray = obj.getJSONArray("server_response");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < notificationArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject notificationObject = notificationArray.getJSONObject(i);

                                //creating a notification object and giving them the values from json object
                                Notifications my_notification = new Notifications(
                                        notificationObject.getString("first_name"),
                                        notificationObject.getString("last_name"),
                                        notificationObject.getString("problem"),
                                        notificationObject.getString("description_problem"));

                                //adding the notification to notificationList
                                notification_list.add(my_notification);
                            }
                            //creating custom adapter object
                            ListViewAdapter adapter = new ListViewAdapter(notification_list, getActivity());
                            //adding the adapter to listView
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }
}



