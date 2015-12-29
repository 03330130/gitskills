package com.kangtai.MassageChairUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity.ListItem;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.kangtai.MassageChairUI.Protocal.DataFrame;
import com.kangtai.MassageChairUI.Protocal.DataFrame.OPERATION;
import com.kangtai.MassageChairUI.Protocal.FucUtil;
import com.kangtai.MassageChairUI.Protocal.JsonParser;
import com.kangtai.MassageChairUI.Protocal.RokolUtil;
import com.kangtai.MassageChairUI.Updater.UpdateManager;

public class OperationActivityHD extends FragmentActivity implements OnTouchListener,OnCheckedChangeListener
		,OnClickListener {
	private View mSuperView, mTopView, mBottomView,mLeftView,mRightView;
	private Button btnPower, btnStop;
	private static final int SNAP_SPEED=200;
	private int menuPadding=50;//useless
	private RelativeLayout.LayoutParams menuParams;
	private RelativeLayout.LayoutParams ButtonParams;
	private float xDown;
	private float xMove;
	private float xUp;
	private boolean isMenuVisible;
	private VelocityTracker mVelocityTracker ;
	private Button btn_Auto,btn_manual,btn_pressure,btn_other,btn_voice_control;
	private FrameLayout mAutoLayout,mManualLayout,mPressureLayout,mOtherLayout,
	        mManualModeLayout,mManualPartLayout,mManualWideLayout,mManualSpeedLayout,mOtherZuliaoSpeedLayout
	        ,mWideSpeedLayout,mNullLayout,mPressureSpeedsLayout,mPressureSpeed1,mPressureSpeed2,mPressureSpeed3,
	        mPressureSpeed4;
	private ListView mListView_auto,mListView_manual,mListView_manual_mode,
	                 mListView_pressure,mListView_other,mListView_left,mListView_mode,mListView_part;
	private ArrayList<ListItem> mList;  
	private DrawerLayout drawerLayout;
	private LinearLayout leftLayout,rightLayout,leftView_checkbox,ckb_other_llayout,m3D_layout;
	private ScrollView ckb_auto_scview;
	private TableLayout tableLayout;
	private FrameLayout containerFramelayout;
	private ArrayList<HashMap<String, Object>> manual_ListItem,left_listItem,
	        auto_ListItem,pressure_ListItem,other_ListItem,part_ListItem,mode_ListItem;
	//copy
		private static final String TAG = "Rokol Massage MainActivity";
		private static final String TAG2="activityHD";
		private static final boolean D = true;
		// Message types sent from the BluetoothService Handler
		public static final int MESSAGE_REMAIN_TIME = 0;
		public static final int MESSAGE_STATE_CHANGE = 1;
		public static final int MESSAGE_READ = 2;
		public static final int MESSAGE_WRITE = 3;
		public static final int MESSAGE_DEVICE_NAME = 4;
		public static final int MESSAGE_TOAST = 5;
		private static final int MESSAGE_POPWINDOW_CLOSE = 6;

		private static final int POPWINDOW_DELAYTIME = 5000;// position adjust
															// closed after 5s
		// Key names received from the BluetoothService Handler
		public static final String DEVICE_NAME = "device_name";
		public static final String DEVICE_ADDRESS = "device_address";
		public static final String TOAST = "toast";
		public static final String BLUETOOTHRECEIVER_ACTION = "com.kangtai.MassageChair.BluetoothReceiver";
		public static int mBluetoothServiceState;
		// Intent request codes
		private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
		private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
		private static final int REQUEST_ENABLE_BT = 3;

		private final int TIMEOUT = 5; // popwindows timeout 5ms
		private long exitTime = 0;// press back button 2 times to exit

		private StringBuffer mOutStringBuffer;
		private BluetoothAdapter mBluetoothAdapter = null;
		// Member object for the Bluetooth services
		private BluetoothService mBluetoothService = null;
		private BluetoothReceiver mBluetoothReceiver;
		private AlertDialog adjustShoulderDialog,powerOffDialog,dialog;
		private SoundPool soundpool;
		private HashMap<Integer,Integer> musicId;
		private SparseIntArray musicid;
		private PopupWindow popMenuWindow,popWindow,popWindow_mode,popupPartWindow,popWindow_Other,
		                    popSpeedWindow,popWidthWindow,pop3DWindow,popZuliaoWindow,popWindow_Auto,
		                    popPressureAllWindow,popPressureFootStrengthWindow,popPressureShoulderWindow,
		                    popPressureWaistWindow;
		private TextView tv_pop_width_item,tv_pop_speed_item,tv_pop_3d_up,tv_pop_3d_down,tv_speed1,tv_speed2,tv_speed3,tv_speed4,
		                 tv_pop_mode_rounie,tv_pop_mode_kouji,tv_pop_mode_paida,tv_pop_mode_roukou,
		                 tv_pop_mode_zhiya,tv_pressure_all_strength,tv_pressure_shoulder_strength,tv_zuliao_speed,
		                 tv_pressure_waist_strength,tv_pressure_foot_strength,tv_other_zuliao,tv_manual_wide,tv_manual_speed;
		private Drawable drawableWidth,drawableSpeed,drawablePressure_pop_item,drawableZuliao;
		private int ShoulderFlag=0,AutoShoulderFlag=0,mClickWidthCount=2,mClickSpeedCount=1,
				mClickStrengthCount=1;
		private VerticalSeekBar seekBar;
		private CheckBox ckb_up,ckb_all,ckb_part,ckb_fixed,ckb_home,ckb_language,ckb_timer,
		                 ckb_colorlight,ckb_sound,ckb_bt,ckb_update,ckb_zuliao,ckb_feet_heat,
		                 ckb_thermal,ckb_music,ckb_zyys,ckb_ksty,ckb_jgsz,ckb_gwdr,ckb_blsbz,
		                 ckb_sthh,ckb_xsbj,ckb_xyfs,ckb_ydyms,ckb_yjkb,ckb_ylam,ckb_ysbj,
		                 ckb_knead,ckb_tap,ckb_knock,ckb_roukou,ckb_shiatsu;
		private Button ckb_down;
		private ImageView mImageBackPosition,img,imgLeftDrawerBar,imgRightDrawerBar;
		private ImageView mImageBackdown,imgShoulderUp,imgShoulderDown;
		private ImageView mImageLegBend;
		private ImageView mImageLegStraighten;
		private ImageView mImageLegStretch;
		private ImageView mImageLegShrink;
		private ImageView mImageZeroGravity;
		private ImageView mImageSleep;

		private UpdateManager mUpdateManager;
		public static boolean mPower = false,mAirSpeed4=false,mAirSpeed2=false,mAirSpeed3=false,mAirSpeed1=false;;;
		private boolean mZeroGravity = false;
		private boolean mSleep = false,zuliaoshow,mManualListFlag=false,mModeListFlag=false,
				mPressureListFlag=false;
		private boolean zuliaoSpeedFase=false,zuliaoFastFlag =true, truezuliaoFlag=true,mPopwindow_clicked=false,
				thermalFeetFlag,cmdThermalFeetFlag=true,thermalHeartFlag=true,preHeatFlag=true,preHeatFeetFlag,
				heatBackFlag=false,touchSoundFlag=true,lightFlag=true,footSwingOn,zuliaoOn=false,
				mModeOn=false,widthAbleFlag=false,speedAbleFlag=false,threeDFlag=true,
						actionUp=false,mNewWidth=false,mNewSpeed=false;;
		private boolean mCommunicationFailed = false;
		private String[] mSendData;
		private String[] mReceiveData;
		private byte[] currentSendData;
		private int mCounter = 0,receivedataFlag=0,unkstyCount=0;
		private SharedPreferences sp,mSharedPreferences;
		private ActionBar actionBar;
		private ImageView imgFoot1,imgFoot2,imgLeg1,imgLeg2,imgSeat1,imgSeat2,imgSeat3,imgArm1,
		imgArm2,imgShoulder1,imgShoulder2,imgWaist11,imgWaist12,imgWasit21,imgWaist22,imgWasit31,
		imgWaist32,imgBack11,imgBack12,imgBack21,imgBack22,imgNeck1,imgNeck2,
		imgup,imgdown;
		private TextView txtTimeCount;
		private ImageView imgHeatFeet;
		private ImageView mImgWidth_State,mImgSpeed_State,mImgStrength_State,mImgPedicure_State;
		private PopupWindow popupWindowMode;
		private RokolApllication mApp;
		private MyCount countTime;
		private boolean kstyFlag=false,unkstyFlag=false;
		private SpeechRecognizer mIat;
		private RecognizerDialog isrDialog ;
		private String text = "";  
		private EditText editText;
		private Context context;
		//����ʶ��
		private String mCloudGrammar=null;
		// �����﷨�ļ�
	    private String mLocalGrammar = null;
		private SpeechRecognizer mAsr;
		private static final String KEY_GRAMMAR_ABNF_ID = "grammar_abnf_id";
		int ret = 0;// �������÷���ֵ
		private String mEngineType = SpeechConstant.TYPE_LOCAL;
		private  View bottomView,view1;
		


	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp=(RokolApllication)this.getApplication();
		Log.d("activityHD", "onCreate-----------()");
		context=getApplicationContext();

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		LayoutInflater inflater=LayoutInflater.from(this);
		 view1=inflater.inflate(R.layout.operation_layout, null);
		bottomView=inflater.inflate(R.layout.op_bottom_view, null);
		setContentView(R.layout.activity_main_drawerlayout);
		
		containerFramelayout=(FrameLayout)findViewById(R.id.container);
		containerFramelayout.addView(view1);
		
//		containerFramelayout
		//��ʼ������ ������д
//		 SpeechUtility.createUtility(OperationActivityHD.this, SpeechConstant.APPID +"=558cc56a");   
//         mIat= SpeechRecognizer.createRecognizer(OperationActivityHD.this, null);
//          isrDialog = new RecognizerDialog(  
//        		  OperationActivityHD.this,minitListener);  
//        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
		
		
		//��������ʶ��
		mSharedPreferences=getSharedPreferences(getPackageName(), MODE_PRIVATE);// ���� ����ʹ��
		//1.��ʼ�� 
		SpeechUtility.createUtility(OperationActivityHD.this, SpeechConstant.APPID +"=558cc56a"); 
		//����SpeechRecognizer����
		mAsr = SpeechRecognizer.createRecognizer(context, minitListener);
		mCloudGrammar = FucUtil.readFile(this,"grammar_sample.abnf","utf-8");
		mLocalGrammar=FucUtil.readFile(this,"order.bnf","utf-8");
        
		
		//include ckb_auto scrollview
		initAutoOtherCKB();
		
        editText=(EditText)findViewById(R.id.edtxt_show_words);
        btn_voice_control=(Button)findViewById(R.id.btn_voice_control);
        
		mTopView = findViewById(R.id.view_top);
		mBottomView = findViewById(R.id.view_bottom);
		btn_Auto=(Button) findViewById(R.id.btn_op_auto);
		btn_manual=(Button)findViewById(R.id.btn_op_manual);
		btn_pressure=(Button)findViewById(R.id.btn_op_pressure);
		btn_other=(Button)findViewById(R.id.btn_op_other);
		
		tableLayout=(TableLayout)findViewById(R.id.table_layout);
		mAutoLayout=(FrameLayout)findViewById(R.id.auto_layout);
		mManualLayout=(FrameLayout)findViewById(R.id.manual_layout);
		mManualModeLayout=(FrameLayout)findViewById(R.id.manual_mode_layout);
		mManualPartLayout=(FrameLayout)findViewById(R.id.manual_part_layout);
		mWideSpeedLayout=(FrameLayout)findViewById(R.id.bottom_wide_speed_layout);
		mManualWideLayout=(FrameLayout)findViewById(R.id.width_layout);
		mManualSpeedLayout=(FrameLayout)findViewById(R.id.speed_layout);
		m3D_layout=(LinearLayout)findViewById(R.id.manual_3d_layout);
		mOtherZuliaoSpeedLayout=(FrameLayout)findViewById(R.id.zuliao_speed_layout);
		mNullLayout=(FrameLayout)findViewById(R.id.null_layout);
		
		//pressure speed layout 2015.10.06
		mPressureSpeedsLayout=(FrameLayout)findViewById(R.id.pressrue_speeds_layout);
		mPressureSpeed1=(FrameLayout)findViewById(R.id.pressure_speed_layout1);
		mPressureSpeed2=(FrameLayout)findViewById(R.id.pressure_speed_layout2);
		mPressureSpeed3=(FrameLayout)findViewById(R.id.pressure_speed_layout3);
		mPressureSpeed4=(FrameLayout)findViewById(R.id.pressure_speed_layout4);
		
		
		mPressureLayout=(FrameLayout)findViewById(R.id.pressure_layout);
		mOtherLayout=(FrameLayout)findViewById(R.id.other_layout);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		leftLayout=(LinearLayout) findViewById(R.id.left);
		rightLayout=(LinearLayout) findViewById(R.id.op_right_view);
//		leftView_checkbox=(LinearLayout)findViewById(R.id.op_leftview_checkbox);
		//right view ini
//		seekBar=(VerticalSeekBar)findViewById(R.id.img_back_up_down);
		mImageBackPosition = (ImageView) findViewById(R.id.img_back_positon);
		mImageSleep = (ImageView) findViewById(R.id.img_sleep_position);
		mImageZeroGravity = (ImageView)findViewById(R.id.img_zero_gravity);
		mImageBackdown = (ImageView) findViewById(R.id.img_back_down);
		mImageLegBend = (ImageView) findViewById(R.id.img_foot_down);
		mImageLegStraighten = (ImageView) findViewById(R.id.img_foot_position);
		mImageLegStretch = (ImageView) findViewById(R.id.img_foot_shen);
		mImageLegShrink = (ImageView) findViewById(R.id.img_foot_stretch);
		//
		imgRightDrawerBar=(ImageView)findViewById(R.id.img_right_drawer_bar);
		imgLeftDrawerBar=(ImageView)findViewById(R.id.img_left_drawer_bar);
		imgRightDrawerBar.setOnClickListener(this);
		imgLeftDrawerBar.setOnClickListener(this);
		drawerLayout.setDrawerListener(new DrawerListener(){

			@Override
			public void onDrawerClosed(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG2, "-----"+arg0.getId());
				arg0.setVisibility(View.VISIBLE);
			}

			@Override
			public void onDrawerOpened(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG2, "-----"+arg0.getId());
//				mViewCome.run();
			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				// TODO Auto-generated method stub
				Log.d(TAG2, "onDrawerSlide-----"+arg1+"---arg0.id=="+arg0.getId());
				if(arg0.getId()==R.id.img_right_drawer_bar){
				if(arg1>0.05){imgRightDrawerBar.setVisibility(View.INVISIBLE);}
				if(arg1<0.01){imgRightDrawerBar.setVisibility(View.VISIBLE);}
				Log.d(TAG2, "---right Slide-----");
				}
			}

			@Override
			public void onDrawerStateChanged(int arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG2, "onDrawerStateChanged-----"+arg0);
			}});
		//left view check box
		ckb_home=(CheckBox)findViewById(R.id.ckb_leftview_home);
		ckb_language=(CheckBox)findViewById(R.id.ckb_leftview_language);
		ckb_timer=(CheckBox)findViewById(R.id.ckb_leftview_timer);
		ckb_colorlight=(CheckBox)findViewById(R.id.ckb_leftview_colorlight);
		ckb_sound=(CheckBox)findViewById(R.id.ckb_leftview_sound);
		ckb_bt=(CheckBox)findViewById(R.id.ckb_leftview_bluetooth);
		ckb_update=(CheckBox)findViewById(R.id.ckb_leftview_update);
		
		
		btn_voice_control.setOnClickListener(this);
		ckb_home.setOnCheckedChangeListener(this);
		ckb_language.setOnCheckedChangeListener(this);
		ckb_timer.setOnCheckedChangeListener(this);
		ckb_colorlight.setOnCheckedChangeListener(this);
		ckb_sound.setOnCheckedChangeListener(this);
		ckb_bt.setOnCheckedChangeListener(this);
		ckb_update.setOnCheckedChangeListener(this);
		ckb_sound.setChecked(mApp.isSoundSwitchOn());
		if (mApp.isSoundSwitchOn()) {
			RokolUtil.setTextShadow(getBaseContext(), ckb_sound);
		} else {
			RokolUtil.setTextNoShadow(getBaseContext(), ckb_sound);
		}
