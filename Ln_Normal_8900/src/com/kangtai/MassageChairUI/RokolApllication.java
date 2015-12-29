package com.kangtai.MassageChairUI;

import java.util.Locale;
import java.util.StringTokenizer;

import com.kangtai.MassageChairUI.R;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

public class RokolApllication extends Application {

	private final String[] MODEL = new String[] { "RK_7905", "RK_7806" };
	// Language
	private final int CHINESE = 0;
	private final int ENGLISH = 1;
	private final int KOREAN = 2;
	private final String LANGUAGE = "Language";
	private int mCurrentLanguage;
	private int mCurrentTimer = 1;
	private Locale mLocale;
	private boolean mSoundSwitch;
	private SharedPreferences sp;

	// Timer
	private final int TIMER_0 = 0;
	private final int TIMER_1 = 1;
	private final int TIMER_2 = 2;

	public int getCurrentLanguage() {
		Log.d(getClass().getSimpleName(), "getCurrentLanguage -----------"+mCurrentLanguage);
		return mCurrentLanguage;
	}

	public int getCurrentTimer() {
		Log.d(getClass().getSimpleName(), "getCurrentTimer "
				+ this.mCurrentTimer);
		return mCurrentTimer;
	}

	public boolean isSoundSwitchOn() {
		return mSoundSwitch = sp.getBoolean("TouchSound", mSoundSwitch);
	}

	public String LanguageToString() {
		int resId;
		switch (this.mCurrentLanguage) {
		case CHINESE:
			resId = R.string.chinese;
			break;
		case ENGLISH:
			resId = R.string.english;
			break;
		case KOREAN:
			resId = R.string.korean;
			break;
		default:
			resId = 0;
			break;

		}
		return getResources().getString(resId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
       Log.d("Application", "Application on creat----");
		sp = getApplicationContext().getSharedPreferences("Device",
				Context.MODE_PRIVATE);

		this.mLocale = Locale.getDefault();

		Locale.setDefault(mLocale);
		setCurrentLanguage(sp.getInt(LANGUAGE, mCurrentLanguage));

	}

	public void setCurrentLanguage(int currentLanguage) {
		this.mCurrentLanguage = currentLanguage;
		setLocale();
	}

	public void setCurrentTimer(int currentTimer) {
		this.mCurrentTimer = currentTimer;

		Log.d(getClass().getSimpleName(), "setCurrentTimer "
				+ this.mCurrentTimer);
		// Intent it=new Intent();
		// startService(it);
	}

	/** Sets the Locale to the given Locale object. */
	private void setLocale() {
		Configuration config = getResources().getConfiguration();

		DisplayMetrics metrics = getResources().getDisplayMetrics();

		switch (this.mCurrentLanguage) {
		case CHINESE:
			config.locale = Locale.SIMPLIFIED_CHINESE;
			break;
		case ENGLISH:
			config.locale = Locale.ENGLISH;
			break;
		case KOREAN:
			config.locale = Locale.KOREA;
			break;
		}

		getResources().updateConfiguration(config, metrics);
		sp.edit().putInt(LANGUAGE, this.mCurrentLanguage).commit();
		// Toast.makeText(context, locale.toString(), Toast.LENGTH_LONG).show();
	}

	public void setSoundSwitch(boolean soundSwitch) {
		this.mSoundSwitch = soundSwitch;
		sp.edit().putBoolean("TouchSound", this.mSoundSwitch).commit();
	}

	/**
	 * ȡ�� Timer�� �ַ���ʾ
	 * 
	 * @return 10min,20min or 30min
	 */
	public String TimerToString() {
		int resId;
		switch (this.mCurrentTimer) {
		case TIMER_0:
			resId = R.string.timer_0;
			break;
		case TIMER_1:
			resId = R.string.timer_1;
			break;

		case TIMER_2:
			resId = R.string.timer_2;
			break;
		default:
			resId = 0;
			break;
		}
		return getResources().getString(resId);
	}

	public String getConnectedDeviceName() {
		return sp.getString("DeviceName", "");
	}
	/*
	 * private void TimerTick() {
	 * 
	 * TimerHandler.removeCallbacks(TimerRunnable);
	 * TimerHandler.postDelayed(TimerRunnable, (this.mCurrentTimer*10+10) *60*
	 * 1000); }
	 * 
	 * Handler TimerHandler=new Handler(){
	 * 
	 * }; Runnable TimerRunnable=new Runnable() {
	 * 
	 * @Override public void run() { // TODO send power off command
	 * 
	 * Toast.makeText(getApplicationContext(), "Timer:"+(mCurrentTimer*10+10)
	 * *60+"send power off command", Toast.LENGTH_LONG).show();
	 * TimerHandler.removeCallbacks(TimerRunnable); } };
	 */
}
