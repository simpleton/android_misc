package com.tencent.mm.pluginsdk.downloader.worker;

import com.tencent.mm.pluginsdk.downloader.PluginLoader;
import com.tencent.mm.pluginsdk.downloader.util.Preconditions;

import java.io.File;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public abstract class DownloadTask implements Runnable {

    protected final String pluginName;
    protected final URL url;
    protected final DownLoadClient downLoadClient;
    protected final PluginLoader pluginLoader;
    protected final File outFile;

    public DownloadTask(
            String pluginName,
            URL url,
            DownLoadClient downLoadClient,
            PluginLoader pluginLoader,
            File outFile) {
        this.pluginName = Preconditions.checkNotNull(pluginName);
        this.url = Preconditions.checkNotNull(url);
        this.downLoadClient = Preconditions.checkNotNull(downLoadClient);
        this.pluginLoader = Preconditions.checkNotNull(pluginLoader);
        this.outFile = Preconditions.checkNotNull(outFile);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(getThreadPrefix() + pluginName);
    }

    public abstract String getThreadPrefix();
}
