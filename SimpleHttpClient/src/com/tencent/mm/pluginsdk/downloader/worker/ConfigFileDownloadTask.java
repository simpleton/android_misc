package com.tencent.mm.pluginsdk.downloader.worker;

import android.util.Log;
import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;

import java.io.File;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public class ConfigFileDownloadTask extends DownloadTask {
    private static final String TAG = "ConfigFileDownloadTask";
    public interface OnComplete {
        public void savedFinished(PluginDescription pluginDescription);
    }

    private final OnComplete onComplete;
    public ConfigFileDownloadTask(
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
        return "ConfigFileDownload_";
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "-->Start downloading config file");
        PluginDescription pluginDescription = pluginRetrieveWorker.retrieveConfig(url);
        pluginRetrieveWorker.saveConfig(pluginDescription, outFile);

        onComplete.savedFinished(pluginDescription);
        Log.i(TAG, "<--End downloading config file");
    }
}
