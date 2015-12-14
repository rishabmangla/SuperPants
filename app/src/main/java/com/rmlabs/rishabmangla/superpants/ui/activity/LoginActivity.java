package com.rmlabs.rishabmangla.superpants.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.rmlabs.rishabmangla.superpants.R;
import com.rmlabs.rishabmangla.superpants.tools.StoreSession;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private static final String KEY_PUBLIC_PROFILE = "public_profile";
    private static final String KEY_USER_FRIENDS = "user_friends";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FB = "fb";
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.authentication_page);
        setSupportActionBar(mToolbar);

        initializeSocialSdks();
    }

    public void initializeSocialSdks() {
        FacebookSdk.sdkInitialize(this);
    }

    @OnClick({R.id.facebook_login})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook_login:
                facebookLogin();
                break;
        }
    }

    public void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(KEY_PUBLIC_PROFILE, KEY_USER_FRIENDS, KEY_EMAIL));
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String facebookToken = loginResult.getAccessToken().getToken();
                new StoreSession(getApplicationContext(), facebookToken);
                startSuperActivity();
            }

            @Override
            public void onCancel() {
                showSnackBar(getResources().getString(R.string.activity_authentication_page_toast_facebook_login_cancelled));
            }

            @Override
            public void onError(FacebookException exception) {
                showSnackBar(getResources().getString(R.string.activity_authentication_page_toast_facebook_login_error));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        if(mCallbackManager != null)
            mCallbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    public void startSuperActivity() {
        Intent intent = new Intent(this, SuperActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void showSnackBar(String message) {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_background));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

}
