package com.sportsrally;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.R.integer;
import android.app.Application;
import android.location.Location;
import android.location.LocationListener;

public class MyValues extends Application {
	
	
	static long spentSeconds;
	static long startTime;
	static long endTime;
	static int idleCounter;
	static double lon,lat;
	static double distance;
	static boolean isMoving;
	static LatLng lastPoint,nowPoint,tmpPoint;
	static ArrayList<Double> myLon;
	static ArrayList<Double> myLat;
	static Location nowLocation;
	static Location lastLocation;
	static String activeTableName=null;
	static int progress;
	
static {idleCounter =0;isMoving=false;}

protected synchronized static boolean idleCount(boolean ismoving) 
{
if(ismoving) {idleCounter=0;isMoving=true;} else {idleCounter++;isMoving=false;}	
return ismoving;
}
	





	


}
