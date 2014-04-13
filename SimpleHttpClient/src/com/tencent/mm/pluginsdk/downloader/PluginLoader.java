package com.tencent.mm.pluginsdk.downloader;

import android.util.Log;
import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;
import com.tencent.mm.pluginsdk.downloader.worker.AarDownloadTask;
import com.tencent.mm.pluginsdk.downloader.worker.ConfigFileDownloadTask;
import com.tencent.mm.pluginsdk.downloader.worker.DownloadTask;
import com.tencent.mm.pluginsdk.downloader.worker.PluginRetrieveWorker;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by simsun on 2014/4/12.
 */
public class PluginLoader {
    private static final String TAG = "PluginLoader";
    private final ThreadPoolExecutor executor;
    private final PluginRetrieveWorker pluginRetrieveWorker;
    public PluginLoader(ThreadPoolExecutor executor, PluginRetrieveWorker worker) {
        this.executor = executor;
        this.pluginRetrieveWorker = worker;
    }

    /**
     * load Config file asynchronously
     * @param pluginName target plugin name {@code PluginManager.SUPPORTED_PLUGINS}
     * @param url url to download
     * @param outFile the file will be written
     * @param callback asynchronized listener
     */
    public void loadConfigFile(
            String pluginName,
            URL url,
            File outFile,
            ConfigFileDownloadTask.OnComplete callback) {
        DownloadTask downloadTask = new ConfigFileDownloadTask(
                pluginName, url, pluginRetrieveWorker, outFile, callback);
        executor.submit(downloadTask);
    }

    /**
     * download target shoot aar package asynchronously.
     * @param pluginName target plugin name {@code PluginManager.SUPPORTED_PLUGINS}
     * @param url url to download
     * @param outFile the file will be written
     * @param callback asynchronized listener
     */
    public void loadPlugin(
            String pluginName,
            URL url,
            File outFile,
            AarDownloadTask.OnComplete callback) {
        DownloadTask downloadTask = new AarDownloadTask(
                pluginName, url, pluginRetrieveWorker, outFile, callback);
        executor.submit(downloadTask);
    }
}
