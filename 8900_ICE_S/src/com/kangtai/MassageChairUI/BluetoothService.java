/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kangtai.MassageChairUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.kangtai.MassageChairUI.Protocal.DataFrame;
import com.kangtai.MassageChairUI.Protocal.RokolUtil;
import com.kangtai.MassageChairUI.Protocal.DataFrame.OPERATION;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for incoming
 * connections, a thread for connecting with a device, and a thread for
 * performing data transmissions when connected.
 */
public class BluetoothService extends Service {
	// Debugging
	private static final String TAG = "BluetoothService";
	private static final boolean D = true;
	private boolean connected;
	private static int connectTime=0;
	private static boolean STOPFLAG=true,serviceCreatFlag=false;
	private static boolean addNewDevice=false;
//	boolean soktcnting=false;
//    boolean soktcnted=false;
	// Name for the SDP record when creating server socket
	private static final String NAME_SECURE = "BluetoothChatSecure";
	private static final String NAME_INSECURE = "BluetoothChatInsecure";
	public final static String BUNDLE_TYPE = "BluetoothService";
	private static String ADDRESS=null;
	/*
	 * liumingtao copy from API reference If you are connecting to a Bluetooth
	 * serial board then try using the well-known SPP UUID
	 * 00001101-0000-1000-8000-00805F9B34FB. However if you are connecting to an
	 * Android peer then please generate your own unique UUID.
	 */

	// Unique UUID for this application
	private static final UUID MY_UUID_SECURE = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");// "fa87c0d0-afac-11de-8a39-0800200c9a66"*
	private static final UUID MY_UUID_INSECURE = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");// "8ce255c0-200a-11e0-ac64-0800200c9a66"

	// Member fields
	private BluetoothAdapter mAdapter;
	// private final Handler mHandler;
//	private AcceptThread mSecureAcceptThread;
	// private AcceptThread mInsecureAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming
												// connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing
													// connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote
													// device

	public static final int REQUIRE_CONNECT = 0;
	public static final int REQUIRE_WRITE = 1;
	private TimerReceiver mTimerReceiver = null;
	BluetoothReceiver mBluetoothReceiver;

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 * 
	 * @param context
	 *            The UI Activity Context
	 * @param handler
	 *            A Handler to send messages back to the UI Activity
	 */
	/*
	 * public BluetoothService(Context context, Handler handler) { mAdapter =
	 * BluetoothAdapter.getDefaultAdapter(); mState = STATE_NONE; mHandler =
	 * handler; }
	 */

	/**
	 * Set the current state of the chat connection
	 * 
	 * @param state
	 *            An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		if (D)
			Log.d(TAG, "setState() " + mState + " -> " + state);
		mState = state;

		Bundle bundle = new Bundle();
		bundle.putInt(BUNDLE_TYPE, OperationActivityHD.MESSAGE_STATE_CHANGE);
		Log.v(TAG, "OperationActivityHD.MESSAGE_STATE_CHANGE:"
				+ OperationActivityHD.MESSAGE_STATE_CHANGE);
		bundle.putInt("state", state);
		showMessage(bundle);
	}

	/**
	 * Return the current connection state.
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
	 */
//	public synchronized void start() {      20150130   TRS
//		if (D)
//			Log.d(TAG, "start-----------------service");
//
//		// Cancel any thread attempting to make a connection
//		if (mConnectThread != null) {
//			mConnectThread.cancel();
//			mConnectThread = null;
//		}
//
//		// Cancel any thread currently running a connection
//		if (mConnectedThread != null) {
//			mConnectedThread.cancel();
//			mConnectedThread = null;
//		}
//
//		setState(STATE_LISTEN);
//		
//		System.out.println("where are u going----------------------");

		// Start the thread to listen on a BluetoothServerSocket
//		if (mSecureAcceptThread == null) {
////			mSecureAcceptThread = new AcceptThread(true);
////			mSecureAcceptThread.start();
//		}
		/*
		 * if (mInsecureAcceptThread == null) { mInsecureAcceptThread = new
		 * AcceptThread(false); mInsecureAcceptThread.start(); }
		 */
//	}

