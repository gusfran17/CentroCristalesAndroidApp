package ar.com.lacomarcasistemas.centrocristalesmobile.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ar.com.lacomarcasistemas.centrocristalesmobile.R;

/**
 * Created by Test on 30/06/2016.
 */
public class Io {

    private static final String TAG = "Resize_Image";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showAlert(Context context,  String title, String message)
    {
        showAlert(context, title, message, null, null, null);
    }

    public static void showAlert(Context context,  String title, String message, DialogInterface.OnClickListener okListener)
    {
        showAlert(context, title, message, okListener, null, null);
    }

    public static void showAlert(Context context,  String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener)
    {
        showAlert(context, title, message, okListener, cancelListener, null);
    }

    public static void showAlert(Context context, String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener, DialogInterface.OnDismissListener dismissListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message);

        okListener =  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        };

        if(okListener != null){
            builder.setPositiveButton(R.string.ok, okListener);
        }

        if(cancelListener != null){
            builder.setNegativeButton(R.string.cancelar, cancelListener);
        }

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        if(dismissListener != null) {
            dialog.setOnDismissListener(dismissListener);
        }

        dialog.show();
    }


    public static Bitmap getBitmap(Context context, String path) {

        Uri uri = getImageContentUri(context, new File(path));

        return getBitmap(context, uri);
    }

    public static Bitmap getBitmap(Context context, Uri uri) {

        ContentResolver mContentResolver = context.getContentResolver();
        InputStream inputStream = null;
        try {
            final int IMAGE_MAX_SIZE = 300000; //  300k
            inputStream = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
            bitmapFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, bitmapFactoryOptions);
            inputStream.close();

            int scale = 1;
            while ((bitmapFactoryOptions.outWidth * bitmapFactoryOptions.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d(TAG, "scale = " + scale + ", orig-width: " + bitmapFactoryOptions.outWidth + ",orig-height: " + bitmapFactoryOptions.outHeight);

            Bitmap sourceBitmap = null;
            inputStream = mContentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                bitmapFactoryOptions = new BitmapFactory.Options();
                bitmapFactoryOptions.inSampleSize = scale;
                sourceBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapFactoryOptions);

                // resize to desired dimensions
                int height = sourceBitmap.getHeight();
                int width = sourceBitmap.getWidth();
                Log.d(TAG, "1th scale operation dimenions - width: " + width + ",height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, (int) x, (int) y, true);
                sourceBitmap.recycle();
                sourceBitmap = scaledBitmap;

                //System.gc();
            } else {
                sourceBitmap = BitmapFactory.decodeStream(inputStream);
            }
            inputStream.close();

            Log.d(TAG, "bitmap size - width: " + sourceBitmap.getWidth() + ", height: " + sourceBitmap.getHeight());
            return sourceBitmap;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(),e);
            return null;
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage(),e);
            return null;
        }
    }

    @Nullable
    public static String saveTempScaledBitmap(Context context, Bitmap scaledBitmad){

        String filename = context.getCacheDir() + "/cc_rb_tf.png";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            scaledBitmad.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    return filename;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static  Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}