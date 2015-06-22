package com.wojtek.arduinobluetoothrobot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ArduinoBluetoothRobotStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arduino_bluetooth_robot_start);
	getActionBar().hide();
		
		//fluent transition to new thread after 4s
		new Handler().postDelayed(new Thread(){
			@Override 
			public void run()
			{
				Intent startActivityIntent = new Intent(ArduinoBluetoothRobotStart.this, MenuActivity.class);
				ArduinoBluetoothRobotStart.this.startActivity(startActivityIntent);
				ArduinoBluetoothRobotStart.this.finish();
				overridePendingTransition(R.layout.fadein, R.layout.fadeout);
			}
		}, 4000);
	}


}
