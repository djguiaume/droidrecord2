package com.persil.droidrecorder;
import java.io.File;
import java.io.IOException;

import com.persil.droidrecorder2.R;

import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class Recorder {

	private MediaRecorder mediaRecorder = null;
	private boolean state;
	private boolean start;
	private String file_path = "";
	private	int Format;
	private String Extantion = ".";

	public Recorder(){
		if (mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
		}
		state = false;
		start = true;
		Format = 0;
	}

	public void setExention(String ext) {
		if (!ext.isEmpty()) {
		Extantion = ext;
		}
	}

	public void setFormat(int format) {
		Format = format;
	}

	public void updateOutputFile() {
		if (!file_path.isEmpty() && !Extantion.isEmpty()) {
		mediaRecorder.setOutputFile(file_path + Extantion);
		}
	}

	public void initRecord(String ext, int Formate) {
		Extantion = ext;
		if (mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
		}
		Format = Formate;
		file_path = Environment.getExternalStorageDirectory()
					+ File.separator + "DroidRecorder" + File.separator;
		file_path = file_path + System.currentTimeMillis();
		state = true;
	}

	public void startRecording() {
		if (start == true && mediaRecorder != null) {
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(Format);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(file_path + Extantion);
			Log.d("FilePath:", file_path + Extantion);
			start = false;
		}
		else if (start == true && mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(Format);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(file_path + Extantion);
			Log.d("FilePath:", file_path + Extantion);
			start = false;
		}
		if (state == true) {
			resumeRecording();
			state = false;
		}
	}

	public void resumeRecording()
	{
		if (mediaRecorder != null) {
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaRecorder.start();
		}
	}

	public void stopRecording() {
		if (mediaRecorder != null) {
		mediaRecorder.stop();
		}
	}

	public void resetRecording() {
		state = false;
		start = true;
		if (mediaRecorder != null) {
			mediaRecorder.reset();
			mediaRecorder.release();
		}
	}

	public void rename(String newName) {
		if (!newName.isEmpty() && (!file_path.isEmpty() && !Extantion.isEmpty())) {
	   File basePath = new File(Environment.getExternalStorageDirectory()
			   +File.separator+"DroidRecorder"+File.separator);
	   String fileName = file_path.substring(file_path.lastIndexOf(File.separator));
	   File from = new File(basePath,fileName+Extantion);
	   File to = new File(basePath,newName+Extantion);
	   from.renameTo(to);
		}
	}
}
