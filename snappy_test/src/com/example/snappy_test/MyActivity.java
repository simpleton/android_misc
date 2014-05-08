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
import com.umeng.analytics.MobclickAgent;

import java.io.*;
import java.util.HashMap;

public class MyActivity extends Activity {
  private static final String TAG = "SnappyTest";
  private static final int TEST_TIME = 3000;

  class result {
    long filesystem;
    long snappyDB;
  }

  private result inAppResult = new result();
  private result inSDcardResult = new result();
  private Bitmap bitmap;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    bitmap = getBitmapFromAsset(this, "clock_2.png");
    MobclickAgent.setDebugMode(true);
  }

  @Override
  protected void onResume() {
    MobclickAgent.onResume(this);
    super.onResume();
    testSdcardStorage(bitmap);
    testInAppStorage(bitmap);
    uploadResult();
  }

  @Override
  protected void onPause() {
    MobclickAgent.onPause(this);
    super.onPause();
  }

  private void uploadResult() {
    //upload inApp result
    uploadInAppResult();
    //upload sdcard result
    uploadSdcardResult();
  }

  private void uploadSdcardResult() {
    Log.e(TAG, String.format("[result] inSDCard: file:%d\t levelDB:%d", inSDcardResult.filesystem, inSDcardResult.snappyDB));
    HashMap<String,String> mapSdcard = new HashMap<String,String>();
    mapSdcard.put("file", String.valueOf(inSDcardResult.filesystem));
    mapSdcard.put("LevelDB", String.valueOf(inSDcardResult.snappyDB));

    onEvent(this, "3", mapSdcard, inSDcardResult.filesystem);
    onEvent(this, "4", mapSdcard, inSDcardResult.snappyDB);
  }

  private void uploadInAppResult() {
    Log.e(TAG, String.format("[result] inApp: file:%d\t levelDB:%d", inAppResult.filesystem, inAppResult.snappyDB));
    HashMap<String,String> map = new HashMap<String,String>();
    map.put("file", String.valueOf(inAppResult.filesystem));
    map.put("LevelDB", String.valueOf(inAppResult.snappyDB));

    onEvent(this, "1", map, inAppResult.filesystem);
    onEvent(this, "2", map, inAppResult.snappyDB);
  }

  public static void onEvent(Context context, String id, HashMap<String,String> m, long value){
    m.put("__ct__", String.valueOf(value));
    MobclickAgent.onEvent(context, id, m);
  }
  private void testInAppStorage(Bitmap bitmap) {
    Log.d(TAG, "Test internal Storage");
    DB snappydb = null;
    try {
      snappydb = DBFactory.open(this, "image");
      Log.i(TAG, "--> start to save image to file");
      long startTime = System.currentTimeMillis();
      for (int i = 0; i < TEST_TIME; ++i) {
        saveBitmapToFile(this, bitmap, String.format("file%d.png", i));
      }
      long endTime = System.currentTimeMillis();
      inAppResult.filesystem = endTime - startTime;
      Log.i(TAG, "<-- end to save image to file");

      long startTime1 = System.currentTimeMillis();
      Log.i(TAG, "--> start to save image to snappy db");
      for (int i = 0; i < TEST_TIME; ++i) {
        snappydb.put(String.valueOf(i), getBitmapAsByteArray(bitmap));
      }
      long endTime1 = System.currentTimeMillis();
      inAppResult.snappyDB = endTime1 - startTime1;
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
      String dbFilePath = folder + File.separator + "db" + File.separator + "imageDB";
      new File(dbFilePath).mkdirs();
      Log.d(TAG, "db path is:" + dbFilePath);
      snappydb = new DBImpl(dbFilePath);

      Log.i(TAG, "--> start to save image to file");
      long startTime = System.currentTimeMillis();
      for (int i = 0; i < TEST_TIME; ++i) {
        saveBitmapToFile(bitmap, new File(folder + File.separator + String.format("file%d.png", i)));
      }
      long endTime = System.currentTimeMillis();
      inSDcardResult.filesystem = endTime - startTime;
      Log.i(TAG, "<-- end to save image to file");

      Log.i(TAG, "--> start to save image to snappy db");
      long startTime1 = System.currentTimeMillis();
      for (int i = 0; i < TEST_TIME; ++i) {
        snappydb.put(String.valueOf(i), getBitmapAsByteArray(bitmap));
      }
      long endTime1 = System.currentTimeMillis();
      inSDcardResult.snappyDB = endTime1 - startTime1;
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
    } catch (IOException e) {
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