//		
		RokolUtil.setTextShadow(getBaseContext(), ckb_colorlight);

		mImageLegBend.setOnTouchListener(new TouchListener());
		mImageLegStraighten.setOnTouchListener(new TouchListener());
		
		mImageLegStretch.setOnTouchListener(new TouchListener());
		mImageLegShrink.setOnTouchListener(new TouchListener());
		
		mImageBackPosition.setOnTouchListener(new TouchListener());
		mImageBackdown.setOnTouchListener(new TouchListener());
		
		mImageZeroGravity.setOnClickListener(this);
		mImageSleep.setOnClickListener(this);

		
		//state init
		txtTimeCount=(TextView)view1.findViewById(R.id.tv_time_count);
		mImgWidth_State=(ImageView)view1.findViewById(R.id.img_width_state);
		mImgSpeed_State=(ImageView)view1.findViewById(R.id.img_speed_state);
		mImgPedicure_State=(ImageView)view1.findViewById(R.id.img_pedicure_state);
		mImgStrength_State=(ImageView)view1.findViewById(R.id.img_strength_state);
		imgFoot1 = (ImageView) view1.findViewById(R.id.op_img_foot1);
		imgFoot2 = (ImageView) view1.findViewById(R.id.op_img_foot2);
		imgLeg1 = (ImageView) view1.findViewById(R.id.op_img_leg1);
		imgLeg2 = (ImageView) view1.findViewById(R.id.op_img_leg2);
		imgSeat1 = (ImageView) view1.findViewById(R.id.op_img_seat1);
		imgSeat2 = (ImageView) view1.findViewById(R.id.op_img_seat2);
		//display icon
        imgHeatFeet=(ImageView)view1.findViewById(R.id.img_heat_feet);
		imgArm1 = (ImageView) view1.findViewById(R.id.op_img_arm1);
		
		imgArm2 = (ImageView)view1.findViewById(R.id.op_img_arm2);
		imgShoulder1 = (ImageView) view1.findViewById(R.id.op_img_shoulder1);
		imgShoulder2 = (ImageView) view1.findViewById(R.id.op_img_shoulder2);
		imgSeat3 = (ImageView) view1.findViewById(R.id.op_img_seat3);
		imgWaist11 = (ImageView) view1.findViewById(R.id.op_img_waist11);
		imgWaist12 = (ImageView) view1.findViewById(R.id.op_img_waist12);
		imgWasit21 = (ImageView)view1.findViewById(R.id.op_img_waist21);
		imgWaist22 = (ImageView) view1.findViewById(R.id.op_img_waist22);
		imgWasit31 = (ImageView) view1.findViewById(R.id.op_img_waist31);
		imgWaist32 = (ImageView) view1.findViewById(R.id.op_img_waist32);
		imgBack11 = (ImageView) view1.findViewById(R.id.op_img_back11);
		imgBack12 = (ImageView) view1.findViewById(R.id.op_img_back12);
		imgBack21 = (ImageView) view1.findViewById(R.id.op_img_back21);
		imgBack22 = (ImageView) view1.findViewById(R.id.op_img_back22);
		imgNeck1 = (ImageView) view1.findViewById(R.id.op_img_neck1);
		imgNeck2 = (ImageView) view1.findViewById(R.id.op_img_neck2);

		mListView_auto=(ListView)findViewById(R.id.lv_auto);
		mListView_manual=(ListView)findViewById(R.id.lv_manual);
		mListView_pressure=(ListView)findViewById(R.id.lv_pressure);
//		mListView_other=(ListView)findViewById(R.id.lv_other);
		mListView_manual_mode=(ListView)findViewById(R.id.lv_mode);
//		initManualModeList();
		tv_manual_wide=(TextView)findViewById(R.id.tv_manual_width);
		tv_manual_speed=(TextView)findViewById(R.id.tv_manual_speed);
		
		
        mList = new ArrayList<ListItem>(); 
        //set bottom menu list
//        setAutoList();
//        setOtherList();
//        setLeftListView();
        
		
		mSuperView = findViewById(R.id.layout_super);
		btnPower = (Button) findViewById(R.id.btn_op_power);
		btnStop = (Button) findViewById(R.id.btn_op_stop);
		

		btnPower.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		mSuperView.setOnTouchListener(this);
		
		btn_Auto.setOnClickListener(this);
		btn_manual.setOnClickListener(this);
		btn_pressure.setOnClickListener(this);
		btn_other.setOnClickListener(this);
		initValues();
		initMusicId();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
	}
	public void onStart() {
		super.onStart();
			Log.d("activityHD", "++ ON START ++mBluetoothServiceState="+mBluetoothServiceState);

		// If BT is not on, request that it be enabled.
		// setupService() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the session
		} 
		else if(mBluetoothServiceState==BluetoothService.STATE_CONNECTED){
//			mPower=true;
			ckb_bt.setSelected(true);
			changePowerState();
			mBluetoothService=new BluetoothService();
			mOutStringBuffer = new StringBuffer("");
			mReceiveData = new String[] { "" };
			sp = OperationActivityHD.this.getSharedPreferences("Device", MODE_PRIVATE);
			touchSoundFlag =mApp.isSoundSwitchOn();
		    Log.d("activityHD", "BluetoothServiceState --is 3 connected---");
	      }
		
		else {
			if (mBluetoothReceiver == null)
				
				setupService();
		}
	}

	@Override
	public synchronized void onResume() {
			super.onResume();
			if(mBluetoothReceiver==null){
			mBluetoothReceiver = new BluetoothReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.intent.action.lxx");
			OperationActivityHD.this.registerReceiver(mBluetoothReceiver, filter);
			Log.d("activityHD", "MainActivity---------->" + "ON RESUME + register Receiver success");}
				
	   }
	private void startConectPairedDevice() {
		// Launch the DeviceListActivity to see devices and do scan
		Intent serverIntent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d("activityHD", "onPause-----------()");
//	
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("activityHD", "onDestroy-----------()");
		unregisterReceiver(mBluetoothReceiver);
		// �˳�ʱ�ͷ�����
		
		mAsr.cancel();
		mAsr.destroy();
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d("activityHD", "onStop-------------()");
//		closeSocket();
		super.onStop();
	}
	private void setupService() {
		Log.d("activityHD", "setupService()");
		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
		mReceiveData = new String[] { "" };
		sp = OperationActivityHD.this.getSharedPreferences("Device", MODE_PRIVATE);
		// Get the device MAC address
		String address = sp.getString("Address", "");
		touchSoundFlag =mApp.isSoundSwitchOn();
		if (address.equals(""))
			{Intent it = new Intent(OperationActivityHD.this, BluetoothService.class);
		    startService(it);//20150130           TRS
			startConectPairedDevice();}
		else {
			Intent it = new Intent(OperationActivityHD.this, BluetoothService.class);
			it.putExtra("address", address);
			startService(it);//20150130  TRS
			System.out.println("sendBroadcast----->"+address);
		}

	}
	//����ʶ���﷨����
	private void initGrammar(){
		if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)){
			mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
			mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
			ret = mAsr.buildGrammar("bnf", mLocalGrammar , localGrammarListener);
					if (ret != ErrorCode.SUCCESS){
					Log.d(TAG,"�﷨����ʧ��,�����룺 " + ret);
					}else{
					Log.d(TAG,"�﷨�����ɹ�");
				         }
	         }else{//����
	        	 mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
	        	 mAsr.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");
				    ret = mAsr.buildGrammar("abnf", mCloudGrammar, grammarListener);
					if(ret != ErrorCode.SUCCESS)
						Log.d(TAG,"�﷨����ʧ��,�����룺" + ret);
	         }
			
	}
	//���ù����﷨����Ϊ����
	private GrammarListener localGrammarListener = new GrammarListener() {
		@Override
		public void onBuildFinish(String grammarId, SpeechError error) {
			if(error == null){
				Log.d(TAG,"localGrammarListener�����ɹ���" + grammarId);
			}else{
				Log.d(TAG,"localGrammarListener����ʧ��,�����룺" + error.getErrorCode());
			}			
		}
	};
   //��ʼʶ��,������������Ϊ�ƶ�
	private GrammarListener grammarListener=new GrammarListener(){

		@Override
		public void onBuildFinish(String grammarId, SpeechError error) {
			// TODO Auto-generated method stub
			if(error == null){
				String grammarID = new String(grammarId);
				Editor editor = mSharedPreferences.edit();
				if(!TextUtils.isEmpty(grammarId))
					editor.putString(KEY_GRAMMAR_ABNF_ID, grammarID);
				editor.commit();
				Log.d(TAG,"�ƶ��﷨�����ɹ���" + grammarId);
			}else{
				Log.d(TAG,"�ƶ��﷨����ʧ��,�����룺" + error.getErrorCode());
			}			
		}};
	 private InitListener minitListener=new InitListener(){

			@Override
			public void onInit(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == ErrorCode.SUCCESS) {}
				Log.d("activityHD","InitListener aro="+arg0);
			}};
	public boolean setParam(){
				boolean result = false;
				//����ʶ������   ����ʶ��
				mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
				//���÷��ؽ��Ϊjson��ʽ
				if(SpeechConstant.TYPE_CLOUD.equalsIgnoreCase(mEngineType))
				{
				    mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");
					String grammarId = mSharedPreferences.getString(KEY_GRAMMAR_ABNF_ID, null);
					if(TextUtils.isEmpty(grammarId))
					{
						result =  false;
					}else {
						//�����ƶ�ʶ��ʹ�õ��﷨id
						mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
						result =  true;
						 }
				}else{
					//���÷���ֵ���ݸ�ʽ
					mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");
					//���ñ���ʶ��ʹ���﷨id
					mAsr.setParameter(SpeechConstant.LOCAL_GRAMMAR, "order");
					//���ñ���ʶ�������ֵ
					mAsr.setParameter(SpeechConstant.ASR_THRESHOLD, "30");
					Log.d(TAG,"�﷨���ع����ɹ� " );	
					result = true;
				}
					
				
				return result;
			}
	private RecognizerListener mRecoListener=new RecognizerListener() {
		
		//��д����ص��ӿ� (����Json��ʽ������û��ɲμ���¼12.1)��
		  //һ������»�ͨ��onResults�ӿڶ�η��ؽ����������ʶ�������Ƕ�ν�����ۼӣ�
		  //���ڽ���Json�Ĵ���ɲμ�MscDemo��JsonParser�ࣻ
		  //isLast����trueʱ�Ự������
		  public void onResult(RecognizerResult results, boolean isLast) {
			  text+=JsonParser.parseIatResult(results.getResultString ());
		  Log.d("activityHD","onResult"+isLast+JsonParser.parseIatResult(results.getResultString ()));
		  editText.setText(text);
		  
		  if(text.contains("�������")){
			  Log.d("activityHD","text == �������");
			  sendCommand(DataFrame.getSendFrame(OPERATION.SHOPPER_ON));
			  
			  }
		  else if(text.contains("��ҽ����")){
			  Log.d("activityHD","text == ��ҽ����");
			  sendCommand(DataFrame.getSendFrame(OPERATION.PAINEASE_ON));
		
		  }
		  else if(text.contains("��������")){
			  Log.d("activityHD","text == ��������");
			  sendCommand(DataFrame.getSendFrame(OPERATION._3DEXPERIENCE_ON));
			  
		  }else if(text.contains("����")|text.contains("�򿪰�Ħ��")){
			  Log.d("activityHD","text == ����");
			  sendCommand(DataFrame.getSendFrame(OPERATION.POWER_ON));
	      }
		  else if(text.contains("�ػ�")|text.contains("�ر�")){
			  Log.d("activityHD","text == �ػ�");
			  sendCommand(DataFrame.getSendFrame(OPERATION.POWER_OFF));
		  }
//		  if(isLast){
////				setParam();
//				mIat.startListening(mRecoListener);
////				setIsRunning(true);
//			}
		  }
		  //�Ự��������ص��ӿ�
		  public void onError(SpeechError error) {
		  error.getPlainDescription(true);} //��ȡ����������}
		  //��ʼ¼��
		  public void onBeginOfSpeech() {
			  Log.d("activityHD","begin speech"+"");
			  editText.setText("");
			  text="";
		  }
		  //����ֵ0~30
		  public void onVolumeChanged(int volume){
			  Log.d("activityHD","on Volume"+"");
		  }
		  //����¼��
		  public void onEndOfSpeech() {
			  Log.d("activityHD","end speech"+"");
		  }
		  //��չ�ýӿ�
		  public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}
		  };	
