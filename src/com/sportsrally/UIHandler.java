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

	public UIHandler(MainActivity activity) {
		super();
		this.mActivity = activity;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO 自動產生的方法 Stub
		super.handleMessage(msg);
		i = 0;
		dbHelper = new DBhelper(this.mActivity);
<<<<<<< HEAD
		Location nowLocation,tmpLocation;
		LatLng nowPoint,tmpPoint;

		final MyValues myapp = (MyValues) mActivity.getApplicationContext();
		
		//更新秒數
		String s = msg.getData().getString("spentTime");
		if (s != null) {
			this.mActivity.txtSpentTime.setText(s);
		}
        //尋找GPS
		if (this.mActivity.bestProvider != "gps") {
			this.mActivity.bestProvider = this.mActivity.mgr.getBestProvider(
					new Criteria(), true);
		}

		nowLocation = myapp.nowLocation;
		tmpLocation = myapp.tmpLocation;
		
		if(nowLocation!=null&&nowLocation.getProvider().equals("gps")){
			
		if (tmpLocation!=nowLocation){
			
			nowPoint = new LatLng(nowLocation.getLatitude(),
					nowLocation.getLongitude());
			
			//將新的GPS位置寫入資料庫
			long rowId = dbHelper.insertLatLng(nowLocation.getLatitude(),
			nowLocation.getLongitude(), myapp.activeTableName);
			
			//畫線
			if(myapp.tmpPoint!=null){
			Polyline line = this.mActivity.gmap
					.addPolyline(new PolylineOptions()
							.add(myapp.tmpPoint, nowPoint)
							.width(6).color(MyValues.color));
			
			myapp.distance += GetDistance(myapp.tmpPoint,nowPoint);
			this.mActivity.txtTotalDistance.setText(String.format("%f", myapp.distance));
						
			}

			//已寫入的點跟位置記錄到tmp提供下次比對
			myapp.tmpLocation = nowLocation;
			myapp.tmpPoint = new LatLng(nowLocation.getLatitude(),
					nowLocation.getLongitude());
		}
		
		}
		//更新平均速度
		double avgspeed = myapp.distance
				/ Double.parseDouble(msg.getData().getString("spentTime"));
		this.mActivity.txtAvgSpeed.setText(String.format("%f", avgspeed));
=======

		final MyValues myapp = (MyValues) mActivity.getApplicationContext();
		String s = msg.getData().getString("spentTime");

		if (s != null) {
			this.mActivity.txtSpentTime.setText(s);
		}

		if (this.mActivity.bestProvider != "gps") {
			this.mActivity.bestProvider = this.mActivity.mgr.getBestProvider(
					new Criteria(), true);
		}

		if (myapp.lastPoint != null && myapp.nowPoint != null) {

			double avgspeed = myapp.distance
					/ Double.parseDouble(msg.getData().getString("spentTime"));
			this.mActivity.txtAvgSpeed.setText(String.format("%f", avgspeed));

			if (myapp.tmpPoint == null)
				myapp.tmpPoint = myapp.nowPoint;

			if (myapp.tmpPoint != null && myapp.nowPoint != null) {

				// if(this.mActivity.bestProvider=="gps"){

				// if(myapp.nowLocation.getProvider().equals("gps")){
				if (myapp.lastLocation.getProvider().equals("gps")
						&& myapp.nowLocation.getProvider().equals("gps")) {
					Polyline line = this.mActivity.gmap
							.addPolyline(new PolylineOptions()
									.add(myapp.tmpPoint, myapp.nowPoint)
									.width(6).color(Color.BLUE));
					myapp.tmpPoint = myapp.nowPoint;
				}

			} else {
				myapp.idleCount(myapp.isMoving);
			}
		}

		long rowId = 100;
		if (myapp.nowPoint != null
				&& myapp.nowLocation.getProvider().equals("gps"))

		{

			rowId = dbHelper.insertLatLng(myapp.nowPoint.latitude,
					myapp.nowPoint.longitude, myapp.activeTableName);
			Cursor cursor = dbHelper.queryLatLngCursor();
			cursor.moveToLast();
			String Lat = Double.toString(cursor.getDouble(1));
			String Lng = Double.toString(cursor.getDouble(2));
			String LatLng = Lat + " " + Lng;
			int i = cursor.getCount();

		}
>>>>>>> e4e540d2b39c7d7cb1dfa036904977a088b4e5c9

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
