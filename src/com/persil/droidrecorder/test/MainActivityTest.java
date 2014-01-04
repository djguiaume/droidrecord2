package com.persil.droidrecorder.test;

import com.persil.droidrecorder.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		MainActivity mainActivity = getActivity();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
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
