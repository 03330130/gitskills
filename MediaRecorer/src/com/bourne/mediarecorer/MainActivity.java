package com.bourne.mediarecorer;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	private final static String TAG="Recorder";
	private Button btn_record,btn_paly;
	private MediaRecorder recorder=null;
	private static int Flag=1;
	private String audioFile;
	private MediaPlayer player=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_record=(Button)findViewById(R.id.btn_record);
		btn_paly=(Button)findViewById(R.id.btn_play);
		btn_record.setOnClickListener(this);
		btn_paly.setOnClickListener(new OnClickListener() {
			boolean mStartPlaying=true;
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				onPlay(mStartPlaying);
				if (mStartPlaying) {
                    btn_paly.setText("Stop playing");
                } else {
                	btn_paly.setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
			}
		});
	}
	private void onPlay(boolean start){
		if(start){
			startPlaying();
		}
		else{
			stopPlaying();
		}
		
	}
	private void startPlaying(){
		player=new MediaPlayer();
		player = new MediaPlayer();
	        try {
	        	player.setDataSource(audioFile);
	        	player.prepare();
	        	player.start();
	        } catch (IOException e) {
	            Log.e(TAG, "prepare() failed");
	        }
	}
	private void stopPlaying(){
//		player.stop();
		player.release();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(Flag){
		case 1:
		Log.d(TAG, "----"+audioFile);
		btn_record.setText("Stop");
		initializeAudio();
		    Flag=2;
		    break;
		case 2:
			btn_record.setText("Record");
			Flag=1;
			stopRecording();
			break;
		}
	}
	private void stopRecording() {
		recorder.stop();
		recorder.release();
		recorder = null;
    }
    private void initializeAudio() {
    	audioFile="/sdcard/"+System.currentTimeMillis()+"autiorecord.3gp";
        recorder = new MediaRecorder();// new出MediaRecorder对象
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置MediaRecorder录制音频的编码为amr.
        recorder.setOutputFile(audioFile);
        // 设置录制好的音频文件保存路径
        try {
                recorder.prepare();// 准备录制
                recorder.start();// 开始录制
        } catch (IllegalStateException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }

}
}
