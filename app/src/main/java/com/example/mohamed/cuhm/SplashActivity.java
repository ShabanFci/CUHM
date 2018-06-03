package com.example.mohamed.cuhm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    private TextView mTextView1, mTextView2;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTextView1 = findViewById(R.id.welcome_text);
        mTextView2 = findViewById(R.id.welcome_caption);
        mImageView = findViewById(R.id.welcome_logo);
        Animation myAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.mytransition);
        mTextView1.startAnimation(myAnim);
        mTextView2.startAnimation(myAnim);
        mImageView.startAnimation(myAnim);
        final Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
