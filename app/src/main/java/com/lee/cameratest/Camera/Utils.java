package com.lee.cameratest.Camera;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by lihe6 on 2016/6/16.
 */
public class Utils {

    public static String getBroadcastIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int maskaddress = dhcpInfo.netmask;
            int ipaddress = wifiInfo.getIpAddress();

            int broadcast = ipaddress & maskaddress | ~maskaddress;
            byte[] quads = new byte[4];
            for (int k = 0; k < 4; k++) {
                quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
            }
             return getBroadcastIp(quads);
        }
        return null;
    }

    private static String getBroadcastIp(byte[] i) {
        if (i.length == 4) {
            return (i[0] & 0xFF) + "." + (i[1] & 0xFF) + "." + (i[2] & 0xFF) + "." + (i[3] & 0xFF);
        }
        return null;
    }

}
