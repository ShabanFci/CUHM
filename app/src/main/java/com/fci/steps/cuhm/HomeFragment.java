package com.fci.steps.cuhm;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    //URL to RegisterDevice.php
//    private static final String URL_REGISTER_DEVICE = "http://192.168.43.92/FCMExample/RegisterDevice.php";

    private static final String TAG = "MONO";
    Button emergencyHelp, requestHelp, mTokenRegister, buttonSendPush;
    //FireBase
    DatabaseReference mDatabase;
    //Firebase Auth and DataBase
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRefDatabase;
    //Firebase Storage
    private StorageReference mStorageRef;
    //ProgressDialog
    ProgressDialog mRegProgress;

    public HomeFragment() {
        // Required empty public constructor
    }

    String email,first_name,last_name ;
    String  problem ;
    String description_problem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buttonSendPush = view.findViewById(R.id.buttonSendNotification);
        buttonSendPush.setOnClickListener(this);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();//Auth
        String currentUser = mCurrentUser.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mRefDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.child("email").getValue().toString();
                first_name = dataSnapshot.child("fname").getValue().toString();
                last_name = dataSnapshot.child("lname").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        emergencyHelp = (Button) view.findViewById(R.id.EmerHelp);
        requestHelp = (Button) view.findViewById(R.id.ReqHelp);
        emergencyHelp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_emergency, null);
                mBuilder.setTitle("Emergency List");
                Button ambulance_btn = mView.findViewById(R.id.btn_ambulance);
                Button fire_btn = mView.findViewById(R.id.btn_fire);
                Button police_btn = mView.findViewById(R.id.btn_police);
                Button support_btn = mView.findViewById(R.id.app_support);

                ambulance_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber("123");
                    }
                });
                fire_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber("180");
                    }
                });
                police_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber("122");
                    }
                });
                support_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callNumber("01145708978");
                    }
                });
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(),

                                emergencyHelp.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
        requestHelp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                sendTokenToServer();

                loadRegisteredDevices();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_reqhelp, null);
                mBuilder.setTitle("Request Help");

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uId = current_user.getUid();
                //Firebase Database
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Help_Request").child(uId).push();

                final Spinner spinCategories = (Spinner) mView.findViewById(R.id.spinCategory);
                ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.reqHelp_Act_problemCategories));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCategories.setAdapter(adapter);
                final EditText editProbDescription = mView.findViewById(R.id.problem_description);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                         problem = spinCategories.getSelectedItem().toString();
                         description_problem = editProbDescription.getText().toString();
                        sendMultiplePush();
                        if (!spinCategories.getSelectedItem().toString().equalsIgnoreCase("Choose a problem..."))
                            Toast.makeText(getActivity(),
                                    spinCategories.getSelectedItem().toString(),
                                    Toast.LENGTH_SHORT).show();
                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("problem", problem);
                        userMap.put("description_problem", description_problem);
                        mDatabase.setValue(userMap);
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
        return view;
    }

    private boolean callNumber(String Number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + Number));
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                startActivity(intent);
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }

    }

    //storing token to mysql server

    private void sendTokenToServer() {
        mRegProgress = new ProgressDialog(getActivity());
        mRegProgress.setMessage("Registering Device...");
        mRegProgress.show();
        mRegProgress.dismiss();

        //Firebase Connect


        final String token = SharedPrefManager.getInstance(getActivity()).getDeviceToken();
        if (token == null) {
            mRegProgress.dismiss();
            Toast.makeText(getActivity(), "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mRegProgress.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(),obj.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mRegProgress.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        MyVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
    }
    private void loadRegisteredDevices() {
        mRegProgress.setMessage("Fetching Devices...");
        mRegProgress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_FETCH_DEVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mRegProgress.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("devices");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
                                    Toast.makeText(getContext(),"Load finish ",Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

        };
        MyVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
    private void sendMultiplePush() {


        mRegProgress.setMessage("Sending Push");
        mRegProgress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mRegProgress.dismiss();

                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        savenotification();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("problem", problem);
                params.put("description_problem", description_problem);

                return params;
            }
        };

        MyVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    public void savenotification(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SAVE_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mRegProgress.dismiss();

                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("problem", problem);
                params.put("description_problem", description_problem);

                return params;
            }
        };

        MyVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    @Override
    public void onClick(View v) {

        if (v == buttonSendPush) {
            startActivity(new Intent(getActivity(), ActivitySendPushNotification.class));
        }
    }
}
