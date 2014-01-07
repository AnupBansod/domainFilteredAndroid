package com.android.newapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class BootCompletedIntentReceiver extends BroadcastReceiver {
	long interval = 30000;
	Intent pushIntent;
	Context c;
	@Override
	public void onReceive(Context context, Intent intent) {
			c= context;
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			    	c.startService(new Intent(context, SocketServerService.class));
			    }
			};
}

