package com.persil.droidrecorder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;

public class Recorder {

	private MediaRecorder mediaRecorder = null;
	private boolean state;
	private String file_path = "";
	private	int Format;
	private String Extantion = ".";
	
public Recorder(){
	if (mediaRecorder == null) {
		mediaRecorder = new MediaRecorder();
	}
	state = false;
	Format = 0;
}

public void initRecord(String ext, int Formate, Context context) {
	Extantion = ext;
	if (mediaRecorder == null) {
		mediaRecorder = new MediaRecorder();
	}
	Format = Formate;
	file_path = context.getFilesDir().getAbsolutePath();
	file_path = file_path + System.currentTimeMillis() + Extantion;
	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	mediaRecorder.setOutputFormat(Format);
	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	mediaRecorder.setOutputFile(file_path);
	state = true;
}

public void startRecording() {
	if (state == true) {
		resumeRecording();
		state = false;
	}
	else {
		pauseRecording();
		state = true;
	}
}

public void pauseRecording()
{
	mediaRecorder.stop();
	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	mediaRecorder.setOutputFormat(Format);
	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
  FileOutputStream paused_file = null;
try {
	paused_file = new FileOutputStream(file_path);
} catch (FileNotFoundException e) {
	e.printStackTrace();
}
  try {
	mediaRecorder.setOutputFile(paused_file.getFD());
} catch (IllegalStateException e) {
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
}
}

public void resumeRecording()
{
	try {
		mediaRecorder.prepare();
	} catch (IllegalStateException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	mediaRecorder.start();
}

public void ResetRecording() {
	state = false;
	if (mediaRecorder != null) {
	mediaRecorder.reset();
	mediaRecorder.release();
	}
}


}