	public synchronized void connect(BluetoothDevice device) {
		if (D)
			Log.d(TAG, "connect device: " + device);

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
//		if(STOPFLAG){
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 * 
	 * @param socket
	 *            The BluetoothSocket on which the connection was made
	 * @param device
	 *            The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}



		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// Send the name of the connected device back to the UI Activity
		// Message msg =
		// mHandler.obtainMessage(OperationActivityHD.MESSAGE_DEVICE_NAME);
		
		Bundle bundle = new Bundle();
		bundle.putInt(BUNDLE_TYPE, OperationActivityHD.MESSAGE_DEVICE_NAME);
		bundle.putString(OperationActivityHD.DEVICE_NAME, device.getName());
		bundle.putString(OperationActivityHD.DEVICE_ADDRESS, device.getAddress());
		System.out.println("connected()--------------showMessage(bundle)");
		showMessage(bundle);
		// msg.setData(bundle);
		// mHandler.sendMessage(msg);

		setState(STATE_CONNECTED);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (D)
			Log.d(TAG, "stop");

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

//		if (mSecureAcceptThread != null) {
//			mSecureAcceptThread.cancel();
//			mSecureAcceptThread = null;
//		}

		/*
		 * if (mInsecureAcceptThread != null) { mInsecureAcceptThread.cancel();
		 * mInsecureAcceptThread = null; }
		 */
//		setState(STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * 
	 * @param out
	 *            The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (D)
				Log.d(TAG, "≈–∂œ «∑Ò¡¨Ω”≤¢∑¢ÀÕ÷∏¡Ó" );
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		// Send a failure message back to the Activity
		// Message msg = mHandler.obtainMessage(OperationActivityHD.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putInt(BUNDLE_TYPE, OperationActivityHD.MESSAGE_TOAST);
		bundle.putString(OperationActivityHD.TOAST, "ccttd");
		// msg.setData(bundle);
		// mHandler.sendMessage(msg);
		showMessage(bundle);
		
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(STATE_CONNECTING);
		if(STOPFLAG && connectTime<2){          //&& !addNewDevice
			try{
				Thread.sleep(2000);}
				catch(Exception e)
				{e.printStackTrace();}
		BluetoothDevice device=mAdapter.getRemoteDevice(ADDRESS);
		BluetoothService.this.connect(device);
		Log.e(TAG, "-------- connection failed----------STOPFLAG---->"+STOPFLAG+","+addNewDevice);}
//		else
//		{STOPFLAG=true;}
		// Start the service over to restart listening mode
//		BluetoothService.this.start();  20150130  TRS
		
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		// Send a failure message back to the Activity
		// Message msg = mHandler.obtainMessage(OperationActivityHD.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putInt(BUNDLE_TYPE, OperationActivityHD.MESSAGE_TOAST);
		bundle.putString(OperationActivityHD.TOAST, "lost");
		// msg.setData(bundle);
		// mHandler.sendMessage(msg);
		showMessage(bundle);
		// Start the service over to restart listening mode
//		BluetoothService.this.start();20150130  TRS
//		System.out.println("conncetedLost()-------------->setState?");
		if(STOPFLAG){
		BluetoothDevice device=mAdapter.getRemoteDevice(ADDRESS);
		BluetoothService.this.connect(device);}
	}

	/**
	 * This thread runs while listening for incoming connections. It behaves
	 * like a server-side client. It runs until a connection is accepted (or
	 * until cancelled).
	 */
//	private class AcceptThread extends Thread {
//		// The local server socket
//		private final BluetoothServerSocket mmServerSocket;
//		private String mSocketType;
//
//		public AcceptThread(boolean secure) {
//			BluetoothServerSocket tmp = null;
//			mSocketType = secure ? "Secure" : "Insecure";
//
//			// Create a new listening server socket
//			try {
//				if (secure) {
//					tmp = mAdapter.listenUsingRfcommWithServiceRecord(
//							NAME_SECURE, MY_UUID_SECURE);
//				} else {
//					tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
//							NAME_INSECURE, MY_UUID_INSECURE);
//				}
//			} catch (IOException e) {
//				Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
//			}
//			mmServerSocket = tmp;
//		}
//
//		public void run() {
//			if (D)
//				Log.d(TAG, "Socket Type: " + mSocketType
//						+ "BEGIN mAcceptThread" + this);
//			setName("AcceptThread" + mSocketType);
//
//			BluetoothSocket socket = null;
//
//			// Listen to the server socket if we're not connected
//			while (mState != STATE_CONNECTED) {
//				try {
//					// This is a blocking call and will only return on a
//					// successful connection or an exception
//					socket = mmServerSocket.accept();
//				} catch (IOException e) {
//					Log.e(TAG, "Socket Type: " + mSocketType
//							+ "accept() failed", e);
//					break;
//				}

