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
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MapForPause extends Activity {
	
	Button btn_back,btn_deleteLatLngTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_for_pause);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_deleteLatLngTable = (Button) findViewById(R.id.btn_deleteLatLngTable);
		
		ArrayList<LatLng> list = null;
		DBhelper dBhelper = new DBhelper(this);
		
		list = dBhelper.getAllFromLatLngTable();
		GoogleMap gmap;
		gmap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.pauseMap)).getMap();
		int i = 0;
		
		if(list!=null){
			
		
		if(list.size()>1){
			int p = list.size();
			Toast.makeText(getApplicationContext(), Integer.toString(p), Toast.LENGTH_LONG).show();
			for(int x=1;x<list.size();x++){
		Polyline line = gmap.addPolyline(new PolylineOptions()
	     .add(list.get(x-1), list.get(x))
	     .width(5)
	     .color(Color.RED));
		gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 17));
			}
		}
		
		}
		
		btn_back.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO �۰ʲ��ͪ���k Stub
				finish();
			}
			
		});
		
		btn_deleteLatLngTable.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO �۰ʲ��ͪ���k Stub
				DBhelper dBhelper = new DBhelper(MapForPause.this);
				dBhelper.deleteLatLngTable();
				finish();
			}
			
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_for_pause, menu);
		return true;
	}

}