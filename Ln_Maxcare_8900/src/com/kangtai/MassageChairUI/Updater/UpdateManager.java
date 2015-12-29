package com.kangtai.MassageChairUI.Updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kangtai.MassageChairUI.R;
import com.kangtai.MassageChairUI.Protocal.RokolUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


public class UpdateManager {
	private static final int NO_NEW_VERSION = 0;
	private static final int FOUND_NEW_VERSION = 1;
	private Context mContext;
	private static float mNewVersionCode;

	private static String mNewVersionName;
	private static float mCurrentVersionCode;

	private static String mVersionPath = "http://www.rokol.cn/upload/app/version.json";

	public UpdateManager(Context context) {
		this.mContext = context;

		getVerCode();
		getVerName();
	}

	/**
	 * get server version
	 * 
	 * @param serverPath
	 * @return
	 * @throws Exception
	 */
	public String getUpdateVersion(String serverPath) throws Exception {

		final StringBuilder newVer = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		// serverPath ��version.json ��·��
		HttpResponse response;
		try {
			response = client.execute(new HttpGet(serverPath));

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				BufferedReader reader;
				reader = new BufferedReader(new InputStreamReader(
						entity.getContent(), "UTF-8"), 8192);

				String line = null;

				while ((line = reader.readLine()) != null) {
					newVer.append(line + "\n");
				}

				reader.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newVer.toString();
	}

	/**
	 * get current version code
	 * 
	 * @return
	 */
	public void getVerCode() {
		float verCode = -1;
		try {
			verCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mCurrentVersionCode = verCode;
	}

	/**
	 * get current version name
	 * 
	 * @param context
	 * @return
	 */
	public String getVerName() {
		String verName = "";
		try {
			verName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	public boolean getServerVerion() {
		try {
			String newVerJson = getUpdateVersion(mVersionPath);
//			JSONArray jsonArray = new JSONArray(newVerJson);
			JSONObject obj=new JSONObject(newVerJson);
//			if (jsonArray.length() > 0) {
//				JSONObject obj = jsonArray.getJSONObject(0);
				try {
					mNewVersionCode = Float
							.parseFloat(obj.getString("verCode"));
					mNewVersionName = obj.getString("verName");
					Log.e("getUpdateVersion", "get version is :"+mNewVersionCode );
				} catch (Exception e) {
					mNewVersionCode = -1;
					mNewVersionName = "";
					return false;
				}
//			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("getUpdateVersion", "get version err :" + e.toString());
			return false;
		}

		return true;
	}

	public void checkToUpdate() throws NameNotFoundException {
		Message msg = new Message();
		new Thread() {
			public void run() {
				getServerVerion();
				Message message = new Message();
				if (mNewVersionCode > mCurrentVersionCode) {
					// to do update

					message.what = FOUND_NEW_VERSION;
					Log.d(">", mNewVersionCode + " " + mCurrentVersionCode);
				} else {

					message.what = NO_NEW_VERSION;
					Log.d("<", mNewVersionCode + " " + mCurrentVersionCode);
				}
				mHandler.sendMessage(message);
				// Log.d("---", mNewVersionCode + "");
			}
		}.start();

	}

	public void showDialog() {
		Builder alert = new AlertDialog.Builder(mContext);
		alert/* .setTitle(mContext.getResources().getString(R.string.upgrade)) */
		.setMessage(
				mContext.getResources().getString(R.string.found_new_version))

				.setPositiveButton(
						mContext.getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								Intent updateIntent = new Intent(mContext,
										UpdateService.class);
								updateIntent.putExtra(
										"app_name",
										mContext.getResources().getString(
												R.string.app_name));
								mContext.startService(updateIntent);
								Log.e("getUpdateVersion", "start service updateinent");

							}
						})
				.setNegativeButton(
						mContext.getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
		alert.create().show();

	}
	//
	

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case FOUND_NEW_VERSION:
				showDialog();
//				testCode();

				break;
			case NO_NEW_VERSION:

				RokolUtil.showToast(mContext, mContext.getResources()
						.getString(R.string.is_latest_version), 1);
				break;
			default:
				break;
			}
		}
	};

}
