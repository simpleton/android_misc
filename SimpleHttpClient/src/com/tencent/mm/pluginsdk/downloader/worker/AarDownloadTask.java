package com.tencent.mm.pluginsdk.downloader.worker;

import android.util.Log;
import com.tencent.mm.pluginsdk.downloader.PluginLoader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public class AarDownloadTask extends DownloadTask {
    private static final String TAG = "AarDownloadTask";
    public AarDownloadTask(
            String pluginName,
            URL url,
            DownLoadClient downLoadClient,
            PluginLoader pluginLoader,
            File outFile) {
        super(pluginName, url, downLoadClient, pluginLoader, outFile);
    }

    @Override
    public String getThreadPrefix() {
        return "AarDownload_";
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "-->start download aarFileTask");
        InputStream inputStream = pluginLoader.retrievePlugin(url);
        pluginLoader.savePlugin(inputStream, outFile);
        Log.i(TAG, "<--end download aarFileTask");
    }
}
