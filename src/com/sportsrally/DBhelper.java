package com.sportsrally;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sportsRallyDB ";
	private static final int DATABASE_VER = 1;
	private static final String INDEX_TABLE_NAME = "indexTable";
	private static final String LATLNG_TABLE_NAME = "latLngTable";
	private static final String INDEX_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ INDEX_TABLE_NAME + " (" + 
			"ID INTEGER PRIMARY KEY AUTOINCREMENT," + 
			"TIME TEXT NOT NULL,"+
			"DESC TEXT"+");";
	private static final String LATLNG_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ LATLNG_TABLE_NAME + " (" + 
			"ID INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			"LAT REAL NOT NULL,"+
			"LNG REAL NOT NULL"+");";
	
	  
	

	public DBhelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VER);
		// TODO 自動產生的建構子 Stub
		
	
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動產生的方法 Stub
    db.execSQL(INDEX_TABLE_CREATE);
	db.execSQL(LATLNG_TABLE_CREATE);	
		Log.d("mytag", "Table Create");
	}
	
	
	
	
	
	public long insertLatLng(double lat,double lng){
		
		SQLiteDatabase db = getWritableDatabase();
//		   String sql = "CREATE TABLE IF NOT EXISTS " + "A" + " (" + "B"
//			          + " text not null, " + "C" + " text not null " + ");";
		db.execSQL(LATLNG_TABLE_CREATE);
		ContentValues cv = new ContentValues();
		cv.put("LAT", lat);
		cv.put("LNG", lng);
		long rowId = db.insert(LATLNG_TABLE_NAME, null, cv);
		db.close();
		return rowId;
		
		
	}
	
	public Cursor queryLatLngCursor (){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(LATLNG_TABLE_NAME, null, null, null, null, null, null);
		
		return cursor;
	}
	
	public ArrayList<LatLng> getAllFromLatLngTable(){
		
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<LatLng> list = new ArrayList<LatLng>();
		Cursor cursor = null;
		cursor = db.query(LATLNG_TABLE_NAME, null, null, null, null, null, null);
		if(cursor!=null){
			
			while(cursor.moveToNext()){
				double x = cursor.getDouble(1);
				double y = cursor.getDouble(2);
				LatLng point = new LatLng(x, y);
				list.add(point);
			}
			
		}
		
		return list;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動產生的方法 Stub

	}
	
	public void deleteLatLngTable (){
		   final String DROP_TABLE = "DROP TABLE IF EXISTS " + LATLNG_TABLE_NAME;
		   SQLiteDatabase db = getWritableDatabase();
		   db.execSQL(DROP_TABLE);
		   onCreate(db);
	}
	

}


