package com.persil.droidrecorder;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerView extends Activity implements OnSeekBarChangeListener {
	
		private static String fileName;
	
		public TextView songName,startTimeField,endTimeField;
		private MediaPlayer mediaPlayer;
		private double startTime = 0;
		private double finalTime = 0;
		private Handler mHandler = new Handler();;
		private SeekBar seekbar;
		private ImageButton playButton,pauseButton;
		private ShareActionProvider mShareActionProvider;
		public int oneTimeOnly = 0;
		final Context context = this;
		private String result;
		Intent shareIntent;
		
		@Override
	   	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_player_view);
	      fileName = getIntent().getExtras().getString("GetFileName");
	      songName = (TextView)findViewById(R.id.textView1);
	      startTimeField =(TextView)findViewById(R.id.TextView1);
	      //endTimeField =(TextView)findViewById(R.id.textView2);
	      seekbar = (SeekBar)findViewById(R.id.seekBar1);
	      playButton = (ImageButton)findViewById(R.id.playButton);
	      pauseButton = (ImageButton)findViewById(R.id.pauseButton);
	      songName.setText(fileName);
	      mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder"+File.separator+fileName)));
	      seekbar.setClickable(true);
	      pauseButton.setEnabled(false);
	      seekbar.setOnSeekBarChangeListener(this);
	      
	      shareIntent = new Intent();
	      shareIntent.setAction(Intent.ACTION_SEND);
	      shareIntent.putExtra("GetFileName",fileName);
	      shareIntent.putExtra(Intent.EXTRA_STREAM,
	    		  Uri.fromFile(new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder"+File.separator+fileName)));
	      shareIntent.setType("audio/*");
	   }

	   public static String getMimeType(String url)
	   {
	       String type = null;
	       String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	       if (extension != null) {
	           MimeTypeMap mime = MimeTypeMap.getSingleton();
	           type = mime.getMimeTypeFromExtension(extension);
	       }
	       return type;
	   }
	   
	   public void play(View view){
	   Toast.makeText(getApplicationContext(), "Playing sound", 
	   Toast.LENGTH_SHORT).show();
	      mediaPlayer.start();
	      finalTime = mediaPlayer.getDuration();
	      startTime = mediaPlayer.getCurrentPosition();
	      if(oneTimeOnly == 0){
	         seekbar.setMax((int) finalTime);
	         oneTimeOnly = 1;
	      } 

	      /*endTimeField.setText(String.format("%d min, %d sec", 
	         TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
	         TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - 
	         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
	         toMinutes((long) finalTime)))
	      );*/
	      startTimeField.setText(String.format("%d:%d", 
	         TimeUnit.MILLISECONDS.toMinutes((long) startTime),
	         TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
	         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
	         toMinutes((long) startTime)))
	      );
	      seekbar.setProgress((int)startTime);
	      onStartThread(100);
	      pauseButton.setEnabled(true);
	      playButton.setEnabled(false);
	   }

	   public void onStopThread() {
		   UpdateSongTime.stop();       
		    mHandler.removeCallbacks(UpdateSongTime);
		}

		public void onStartThread(long delayMillis) {
		    mHandler.postDelayed(UpdateSongTime, delayMillis);
		}

	   
	   private StoppableRunnable UpdateSongTime = new StoppableRunnable() {
		   public void stoppableRun() {
			   startTime = mediaPlayer.getCurrentPosition();
			   if (startTime >= mediaPlayer.getDuration())
			   {
				   mediaPlayer.seekTo(mediaPlayer.getDuration());
				   pauseButton.setEnabled(false);
				   playButton.setEnabled(true);
				   startTime = 0;
				   seekbar.setProgress((int)startTime);
				   Log.w("stopable", "startTime=0");
				   onStopThread();
			   }
			   else{
		       startTimeField.setText(String.format("%d:%d", 
		    		   TimeUnit.MILLISECONDS.toMinutes((long) startTime),
		    		   TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
		    		   TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
		    				   toMinutes((long) startTime))));
		       seekbar.setProgress((int)startTime);
			   onStartThread(100);
			   }
		   }
	   };
	   
	   private void reinitialize()
	   {
		   //fileName = getIntent().getExtras().getString("GetFileName");
	        songName = (TextView)findViewById(R.id.textView1);
		    startTimeField =(TextView)findViewById(R.id.TextView1);
		    //endTimeField =(TextView)findViewById(R.id.textView2);
		    seekbar = (SeekBar)findViewById(R.id.seekBar1);
		    playButton = (ImageButton)findViewById(R.id.playButton);
		    pauseButton = (ImageButton)findViewById(R.id.pauseButton);
		    songName.setText(fileName);
		    finalTime = mediaPlayer.getDuration();
		    seekbar.setMax((int) finalTime);
		    if (mediaPlayer.isPlaying())
		    {
			      pauseButton.setEnabled(true);
			      playButton.setEnabled(false);
		    }
		    else
		    {
			      pauseButton.setEnabled(false);
			      playButton.setEnabled(true);
		    }
	   }
	   
	   public void pause(View view){
	      Toast.makeText(getApplicationContext(), "Pausing sound", 
	      Toast.LENGTH_SHORT).show();

	      mediaPlayer.pause();
	      pauseButton.setEnabled(false);
	      playButton.setEnabled(true);
	   }	

	   private void actionRename(String newName)
	   {
		   File basePath = new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder"+File.separator);
		   String extension = fileName.substring(fileName.lastIndexOf('.'));
		   File from = new File(basePath,fileName);
		   File to = new File(basePath,newName+extension);
		   if (from.renameTo(to))
		   {
			   fileName = newName + extension;
			   reinitialize();
			   shareIntent.putExtra(Intent.EXTRA_STREAM,
			    		  Uri.fromFile(new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder"+File.separator+fileName)));
			   setShareIntent(shareIntent);
		   }
	   }
	   
	   private void showRename()
	   {
		// get prompts.xml view
			LayoutInflater li = LayoutInflater.from(context);
			View promptsView = li.inflate(R.layout.prompt, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

			// set prompts.xml to alert dialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
			userInput.setText(fileName.substring(0, fileName.lastIndexOf('.')));

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
					// get user input and set it to result
					// edit text
					result = userInput.getText().toString();
					Log.d("popup", userInput.getText().toString());
					actionRename(result);
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				    }
				  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
	   }
	   
	   private void actionDelete()
	   {
		   File file = new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder"+File.separator+fileName);
		   file.delete();
		   onBackPressed();
	   }
	   
	   @Override
	    public void onProgressChanged(SeekBar seekBar, int progress,
	    		boolean fromUser) {
	    	// TODO Auto-generated method stub
		   
	    }

	   @SuppressLint("NewApi") @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
		    // Inflate menu resource file.
		    getMenuInflater().inflate(R.menu.player_view, menu);

		    // Locate MenuItem with ShareActionProvider
		    MenuItem item = menu.findItem(R.id.menu_item_share);

		    // Fetch and store ShareActionProvider
		    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		      setShareIntent(shareIntent);
		    // Return true to display menu
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
	           default:
	               return super.onOptionsItemSelected(item);
	       }
	   }
	   
	   private void setShareIntent(Intent shareIntent) {
		    if (mShareActionProvider != null) {
		    	Log.w("share", "share action provider pas nul");
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
		   	onStopThread();
	    	mediaPlayer.pause();
	    	mediaPlayer.release();
	    	super.onBackPressed();
	        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	    }
		
	    @SuppressLint("NewApi") @Override
	    public boolean	onNavigateUp() {
	    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	    		onStopThread();
		    	mediaPlayer.pause();
		    	mediaPlayer.release();
		    	boolean ret = super.onNavigateUp();
		        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				return ret;
	    	}
	       return false;
	    }

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			int progress = arg0.getProgress();
			mediaPlayer.seekTo(progress);
			startTime = progress;
		}
	   
	 }
	/*String fileName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_view);
		fileName = getIntent().getExtras().getString("GetFileName");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_view, menu);
		return true;
	}
*/
