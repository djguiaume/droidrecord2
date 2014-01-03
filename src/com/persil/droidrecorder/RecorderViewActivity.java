package com.persil.droidrecorder;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import com.persil.droidrecorder2.R;

public class RecorderViewActivity extends Activity {

	//@SuppressLint("NewApi");
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder_view);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		ImageButton recordButton = (ImageButton) findViewById(R.id.recordButton);
		Boolean recordStarted = new Boolean(false);
		recordButton.setTag(recordStarted);
	}

    @Override
    public void onBackPressed() {
    	super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
	
    @SuppressLint("NewApi") @Override
    public boolean	onNavigateUp() {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	    	boolean ret = super.onNavigateUp();
	        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return ret;
    	}
       return false;
    }
    
    public void onRecordButtonClick(View view) {
    	Log.d("RecorderView", "recordButton clicked");
    	ImageButton button = (ImageButton) view;
    	Boolean recording = (Boolean) button.getTag();
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	
    	if (recording) {
    		//TODO: pause recording
    		recording = false;
    	}
    	else {
    		//TODO: start recording
    		recording = true;
    		Log.d("RecorderView", String.valueOf(getFormat()));
    	}

		button.setImageResource(
				recording ? R.drawable.record : R.drawable.pause);
		button.setTag(recording);
		formatSpinner.setEnabled(recording);
    }
    
    public void onStopButtonClick(View view) {
    	Log.d("RecorderView", "stopButton clicked");
    	showSaveDialog();
    }
    
    public void showSaveDialog() {	 
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.save_dialog, null))
        	.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
        		@Override
        		public void onClick(DialogInterface dialog, int id) {
        			// sign in the user ...
        		}
        	})
        	.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			//getDialog().cancel();
        		}
        	});
        builder.create();
        builder.show();
    }
    
    private long getFormat() {
    	long[] formatList = {
    		MediaRecorder.OutputFormat.THREE_GPP,
    		MediaRecorder.OutputFormat.MPEG_4,
    		MediaRecorder.OutputFormat.AMR_NB,
    		MediaRecorder.OutputFormat.AMR_WB,
    		MediaRecorder.OutputFormat.AAC_ADTS
    	};
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	return formatList[(int) formatSpinner.getSelectedItemId()];
    }

}
