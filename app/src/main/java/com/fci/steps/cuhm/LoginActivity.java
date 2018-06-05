package com.fci.steps.cuhm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    EditText loginInputEmail,
            loginInputPassword;
    Button mBtnlogin,
            mBtnLinkSignup;
    FirebaseAuth mAuth;
    ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize The FirebaseAuth Instance.
        mAuth = FirebaseAuth.getInstance();

        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        mBtnlogin = (Button) findViewById(R.id.btn_login);
        mBtnLinkSignup = (Button) findViewById(R.id.btn_link_signup);

        mBtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginInputEmail.getText().toString();
                String pass = loginInputPassword.getText().toString();
                if (!isValidEmail(email)) {
                    loginInputEmail.setError("Invalid Email");
                    loginInputEmail.requestFocus();
                } else if (!isValidPassword(pass)) {
                    loginInputPassword.setError("Invalid Password");
                    loginInputPassword.requestFocus();
                } else if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
                    mRegProgress = new ProgressDialog(LoginActivity.this);
                    mRegProgress.setTitle("Logging In");
                    mRegProgress.setMessage("PLZ Wait While Login Your Account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    loginUser(email, pass);
                } else {
                    Toast.makeText(LoginActivity.this, "Cannot Sign in.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        this.setTitle("Log In");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendToStart();
        } else {
            Toast.makeText(LoginActivity.this, "Create New Account or Login with your Email and Password ", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(startIntent);
        finish();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    public void forgetPassword(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void loginUser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mRegProgress.dismiss();

                            Intent main_intent = new Intent(LoginActivity.this, MainActivity.class);
                            main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(main_intent);
                            finish();
                        } else {

                            mRegProgress.hide();

                            Toast.makeText(LoginActivity.this, "Cannot Sign in. Plz Check Your Connection and Try Again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
