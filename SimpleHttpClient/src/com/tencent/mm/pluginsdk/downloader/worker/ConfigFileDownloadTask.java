package com.tencent.mm.pluginsdk.downloader.worker;

import android.util.Log;
import com.tencent.mm.pluginsdk.downloader.PluginLoader;
import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;

import java.io.File;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public class ConfigFileDownloadTask extends DownloadTask {
    private static final String TAG = "ConfigFileDownloadTask";

    public ConfigFileDownloadTask(
            String pluginName,
            URL url,
            PluginLoader pluginLoader,
            File outFile) {
        super(pluginName, url, pluginLoader, outFile);
    }

    @Override
    public String getThreadPrefix() {
        return "ConfigFileDownload_";
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "-->Start downloading config file");
        PluginDescription pluginDescription = pluginLoader.retrieveConfig(url);
        pluginLoader.saveConfig(pluginDescription, outFile);
        Log.i(TAG, "<--End downloading config file");
    }
}
