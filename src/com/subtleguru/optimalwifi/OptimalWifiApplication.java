package com.subtleguru.optimalwifi;

import android.app.Application;

public class OptimalWifiApplication extends Application {
	
	private long scanPeriod=180;  // In seconds
	private boolean running = false;  
	
	private boolean waitingForScanResults;
	private OptimalWifiActivity optimalWifiActivity = null;
	private boolean runOnStart = true;
	private boolean runNow  = false;
	
	public static String VERSION = "0.92";
	public static final String TAG = "OptimalWifi";
	
	public boolean isRunNow() {
		return runNow;
	}

	public void setRunNow(boolean runNow) {
		this.runNow = runNow;
	}

	public boolean isRunOnStart() {
		return runOnStart;
	}

	public void setRunOnStart(boolean runOnStart) {
		this.runOnStart = runOnStart;
	}

	public boolean isWaitingForScanResults() {
		return waitingForScanResults;
	}

	public void setWaitingForScanResults(boolean waitingForScanResults) {
		this.waitingForScanResults = waitingForScanResults;
	}

	public long getScanPeriod() {
		return scanPeriod;
	}

	public void setScanPeriod(long scanPeriod) {
		this.scanPeriod = scanPeriod;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public void setActivity(OptimalWifiActivity optimalWifiActivity) {
		this.optimalWifiActivity = optimalWifiActivity;		
	}

	public OptimalWifiActivity getOptimalWifiActivity() {
		return optimalWifiActivity;
	}	
	

}
