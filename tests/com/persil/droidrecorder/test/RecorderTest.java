package com.persil.droidrecorder.test;

import android.media.MediaRecorder;

import com.persil.droidrecorder.Recorder;

import junit.framework.TestCase;

public class RecorderTest extends TestCase {

	public RecorderTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRecorder() {
		Recorder recorder = new Recorder();
		if (recorder.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4) == true)
			System.out.println("InitRecord Correctly !");
		else
			fail("InitRecord didn't work Correctly !");
		if ((recorder.setExention(".3gp")) == true)
			System.out.println("Extention .mp4 Set Correctly !");
		else
			fail("try to set Extention with null string !");	
		recorder.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		if (recorder.updateOutputFile() == true)
			System.out.println("File update Correctly !");
		else
			fail("File didn't update Correctly !");
		if (recorder.startRecording() == true)
			System.out.println("StartRecording Success !");
		else
			fail("StartRecording Fail !");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (recorder.stopRecording() == true)
			System.out.println("StopRecording Success !");
		else
			fail("StopRecording Fail !");
		if (recorder.resetRecording() == true)
			System.out.println("ResetRecording Success !");
		else
			fail("ResetRecording Fail !");
		if (recorder.deleteFile() == true)
			System.out.println("DeleteFileRecording Success !");
		else
			fail("DeleteFileRecording Fail !");
		
		Recorder recorder2 = new Recorder();
		if (recorder2.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4) == true)
			System.out.println("InitRecord Correctly !");
		else
			fail("InitRecord didn't work Correctly !");
		if ((recorder2.setExention(".3gp")) == true)
			System.out.println("Extention .mp4 Set Correctly !");
		else
			fail("try to set Extention with null string !");	
		recorder2.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		if (recorder2.updateOutputFile() == true)
			System.out.println("File update Correctly !");
		else
			fail("File didn't update Correctly !");
		if (recorder2.startRecording() == true)
			System.out.println("StartRecording Success !");
		else
			fail("StartRecording Fail !");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (recorder2.stopRecording() == true)
			System.out.println("StopRecording Success !");
		else
			fail("StopRecording Fail !");
		if (recorder2.resetRecording() == true)
			System.out.println("ResetRecording Success !");
		else
			fail("ResetRecording Fail !");
		if (recorder2.rename("RenameTest") == true)
			System.out.println("RenameRecording Success !");
		else
			fail("RenameRecording Fail !");
		recorder2.deleteFile();
	}

	public void testSetExention() {
		Recorder recorder = new Recorder();
		if ((recorder.setExention(".mp4")) == true)
			System.out.println("Extention .mp4 Set Correctly !");
		else
			fail("try to set Extention with null string !");	
	}
	
	public void testUpdateOutputFile() {
		Recorder recorder = new Recorder();
		recorder.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4);
		recorder.setExention(".3gp");
		recorder.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		if (recorder.updateOutputFile() == true)
			System.out.println("File update Correctly !");
		else
			fail("File didn't update Correctly !");
	}
	
	public void testInitRecord() {
		Recorder recorder = new Recorder();
		if (recorder.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4) == true)
			System.out.println("InitRecord Correctly !");
		else
			fail("InitRecord didn't work Correctly !");
		
	}
	
	public void testStartRecording() {
		Recorder recorder = new Recorder();
		recorder.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4);
		recorder.setExention(".3gp");
		recorder.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.updateOutputFile();
		if (recorder.startRecording() == true)
			System.out.println("StartRecording Success !");
		else
			fail("StartRecording Fail !");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recorder.stopRecording();
		recorder.resetRecording();
		recorder.deleteFile();
	}
	
	public void testStopRecording() {
		Recorder recorder = new Recorder();
		recorder.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4);
		recorder.setExention(".3gp");
		recorder.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.updateOutputFile();
		recorder.startRecording();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (recorder.stopRecording() == true)
		System.out.println("StopRecording Success !");
	else
		fail("StopRecording Fail !");
		recorder.resetRecording();
		recorder.deleteFile();
	}
	
	public void testResetRecording() {
		Recorder recorder = new Recorder();
		recorder.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4);
		recorder.setExention(".3gp");
		recorder.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.updateOutputFile();
		recorder.startRecording();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recorder.stopRecording();
		if (recorder.resetRecording() == true)
			System.out.println("ResetRecording Success !");
		else
			fail("ResetRecording Fail !");
		recorder.deleteFile();
	}
	public void testDeleteFile() {
		Recorder recorder = new Recorder();
		recorder.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4);
		recorder.setExention(".3gp");
		recorder.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.updateOutputFile();
		recorder.startRecording();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recorder.stopRecording();
		recorder.resetRecording();
		if (recorder.deleteFile() == true)
			System.out.println("DeleteFileRecording Success !");
		else
			fail("DeleteFileRecording Fail !");
	}
	public void testRename() {
		Recorder recorder2 = new Recorder();
		recorder2.initRecord(".mp4", MediaRecorder.OutputFormat.MPEG_4);
		recorder2.setExention(".3gp");
		recorder2.setFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder2.updateOutputFile();
		recorder2.startRecording();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recorder2.stopRecording();
		recorder2.resetRecording();
		if (recorder2.rename("RenameTest") == true)
			System.out.println("RenameRecording Success !");
		else
			fail("RenameRecording Fail !");
		if (recorder2.rename("") == true)
			fail("RenameRecording renamed with and empty name");
		else
			System.out.println("RenameRecording with an empty parameter : success");
	}
}
