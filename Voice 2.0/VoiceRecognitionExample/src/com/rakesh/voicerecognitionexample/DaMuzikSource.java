package com.rakesh.voicerecognitionexample;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DaMuzikSource {
	// Database fields
	private SQLiteDatabase database;
	private DaMuzikHelper musicHelper;
	private static String TAG = "MUS";
	private static String ORDER_BY = DaMuzikHelper.ARTIST;
	
	private String[] allColumns = { DaMuzikHelper._ID, DaMuzikHelper.NAME, DaMuzikHelper.ARTIST, 
			DaMuzikHelper.PATHWAY, };

	public DaMuzikSource(Context context) {
		musicHelper = new DaMuzikHelper(context);
	}
	
	public void open() throws SQLException {
		database = musicHelper.getWritableDatabase();
	}
	
	public void close() {
		musicHelper.close();
	}
	
	public boolean addMusic(String name, String artist, String pathWay)
	{
		ContentValues values = new ContentValues();
		values.put(DaMuzikHelper.NAME, name);
		values.put(DaMuzikHelper.PATHWAY, pathWay);
		values.put(DaMuzikHelper.ARTIST, artist);
		long insertId = database.insert(DaMuzikHelper.TABLE_NAME, null, values);
		if (insertId == -1)
			return false;
		else
			return true;
	}
	
	public void deleteMusic(MusicEntry music) {
		long id = music.getId();
		Log.d(TAG, "Music deleted with id: " + id);
		database.delete(DaMuzikHelper.TABLE_NAME, DaMuzikHelper._ID
				+ " = " + id, null);
	}
	
	public List<MusicEntry> getAllMusic() {
		List<MusicEntry> mus = new ArrayList<MusicEntry>();
		MusicEntry entry;
		Cursor cursor = database.query(DaMuzikHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			entry = cursorToEntry(cursor);
			mus.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return mus;
	}
	
	public List<MusicEntry> getMusicByArtist(String artist) {
		List<MusicEntry> mus = new ArrayList<MusicEntry>();
		MusicEntry entry;

		Cursor cursor = database.query(DaMuzikHelper.TABLE_NAME,
				allColumns, DaMuzikHelper.NAME + " LIKE ? ", new String[] { "%"+artist+"%",}, null, null, ORDER_BY);

		//database.query(DepartmentInfoHelper.TABLE_NAME,
				//allColumns, DepartmentInfoHelper.LOCATION + " LIKE ? OR " + DepartmentInfoHelper.LOCATION + " LIKE ? ", new String[] { "%Cluster%", "%Building%" }, null, null, ORDER_BY);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			entry = cursorToEntry(cursor);
			mus.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return mus;
	}
	
	public List<MusicEntry> queryMusicByGenre(String genre) {
		List<MusicEntry> mus = new ArrayList<MusicEntry>();
		MusicEntry entry;

		Cursor cursor = database.query(DaMuzikHelper.TABLE_NAME,
				allColumns, DaMuzikHelper.NAME + " LIKE ? ", new String[] { "%"+genre+"%",}, null, null, ORDER_BY);

		//database.query(DepartmentInfoHelper.TABLE_NAME,
				//allColumns, DepartmentInfoHelper.LOCATION + " LIKE ? OR " + DepartmentInfoHelper.LOCATION + " LIKE ? ", new String[] { "%Cluster%", "%Building%" }, null, null, ORDER_BY);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			entry = cursorToEntry(cursor);
			mus.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return mus;
	}
	
	private MusicEntry cursorToEntry(Cursor cursor) {
		MusicEntry entry = new MusicEntry();
		entry.setId(cursor.getLong(0));
		entry.setName(cursor.getString(1));
		entry.setArtist(cursor.getString(2));
		entry.setpathWay(cursor.getString(3));
		return entry;
	}
}
