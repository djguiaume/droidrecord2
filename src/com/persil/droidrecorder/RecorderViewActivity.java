package com.persil.droidrecorder;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

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
    	Boolean recordStarted = (Boolean) button.getTag();
    	
    	if (recordStarted) {
    		button.setImageResource(R.drawable.record);
    		//TODO: pause recording
    		button.setTag(new Boolean(false));
    	}
    	else {
    		button.setImageResource(R.drawable.pause);
    		//TODO: start recording
    		button.setTag(new Boolean(true));
    	}
    }
    
    public void onStopButtonClick(View view) {
    	Log.d("RecorderView", "stopButton clicked");
    }

}
