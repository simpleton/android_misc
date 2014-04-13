package com.tencent.mm.pluginsdk.downloader;

import com.tencent.mm.pluginsdk.downloader.worker.MMPluginRetrieveWorker;
import com.tencent.mm.pluginsdk.downloader.worker.PluginExecutorService;
import com.tencent.mm.pluginsdk.downloader.worker.PluginRetrieveWorker;

import java.util.concurrent.ThreadPoolExecutor;

public class MMPluginLoaderFacotry {

    public static PluginLoader build() {
        ThreadPoolExecutor threadPoolExecutor = new PluginExecutorService();
        PluginRetrieveWorker pluginRetrieveWorker = new MMPluginRetrieveWorker(new OkHttpDownloadClient());
        return new PluginLoader(threadPoolExecutor, pluginRetrieveWorker);
    }
}
