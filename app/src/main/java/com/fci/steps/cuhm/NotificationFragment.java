package com.fci.steps.cuhm;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    String first_name;
    String last_name;
    String problem;
    String description_problem;

    private RecyclerView mList;

    private List<Notifications> mNotificationList;
    private RecyclerViewAdapter mRecyclerAdapter;

    //the URL having the json data
    private static final String JSON_URL = "http://cuhm.000webhostapp.com/FCMExample/getnotif.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Mono = inflater.inflate(R.layout.fragment_notification, container, false);


        mList = Mono.findViewById(R.id.main_notification_list);
        mNotificationList = new ArrayList<>();
        mRecyclerAdapter = new RecyclerViewAdapter(mNotificationList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mList.setLayoutManager(mLayoutManager);
        // adding inbuilt divider line
        mList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mRecyclerAdapter);

        //this method will fetch and parse the data
        loadNotificationList();
        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), NotificationDetailsActivity.class);
                        intent.putExtra("first_name", first_name);
                        intent.putExtra("last_name", last_name);
                        intent.putExtra("problem", problem);
                        intent.putExtra("description_problem", description_problem);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return Mono;
    }

    private void loadNotificationList() {

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                                        first_name = notificationObject.getString("first_name"),
                                        last_name = notificationObject.getString("last_name"),
                                        problem = notificationObject.getString("problem"),
                                        description_problem = notificationObject.getString("description_problem"));

                                //adding the notification to notificationList
                                mNotificationList.add(my_notification);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mRecyclerAdapter.notifyDataSetChanged();
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



