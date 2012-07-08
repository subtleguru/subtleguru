package com.subtleguru.optimalwifi;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class OptimalWifiActivity extends Activity {
	
	private static final String TAG = OptimalWifiApplication.TAG;

	OptimalWifiApplication app;
	Button applyBtn;
	private TextView runningText;
	private TextView ssidText;
	private TextView wifiText;
	private TextView periodText;
	private CheckBox runOnStart;
	private CheckBox runNow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		app = (com.subtleguru.optimalwifi.OptimalWifiApplication) getApplication();
		app.setActivity(this);
		runningText = (TextView)findViewById(R.id.running);
		ssidText = (TextView)findViewById(R.id.ssid);
		wifiText = (TextView)findViewById(R.id.wifi);
		periodText = (TextView)findViewById(R.id.scanPeriod);
		runOnStart = (CheckBox)findViewById(R.id.runOnStart);
		runNow = (CheckBox)findViewById(R.id.runNow);
		
		//applyBtn = (Button)findViewById(R.id.apply);
		updateStatus();
		restoreState(savedInstanceState);
		
		final Button refreshBtn = (Button)findViewById(R.id.refresh);
				
		runNow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				  try {
					app.setScanPeriod(Long.parseLong(periodText.getText().toString()));
				  } catch (NumberFormatException e1) {
				  }
				  app.setRunOnStart(runOnStart.isChecked());
				  app.setRunNow(runNow.isChecked());
				  Intent i = new Intent();
				  i.setClassName( "com.subtleguru.optimalwifi", "com.subtleguru.optimalwifi.OptimalWifiService");
				  if (!app.isRunning() && runNow.isChecked()) {
					  startService(i);
				  } else {
					 if (app.isRunning() && !runNow.isChecked()) { 
					  stopService(i);
					 }
				  }
				  try {
					Thread.sleep(500);  // give the  service time to start
				  } catch (InterruptedException e) {
				  }				  
				  updateStatus();
			}			
		});	
		
		refreshBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateStatus();
			}			
		});	
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"Resuming activity....");
		updateStatus();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"Pausing activity....");
	}

	public void updateStatus() {		
		WifiManager wifiMgr = (WifiManager) getApplicationContext()
				.getSystemService(WIFI_SERVICE);
		String status = "Wifi is %s"; 
		wifiText.setText(String.format(status,(wifiMgr.isWifiEnabled() ? "enabled" : "disabled")));
		runningText.setText(String.format("OptimalWifi is %s in the background", 
				(app.isRunning() ? "running" : "not running")));

		if (wifiMgr.isWifiEnabled()) {
			String ssid = wifiMgr.getConnectionInfo().getSSID();
			ssidText.setText("Connected to SSID: " + ssid);
		} else {
			ssidText.setText("Not connected to wifi");
		}		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("periodText",periodText.getText().toString());
		outState.putBoolean("runOnStart",runOnStart.isChecked());
		outState.putBoolean("runNow",runNow.isChecked());		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		restoreState(savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void restoreState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.getString("periodText") != null) {
				periodText.setText(savedInstanceState.getString("periodText"));
			}
			if (savedInstanceState.get("runOnStart") != null) {
				runOnStart.setChecked(savedInstanceState
						.getBoolean("runOnStart"));
			}
			if (savedInstanceState.get("runNow") != null) {
				runNow.setChecked(savedInstanceState.getBoolean("runNow"));
			}
		} else {
			periodText.setText(String.format("%d",app.getScanPeriod()));
			runOnStart.setChecked(app.isRunOnStart());
			runNow.setChecked(app.isRunNow());					
		}
	}
	
	

}
