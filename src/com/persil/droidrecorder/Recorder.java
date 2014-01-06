package com.persil.droidrecorder;
import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class Recorder {

	private MediaRecorder mediaRecorder = null;
	private boolean state;
	private String file_path = "";
	private	int Format;
	private String Extantion = ".3gp";

	public Recorder(){
		if (mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
		}
		state = false;
		Format = MediaRecorder.OutputFormat.THREE_GPP;
	}

	public boolean setExention(String ext) {
		if (!ext.isEmpty()) {
		Extantion = ext;
		return true;
		}
		return false;
	}

	public void setFormat(int format) {
		Format = format;
	}

	public boolean updateOutputFile() {
		if (!file_path.isEmpty() && !Extantion.isEmpty()) {
		mediaRecorder.setOutputFile(file_path + Extantion);
		return true;
		}
		return false;
	}

	public boolean initRecord(String ext, int Formate) {
		Extantion = ext;
		if (mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
		}
		Format = Formate;
		file_path = Environment.getExternalStorageDirectory()
					+ File.separator + "DroidRecorder" + File.separator;
		file_path = file_path + System.currentTimeMillis();
		state = true;
		if (file_path.isEmpty())
			return false;
		return true;
	}

	public boolean startRecording() {
		if (state == true && mediaRecorder != null) {
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(Format);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(file_path + Extantion);
			resumeRecording();
			return true;
		}
		else if (state == true && mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(Format);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(file_path + Extantion);
			resumeRecording();
			return true;
		}
		return false;

	}

	private void resumeRecording()
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

	public boolean stopRecording() {
		if (mediaRecorder != null) {
		mediaRecorder.stop();
		return true;
		}
		return false;
	}

	public boolean resetRecording() {
		if (mediaRecorder != null) {
			mediaRecorder.reset();
			mediaRecorder.release();
			return true;
		}
		return false;
	}

	public boolean deleteFile() {
		File Tmp = new File(file_path + Extantion);
		if (Tmp.exists()) {
		Tmp.delete();
		return true;
		}
		return false;
	}
	
	public boolean rename(String newName) {
		if (!newName.isEmpty() && (!file_path.isEmpty() && !Extantion.isEmpty())) {
	   File basePath = new File(Environment.getExternalStorageDirectory()
			   +File.separator+"DroidRecorder"+File.separator);
	   String fileName = file_path.substring(file_path.lastIndexOf(File.separator));
	   File from = new File(basePath,fileName+Extantion);
	   File to = new File(basePath,newName+Extantion);
	   from.renameTo(to);
	   return true;
		}
		return false;
	}
}
