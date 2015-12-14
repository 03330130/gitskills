package com.kangtai.MassageChairUI;

import com.kangtai.MassageChairUI.R;
import com.kangtai.MassageChairUI.Protocal.RokolUtil;
import com.kangtai.MassageChairUI.Updater.UpdateManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 4000; // delay 3 second
	private TextView mVersion;
	private ImageView imgLogo;
	AnimationDrawable anim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		mVersion = (TextView) findViewById(R.id.tv_version);
		imgLogo = (ImageView) findViewById(R.id.imageView1);
//		imgLogo.setBackgroundResource(R.drawable.splash_animation);
//		anim = (AnimationDrawable) imgLogo.getBackground();
		// mVersion.setText("V"+ new
		// UpdateManager(this.getApplicationContext()).getVerName());
		new Handler().postDelayed(new Runnable() {
			public void run() {

				// Get local Bluetooth adapter
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
						.getDefaultAdapter();

				// If the adapter is null, then Bluetooth is not supported
				/*if (mBluetoothAdapter != null) {
*/
					Intent mainIntent = new Intent(SplashActivity.this,
							MainActivityHD.class);
					SplashActivity.this.startActivity(mainIntent);

				/*} else {
					RokolUtil.showToast(SplashActivity.this,
							"Bluetooth is not available", 0);

				}*/
				SplashActivity.this.finish();
				return;
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		anim.start();
		
	}

}
