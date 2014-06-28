package com.sportsrally;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
import android.widget.Toast;

public class DBhelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sportsRallyDB ";
	private static final int DATABASE_VER = 1;
	static final String INDEX_TABLE_NAME = "indexTable";
	private static final String LATLNG_TABLE_NAME = "latLngTable";
	private static final String INDEX_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ INDEX_TABLE_NAME
			+ " ("
			+ "_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "TNAME TEXT NOT NULL," + "DESC TEXT" + ");";
	private static final String LATLNG_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ LATLNG_TABLE_NAME
			+ " ("
			+ "_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "LAT REAL NOT NULL,"
			+ "LNG REAL NOT NULL" + ");";
	static int indexTableSize;
	Context context;

	public DBhelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VER);
		// TODO 自動產生的建構子 Stub

		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動產生的方法 Stub
		db.execSQL(INDEX_TABLE_CREATE);
		db.execSQL(LATLNG_TABLE_CREATE);
		Log.d("mytag", "Table Create");
	}

	public long insertLatLng(double lat, double lng) {

		SQLiteDatabase db = getWritableDatabase();
		// String sql = "CREATE TABLE IF NOT EXISTS " + "A" + " (" + "B"
		// + " text not null, " + "C" + " text not null " + ");";
		db.execSQL(LATLNG_TABLE_CREATE);
		ContentValues cv = new ContentValues();
		cv.put("LAT", lat);
		cv.put("LNG", lng);
		long rowId = db.insert(LATLNG_TABLE_NAME, null, cv);
		// db.close();
		return rowId;

	}

	public Cursor queryLatLngCursor() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(LATLNG_TABLE_NAME, null, null, null, null,
				null, null);
		// db.close();
		return cursor;
	}

	public ArrayList<LatLng> getAllFromLatLngTable() {

		SQLiteDatabase db = getReadableDatabase();
		ArrayList<LatLng> list = new ArrayList<LatLng>();
		Cursor cursor = null;
		cursor = db
				.query(LATLNG_TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null) {

			while (cursor.moveToNext()) {
				double x = cursor.getDouble(1);
				double y = cursor.getDouble(2);
				LatLng point = new LatLng(x, y);
				list.add(point);
			}

		}
		// db.close();
		return list;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動產生的方法 Stub

	}

	public void deleteLatLngTable() {
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + LATLNG_TABLE_NAME;
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

	public void deleteLatLngTable(String tableName) {
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

	void createTable(String tablename) {
		SQLiteDatabase db = getWritableDatabase();
		String LATLNG_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + tablename
				+ " (" + "_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "LAT REAL NOT NULL," + "LNG REAL NOT NULL" + ");";
		db.execSQL(LATLNG_TABLE_CREATE);
		insertIndexTable(tablename);
		// db.close();

	}

	public long insertLatLng(double lat, double lng, String tablename) {

		SQLiteDatabase db = getWritableDatabase();
		String LATLNG_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + tablename
				+ " (" + "_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "LAT REAL NOT NULL," + "LNG REAL NOT NULL" + ");";
		db.execSQL(LATLNG_TABLE_CREATE);
		ContentValues cv = new ContentValues();
		cv.put("LAT", lat);
		cv.put("LNG", lng);
		long rowId = db.insert(tablename, null, cv);
		// db.close();
		return rowId;

	}

	public long insertIndexTable(String tablename) {
		// db.execSQL("DROP TABLE IF EXISTS indexTable");
		SQLiteDatabase db = getWritableDatabase();
		
		String INDEX_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ INDEX_TABLE_NAME + " ("
				+ "_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "TNAME TEXT NOT NULL," + "DESC TEXT" + ");";
		db.execSQL(INDEX_TABLE_CREATE);
		ContentValues cv = new ContentValues();

		cv.put("TNAME", tablename);
		cv.put("DESC", "");
		long rowId = db.insert(INDEX_TABLE_NAME, null, cv);
		if (rowId > 0)
			showToast("insertIndex OK");
		// db.close();
		return rowId;

	}

	public Cursor queryLatLngCursor(String tablename) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(tablename, null, null, null, null, null, null);
		// db.close();
		return cursor;
	}

	public void updateIndexTable(String recordTableName, String Description) {
		SQLiteDatabase db = getWritableDatabase();

		Cursor cursor = db.query(INDEX_TABLE_NAME, new String[] { "_ID",
				"TNAME", "DESC" }, "TNAME=?", new String[] { recordTableName },
				null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			String id = cursor.getColumnName(0);
			ContentValues cv = new ContentValues();
			cv.put("DESC", Description);
			long rowId = db.update(INDEX_TABLE_NAME, cv, "_ID=" + id, null);
			cursor = db.query(INDEX_TABLE_NAME, new String[] { "_ID", "TNAME",
					"DESC" }, "TNAME=?", new String[] { recordTableName },
					null, null, null);
			cursor.moveToFirst();
			String a = cursor.getString(0);
			String b = cursor.getString(1);
			String c = cursor.getString(2);
			if (!c.equals("")) {
				String d = "ID " + a + " TNAME " + b + " DESC " + c + " Saved";
				if (rowId > 0)
					showToast(d);
			} else {
				showToast("Invalid Name");
			}
		}

	}

	public void deleteInvalidTable() {

		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(INDEX_TABLE_NAME, new String[] { "_ID",
				"TNAME", "DESC" }, "DESC=?", new String[] { "" }, null, null,
				null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(0);
				String tablename = cursor.getColumnName(1);
				db.execSQL("DROP TABLE IF EXISTS " + tablename);
				db.delete("indexTable", "_ID="+id, null);
				showToast("ID:"+Integer.toString(id)+"is Deleted");
				}
			}
		}

	

	public ArrayList<LatLng> getAllFromLatLngTable(String tablename) {

		SQLiteDatabase db = getReadableDatabase();
		ArrayList<LatLng> list = new ArrayList<LatLng>();
		Cursor cursor = null;
		cursor = db.query(tablename, null, null, null, null, null, null);
		if (cursor != null) {

			while (cursor.moveToNext()) {
				double x = cursor.getDouble(1);
				double y = cursor.getDouble(2);
				LatLng point = new LatLng(x, y);
				list.add(point);
			}

		}
		// db.close();
		return list;
	}

	public ArrayList<String> getAllFromIndexTable() {

		SQLiteDatabase db = getReadableDatabase();
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = null;
		cursor = db.query(INDEX_TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null) {

			while (cursor.moveToNext()) {
				String x = cursor.getString(1);
				// double y = cursor.getDouble(2);
				// LatLng point = new LatLng(x, y);
				list.add(x);
			}

		}
		// db.close();
		return list;
	}

	String getNewTableName() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
		String str = "A" + formatter.format(curDate);
		return str;

	}

	void showToast(String s) {
		Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_LONG)
				.show();
	}

}
