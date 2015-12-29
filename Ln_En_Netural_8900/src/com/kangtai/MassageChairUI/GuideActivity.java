package com.kangtai.MassageChairUI;

	import java.util.ArrayList;

	import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

	public class GuideActivity  extends Activity {
		
		//定义ViewPager对象
		private ViewPager viewPager;
		private final static String tag="ViewPager";
		
		//定义ViewPager适配器
		private ViewPagerAdapter vpAdapter;
		
		//定义一个ArrayList来存放View
		private ArrayList<View> views;
		

		//引导图片资源
	    private static final int[] pics = {R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};
	    
	    //底部小点的图片
	    private ImageView[] points;
	    
	    //记录当前选中位置
	    private int currentIndex;
	    private Button btn;
	    private SharedPreferences sp;
	    

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_guide_main);
	
			initView();
			initData();
		}
		
		/**
		 * 初始化组件
		 */
		private void initView(){
			//实例化ArrayList对象
			views = new ArrayList<View>();
			
			//实例化ViewPager
			viewPager = (ViewPager) findViewById(R.id.viewpager);
			
			//实例化ViewPager适配器
			vpAdapter = new ViewPagerAdapter(views);
		}
		
		/**
		 * 初始化数据
		 */
		private void initData(){
	        LayoutInflater inflater = getLayoutInflater();
	        for(int i=0; i<pics.length; i++) {
	        	View v = inflater.inflate(R.layout.guide_item_view, null);
	            ImageView image = (ImageView)v.findViewById(R.id.image);
	            btn=(Button)v.findViewById(R.id.btn);
	            btn.setOnClickListener(new BtnListener());
	            image.setImageResource(pics[i]);
	            views.add(v);
	            
	        }
	        
	        //设置数据
	        viewPager.setAdapter(vpAdapter);
	        //设置监听
	        viewPager.setOnPageChangeListener(new pageListener());
	        
	        //初始化底部小点
	        initPoint();
		}
		
		/**
		 * 初始化底部小点
		 */
		private void initPoint(){
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);       
	        points = new ImageView[pics.length];
//	        points[0]=(ImageView)linearLayout.findViewById(R.id.img1);
	       

	        //循环取得小点图片
	        for (int i = 0; i < pics.length; i++) {
	        	//得到一个LinearLayout下面的每一个子元素
	        	points[i] = (ImageView) linearLayout.getChildAt(i);
	        	//默认都设为灰色
	        	points[i].setEnabled(true);
	        	if(points[i]==null)   Log.d(tag, ""+pics.length+points[0]+"=null-----");
//	        	//给每个小点设置监听
	        	points[i].setOnClickListener(new pointListener());
//	        	//设置位置tag，方便取出与当前位置对应
	        	points[i].setTag(i);
	        }
	        
	        //设置当面默认的位置
	        currentIndex = 0;
	        Log.d(tag, ""+pics.length+points[0]);
	        //设置为白色，即选中状态
	        points[currentIndex].setEnabled(false);
		}

		private class pageListener implements OnPageChangeListener{

			/**
			 * 当滑动状态改变时调用
			 */
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				 Log.d(tag,"=onPageScrollStateChanged-----"+arg0);
			}
			/**
			 * 当当前页面被滑动时调用
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				Log.d(tag,"=onPageScrolled-----"+arg0+"-"+arg1+"-"+arg2);
				if(arg0==2){btn.setVisibility(View.VISIBLE);}
				else btn.setVisibility(View.INVISIBLE);
			}
			/**
			 * 当新的页面被选中时调用
			 */
			@Override
			public void onPageSelected(int position) {
				// 设置底部小点选中状态
				setCurDot(position);
				Log.d(tag,"=onPageSelected-----"+position);
			}
			
		}
		
		private class pointListener implements OnClickListener{
			/**
			 * 通过点击事件来切换当前的页面
			 */
			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				setCurView(position);
				setCurDot(position);
				Log.d(tag,"=pointListener-----"+position);
			}
			
		}
		/**
		 * 设置当前页面的位置
		 */
		private void setCurView(int position) {
			Log.d(tag,"=setCurView-----"+position);
			if (position < 0 || position >= pics.length) {
				
				return;
			}
			viewPager.setCurrentItem(position);
		}
		
	    /**
	     * 设置当前的小点的位置
	     */
	    private void setCurDot(int positon){
	    	Log.d(tag,"=setCurDot-----"+positon);
	         if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
	        	 Log.d(tag,"=setCurDot-----return"+positon);
	             return;
	         }
	         points[positon].setEnabled(false);
	         points[currentIndex].setEnabled(true);

	         currentIndex = positon;
	     }
	    
	    class BtnListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 Log.d(tag,"btn onClick----");
			 Intent intent = new Intent();   
	         intent.setClass(getApplicationContext(),MainActivityHD.class);    
	         startActivity(intent);    
	         GuideActivity.this.finish();  
		}
	    }
	}

