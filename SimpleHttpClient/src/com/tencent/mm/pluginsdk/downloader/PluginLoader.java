package com.tencent.mm.pluginsdk.downloader;

import com.tencent.mm.pluginsdk.downloader.worker.AarDownloadTask;
import com.tencent.mm.pluginsdk.downloader.worker.ConfigFileDownloadTask;
import com.tencent.mm.pluginsdk.downloader.worker.PluginRetrieveWorker;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by simsun on 2014/4/12.
 */
public class PluginLoader {
    private final ThreadPoolExecutor executor;

    public PluginLoader(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    /**
     * load Config file asynchronously
     * @param pluginName target plugin name {@code PluginManager.SUPPORTED_PLUGINS}
     * @param url url to download
     * @param pluginRetrieveWorker just a worker wrapper for downloading plugin's info
     * @param outFile the file will be written
     * @param callback asynchronized listener
     */
    public void loadConfigFile(
            String pluginName,
            URL url,
            PluginRetrieveWorker pluginRetrieveWorker,
            File outFile,
            ConfigFileDownloadTask.OnComplete callback) {

    }

    /**
     * download target shoot aar package asynchronously.
     * @param pluginName target plugin name {@code PluginManager.SUPPORTED_PLUGINS}
     * @param url url to download
     * @param pluginRetrieveWorker just a worker wrapper for downloading plugin's info
     * @param outFile the file will be written
     * @param callback asynchronized listener
     */
    public void loadPlugin(
            String pluginName,
            URL url,
            PluginRetrieveWorker pluginRetrieveWorker,
            File outFile,
            AarDownloadTask.OnComplete callback) {

    }

}
