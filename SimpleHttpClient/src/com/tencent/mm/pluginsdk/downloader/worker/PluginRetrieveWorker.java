package com.tencent.mm.pluginsdk.downloader.worker;

import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by simsun on 2014/4/11.
 */
public interface PluginRetrieveWorker {
    public InputStream retrievePlugin(URL pluginUrl);
    public void savePlugin(InputStream inputStream, File pluginFile);

    public PluginDescription retrieveConfig(URL configUrl);
    public void saveConfig(PluginDescription pluginDescription, File configFile);

    public boolean verifyPlugin(String hash, File Plugin);
}
