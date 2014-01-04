package com.persil.droidrecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import com.persil.droidrecorder2.R;

public class PlayerView extends Activity{

	private static String fileName;

	public TextView songName,startTimeField,endTimeField;
	private MediaPlayer mediaPlayer;
	private double startTime = 0;
	private double finalTime = 0;
	private Handler mHandler = new Handler();;
	private SeekBar seekbar;
	private ImageButton playButton,pauseButton;
	private ShareActionProvider mShareActionProvider;
	private int oneTimeOnly = 0;
	final Context context = this;
	private String result;
	Intent shareIntent;
	AudioManager am;
	String basePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_view);
		basePath = Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder"+File.separator;
		fileName = getIntent().getExtras().getString("GetFileName");
		initialize();
		songName.setText(fileName);
		mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(basePath + fileName)));
		seekbar.setClickable(true);
		pauseButton.setEnabled(false);
		overrideSeekBArListner();
		initializeShareIntent();
		mediaPlayer.setOnCompletionListener (new OnCompletionListener()
		{

			@Override
			public void onCompletion(MediaPlayer arg0) {
				onStopThread();
				//mediaPlayer.seekTo(mediaPlayer.getDuration());
				pauseButton.setEnabled(false);
				playButton.setEnabled(true);
				startTime = 0;
				seekbar.setProgress((int)startTime);
				Log.w("stopable", "startTime=0");
				mediaPlayer.reset();
				try {
					mediaPlayer.setDataSource(basePath+fileName);
					mediaPlayer.prepare();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				onStopThread();
				am.abandonAudioFocus(afChangeListener);
				
				
			}
			
		}
		);
	}


	private void initialize()
	{
		songName = (TextView)findViewById(R.id.textView1);
		startTimeField =(TextView)findViewById(R.id.TextView1);
		seekbar = (SeekBar)findViewById(R.id.seekBar1);
		playButton = (ImageButton)findViewById(R.id.playButton);
		pauseButton = (ImageButton)findViewById(R.id.pauseButton);
		seekbar.setClickable(true);
		overrideSeekBArListner();
	}

	private void reinitialize()
	{
		initialize();
		songName.setText(fileName);
		finalTime = mediaPlayer.getDuration();
		seekbar.setMax((int) finalTime);
		boolean isPlaying = mediaPlayer.isPlaying();
		pauseButton.setEnabled(isPlaying);
		playButton.setEnabled(!isPlaying);
	}

	
	private void initializeShareIntent()
	{
		shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra("GetFileName",fileName);
		shareIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.fromFile(new File(basePath+fileName)));
		shareIntent.setType("audio/*");
	}

	// MEDIA PLAYER
	
	private void releasMediaPlayer()
	{
		onStopThread();
		mediaPlayer.stop();
		mediaPlayer.release();
		am.abandonAudioFocus(afChangeListener);
	}
	
	private void pauseMediaPlayer()
	{
		mediaPlayer.pause();
		pauseButton.setEnabled(false);
		playButton.setEnabled(true);
		am.abandonAudioFocus(afChangeListener);
	}
	
	public void play(View view){
		int result = am.requestAudioFocus(afChangeListener,
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			//am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);

			Toast.makeText(getApplicationContext(), "Playing sound", 
					Toast.LENGTH_SHORT).show();
			mediaPlayer.start();
			finalTime = mediaPlayer.getDuration();
			startTime = mediaPlayer.getCurrentPosition();
			if(oneTimeOnly == 0){
				seekbar.setMax((int) finalTime);
				oneTimeOnly = 1;
			} 
			updateSeekbar(startTime);
			onStartThread(100);
			pauseButton.setEnabled(true);
			playButton.setEnabled(false);
			// Start playback.
		}
		else 
			onBackPressed();
	}

	public void pause(View view){
		Toast.makeText(getApplicationContext(), "Pausing sound", 
				Toast.LENGTH_SHORT).show();

		mediaPlayer.pause();
		pauseButton.setEnabled(false);
		playButton.setEnabled(true);
		am.abandonAudioFocus(afChangeListener);
	}	

	// UPDATER
	
	private void onStopThread() {
		UpdateSongTime.stop();
		mHandler.removeCallbacks(UpdateSongTime);
	}

	private void onStartThread(long delayMillis) {
		mHandler.postDelayed(UpdateSongTime, delayMillis);
	}

	private StoppableRunnable UpdateSongTime = new StoppableRunnable() {
		public void stoppableRun() {
			startTime = mediaPlayer.getCurrentPosition();
			/*if (startTime >= mediaPlayer.getDuration())
			{
				mediaPlayer.seekTo(mediaPlayer.getDuration());
				pauseButton.setEnabled(false);
				playButton.setEnabled(true);
				startTime = 0;
				seekbar.setProgress((int)startTime);
				Log.w("stopable", "startTime=0");
				onStopThread();
				am.abandonAudioFocus(afChangeListener);
			}
			else{*/
				updateSeekbar(startTime);
				onStartThread(100);
			//}
		}
	};
	
	private void updateSeekbar(double startTime)
	{
		startTimeField.setText(String.format("%d:%d", 
				TimeUnit.MILLISECONDS.toMinutes((long) startTime),
				TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) startTime)))
				);
		seekbar.setProgress((int)startTime);
	}

	// FILE ACTION
	
	private void actionRename(String newName)
	{
		File oldPath = new File(basePath);
		String extension = fileName.substring(fileName.lastIndexOf('.'));
		File from = new File(oldPath,fileName);
		File to = new File(oldPath,newName+extension);
		if (from.renameTo(to))
		{
			fileName = newName + extension;
			reinitialize();
			shareIntent.putExtra(Intent.EXTRA_STREAM,
					Uri.fromFile(new File(oldPath+fileName)));
			setShareIntent(shareIntent);
		}
	}

	private void showRename()
	{
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.prompt, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
		userInput.setText(fileName.substring(0, fileName.lastIndexOf('.')));
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				result = userInput.getText().toString();
				actionRename(result);
			}
		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void actionDelete()
	{
		File file = new File(basePath+fileName);
		file.delete();
		onBackPressed();
	}

	private void actionCopy()
	{
		File sourceLocation = new File(basePath+fileName);
		File destSound = new File(Environment.getExternalStorageDirectory()+File.separator+"media"+File.separator+"ringtones"+File.separator+fileName);

		try {
			copyFile(sourceLocation, destSound);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setMediaDB(File targetLocation)
	{
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, targetLocation.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, fileName);
		values.put(MediaStore.MediaColumns.SIZE, 215454);
		values.put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(targetLocation.getAbsolutePath()));
		values.put(MediaStore.Audio.Media.ARTIST, "droidRecorder");
		values.put(MediaStore.Audio.Media.DURATION, mediaPlayer.getDuration());
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
		//Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(targetLocation.getAbsolutePath());
		this.getContentResolver().insert(uri, values);
	}

	private void copyFile(File sourceLocation, File targetLocation)
			throws IOException {
		File directory = targetLocation.getParentFile();
		if (directory != null && !directory.exists() && !directory.mkdirs()) {
			throw new IOException("Cannot create dir " + directory.getAbsolutePath());
		}
		InputStream in = new FileInputStream(sourceLocation);
		OutputStream out = new FileOutputStream(targetLocation);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		setMediaDB(targetLocation);
	}
	
	private static String getMimeType(String url)
	{
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	// LISTENENER

	@SuppressLint("NewApi") @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.player_view, menu);
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareIntent(shareIntent);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_rename:
			showRename();
			return true;
		case R.id.action_delete:
			actionDelete();
			return true;
		case R.id.action_copy:
			actionCopy();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
			setContentView(R.layout.activity_player_view);
			Log.e("On Config Change","LANDSCAPE");
		}else{
			setContentView(R.layout.activity_player_view);
			Log.e("On Config Change","PORTRAIT");
		}
		reinitialize();
	}

	@Override
	public void onBackPressed() {
		releasMediaPlayer();
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@SuppressLint("NewApi") @Override
	public boolean	onNavigateUp() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			releasMediaPlayer();
			boolean ret = super.onNavigateUp();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return ret;
		}
		return false;
	}


	private void overrideSeekBArListner()
	{
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				int progress = arg0.getProgress();
				mediaPlayer.seekTo(progress);
				startTime = progress;
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}
		});
	}
	
	OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT 
					|| focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				pauseMediaPlayer();
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
					mediaPlayer.start();
					pauseButton.setEnabled(true);
					playButton.setEnabled(false);
			}
		}
	};

}