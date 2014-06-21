package com.sportsrally;

import java.util.Currency;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class UIHandler extends Handler {

	private MainActivity mActivity;
	DBhelper dbHelper;
	int i;
	
		public UIHandler (MainActivity activity){
		super();
		this.mActivity = activity ;}

	@Override
	public void handleMessage(Message msg) {
		// TODO 自動產生的方法 Stub
		super.handleMessage(msg);
		i=0;
		dbHelper = new DBhelper(this.mActivity);
		
		final MyValues myapp = (MyValues)mActivity.getApplicationContext();
		String s =msg.getData().getString("spentTime");
		
		if(s!=null){this.mActivity.txtSpentTime.setText(s);}
		
		if(this.mActivity.bestProvider!="gps"){
		this.mActivity.bestProvider = this.mActivity.mgr.getBestProvider(new Criteria(), true);
		}
		
	  	if(myapp.lastPoint!=null&&myapp.nowPoint!=null){
	  		
//	  		if(myapp.idleCount(myapp.isMoving)){
//			myapp.distance += GetDistance(myapp.lastPoint,myapp.nowPoint);
//			myapp.isMoving = false;
//			} else {myapp.idleCount(false);}
 			  		
			double avgspeed = myapp.distance / Double.parseDouble(msg.getData().getString("spentTime"));
			this.mActivity.txtAvgSpeed.setText(String.format("%f", avgspeed));
			
			if(myapp.tmpPoint==null) myapp.tmpPoint=myapp.nowPoint;
			
			if(myapp.tmpPoint!=null&&myapp.nowPoint!=null){
				
				
				
			if(this.mActivity.bestProvider=="gps"){	
//				Toast.makeText(this.mActivity, this.mActivity.bestProvider, Toast.LENGTH_LONG).show();
			Polyline line = this.mActivity.gmap.addPolyline(new PolylineOptions()
		     .add(myapp.tmpPoint, myapp.nowPoint)
		     .width(5)
		     .color(Color.RED));
			myapp.tmpPoint=myapp.nowPoint;
			}
			
			} else {myapp.idleCount(myapp.isMoving);}}
		
//	  	Toast.makeText(this.mActivity,Integer.toString(myapp.idleCounter), Toast.LENGTH_LONG).show();  		
	  	 long rowId=100;
	  	if(myapp.nowPoint != null)
	  		
	  	{  
	  	  rowId = dbHelper.insertLatLng(myapp.nowPoint.latitude, myapp.nowPoint.longitude);
	  	 
	  	 Cursor cursor =dbHelper.queryLatLngCursor();
	  	 cursor.moveToLast();
	  	 String Lat = Double.toString(cursor.getDouble(1));
	  	 String Lng = Double.toString(cursor.getDouble(2));
	  	 String LatLng = Lat +" "+ Lng;
	  	 int i = cursor.getCount();
	  //	Toast.makeText(this.mActivity,LatLng, Toast.LENGTH_LONG).show();
	  	}
	  	
	  	
	  	}
	  	
	  	
	  	
	
	
	public double GetDistance(LatLng gp1,LatLng gp2)
	{ 
	 double earthRadius = 3958.75; 
	 double latDiff = Math.toRadians(gp2.latitude-gp1.latitude); 
	 double lngDiff = Math.toRadians(gp2.longitude-gp1.longitude); 
	 double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) + 
	 Math.cos(Math.toRadians(gp1.latitude)) * 
	Math.cos(Math.toRadians(gp2.latitude)) * 
	 Math.sin(lngDiff /2) * Math.sin(lngDiff /2); 
	 double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	 double distance = earthRadius * c; 
	 int meterConversion = 1609; 
	 return new Float(distance * meterConversion).floatValue(); 
	 
	} 
	
	
	
	
}
