package com.sportsrally;

import java.security.Provider;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.security.auth.PrivateCredentialPermission;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	Button btnToggle, btnEnd,btnMusic;

	TextView txtSpentTime, txtSpeed, txtTotalDistance, txtAvgSpeed;
	EditText etxRecordName;
	private Context mContext;
	private UIHandler mUIHandler;
	private ProgressDialog myDialog;
	boolean isGpsEnabled;
	boolean isNetworkEnable;
	boolean isInternetEnable;
	long lastUpdateTime;
	static String bestProvider = "";
	static final int MIN_TIME = 1000;
	static final float MIN_DIST = 1;
	int gg;
	static LocationManager mgr = null;
	static Location myLocation;
	Marker marker;
	float zoom;
	GoogleMap gmap;
	DBhelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnToggle = (Button) findViewById(R.id.buttonToggle);
		btnToggle.setText("OFF");
		btnEnd = (Button) findViewById(R.id.btnEnd);
		btnMusic = (Button) findViewById(R.id.btnMusic);
		gmap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.pauseMap)).getMap();
		txtSpentTime = (TextView) findViewById(R.id.spentTime);
		txtSpeed = (TextView) findViewById(R.id.txtSpeed);
		txtAvgSpeed = (TextView) findViewById(R.id.txtAvgSpeed);
		txtTotalDistance = (TextView) findViewById(R.id.txtTotalDistance);
		mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

		dbHelper = new DBhelper(this);

		mContext = this;
		final MyValues myapp = (MyValues) mContext.getApplicationContext();

		myapp.spentSeconds = 0;
		myapp.distance = 0;
		myapp.idleCounter = 0;
		myapp.nowLocation = null;
		myapp.nowPoint = null;
		myapp.lastPoint = null;
		myapp.tmpPoint = null;
		myapp.isMoving = false;

		if (myapp.activeTableName == null) {
			myapp.activeTableName = dbHelper.getNewTableName();
			dbHelper.createTable(myapp.activeTableName);

		}
        
		btnMusic.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
				startActivity(intent);
			}
			
		});
		
		
		btnToggle.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub

				if (TimerService.getTimerState().equals(
						TimerService.State.Stopped)) {

					final MyValues myapp = (MyValues) mContext
							.getApplicationContext();
					if (myapp.activeTableName == null) {
						myapp.activeTableName = dbHelper.getNewTableName();
						dbHelper.createTable(myapp.activeTableName);
					}

					myapp.startTime = System.currentTimeMillis();

					Intent intent = new Intent(TimerService.ACTION_PLAY);
					startService(intent);
					btnToggle.setText("PAUSE");
					btnEnd.setVisibility(View.INVISIBLE);

				} else {
					// send intent to make service stop
					Intent intent = new Intent(TimerService.ACTION_STOP);
					startService(intent);
					btnToggle.setText("ON");
					btnEnd.setVisibility(View.VISIBLE);

					Intent intent2 = new Intent();
					intent2.setClass(MainActivity.this, MapForPause.class);
					startActivity(intent2);

				}

			}
		});

		btnEnd.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub


				showSaveTableDialog();
			

			}

		});

	}

	// onStart

	@Override
	protected void onStart() {
		// TODO 自動產生的方法 Stub
		super.onStart();

		haveInternet();
		checkProvider();
		Location lastKnownLocation = null;
		mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		bestProvider = null;
		if (isNetworkEnable && !isGpsEnabled) {
			bestProvider = LocationManager.NETWORK_PROVIDER;
			lastKnownLocation = mgr
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (isGpsEnabled) {
			bestProvider = LocationManager.GPS_PROVIDER;
			lastKnownLocation = mgr
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (bestProvider == null)
			bestProvider = LocationManager.PASSIVE_PROVIDER;
		if (lastKnownLocation == null) {
			lastKnownLocation = mgr
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		mgr.requestLocationUpdates(MainActivity.bestProvider, MIN_TIME,
				MIN_DIST, this);
		if (bestProvider != LocationManager.PASSIVE_PROVIDER) {
			createCancelProgressDialog("系統", "取得" + bestProvider + "定位中..請稍待",
					"取消");
		} else {
			btnToggle.setVisibility(View.INVISIBLE);
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("系統");
			dialog.setMessage("沒有可使用的定位來源");
			dialog.setIcon(android.R.drawable.ic_dialog_alert);
			dialog.setCancelable(false);
			dialog.setPositiveButton("更改設定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							showNetworkSetup();
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			dialog.show();
		}

	}

	@SuppressWarnings("deprecation")
	private void createCancelProgressDialog(String title, String message,
			String buttonText) {
		myDialog = new ProgressDialog(this);
		myDialog.setTitle(title);
		myDialog.setMessage(message);
		myDialog.setButton(buttonText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Use either finish() or return() to either close the activity
				// or just the diag

			}
		});
		myDialog.show();
	}

	static public void getLocationProvider() {
		Criteria mCriteria = new Criteria();
		mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		mCriteria.setAltitudeRequired(true);
		mCriteria.setBearingRequired(true);
		mCriteria.setSpeedRequired(true);
		mCriteria.setCostAllowed(true);
		mCriteria.setPowerRequirement(Criteria.POWER_HIGH);
		bestProvider = mgr.getBestProvider(mCriteria, true);
		myLocation = mgr.getLastKnownLocation(bestProvider);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO 自動產生的方法 Stub
		super.onResume();

		if (TimerService.getTimerState().equals(TimerService.State.Running)) {
			btnToggle.setText("Stop");

			mUIHandler = new UIHandler(MainActivity.this);
			TimerService.registerHandler(mUIHandler);
			TimerService.resetServiceThreadHandler();

		} else {
			btnToggle.setText("On");
			mUIHandler = new UIHandler(MainActivity.this);
			TimerService.registerHandler(mUIHandler);

		}

	}

	@Override
	protected void onStop() {
		// TODO 自動產生的方法 Stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO 自動產生的方法 Stub
		if (mgr != null) {
			mgr.removeUpdates(this);
		}
		Intent intent = new Intent(MainActivity.this, TimerService.class);
		android.os.Process.killProcess(android.os.Process.myPid());

		stopService(intent);
		
		dbHelper.close();
		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO 自動產生的方法 Stub
		final MyValues myapp = (MyValues) this.getApplicationContext();
		btnToggle.setVisibility(View.VISIBLE);
		myapp.isMoving = true;
		myDialog.dismiss();
		myapp.lastPoint = myapp.nowPoint;
		myapp.lastLocation = myapp.nowLocation;
		myapp.nowPoint = new LatLng(location.getLatitude(),
				location.getLongitude());
		myapp.nowLocation = location;
		zoom = 17;
		float speed = location.getSpeed();
		txtSpeed.setText(String.format("%f", speed));
		int movingIcon=0;
		if (speed==0) movingIcon=R.drawable.man;
		if (speed>0&&speed<=2.5) movingIcon = R.drawable.turtle;
		if (speed>2.5&&speed<=5) movingIcon=R.drawable.rabbit;
		if(speed>5) movingIcon = R.drawable.eagle;
		if(speed>10) movingIcon = R.drawable.superman;
		gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(myapp.nowPoint,
				zoom));
		if (marker != null)
			marker.remove();
		marker = gmap.addMarker(new MarkerOptions().position(myapp.nowPoint)
				.icon(BitmapDescriptorFactory.fromResource(movingIcon)));

		if (location.getProvider().equals("gps")
				&& myapp.lastPoint != null && myapp.nowPoint != null) {
			myapp.distance += GetDistance(myapp.lastPoint, myapp.nowPoint);
			txtTotalDistance.setText(String.format("%f", myapp.distance));
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動產生的方法 Stub
		// Log.d("mytag","onProviderDisabled");
		// getLocationProvider();
		// mgr.requestLocationUpdates(MainActivity.bestProvider, MIN_TIME,
		// MIN_DIST, this);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動產生的方法 Stub
		// Log.d("mytag","onProviderEnabled");
		// getLocationProvider();
		// mgr.requestLocationUpdates(MainActivity.bestProvider, MIN_TIME,
		// MIN_DIST, this);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動產生的方法 Stub
		// Log.d("mytag","onStatusChanged");
		// getLocationProvider();
		// mgr.requestLocationUpdates(MainActivity.bestProvider, MIN_TIME,
		// MIN_DIST, this);

	}

	void showToast(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}

	private boolean haveInternet() {
		boolean result = false;
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			result = false;
		} else {
			if (!info.isConnected()) {
				result = false;
			} else {
				result = true;
			}
		}
		isInternetEnable = result;
		return result;
	}

	void checkProvider() {
		mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		isGpsEnabled = mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnable = mgr
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	}

	void showNetworkSetup() {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("System");
		dialog.setMessage("Setting");
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);

		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		dialog.setNeutralButton("Network", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		});

		dialog.setPositiveButton("Locating Way", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				startActivity(new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});

		dialog.show();

	}
	
	void showSaveTableDialog(){
		LayoutInflater inflater = LayoutInflater.from(this);        
		View alert_view = inflater.inflate(R.layout.alert_save_view,null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		etxRecordName = (EditText)alert_view.findViewById(R.id.etxRecordName);
		dialog.setTitle("System");
		dialog.setMessage("Record Description：");
		dialog.setView(alert_view);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);

		dialog.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dbHelper.deleteInvalidTable();
				 Intent intent = new Intent(MainActivity.this,
				 TimerService.class);
				 stopService(intent);
				 finish();
			}
		});

		dialog.setNeutralButton("BACK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});

		dialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final MyValues myapp = (MyValues) mContext
						.getApplicationContext();
				String DESC = etxRecordName.getText().toString();
				dbHelper.updateIndexTable (myapp.activeTableName,DESC);

			}
		});

		dialog.show();
		
		
		
		
	}

	public double GetDistance(LatLng gp1, LatLng gp2) {
		double earthRadius = 3958.75;
		double latDiff = Math.toRadians(gp2.latitude - gp1.latitude);
		double lngDiff = Math.toRadians(gp2.longitude - gp1.longitude);
		double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
				+ Math.cos(Math.toRadians(gp1.latitude))
				* Math.cos(Math.toRadians(gp2.latitude))
				* Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;
		int meterConversion = 1609;
		return new Float(distance * meterConversion).floatValue();

	}

}
