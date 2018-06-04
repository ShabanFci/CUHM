package com.example.StepsGroupFCI.cuhm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    public void checkMail(View view) {
        EditText forget_email = (EditText) findViewById(R.id.forget_email);
        final String email = forget_email.getText().toString();
        if (!isValidEmail(email)) {
            forget_email.setError("Invalid Email");
        } else {
            Toast.makeText(this, " Mail Sent To Your E-mail  ,Go To Mail and Reset your Password", Toast.LENGTH_LONG).show();
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
