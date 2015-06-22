package com.wojtek.arduinobluetoothrobot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import com.wojtek.arduinobluetoothrobot.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {

	private ImageButton ib_Beetle;
	private Button b_Connect;
	private Button b_Accelerometer;
	private Button b_Ultrasonic;
	private Button b_Light;
	private TextView tv_Accelerometer;
	private Vibrator vibrator;
	private BluetoothDevice nxtDevice;
	private AlertDialog.Builder adB; ;

	private int widthScreen;
	private int heightScreen;
	private int x;
	private int y;

	private SensorManager gSensor;
	private SensorEventListener gSensorEventListener;
	private BluetoothSocket btSocket;
	public OutputStream beetle_OutputStream;
	private BluetoothAdapter btAdapter;
	private int REQUEST_ENABLE_BT = 1;
	private String beetle_id = "00:13:03:21:06:14";

	private boolean boolean_Connect = false;
	private boolean boolean_Accelerometer = false;
	private boolean boolean_Ultrasonic = false;
	private boolean boolean_Light = false;

	private enum Sterowanie {
		FORWARD, FAST_FORWARD, BACKWARD, RIGHT, FAST_RIGHT, LEFT, FAST_LEFT, LEFT_B, FAST_LEFT_B, RIGHT_B, FAST_RIGHT_B, STOP, PAUSE
	};

	Sterowanie sterowanie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		// download screen width
		widthScreen = getResources().getDisplayMetrics().widthPixels;
		// download screen height
		heightScreen = getResources().getDisplayMetrics().heightPixels;
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		//btAdapter = BluetoothAdapter.getDefaultAdapter();
		ib_Beetle = (ImageButton) findViewById(R.id.imageButton_Garbus);
		b_Connect = (Button) findViewById(R.id.button_wojtek_Connect);
		b_Accelerometer = (Button) findViewById(R.id.button_wojtek_Accelerometer);
		b_Ultrasonic = (Button) findViewById(R.id.button_wojtek_Ultrasonic);
		b_Light = (Button) findViewById(R.id.button_wojtek_Light);
		tv_Accelerometer = (TextView) findViewById(R.id.textView_wojtek_Accelerometer);
		//connectBluetooth();
		// creating scalable buttons depending of screen width

		Drawable imageButtonBeetle = getResources().getDrawable(
				R.drawable.garbus);
		Bitmap bitmapLock = ((BitmapDrawable) imageButtonBeetle).getBitmap();
		Drawable ibB = new BitmapDrawable(getResources(),
				Bitmap.createScaledBitmap(bitmapLock, widthScreen / 10,
						widthScreen / 17, true));
		ib_Beetle.setBackground(ibB);

		

		ib_Beetle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				vibrator.vibrate(500);
				Toast.makeText(
						getApplicationContext(),
						"Wojtek Olasiewicz" + "\n\r" + "w.olasiewicz@gmail.com",
						Toast.LENGTH_LONG).show();

			}
		});
        b_Connect.setY(heightScreen/10);
		b_Connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vibrator.vibrate(500);
				connectBeetle();
			}
		});
		b_Accelerometer.setY(heightScreen/10 + b_Connect.getHeight());
		b_Accelerometer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (boolean_Connect == false) {
					Toast.makeText(getApplicationContext(),
							R.string.wojtek_ToastFirstConnect,
							Toast.LENGTH_SHORT).show();
					return;
				}

				else {
					if (boolean_Accelerometer == false) {

						if ((boolean_Ultrasonic) || (boolean_Light)) {
							Toast.makeText(getApplicationContext(),
									R.string.wojtek_check_activity,
									Toast.LENGTH_SHORT).show();
							return;
						}
						vibrator.vibrate(500);
						startAccelerometer();
						b_Accelerometer
								.setText(R.string.button_wojtek_Accelerometer_On);
						boolean_Accelerometer = true;
					} else {
						vibrator.vibrate(500);
						stopAccelerometer();
						b_Accelerometer
								.setText(R.string.button_wojtek_Accelerometer_Off);
						boolean_Accelerometer = false;
					}
				}

			}
		});
		b_Ultrasonic.setY(heightScreen/10 + b_Accelerometer.getHeight());
		b_Ultrasonic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (boolean_Connect == false) {
					Toast.makeText(getApplicationContext(),
							R.string.wojtek_ToastFirstConnect,
							Toast.LENGTH_SHORT).show();
					return;
				}

				else {
					if (boolean_Ultrasonic == false) {
						if ((boolean_Accelerometer) || (boolean_Light)) {
							Toast.makeText(getApplicationContext(),
									R.string.wojtek_check_activity,
									Toast.LENGTH_SHORT).show();
							return;
						}
						vibrator.vibrate(500);
						startUltrasonic();
						b_Ultrasonic
								.setText(R.string.button_wojtek_Ultrasonic_On);
						boolean_Ultrasonic = true;
					} else {
						vibrator.vibrate(500);
						stop();
						b_Ultrasonic
								.setText(R.string.button_wojtek_Ultrasonic_Off);
						boolean_Ultrasonic = false;
					}
				}

			}
		});
		b_Light.setY(heightScreen/10 + b_Ultrasonic.getHeight());
		b_Light.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (boolean_Connect == false) {
					Toast.makeText(getApplicationContext(),
							R.string.wojtek_ToastFirstConnect,
							Toast.LENGTH_SHORT).show();
					return;
				}

				else {
					if (boolean_Light == false) {
						if ((boolean_Ultrasonic) || (boolean_Accelerometer)) {
							Toast.makeText(getApplicationContext(),
									R.string.wojtek_check_activity,
									Toast.LENGTH_SHORT).show();
							return;
						}
						vibrator.vibrate(500);
						startLight();
						b_Light.setText(R.string.button_wojtek_Light_On);
						boolean_Light = true;
					} else {
						vibrator.vibrate(500);
						stop();
						b_Light.setText(R.string.button_wojtek_Light_Off);
						boolean_Light = false;
					}
				}

			}
		});

	}

	private void connectBeetle() {

		if (boolean_Connect == false) {

			nxtDevice = btAdapter.getRemoteDevice(beetle_id);

			try {
				btSocket = nxtDevice.createRfcommSocketToServiceRecord(UUID
						.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				btSocket.connect();
				Toast.makeText(getBaseContext(),
						R.string.wojtek_ToastConnected, Toast.LENGTH_SHORT)
						.show();

			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						R.string.wojtek_ToastNotConnected, Toast.LENGTH_SHORT)
						.show();
				return;

			}
			boolean_Connect = true;
			b_Connect.setText(R.string.button_wojtek_Connected);

		} else {
			if ((boolean_Ultrasonic) || (boolean_Accelerometer)
					|| (boolean_Light)) {
				Toast.makeText(getApplicationContext(),
						R.string.wojtek_check_activity, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			try {

				btSocket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			b_Connect.setText(R.string.button_wojtek_Disconnected);
			boolean_Connect = false;
		}
	}

	private void connectBluetooth() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter != null) {
			if (!btAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	private void sendData(String message) {
		byte[] command = message.getBytes();

		try {
			beetle_OutputStream = btSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			beetle_OutputStream.write(command);

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	private void startAccelerometer() {

		sendData("p");
		sendData("A");
		gSensorEventListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {

				x = Math.round(event.values[0]);
				y = Math.round(event.values[1]);

				if (x <= -3 && x > -6) {
					if (y > -3 || y < 3) {
						sterowanie = Sterowanie.FORWARD;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_forward);
					}

					if (y <= -3 && y > -8) {
						sterowanie = Sterowanie.LEFT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_left);
					}

					if (y <= -8) {
						sterowanie = Sterowanie.FAST_LEFT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_left);
					}

					if (y >= 3 && y < 8) {
						sterowanie = Sterowanie.RIGHT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_right);
					}

					if (y >= 8) {
						sterowanie = Sterowanie.FAST_RIGHT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_right);
					}
				}

				if (x <= -6) {
					if (y > -3 || y < 3) {
						sterowanie = Sterowanie.FAST_FORWARD;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_forward);
					}

					if (y <= -3 && y > -8) {
						sterowanie = Sterowanie.LEFT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_left);
					}

					if (y <= -8) {
						sterowanie = Sterowanie.FAST_LEFT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_left);
					}

					if (y >= 3 && y < 8) {
						sterowanie = Sterowanie.RIGHT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_right);
					}

					if (y >= 8) {
						sterowanie = Sterowanie.FAST_RIGHT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_right);
					}

				}

				if ((x >= 0 && x < 3) || (x <= 0 && x > -3)) {
					if ((y >= 0 && y < 3) || (y <= 0 && y > -3)) {
						sterowanie = Sterowanie.STOP;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_stop);
					}

					if (y <= -3 && y > -8) {
						sterowanie = Sterowanie.LEFT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_left);
					}

					if (y <= -8) {
						sterowanie = Sterowanie.FAST_LEFT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_left);
					}

					if (y >= 3 && y < 8) {
						sterowanie = Sterowanie.RIGHT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_right);
					}

					if (y >= 8) {
						sterowanie = Sterowanie.FAST_RIGHT;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_right);
					}

				}

				if (x >= 3) {
					if (y > -3 || y < 3) {
						sterowanie = Sterowanie.BACKWARD;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_backward);
					}

					if (y <= -3 && y > -8) {
						sterowanie = Sterowanie.LEFT_B;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_left);
					}

					if (y <= -8) {
						sterowanie = Sterowanie.FAST_LEFT_B;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_left);
					}

					if (y >= 3 && y < 8) {
						sterowanie = Sterowanie.RIGHT_B;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_right);
					}

					if (y >= 8) {
						sterowanie = Sterowanie.FAST_RIGHT_B;
						commands();
						tv_Accelerometer
								.setText(R.string.textView_accelerometer_f_right);
					}

				}
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

		};

		gSensor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		gSensor.registerListener(gSensorEventListener,
				gSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	private void stopAccelerometer() {
		gSensor.unregisterListener(gSensorEventListener);
		sterowanie = Sterowanie.PAUSE;
		commands();
		tv_Accelerometer.setText("");

	}

	private void stop() {
		sendData("X");
	}

	private void commands() {
		switch (sterowanie) {
		case BACKWARD:
			sendData("b");
			break;

		case FORWARD:
			sendData("f");
			break;

		case FAST_FORWARD:
			sendData("F");
			break;

		case RIGHT:
			sendData("r");
			break;

		case RIGHT_B:
			sendData(">");
			break;

		case FAST_RIGHT:
			sendData("R");
			break;

		case FAST_RIGHT_B:
			sendData("}");
			break;

		case LEFT:
			sendData("l");
			break;

		case FAST_LEFT:
			sendData("L");
			break;

		case LEFT_B:
			sendData("<");
			break;

		case FAST_LEFT_B:
			sendData("{");
			break;

		case STOP:
			sendData("s");
			break;

		case PAUSE:
			sendData("X");
			break;
		}
	}

	private void startUltrasonic() {
		sendData("p");
		sendData("U");
	}

	private void startLight() {
		sendData("p");
		sendData("S");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_close:
			vibrator.vibrate(500);
			adB = new AlertDialog.Builder(this);
			adB.setTitle(R.string.menuActivity_dialogExit_title);
			adB.setMessage(R.string.menuActivity_dialogExit_message);
			adB.setPositiveButton(R.string.dialog_Exit,
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int which) {

					btAdapter.disable();
					System.exit(0);
				}
			});
			adB.setNegativeButton(R.string.dialog_Cancel,
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int which) {
					dialog.cancel();
				}
			});

	adB.show();
	break;
}
			
			
			return true;
		}
		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_wojtek__car, menu);
		return true;
	}


	@Override
	protected void onResume() {
		super.onResume();
		
		connectBluetooth();
		gSensor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		gSensor.registerListener(gSensorEventListener,
				gSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		/** Turn on: */
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.screenBrightness = 0.9f;
		this.getWindow().setAttributes(params);
	}

	@Override
	protected void onPause() {
		super.onPause();
	
	}

}
