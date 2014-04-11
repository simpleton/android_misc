package com.tencent.mm.pluginsdk.downloader.worker;

import com.tencent.mm.pluginsdk.downloader.util.Preconditions;

import java.io.File;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public abstract class DownloadTask implements Runnable {

    protected final String pluginName;
    protected final URL url;
    protected final PluginRetrieveWorker pluginRetrieveWorker;
    protected final File outFile;

    public DownloadTask(
            String pluginName,
            URL url,
            PluginRetrieveWorker pluginRetrieveWorker,
            File outFile) {
        this.pluginName = Preconditions.checkNotNull(pluginName);
        this.url = Preconditions.checkNotNull(url);
        this.pluginRetrieveWorker = Preconditions.checkNotNull(pluginRetrieveWorker);
        this.outFile = Preconditions.checkNotNull(outFile);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(getThreadPrefix() + pluginName);
    }

    public abstract String getThreadPrefix();
}
