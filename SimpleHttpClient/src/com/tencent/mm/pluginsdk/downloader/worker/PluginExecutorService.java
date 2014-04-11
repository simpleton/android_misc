package com.tencent.mm.pluginsdk.downloader.worker;

import android.net.NetworkInfo;
import com.tencent.mm.pluginsdk.downloader.util.NetWork;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by simsun on 2014/4/11.
 */
public class PluginExecutorService extends ThreadPoolExecutor {
    private static final int DEFAULT_THREAD_COUNT = 2;

    public PluginExecutorService() {
        super(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new NetWork.PluginThreadFactory());
    }

    void adjustThreadCount(NetworkInfo info) {
        int type = NetWork.getNetworkType(info);
        switch (type) {
            case NetWork.MOBILE_2G:
                setThreadCount(1);
                break;
            case NetWork.MOBILE_3G:
                setThreadCount(2);
                break;
            case NetWork.MOBILE_4G:
                setThreadCount(3);
                break;
            case NetWork.WIFI:
                setThreadCount(4);
                break;
            default:
                setThreadCount(DEFAULT_THREAD_COUNT);
        }
    }

    private void setThreadCount(int threadCount) {
        setCorePoolSize(threadCount);
        setMaximumPoolSize(threadCount);
    }
}
