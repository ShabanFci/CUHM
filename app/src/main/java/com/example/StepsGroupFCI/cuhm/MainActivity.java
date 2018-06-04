package com.example.StepsGroupFCI.cuhm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView userName, userEmail;

    //Firebase Auth and DataBase
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRefDatabase;
    //Firebase Storage
    private StorageReference mStorageRef;

    private FragmentManager fragmentManager;
    private android.support.v4.app.Fragment fragment = null;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new IndexFragment();
        fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.email);
        //Firebase Connect
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();//Auth
        String currentUser = mCurrentUser.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mRefDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("fname").getValue().toString();
                String lname = dataSnapshot.child("lname").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
//                String address = dataSnapshot.child("address").getValue().toString();
//                String job = dataSnapshot.child("job").getValue().toString();
//                String phone = dataSnapshot.child("phone").getValue().toString();
//                String image = dataSnapshot.child("image").getValue().toString();
//                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                userName.setText(fname + " " + lname);
                userEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                fragment = setFragment(id);
                displayFragment(fragment);
                return true;
            }
        });
        // start location service

        Intent tracker_intent = new Intent(this, TrackerActivity.class);
        startActivity(tracker_intent);


//        /*
//        * If the device is having android oreo we will create a notification channel
//        * */
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
//            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mNotificationManager.createNotificationChannel(mChannel);
//        }
//
//        /*
//        * Displaying a notification locally
//        */
//        MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");
    }

    private void displayFragment(Fragment fragment) {

        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container_wrapper, fragment);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
    }

    public Fragment setFragment(int id) {
        if (id == R.id.nav_home) {
            fragment = new IndexFragment();
        } else if (id == R.id.nav_profile || id == R.id.action_profile) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_contactList) {
            fragment = new ContactListFragment();
        } else if (id == R.id.nav_AppSetting || id == R.id.action_appsettings) {
            fragment = new AppSettingFragment();
        } else if (id == R.id.nav_notifSetting) {
            fragment = new NotificationSettingFragment();
        } else if (id == R.id.nav_Termspolicies) {
            fragment = new TermsAndPoliciesFragment();
        } else if (id == R.id.nav_contactUs) {
            fragment = new ContactUsFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutUsFragment();
        } else if (id == R.id.nav_LogOut) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        fragment = setFragment(id);
        displayFragment(fragment);
        if (item.getItemId() == R.id.btnsignout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
