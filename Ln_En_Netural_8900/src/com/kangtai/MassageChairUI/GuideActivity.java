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
		
		//����ViewPager����
		private ViewPager viewPager;
		private final static String tag="ViewPager";
		
		//����ViewPager������
		private ViewPagerAdapter vpAdapter;
		
		//����һ��ArrayList�����View
		private ArrayList<View> views;
		

		//����ͼƬ��Դ
	    private static final int[] pics = {R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};
	    
	    //�ײ�С���ͼƬ
	    private ImageView[] points;
	    
	    //��¼��ǰѡ��λ��
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
		 * ��ʼ�����
		 */
		private void initView(){
			//ʵ����ArrayList����
			views = new ArrayList<View>();
			
			//ʵ����ViewPager
			viewPager = (ViewPager) findViewById(R.id.viewpager);
			
			//ʵ����ViewPager������
			vpAdapter = new ViewPagerAdapter(views);
		}
		
		/**
		 * ��ʼ������
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
	        
	        //��������
	        viewPager.setAdapter(vpAdapter);
	        //���ü���
	        viewPager.setOnPageChangeListener(new pageListener());
	        
	        //��ʼ���ײ�С��
	        initPoint();
		}
		
		/**
		 * ��ʼ���ײ�С��
		 */
		private void initPoint(){
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);       
	        points = new ImageView[pics.length];
//	        points[0]=(ImageView)linearLayout.findViewById(R.id.img1);
	       

	        //ѭ��ȡ��С��ͼƬ
	        for (int i = 0; i < pics.length; i++) {
	        	//�õ�һ��LinearLayout�����ÿһ����Ԫ��
	        	points[i] = (ImageView) linearLayout.getChildAt(i);
	        	//Ĭ�϶���Ϊ��ɫ
	        	points[i].setEnabled(true);
	        	if(points[i]==null)   Log.d(tag, ""+pics.length+points[0]+"=null-----");
//	        	//��ÿ��С�����ü���
	        	points[i].setOnClickListener(new pointListener());
//	        	//����λ��tag������ȡ���뵱ǰλ�ö�Ӧ
	        	points[i].setTag(i);
	        }
	        
	        //���õ���Ĭ�ϵ�λ��
	        currentIndex = 0;
	        Log.d(tag, ""+pics.length+points[0]);
	        //����Ϊ��ɫ����ѡ��״̬
	        points[currentIndex].setEnabled(false);
		}

		private class pageListener implements OnPageChangeListener{

			/**
			 * ������״̬�ı�ʱ����
			 */
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				 Log.d(tag,"=onPageScrollStateChanged-----"+arg0);
			}
			/**
			 * ����ǰҳ�汻����ʱ����
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				Log.d(tag,"=onPageScrolled-----"+arg0+"-"+arg1+"-"+arg2);
				if(arg0==2){btn.setVisibility(View.VISIBLE);}
				else btn.setVisibility(View.INVISIBLE);
			}
			/**
			 * ���µ�ҳ�汻ѡ��ʱ����
			 */
			@Override
			public void onPageSelected(int position) {
				// ���õײ�С��ѡ��״̬
				setCurDot(position);
				Log.d(tag,"=onPageSelected-----"+position);
			}
			
		}
		
		private class pointListener implements OnClickListener{
			/**
			 * ͨ������¼����л���ǰ��ҳ��
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
		 * ���õ�ǰҳ���λ��
		 */
		private void setCurView(int position) {
			Log.d(tag,"=setCurView-----"+position);
			if (position < 0 || position >= pics.length) {
				
				return;
			}
			viewPager.setCurrentItem(position);
		}
		
	    /**
	     * ���õ�ǰ��С���λ��
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

