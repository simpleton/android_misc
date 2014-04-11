package com.tencent.mm.pluginsdk.downloader;

import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;
import com.tencent.mm.pluginsdk.downloader.util.HexUtil;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by simsun on 2014/4/11.
 */
public class MMPluginLoader implements PluginLoader {

    @Override
    public boolean verifyPlugin(String hash, File Plugin) {
        return HexUtil.getMd5ByFile(Plugin).equals(hash);
    }

    @Override
    public InputStream retrievePlugin(URL pluginUrl) {
        return null;
    }

    @Override
    public void savePlugin(InputStream inputStream, File pluginFile) {

    }

    @Override
    public PluginDescription retrieveConfig(URL configUrl) {
        return null;
    }

    @Override
    public void saveConfig(PluginDescription pluginDescription, File configFile) {

    }
}