private RecognizerDialogListener mRecoDialogListener=new RecognizerDialogListener() {
				@Override
				public void onResult(RecognizerResult results, boolean arg1) {
					// TODO Auto-generated method stub
					  text+=JsonParser.parseIatResult(results.getResultString ());
					  Log.d("activityHD",JsonParser.parseIatResult(results.getResultString ()));
					  editText.setText(text);text+="####";
					  Log.d("activityHD","text.substring(0,2)"+text);
					  if(text.contains("�������")){
						  Log.d("activityHD","text == �������");
						  sendCommand(DataFrame.getSendFrame(OPERATION.SHOPPER_ON));
//						  setAutoGwdr(checked);
						  }
					  else if(text.contains("��ҽ����")){
						  Log.d("activityHD","text == ��ҽ����");
						  sendCommand(DataFrame.getSendFrame(OPERATION.PAINEASE_ON));
//						  setAutoSthh(checked);
					  }
					  else if(text.contains("��������")){
						  Log.d("activityHD","text == ��������");
						  sendCommand(DataFrame.getSendFrame(OPERATION._3DEXPERIENCE_ON));
//						  setAutoKsty(checked);
					  }else if(text.contains("����")|text.contains("�򿪰�Ħ��")){
						  Log.d("activityHD","text == ����");
						  sendCommand(DataFrame.getSendFrame(OPERATION.POWER_ON));
				      }
					  else if(text.contains("�ػ�")|text.contains("�ر�")){
						  Log.d("activityHD","text == �ػ�");
						  sendCommand(DataFrame.getSendFrame(OPERATION.POWER_OFF));
					  }
				}
				@Override
				public void onError(SpeechError arg0) {
					// TODO Auto-generated method stub
					Log.d("activityHD","recognizer diolag"+arg0);
				}
			};
			
	private void tellNetStatus(){
				if (!RokolUtil.checkNetWorkStatus(getBaseContext())) {
					mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
					if(!SpeechUtility.getUtility().checkServiceInstalled ()){  
					    String url = SpeechUtility.getUtility().getComponentUrl();  
					    Uri uri = Uri.parse(url);  
					    Log.d("activityHD","-----mInstaller.install();---" );
					    Intent it = new Intent(Intent.ACTION_VIEW, uri);  
					    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    context.startActivity(it);  
					}   
					 else {
							String result = FucUtil.checkLocalResource();
							if (!TextUtils.isEmpty(result)) {
								Log.d("activityHD","-----!TextUtils.isEmpty(result---" +result);
								editText.setText(result);
							}
						}
					}
			}
	private void initAutoOtherCKB(){
		ckb_auto_scview=(ScrollView)findViewById(R.id.ckb_auto);
		ckb_other_llayout=(LinearLayout)findViewById(R.id.ckb_other);
		
		ckb_zyys=(CheckBox)findViewById(R.id.ckb_zyys);
		ckb_yjkb=(CheckBox)findViewById(R.id.ckb_yjkb);
		ckb_xsbj=(CheckBox)findViewById(R.id.ckb_xfbj);
		ckb_ydyms=(CheckBox)findViewById(R.id.ckb_ydyms);
		ckb_jgsz=(CheckBox)findViewById(R.id.ckb_jgsz);
		ckb_xyfs=(CheckBox)findViewById(R.id.ckb_xyfs);
		ckb_gwdr=(CheckBox)findViewById(R.id.ckb_gwdr);
		ckb_blsbz=(CheckBox)findViewById(R.id.ckb_blsbz);
		ckb_ksty=(CheckBox)findViewById(R.id.ckb_ksty);
		ckb_ylam=(CheckBox)findViewById(R.id.ckb_ylam);
		ckb_ysbj=(CheckBox)findViewById(R.id.ckb_ysbj);
		ckb_sthh=(CheckBox)findViewById(R.id.ckb_sthh);
		//other ckb
		ckb_zuliao=(CheckBox)findViewById(R.id.ckb_other_zuliao);
		ckb_feet_heat=(CheckBox)findViewById(R.id.ckb_other_feet_thermal);
		ckb_thermal=(CheckBox)findViewById(R.id.ckb_other_thermal);
		ckb_music=(CheckBox)findViewById(R.id.ckb_other_music);
		ckb_zuliao.setOnCheckedChangeListener(this);
		ckb_feet_heat.setOnCheckedChangeListener(this);
		ckb_thermal.setOnCheckedChangeListener(this);
		ckb_music.setOnCheckedChangeListener(this);
		//part
		ckb_all=(CheckBox)findViewById(R.id.ckb_part_all);
		ckb_part=(CheckBox)findViewById(R.id.ckb_part_part);
		ckb_fixed=(CheckBox)findViewById(R.id.ckb_part_fixed);
		
		ckb_sthh.setOnCheckedChangeListener(this);
		ckb_xsbj.setOnCheckedChangeListener(this);
		ckb_yjkb.setOnCheckedChangeListener(this);
		ckb_zyys.setOnCheckedChangeListener(this);
		ckb_ydyms.setOnCheckedChangeListener(this);
		ckb_jgsz.setOnCheckedChangeListener(this);
		ckb_xyfs.setOnCheckedChangeListener(this);
		ckb_gwdr.setOnCheckedChangeListener(this);
		ckb_blsbz.setOnCheckedChangeListener(this);
		ckb_ysbj.setOnCheckedChangeListener(this);
		ckb_ksty.setOnCheckedChangeListener(this);
		ckb_ylam.setOnCheckedChangeListener(this);
	}
	private void initManualModeList(){
		if(!mModeListFlag){
			mModeListFlag=true;
		mode_ListItem=new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map=new HashMap<String,Object>();
		map.put("ItemImage",R.drawable.op_img_rounie_normal);
		map.put("ItemTitle", getResources().getString(R.string.knead));
		mode_ListItem.add(map);
		map=new HashMap<String,Object>();
		map.put("ItemImage",R.drawable.op_img_kouji_normal);
		map.put("ItemTitle", getResources().getString(R.string.rap));
		mode_ListItem.add(map);
		map=new HashMap<String,Object>();
		map.put("ItemImage",R.drawable.op_img_paida_normal);
		map.put("ItemTitle", getResources().getString(R.string.pat));
		mode_ListItem.add(map);
		map=new HashMap<String,Object>();
		map.put("ItemImage",R.drawable.op_img_roukou_normal);
		map.put("ItemTitle", getResources().getString(R.string.knead_knock));
		mode_ListItem.add(map);
		map=new HashMap<String,Object>();
		map.put("ItemImage",R.drawable.op_img_zhiya_normal);
		map.put("ItemTitle", getResources().getString(R.string.finger));
		mode_ListItem.add(map);
		
		SimpleAdapter adapter=new SimpleAdapter(this, mode_ListItem, R.layout.op_mode_list_item,
								new String[]{"ItemImage","ItemTitle"}, 
								new int[]{R.id.img_mode_item,R.id.tv_mode_item});
		mListView_manual_mode.setAdapter(adapter);
		}
		mListView_manual_mode.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				   if (((ListView)arg0).getTag() != null){

	                    ((View)((ListView)arg0).getTag()).setBackgroundDrawable(null);
	            }
	            ((ListView)arg0).setTag(arg1);
	            arg1.setBackgroundResource(R.drawable.bk_auto_list);
				if(mode_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.knead)))
                { 
					 sendCommand(DataFrame.getSendFrame(OPERATION.BACK_KNEADING_ON));
					 ckb_all.setChecked(true);mNewWidth=true;mNewSpeed=true;
						widthAbleFlag=false;
						speedAbleFlag=true;
						threeDFlag=false;
						mModeOn=true;
				
                }
				if(mode_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.rap)))
                {
					sendCommand(DataFrame.getSendFrame(OPERATION.BACK_TAPPING_ON));
					ckb_all.setChecked(true);mNewWidth=true;mNewSpeed=true;
					widthAbleFlag=true;
					speedAbleFlag=true;
					threeDFlag=false;
					mModeOn=true;
                }
				if(mode_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.pat)))
                {ckb_all.setChecked(true);mNewWidth=true;mNewSpeed=true;
					sendCommand(DataFrame.getSendFrame(OPERATION.BACK_CLAPPING_ON));
					widthAbleFlag=true;
					speedAbleFlag=true;
					threeDFlag=false;
					mModeOn=true;
                }
				if(mode_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.knead_knock)))
                {ckb_all.setChecked(true);mNewWidth=true;mNewSpeed=true;
					sendCommand(DataFrame.getSendFrame(OPERATION.BACK_KNEADING_TAPPING_ON));
							
					widthAbleFlag=false;
					speedAbleFlag=true;
					mModeOn=true;
					threeDFlag=false;
//					 setPartLvImg(mListView_mode,arg2, R.drawable.op_img_roukou_active);
                }
				if(mode_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.finger)))
                {
					sendCommand(DataFrame.getSendFrame(OPERATION.BACK_FINGER_ON));
					ckb_all.setChecked(true);mNewWidth=true;mNewSpeed=true;
					widthAbleFlag=true;
					speedAbleFlag=false;
					mModeOn=true;
					threeDFlag=true;
//					setPartLvImg(mListView_mode,arg2, R.drawable.op_img_zhiya_active);
                }
				RokolUtil.performTouchSound(getBaseContext());
				setCountTime();
			}
		});
	}
	
	private void initValues(){
		DisplayMetrics mMetrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
//		screenWidth=mMetrics.widthPixels;
//		menuParams=(RelativeLayout.LayoutParams)mLeftView.getLayoutParams();
//		ButtonParams=(RelativeLayout.LayoutParams)mBottomView.getLayoutParams();
//		menuParams.width=screenWidth-menuPadding;
//		leftEdge=-menuParams.width;
//		menuParams.leftMargin=leftEdge;
	}
	private void initMusicId(){
		soundpool=new SoundPool(12,3,0);
		musicid=new SparseIntArray();
		musicid.put(1, soundpool.load(this, R.raw.auto_detection, 1));
		musicid.put(2, soundpool.load(this, R.raw.jiangao, 1));
		musicId=new HashMap<Integer,Integer>();
		musicId.put(1, soundpool.load(this, R.raw.auto_detection, 1));
		musicId.put(2, soundpool.load(this, R.raw.jiangao, 1));
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			mBottomView.setVisibility(mBottomView.isShown()?View.INVISIBLE:View.VISIBLE);
			mAutoLayout.setVisibility(mAutoLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mManualLayout.setVisibility(mManualLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mPressureLayout.setVisibility(mPressureLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mOtherLayout.setVisibility(mOtherLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mManualModeLayout.setVisibility(mManualModeLayout.isShown()?View.GONE:View.GONE);
			mManualPartLayout.setVisibility(mManualPartLayout.isShown()?View.GONE:View.GONE);
			mWideSpeedLayout.setVisibility(mWideSpeedLayout.isShown()?View.GONE:View.GONE);
			mPressureSpeedsLayout.setVisibility(mPressureSpeedsLayout.isShown()?View.GONE:View.GONE);
		}

		   
		   switch (v.getId()) {
			case R.id.img_back_up:
				Log.d(TAG2, "click back up----");
				clickBackUp(event);
				break;
			case R.id.img_back_down:
				Log.d(TAG2, "click img_back_down----");
				clickBackDown(event);
				break;
			case R.id.img_leg_straighten:
				Log.d(TAG2, "click img_leg_straighten----");
				clickLegStraighten(event);
				break;
			case R.id.img_leg_bend:
				Log.d(TAG2, "click img_leg_bend----");
				clickLegBend(event);
				break;
			case R.id.img_leg_shrink:
				Log.d(TAG2, "click  img_leg_shrink----");
				clickLegShrink(event);
				break;
			case R.id.img_leg_stretch:
				Log.d(TAG2, "click  img_leg_stretch----");
				clickLegStretch(event);
				break;
//			case R.id.img_up:
//				shoulderUp(event);
//				break;
//			case R.id.img_down:
//				shoulderDown(event);
//				break;
			case R.id.ckb_part_up:
				Log.d(TAG2, "on touch focus up----");
//				fixPointUp(event);
				break;
			case R.id.ckb_part_down:
				Log.d(TAG2, "on touch focus down----");
//				fixPointDown(event);
				break;
			default:
				break;
			}
			mPopwindow_clicked = true;
			
		return true;
		
	}
	 /** 
     * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾcontent�������ָ�ƶ��ľ����Ǹ������ҵ�ǰmenu�ǿɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾcontent�� 
     *  
     * @return ��ǰ��������ʾcontent����true�����򷵻�false�� 
     */  
    private boolean wantToShowContent() {  
        return xUp - xDown < 0 && isMenuVisible;  
    }  
  
    /** 
     * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾmenu�������ָ�ƶ��ľ������������ҵ�ǰmenu�ǲ��ɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾmenu�� 
     *  
     * @return ��ǰ��������ʾmenu����true�����򷵻�false�� 
     */  
    private boolean wantToShowMenu() {  
        return xUp - xDown > 0 && !isMenuVisible;  
    } 

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_op_power:
//			mBottomView.setVisibility(mBottomView.isShown() ? View.INVISIBLE
//					: View.VISIBLE);
			
			Log.d(TAG2, "set----ckb-----false");
			if (!mPower) {
				sendCommand(DataFrame.getSendFrame(OPERATION.POWER_ON));
				//��������ʶ���﷨
				initGrammar();
//				 mPower = true;
			} else {
				sendCommand(DataFrame.getSendFrame(OPERATION.POWER_OFF));
				;
//				 mPower = false;
				
			}
			
			break;
		case R.id.btn_op_stop:
			btnStop.setSelected(true);
			sendCommand(DataFrame.getSendFrame(OPERATION.STOP));
			break;
		case R.id.img_right_drawer_bar:
			Log.d(TAG2, "img_drawer_bar----");
			drawerLayout.openDrawer(Gravity.RIGHT);
			break;
		case R.id.img_left_drawer_bar:
			Log.d(TAG2, "img_drawer_bar----");
			drawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.btn_voice_control:
//			mIat.startListening(mRecoListener);
			if(setParam()){
				ret = mAsr.startListening(mRecoListener);
//				isrDialog.setListener(mRecoDialogListener);
//       	        isrDialog.show(); 
				text="";
	       	    editText.setText(text);
				Log.d(TAG,"��ʼʶ�� " + ret);	
				}
				if (ret != ErrorCode.SUCCESS) {
					if(ret == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
						//δ��װ����ת����ʾ��װҳ��
						Log.d(TAG,"δ��װ����ת����ʾ��װҳ�� " + ret);	
					}else {
						Log.d(TAG,"ʶ��ʧ��,������: " + ret);	
					}
				}
				//������д
//			mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//			tellNetStatus();//�ж��Ƿ��������磬��������ʹ�ñ���ʶ��
//			isrDialog.setListener(mRecoDialogListener);
//       	    isrDialog.show(); 
//       	    text="";
//       	    editText.setText(text);
			break;
		case R.id.btn_op_auto:
			mAutoLayout.setVisibility(View.VISIBLE);
//			showAutoWindow();
			mManualLayout.setVisibility(View.GONE);
			mPressureLayout.setVisibility(View.INVISIBLE);
			 mPressureSpeedsLayout.setVisibility(View.GONE);
			 mManualPartLayout.setVisibility(View.GONE);
			mOtherLayout.setVisibility(View.INVISIBLE);
			mManualModeLayout.setVisibility(View.GONE);
			mWideSpeedLayout.setVisibility(View.GONE);
			btn_Auto.setSelected(true);
			Log.d("activityHD", "click btn_auto");
			break;
		case R.id.btn_op_manual:
			if(mManualLayout.getVisibility()==View.VISIBLE)return;
			 setManualList();
			mManualLayout.setVisibility(View.VISIBLE);
			mAutoLayout.setVisibility(View.INVISIBLE);
			 mPressureSpeedsLayout.setVisibility(View.GONE);
			mPressureLayout.setVisibility(View.INVISIBLE);
			mOtherLayout.setVisibility(View.INVISIBLE);
			mManualModeLayout.setVisibility(View.GONE);
			mWideSpeedLayout.setVisibility(View.GONE);
			break;
		case R.id.btn_op_pressure:
			 setPressureList();
			mPressureLayout.setVisibility(View.VISIBLE);
			mAutoLayout.setVisibility(View.INVISIBLE);
			 mPressureSpeedsLayout.setVisibility(View.GONE);
			 mManualPartLayout.setVisibility(View.GONE);
			mManualLayout.setVisibility(View.INVISIBLE);
			mOtherLayout.setVisibility(View.INVISIBLE);
			mWideSpeedLayout.setVisibility(View.GONE);
			mManualModeLayout.setVisibility(View.GONE);
			break;
		case R.id.btn_op_other:
			mOtherLayout.setVisibility(View.VISIBLE);
			mAutoLayout.setVisibility(View.INVISIBLE);
			mManualLayout.setVisibility(View.INVISIBLE);
			mPressureLayout.setVisibility(View.INVISIBLE);
			 mPressureSpeedsLayout.setVisibility(View.GONE);
			 mManualPartLayout.setVisibility(View.GONE);
			mWideSpeedLayout.setVisibility(View.GONE);
			mManualModeLayout.setVisibility(View.GONE);
//			btn_Auto.setSelected(true);
			break;
		case R.id.img_zero_gravity:
			Log.d(TAG2, "click zero gravity----");
			clickZeroGravity();
			break;
		case R.id.img_sleep_position:
			Log.d(TAG2, "click sleep position----");
			clickSleep();
			break;
		case R.id.tv_mode_paida:
			sendCommand(DataFrame.getSendFrame(OPERATION.BACK_FIXED));
			tv_pop_mode_paida.setBackgroundResource(R.drawable.bk_auto_list);
			tv_pop_mode_rounie.setBackgroundResource(android.R.color.white);
			tv_pop_mode_kouji.setBackgroundResource(android.R.color.white);
			break;
			
		}
		RokolUtil.performTouchSound(getBaseContext());
	}
	
	
	private void setAutoZyys(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.FATIGUERECOVERY_ON));
			}
			else{
			}
		}
	private void setAutoYsbj(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.HEALTH_ON));
			}
			else{
			}
		}
	private void setAutoYlam(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.RHYTHM_ON));
			}
			else{
			}
		}
	private void setAutoYjkb(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.YOGIOPENBACK_ON));
			}
			else{
			}
		}
	private void setAutoYdyms(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.ATHLETEMODE_ON));
			}
			else{
			}
		}
	private void setAutoXyfs(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.XiaoYaoFangSong));
			}
			else{
			}
		}
	private void setAutoXsbj(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
		    ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.XiuShenBaoJian));
			}
			else{
			}
		}
	private void setAutoSthh(boolean check){
		if(check){
			ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.PAINEASE_ON));
			}
			else{
			}
		}
	private void setAutoKsty(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION._3DEXPERIENCE_ON));
			}
			else{
			}
		}
	private void setAutoJgsz(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.MOODRELAX_ON));
			}
			else{
			}
		}
	private void setAutoGwdr(boolean check){
		if(check){
			ckb_sthh.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.SHOPPER_ON));
			}
			else{
			}
		}
	private void setAutoBlsbz(boolean check){
		if(check){
			ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
			ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
			ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
			ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
			ckb_ylam.setChecked(false);
			ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
			sendCommand(DataFrame.getSendFrame(OPERATION.OFFICEWORKER_ON));
			}
			else{
			}
		}
	private void setCheckBoxFalse(){
		ckb_sthh.setChecked(false);ckb_gwdr.setChecked(false);
		ckb_xyfs.setChecked(false);ckb_jgsz.setChecked(false);
		ckb_xsbj.setChecked(false);ckb_ksty.setChecked(false);
		ckb_ydyms.setChecked(false);ckb_yjkb.setChecked(false);
		ckb_blsbz.setChecked(false);ckb_ylam.setChecked(false);
		ckb_ysbj.setChecked(false);ckb_zyys.setChecked(false);
		ckb_zuliao.setChecked(false);ckb_feet_heat.setChecked(false);
		ckb_thermal.setChecked(false);
		ckb_fixed.setChecked(false);
		ckb_all.setChecked(false);ckb_part.setChecked(false);
		mManualListFlag=false;mModeListFlag=false;
				mPressureListFlag=false;
	}
	
	private void showpartPopupWindow(){
		
//		if(popWindow_mode==null){Log.d(TAG2, "popWindow!=null--------"+popWindow);
//		LayoutInflater inflater=getLayoutInflater();
//		View view=inflater.inflate(R.layout.op_part_ckb_item,null);
		ckb_all=(CheckBox)findViewById(R.id.ckb_part_all);
		ckb_part=(CheckBox)findViewById(R.id.ckb_part_part);
		ckb_fixed=(CheckBox)findViewById(R.id.ckb_part_fixed);
		ckb_up=(CheckBox)findViewById(R.id.ckb_part_up);
		ckb_down=(Button)findViewById(R.id.ckb_part_down);
		 ckb_up.setOnClickListener(this);
		 ckb_down.setOnClickListener(this);
//		popWindow_mode=new PopupWindow(view,187,270);
//		popWindow_mode.setBackgroundDrawable(new ColorDrawable(
//				android.R.color.white));
//		popWindow_mode.setOutsideTouchable(true);
//		}
//		popWindow_mode.showAsDropDown(btn_pressure, 0, 0);
		ckb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					ckb_part.setChecked(false);
					ckb_fixed.setChecked(false);
				sendCommand(DataFrame.getSendFrame(OPERATION.BACK_ALL));;
				Log.d(TAG2, "part all----");
				RokolUtil.performTouchSound(getBaseContext());}
			}
		});
        ckb_part.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
				Log.d(TAG2, "part part----");
				ckb_all.setChecked(false);
				ckb_fixed.setChecked(false);
				sendCommand(DataFrame.getSendFrame(OPERATION.BACK_BACK));
				RokolUtil.performTouchSound(getBaseContext());}
				else{
					Log.d(TAG2, "part part-unchecked---");
				}
				
				
			}
		});
      ckb_fixed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
				Log.d(TAG2, "part fixed----");
				ckb_all.setChecked(false);
				ckb_part.setChecked(false);
				sendCommand(DataFrame.getSendFrame(OPERATION.BACK_FIXED));
				RokolUtil.performTouchSound(getBaseContext());}
				else{}
				
			}
		});
      ckb_up.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			Log.d(TAG2, "part on touch up----");
			fixPointUp(event);
			
			return false;
		}
	});
      ckb_down.setOnTouchListener(new OnTouchListener() {
  		
  		@Override
  		public boolean onTouch(View v, MotionEvent event) {
  			// TODO Auto-generated method stub
  			Log.d(TAG2, "part on touch down----");
  			fixPointDown(event);
  			return false;
  		}
  	});

		}
		
	private void clickPressureStrength(final TextView tv_pressure_all_strength) {

			
			tv_pressure_all_strength.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// TODO Auto-generated method stub
					switch (mClickStrengthCount) {
					case 0:
						sendCommand(DataFrame
								.getSendFrame(OPERATION.PRESSURE_SPEED_1));
						setTextDrawabe(drawablePressure_pop_item, tv_pressure_all_strength, 
								R.drawable.op_weak, R.string.weaken);
					
						mClickStrengthCount++;
						Log.d("pressure", "mClick pressure weak-------"+mClickStrengthCount);
						break;
					case 1:
						sendCommand(DataFrame
								.getSendFrame(OPERATION.PRESSURE_SPEED_2));
						setTextDrawabe(drawablePressure_pop_item, tv_pressure_all_strength, 
								R.drawable.op_middle, R.string.middle);
							mClickStrengthCount++;
							Log.d("pressure", "mClickStrengthCount--------"+mClickStrengthCount);
						break;
					case 2:
						sendCommand(DataFrame
								.getSendFrame(OPERATION.PRESSURE_SPEED_3));
						mClickStrengthCount=0;
						tv_pressure_all_strength.setBackgroundResource(R.drawable.bk_auto_list);
						setTextDrawabe(drawablePressure_pop_item, tv_pressure_all_strength, 
								R.drawable.op_strong, R.string.strengthen);
						Log.d("pressure", "mClickStrengthCount--------"+mClickStrengthCount);
						break;
					}
					RokolUtil.performTouchSound(getBaseContext());

				}
			});
		

	}
	private void showPopupZuliaoWindow(View parent){
		Log.d(TAG2, "popZuliaoWindow ==--------");
		if (popZuliaoWindow == null) {
			Log.d(TAG2, "popZuliaoWindow!=null---init-----");
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.op_other_zuliao_speed_type, null);
			tv_other_zuliao=(TextView)view.findViewById(R.id.tv_other_zuoliao);
			drawableZuliao= getResources().getDrawable(R.drawable.op_other_zuliao_rouhe);
			popZuliaoWindow = new PopupWindow(view,
					186, 66);
//			popWindow.setFocusable(true);
			popZuliaoWindow.setBackgroundDrawable(new ColorDrawable(
					android.R.color.white));
			popZuliaoWindow.setOutsideTouchable(true);
		}
		popZuliaoWindow.showAsDropDown(parent, -2, 204);
		
		Log.d(TAG2, " popZuliaoWindow ----and send cmd soft----"+tv_other_zuliao);
		tv_other_zuliao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(zuliaoFastFlag){
				setTextDrawabe(drawableZuliao, tv_other_zuliao, R.drawable.op_strong, 
						R.string.fast);
				tv_other_zuliao.setBackgroundResource(R.drawable.bk_auto_list);
//				popZuliaoWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bk_auto_list));
				zuliaoFastFlag=false;
				sendCommand(DataFrame
						.getSendFrame(OPERATION.PEDICURE_FAST));
				mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_high));;
				}
				else {setTextDrawabe(drawableZuliao, tv_other_zuliao, R.drawable.op_other_zuliao_rouhe, 
						R.string.soft);
				zuliaoFastFlag=true;
				sendCommand(DataFrame
						.getSendFrame(OPERATION.PEDICURE_SOFT));
				mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_middle));;
				}
				RokolUtil.performTouchSound(getBaseContext());
			}
			
		});
		
	}
	private void showSpeedPopWindow(View parent){
		Log.d(TAG2, "showSpeedPopWindow ==--------");
//		if (popWindow == null) {
			Log.d(TAG, "popWindow!=null--------"+popWindow);
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.op_pressure_all_strength_item, null);
			tv_pressure_all_strength=(TextView)view.findViewById(R.id.tv_pressure_all_strength);
			 drawablePressure_pop_item= getResources().getDrawable(R.drawable.op_middle);
			popWindow = new PopupWindow(view,
					175, 65);
			mClickStrengthCount++;
