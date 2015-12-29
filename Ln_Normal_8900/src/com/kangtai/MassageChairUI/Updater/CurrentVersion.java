package com.kangtai.MassageChairUI.Updater;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class CurrentVersion {
	//获取版本号
	public static int getVerCode(Context context) {  
	         int verCode = -1;  
	         try {  
	             verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;  
	         } catch (NameNotFoundException e) {  
	             e.printStackTrace();
	         }  
	         return verCode;  
	     }  
    //获取版本名
    public static String getVerName(Context context) {  
         String verName = "";  
         try {  
             verName = context.getPackageManager().getPackageInfo("com.nc", 0).versionName;  
         } catch (NameNotFoundException e) {  
           e.printStackTrace();
         }  
         return verName;     
     }       

}
