package com.persil.droidrecorder;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;

import com.persil.droidrecorder.Recorder;
import com.persil.droidrecorder2.R;

public class RecorderViewActivity extends Activity {
	private Boolean		recording = false;
	private Recorder	recorder;
	private Chronometer	recordTimer;
	private View		save_view;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder_view);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);

		}
		recorder = new Recorder();
		recorder.initRecord(getExt(), getFormat());

    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	formatSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(
    	    		AdapterView<?> parentView,
    	    		View selectedItemView,
    	    		int position, long id) {
    			recorder.setExention(getExt());
    			recorder.setFormat(getFormat());
    	    }
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
    			recorder.setExention(getExt());
    			recorder.setFormat(getFormat());
			}
    	});
    	recordTimer = (Chronometer) findViewById(R.id.recordTimer);
    	updateControlState();
	}

    @Override
    public void onBackPressed() {
    	recorder.resetRecording();
    	super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
	
    @SuppressLint("NewApi") @Override
    public boolean	onNavigateUp() {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    		recorder.resetRecording();
	    	boolean ret = super.onNavigateUp();
	        overridePendingTransition(
	        		R.anim.push_right_in, R.anim.push_right_out);
			return ret;
    	}
       return false;
    }

	public void onConfigurationChanged(Configuration newConfig) {
		long recordTimeSave =  recordTimer.getBase();
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_recorder_view);
		recordTimer = (Chronometer) findViewById(R.id.recordTimer);
		if (recording) {
			recordTimer.setBase(recordTimeSave);
			recordTimer.start();
		}
		updateControlState();
	}
    
    public void onRecordButtonClick(View view) {
    	Log.d("RecorderView", "recordButton clicked");
		recorder.startRecording();
		recordTimer.setBase(SystemClock.elapsedRealtime());
		recordTimer.start();
		recording = true;
		updateControlState();
    }
    
    public void onStopButtonClick(View view) {
    	Log.d("RecorderView", "stopButton clicked");
    	recorder.stopRecording();
    	recordTimer.stop();
    	recording = false;
		updateControlState();
    	showSaveDialog();
    }
    
    private void updateControlState() {
		ImageButton recordButton = (ImageButton) findViewById(R.id.recordButton);
		Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
		ImageButton stopButton = (ImageButton) findViewById(R.id.stopButton);
		recordButton.setEnabled(!recording);
		formatSpinner.setEnabled(!recording);
		stopButton.setEnabled(recording);
    }
    
    private void showSaveDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        save_view = inflater.inflate(R.layout.save_dialog, null);
        builder.setView(save_view)
        	.setPositiveButton(
        			R.string.save, new DialogInterface.OnClickListener() {
        		@Override
        		public void onClick(DialogInterface dialog, int id) {
        			Log.d("RecorderView", "Save clicked");
        			EditText file_name =
        					(EditText) save_view.findViewById(R.id.save_dialog_file_name);
        			recorder.rename(file_name.getText().toString());
        			onBackPressed();
        		}
        	})
        	.setNegativeButton(
        			R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
        		public void onClick(DialogInterface dialog, int id) {
        			Log.d("RecorderView", "Cancel clicked");
        			recorder.deleteFile();
        	    	onBackPressed();
        		}
        	});
        builder.create();
        builder.show();
    }

    private int getFormat() {
    	int[] formatList = {
    		MediaRecorder.OutputFormat.THREE_GPP,
    		MediaRecorder.OutputFormat.MPEG_4,
    		MediaRecorder.OutputFormat.AMR_NB
    	};
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	return formatList[(int) formatSpinner.getSelectedItemId()];
    }

    private String getExt() {
    	String[] formatList = {
    		".3gp",
    		".mp4",
    		".amr"
    	};
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	return formatList[(int) formatSpinner.getSelectedItemId()];
    }
}
