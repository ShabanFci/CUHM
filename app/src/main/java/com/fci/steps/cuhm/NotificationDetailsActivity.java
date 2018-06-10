package com.fci.steps.cuhm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    String first_name;
    String last_name;
    String problem;
    String description_problem;

    ImageView mProblemType;
    TextView mUserName, mProblem, mDescriptionProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        mToolbar = (Toolbar) findViewById(R.id.notification_details_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notification Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        problem = getIntent().getStringExtra("problem");
        description_problem = getIntent().getStringExtra("description_problem");

        mUserName = findViewById(R.id.notification_details_user_name);
        mProblem = findViewById(R.id.notification_details_problem);
        mDescriptionProblem = findViewById(R.id.notification_details_description_problem);

        mUserName.setText(first_name + " " + last_name);
        mProblem.setText(problem);
        mDescriptionProblem.setText(description_problem);
    }
}
