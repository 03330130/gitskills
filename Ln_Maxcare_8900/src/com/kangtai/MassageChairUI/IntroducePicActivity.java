package com.kangtai.MassageChairUI;

import com.kangtai.MassageChairUI.R;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class IntroducePicActivity extends Activity {
	
	private ImageView img_introduce;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_activity_main);
		img_introduce=(ImageView)findViewById(R.id.img_pic_introduce);
		Bundle bundle=getIntent().getExtras();
		int i=bundle.getInt("data");
		switch(i){
		case 1:
			Log.d("activityHD", "get bundle----data----stroy---");
			img_introduce.setImageResource(R.drawable.op_img_story);
			break;
		case 2:
			Log.d("activityHD", "get bundle----data----center---");
			img_introduce.setImageDrawable(getResources().getDrawable(R.drawable.op_img_center));
			break;
		case 3:
			Log.d("activityHD", "get bundle----data----health---");
			img_introduce.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.op_img_health));
			break;
		}
	}
}
