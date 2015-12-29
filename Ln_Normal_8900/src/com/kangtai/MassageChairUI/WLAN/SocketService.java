package com.kangtai.MassageChairUI.WLAN;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class SocketService {

	/* 服务器地�? */
	private final String SERVER_HOST_IP;

	/* 服务器端�? */
	private final int SERVER_HOST_PORT = 9500;
	private Thread mThread = null;
	private  String mStrMSG = "";
	private BufferedReader mBufferedReader = null;
	public SocketService(String server_host_ip) {
		SERVER_HOST_IP = server_host_ip;
		initClientSocket();
	}

	private Socket socket;
	private BufferedWriter bw;

	public void handleException(Exception e, String prefix) {
		e.printStackTrace();
		Log.d(prefix, e.getMessage());
	}

	private void initClientSocket() {
		new Thread() {
			public void run() {
				try {
					/* 连接服务�? */
					socket = new Socket(SERVER_HOST_IP, SERVER_HOST_PORT);
					Log.d("ip port", SERVER_HOST_IP+SERVER_HOST_PORT+socket.isConnected());
					/* 获取输出�? */
					
					 OutputStream os = socket.getOutputStream();
			            OutputStreamWriter osw = new OutputStreamWriter(os);
			             bw = new BufferedWriter(osw);
			             mBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			             mThread = new Thread(mRunnable); 
			             mThread.start();
					sendMessage("wlan test is ok , sent from android socket client!/r/n");

				} catch (UnknownHostException e) {
					handleException(e,
							"unknown host exception: " + e.toString());
				} catch (IOException e) {
					handleException(e, "io exception: " + e.toString());
				}catch(Exception e){
					handleException(e, "exception: " + e.toString());
				}
			}
		}.start();
	}
	// 线程:监听服务器发来的消息
	     private Runnable mRunnable = new Runnable()
	     {
	         public void run()
	         {
	             while (true)
	             {
	                 try
	                 {
	                     if ((mStrMSG = mBufferedReader.readLine()) != null)
	                     {                        
	                         mStrMSG += "\n";// 消息换行
	                      Log.d("read ", mStrMSG);
	                         //  mHandler.sendMessage(mHandler.obtainMessage());// 发�?�消�?
	                     }                    
	                 } catch (Exception e)
	                 {
	                     Log.e("read ", e.toString());
	                 }
	             }
	         }
	     };

	private void sendMessage(String msg) {
		
         try {
			bw.write(msg);
			 bw.flush();
			 Log.d("====", msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("=eee=", e.getMessage());
		}
        

		
	}

}
