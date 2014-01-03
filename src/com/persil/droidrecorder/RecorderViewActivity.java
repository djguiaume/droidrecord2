package com.persil.droidrecorder;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.persil.droidrecorder2.Recorder;

public class RecorderViewActivity extends Activity {
	private Recorder recorder;
	private View save_view;

	//@SuppressLint("NewApi");
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder_view);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
		recorder = new Recorder();
		recorder.initRecord(getExt(), getFormat());
    	formatSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
    			recorder.setExention(getExt());
    			recorder.setFormat(getFormat());
    	    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
    			recorder.setExention(getExt());
    			recorder.setFormat(getFormat());
			}
    	});
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
	    	boolean ret = super.onNavigateUp();
	        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return ret;
    	}
       return false;
    }
    
    public void onRecordButtonClick(View view) {
    	Log.d("RecorderView", "recordButton clicked");
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	
		recorder.startRecording();
		formatSpinner.setEnabled(false);
    }
    
    public void onStopButtonClick(View view) {
    	Log.d("RecorderView", "stopButton clicked");
    	recorder.stopRecording();
    	showSaveDialog();
    }
    
    public void showSaveDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        save_view = inflater.inflate(R.layout.save_dialog, null);
        builder.setView(save_view)
        	.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
        		@Override
        		public void onClick(DialogInterface dialog, int id) {
        			Log.d("RecorderView", "Save clicked");
        			EditText file_name =
        					(EditText) save_view.findViewById(R.id.save_dialog_file_name);
        			recorder.rename(file_name.getText().toString());
        		}
        	})
        	.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			Log.d("RecorderView", "Cancel clicked");
        		}
        	});
        builder.create();
        builder.show();
    }

    private int getFormat() {
    	int[] formatList = {
    		MediaRecorder.OutputFormat.THREE_GPP,
    		MediaRecorder.OutputFormat.MPEG_4,
    		MediaRecorder.OutputFormat.AMR_NB,
    		MediaRecorder.OutputFormat.AMR_WB,
    		MediaRecorder.OutputFormat.AAC_ADTS
    	};
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	return formatList[(int) formatSpinner.getSelectedItemId()];
    }

    private String getExt() {
    	String[] formatList = {
    		".3gp",
    		".mp4",
    		".amr",
    		".amr",
    		".aac"
    	};
    	Spinner formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
    	return formatList[(int) formatSpinner.getSelectedItemId()];
    }
}
