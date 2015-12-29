package com.kangtai.MassageChairUI.Receiver;


import com.kangtai.MassageChairUI.MainActivityHD;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 

 */
public class SwitchReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent it = new Intent(context,MainActivityHD.class);
		it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
//		new OperationActivityHD().finish();
	}

}
