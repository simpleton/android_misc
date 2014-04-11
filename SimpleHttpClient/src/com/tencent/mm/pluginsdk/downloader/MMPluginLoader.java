package com.tencent.mm.pluginsdk.downloader;

import android.util.Log;
import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;
import com.tencent.mm.pluginsdk.downloader.util.HexUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by simsun on 2014/4/11.
 */
public class MMPluginLoader implements PluginLoader {
    private static final String TAG = "MMPluginLoader";
    private final DownLoadClient downLoadClient;

    public MMPluginLoader(DownLoadClient downLoadClient) {
        this.downLoadClient = downLoadClient;
    }

    @Override
    public boolean verifyPlugin(String hash, File Plugin) {
        return HexUtil.getMd5ByFile(Plugin).equals(hash);
    }

    @Override
    public InputStream retrievePlugin(URL pluginUrl) {
        return downLoadClient.open(pluginUrl);
    }

    @Override
    public void savePlugin(InputStream inputStream, File pluginFile) {
        HexUtil.writeInputStreamToFile(inputStream, pluginFile);
    }

    @Override
    public PluginDescription retrieveConfig(URL configUrl) {
        InputStream inputStream = downLoadClient.open(configUrl);
        try {
            byte[] bytes = HexUtil.readFully(inputStream);
            Log.d(TAG, "config:" + new String(bytes, "UTF-8"));
            return new PluginDescription(new JSONObject(new String(bytes)));
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    @Override
    public void saveConfig(PluginDescription pluginDescription, File configFile) {

    }
}
