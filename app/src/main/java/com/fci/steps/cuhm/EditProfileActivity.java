package com.fci.steps.cuhm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    EditText editFName, editLName, editEmail, editAddress, editJob, editPhone;
    Button mBtnSaveChange, mBtnChangeImage;
    //Firebase Auth and DataBase
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRefDatabase;
    //Firebase Storage
    private StorageReference mImageStorage;
    //progress
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);
        //Firebase Connect
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();//Auth
        String currentUser = mCurrentUser.getUid();
        mImageStorage = FirebaseStorage.getInstance().getReference();//Storage
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

        mBtnChangeImage = findViewById(R.id.btn_load_image);
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
        mBtnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallary = new Intent();
                intentGallary.setType("image/*");
                intentGallary.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentGallary, "Select Image"), GALLERY_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            // start cropping activity for pre-acquired image saved on teh device
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(EditProfileActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mRegProgress = new ProgressDialog(EditProfileActivity.this);
                mRegProgress.setTitle("Uploading Image...");
                mRegProgress.setMessage("Plz Wait While Uploading Image");
                mRegProgress.setCanceledOnTouchOutside(false);
                mRegProgress.show();

                Uri resultUri = result.getUri();

                final File thum_filePath = new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                String currentUserId = mCurrentUser.getUid();
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thum_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filePath = mImageStorage.child("profile_images").child(currentUserId + ".jpg");
//                StorageReference filePath = mStorageRef.child("profile_images").child(random()+".jpg");
                final StorageReference thumb_filePath = mImageStorage.child("profile_images").child("thumbs").child(currentUserId + "jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                    HashMap updateHashMap = new HashMap();
                                    updateHashMap.put("image", download_url);
                                    updateHashMap.put("thumb_image", thumb_downloadUrl);

                                    if (thumb_task.isSuccessful()) {
                                        mRefDatabase.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mRegProgress.dismiss();
                                                    Toast.makeText(EditProfileActivity.this, "Upload Done", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        mRegProgress.hide();
                                        Toast.makeText(EditProfileActivity.this, "Not Uploaded Thumbnail.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            mRegProgress.hide();
                            Toast.makeText(EditProfileActivity.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
