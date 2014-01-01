package com.persil.droidrecorder;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;

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

}
