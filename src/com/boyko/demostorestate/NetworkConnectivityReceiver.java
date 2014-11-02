package com.boyko.demostorestate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivityReceiver extends BroadcastReceiver {


	public static final String NETWORK = "network";

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			Logger.e("Connectivity Manager is null!");
			return;
		}

		NetworkInfo network = cm.getActiveNetworkInfo();
			String networkName = network.getTypeName();

			
			Logger.e(networkName + ", " + network.isConnected());

			Intent svcIntent = new Intent(context,InitService.class);
			svcIntent.putExtra(NETWORK, networkName);
			context.startService(svcIntent);
	}

}