//			popWindow.setFocusable(true);
			popWindow.setBackgroundDrawable(new ColorDrawable(
					android.R.color.white));
			popWindow.setOutsideTouchable(true);
			
			tv_pressure_all_strength.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// TODO Auto-generated method stub
					switch (mClickStrengthCount) {
					case 0:
						sendCommand(DataFrame
								.getSendFrame(OPERATION.BACK_SPEED_1));
						setTextDrawabe(drawablePressure_pop_item, tv_pressure_all_strength, 
								R.drawable.op_weak, R.string.weaken);
					
						mClickStrengthCount++;
						Log.d(TAG2, "mClickStrengthCount-------"+mClickStrengthCount);
						break;
					case 1:
						sendCommand(DataFrame
								.getSendFrame(OPERATION.BACK_SPEED_2));
						setTextDrawabe(drawablePressure_pop_item, tv_pressure_all_strength, 
								R.drawable.op_middle, R.string.middle);
							mClickStrengthCount++;
							Log.d(TAG2, "mClickStrengthCount--------"+mClickStrengthCount);
						break;
					case 2:
						sendCommand(DataFrame
								.getSendFrame(OPERATION.BACK_SPEED_3));
						mClickStrengthCount=0;
						tv_pressure_all_strength.setBackgroundResource(R.drawable.bk_auto_list);
						setTextDrawabe(drawablePressure_pop_item, tv_pressure_all_strength, 
								R.drawable.op_strong, R.string.strengthen);
						Log.d(TAG2, "mClickStrengthCount--------"+mClickStrengthCount);
						break;
					}
					RokolUtil.performTouchSound(getBaseContext());

				}
			});
		
	}
	
	private void showPopup3DWindow(View parent) {
		Log.d(TAG, "pop3DWindow ==--------");
		if (pop3DWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.op_popup_width_item, null);
			tv_pop_3d_up=(TextView)view.findViewById(R.id.tv_pop_3d_item_up);
			tv_pop_3d_down=(TextView)view.findViewById(R.id.tv_pop_3d_item_down);
			pop3DWindow = new PopupWindow(view,175, 110);
					
			Log.d(TAG, "popWindow!=null--------"+pop3DWindow);
			pop3DWindow.setBackgroundDrawable(new ColorDrawable(
					android.R.color.white));
			pop3DWindow.setOutsideTouchable(true);
			tv_pop_3d_up.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_3D_FORWARD));
					Log.d(TAG2, "3d strong ++--------");
					
					RokolUtil.performTouchSound(getBaseContext());
				}
			});
			tv_pop_3d_down.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_3D_BACKWARD));
					Log.d(TAG2, "3d weak-- --------");
					RokolUtil.performTouchSound(getBaseContext());
				}
				
			});
		}
		pop3DWindow.showAsDropDown(btn_pressure, 0,0);
		
	}
	private void showWidthPopupWindow(View parent) {
		if(!widthAbleFlag)
			return;
		Log.d(TAG2, "popwidthWindow ==--------");
		if (popWidthWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.op_popupwindow_item, null);
			 tv_pop_width_item=(TextView)view.findViewById(R.id.tv_pop_item);
			 drawableWidth= getResources().getDrawable(R.drawable.op_img_middle);
			 drawableWidth.setBounds(0, 0, drawableWidth.getMinimumWidth(), drawableWidth.getMinimumHeight());
			tv_pop_width_item.setCompoundDrawables(drawableWidth,null,null,null);
			popWidthWindow = new PopupWindow(view,
					175, 60);
//			popWindow.setFocusable(true);
			popWidthWindow.setBackgroundDrawable(new ColorDrawable(
					android.R.color.white));
			popWidthWindow.setOutsideTouchable(true);
			Log.d(TAG2, "popwidthWindow--------"+popWidthWindow);
			 tv_pop_width_item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG2, "manual wide text click------"+mClickWidthCount);
					// TODO Auto-generated method stub
					switch (mClickWidthCount) {
					case 0:
						mClickWidthCount++;
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_NARROW));
					setTextDrawabe(drawableWidth,tv_pop_width_item,R.drawable.op_img_narrow,R.string.narrow);
					break;
					case 1:
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_CENTER));
					setTextDrawabe(drawableWidth,tv_pop_width_item,R.drawable.op_img_middle,R.string.middle);
					mClickWidthCount++;
					
					break;
					case 2:
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_WIDE));
					mClickWidthCount=0;
					tv_pop_width_item.setBackgroundResource(R.drawable.bk_auto_list);
					setTextDrawabe(drawableWidth,tv_pop_width_item,R.drawable.op_img_width,R.string.wide);
					break;
					}
					RokolUtil.performTouchSound(getBaseContext());
				}
			});
			
		}
		popWidthWindow.showAsDropDown(parent, 0, 104);
		
	}
	private void setManualList(){
		if(!mManualListFlag){
			mManualListFlag=true;
		 manual_ListItem = new ArrayList<HashMap<String, Object>>();  

        HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemImage", R.drawable.op_manual_mode_normal);//ͼ����Դ��ID  
        map.put("ItemTitle", getResources().getString(R.string.mode));  
        map.put("arrow", R.drawable.op_manual_arrow);
        manual_ListItem.add(map); 
        
        map = new HashMap<String, Object>();  
        map.put("ItemImage", R.drawable.op_manual_part_normal);//ͼ����Դ��ID  
        map.put("ItemTitle", getResources().getString(R.string.part));  
        map.put("arrow", R.drawable.op_manual_arrow);
        manual_ListItem.add(map); 
        
        map = new HashMap<String, Object>();  
        map.put("ItemImage", R.drawable.op_manual_width_normal);//ͼ����Դ��ID  
        map.put("ItemTitle", getResources().getString(R.string.width)); 
        map.put("arrow", R.drawable.op_manual_arrow);
        manual_ListItem.add(map); 
        
        map = new HashMap<String, Object>();  
        map.put("ItemImage", R.drawable.op_manual_speed_normal);//ͼ����Դ��ID  
        map.put("ItemTitle", getResources().getString(R.string.speed)); 
        map.put("arrow", R.drawable.op_manual_arrow);
        manual_ListItem.add(map); 
        
        map = new HashMap<String, Object>();  
        map.put("ItemImage", R.drawable.op_manual_3d_normal);//ͼ����Դ��ID  
        map.put("ItemTitle", getResources().getString(R.string.threeD)); 
        map.put("arrow", R.drawable.op_manual_arrow);
        manual_ListItem.add(map); 
        
        //������������Item�Ͷ�̬�����Ӧ��Ԫ��  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,manual_ListItem,//����Դ   
            R.layout.op_manual_list_item,//ListItem��XMLʵ��  
            //��̬������ImageItem��Ӧ������          
            new String[] {"ItemImage","ItemTitle", "arrow"},   
            //ImageItem��XML�ļ������һ��ImageView,����TextView ID  
            new int[] {R.id.img_manual_icon,R.id.tv_manual_info,R.id.img_manual_arrow}  
        );  
        mListView_manual.setAdapter(listItemAdapter); 
        }
        mListView_manual.setOnItemClickListener(new OnItemClickListener() {
			 
	           @SuppressWarnings("deprecation")
			@Override
	           public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                   long arg3) {
	        	   if (((ListView)arg0).getTag() != null){

	                    ((View)((ListView)arg0).getTag()).setBackgroundDrawable(null);

	            }

	            ((ListView)arg0).setTag(arg1);

	            arg1.setBackgroundResource(R.drawable.bk_auto_list);
	        	   RokolUtil.performTouchSound(getBaseContext());
	               Log.d("activityHD","-----"+arg2);
	        	   if(manual_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.mode)))
	                {
//	        		   showModePopupWindow();
//	        		   popupModeListView();
	        		   initManualModeList();
	        		   mManualModeLayout.setVisibility(View.VISIBLE);
	        		   mWideSpeedLayout.setVisibility(View.GONE);
	        		   mManualPartLayout.setVisibility(View.GONE);
	        		   mPressureLayout.setVisibility(View.GONE);
	        		   
	                }
	        	   if(manual_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.part)))
	                {
//	        		   popupPartListView();
	        		   if(!mModeOn)return;
	        		   
	        		   mManualPartLayout.setVisibility(View.VISIBLE);
	        		   mWideSpeedLayout.setVisibility(View.GONE);
	        		   mManualModeLayout.setVisibility(View.GONE);
	        		   mPressureLayout.setVisibility(View.GONE);
	        		   showpartPopupWindow();
	                }
	        	   if(manual_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.width))){
	        		   if(!widthAbleFlag)
	        				return;
//	        		   showWidthPopupWindow(btn_pressure);
	        		   //���������ٶ�
	        		   if(zuliaoshow)mOtherZuliaoSpeedLayout.setVisibility(View.INVISIBLE);
	        		   mWideSpeedLayout.setVisibility(View.VISIBLE);
	        		   mManualWideLayout.setVisibility(View.VISIBLE);
	        		   m3D_layout.setVisibility(View.INVISIBLE);
	        		   mNullLayout.setVisibility(View.GONE);
	        		   tv_manual_wide.setBackgroundResource(R.drawable.bk_auto_list);
	        		   if(mNewWidth){
	        			   Log.d(TAG2, "new width------"+mClickWidthCount+mNewWidth);
	        		   sendCommand(DataFrame
	   						.getSendFrame(OPERATION.BACK_CENTER));
	        		   mClickWidthCount=2;
	        		   setTextDrawabe(drawableWidth,tv_manual_wide,R.drawable.op_img_middle,R.string.middle);
	        		   mNewWidth=false;}
	        		   setwidth();
	        		   mManualSpeedLayout.setVisibility(View.GONE);
	        		   mManualPartLayout.setVisibility(View.GONE);
	        		   mManualModeLayout.setVisibility(View.GONE);
	        		   mPressureLayout.setVisibility(View.GONE);
	        	   }
                   if(manual_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.speed))){
                	   if(!speedAbleFlag)
               			return;
                	   mClickStrengthCount=2;
//                	   showSpeedPopWindow(btn_pressure);
                	   if(zuliaoshow)mOtherZuliaoSpeedLayout.setVisibility(View.INVISIBLE);
                	   mWideSpeedLayout.setVisibility(View.VISIBLE);
                	   mManualSpeedLayout.setVisibility(View.VISIBLE);
                	   mManualWideLayout.setVisibility(View.INVISIBLE);
                	   mNullLayout.setVisibility(View.INVISIBLE);
                	   m3D_layout.setVisibility(View.GONE);
                	   tv_manual_speed.setBackgroundResource(R.drawable.bk_auto_list);
                	   setManualSpeed();
                	   mManualPartLayout.setVisibility(View.GONE);
	        		   mManualModeLayout.setVisibility(View.GONE);
	        		   mPressureLayout.setVisibility(View.GONE);
	        		   if(mNewSpeed){
		        		   setTextDrawabe(drawablePressure_pop_item, tv_manual_speed, 
									R.drawable.op_middle, R.string.middle);
		        		   mClickSpeedCount=2;
	                	   sendCommand(DataFrame
									.getSendFrame(OPERATION.BACK_SPEED_2));
	                	   mNewSpeed=false;}
	        	   }
                  if(manual_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.threeD))){
              		if (threeDFlag)return;
//                	   showPopup3DWindow(btn_pressure);
              		 if(zuliaoshow)mOtherZuliaoSpeedLayout.setVisibility(View.INVISIBLE);
              		   mWideSpeedLayout.setVisibility(View.VISIBLE);
              		   m3D_layout.setVisibility(View.VISIBLE);
              		   mNullLayout.setVisibility(View.GONE);
              		   mManualSpeedLayout.setVisibility(View.GONE);
              		   mManualWideLayout.setVisibility(View.INVISIBLE);
              		   set3Dwindow();
                	   mManualPartLayout.setVisibility(View.GONE);
	        		   mManualModeLayout.setVisibility(View.GONE);
	        		   mPressureLayout.setVisibility(View.GONE);
	        	   }
                 

	            }
	      });
	}
	private void setPressureList(){
		if(!mPressureListFlag){
			mPressureListFlag=true;
		 pressure_ListItem = new ArrayList<HashMap<String, Object>>();  

       HashMap<String, Object> map = new HashMap<String, Object>();  
       map.put("ItemTitle", getResources().getString(R.string.all));  
       map.put("arrow", R.drawable.op_manual_arrow);
       pressure_ListItem.add(map); 
       
       map = new HashMap<String, Object>();  
       map.put("ItemTitle", getResources().getString(R.string.hand_shoulder));  
       map.put("arrow", R.drawable.op_manual_arrow);
       pressure_ListItem.add(map); 
       
       map = new HashMap<String, Object>();  
       map.put("ItemTitle", getResources().getString(R.string.waist)); 
       map.put("arrow", R.drawable.op_manual_arrow);
       pressure_ListItem.add(map); 
       
       map = new HashMap<String, Object>();  
       map.put("ItemTitle", getResources().getString(R.string.leg_foot)); 
       map.put("arrow", R.drawable.op_manual_arrow);
       pressure_ListItem.add(map); 
       
       
       //������������Item�Ͷ�̬�����Ӧ��Ԫ��  

       SimpleAdapter listItemAdapter = new SimpleAdapter(this,pressure_ListItem,//����Դ   

           R.layout.op_pressure_list_view,//ListItem��XMLʵ��  

           //��̬������ImageItem��Ӧ������          

           new String[] {"ItemTitle", "arrow"},   

           //ImageItem��XML�ļ������һ��ImageView,����TextView ID  

           new int[] {R.id.tv_pressure_info,R.id.img_pressure_arrow}  

       );  
       
       mListView_pressure.setAdapter(listItemAdapter); 
       }
       mListView_pressure.setOnItemClickListener(new OnItemClickListener() {
			 
           @SuppressWarnings("deprecation")
		@Override
           public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                   long arg3) {
        	   
        	   if (((ListView)arg0).getTag() != null){

                   ((View)((ListView)arg0).getTag()).setBackgroundDrawable(null);
           }
           ((ListView)arg0).setTag(arg1);

           arg1.setBackgroundResource(R.drawable.bk_auto_list);
           
               Log.d("pressure","-----"+arg2);
        	   if(pressure_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.leg_foot)))
                {
                    tv_speed4=(TextView)findViewById(R.id.tv_speed4);
        		   clickPressureStrength(tv_speed4);
        		   if(!mAirSpeed4){
         			   mAirSpeed4=true;mAirSpeed2=false;mAirSpeed3=false;mAirSpeed1=false;
         			   setCmdDrawable(OPERATION.PRESSURE_LEG_ON,tv_speed4);}
                mPressureSpeedsLayout.setVisibility(View.VISIBLE);
    		    mPressureSpeed4.setVisibility(View.VISIBLE);tv_speed4.setBackgroundResource(R.drawable.bk_auto_list);
    		    mPressureSpeed2.setVisibility(View.INVISIBLE);
    		    mPressureSpeed3.setVisibility(View.INVISIBLE);
    		    mPressureSpeed1.setVisibility(View.INVISIBLE);
    		    mOtherLayout.setVisibility(View.GONE);
    			mAutoLayout.setVisibility(View.INVISIBLE);
    			mManualLayout.setVisibility(View.INVISIBLE);
    			mWideSpeedLayout.setVisibility(View.GONE);
    			mManualModeLayout.setVisibility(View.GONE);
    		    

        		   
                }
        	   
        	   if(pressure_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.all)))
               {
               tv_speed1=(TextView)findViewById(R.id.tv_speed1);
    		   clickPressureStrength(tv_speed1);
    		   if(!mAirSpeed1){
    			   mAirSpeed1=true;mAirSpeed2=false;mAirSpeed3=false;mAirSpeed4=false;
       		       setCmdDrawable(OPERATION.PRESSURE_AUTO_ON,tv_speed1);
       		      }
       		mPressureSpeedsLayout.setVisibility(View.VISIBLE);
       		mPressureSpeed1.setVisibility(View.VISIBLE);tv_speed1.setBackgroundResource(R.drawable.bk_auto_list);
		    mPressureSpeed2.setVisibility(View.INVISIBLE);
		    mPressureSpeed3.setVisibility(View.INVISIBLE);
		    mPressureSpeed4.setVisibility(View.INVISIBLE);
		    mOtherLayout.setVisibility(View.GONE);
			mAutoLayout.setVisibility(View.INVISIBLE);
			mManualLayout.setVisibility(View.INVISIBLE);
			mWideSpeedLayout.setVisibility(View.GONE);
			mManualModeLayout.setVisibility(View.GONE);
               }
        	   
        	   if(pressure_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.hand_shoulder)))
               {
               tv_speed2=(TextView)findViewById(R.id.tv_speed2);
    		   clickPressureStrength(tv_speed2);
    		   if(!mAirSpeed2){
    			   mAirSpeed2=true;mAirSpeed1=false;mAirSpeed3=false;mAirSpeed4=false;
        		   setCmdDrawable(OPERATION.PRESSURE_HAND_ON,tv_speed2);}    //set drawable and send cmd
        		   mPressureSpeedsLayout.setVisibility(View.VISIBLE);
        		   mPressureSpeed2.setVisibility(View.VISIBLE);tv_speed2.setBackgroundResource(R.drawable.bk_auto_list);
       		    	mPressureSpeed1.setVisibility(View.INVISIBLE);
       		    	mPressureSpeed3.setVisibility(View.INVISIBLE);
		  		    mPressureSpeed4.setVisibility(View.INVISIBLE);
		  		    mOtherLayout.setVisibility(View.GONE);
		  			mAutoLayout.setVisibility(View.INVISIBLE);
		  			mManualLayout.setVisibility(View.INVISIBLE);
		  			mWideSpeedLayout.setVisibility(View.GONE);
		  			mManualModeLayout.setVisibility(View.GONE);
               }
        	   
        	   if(pressure_ListItem.get(arg2).get("ItemTitle").equals(getString(R.string.waist)))
               {
               tv_speed3=(TextView)findViewById(R.id.tv_speed3);
               if(!mAirSpeed3){
    			   mAirSpeed3=true;mAirSpeed2=false;mAirSpeed1=false;mAirSpeed4=false;
        		   setCmdDrawable(OPERATION.PRESSURE_SEAT_ON,tv_speed3);}
        		   clickPressureStrength(tv_speed3);
        		   mPressureSpeedsLayout.setVisibility(View.VISIBLE);
       		   mPressureSpeed3.setVisibility(View.VISIBLE);tv_speed3.setBackgroundResource(R.drawable.bk_auto_list);
       		    mPressureSpeed2.setVisibility(View.INVISIBLE);
		    	mPressureSpeed1.setVisibility(View.INVISIBLE);
	  		    mPressureSpeed4.setVisibility(View.INVISIBLE);
	  		    mOtherLayout.setVisibility(View.GONE);
	  			mAutoLayout.setVisibility(View.INVISIBLE);
	  			mManualLayout.setVisibility(View.INVISIBLE);
	  			mWideSpeedLayout.setVisibility(View.GONE);
	  			mManualModeLayout.setVisibility(View.GONE);
       		    
               }
        	   
        	   RokolUtil.performTouchSound(getBaseContext());
        	   setCountTime();
            }
      });

	}
	private void setCmdDrawable(OPERATION op,TextView tv){
		      sendCommand(DataFrame.getSendFrame(op));
		      setTextDrawabe(drawablePressure_pop_item, tv, 
					R.drawable.op_middle, R.string.middle);
		      mClickStrengthCount=2;
		      Log.d("pressure","--set pressure middle---");
	}
	private void setZuliao(boolean isChecked){
		tv_zuliao_speed=(TextView)findViewById(R.id.tv_other_zuoliao);
		if(isChecked&!zuliaoOn){
			sendCommand(DataFrame.getSendFrame(OPERATION.PEDICURE_SOFT));;
			setTextDrawabe(drawableZuliao, tv_zuliao_speed, R.drawable.op_other_zuliao_rouhe, 
					R.string.soft);
			zuliaoFastFlag=true;
			zuliaoOn=true;
			mWideSpeedLayout.setVisibility(View.VISIBLE);
			mOtherZuliaoSpeedLayout.setVisibility(View.VISIBLE);
			mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_middle));;
    		tv_zuliao_speed.setBackgroundResource(R.drawable.bk_auto_list);
			setZulliaoSpeed();
			zuliaoshow=true;
     	   mManualSpeedLayout.setVisibility(View.INVISIBLE);
     	   mManualWideLayout.setVisibility(View.INVISIBLE);
     	   mPressureLayout.setVisibility(View.GONE);
     	   mNullLayout.setVisibility(View.INVISIBLE);
     	   m3D_layout.setVisibility(View.GONE);
			Log.d(TAG2, "other zuliao---111111-");
			}
			
			else if(zuliaoOn&(mWideSpeedLayout.getVisibility()==View.GONE||mWideSpeedLayout.getVisibility()==View.INVISIBLE))
			      {mWideSpeedLayout.setVisibility(View.VISIBLE);setZulliaoSpeed();ckb_zuliao.setChecked(true);
			       mManualSpeedLayout.setVisibility(View.INVISIBLE);mOtherZuliaoSpeedLayout.setVisibility(View.VISIBLE);
		     	   mManualWideLayout.setVisibility(View.INVISIBLE);
		     	   mPressureLayout.setVisibility(View.GONE);
		     	   mNullLayout.setVisibility(View.INVISIBLE);
		     	   m3D_layout.setVisibility(View.GONE);
		     	  Log.d(TAG2, "other zuliao---22222-");}
			else{	
				mWideSpeedLayout.setVisibility(View.GONE);
				mPressureLayout.setVisibility(View.INVISIBLE);
				zuliaoOn=false;
				sendCommand(DataFrame.getSendFrame(OPERATION.PEDICURE_OFF));
				mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_off));;Log.d(TAG2, "other zuliao---33333-");
			}
	}
	private void setFeetHeat(boolean isChecked){
		if(isChecked){
			Log.d(TAG2, "other feet thermal----");
			sendCommand(DataFrame.getSendFrame(OPERATION.THERMALFEET_ON));
			}
			else{
				Log.d(TAG2, "feetthermal-unchecked---");
				sendCommand(DataFrame.getSendFrame(OPERATION.THERMALFEET_OFF));
			}
	}
	private void setBackHeat(boolean isChecked){
		if(isChecked)
		{
		Log.d(TAG2, "other thermal checked----");
		sendCommand(DataFrame.getSendFrame(OPERATION.THEMALMETHOD_ON));
		RokolUtil.performTouchSound(getBaseContext());
		}
		else{
			Log.d(TAG2, "other thermal unchecked----");
			sendCommand(DataFrame.getSendFrame(OPERATION.THEMALMETHOD_OFF));
		    }
		}
	private void setMusic(){
		if(checkPackageExist()){
			callingDuomiMusic();
	   }
	};
	private void setZulliaoSpeed(){
		
		
		tv_zuliao_speed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(zuliaoFastFlag){
				setTextDrawabe(drawableZuliao, tv_zuliao_speed, R.drawable.op_strong, 
						R.string.fast);
				tv_zuliao_speed.setBackgroundResource(R.drawable.bk_auto_list);
				zuliaoFastFlag=false;
				zuliaoSpeedFase=true;
				sendCommand(DataFrame
						.getSendFrame(OPERATION.PEDICURE_FAST));
				mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_high));;
				}
				else {setTextDrawabe(drawableZuliao, tv_zuliao_speed, R.drawable.op_other_zuliao_rouhe, 
						R.string.soft);
				zuliaoFastFlag=true;
				
				sendCommand(DataFrame
						.getSendFrame(OPERATION.PEDICURE_SOFT));
				mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_middle));;
				}
				RokolUtil.performTouchSound(getBaseContext());
			}
			
		});
	}
	private void setwidth(){
		tv_manual_wide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG2, "manual wide text click------"+mClickWidthCount);
				// TODO Auto-generated method stub
				switch (mClickWidthCount) {
				case 0:
					mClickWidthCount++;
				sendCommand(DataFrame
						.getSendFrame(OPERATION.BACK_NARROW));
				setTextDrawabe(drawableWidth,tv_manual_wide,R.drawable.op_img_narrow,R.string.narrow);
				break;
				case 1:
				sendCommand(DataFrame
						.getSendFrame(OPERATION.BACK_CENTER));
				setTextDrawabe(drawableWidth,tv_manual_wide,R.drawable.op_img_middle,R.string.middle);
				mClickWidthCount++;
				
				break;
				case 2:
				sendCommand(DataFrame
						.getSendFrame(OPERATION.BACK_WIDE));
				mClickWidthCount=0;
				tv_manual_wide.setBackgroundResource(R.drawable.bk_auto_list);
				setTextDrawabe(drawableWidth,tv_manual_wide,R.drawable.op_img_width,R.string.wide);
				break;
				}
				RokolUtil.performTouchSound(getBaseContext());
			}
		});
	}
	private void setManualSpeed(){
		tv_manual_speed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				switch (mClickSpeedCount) {
				case 0:
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_SPEED_1));
					setTextDrawabe(drawablePressure_pop_item, tv_manual_speed, 
							R.drawable.op_weak, R.string.weaken);
				
					mClickSpeedCount++;
					Log.d(TAG2, "mClickStrengthCount-------"+mClickSpeedCount);
					break;
				case 1:
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_SPEED_2));
					setTextDrawabe(drawablePressure_pop_item, tv_manual_speed, 
							R.drawable.op_middle, R.string.middle);
					mClickSpeedCount++;
						Log.d(TAG2, "mClickStrengthCount--------"+mClickSpeedCount);
					break;
				case 2:
					sendCommand(DataFrame
							.getSendFrame(OPERATION.BACK_SPEED_3));
					mClickSpeedCount=0;
					tv_manual_speed.setBackgroundResource(R.drawable.bk_auto_list);
					setTextDrawabe(drawablePressure_pop_item, tv_manual_speed, 
							R.drawable.op_strong, R.string.strengthen);
					Log.d(TAG2, "mClickStrengthCount--------"+mClickSpeedCount);
					break;
				}
				RokolUtil.performTouchSound(getBaseContext());

			}
		});
	}
	private void set3Dwindow(){
		tv_pop_3d_up=(TextView)findViewById(R.id.tv_pop_3d_item_up);
		tv_pop_3d_down=(TextView)findViewById(R.id.tv_pop_3d_item_down);
				
		tv_pop_3d_up.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendCommand(DataFrame
						.getSendFrame(OPERATION.BACK_3D_FORWARD));
				Log.d(TAG2, "3d strong ++--------");
				
				RokolUtil.performTouchSound(getBaseContext());
			}
		});
		tv_pop_3d_down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendCommand(DataFrame
						.getSendFrame(OPERATION.BACK_3D_BACKWARD));
				Log.d(TAG2, "3d weak-- --------");
				RokolUtil.performTouchSound(getBaseContext());
			}
			
		});
	}

		
	
	private void setPartLvImg(ListView lv,int arg2,Object obj){
		ListAdapter la =lv.getAdapter();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map= (HashMap<String,Object>)la.getItem(arg2);
		map.put("ItemImage", obj);
		 ((SimpleAdapter)la).notifyDataSetChanged();
	}

	protected void sendCommand(byte[] data) {
		// Check that we're actually connected before trying anything
		if (mBluetoothServiceState != BluetoothService.STATE_CONNECTED) {
			RokolUtil.showToast(getApplicationContext(), getResources()
					.getString(R.string.not_connected), 1);
			// TODO: startConectPairedDevice();
			return;
		}

		// Check that there's actually something to send
		if (data.length > 0) {
			// Get the message bytes and tell the BluetoothService to write

			// send=new byte[]{(byte) 0xf1,(byte) 0xaa,0x55};
			// send=new DataFrame().getSendFrame(OPERATION.BACK_SPEED_2);
			// send=new PowerOnOffDataFrame(false).getSendData();
			// Toast.makeText(getApplicationContext(), new
			// PowerOnOffDataFrame(true).getOperationCode(),
			// Toast.LENGTH_LONG).show();
			this.currentSendData = data;
			// mBluetoothService.write(this.currentSendData);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			mCommunicationFailed = false;
			// ensureConnect();

			Intent intent = new Intent();
			intent.setAction(BLUETOOTHRECEIVER_ACTION);
			intent.putExtra("cmd", BluetoothService.REQUIRE_WRITE);
			intent.putExtra("command", this.currentSendData);
			// intent.putExtra("value", value);
			sendBroadcast(intent);// send Broadcast
			// delay 500 Ms to avoid continuous press command button
			

			 
		}
	}

	private void ensureConnect() {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mCounter++ < 3) {
					if (mReceiveData[0].equals("")) {
						sendCommand(currentSendData);
						ensureConnect();
						return;
					} else {
						mCounter = 0;
						return;
					}
				} else {
//					RokolUtil.showToast(getApplicationContext(), getResources()
//							.getString(R.string.communication_failed), 1);
					mCounter = 0;
				}
			}
		}, TIMEOUT);
	}

	// 20140611 debug receive
	private void ensureFeedBack(byte[] readBuf, int length) {
		mReceiveData = RokolUtil.bytesToHexStrings(readBuf, length);
		if (mReceiveData[0].toUpperCase().equals("F1")) {
			 //receivedataFlag++;//
//			if(mPower==false & receivedataFlag>25){mPower=true;changePowerState();}
			// TODO: handle read state
			checkState(readBuf);
			return;
		}
		// send from hand controller
		if (mReceiveData[0].toUpperCase().equals("F5")) {
			checkReceiveData(readBuf);

		}
		// send from BlueTooth
		if (mReceiveData[0].toUpperCase().equals("F2")) {

			if (mCommunicationFailed) {
					Log.v(TAG2, "not available device   mCommunicationFailed") ;
				return;
			}

			if (mCounter < 5) {
				if (mSendData != null
						&& !(RokolUtil.compareStingArray(mSendData,
								mReceiveData))) {
					// the send data and feedback data is not equal.
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendCommand(this.currentSendData);
					mCounter++;
				
				} else {
					mCounter = 0;
					checkReceiveData(readBuf);

				}

			} else {// send times >3 ,communication failed

//				RokolUtil.showToast(getApplicationContext(), getResources()
//						.getString(R.string.communication_failed), 1);

				mCounter = 0;
				mReceiveData = new String[] { "" };
				mCommunicationFailed = true;
			}
		}
	}

	/**
	 * check the system state
	 */
	private void checkState(byte[] readBuf) {
		
		for (int i = 1; i < readBuf.length; i++) {
//			Log.d("set state", "-------STATE CODE"+" "+i + readBuf.length);
			byte b = readBuf[i];
			switch (i) {
			case 1:
					setAirPosition(b);
				break;
			case 2:
				setHeatState(b);
				
				break;
			case 3:
				
				setHeatFeet(b);
				break;
			case 4:
					PreHeat(b);
					PreHeatFeet(b);
					setFeetStatus();
				break;
			case 5:
					setStrength(b);
				
				break;

				
			case 6:// shoulder air pressure
					setShoulderState(b);
				
				break;
			
//			case 7:
//				break;
			case 8:
				
				if(((b>>6)&0x1)==1 && ShoulderFlag==0){
					ShoulderFlag=1;
//				RokolUtil.showToast(getApplicationContext(), "�ֶ��������", 1);
				verifyShoulderHeight();
				soundpool.play(musicid.get(2), 0.1f, 0.1f, 1, 0, 1);
				Log.d(TAG, "�ֶ����------");}
				else if(((b>>6)&0x1)==0 && ShoulderFlag==1)
					{adjustShoulderDialog.dismiss();
					ShoulderFlag=0;
					}
					setSpeed(b);
//					setKneadstate(b);
//					setKnockState(b);
//					setTapState(b);
//					setShiatsuState(b);
				
				
				break;
			case 9:
				setMachinePosition(b);
				break;
			case 10:
                	setWidth(b);
				break;
			case 11:
				if ((byte) ((b >> 1) & 0x1) == 1 && AutoShoulderFlag==0) {
					AutoShoulderFlag=1;
//					RokolUtil.showToast(getApplicationContext(), "���ڼ���ߡ���", 1);
					 dialog=new AlertDialog.Builder(this).
							setMessage(getResources().getString(R.string.auto_shoulder)+"")
							.show();   
					dialog.setCanceledOnTouchOutside(false);
					soundpool.play(musicid.get(1), 0.1f, 0.1f, 1, 0, 1);
					Log.d(TAG2, "���ڼ����------"+musicid.get(1));
//					RokolUtil.performTouchSound(getBaseContext(), R.raw.auto_detection,true);
//					dialog.dismiss();
				}else if(AutoShoulderFlag==1 && (byte) ((b >> 1) & 0x1) == 0){
					AutoShoulderFlag=0;
					dialog.dismiss();
				}
				
				break;

			}

		}

	}
	


	/**
	 * check the massage state
	 */
	private void checkReceiveData(byte[] readBuf) {
		Log.d(TAG, "checkReceiveData");
		
		
		if (readBuf[1] == DataFrame.mPower_ON) {

			mPower = true;
			changePowerState();
			ckb_colorlight.setChecked(true);
			setCountTime();
		}
		if (readBuf[1] == DataFrame.mPower_OFF) {
			
			mPower = false;
			zuliaoOn=false;
			mSleep=false;
			mZeroGravity=false;
			changePowerState();
			setCheckBoxFalse();
//			LayoutInflater inflater=LayoutInflater.from(this);
//			inflater.inflate(R.layout.op_bottom_view, null);
			
			mBottomView.setVisibility(mBottomView.isShown()?View.INVISIBLE:View.VISIBLE);
			mAutoLayout.setVisibility(mAutoLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mManualLayout.setVisibility(mManualLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mPressureLayout.setVisibility(mPressureLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mOtherLayout.setVisibility(mOtherLayout.isShown()?View.INVISIBLE:View.INVISIBLE);
			mManualModeLayout.setVisibility(mManualModeLayout.isShown()?View.GONE:View.GONE);
			mManualPartLayout.setVisibility(mManualPartLayout.isShown()?View.GONE:View.GONE);
			mWideSpeedLayout.setVisibility(mWideSpeedLayout.isShown()?View.GONE:View.GONE);
			mPressureSpeedsLayout.setVisibility(mPressureSpeedsLayout.isShown()?View.GONE:View.GONE);
			
			txtTimeCount.setText("0");
			ckb_colorlight.setChecked(false);
			if(countTime!=null){
			countTime.cancel();}
			unkstyCount=0;
//			setupView();
//			LayoutInflater inflater=getLayoutInflater();
//			View view=inflater.inflate(R.layout.op_auto_ckb_item,null);
			
			RokolUtil.showToast(getApplicationContext(), getResources()
					.getString(R.string.power_off_message), 1);
			}
		
		if (readBuf[1] == DataFrame.mStop)
			// mPower = false;
			// the last line
			btnStop.setSelected(false);
			mReceiveData = new String[] { "" };
	    }

	
	private void changePowerState() {
		btnPower.setSelected(mPower);
		Log.d(TAG, "changePowerState");
	}

	// verify shoulder height
	protected void verifyShoulderHeight() {
		// TODO:
		if(adjustShoulderDialog!=null&&adjustShoulderDialog.isShowing())
			return;
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.adjust_shoulder_height, null);
		adjustShoulderDialog=new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.manual_shoulder))//
				.setView(layout)//
				//.setPositiveButton("ȷ��", null)//
				.setNegativeButton( R.string.cancel, null)//
				.show();
//		layout.getParent().requestDisallowInterceptTouchEvent(true);
		adjustShoulderDialog.setCanceledOnTouchOutside(false);
		imgShoulderUp=(ImageView) layout.findViewById(R.id.img_up);
		imgShoulderDown=(ImageView) layout.findViewById(R.id.img_down);
		imgShoulderUp.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				shoulderUp(event);
				return false;
			}
		});
		imgShoulderDown.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				shoulderDown(event);
				return false;
			}
		});//����һ��ontouchlistener
