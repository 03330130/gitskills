package com.kangtai.MassageChairUI.Updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.kangtai.MassageChairUI.OperationActivityHD;
import com.kangtai.MassageChairUI.R;
import com.kangtai.MassageChairUI.Protocal.RokolUtil;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

/***
 * download apk
 * 
 * @author
 * 
 */
public class UpdateService extends Service {
	private static final int TIMEOUT = 10 * 1000;// time out
	// apk  url
	private static final String down_url = "http://www.rokol.cn/upload/app/Rokol_8900_zh.apk";
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;

	private String app_name;

	private NotificationManager notificationManager;
	private Notification notification;

	private Intent updateIntent;
	private PendingIntent pendingIntent;

	private int notification_id = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("getUpdateVersion", "update service on start command");
		app_name = intent.getStringExtra("app_name");
		// create file 
		RokolUtil.createFile(app_name);
		Log.e("getUpdateVersion", "install creat file");

		createNotification();

		createThread();
		Log.e("getUpdateVersion", "handler download");
		return super.onStartCommand(intent, flags, startId);

	}

	/***
	 * download 
	 */
	public void createThread() {
		/***
		 * update UI
		 */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					// create uri 
					Uri uri = Uri.fromFile(RokolUtil.updateFile);
					Install(uri);
//					notificationManager.cancel(notification_id);
					Log.e("getUpdateVersion", "install uri");

					break;
				case DOWN_ERROR:
//					builder.setContentInfo(" Download error!!");
//					builder.setContentTitle(app_name);
//					builder.setContentIntent(pendingIntent);
					Log.e("getUpdateVersion", "error");

					break;

				default:
					//stopService(updateIntent);
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateFile(down_url,
							RokolUtil.updateFile.toString());
					if (downloadSize > 0) {
						// download success 
						message.what = DOWN_OK;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}

			}
		}).start();
	}

	// install apk

	private void Install(Uri uri) {

		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setAction(android.content.Intent.ACTION_VIEW);

		intent.setDataAndType(uri,
				"application/vnd.android.package-archive");

		startActivity(intent);
		stopService(updateIntent);

	}

	/***
	 * noticification 
	 */
	RemoteViews contentView;
	Builder builder;

	public void createNotification() {

		updateIntent = new Intent(this, OperationActivityHD.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		/***
		 * 示Notification
		 */
		contentView = new RemoteViews(getBaseContext().getPackageName(),
				R.layout.notification_item);
		Log.e("getUpdateVersion", "createNotification:+packagename--------"+getBaseContext().getPackageName() );
		contentView.setTextViewText(R.id.notificationTitle, "update");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		builder = new Notification.Builder(this);

		builder.setContentIntent(pendingIntent).setContent(contentView)
				.setSmallIcon(R.drawable.app_logo)
				.setWhen(System.currentTimeMillis());

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = builder.build();

		Log.e("getUpdateVersion", "createNotification:" );

	}

	/***
	 * download file 
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;// update step
		int totalSize;// file size 
		int downloadCount = 0;// 
		int updateCount = 0;// 
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		// file size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;
			/**
			 * step 5%
			 */
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				// 锟侥憋拷通知锟斤�?
				// notification.setLatestEventInfo(this, "锟斤拷锟斤拷锟斤拷锟斤拷...", updateCount
				// + "%" + "", pendingIntent);
				contentView.setTextViewText(R.id.notificationPercent,
						updateCount + "%");
				contentView.setProgressBar(R.id.notificationProgress, 100,
						updateCount, false);
				// show_view
				notificationManager.notify(notification_id, notification);

			}

		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();

		return downloadCount;

	}

}
