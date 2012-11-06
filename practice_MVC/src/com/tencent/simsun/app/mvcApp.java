/**
 * 
 */
package com.tencent.simsun.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * @author simsun
 *
 */
public class mvcApp extends Application {
    private static final String TAG = mvcApp.class.getSimpleName();
    private static mvcApp instance;
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate was called");
        instance = this;
    }
    
    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