//				// If a connection was accepted
//				if (socket != null) {
//					synchronized (BluetoothService.this) {
//						switch (mState) {
//						case STATE_LISTEN:
//						case STATE_CONNECTING:
//							// Situation normal. Start the connected thread.
//							connected(socket, socket.getRemoteDevice(),
//									mSocketType);
//							break;
//						case STATE_NONE:
//						case STATE_CONNECTED:
//							// Either not ready or already connected. Terminate
//							// new socket.
//							try {
//								socket.close();
//							} catch (IOException e) {
//								Log.e(TAG, "Could not close unwanted socket", e);
//							}
//							break;
//						}
//					}
//				}
//			}
//			if (D)
//				Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
//
//		}
//
//		public void cancel() {
//			if (D)
//				Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
//			try {
//				mmServerSocket.close();
//			} catch (IOException e) {
//				Log.e(TAG, "Socket Type" + mSocketType
//						+ "close() of server failed", e);
//			}
//		}
//	}


	/**
	 * This thread runs while attempting to make an outgoing connection with a
	 * device. It runs straight through; the connection either succeeds or
	 * fails.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private String mSocketType;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;
			 connected=false;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
					tmp = device
							.createRfcommSocketToServiceRecord(MY_UUID_SECURE );
				
			} catch (IOException e) {
				Log.e(TAG, "----------- " + "create() socket failed", e);
			}
			mmSocket = tmp;
			Log.e(TAG, "--------got socket--- ");
		}

		public void run() {
//			setName("ConnectThread");
			mAdapter.cancelDiscovery();
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				
				mmSocket.connect();
				connected=true;
				Log.e(TAG, "-------- socket connecting--- ");
			} catch (IOException e) {
				// Close the socket
				try {
					mmSocket.close();
					connectTime++;
					Log.e(TAG, "-------- socket closed--- ");
				} catch (IOException e2) {
					Log.e(TAG, "socket.close() exception-----------" + e2);
				}
			}
			if (!connected){
				if(!STOPFLAG){return;}
				connectionFailed();
				System.out.println("connectionFailed,,, then back to connectthread-------");
				return;
				}
			
			// Reset the ConnectThread because we're done
			synchronized (BluetoothService.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(mmSocket, mmDevice);
		}

		public  void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect----------> " + mSocketType
						+ " socket failed", e);
			}
		}
	}

	/**
	 * This thread runs during a connection with a remote device. It handles all
	 * incoming and outgoing transmissions.
	 */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;

		}

		/**
		 * @author lmt @ there is a bug with Android InputStream it is reported
		 *         as below: {@link http
		 *         ://stackoverflow.com/questions/12294705/error
		 *         -in-reading-data-
		 *         from-inputstream-in-bluetooth-on-android?rq=1}
		 */
		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[3];
			byte[] buffers = new byte[13];
			byte[] tempBuffer = new byte[100];
