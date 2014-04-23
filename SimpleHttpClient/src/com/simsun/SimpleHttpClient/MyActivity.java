package com.simsun.SimpleHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.simsun.SimpleHttpClient.httpclient.AsyncHttpClient;
import com.simsun.SimpleHttpClient.httpclient.AsyncHttpResponseHandler;
import com.squareup.okhttp.OkHttpClient;
import com.tencent.mm.pluginsdk.downloader.MMPluginLoaderFacotry;
import com.tencent.mm.pluginsdk.downloader.PluginManager;
import com.tencent.mm.pluginsdk.downloader.util.HexUtil;
import com.tencent.mm.pluginsdk.downloader.worker.AarDownloadTask;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyActivity extends Activity {
    private static final String TAG = "MyActivity";
    /**
     * Called when the activity is first created.
     */
    private static final String API_URL = "https://res.wx.qq.com/mpres/htmledition/images/icon/emotion/21.gif";
    final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btn = (Button) findViewById(R.id.btn_normal);
        final AsyncHttpClient client = new AsyncHttpClient();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.get(MyActivity.this, API_URL, null, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        Log.i(TAG, "onSuccess" + content);
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        super.onFailure(error, content);
                        Log.i(TAG, "onFailure" + content);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        Log.i(TAG, "onStart");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Log.i(TAG, "onFinish");
                    }
                });
            }
        });


        Button okBtn = (Button) findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OkRetireTask().execute(API_URL);
            }
        });
        final PluginManager pluginManager = new PluginManager(MMPluginLoaderFacotry.build());
        Button pluginBtn = (Button) findViewById(R.id.btn_plugin);
        pluginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pluginManager.startDownloadPluginIfNecessary(
                            MyActivity.this,
                            PluginManager.SUPPORTED_PLUGINS[0],
                            new AarDownloadTask.OnComplete() {
                                @Override
                                public void savedFinished(File plugin) {
                                    try {
                                        Log.i(TAG, plugin.getAbsolutePath());
                                        Log.i(TAG, HexUtil.readFileAsString(plugin));
                                    } catch (IOException e) {
                                        Log.e(TAG, e.toString());
                                    }
                                }
                            }
                    );
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });


        /*String className = (Thread.currentThread().getStackTrace()[3]).getClassName();
        try {
            Log.e(TAG, (Thread.currentThread().getStackTrace()[3]).getClass().newInstance().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ClassLoader callerClassLoader = (Thread.currentThread().getStackTrace()[3]).getClass().getClassLoader();
        Log.e(TAG, callerClassLoader.toString());
        final ClassLoader callerClassLoader1 = Thread.currentThread().getStackTrace()[2].getClass().getClassLoader();
        Log.e(TAG, callerClassLoader1.toString());
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            Log.w(TAG, stackTraceElement.toString());
        }
        Log.e(TAG, Thread.currentThread().getStackTrace()[3].toString());
        Log.e(TAG, Thread.currentThread().getStackTrace()[2].toString());*/
        //Log.e(TAG, VMStack.getCallingClassLoader());
    }

    public byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    class OkRetireTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                connection = okHttpClient.open(new URL(params[0]));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream in = null;
            try {
                // Read the response.
                in = connection.getInputStream();
                byte[] response = readFully(in);
                Log.d(TAG, new String(response, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
