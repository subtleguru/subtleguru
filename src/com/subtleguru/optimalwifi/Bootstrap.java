package com.subtleguru.optimalwifi;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Bootstrap extends BroadcastReceiver {
	
	private static final String LOG_TAG = OptimalWifiApplication.TAG;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if ("android.intent.action.BOOT_COMPLETED".equals(action) ) {
			  Intent i = new Intent();
			  i.setClassName( "com.subtleguru.optimalwifi", "com.subtleguru.optimalwifi.OptimalWifiService");
			  ComponentName cname = context.startService(i);
			  if (cname == null)
				Log.e( LOG_TAG,"BackgroundService was not started on boot" );
			  else
				Log.d( LOG_TAG,"BackgroundService started on boot" );
			}
	}
}