//		RokolUtil.performTouchSound(getBaseContext());
	}
	private void shoulderUp(MotionEvent event){
		Log.d(TAG, (event.getAction() == MotionEvent.ACTION_CANCEL)+"---"+(event.getAction() == MotionEvent.ACTION_UP));
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			imgShoulderUp.setImageDrawable(getResources().getDrawable(R.drawable.part_up_activated));
			sendCommand(DataFrame.getSendFrame(OPERATION.SHOULDER_UP));
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame.getSendFrame(OPERATION.SHOULDER_STOP));
			imgShoulderUp.setImageDrawable(getResources().getDrawable(R.drawable.part_up_normal));
		}
	}
	private void shoulderDown(MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.SHOULDER_DOWN));
			imgShoulderDown.setImageDrawable(getResources().getDrawable(R.drawable.part_up_activated));
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame.getSendFrame(OPERATION.SHOULDER_STOP));
			imgShoulderDown.setImageDrawable(getResources().getDrawable(R.drawable.part_up_normal));
		}
	}
	private void fixPointUp(MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BACK_ADJUST_UP));
//			imgup.setSelected(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BACK_ADJUST_STOP));
//			imgup.setSelected(false);
			}
	}
	private void fixPointDown(MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BACK_ADJUST_DOWN));
