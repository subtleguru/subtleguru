package com.subtleguru.optimalwifi;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class OptimalWifiService extends Service {	
	
	private static final String TAG = OptimalWifiApplication.TAG;
	
	NotificationManager notiMgr;

	Handler handler = new Handler();
	OptimalWifiApplication app;
	
	@Override
	public void onCreate() {
		Log.i(TAG,"OptimalWifi service - version = " + OptimalWifiApplication.VERSION);
		super.onCreate();
		app =  (OptimalWifiApplication)getApplication();
		app.setRunning(true);
		notiMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Thread thread = new Thread(null,new ServiceWorker(), "OptimalWifiService");
		Toast.makeText(this, "OptimalWifiService starting", Toast.LENGTH_SHORT).show();
		thread.start();
	}
	
	class ServiceWorker implements Runnable {		
		public void run() {
			performBackgroundProcessing();			
		}		
	};
	
	private void performBackgroundProcessing() {	    
	    app.setRunning(true);
		WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		WifiScanResultsReceiver scanReciever = new WifiScanResultsReceiver(wifiMgr,handler,getApplicationContext());
		while (app.isRunning() && !app.isWaitingForScanResults()) {			
			if (wifiMgr.isWifiEnabled()) {
				IntentFilter i = new IntentFilter(); 
			    i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION); 
			    registerReceiver(scanReciever,i);
			    app.setWaitingForScanResults(true);
			    wifiMgr.startScan();	
	//		    handler.post(notifyWifiChange);
			    Log.d(TAG,"Checking Wifi Strength");
			}
			try {
				Thread.sleep(app.getScanPeriod()*1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
//	private Runnable notifyWifiChange = new Runnable() {
//	   public void run() {
//	      Toast.makeText(getApplicationContext(), "Scanning wifi ...", Toast.LENGTH_SHORT).show();
//	   }
//	};
    	

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "OptimalWifiService stopped", Toast.LENGTH_SHORT).show();
		app.setRunning(false);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
