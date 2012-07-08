package com.subtleguru.optimalwifi;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class WifiScanResultsReceiver extends BroadcastReceiver {

    WifiManager                 wifiMgr = null;
    Handler                     handler = null;
    Context                     context = null;
    OptimalWifiApplication      app     = null;
    String                      SSID    = "n/a";

    private static final String TAG     = OptimalWifiApplication.TAG; ;

    public WifiScanResultsReceiver(WifiManager wifiMgr, Handler handler, Context cntxr) {
        super();
        this.wifiMgr = wifiMgr;
        this.handler = handler;
        this.context = cntxr;
    }

    @Override
    public void onReceive(Context c, Intent intent) {
        c.unregisterReceiver(this);
        app = (OptimalWifiApplication) c.getApplicationContext();
        List<ScanResult> results = wifiMgr.getScanResults();
        if (results != null) {
            List<WifiConfiguration> knownAccessPoints = wifiMgr.getConfiguredNetworks();
            ScanResult bestSignal = null;
            WifiConfiguration bestConfiguredSignal = null;
            for (ScanResult result : results) {
                if (bestSignal == null || (WifiManager.compareSignalLevel(bestSignal.level, result.level) < 0))
                    bestSignal = result;
                for (WifiConfiguration wifiConfig : knownAccessPoints) {
                    // Log.d(TAG," remembered " + wifiConfig.SSID + "," +
                    // wifiConfig.BSSID);
                    if (equalsSSID(bestSignal.SSID, wifiConfig.SSID)) {
                        bestConfiguredSignal = wifiConfig;
                    }
                }
            }

            if (bestConfiguredSignal == null) {
                String message = String.format("%s networks in range, but none are known.");
                Log.d(TAG, message);
            } else {
                SSID = bestConfiguredSignal.SSID;
                String message = String.format("%s networks in range, %s is the strongest.", results.size(), bestSignal.SSID);
                Log.d(TAG, message);

                message = String.format("%s is the strongest configured signal", bestConfiguredSignal.SSID);
                Log.d(TAG, message);

                if (!equalsSSID(wifiMgr.getConnectionInfo().getSSID(), bestConfiguredSignal.SSID)) {
                    wifiMgr.enableNetwork(bestConfiguredSignal.networkId, true);
                    message = String.format("Optimizing wifi, connecting to SSID %s", bestConfiguredSignal.SSID);
                    Log.i(TAG, message);
                    handler.post(notifyWifiChange);
                }
            }
        }
        app.setWaitingForScanResults(false);
    }

    private boolean equalsSSID(String a, String b) {
        if (a == null || b == null) {
            return false;
        } else {
            return a.equals(b.replaceAll("\"", ""));
        }
    }

    private Runnable notifyWifiChange = new Runnable() {
        public void run() {
            Toast.makeText(context, "Connected to access point " + SSID, Toast.LENGTH_LONG).show();
            if (app.getOptimalWifiActivity() != null) {
                app.getOptimalWifiActivity().updateStatus();
            }
        }
    };

}