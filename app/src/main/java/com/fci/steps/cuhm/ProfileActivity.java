package com.fci.steps.cuhm;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextView mDisplayProfileName, mDisplayProfileStatus, mDisplayProfileFriend;
    private ImageView mDisplayProfileImage;
    private Button mProfileSendReqBtn;

    private DatabaseReference mUsersDataBase;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = (Toolbar) findViewById(R.id.connection_profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Connection Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String userId = getIntent().getStringExtra("userId");

        mUsersDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mDisplayProfileImage = findViewById(R.id.profile_image);
        mDisplayProfileName = findViewById(R.id.display_profile_name);
        mDisplayProfileStatus = findViewById(R.id.display_profile_status);

        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("fname").getValue().toString();
                String displayJob = dataSnapshot.child("job").getValue().toString();
                String displayImage = dataSnapshot.child("image").getValue().toString();


                mDisplayProfileName.setText(displayName);
                mDisplayProfileStatus.setText(displayJob);

                Picasso.with(ProfileActivity.this).load(displayImage).placeholder(R.drawable.profileboy).into(mDisplayProfileImage);
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
