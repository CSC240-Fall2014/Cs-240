package com.rakesh.voicerecognitionexample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class VoiceRecognitionActivity extends Activity  {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

	private EditText metTextHint;
	private TextView Choice, Stats, In1, In2;
	private Spinner msTextMatches;
	private Button bPause;
	private ImageButton mbtSpeak;
	private ToggleButton tShuff;
	private String[] cList;
	private boolean shuf;
	private int song;
	private ArrayList<MusicEntry> ID = new ArrayList<MusicEntry>();
	private ArrayList<MusicEntry> Origin = new ArrayList<MusicEntry>();
	private int special;
	private MediaPlayer vMP = new MediaPlayer();;
	private String rPath = Environment.getExternalStorageDirectory() + File.separator + "Voice Songs" + File.separator;
	private int vol;
	private SeekBar seek;
	private DaMuzikSource database;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_voice_recognition);
		File directory = new File(Environment.getExternalStorageDirectory()  + File.separator + "Voice Songs");
		boolean test = directory.mkdirs();
		//String rPath = Environment.getExternalStorageDirectory() + File.separator + "Voice Songs" + File.separator;
		seek = (SeekBar)findViewById(R.id.seek);
		database = new DaMuzikSource(this);
		
		database.open();
		addSong(rPath + "Cannon.mp3");
		addSong(rPath + "Jazz.mp3");
		addSong(rPath + "Presto.mp3");
		addSong(rPath + "Spring.mp3");
		
		//Toast.makeText(getApplicationContext(), test + "" , Toast.LENGTH_LONG).show();
		Choice = (TextView)findViewById(R.id.choice);
		Stats = (TextView)findViewById(R.id.stat);
		
		tShuff = (ToggleButton)findViewById(R.id.tShuff);
		In1 = (TextView)findViewById(R.id.in1);
		In2 = (TextView)findViewById(R.id.in2);
		//msTextMatches = (Spinner) findViewById(R.id.sNoOfMatches);
		mbtSpeak = (ImageButton) findViewById(R.id.btSpeak);
		bPause = (Button) findViewById(R.id.bPause);
		 
		 
		 
		
		//ID = (ArrayList<MusicEntry>) database.getAllMusic();
		
		Origin = ID;
		
		
		song = 0;
		special = 0;
		vol = 4; 
		bPause.setEnabled(false);
		
		vMP.setOnCompletionListener(new OnCompletionListener()
		{

			@Override
			public void onCompletion(MediaPlayer vMP) 
			{
				onFin();
				
			}
			
		});
		
		setSong();
		seek.setOnTouchListener(new OnTouchListener() {@Override public boolean onTouch(View v, MotionEvent event) {
        	seekChange(v);
			return false; }
		});
		
		shuf = false;
	}
	public int getID()
	{
		return (int) ID.get(song).getId();
	}
	public void seekChange(View v)
	{
		if(vMP.isPlaying())
		{
			SeekBar temp = (SeekBar)v;
			vMP.seekTo(temp.getProgress());
			
		}
	}
	public void update()	
	{
		Handler handler = new Handler();
		seek.setProgress(vMP.getCurrentPosition());
		if (vMP.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	update();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}else{
    		vMP.pause();
    		
    		seek.setProgress(0);
    	}

	}
	@Override
	public void onDestroy()
	{
		vMP.release();
	}
	 // A private method to help us initialize our variables.
	
	public void setSong()
	{
		vMP.reset();
		try {
			vMP.setDataSource(ID.get(song).getPathWay());
			update();
			
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			vMP.prepare();
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	public void onFin()
	{
		song = (song + 1) % ID.size();
		Choice.setText(ID.get(song).toString());
		
		setSong();
		vMP.start();
		
		//use the next indexed ID to retrieve next source
		//Set source to new Song
	}
	public void onPlay(View v)
	{
		Stats.setText("Play");
		Choice.setText(ID.get(song).getName());
		setSong();
		vMP.start();
		bPause.setEnabled(true);
	}
	public void onStop(View v)
	{

		Stats.setText("Stop");
		Choice.setText("");
		vMP.stop();
		bPause.setEnabled(false);
	}
	public void onPause(View v)
	{
		Stats.setText("Pause");
		vMP.pause();
	}
	public void onLast(View v)
	{
		if(song != 0)
		{
			song --;
			
			Choice.setText(ID.get(song).getName());
			setSong();
			vMP.start();
			
		}
	}
	public void onNext(View v)
	{
		onFin();
		
	}
	public void onShuff(View v)
	{
		if(shuf)
		{
			ID = Origin;
			
			song = 0;
			Stats.setText("Play");
			Choice.setText(ID.get(song).getName());
		}
		else
		{
			Collections.shuffle(ID);
			song = 0;
			Choice.setText(ID.get(song).getName());
			Stats.setText("Play");
			
		}
		shuf = !shuf;
		setSong();
		vMP.start();
		
	}

	public void checkVoiceRecognition() {
		// Check if voice recognition is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			mbtSpeak.setEnabled(false);
			Toast.makeText(this, "Voice recognizer not present",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void speak(View view) 
	{
		vRecognition();
	}
	public void vRecognition()
	{
		String[] Prompts = {"Please Say A Command", "Please Name An Artist"};
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		// Display an hint to the user about what he should say.
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, Prompts[special]);

		// Given an hint to the recognizer about what the user is going to say
		//intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
			//	RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

		// If number of Matches is not selected then return show toast message
		//if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
		//	Toast.makeText(this, "Please select No. of Matches from spinner",
		//			Toast.LENGTH_SHORT).show();
		//	return;
		//}

		//int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
			//	.toString());
		// Specify how many results you want to receive. The results will be
		// sorted where the first result is the one with higher confidence.

		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				String Rec;
				if(In1.getText().equals(""))
				{
					ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					
					Rec = textMatchList.get(0);
					
				}
				else
				{
					Rec = In1.getText().toString();
				}
				Rec.toLowerCase();
				
				if(special == 1)
				{
					if(In2.getText() != "")
					{
						Rec = In2.getText().toString();
					}
					
					Choice.setText(Rec);
					special = 0;
					ID = (ArrayList<MusicEntry>) database.getMusicByArtist(Rec);
					Origin = ID;
					//Insert a Query to get all the entries of a specific artist and then add their ID to an arraylist for Now Playing
				}

				//If Voice recognition is successful then it returns RESULT_OK
				else if(special == 0) 
				{

					
					
					if(Rec.contains("quit")|| Rec.contains("exit"))
					{
						System.exit(0);
					}
					else if(Rec.contains("test"))
					{
						Choice.setText("Test");
					}
					else if(Rec.contains("artist"))
					{
						special = 1;
						vRecognition();
						
					}
					else if(Rec.contains("up") || Rec.contains("increase"))
					{
						if (vol < 10)
						{
							vol += 2;
							float log1=(float)(Math.log(10-vol)/Math.log(10));
							vMP.setVolume(1-log1, 1-log1);
						}
						
					}
					else if(Rec.contains("down") || Rec.contains("decrease"))
					{
						if(vol >= 2)
						{
							vol -= 2;
							vol += 2;
							float log1=(float)(Math.log(10-vol)/Math.log(10));
							vMP.setVolume(1-log1, 1-log1);
						}
					}
					else if(Rec.contains("play"))
					{
						Stats.setText("Play");
						Choice.setText(ID.get(song).getName());
						setSong();
						vMP.start();
						bPause.setEnabled(true);
					}
					else if(Rec.contains("pause"))
					{
						Stats.setText("Pause");
						vMP.pause();
					}
					else if(Rec.contains("stop"))
					{
						Stats.setText("Stop");
						Choice.setText("");
						vMP.stop();
						bPause.setEnabled(false);
						
					}
					else if(Rec.contains("next"))
					{
						onFin();
						
					}
					else if((Rec.contains("last") || Rec.contains("previous")))
					{
						if(song != 0)
						{
							song --;
							
							Choice.setText(ID.get(song).getName());
							setSong();
							vMP.start();
						}
						
					}
					
					else if(Rec.contains("shuffle") || Rec.contains("random"))
					{
						if(shuf)
						{
							ID = Origin;
							song = 0;
							Stats.setText("Play");
							tShuff.setChecked(false);
							Choice.setText(ID.get(song).getName());
						}
						else
						{
							Collections.shuffle(ID);
							song = 0;
							Choice.setText(ID.get(song).getName());
							Stats.setText("Play");
							
							tShuff.setChecked(true);
						}
						shuf = !shuf;
						setSong();
						vMP.start();
						
					}
					else
					{
						Choice.setText("ERROR");
					}

				}
			}
			
			
			
			//Result code for various error.	
			else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
				showToastMessage("Audio Error");
			}else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
				showToastMessage("Client Error");
			}else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
				showToastMessage("Network Error");
			}else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
				showToastMessage("No Match");
			}else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
				showToastMessage("Server Error");
			}
			else 
			{
				
			}
			
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	void showToastMessage(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
		database.addMusic(se.getName(), se.getArtist(), se.getPathWay());
	}
}
