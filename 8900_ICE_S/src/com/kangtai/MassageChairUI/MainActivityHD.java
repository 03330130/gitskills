package com.kangtai.MassageChairUI;

import com.kangtai.MassageChairUI.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivityHD extends Activity implements OnTouchListener,OnClickListener,
		ViewFactory {
	private ImageView mTopView, mBottomView,mImgIntrduce;
	private Button mButtonServiceCenter, mButtonBrandStory, mButtonHealthCare,
			mButtonStart;
	private ImageSwitcher mImageSwitcher;
	private PopupWindow popupWindow;
	private int[] imgIds;
	/**
	 * 当前选中的图片id序号
	 */
	private int currentPosition = 1;
	/**
	 * 按下点的X坐标
	 */
	private float downX;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);
		//判断是否启动导航页面
		sp=getSharedPreferences("count", Context.MODE_PRIVATE);
		int count=sp.getInt("countt", 0);
		
		if(count==0){
//			 Log.d("activityHD","count==0----");
//			 Intent intent = new Intent();   
//	         intent.setClass(getApplicationContext(),GuideActivity.class);    
//	         startActivity(intent);    
//	         this.finish();  
		}
		Editor editor=sp.edit();
		editor.putInt("countt", ++count);
		Log.d("activityHD","count is----"+count);
		editor.commit();
		// 顶部，底部背景动画
		
		
		mTopView = (ImageView) findViewById(R.id.iv_top);
		mBottomView = (ImageView) findViewById(R.id.iv_bottom);
		Log.d("activityHD","topview is----"+mTopView+"----mBottomView"+mBottomView);
		// 动画效果参数直接定义
		Animation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(3000);

		// 动画效果从XMl文件中定义
		// Animation animation = AnimationUtils.loadAnimation(this,
		// R.anim.alpha);

		mTopView.setAnimation(animation);
		mBottomView.setAnimation(animation);

		// 中间椅子动画
		/*
		 * mLayoutChairs = (RelativeLayout) findViewById(R.id.layout_chairs);
		 * mLayoutChairs.setAnimation(AnimationUtils.loadAnimation(this,
		 * R.anim.main_chairs_top_in));
		 * mLayoutChairs.setVisibility(View.VISIBLE);
		 */
		// 底部按钮动画
		mButtonStart = (Button) findViewById(R.id.btn_start);
		mButtonStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(MainActivityHD.this, OperationActivityHD.class);
				startActivity(intent);
//				setContentView(R.layout.activity_main_drawerlayout);
				overridePendingTransition(R.anim.push_up_in,
						R.anim.push_up_out);
				MainActivityHD.this.finish();
			}
		});
		mButtonStart.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.main_btnstart_bottom_in));
		mButtonStart.setVisibility(View.VISIBLE);
		// 椅子图片切换
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.is_chairs);
		mImageSwitcher.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.main_chairs_top_in));
		mImageSwitcher.setVisibility(View.VISIBLE);
		mImageSwitcher.setFactory(this);
		// 设置OnTouchListener，我们通过Touch事件来切换图片
		mImageSwitcher.setOnTouchListener(this);

		imgIds = new int[] { R.drawable.main_chair_1, R.drawable.main_chair_2,
				R.drawable.main_chair_3, R.drawable.main_chair_4,
				R.drawable.main_chair_5 };

		mImageSwitcher.setImageResource(imgIds[currentPosition]);
		// 3 个 按钮
		Animation a = new TranslateAnimation(0.0f, 0.0f, 0.0f, -10.0f);
		a.setDuration(1500);
		// a.setStartOffset(300);
		a.setRepeatMode(Animation.RESTART);
		a.setRepeatCount(Animation.INFINITE);
		a.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.cycle_interpolator));
		mButtonServiceCenter = (Button) findViewById(R.id.btn_service_center);
		mButtonBrandStory = (Button) findViewById(R.id.btn_brand_story);
		mButtonHealthCare = (Button) findViewById(R.id.btn_health_care);
		mButtonBrandStory.setOnClickListener(this);
		mButtonHealthCare.setOnClickListener(this);
		mButtonServiceCenter.setOnClickListener(this);
		mButtonServiceCenter.startAnimation(a);
		mButtonBrandStory.startAnimation(a);
		mButtonHealthCare.startAnimation(a);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 手指按下的X坐标
			downX = event.getX();
			break;
		}
		case MotionEvent.ACTION_UP: {
			float lastX = event.getX();
			// 抬起的时候的X坐标大于按下的时候就显示上一张图片
			if (lastX > downX) {
				if (currentPosition > 0) {
					currentPosition--;

				} else {
					currentPosition = imgIds.length - 1;
					// Toast.makeText(getApplication(), "已经是第一张",
					// Toast.LENGTH_SHORT).show();
				}
				// 设置动画
				mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
						getApplication(), R.anim.main_chair_left_in));
				mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
						getApplication(), R.anim.main_chair_right_out));

				mImageSwitcher.setImageResource(imgIds[currentPosition
						% imgIds.length]);
			}

			if (lastX < downX) {
				if (currentPosition < imgIds.length - 1) {
					currentPosition++;
				} else {
					currentPosition = 1;
					// Toast.makeText(getApplication(), "到了最后一张",
					// Toast.LENGTH_SHORT).show();
				}
				mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
						getApplication(), R.anim.main_chair_right_in));
				mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
						getApplication(), R.anim.main_chair_left_out));

				mImageSwitcher.setImageResource(imgIds[currentPosition]);
			}
		}

			break;
		}

		return true;
	}

	@Override
	public View makeView() {
		final ImageView imageView = new ImageView(this);

		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return imageView;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		Uri uri;
		Bundle bundle=new Bundle();
		LayoutInflater inflater=getLayoutInflater();
		View popView=inflater.inflate(R.layout.pic_activity_main, null);
		mImgIntrduce=(ImageView)popView.findViewById(R.id.img_pic_introduce);
		switch(v.getId()){
		case R.id.btn_brand_story:
			Log.d("activityHD", "btn_brand_story----clicked-------");
			 uri = Uri.parse("http://m.rokol.cn"); 
			 intent = new Intent(Intent.ACTION_VIEW, uri); 
			startActivity(intent);
			popupWindow=new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.op_img_story));
//			PopupWindow.showAsDropDown(mButtonBrandStory,10,10);  	
			break;
		case R.id.btn_service_center:
			Log.d("activityHD", "btn_service_center----clicked-------");
			uri=Uri.parse("http://m.rokol.cn/richTextList_model344e0a7d2-3eef-475f-8ebd-54746b393f56.html?mtUUID=44e0a7d2-3eef-475f-8ebd-54746b393f56&layout=layoutrichlist3");
			intent=new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
		case R.id.btn_health_care:
			Log.d("activityHD", "btn_health_care----clicked-------");
			uri=Uri.parse("http://m.rokol.cn/richTextList_model31580718f-c218-4752-88e3-dd036d0c9be0.html?mtUUID=1580718f-c218-4752-88e3-dd036d0c9be0&layout=layoutrichlist3");
			intent=new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
			break;
		}
		
	}

}
