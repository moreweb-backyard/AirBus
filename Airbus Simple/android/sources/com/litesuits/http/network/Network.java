package com.litesuits.http.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.litesuits.http.log.HttpLog;
import java.lang.reflect.Method;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class Network {
    private static final String TAG = "Network";

    public enum NetType {
        None(1),
        Mobile(2),
        Wifi(4),
        Other(8);

        public int value;

        NetType(int i) {
            this.value = i;
        }
    }

    public static ConnectivityManager getConnManager(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = getConnManager(context).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isConnectedOrConnecting(Context context) {
        NetworkInfo[] allNetworkInfo = getConnManager(context).getAllNetworkInfo();
        if (allNetworkInfo != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static NetType getConnectedType(Context context) {
        NetworkInfo activeNetworkInfo = getConnManager(context).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            switch (activeNetworkInfo.getType()) {
                case 0:
                    return NetType.Mobile;
                case 1:
                    return NetType.Wifi;
                default:
                    return NetType.Other;
            }
        }
        return NetType.None;
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo activeNetworkInfo = getConnManager(context).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1 && activeNetworkInfo.isConnected();
    }

    public static boolean isMobileConnected(Context context) {
        NetworkInfo activeNetworkInfo = getConnManager(context).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0 && activeNetworkInfo.isConnected();
    }

    public static boolean isAvailable(Context context) {
        return isWifiAvailable(context) || (isMobileAvailable(context) && isMobileEnabled(context));
    }

    public static boolean isWifiAvailable(Context context) {
        NetworkInfo[] allNetworkInfo = getConnManager(context).getAllNetworkInfo();
        if (allNetworkInfo != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getType() == 1) {
                    return networkInfo.isAvailable();
                }
            }
        }
        return false;
    }

    public static boolean isMobileAvailable(Context context) {
        NetworkInfo[] allNetworkInfo = getConnManager(context).getAllNetworkInfo();
        if (allNetworkInfo != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getType() == 0) {
                    return networkInfo.isAvailable();
                }
            }
        }
        return false;
    }

    public static boolean isMobileEnabled(Context context) throws NoSuchMethodException, SecurityException {
        try {
            Method declaredMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            return ((Boolean) declaredMethod.invoke(getConnManager(context), new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean printNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            HttpLog.i(TAG, "-------------$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$-------------");
            HttpLog.i(TAG, "getActiveNetworkInfo: " + activeNetworkInfo);
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (allNetworkInfo != null) {
                for (int i = 0; i < allNetworkInfo.length; i++) {
                    HttpLog.i(TAG, "NetworkInfo[" + i + "]isAvailable : " + allNetworkInfo[i].isAvailable());
                    HttpLog.i(TAG, "NetworkInfo[" + i + "]isConnected : " + allNetworkInfo[i].isConnected());
                    HttpLog.i(TAG, "NetworkInfo[" + i + "]isConnectedOrConnecting : " + allNetworkInfo[i].isConnectedOrConnecting());
                    HttpLog.i(TAG, "NetworkInfo[" + i + "]: " + allNetworkInfo[i]);
                }
                HttpLog.i(TAG, IOUtils.LINE_SEPARATOR_UNIX);
            } else {
                HttpLog.i(TAG, "getAllNetworkInfo is null");
            }
        }
        return false;
    }
}
