package com.example.snappy_test;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.snappydb.internal.DBImpl;

import java.io.*;

public class MyActivity extends Activity {
    private static final String TAG = "SnappyTest";
    private static final int TEST_TIME = 3000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Bitmap bitmap = getBitmapFromAsset(this, "clock_2.png");
        //testInAppStorage(bitmap);
        testSdcardStorage(bitmap);
    }

    private void testInAppStorage(Bitmap bitmap) {
        Log.d(TAG, "Test internal Storage");
        DB snappydb = null;
        try {
            snappydb = DBFactory.open(this, "image");
            Log.i(TAG, "--> start to save image to file");
            for (int i = 0; i < TEST_TIME; ++i) {
                saveBitmapToFile(this, bitmap, String.format("file%d.png", i));
            }
            Log.i(TAG, "<-- end to save image to file");

            Log.i(TAG, "--> start to save image to snappy db");
            for (int i = 0; i < TEST_TIME; ++i) {
                snappydb.put(String.valueOf(i), getBitmapAsByteArray(bitmap));
            }
            Log.i(TAG, "<-- end to save image to snappy db");


        } catch (SnappydbException e) {
            e.printStackTrace();
        } finally {
            if (snappydb != null) {
                try {
                    snappydb.close();
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void testSdcardStorage(Bitmap bitmap) {
        Log.d(TAG, "Test Sdcard");
        DB snappydb = null;
        try {
            String folder = getExternalFilesDir(null).getAbsolutePath();
            String dbFilePath = folder + File.separator + "imageDB";

            Log.d(TAG, "db path is:" + dbFilePath);
            snappydb = new DBImpl(dbFilePath);

            Log.i(TAG, "--> start to save image to file");
            for (int i = 0; i < TEST_TIME; ++i) {
                saveBitmapToFile(this, bitmap, dbFilePath + File.separator + String.format("file%d.png", i));
            }
            Log.i(TAG, "<-- end to save image to file");

            Log.i(TAG, "--> start to save image to snappy db");
            for (int i = 0; i < TEST_TIME; ++i) {
                snappydb.put(String.valueOf(i), getBitmapAsByteArray(bitmap));
            }
            Log.i(TAG, "<-- end to save image to snappy db");


        } catch (SnappydbException e) {
            e.printStackTrace();
        } finally {
            if (snappydb != null) {
                try {
                    snappydb.close();
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static Bitmap getBitmapFromAsset(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();

        InputStream inputStream;
        Bitmap bitmap;
        try {
            inputStream = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private static void saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        saveBitmapToFile(bitmap, file);
    }

    private static void saveBitmapToFile(Bitmap bitmap, File file) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static {
        System.loadLibrary("snappydb-native");
    }
}