//			imgdown.setSelected(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BACK_ADJUST_STOP));
//			imgdown.setSelected(true);
			}
	}
	private void clickBackUp(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d(TAG2, "touch down back up----");
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_BACK_UP));
			mImageBackPosition.setSelected(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_BACK_STOP));
			mImageBackPosition.setSelected(false);
			Log.d(TAG2, "back up stop-------");
		}
		mSleep = false;
		mZeroGravity = false;
		mImageZeroGravity.setSelected(mZeroGravity);
		mImageSleep.setSelected(mSleep);
		// Toast.makeText(getApplication(), event.getAction()+"",
		// Toast.LENGTH_LONG).show();
		/*
		 * mImageBackup.setSelected(true); mImageBackdown.setSelected(false);
		 * mImageLegBend.setSelected(false);
		 * mImageLegStraighten.setSelected(false);
		 * mImageLegStretch.setSelected(false);
		 * mImageLegShrink.setSelected(false); mImage_1_3.setSelected(false);
		 * mImage_1_6.setSelected(false);
		 */
	}
	private void clickSleep() {
		if (!mSleep) {
			// open sleep

			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_SLEEP));

		} else {
			sendCommand(DataFrame
					.getSendFrame(OPERATION.BRACE_ZERO_GRAVITY_SLEEP_STOP));
		}
		mSleep = !mSleep;
		mImageSleep.setSelected(mSleep);
		mZeroGravity = false;
		mImageZeroGravity.setSelected(mZeroGravity);
		}
	private void clickZeroGravity() {
		if (!mZeroGravity) {
			// open zero gravity
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_ZERO_GRAVITY));

		} else {
			sendCommand(DataFrame
					.getSendFrame(OPERATION.BRACE_ZERO_GRAVITY_SLEEP_STOP));
		}
		mZeroGravity = !mZeroGravity;
		mImageZeroGravity.setSelected(mZeroGravity);
		mSleep = false;
		mImageSleep.setSelected(mSleep);
		/*
		 * mImageBackup.setSelected(false); mImageBackdown.setSelected(false);
		 * mImageLegBend.setSelected(false);
		 * mImageLegStraighten.setSelected(false);
		 * mImageLegStretch.setSelected(false);
		 * mImageLegShrink.setSelected(false); mImage_1_3.setSelected(true);
		 * mImage_1_6.setSelected(false);
		 */

	}
	private void clickLegShrink(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_LEG_SHRINK));
			mImageLegShrink.setSelected(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame
					.getSendFrame(OPERATION.BRACE_LEG_STRETCH_SHRINK_STOP));
			mImageLegShrink.setSelected(false);
		}
		mSleep = false;
		mZeroGravity = false;
		mImageZeroGravity.setSelected(mZeroGravity);
		mImageSleep.setSelected(mSleep);
		/*
		 * mImageBackup.setSelected(false); mImageBackdown.setSelected(false);
		 * mImageLegBend.setSelected(false);
		 * mImageLegStraighten.setSelected(false);
		 * mImageLegStretch.setSelected(false);
		 * mImageLegShrink.setSelected(true); mImage_1_3.setSelected(false);
		 * mImage_1_6.setSelected(false);
		 */
	}

	private void clickBackDown(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_BACK_DOWN));
			mImageBackdown.setSelected(true);
			Log.d(TAG2, "touch down back down----");
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_BACK_STOP));
			mImageBackdown.setSelected(false);
		}
		mSleep = false;
		mZeroGravity = false;
		mImageZeroGravity.setSelected(mZeroGravity);
		mImageSleep.setSelected(mSleep);
		/*
		 * mImageBackup.setSelected(false); mImageBackdown.setSelected(true);
		 * mImageLegBend.setSelected(false);
		 * mImageLegStraighten.setSelected(false);
		 * mImageLegStretch.setSelected(false);
		 * mImageLegShrink.setSelected(false); mImage_1_3.setSelected(false);
		 * mImage_1_6.setSelected(false);
		 */
	}

	private void clickLegBend(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_LEG_BEND));
			mImageLegBend.setSelected(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame
					.getSendFrame(OPERATION.BRACE_LEG_STRAIGHTEN_BEND_STOP));
			mImageLegBend.setSelected(false);
		}
		mSleep = false;
		mZeroGravity = false;
		mImageZeroGravity.setSelected(mZeroGravity);
		mImageSleep.setSelected(mSleep);
		/*
		 * mImageBackup.setSelected(false); mImageBackdown.setSelected(false);
		 * mImageLegBend.setSelected(true);
		 * mImageLegStraighten.setSelected(false);
		 * mImageLegStretch.setSelected(false);
		 * mImageLegShrink.setSelected(false); mImage_1_3.setSelected(false);
		 * mImage_1_6.setSelected(false);
		 */
	}

	private void clickLegStraighten(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_LEG_STRAIGHTEN));
			mImageLegStraighten.setSelected(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame
					.getSendFrame(OPERATION.BRACE_LEG_STRAIGHTEN_BEND_STOP));
			mImageLegStraighten.setSelected(false);
		}
		mSleep = false;
		mZeroGravity = false;
		mImageZeroGravity.setSelected(mZeroGravity);
		mImageSleep.setSelected(mSleep);
		/*
		 * mImageBackup.setSelected(false); mImageBackdown.setSelected(false);
		 * mImageLegBend.setSelected(false);
		 * mImageLegStraighten.setSelected(true);
		 * mImageLegStretch.setSelected(false);
		 * mImageLegShrink.setSelected(false); mImage_1_3.setSelected(false);
		 * mImage_1_6.setSelected(false);
		 */
	}

	private void clickLegStretch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sendCommand(DataFrame.getSendFrame(OPERATION.BRACE_LEG_STRETCH));
			mImageLegStretch.setSelected(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			sendCommand(DataFrame
					.getSendFrame(OPERATION.BRACE_LEG_STRETCH_SHRINK_STOP));
			mImageLegStretch.setSelected(false);
		}
		mSleep = false;
		mZeroGravity = false;
		mImageZeroGravity.setSelected(mZeroGravity);
		mImageSleep.setSelected(mSleep);
		/*
		 * mImageBackup.setSelected(false); mImageBackdown.setSelected(false);
		 * mImageLegBend.setSelected(false);
		 * mImageLegStraighten.setSelected(false);
		 * mImageLegStretch.setSelected(true);
		 * mImageLegShrink.setSelected(false); mImage_1_3.setSelected(false);
		 * mImage_1_6.setSelected(false);
		 */
	}
	
		
		
	

	
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			showMoreMenu();
		}
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				RokolUtil.showToast(getApplicationContext(), getResources()
						.getString(R.string.back_toast), 0);
				exitTime = System.currentTimeMillis();
			} else {
				this.finish();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "requestCode " + requestCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
				Log.d(TAG, "qi dong DeviceListActivity-------");

			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// connectDevice(data, false);
				
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupService();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "BT not enabled");
				RokolUtil.showToast(getApplicationContext(), getResources()
						.getString(R.string.bt_not_enabled_leaving), 1);
				finish();
			}
		}
	}
	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		Log.v(TAG, " Attempt to connect to address:" + address);
		Intent intent = new Intent(BLUETOOTHRECEIVER_ACTION);
		intent.putExtra("cmd", BluetoothService.REQUIRE_CONNECT);
		intent.putExtra(DEVICE_ADDRESS, address);
		sendBroadcast(intent);
	}
	private void closeSocket(){
		Intent intent=new Intent(BLUETOOTHRECEIVER_ACTION);
		intent.putExtra("cmd", 10);
		sendBroadcast(intent);
	}
	

	/**
	 * bluetoothReciver listen to the feedback from bluetoothservice
	 * 
	 * @author
	 * 
	 */
	public class BluetoothReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.lxx")) {
				Log.v(TAG, "receive message from service  ");
				
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt(BluetoothService.BUNDLE_TYPE);
				Log.v(TAG, "receive message from service  " + cmd);
				switch (cmd) {
				case MESSAGE_STATE_CHANGE:

					mBluetoothServiceState = bundle.getInt("state");
					Log.v(TAG, "receive mBluetoothServiceState from service  "
							+ mBluetoothServiceState);
					
					if(mBluetoothServiceState==BluetoothService.STATE_CONNECTED){
						ckb_bt.setSelected(true);
						Log.d(TAG2, "bluetooth connected -----");
						receivedataFlag=0;
					}
					else if(receivedataFlag==4){
//					mPower=false;
//					changePowerState();
					ckb_bt.setSelected(false);
				    }
					Log.d(TAG2, "bluetooth disconnected -----"+receivedataFlag++);
					break;
				case MESSAGE_TOAST:
					String message = bundle.getString(TOAST);
					Log.d(TAG, " Message-TOAST " + message);
					if(message.equalsIgnoreCase("ccttd")){
						message=getResources().getString(R.string.could_not_connect);
//						RokolUtil.showToast(getApplicationContext(), 
//								getResources().getString(R.string.could_not_connect), 1);
						}
					if(message.equalsIgnoreCase("lost")){
						message=getResources().getString(R.string.connection_lost);
						}
					RokolUtil.showToast(getApplicationContext(), message, 1);
//					onStart();
					break;
				case MESSAGE_DEVICE_NAME:
					// save the connected device's address
					String connectedDeviceName = bundle.getString(DEVICE_NAME);
					String connectedDeviceAddress = bundle
							.getString(DEVICE_ADDRESS);
					RokolUtil.showToast(getApplicationContext(),
							getResources().getString(R.string.connected_to) +""+ connectedDeviceName, 1);
					sp.edit().putString("Address", connectedDeviceAddress)
							.commit();
					sp.edit().putString("DeviceName", connectedDeviceName)
							.commit();
					break;
				case MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) bundle.getByteArray("buffer");
					// construct a string from the buffer
					// String writeMessage = new String(writeBuf);
					mSendData = RokolUtil.bytesToHexStrings(writeBuf,
							writeBuf.length);
					break;
				case MESSAGE_READ:
					byte[] buffer = bundle.getByteArray("buffer");
					int length = bundle.getInt("length");
					Log.d(TAG,
							"read: length="
									+ length
									+ "   "
									+ RokolUtil
											.bytesToHexString(buffer, length));
					// TODO: mark here for power
					ensureFeedBack(buffer, length);
					break;
				case MESSAGE_REMAIN_TIME:
					int minutes = bundle.getInt("RemainTime");
					if (D)
						Log.d(TAG, "remain minutes:" + minutes);
					RokolUtil.showToast(getApplicationContext(),
							"remain minutes:" + minutes, 1);

					break;
				}

			}
		}
	}
	
	protected void PreHeat(byte b){
    	int i= ((b>>4)&0x1)*1;
    	
    	if((byte)((b>>4)&0x1)==1){
    		preHeatFlag=true;
			Log.d("state", "set----------- pre heat back"+i);
			
		}else{
			preHeatFlag=false;
		}
	}
	protected void PreHeatFeet(byte b){
    	
    	if((byte)((b>>5)&0x1)==1){
    		preHeatFeetFlag=true;
//    		txtTemp.setText(R.string.preheating);
			Log.d(TAG2, "set-----------preHeatFeetFlag---"+preHeatFeetFlag);
//			imgHeatFeet.setImageBitmap(BitmapFactory.
//					decodeResource(getResources(), R.drawable.state_heat_feet_pre));
		}
    	else{preHeatFeetFlag=false;
    	Log.d(TAG2, "set-----------preHeatFeetFlag---"+preHeatFeetFlag);}
	}
	protected void setHeatClose(){
//		imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//				R.drawable.state_heat_feet));
//		imgHeat.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//				R.drawable.state_heat));
	}
	/**
	 * set shoulder state
	 * 
	 * @param b
	 */
	protected void setShoulderState(byte b) {
		if ((byte) ((b >> 1) & 0x1) == 1) {
			setImage(imgShoulder1, true);
			setImage(imgShoulder2, true);
		} else {
			setImage(imgShoulder1, false);
			setImage(imgShoulder2, false);
		}
	}
    protected void setZeroState(byte b){
//    	if((byte)((b>>5)&0x1)==1){
//			imgZero.setImageBitmap(BitmapFactory.decodeResource(getResources(), 
//					R.drawable.state_zero_gravity_on));
//		}
//		else{
//			imgZero.setImageBitmap(BitmapFactory.decodeResource(getResources(), 
//					R.drawable.state_zero_gravity));
//		}
	}
	protected void setShiatsuState(byte b){
		int k=((b>>4)&0x1)*1+((b>>5)&0x1)*2;
//		if(k==2){
//			Log.d("state", "set Shiatsu state" );
//			imgShiatsu.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//					R.drawable.state_shiatsu_on));
//		}
//		else{
//			imgShiatsu.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//					R.drawable.state_shiatsu));
//		}
	}
    protected void setTapState(byte b){
    	int k=((b>>4)&0x1)*1+((b>>5)&0x1)*2;
    	Log.d("state", "set tap==1 state"+k );
//    	if(k==1){
//    		imgTap.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//    				R.drawable.state_tap_on));;
//    	}else{
//    		imgTap.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//    				R.drawable.state_tap));;
//    	}
    }
	protected void setKnockState(byte b){
		int k=((b>>4)&0x1)*1+((b>>5)&0x1)*2;
		Log.d("state", "set knock==3 state"+k );
//		if(k==3){
//			imgKnock.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//					R.drawable.state_knock_on));
//		}
//		else{
//			imgKnock.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//					R.drawable.state_knock));
//		}
	}
	protected void setHeatState(byte b){
		if((byte)((b>>6)&0x1)==1){
			Log.d("TAG2", "set machine heat" );
			heatBackFlag=true;
		}
		else 
			{heatBackFlag=false;
		    preHeatFlag=false;
		Log.d("TAG2", "set machine heat off----" );}
		
	}
	protected void setFeetStatus(){
		if(thermalFeetFlag & footSwingOn){
			Log.d(TAG2, "set swing & heat on" );
			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.state_heat_swing_on));
		}
		if(thermalFeetFlag & !footSwingOn){
			Log.d(TAG2, "set  heat foot  on" );
			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.state_heat_feet_on));
		}
		if(preHeatFeetFlag & footSwingOn){
			Log.d(TAG2, "set swing & pre_heat on" );
			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.state_preheat_swing_on));
		}
		if(preHeatFeetFlag & !footSwingOn){
			Log.d(TAG2, "set pre_heat feet on" );
			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.state_heat_feet_pre));
		}
		if(!thermalFeetFlag & footSwingOn){
			Log.d(TAG2, "Statue set  swing on" );
			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.state_swing_on));
		}
		if(!thermalFeetFlag & !footSwingOn & !preHeatFeetFlag){
			Log.d(TAG2, "Statue set  feet statues off" );
			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.state_heat_feet));
		}
		
	}
	protected void setHeatFeet(byte b){
		int k=((b>>6)&0x1);
		if((byte)((b>>6)&0x1)==1){
			Log.d(TAG2, "----------- thermalFeetFlag on"+thermalFeetFlag);
			thermalFeetFlag=true;
//			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//					R.drawable.state_heat_feet_on));
		}else //if(!preHeatFeetFlag)
		{
			thermalFeetFlag=false;
			Log.d(TAG2, "------set----- thermalFeetFlag off"+thermalFeetFlag);
//			imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//			R.drawable.state_heat_feet));
//			imgHeatFeet.setAlpha(0.0f);
		}
	}
	
	protected void setStrength(byte b){
		int i=((b>>0)&0x1)*1+((b>>1)&0x1)*2;
		Log.d(TAG2, "strength i=" + i);
		switch(i){
		case 0:
			mImgStrength_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_off));;
			break;
		case 1:
			mImgStrength_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_low));;
