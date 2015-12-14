package com.rmlabs.rishabmangla.superpants.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.rmlabs.rishabmangla.superpants.R;
import com.rmlabs.rishabmangla.superpants.tools.StoreSession;
import com.rmlabs.rishabmangla.superpants.tools.Utils;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WhatToday extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.random_top)
    ImageView mRandomTop;

    @Bind(R.id.random_pants)
    ImageView mRandomPants;

    Uri mCurrentTopUri;
    String mTopsUriList[];
    Uri mCurrentPantsUri;
    String mPantsUriList[];

    Random r = new Random();
    private int numberOfTops = 0;
    private int numberOfPants = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_today);

        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.what_today_title);
        setSupportActionBar(mToolbar);

        initialize();
        setTop();
        setPants();
    }

    private void initialize() {
        StoreSession ssn = new StoreSession(getApplicationContext());
        mTopsUriList = ssn.retrieveArray(StoreSession.TOPS_URI);
        if(mTopsUriList != null)
            numberOfTops = mTopsUriList.length;

        mPantsUriList = ssn.retrieveArray(StoreSession.PANTS_URI);
        if(mPantsUriList != null)
            numberOfPants = mPantsUriList.length;
    }

    private void setTop() {
        if(numberOfTops <= 0)
            return;
        int serialNumber = r.nextInt(numberOfTops);
        try {
            mCurrentTopUri = Uri.parse(mTopsUriList[serialNumber]);
//            Picasso.with(this).load(mCurrentTopUri).into(mRandomTop);
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });
            builder.build().load(mCurrentTopUri).resize(256, 256).centerCrop().into(mRandomTop);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setPants() {
        if(numberOfPants <= 0)
            return;
        int serialNumber = r.nextInt(numberOfPants);
        try {
            mCurrentPantsUri = Uri.parse(mPantsUriList[serialNumber]);
//            Picasso.with(this).load(mCurrentPantsUri).into(mRandomPants);
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(mCurrentPantsUri).resize(256,256).centerCrop().into(mRandomPants);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnClick({R.id.fab_dislike, R.id.fab_wishlist})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_dislike:
                if(numberOfTops <= 1 && numberOfPants <= 1){
                    Snackbar.make(view, "Add some more stuff to your wardrobe", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, R.string.dislike_toast, Snackbar.LENGTH_LONG).show();
                    setTop();
                    setPants();
                }
                break;
            case R.id.fab_wishlist:
                new StoreSession(getApplicationContext()).storeArray(StoreSession.BOOKMARKS_TOPS_URI, mCurrentTopUri.toString());
                new StoreSession(getApplicationContext()).storeArray(StoreSession.BOOKMARKS_PANTS_URI, mCurrentPantsUri.toString());
                Snackbar.make(view, R.string.bookmarked_toast, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

}
