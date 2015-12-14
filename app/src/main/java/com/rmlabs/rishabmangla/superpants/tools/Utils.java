package com.rmlabs.rishabmangla.superpants.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rmlabs.rishabmangla.superpants.R;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by rishabmangla on 13/12/15.
 */
public class Utils {

    // directory name to store captured images
    private static final String IMAGE_DIRECTORY_NAME = "Super Pairs";

    public static void showErrorMsg(Context context, String message) {
        try {
            showSnackBar(context, message);
        } catch (Exception e) {
            showToastBar(context, message);
        }
    }

    public static void showSnackBar(Context context, String message) {
        View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showToastBar(Context context, String message) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }

//    public static String getUniqueImageFilename() {
////        File.createTempFile();
//        return "img_"+ System.currentTimeMillis() + ".jpg";
//    }

    /**
     * Creating file uri to store image
     */
    public static Uri getOutputMediaFileUri() {
        try {
            return Uri.fromFile(getOutputMediaFile());
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * returning image
     */
    public static File getOutputMediaFile() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            Log.d("getOutputMediaFile()", "No external storage state");
//            return null;
//        }

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        Calendar c = Calendar.getInstance();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date(c.getTimeInMillis()));
        File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
}
