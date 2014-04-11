package com.tencent.mm.pluginsdk.downloader;

import android.util.Log;
import com.squareup.okhttp.OkHttpClient;
import com.tencent.mm.pluginsdk.downloader.DownLoadClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public class OkHttpDownloadClient implements DownLoadClient {
    private static final String TAG = "okHttpDownloadClient";
    final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public InputStream open(URL url) {
        HttpURLConnection httpURLConnection = okHttpClient.open(url);
        InputStream inputStream = null;
        try {
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return inputStream;
    }
}
