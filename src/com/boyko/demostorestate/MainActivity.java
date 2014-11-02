package com.boyko.demostorestate;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import com.boyko.demostorestate.InitService.LocalBinder;

public class MainActivity extends Activity {

	protected InitService service;
	private TextView twLabel;
	
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			service =  null;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = ((LocalBinder)binder).getService();
			twLabel.setText(service.getCurrentNetworkState());
		}
	};
	private NetworkStateStatusReceiver networkStatusBrodcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		twLabel = (TextView) findViewById(R.id.tw_label);
		twLabel.setText("Updating data...");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		networkStatusBrodcastReceiver = new NetworkStateStatusReceiver();
		IntentFilter filter = new IntentFilter(InitService.ACTION_UPDATE_NETWORK_STATE);
		registerReceiver(networkStatusBrodcastReceiver , filter );
		bindService(new Intent(this, InitService.class), conn, BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unbindService(conn);
		unregisterReceiver(networkStatusBrodcastReceiver);
	}
	
	private class NetworkStateStatusReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String stringExtra = intent.getStringExtra(InitService.EXTRA_NETWORK_STATUS);
			if(stringExtra!=null){
				twLabel.setText(stringExtra);
			}
		}
	}
}
