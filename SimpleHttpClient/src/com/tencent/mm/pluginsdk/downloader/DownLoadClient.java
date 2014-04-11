package com.tencent.mm.pluginsdk.downloader;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by simsun on 2014/4/12.
 */
public interface DownLoadClient {
    public InputStream open(URL url);
}