//			txtStrength.setText(R.string.strength_low);
			break;
		case 2:
			mImgStrength_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_middle));;
//			txtStrength.setText(R.string.strength_middle);
			break;
		case 3:
			mImgStrength_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_high));;
//			txtStrength.setText(R.string.strength_high);
			break;
		}
	}
    protected void setKneadstate(byte b){
    	int i= ((b>>0)&0x1)*1+((b>>1)&0x1)*2;
//    	if(i!=0){
//    		imgKnead.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//    				R.drawable.state_knead_on));;
//    	}else{
//    		imgKnead.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//    				R.drawable.state_knead));;
//    	}
    }

	
	protected void setSpeed(byte b){
		
		int i= ((b>>0)&0x1)*1+((b>>1)&0x1)*2;
		int k=((b>>2)&0x1)*1+((b>>3)&0x1)*2;
		if (k>i){i=k;}
		Log.d(TAG2, "speed img is" + i);
		switch(i){
		case 0:
			mImgSpeed_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_off));;
			break;
		case 1:
			mImgSpeed_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_low));;
//			txtSpeed.setText(R.string.lower);
			break;
		case 2:
			mImgSpeed_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_middle));;
//			txtSpeed.setText(R.string.middle);
			break;
		case 3:
			mImgSpeed_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_speed_high));;
