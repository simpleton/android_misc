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
import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
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
            }
            finally {
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
