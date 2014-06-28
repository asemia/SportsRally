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
		// TODO �۰ʲ��ͪ���k Stub
		super.handleMessage(msg);
		i = 0;
		dbHelper = new DBhelper(this.mActivity);
		Location nowLocation,tmpLocation;
		LatLng nowPoint,tmpPoint;

		final MyValues myapp = (MyValues) mActivity.getApplicationContext();
		
		//��s���
		String s = msg.getData().getString("spentTime");
		if (s != null) {
			this.mActivity.txtSpentTime.setText(s);
		}
        //�M��GPS
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
			
			//�N�s��GPS��m�g�J��Ʈw
			long rowId = dbHelper.insertLatLng(nowLocation.getLatitude(),
			nowLocation.getLongitude(), myapp.activeTableName);
			
			//�e�u
			if(myapp.tmpPoint!=null){
			Polyline line = this.mActivity.gmap
					.addPolyline(new PolylineOptions()
							.add(myapp.tmpPoint, nowPoint)
							.width(6).color(MyValues.color));
			
			myapp.distance += GetDistance(myapp.tmpPoint,nowPoint);
			this.mActivity.txtTotalDistance.setText(String.format("%f", myapp.distance));
						
			}

			//�w�g�J���I���m�O����tmp���ѤU�����
			myapp.tmpLocation = nowLocation;
			myapp.tmpPoint = new LatLng(nowLocation.getLatitude(),
					nowLocation.getLongitude());
		}
		
		}
		//��s�����t��
		double avgspeed = myapp.distance
				/ Double.parseDouble(msg.getData().getString("spentTime"));
		this.mActivity.txtAvgSpeed.setText(String.format("%f", avgspeed));

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
