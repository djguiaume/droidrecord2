package com.persil.droidrecorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.content.Intent;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void toRecorder(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, RecorderViewActivity.class);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
    public void toBrowser(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, BrowserViewActivity.class);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
}
