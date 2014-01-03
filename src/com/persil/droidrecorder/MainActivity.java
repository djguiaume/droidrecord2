package com.persil.droidrecorder;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import com.persil.droidrecorder2.R;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d("DroidRecorder", "No SDCARD");
	    } else {
	        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder");
	        directory.mkdirs();
	    }
        setContentView(R.layout.activity_main);
    }
    
    public void toRecorder(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, RecorderViewActivity.class);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
    public void toBrowser(View view) {
        // Do something in response to button
    	/*Intent intent = new Intent(this, BrowserViewActivity.class);
    	startActivity(intent);*/
    	Intent intent1 = new Intent(this, FileChooser.class);
        startActivity(intent1);
    	overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
}