//			txtSpeed.setText(R.string.higher);
			break;
		
		}
	}
	protected void setWidth(byte b){
		int i=((b>>4)& 0x1)*1+((b>>5)&0x1)*2;
		Log.d(TAG2, "narrow Width i=" + i);
		
		switch(i){
		case 0:
			mImgWidth_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_width_off));;
			break;
		case 1:
			mImgWidth_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_width));;
			break;
		case 2:
			mImgWidth_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    				R.drawable.op_state_middle));;
			break;
		case 3:
			
			mImgWidth_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
    	    				R.drawable.op_state_narrow));;
			break;
		}
	}

	/**
	 * set machine position
	 * 
	 * @param b
	 */
	protected void setMachinePosition(byte b) {

		int i = ((b >> 0) & 0x1) * 1 + ((b >> 1) & 0x1) * 2 + ((b >> 2) & 0x1)
				* 4 + ((b >> 3) & 0x1) * 8;
		Log.d("state", "i=" + i);
		switch (i) {
		case 0:
			setHeatImage(imgSeat3, true);
			//
			setImage(imgWaist11, false);
			setImage(imgWaist12, false);
			setImage(imgWasit21, false);
			setImage(imgWaist22, false);
			setImage(imgWasit31, false);
			setImage(imgWaist32, false);
			setImage(imgBack11, false);
			setImage(imgBack12, false);
			setImage(imgBack21, false);
			setImage(imgBack22, false);
			setImage(imgNeck1, false);
			setImage(imgNeck2, false);
			break;
		case 3:
			setHeatImage(imgWaist11, true);
			setHeatImage(imgWaist12, true);
			//
			setImage(imgSeat3, false);
			setImage(imgWasit21, false);
			setImage(imgWaist22, false);
			setImage(imgWasit31, false);
			setImage(imgWaist32, false);
			setImage(imgBack11, false);
			setImage(imgBack12, false);
			setImage(imgBack21, false);
			setImage(imgBack22, false);
			setImage(imgNeck1, false);
			setImage(imgNeck2, false);
			break;
		case 4:
			setHeatImage(imgWasit21, true);
			setHeatImage(imgWaist22, true);
			//
			setImage(imgSeat3, false);
			setImage(imgWaist11, false);
			setImage(imgWaist12, false);
			setImage(imgWasit31, false);
			setImage(imgWaist32, false);
			setImage(imgBack11, false);
			setImage(imgBack12, false);
			setImage(imgBack21, false);
			setImage(imgBack22, false);
			setImage(imgNeck1, false);
			setImage(imgNeck2, false);
			break;
		case 5:
			setHeatImage(imgWasit31, true);
			setHeatImage(imgWaist32, true);
			//
			setImage(imgSeat3, false);
			setImage(imgWaist11, false);
			setImage(imgWaist12, false);
			setImage(imgWasit21, false);
			setImage(imgWaist22, false);
			setImage(imgBack11, false);
			setImage(imgBack12, false);
			setImage(imgBack21, false);
			setImage(imgBack22, false);
			setImage(imgNeck1, false);
			setImage(imgNeck2, false);
			break;
		case 6:
			setHeatImage(imgBack11, true);
			setHeatImage(imgBack12, true);
			//
			setImage(imgSeat3, false);
			setImage(imgWaist11, false);
			setImage(imgWaist12, false);
			setImage(imgWasit21, false);
			setImage(imgWaist22, false);
			setImage(imgWasit31, false);
			setImage(imgWaist32, false);
			setImage(imgBack21, false);
			setImage(imgBack22, false);
			setImage(imgNeck1, false);
			setImage(imgNeck2, false);
			break;
		case 7:
			setHeatImage(imgBack21, true);
			setHeatImage(imgBack22, true);
			//
			setImage(imgSeat3, false);
			setImage(imgWaist11, false);
			setImage(imgWaist12, false);
			setImage(imgWasit21, false);
			setImage(imgWaist22, false);
			setImage(imgWasit31, false);
			setImage(imgWaist32, false);
			setImage(imgBack11, false);
			setImage(imgBack12, false);
			setImage(imgNeck1, false);
			setImage(imgNeck2, false);
			break;
		case 8:
			setHeatImage(imgNeck1, true);
			setHeatImage(imgNeck2, true);
			//
			setImage(imgSeat3, false);
			setImage(imgWaist11, false);
			setImage(imgWaist12, false);
			setImage(imgWasit21, false);
			setImage(imgWaist22, false);
			setImage(imgWasit31, false);
			setImage(imgWaist32, false);
			setImage(imgBack11, false);
			setImage(imgBack12, false);
			setImage(imgBack21, false);
			setImage(imgBack22, false);
			break;
		}
	}
	private void ShowTimerDialog() {
		final int timer = mApp.getCurrentTimer();
		new AlertDialog.Builder(this)
		// .setTitle(mContext.getResources().getString(R.string.language))
		// .setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(
						new String[] {
								       this.getResources().getString(
										    R.string.timer_0),
										this.getResources().getString(
										    R.string.timer_1),
										this.getResources().getString(
										    R.string.timer_2) }, timer,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (timer != which) {
									setTimer(which);

								}
								dialog.dismiss();
							}
						}).show();

	}
	private void setTimer(int choice) {
		/*
		 * if (!MainActivity.mPower) { // TODO translation
		 * Toast.makeText(mContext, "please turn on the massage chair firstly!",
		 * Toast.LENGTH_SHORT).show(); return; }
		 */
		mApp.setCurrentTimer(choice);
//		mTextTimer.setText(mApp.TimerToString());
		// send broadcast to start stop timer
		/*
		 * Intent it = new Intent("com.kangtai.MassageChair.StopTimerService");
		 * it.putExtra("time", choice); it.putExtra("start", true);
		 * mContext.sendBroadcast(it);
		 */
		 if(countTime!=null)countTime.cancel();
		 
		switch (choice) {
		case 0:
					sendCommand(DataFrame.getSendFrame(OPERATION.TIMER_10));
					countTime=new MyCount(10*60000, 60000);
					countTime.start();
			break;
		case 1:
					sendCommand(DataFrame.getSendFrame(OPERATION.TIMER_20));
					countTime=new MyCount(20*60000, 60000);
					countTime.start();
			break;
		case 2:
					sendCommand(DataFrame.getSendFrame(OPERATION.TIMER_30));
					countTime=new MyCount(30*60000, 60000);
					countTime.start();
			break;

		}
		Log.d("", "set timer !!" + choice);
	}

	
	private void callingDuomiMusic(){
		Intent comIntent=new Intent();
		ComponentName com=new ComponentName("com.android.music",
				"com.android.music.MusicBrowserActivity");
		comIntent.setComponent(com);
		comIntent.setAction("android.intent.action.MAIN");
		startActivity(comIntent);
	}
	private void ShowLanguageDialog() {
		final int language = mApp.getCurrentLanguage();
		new AlertDialog.Builder(this)
		// .setTitle(mContext.getResources().getString(R.string.language))
		// .setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(
						new String[] {
								getApplicationContext().getResources().getString(
										R.string.chinese),
										getApplicationContext().getResources().getString(
										R.string.english),
										getApplicationContext().getResources().getString(
										R.string.korean) }, language,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (language != which)
									setLanguage(which);
								dialog.dismiss();
							}
						}).show();

	}
	private void setLanguage(int choice) {

		Intent intent = new Intent("com.kangtai.rokol.language");
		mApp.setCurrentLanguage(choice);

		this.sendBroadcast(intent);
		this.finish();

		// Toast.makeText(mContext, "---", Toast.LENGTH_SHORT).show();
	}
	private boolean checkPackageExist(){
		List<PackageInfo> packages = this.getPackageManager().getInstalledPackages(0);
		
		for(int i=0;i<packages.size();i++){
			PackageInfo packageinfo=packages.get(i);
			System.out.println("------packageName"+packageinfo.packageName + ""+ packages.size());
			if(packageinfo.packageName.equals("com.android.music")){
				return true;
			}
			
		}
		return false;
	}
	protected void setAirPosition(byte b) {
		if ((byte) ((b >> 6) & 0x1) == 1) {

		} else {

		}

		if ((byte) ((b >> 5) & 0x1) == 1) {
			setImage(imgArm1, true);
			setImage(imgArm2, true);
		} else {
			setImage(imgArm1, false);
			setImage(imgArm2, false);
		}
		if ((byte) ((b >> 4) & 0x1) == 1) {
			setImage(imgWaist11, true);
			setImage(imgWaist12, true);
		} else {
			setImage(imgWaist11, false);
			setImage(imgWaist12, false);

		}
		if ((byte) ((b >> 3) & 0x1) == 1) {
			setImage(imgSeat1, true);
			setImage(imgSeat2, true);

		} else {
			setImage(imgSeat1, false);
			setImage(imgSeat2, false);
		}
		if ((byte) ((b >> 2) & 0x1) == 1) {
			setImage(imgLeg1, true);
			setImage(imgLeg2, true);
		} else {
			setImage(imgLeg1, false);
			setImage(imgLeg2, false);
		}
		if ((byte) ((b >> 1) & 0x1) == 1) {
			setImage(imgFoot1, true);
			setImage(imgFoot2, true);
		} else {
			setImage(imgFoot1, false);
			setImage(imgFoot2, false);
		}
//B0 ���� 8900
		if ((byte) ((b >> 0) & 0x1) == 1) {
	    		footSwingOn=true;
	    		Log.d(TAG2, "set foot swing on----------"+footSwingOn);
	    		if(zuliaoSpeedFase){mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_high));;}
//	    		imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//	    				R.drawable.state_swing_on));;
	    		mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_middle));;
	    	}else{
	    		footSwingOn=false;
	    		Log.d(TAG2, "set foot swing off----------"+footSwingOn);
//	    		imgHeatFeet.setImageBitmap(BitmapFactory.decodeResource(getResources(),
//	    				R.drawable.state_swing));;
	    		mImgPedicure_State.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	    				R.drawable.op_state_speed_off));;
	    	
	    }
	}

	

	private void setImage(ImageView image, Boolean enable) {
		image.setImageBitmap(BitmapFactory.decodeResource(getResources(),
				enable ? R.drawable.enable : R.drawable.disable));
		
	}
	private void setHeatImage(ImageView image, Boolean enable) {
		Log.d("FragemntState", "preHeatFlag ----- heatBackFlag" +preHeatFlag+ heatBackFlag);
		if(preHeatFlag)
		image.setImageBitmap(BitmapFactory.decodeResource(getResources(),
				enable ? R.drawable.pre_heat_dot : R.drawable.disable));
		else if(heatBackFlag)
			image.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					enable ? R.drawable.heat_dot : R.drawable.disable));
		else
			image.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					enable ? R.drawable.enable : R.drawable.disable));
	}

	private void setTextDrawabe(Drawable drawable,TextView text,int drawableid,int textviewid){
		drawable= getResources().getDrawable(drawableid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		text.setCompoundDrawables(drawable,null,null,null);
		text.setText(getResources().getString(textviewid));
	}

	/**
	 * ByteתBit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}
	class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
        	
            super(millisInFuture, countDownInterval);    
            txtTimeCount.setText(millisInFuture/60000+"");
            Log.d("getUpdateVersion", "init timer -----"+millisInFuture/60000);
        }     
        @Override     
        public void onFinish() {     
        	txtTimeCount.setText("0");
        	kstyFlag=false;
        	sendCommand(DataFrame.getSendFrame(OPERATION.POWER_OFF));
        }     
        @Override     
        public void onTick(long millisUntilFinished) {
        	long t=(millisUntilFinished / 60000)+1;
        	Log.d("getUpdateVersion", "countdown time is -----"+t);
        	
        	txtTimeCount.setText(t+"");   
        	txtTimeCount.setTextColor(Color.BLACK);
        	
        }    
    }     


private final class TouchListener implements OnTouchListener{
	private  PointF point=new PointF();
	private Matrix currentMatrix=new Matrix();
	private Matrix matrix=new Matrix();
	private boolean upFlag=false,moveFlag=false;
	private float dy;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.getParent().requestDisallowInterceptTouchEvent(true);

		// TODO Auto-generated method stub
//		if(v.getId()==R.id.img_drag2)img=img2;else img=img1;
//		img=(ImageView)v;
		switch(v.getId()){
		case R.id.img_back_positon:                //case R.id.img_back_up:   0911
			clickBackUp(event);
			break;
		case R.id.img_back_down:
			Log.d(TAG2, "click img_back_down----");
			clickBackDown(event);
			break;
		case R.id.img_foot_position:
			Log.d(TAG2, "click img_leg_up----"); //foot up 1023
			clickLegStraighten(event);
			break;
		case R.id.img_foot_down:
			Log.d(TAG2, "click img_leg_down----"); //foot down 1023
			clickLegBend(event);
			break;
		case R.id.img_foot_shen:
			Log.d(TAG2, "click  img_leg_shen----");//shen 1023
			clickLegStretch(event);
			break;
		case R.id.img_foot_stretch:
			Log.d(TAG2, "click  img_leg_suo----");//suo 1023
			clickLegShrink(event);
			break;
		}
		return true;
	}}


@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	// TODO Auto-generated method stub
	switch(buttonView.getId()){
	case R.id.ckb_other_zuliao:
		setZuliao(isChecked);
		break;
	case R.id.ckb_other_thermal:
		setFeetHeat(isChecked);
		break;
	case R.id.ckb_other_feet_thermal:
		setBackHeat(isChecked);
		break;
	case R.id.ckb_other_music:
		setMusic();
		break;
	case R.id.ckb_sthh:
		setAutoSthh(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
//		setCountTime();
//		unkstyFlag=true;
		break;
	case R.id.ckb_xfbj:
//		setCountTime();
//		unkstyFlag=true;
		setAutoXsbj(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
		break;
	case R.id.ckb_xyfs:
//		setCountTime();
//		unkstyFlag=true;
		setAutoXyfs(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
		break;
	case R.id.ckb_gwdr:
//		setCountTime();
//		unkstyFlag=true;
		setAutoGwdr(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
		break;
	case R.id.ckb_zyys:
		setAutoZyys(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
//		setCountTime();
//		unkstyFlag=true;
		break;
	case R.id.ckb_ylam:
		setAutoYlam(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
//		unkstyFlag=true;
//		setCountTime();
		break;
	case R.id.ckb_ksty:
		kstyFlag=isChecked;
		setAutoKsty(isChecked);
//		if(countTime!=null)countTime.cancel();
//		new MyCount(9*60000, 60000).start();
//		countTime.start();
//		if(isChecked){setCountTime();
//		Log.d("getUpdateVersion","ksty ischecked"+isChecked);}
		Log.d("getUpdateVersion", "ksty checked"+isChecked);
		break;
	case R.id.ckb_yjkb:
//		setCountTime();
//		unkstyFlag=true;
		setAutoYjkb(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
		break;
	case R.id.ckb_ydyms:
//		unkstyFlag=true;
//		setCountTime();
		setAutoYdyms(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
		break;
	case R.id.ckb_blsbz:
//		unkstyFlag=true;
//		setCountTime();
		setAutoBlsbz(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
		break;
	case R.id.ckb_ysbj:
//		unkstyFlag=true;
		setAutoYsbj(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
//		setCountTime();
		break;
	case R.id.ckb_jgsz:
//		unkstyFlag=true;
//		setCountTime();
		setAutoJgsz(isChecked);zuliaoOn=false;ckb_zuliao.setChecked(false);
		break;
		
	case R.id.ckb_leftview_home:
		Intent intent=new Intent();
		   intent.setClass(OperationActivityHD.this,MainActivityHD.class );
		   startActivity(intent);
		   this.finish();
		Log.d(TAG2, "check box home----------");
		break;
	case R.id.ckb_leftview_language:
		ShowLanguageDialog();
		 
		Log.d(TAG2, "check box language----------");
		break;
	case R.id.ckb_leftview_timer:
		ShowTimerDialog();
		
		break;
	case R.id.ckb_leftview_colorlight:
		if(isChecked){
//			   if (lightFlag) {
	   				
	 				  sendCommand(DataFrame.getSendFrame(OPERATION.LED_ON));
	 				 RokolUtil.setTextShadow(getBaseContext(), ckb_colorlight);
		    Log.d(TAG2, "check box ckb_leftview_colorlight on----------");}
		else{
			sendCommand(DataFrame.getSendFrame(OPERATION.LED_OFF));
			 RokolUtil.setTextNoShadow(getBaseContext(), ckb_colorlight);
			Log.d(TAG2, "check box ckb_leftview_colorlight off----------");
		}
		break;
	case R.id.ckb_leftview_sound:
		if(isChecked){
//			if (touchSoundFlag) {
	   				mApp.setSoundSwitch(true);
	   				RokolUtil.setTextShadow(getBaseContext(), ckb_sound);
			Log.d(TAG2, "check box ckb_leftview_sound on----------");}
			else{
				mApp.setSoundSwitch(false);
				RokolUtil.setTextNoShadow(getBaseContext(), ckb_sound);
				Log.d(TAG2, "check box ckb_leftview_sound off----------");
			}
		break;
		
	case R.id.ckb_leftview_bluetooth:
		startConectPairedDevice();

		
		break;
	case R.id.ckb_leftview_update:
		if (!RokolUtil.checkNetWorkStatus(getBaseContext())) {
			RokolUtil.performTouchSound(getBaseContext());
			RokolUtil.showToast(getBaseContext(), "network is not available", 1);
			return;
		}
		try {
			mUpdateManager=new UpdateManager(OperationActivityHD.this);
			mUpdateManager.checkToUpdate();
			// Log.d(
			// "start update","----------------- click update check");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Log.d( TAG2,e.toString()+"update exception----");
		}
        
		
		 break;
	}
	
	//set timer
	 Log.d( "getUpdateVersion","ckb id"+buttonView.getId());
	 
	if(buttonView.getId()==R.id.ckb_ksty)
	{
		if(kstyFlag){  
			setCountTime(8); unkstyFlag=false;unkstyCount=0;zuliaoSpeedFase=false;
			zuliaoOn=false;ckb_zuliao.setChecked(false);
	     Log.d( "getUpdateVersion","set ksty timer ");}}
	
	else if(buttonView.getId()==R.id.ckb_blsbz | buttonView.getId()==R.id.ckb_gwdr|
			buttonView.getId()==R.id.ckb_jgsz|buttonView.getId()==R.id.ckb_sthh|
			buttonView.getId()==R.id.ckb_xfbj|buttonView.getId()==R.id.ckb_xyfs|
			buttonView.getId()==R.id.ckb_ydyms|buttonView.getId()==R.id.ckb_yjkb|
			buttonView.getId()==R.id.ckb_ylam|buttonView.getId()==R.id.ckb_ysbj|
			buttonView.getId()==R.id.ckb_zyys|buttonView.getId()==R.id.ckb_other_zuliao|
			buttonView.getId()==R.id.ckb_other_thermal|
			buttonView.getId()==R.id.ckb_other_feet_thermal
			){
		unkstyFlag=true;  setCountTime(); zuliaoSpeedFase=false;
		
	     Log.d( "getUpdateVersion","other timer flag");
	     }
	
	RokolUtil.performTouchSound(getBaseContext());
	Log.d(TAG2, "touch sound---------");
}

private void setCountTime(){
	 int time=0;
	 if(countTime!=null)countTime.cancel();
	
	 if(unkstyCount==0){time=20;unkstyCount++;}
	 else time=Integer.parseInt(txtTimeCount.getText().toString());
	 
	 Log.d("getUpdateVersion",time+" & unkstyFlag "+unkstyFlag);
	countTime=new MyCount((time)*60000, 30000);
	countTime.start();
	}
private void setCountTime(int time){
	 if(countTime!=null)countTime.cancel();
	countTime=new MyCount((time)*60000, 30000);
	countTime.start();
}
}

