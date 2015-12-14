package com.rmlabs.rishabmangla.superpants.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.rmlabs.rishabmangla.superpants.R;
import com.rmlabs.rishabmangla.superpants.tools.StoreSession;
import com.rmlabs.rishabmangla.superpants.tools.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuperActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.what_today)
    ImageView mWhatToday;

    @Bind(R.id.add_top)
    ImageView mAddTop;

    @Bind(R.id.add_pants)
    ImageView mAddPants;

    @Bind(R.id.bookmarks)
    ImageView mBookmarks;

    private final int SELECT_PICTURE_REQUEST_CODE = 1;
    private Uri mOutputFileUri;
    private String uriKey = StoreSession.PANTS_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super);

        ButterKnife.bind(this);

    }

    @OnClick({R.id.what_today, R.id.add_top, R.id.add_pants, R.id.bookmarks})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.what_today:
                if(validateInventory()){
                    showWhatToday();
                }
                break;

            case R.id.add_top:
                uriKey = StoreSession.TOPS_URI;
                openImageIntent();
                break;

            case R.id.add_pants:
                uriKey = StoreSession.PANTS_URI;
                openImageIntent();
                break;

            case R.id.bookmarks:
                if(validateBookmarks())
                    showBookmarks();
                break;
        }
    }

    private boolean validateInventory() {
        if(!hasTop() && !hasPants()){
            Utils.showErrorMsg(this, getResources().getString(R.string.no_pair));
            return false;
        }else if(!hasPants()){
            Utils.showErrorMsg(this, getResources().getString(R.string.no_pants));
            return false;
        }else if(!hasTop()){
            Utils.showErrorMsg(this, getResources().getString(R.string.no_top));
            return false;
        }
        return true;
    }

    private boolean hasTop() {
        StoreSession ssn = new StoreSession(getApplicationContext());
        String[] topsUri = ssn.retrieveArray(StoreSession.TOPS_URI);
        if(topsUri != null) {
            int numberOfTops = topsUri.length;
            if(numberOfTops > 0)
                return true;
        }
        return false;
    }

    public boolean hasPants() {
        StoreSession ssn = new StoreSession(getApplicationContext());
        String[] pantsUri = ssn.retrieveArray(StoreSession.PANTS_URI);
        if(pantsUri != null) {
            int numberOfPants = pantsUri.length;
            if(numberOfPants > 0)
                return true;
        }
        return false;
    }

    public void showWhatToday() {
        Intent intent = new Intent(this, WhatToday.class);
        startActivity(intent);
    }

    private boolean validateBookmarks() {
        StoreSession ssn = new StoreSession(getApplicationContext());
        String[] topsUri = ssn.retrieveArray(StoreSession.BOOKMARKS_TOPS_URI);
        if(topsUri != null) {
            int numberOfTops = topsUri.length;
            if(numberOfTops > 0)
                return true;
        }
        Utils.showErrorMsg(this, getResources().getString(R.string.no_bookmarks));
        return false;
    }

    private void showBookmarks() {
        Intent intent = new Intent(this, Bookmarks.class);
        startActivity(intent);
    }

    private void openImageIntent() {
        mOutputFileUri = Utils.getOutputMediaFileUri();
        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19){
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            galleryIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = mOutputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                if(selectedImageUri != null)
                    new StoreSession(getApplicationContext()).storeArray(uriKey, selectedImageUri.toString());
            }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Utils.showErrorMsg(getApplicationContext(),
                    getString(R.string.image_capture_cancelled));
        } else {
            // failed to capture image
            Utils.showErrorMsg(getApplicationContext(),
                    getString(R.string.image_capture_failed));
        }
    }

}
