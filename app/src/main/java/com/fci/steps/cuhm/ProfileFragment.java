package com.fci.steps.cuhm;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private DatabaseReference mRefDatabase;
    private FirebaseUser mCurrentUser;
    private ImageView BtnEditProfile;
    public TextView userName
            ,userLName
            ,userEmail
            ,userPhone
            ,userAddress
            ,userJob;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_profile, container, false);

       BtnEditProfile = view.findViewById(R.id.editProf);

//        user_image = view.findViewById(R.id.UserImage);
        userName = view.findViewById(R.id.fname);
//        userFName = view.findViewById(R.id.editUserFName);
        userLName = view.findViewById(R.id.user_lName);
        userAddress = view.findViewById(R.id.user_address);
        userEmail = view.findViewById(R.id.user_email);
        userPhone = view.findViewById(R.id.user_phone);
        userJob = view.findViewById(R.id.user_job);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();//Auth
        String userId = mCurrentUser.getUid();
//        mStorageRef = FirebaseStorage.getInstance().getReference();//Storage
        mRefDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);//DataBase
        mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("fname").getValue().toString();
                String lname = dataSnapshot.child("lname").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String job = dataSnapshot.child("job").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                userName.setText(fname);
                userLName.setText(lname);
                userAddress.setText(address);
                userEmail.setText(email);
                userJob.setText(job);
                userPhone.setText(phone);

//                Picasso.with(SettingActivity.this).load(image).placeholder(R.drawable.ic_launcher_foreground).into(mProfileImage);
//                if (!image.equals("default")) {
////                    Picasso.with(SettingActivity.this).load(image).into(mProfileImage);
//                    Picasso.with(SettingActivity.this).load(image).placeholder(R.mipmap.ic_launcher_round).into(mProfileImage);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        BtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),EditprofileActivity.class);
//                intent.putExtra("UserFName",userFName.getText().toString());
                intent.putExtra("UserName",userName.getText().toString());
                intent.putExtra("UserLName",userLName.getText().toString());
                intent.putExtra("Email",userEmail.getText().toString());
                intent.putExtra("Address",userAddress.getText().toString());
                intent.putExtra("Job",userJob.getText().toString());
                intent.putExtra("Phone",userPhone.getText().toString());
                startActivity(intent);
            }
        });

        final Button btnNear_by_helper =(Button)view.findViewById(R.id.btn_nearByHelper);
        btnNear_by_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext()  , NearByHelperMapActivity.class);
                startActivity(intent);
            }
        });

        final Button btnGetCurrentLoc =(Button)view.findViewById(R.id.btn_getCurrentLoc);
        btnGetCurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext()  , RequesterRouteActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
