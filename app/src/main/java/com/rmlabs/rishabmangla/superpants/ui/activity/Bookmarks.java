package com.rmlabs.rishabmangla.superpants.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rmlabs.rishabmangla.superpants.R;
import com.rmlabs.rishabmangla.superpants.tools.Utils;
import com.rmlabs.rishabmangla.superpants.ui.adapter.BookmarksAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Bookmarks extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    protected ViewPager mViewPager;
    @Bind(R.id.bookmarks_indicator)
    protected CirclePageIndicator pagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.title_activity_bookmarks);
        setSupportActionBar(mToolbar);
        setPager();
    }

    public void setPager() {
        BookmarksAdapter bookmarksAdapter = new BookmarksAdapter(getApplicationContext());
        mViewPager.setAdapter(bookmarksAdapter);
        pagerIndicator.setViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bookmarks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            createImageFile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //create img file with top and pants combined, and then share
    public void createImageFile() {
        View view = mViewPager;
        view.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap bitmap = view.getDrawingCache();
        File picFile = Utils.getOutputMediaFile();
        try {
            picFile.createNewFile();
            FileOutputStream picOut = new FileOutputStream(picFile);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), (int) (bitmap.getHeight() / 1.2));
            boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, picOut);
            if (saved) {
                sharePair(picFile);
            } else {
                //Error
                Utils.showErrorMsg(this, getString(R.string.error_fail_image_save));
            }
            picOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showErrorMsg(this, getString(R.string.error_no_external_storage));
        }
        view.destroyDrawingCache();
    }

    private void sharePair(File picFile) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(picFile.getAbsolutePath()));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
