package com.rmlabs.rishabmangla.superpants.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rmlabs.rishabmangla.superpants.R;
import com.rmlabs.rishabmangla.superpants.tools.StoreSession;

public class SplashScreen extends AppCompatActivity {

    boolean mIsFront = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }

    public void navigateToOther() {
        Intent intent;
        if (new StoreSession(getApplicationContext()).retrieveSessiontoken() == null) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), SuperActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsFront = false;
    }

    public boolean isFront() {
        return mIsFront;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (SplashScreen.this.isFront()) {
                        navigateToOther();
                    }
                }
            }
        }.start();
    }

}
