package com.fci.steps.cuhm;


import android.annotation.SuppressLint;
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
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private DatabaseReference mRefDatabase;
    private FirebaseUser mCurrentUser;
    private ImageView BtnEditProfile, img;
    public TextView userName
            ,userLName
            ,userEmail
            ,userPhone
            ,userAddress
            ,userJob;
    CircleImageView mImageView;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        BtnEditProfile = view.findViewById(R.id.editProf);

        userName = view.findViewById(R.id.fname);
        userLName = view.findViewById(R.id.user_lName);
        userAddress = view.findViewById(R.id.user_address);
        userEmail = view.findViewById(R.id.user_email);
        userPhone = view.findViewById(R.id.user_phone);
        userJob = view.findViewById(R.id.user_job);
        mImageView = view.findViewById(R.id.UserImage);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();//Auth
        assert mCurrentUser != null;
        String userId = mCurrentUser.getUid();
//        mStorageRef = FirebaseStorage.getInstance().getReference();//Storage
        mRefDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);//DataBase
        mRefDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fname = Objects.requireNonNull(dataSnapshot.child("fname").getValue()).toString();
                String lname = Objects.requireNonNull(dataSnapshot.child("lname").getValue()).toString();
                String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                String address = Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString();
                String job = Objects.requireNonNull(dataSnapshot.child("job").getValue()).toString();
                String phone = Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString();
                String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
//                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                userName.setText(fname + " ");
                userLName.setText(lname);
                userAddress.setText(address);
                userEmail.setText(email);
                userJob.setText(job);
                userPhone.setText(phone);


//                Picasso.with(getActivity()).load(image).placeholder(R.drawable.ic_launcher_foreground).into(mImageView);
                if (!image.equals("default")) {
//                    Picasso.with(SettingActivity.this).load(image).into(mProfileImage);
                    Picasso.with(getActivity()).load(image).placeholder(R.drawable.man).into(mImageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        BtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
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

        final Button btnNear_by_helper = view.findViewById(R.id.btn_nearByHelper);
        btnNear_by_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NearbyHelperMapActivity.class);
                startActivity(intent);
            }
        });

        final Button btnGetCurrentLoc = view.findViewById(R.id.btn_getCurrentLoc);
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
