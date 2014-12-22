package edu.ncc.damuzikdb;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract.Document;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	// private static String TAG = "DEPT";
	private DaMuzikSource datasource;
	private ArrayAdapter<MusicEntry> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		datasource = new DaMuzikSource(this);
		datasource.open();
		
		// for tablet
		/*addSong(new File("/storage/extSdcard/Music/forest.mp3"));
		addSong(new File("/storage/extSdcard/Music/title.mp3"));
		addSong(new File("/storage/extSdcard/Music/zelda.mp3"));
		addSong(new File("/storage/extSdcard/Music/ranch.mp3"));
		addSong(new File("/storage/extSdcard/Music/woods.mp3"));*/
		
		addSong("/storage/sdcard1/Music/forest.mp3");
		/*addSong(new File("/storage/sdcard1/Music/title.mp3"));
		addSong(new File("/storage/sdcard1/Music/zelda.mp3"));
		addSong(new File("/storage/sdcard1/Music/ranch.mp3"));
		addSong(new File("/storage/sdcard1/Music/woods.mp3"));*/

		
		

		// retrieve all the departments from the database
		List<MusicEntry> values = datasource.getAllMusic();
		adapter = new ArrayAdapter<MusicEntry>(this,
				android.R.layout.simple_list_item_1, values);

		// add departments to the database if it is currently empty
		if (adapter.getCount() == 0) {
			new ParseURL().execute();
		}

	}

	private MusicEntry getSongEntry(String way) {
		String[] info = new String[3];
		MusicEntry se = new MusicEntry();

		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(way);
		
		info[0] = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		info[1] = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		info[2] = way;

		se = new MusicEntry(info[0], info[1], info[2]);

		return se;
	}

	private void addSong(String way) {
		MusicEntry se = getSongEntry(way);
		datasource.addMusic(se.getName(), se.getArtist(), se.getPathWay());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<MusicEntry> adapter = (ArrayAdapter<MusicEntry>) getListAdapter();
		MusicEntry mus = null;
		List<MusicEntry> values;
		String artist = new String();

		switch (view.getId()) {

		// case R.id.delete:
		// if (getListAdapter().getCount() > 0) {
		// dept = (DepartmentEntry) getListAdapter().getItem(0);
		// datasource.deleteDept(dept);
		// adapter.remove(dept);
		// }
		// break;
		case R.id.show:
			values = datasource.getAllMusic();

			adapter = new ArrayAdapter<MusicEntry>(this,
					android.R.layout.simple_list_item_1, values);
			setListAdapter(adapter);
			break;

		case R.id.artist:

			values = datasource.getMusicByArtist(artist);

			adapter = new ArrayAdapter<MusicEntry>(this,
					android.R.layout.simple_list_item_1, values);
			setListAdapter(adapter);
			break;
		}
		adapter.notifyDataSetChanged();
	}

	private class ParseURL extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String str;
			String deptName;
			String deptPhone;
			String deptLocation;
			Document doc;
			int count = 0;

			// try {
			// // connect to the webpage
			// doc =
			// Jsoup.connect("http://www.ncc.edu/contactus/deptdirectory.shtml").get();
			//
			// // find the body of the webpage
			// Elements tableEntries = doc.select("tbody");
			// for (Element e : tableEntries)
			// {
			// // look for a row in the table
			// Elements trs = e.getElementsByTag("tr");
			//
			// // for each element in the row (there are 5)
			// for (Element e2 : trs)
			// {
			// // get the table descriptor
			// Elements tds = e2.getElementsByTag("td");
			//
			// // ignore the first row
			// if (count > 0) {
			// // get the department name and remove the formatting tags
			// str = tds.get(0).toString();
			// int startPos = str.indexOf(">", 4);
			// int endPos = str.indexOf("a>");
			// if (endPos == -1)
			// {
			// startPos = 3;
			// endPos = str.indexOf("td>",4);
			// }
			// deptName = str.substring(startPos+1, endPos-2);
			//
			// // get the department phone number
			// str = tds.get(1).toString();
			// startPos = 4;
			// endPos = str.indexOf("td>",4);
			// deptPhone = str.substring(startPos, endPos-2);
			//
			// // get the department location
			// str = tds.get(4).toString();
			// endPos = str.indexOf("td>", 4);
			// deptLocation = str.substring(startPos, endPos-2);
			//
			// datasource.addDept(deptName, deptPhone, deptLocation);
			// }
			// count++;
			// }
			// }
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// gets called after the task has completed

		}
	}
}