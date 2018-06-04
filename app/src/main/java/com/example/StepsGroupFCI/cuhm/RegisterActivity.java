package com.example.StepsGroupFCI.cuhm;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    EditText editTxtFName,
            editTxtLName,
            editTxtEmail,
            editTxtJob,
            editTxtPhone,
            editTxtAddress,
            editTxtPass,
            editTxtConfPass;

    Button mBtnRegister;
    //FireBase
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    //ProgressDialog
    ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Instance
        mAuth = FirebaseAuth.getInstance();

        editTxtFName = findViewById(R.id.reg_fname);
        editTxtLName = findViewById(R.id.reg_lname);
        editTxtEmail = findViewById(R.id.reg_email);
        editTxtJob = findViewById(R.id.reg_job);
        editTxtPhone = findViewById(R.id.reg_phone);
        editTxtAddress = findViewById(R.id.reg_address);
        editTxtPass = findViewById(R.id.reg_password);
        editTxtConfPass = findViewById(R.id.reg_Confpassword);
        mBtnRegister = findViewById(R.id.reg_button);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = editTxtFName.getText().toString();
                String lName = editTxtLName.getText().toString();
                String address = editTxtAddress.getText().toString();
                String job = editTxtJob.getText().toString();
                String phone = editTxtPhone.getText().toString();
                String pass = editTxtPass.getText().toString();
                String email = editTxtEmail.getText().toString();
                String confpass = editTxtConfPass.getText().toString();

                if (fName.isEmpty()) {
                    editTxtFName.setError("First Name Required");
                    editTxtFName.requestFocus();
                } else if (!isValidEmail(email)) {
                    editTxtEmail.setError("Invalid Email");
                    editTxtEmail.requestFocus();
                } else if (!isValidPassword(pass)) {
                    editTxtPass.setError("Invalid Password .. should be at least 6 character");
                    editTxtPass.requestFocus();
                } else if (!pass.equals(confpass)) {
                    editTxtConfPass.setError("Password does not match");
                    editTxtConfPass.requestFocus();
                } else if (!TextUtils.isEmpty(fName) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass) || !TextUtils.isEmpty(job)) {
                    mRegProgress = new ProgressDialog(RegisterActivity.this);
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("PLZ Wait While Create Your Account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    registerUser(fName, lName, email, pass, address, job, phone);

                } else {
                    Toast.makeText(RegisterActivity.this, "Sign Up Problem",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void LogIn_Link(View view) {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
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

    private void registerUser(final String fName, final String lName, final String email, final String password, final String address, final String job, final String phone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uId = current_user.getUid();
                            //Firebase Database
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("fname", fName);
                            userMap.put("lname", lName);
                            userMap.put("email", email);
                            userMap.put("password", password);
                            userMap.put("job", job);
                            userMap.put("phone", phone);
                            userMap.put("address", address);
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "ImageURL");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //progress dismis before go to the new activity
                                        mRegProgress.dismiss();

                                        Intent main_intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(main_intent);
                                        finish();
                                    }
                                }
                            });
                        } else {

                            mRegProgress.hide();
                            Toast.makeText(RegisterActivity.this, "Cannot Sign in. Plz Check Your Connection and Try Again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
