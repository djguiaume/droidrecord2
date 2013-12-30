package com.persil.droidrecorder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BrowserViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bowser_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bowser_view, menu);
		return true;
	}

}
