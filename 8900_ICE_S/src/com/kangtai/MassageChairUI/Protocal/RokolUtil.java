package com.kangtai.MassageChairUI.Protocal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

import com.kangtai.MassageChairUI.R;
import com.kangtai.MassageChairUI.RokolApllication;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RokolUtil {

	private static RokolApllication mApp;
	private static SoundPool mSoundPool;
	private static int mSoundSource;
	private static File updateDir;
	public static File updateFile;
	private static Toast toast;

	/**
	 * check the network available or not
	 * 
	 * @param context
	 * @return available true,otherwise false
	 */
	public static boolean checkNetWorkStatus(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
			Log.i("NetStatus", "The net was connected");
		} else {
			result = false;
			Log.i("NetStatus", "The net was bad!");
		}
		return result;
	}

	public static boolean checWlankNetworkConnection(Context context) {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// final android.net.NetworkInfo mobile
		// =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isConnected()/* ||mobile.isAvailable() */)
			return true;
		else
			return false;
	}

	/**
	 * check if the Service is working
	 * 
	 * @param context
	 * @param serviceClassName
	 * @return
	 */
	public static boolean isServiceWorked(Context context,
			String serviceClassName) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(serviceClassName)) {
				return true;
			}
		}
		return false;
	}

	/***
	 * create file
	 * 
	 * @param name
	 *            The file name
	 */
	public static void createFile(String name) {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory()
					+ "/download");
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * byte to hex string
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String bytesToHexString(byte[] src, int length) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * byte to string array
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String[] bytesToHexStrings(byte[] src, int length) {
		if (src == null || src.length <= 0) {
			return null;
		}
		String[] str = new String[length];

		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				str[i] = "0";
			}
			str[i] = hv;
		}
		return str;
	}

	/**
	 * compare 2 string array from the index 1
	 * 
	 * @param arg1
	 * @param arg2
	 *            receive data
	 * @return equal is true, otherwise false
	 */
	public static boolean compareStingArray(String[] arg1, String[] arg2) {
		if (arg1.length == arg2.length) {

			for (int i = 1; i < arg1.length; i++) {

				if (!(arg1[i].equals(arg2[i]))) {
					Log.d("Compare", "" + (arg1[i].equals(arg2[i])));
					return false;
				}
			}
			return true;
		}
		Log.d("Compare", arg2.length + "   " + arg1.length);
		return false;
	}

	/**
	 * cancel shadow to text
	 * 
	 * @param textView
	 * @return
	 */
	public static void setTextNoShadow(Context context, View view) {
//		((TextView) view).setTextAppearance(context, R.style.main_text_style);
//		((TextView) view).setShadowLayer(0, 0, 0, 0x00000000);
		((TextView) view).setSingleLine(true);
		((TextView) view).setEllipsize(TruncateAt.MARQUEE);
	}

	/**
	 * add shadow to text
	 * 
	 * @param textView
	 */
	public static void setTextShadow(Context context, View view) {
//		((TextView) view).setTextAppearance(context, R.style.TextSizeLarge);
//		((TextView) view).setShadowLayer(5.5f, 2, 3, 0xff000000);
		((TextView) view).setSingleLine(true);
		((TextView) view).setEllipsize(TruncateAt.MARQUEE);
	}

	/**
	 * play sound
	 * 
	 * @param mContext
	 */
	public static void performTouchSound(Context mContext) {
		mApp = (RokolApllication) mContext.getApplicationContext();
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);// ��һ������Ϊͬʱ���������������������ڶ����������ͣ�����Ϊ��������
		mSoundSource = mSoundPool.load(mContext, R.raw.sound, 1);
		AudioManager am = (AudioManager) mContext
				.getSystemService(mContext.AUDIO_SERVICE);
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float audioCurrentVolumn = am
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		final float volumnRatio = audioCurrentVolumn / audioMaxVolumn;

		if (mApp.isSoundSwitchOn())
			mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					if (status == 0) {
						soundPool.play(mSoundSource, volumnRatio, volumnRatio,
								1, 0, 1);
						// release memory resource
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								try {
									mSoundPool.release();
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}, 500);
					}
				}

			});
	}

	/**
	 * Show Toast
	 * 
	 * @param context
	 * @param msg
	 * @param displayTime
	 *            LENGTH_LONG = 1 ,else 0
	 */
	public static void showToast(Context context, String msg, int length) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, length);
			toast.setGravity(Gravity.CENTER, 0, 0);

		} else {
			toast.setText(msg);		
		}
		toast.show();
	}
}
