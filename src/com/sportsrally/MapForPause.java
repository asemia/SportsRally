package com.sportsrally;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.R.array;
import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MapForPause extends Activity {

	Button btn_back, btn_deleteLatLngTable;
	Context context = this;
	ArrayList<LatLng> list = null;
	DBhelper dBhelper = new DBhelper(this);
	GoogleMap gmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_for_pause);

		btn_back = (Button) findViewById(R.id.btnMusic);
		btn_deleteLatLngTable = (Button) findViewById(R.id.btn_deleteLatLngTable);
		final MyValues myapp = (MyValues) context.getApplicationContext();
		btn_back.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				finish();
			}

		});

		btn_deleteLatLngTable.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				DBhelper dBhelper = new DBhelper(MapForPause.this);
				dBhelper.deleteLatLngTable(myapp.activeTableName);
				myapp.activeTableName = dBhelper.getNewTableName();
				dBhelper.createTable(myapp.activeTableName);
				myapp.spentSeconds = 0;
				myapp.distance = 0;
				myapp.idleCounter = 0;
				myapp.nowLocation = null;
				myapp.nowPoint = null;
				myapp.lastPoint = null;
				myapp.tmpPoint = null;
				myapp.isMoving = false;
				finish();
			}

		});

	}

	@Override
	protected void onResume() {
		// TODO 自動產生的方法 Stub
		super.onResume();

		final MyValues myapp = (MyValues) context.getApplicationContext();
		myapp.progress = 0;
		int i = 0;
		list = dBhelper.getAllFromLatLngTable(myapp.activeTableName);
		gmap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.pauseMap)).getMap();
		

		

		if (list != null) {
			
			if (list.size() > 1) {
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO 自動產生的方法 Stub
						new doAsyncTask(context).execute();
					}
				});
				
				int p = list.size();
				int progress = 0;
				Toast.makeText(getApplicationContext(),
						Integer.toString(p) + myapp.activeTableName,
						Toast.LENGTH_LONG).show();
				for (int x = 1; x < list.size(); x++) {
					progress = (int) ((x * 100.0) / p);
					myapp.progress = progress;
					Polyline line = gmap
							.addPolyline(new PolylineOptions()
									.add(list.get(x - 1), list.get(x))
									.width(5).color(Color.RED));
				
				}
				myapp.progress = 101;
				gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 15));	
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_for_pause, menu);
		return true;
	}

	void showToast(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}

}
