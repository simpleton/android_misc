package com.tencent.mm.pluginsdk.downloader.worker;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public class AarDownloadTask extends DownloadTask {
    private static final String TAG = "AarDownloadTask";

    public interface OnComplete {
        public void savedFinished(File plugin);
    }

    private final OnComplete onComplete;
    public AarDownloadTask(
            String pluginName,
            URL url,
            PluginRetrieveWorker pluginLoader,
            File outFile,
            OnComplete onComplete) {
        super(pluginName, url, pluginLoader, outFile);
        this.onComplete = onComplete;
    }

    @Override
    public String getThreadPrefix() {
        return "AarDownload_";
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "-->start download aarFileTask");
        InputStream inputStream = pluginRetrieveWorker.retrievePlugin(url);
        pluginRetrieveWorker.savePlugin(inputStream, outFile);

        onComplete.savedFinished(outFile);
        Log.i(TAG, "<--end download aarFileTask");
    }
}
