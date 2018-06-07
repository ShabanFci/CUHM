package com.fci.steps.cuhm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EditProfileActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    EditText editFName, editLName, editEmail, editAddress, editJob, editPhone;
    Button mBtnSaveChange;
    //Firebase Auth and DataBase
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRefDatabase;
    //Firebase Storage
    private StorageReference mStorageRef;
    //progress
    private ProgressDialog mRegProgress;


//        if (!isValidEmail(email)) {
//            emailEditText.setError("Invalid Email");
//            emailEditText.requestFocus();
//        } else {
//            Toast.makeText(this, "Changes Saved", Toast.LENGTH_LONG).show();
//
//        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        //Firebase Connect
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();//Auth
        String currentUser = mCurrentUser.getUid();
//        mStorageRef = FirebaseStorage.getInstance().getReference();//Storage
        mRefDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);//DataBase


        Intent intent = getIntent();
        String name = intent.getStringExtra("UserName");
        String lname = intent.getStringExtra("UserLName");
        String email = intent.getStringExtra("Email");
        String address = intent.getStringExtra("Address");
        String job = intent.getStringExtra("Job");
        String phone = intent.getStringExtra("Phone");

        editFName = findViewById(R.id.editUserFName);
        editFName.setText(name);
        editLName = findViewById(R.id.editUserLName);
        editLName.setText(lname);
        editEmail = findViewById(R.id.editEmail);
        editEmail.setText(email);
        editAddress = findViewById(R.id.editUserAddress);
        editAddress.setText(address);
        editJob = findViewById(R.id.editJob);
        editJob.setText(job);
        editPhone = findViewById(R.id.editUserPhone);
        editPhone.setText(phone);
        mBtnSaveChange = findViewById(R.id.save_change);//save change btn in editprofile activity

        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress
                mRegProgress = new ProgressDialog(EditProfileActivity.this);
                mRegProgress.setTitle("Saving Status");
                mRegProgress.setMessage("PLZ Wait to Saving Change");
                mRegProgress.setCanceledOnTouchOutside(false);
                mRegProgress.show();
                String fname = editFName.getText().toString();
                String lname = editLName.getText().toString();
                String email = editEmail.getText().toString();
                String address = editAddress.getText().toString();
                String job = editJob.getText().toString();
                String phone = editPhone.getText().toString();

                mRefDatabase.child("fname").setValue(fname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //progress dismis if success
                            mRegProgress.dismiss();
                            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Getting Some Error in Saving Changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mRefDatabase.child("fname").setValue(fname);
                mRefDatabase.child("lname").setValue(lname);
                mRefDatabase.child("email").setValue(email);
                mRefDatabase.child("address").setValue(address);
                mRefDatabase.child("phone").setValue(phone);
                mRefDatabase.child("job").setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //progress dismis if success
                            mRegProgress.dismiss();
                            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Getting Some Error in Saving Changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
//        Button buttonLoadImage = findViewById(R.id.buttonLoadPicture);
//        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

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
