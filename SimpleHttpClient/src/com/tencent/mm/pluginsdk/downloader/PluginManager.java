package com.tencent.mm.pluginsdk.downloader;

import android.content.Context;
import android.util.Log;
import com.tencent.mm.pluginsdk.downloader.model.PluginDescription;
import com.tencent.mm.pluginsdk.downloader.worker.PluginRetrieveWorker;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by simsun on 2014/4/11.
 */
public class PluginManager {
    private static final String TAG = "PluginManager";
    public static final String STATUS_DONE = "DONE";
    public static final String STATUS_IDLE = "IDLE";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String[] SUPPORTED_PLUGINS = {"shoot"};

    private final PluginRetrieveWorker loader;

    public PluginManager(PluginRetrieveWorker loader) {
        this.loader = loader;
    }

    public interface StatusChangeListener {
        void onStatusChanged(PluginDescription description, String currStatus);
    }
    private List<StatusChangeListener> listeners = Collections.synchronizedList(new ArrayList<StatusChangeListener>());

    public void addListener(StatusChangeListener listener) {
        if (listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(StatusChangeListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
    /**
     * get target plugin's Description. <b>DONOT Call this method in UI Thread<b/>
     * @param pluginName target plugin name, it should contains {@code SUPPORTED_PLUGINS}
     * @return {@link PluginDescription} Plugin descriptions which retieved from Internet.
     */
    public PluginDescription getPluginDescription(Context context, String pluginName) {
        PluginDescription pluginDescription = null;
        try {
            pluginDescription = loadLocalDescription(context, pluginName);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        /*local storage didn't existed, retireve from network*/
        if (pluginDescription == null) {
            pluginDescription = updateDescription(pluginName);
        }
        return pluginDescription;
    }

    /**
     * force update plugin config json file. <b>DONOT Call this method in UI Thread<b/>
     * @param pluginName target plugin name, it should contains {@code SUPPORTED_PLUGINS}
     * @return {@link PluginDescription} Plugin description
     */
    public PluginDescription updateDescription(String pluginName) {
        URL url = null;
        try {
            url = buildPluginConfigUrl(getCDNDomain(), pluginName, getClientVersion());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
        }
        return loader.retrieveConfig(url);
    }

    /**
     * start downloading target plugin, if local repo didnot existed or exipred.
     * @param pluginName target plugin name, it should contains {@code SUPPORTED_PLUGINS}
     */
    public void startDownloadPluginIfNecessary(Context context, String pluginName) {
        try {
            PluginDescription localDescription = loadLocalDescription(context, pluginName);
            PluginDescription remoteDescription = updateDescription(pluginName);
            if (needDownloadPlugin(localDescription, remoteDescription)) {
               /* loader.retrievePlugin(new URL(remoteDescription.url),
                        new File(getPluginStorage(context), remoteDescription.md5 + ".aar"));*/
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean needDownloadPlugin(PluginDescription local, PluginDescription remote) {
        return local == null || !local.md5.equals(remote.md5);
    }

    public static File getPluginStorage(Context context) {
        File file = new File(context.getFilesDir(), "plugin_repo");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return file;
    }

    public static String getClientVersion() {
        return "5.3";
    }

    public static String getCDNDomain() {
        return "http://106.187.52.67:8080";
    }

    public static File getConfigFile(Context context, String pluginName) {
        return new File(getPluginStorage(context), pluginName + "_config.json");
    }

    private PluginDescription loadLocalDescription(Context context, String pluginName) throws IOException{
        File file = getConfigFile(context, pluginName);
        if (!file.exists()) {
            return null;
        }

        PluginDescription pluginDescription = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            pluginDescription = new PluginDescription(new JSONObject(sb.toString()));
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return pluginDescription;
    }

    private URL buildPluginConfigUrl(String domain, String pluginName, String clientVersion) throws MalformedURLException{
        return new URL(String.format("%s/%s/%s_config.json", domain, clientVersion, pluginName));
    }


}

