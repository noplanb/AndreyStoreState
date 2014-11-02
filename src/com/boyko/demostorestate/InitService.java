package com.boyko.demostorestate;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;


public class InitService extends Service {

	public static final String EXTRA_NETWORK_STATUS = "EXTRA_NETWORK_STATUS";
	public static final String ACTION_UPDATE_NETWORK_STATE = "ACTION_UPDATE_NETWORK_STATE";
	private String currentNetworkState;

	@Override
	public IBinder onBind(Intent intent) {
		return new LocalBinder();
	}
	
	public class LocalBinder extends Binder{
		public InitService getService() {
			return InitService.this;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null) {
			currentNetworkState = network.getTypeName();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent!=null && intent.getStringExtra(NetworkConnectivityReceiver.NETWORK)!=null){
			currentNetworkState = intent.getStringExtra(NetworkConnectivityReceiver.NETWORK);
			Intent intentBr = new Intent(ACTION_UPDATE_NETWORK_STATE);
			intentBr.putExtra(EXTRA_NETWORK_STATUS, currentNetworkState);
			sendBroadcast(intentBr);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public String getCurrentNetworkState() {
		return currentNetworkState;
	}

}
