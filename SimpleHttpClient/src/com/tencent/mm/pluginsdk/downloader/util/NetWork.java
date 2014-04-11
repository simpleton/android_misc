package com.tencent.mm.pluginsdk.downloader.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.concurrent.ThreadFactory;

/**
 * Created by simsun on 2014/4/11.
 */
public class NetWork {
    public static final int UNKNOWN = 0;
    public static final int MOBILE_2G = 1;
    public static final int MOBILE_3G = 2;
    public static final int MOBILE_4G = 3;
    public static final int WIFI = 4;
    private static final String TAG = "NetWork";

    /**
     * get current net workstatus
     */
    public static int getNetworkType(NetworkInfo info) {
        if (info == null || !info.isConnectedOrConnecting()) {
            return UNKNOWN;
        }
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                return WIFI;
            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE: // 4G
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        return MOBILE_4G;
                    case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        return MOBILE_3G;
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return MOBILE_2G;
                    default:
                        return UNKNOWN;
                }
            default:
                return UNKNOWN;
        }

    }

    public static class PluginThreadFactory implements ThreadFactory {
        @SuppressWarnings("NullableProblems")
        public Thread newThread(Runnable r) {
            return new PluginThread(r);
        }
    }

    private static class PluginThread extends Thread {
        public PluginThread(Runnable r) {
            super(r);
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            super.run();
        }
    }
}
