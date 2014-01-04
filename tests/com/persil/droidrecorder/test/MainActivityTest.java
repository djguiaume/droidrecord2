package com.persil.droidrecorder.test;

import com.persil.droidrecorder.MainActivity;
import com.persil.droidrecorder2.R;

import android.graphics.Rect;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private ImageButton recorderButton;
	private Button browserButton;
	private View mainLayout;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MainActivity mainActivity = getActivity();
		recorderButton = (ImageButton) mainActivity.findViewById(R.id.recorderButton);
		browserButton = (Button) mainActivity.findViewById(R.id.browserButton);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddButtonOnScreen()
	 {
		int fullWidth = mainLayout.getWidth();  
		   int fullHeight = mainLayout.getHeight();  
		   int[] mainLayoutLocation = new int[2];  
		   mainLayout.getLocationOnScreen(mainLayoutLocation);  
		   int[] viewLocation = new int[2];  
		   recorderButton.getLocationOnScreen(viewLocation);  
		   Rect outRect = new Rect();  
		   recorderButton.getDrawingRect(outRect);  
		   assertTrue("Add button off the right of the screen", fullWidth  
		           + mainLayoutLocation[0] > outRect.width() + viewLocation[0]);  
		   assertTrue("Add button off the bottom of the screen", fullHeight  
		           + mainLayoutLocation[1] > outRect.height() + viewLocation[1]);
	 }
	
	public void testOnCreateBundle() {
		fail("Not yet implemented"); // TODO
	}

	public void testToRecorder() {
		fail("Not yet implemented"); // TODO
	}

	public void testToBrowser() {
		fail("Not yet implemented"); // TODO
	}

}
