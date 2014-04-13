package com.tencent.mm.pluginsdk.downloader.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.tencent.mm.pluginsdk.downloader.util.Preconditions;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by simsun on 2014/4/11.
 */
public class PluginDescription implements Parcelable{

    /**Download when required*/
    public static final int DOWN_NONE = 0;
    /**Try to download in background if Wifi or faster network is available*/
    public static final int DOWN_WIFI = 1;
    /**Try to download in background if 3G or faster network is available*/
    public static final int DOWN_3G = 2;
    /**Try to download in background*/
    public static final int DOWN_ALWAYS = DOWN_3G | DOWN_WIFI;

    /**id = pluginName + version*/
    public final String id;
    public final String url;
    public final String md5;
    public final String version;
    private String jsonString;
    public int size;

    public PluginDescription(JSONObject json) throws JSONException {
        this.jsonString = Preconditions.checkNotNull(json).toString();
        this.id = Preconditions.checkNotNull(json.getString("id"));
        this.url = Preconditions.checkNotNull(json.getString("url"));
        this.md5 = Preconditions.checkNotNull(json.getString("md5"));
        this.version = Preconditions.checkNotNull(json.getString("version"));
        this.size = json.optInt("size");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(md5);
        dest.writeString(version);
        dest.writeInt(size);
    }

    public static final Parcelable.Creator<PluginDescription> CREATOR
            = new Parcelable.Creator<PluginDescription>() {
        @Override
        public PluginDescription createFromParcel(Parcel source) {
            return new PluginDescription(source);
        }

        @Override
        public PluginDescription[] newArray(int size) {
            return new PluginDescription[size];
        }
    };

    protected PluginDescription(Parcel source) {
        id = Preconditions.checkNotNull(source.readString());
        url = Preconditions.checkNotNull(source.readString());
        md5 = Preconditions.checkNotNull(source.readString());
        version = Preconditions.checkNotNull(source.readString());
        size = source.readInt();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append(":")
                .append(url).append(":")
                .append(md5).append(":")
                .append(version);
        return stringBuilder.toString();
    }

    public String getJsonString() {
        if (jsonString == null || jsonString.length() <= 0) {
            throw new IllegalArgumentException("jsonString is empty");
        }
        return jsonString;
    }
}