//			StringBuilder stringbuilder=new StringBuilder();
			int bytes=0;
			int index = 0;
			int sum = 0;
			
			STOPFLAG=true;
			connectTime=0;
			// Keep listening to the InputStream while connected
			while (true) {
				bytes=0;
				try {//while (true) {
					int flagcount=0;
//						bytes += mmInStream.read(buffer,bytes,12-bytes);
					bytes=mmInStream.read(buffer);
//					String a=Integer.toHexString(bytes);
					Log.d(TAG, "The first buffer.length is: "+bytes+".    buffer is: "
                            +RokolUtil.bytesToHexString(buffer, bytes));
						

						if (buffer[0] == (byte) 0xF1
								|| buffer[0] == (byte) 0xF5
								|| buffer[0] == (byte) 0xF2 || sum > 12) {
							index = 0;
							sum = 0;
							
						while (buffer[0] == (byte) 0xF1 && bytes<13){
							if(flagcount==0){
							System.arraycopy(buffer, 0, buffers, 0, bytes);
							flagcount++;
							}
							bytes+=mmInStream.read(buffers,bytes,13-bytes);
						} 
						
						if( buffer[0]==(byte)0xF2 || buffer[0]==(byte)0xF5){
							if( bytes<3)
							{
								Log.d(TAG, "------->bytes<3 read F2");
							 try{
								bytes+=mmInStream.read(buffer,bytes,3-bytes);
							    }
								catch(IOException e){
									break;
								}
							}
						 System.arraycopy(buffer, 0, buffers, 0, bytes);
						}
						
						
						
						Log.d(TAG, "---InStream.read() buffer is:> "+RokolUtil.bytesToHexString(buffers, bytes));
						
						byte[] arr = new byte[bytes];
							
						System.arraycopy(buffers, 0, arr, 0, bytes);
						for (int i = 0; i < bytes; i++) {
							if (i > 0
									&& (arr[i] == (byte) 0xF1
											|| arr[i] == (byte) 0xF2 || 
											arr[i] == (byte) 0xF5)) {
//								Log.d(TAG,
//										"break i ="
//												+ i
//												+ (i > 0 && (arr[i] == (byte) 0xF1
//														|| arr[i] == (byte) 0xF2 || arr[i] == (byte) 0xF5)));
								break;
							}
							tempBuffer[i + index] = arr[i];
							sum++;
						}
						//if (D)
//							Log.d(TAG, ""//mmInStream.available()
//									+ "  2  mminsream---" + index + "  " + sum
//									+ " (tempBuffer[0]==(byte)0xF1)="
//									+ (tempBuffer[0] == (byte) 0xF1)
//									+ " (tempBuffer[0]==(byte)0xF5)="
//									+ (tempBuffer[0] == (byte) 0xF5)
//									+ " (tempBuffer[0]==(byte)0xF2)="
//									+ (tempBuffer[0] == (byte) 0xF2) + " sum="
//									+ sum);
						index = index + bytes;
						if (sum == 13 && tempBuffer[0] == (byte) 0xF1) {

							// Send the obtained bytes to the UI Activity
							Bundle bundle = new Bundle();
							bundle.putInt(BUNDLE_TYPE,
									OperationActivityHD.MESSAGE_READ);
							bundle.putByteArray("buffer", tempBuffer);
							bundle.putInt("length", sum);
							showMessage(bundle);
							index = 0;
							sum = 0;
						}
						if (sum == 3
								&& (tempBuffer[0] == (byte) 0xF2 || tempBuffer[0] == (byte) 0xF5)) {
                            Log.d(TAG, "return data sum=3"+RokolUtil.bytesToHexString(tempBuffer, sum));
							// Send the obtained bytes to the UI Activity
							Bundle bundle = new Bundle();
							bundle.putInt(BUNDLE_TYPE,
									OperationActivityHD.MESSAGE_READ);
							bundle.putByteArray("buffer", tempBuffer);
							bundle.putInt("length", sum);
							showMessage(bundle);
							index = 0;
							sum = 0;
						}

					} /*
					 * else { SystemClock.sleep(100); }
					 */

				} catch (IOException e) {
					Log.e(TAG, "connectedthread---------disconnected", e);
					connectionLost();
					// Start the service over to restart listening mode
//					BluetoothService.this.start();  20150130   TRS
					break;
				}
			}
		}

		/**
		 * Write to the connected OutStream.
		 * 
		 * @param buffer
		 *            The bytes to write
		 */
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);

				// Share the sent message back to the UI Activity
				/*
				 * mHandler.obtainMessage(OperationActivityHD.MESSAGE_WRITE, -1, -1,
				 * buffer).sendToTarget();
				 */
				Bundle bundle = new Bundle();
				bundle.putInt(BUNDLE_TYPE, OperationActivityHD.MESSAGE_WRITE);
				bundle.putByteArray("buffer", buffer);
				System.out.println("mmoutStream.write---------------->" + bundle);
				showMessage(bundle);
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	private RokolApllication mApp;
	Handler TimerHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = new Bundle();
			bundle.putInt(BUNDLE_TYPE, OperationActivityHD.MESSAGE_REMAIN_TIME);
			bundle.putInt("RemainTime", mCurrentTimer);
			showMessage(bundle);
			TimerTick(true);
		}
	};
	Runnable TimerRunnable = new Runnable() {

		@Override
		public void run() {
			mCurrentTimer--;
			if (mCurrentTimer > 0) {
				Message msg = TimerHandler.obtainMessage();
				TimerHandler.sendMessage(msg);
				if (D)
					Log.d(TAG, mCurrentTimer + "");
			} else {
				TimerHandler.removeCallbacks(TimerRunnable);
			}
		}
	};
	// Timer
	private int mCurrentTimer;

	private void TimerTick(boolean start) {

		if (start)

			TimerHandler.postDelayed(TimerRunnable, 60 * 1000);
		else
			TimerHandler.removeCallbacks(TimerRunnable);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		STOPFLAG=true;serviceCreatFlag=true;

		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		mBluetoothReceiver = new BluetoothReceiver();
		mApp = new RokolApllication();
		IntentFilter filter = new IntentFilter();

		filter.addAction(OperationActivityHD.BLUETOOTHRECEIVER_ACTION);
		// register Broadcast Receiver
		registerReceiver(mBluetoothReceiver, filter);

		mTimerReceiver = new TimerReceiver();
		// register receiver
		IntentFilter counterActionFilter = new IntentFilter(
				"com.kangtai.MassageChair.StopTimerService");
		registerReceiver(mTimerReceiver, counterActionFilter);

		mCurrentTimer = (mApp.getCurrentTimer() * 10 + 10); // * 60 *1000;
		// 20 * 1000;//

		if (D)
			Log.d(TAG, "service--------the first setp---++onCreate() ++ ");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		STOPFLAG=false;
		// unregister receiver
		if (mTimerReceiver != null)
			unregisterReceiver(mTimerReceiver);
		if (mBluetoothReceiver != null)
			unregisterReceiver(mBluetoothReceiver);

		this.stop();
		if (D)
			Log.d(TAG, "++onDestroy() ++ ");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		 ADDRESS=intent.getStringExtra("address");
		 if(ADDRESS!=null&serviceCreatFlag){
		
		BluetoothDevice device=mAdapter.getRemoteDevice(ADDRESS);
		BluetoothService.this.connect(device);}
		Log.d(TAG, "-----server the second setp----onStartCommand ++ "+ADDRESS+serviceCreatFlag);
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null; 
	}

	public void showMessage(Bundle bundle) {// ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩœ¢
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setAction("android.intent.action.lxx");
		sendBroadcast(intent);

	}

	/**
	 * TimerService
	 * 
	 * @author
	 * 
	 */
	private class TimerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					"com.kangtai.MassageChair.StopTimerService")) {
				mCurrentTimer = intent.getIntExtra("time",
						mApp.getCurrentTimer()) * 10 + 10;
				boolean start = intent.getBooleanExtra("start", false);
				//send current time to activity  0318  TRS
//				Bundle bundle = new Bundle();
//				bundle.putInt(BUNDLE_TYPE, OperationActivityHD.MESSAGE_REMAIN_TIME);
//				bundle.putInt("RemainTime", mCurrentTimer);
//				showMessage(bundle);
				TimerTick(start);

//				if (D)
//					Log.d(TAG,
//							start
//									+ "   ====  "
//									+ RokolUtil.bytesToHexString(DataFrame
//											.getSendFrame(OPERATION.STOPTIMER),
//											3));

			}
		}

	}

	// listen to command from Activity
	private class BluetoothReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(TAG, "BluetoothReceiver onReceive");
			if (intent.getAction()
					.equals(OperationActivityHD.BLUETOOTHRECEIVER_ACTION)) {
				int cmd = intent.getIntExtra("cmd", -1);//

				switch (cmd) {
				case REQUIRE_CONNECT:
//					BluetoothService.addNewDevice=true;
					STOPFLAG=false;
					
					ADDRESS= intent
							.getStringExtra(OperationActivityHD.DEVICE_ADDRESS);
					BluetoothDevice device = mAdapter.getRemoteDevice(ADDRESS);
						Log.d(TAG, "Receiver get address----------->"+ "--"+ADDRESS );
					BluetoothService.this.connect(device);
					
					break;
				case REQUIRE_WRITE:
					final byte[] command = intent.getByteArrayExtra("command");
					// int value = intent.getIntExtra("value", 0);
                    
					write(command);
					if (D)
						Log.d(TAG, "∑¢ÀÕ÷∏¡Ó---------->" + RokolUtil.bytesToHexString(command, command.length));
					break;
				case 2:
					break;
				case 10:
					Log.d(TAG, "Close socket----------->" );
					break;
				}

			}
		}
	}

}
