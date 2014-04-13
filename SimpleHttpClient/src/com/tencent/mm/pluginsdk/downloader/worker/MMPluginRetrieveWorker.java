package com.tencent.mm.pluginsdk.downloader.worker;

import android.util.Log;
import com.tencent.mm.pluginsdk.downloader.DownLoadClient;
import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;
import com.tencent.mm.pluginsdk.downloader.util.HexUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

/**
 * Created by simsun on 2014/4/11.
 */
public class MMPluginRetrieveWorker implements PluginRetrieveWorker {
    private static final String TAG = "MMPluginRetrieveWorker";
    private final DownLoadClient downLoadClient;

    public MMPluginRetrieveWorker(DownLoadClient downLoadClient) {
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
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(configFile);
            fileWriter.write(pluginDescription.getJsonString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            HexUtil.closeQuietly(fileWriter);
        }
    }
}
