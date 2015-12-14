package com.kangtai.MassageChairUI.Protocal;

import android.util.Log;

/*
 * @ Author:liumingtao 
 * @ Date:20140531
 * massage chair Protocol class 
 */
public class DataFrame {
	// from bluetooth
	public final static byte mAddress = (byte) 0xF1;
	// from hand controller
	public final static String TAG="DATAFRAME";
	public final static byte mHandControllerAddress = (byte) 0xF5;

	final public static byte mPower_ON = 0x10;
	final public static byte mPower_OFF = 0x11;
	//Temperature
	final public static byte mTemp_Raise=(byte) 0x9A;
	final public static byte mTemp_Reduce=(byte) 0x9B;

	final public static byte mStop = 0x12;
	final public static byte mStopTimer = 0x13;
	final public static byte mBurnning=0x1a;
	final public static byte mTimer_10 = (byte) 0xC1;
	final public static byte mTimer_20 = (byte) 0xC2;
	final public static byte mTimer_30 = (byte) 0xC3;
	final public static byte mTimer_45 = (byte) 0xc0;//2015'11'20
	//LED
	final public static byte mLed_ON= (byte) 0xB8;
	final public static byte mLed_OFF=(byte)0xB9;
	/**
	 * RK_ 7905
	 * 自动程序命令码：中医：0x20，酸痛：0x22，筋骨：0x24，快速体验：0x1e，特色：白领0x91，购物：0x92，瑜伽：0x93
	 * ，运动员：0x94，益寿：0x95" 逍遥放松 96   修身保健 98
	 */
	// Auto massage
	//
	final public static byte mFatigueRecovery_ON = 0x20;
	final public static byte mFatigueRecovery_OFF = 0x21;
	//
	final public static byte mPainEase_ON = 0x22;
	final public static byte mPainEase_OFF = 0x23;
	//
	final public static byte mMoodRelax_ON = 0x24;
	final public static byte mMoodRelax_OFF = 0x25;
	//
	final public static byte mXiaoYaoFangSong = (byte) 0x96;
	final public static byte mXiuShenBaoJian= (byte) 0x98;
	//
	final public static byte mRhythm_ON = 0x2E;
	// 3D
	final public static byte m3DExperience_ON = 0x1E;
	//
	final public static byte mOfficeWorker_ON = (byte) 0x91;
	//
	final public static byte mShopper_ON = (byte) 0x92;
	//
	final public static byte mYogiOpenBack_ON = (byte) 0x93;
	//
	final public static byte mAthleteMode_ON = (byte) 0x94;
	//
	final public static byte mHealth_ON = (byte) 0x95;
	//2015 11 20
	final public static byte mBack_Rubbing_ON=(byte) 0xA4;//cuo bei
	final public static byte mPulse_Shiatsu_ON = (byte)0xA5;//gun ya
	final public static byte mFinger_Knead_ON =(byte)0xA6;// zhi rou
	// whole
	final public static byte mBack_Whole_ON = 0x26;
	final public static byte mBack_Whole_OFF = (byte) (mBack_Whole_ON + 0x01);
	// shoulder
	final public static byte mBack_Shoulder_ON = 0x28;
	final public static byte mBack_Shoulder_OFF = (byte) (mBack_Shoulder_ON + 0x01);
	// back
	final public static byte mBack_Back_ON = 0x2A;
	final public static byte mBack_back_OFF = (byte) (mBack_Back_ON + 0x01);
	// waist
	final public static byte mBack_Waist_ON = 0x2C;
	final public static byte mBack_Waist_OFF = (byte) (mBack_Waist_ON + 0x01);
	// custom
	final public static byte mBack_Custom_ON = 0x30;
	final public static byte mBack_Custom_OFF = (byte) (mBack_Custom_ON + 0x01);
	// kneading
	final public static byte mBack_Kneading_ON = 0x32;
	final public static byte mBack_Kneading_OFF = (byte) (mBack_Kneading_ON + 0x01);
	// tapping
	final public static byte mBack_Tapping_ON = 0x34;
	final public static byte mBack_Tapping_OFF = (byte) (mBack_Tapping_ON + 0x01);
	// clapping
	final public static byte mBack_Clapping_ON = 0x36;
	final public static byte mBack_Clapping_OFF = (byte) (mBack_Clapping_ON + 0x01);
	// finger
	final public static byte mBack_Finger_ON = 0x38;
	final public static byte mBack_Finger_OFF = (byte) (mBack_Finger_ON + 0x01);
	// kneading+tapping
	final public static byte mBack_Kneading_Tapping_ON = 0x3A;
	final public static byte mBack_Kneading_Tapping_OFF = (byte) (mBack_Kneading_Tapping_ON + 0x01);
	// shoulder height
	final public static byte mBack_ShoulderHeight_1 = 0x3C;
	final public static byte mBack_ShoulderHeight_2 = (byte) (mBack_ShoulderHeight_1 + 0x01);
	final public static byte mBack_ShoulderHeight_3 = (byte) (mBack_ShoulderHeight_2 + 0x01);
	final public static byte mBack_ShoulderHeight_4 = (byte) (mBack_ShoulderHeight_3 + 0x01);
	final public static byte mBack_ShoulderHeight_5 = (byte) (mBack_ShoulderHeight_4 + 0x01);
	// part of back
	final public static byte mBack_All = 0x41;
	final public static byte mBack_Shoulder = 0x42;
	final public static byte mBack_Back = 0x43;
	final public static byte mBack_Waist = 0x44;
	final public static byte mBack_Fixed = 0x45;
	// adjust
	final public static byte mBack_Adjust_UP = 0x46;
	final public static byte mBack_Adjust_DOWN = 0x47;
	final public static byte mBack_Adjust_STOP = 0x48;
	// 3D
	final public static byte mBack_3D_FORWARD = 0x49;
	final public static byte mBack_3D_BACKWARD = 0x4A;
	final public static byte mBack_3D_STOP = 0x4B;
	// wide center narrow
	final public static byte mBack_WIDE = 0x4C;
	final public static byte mBack_CENTER = 0x4D;
	final public static byte mBack_NARROW = 0x4E;
	// speed
	final public static byte mBack_SPEED_1 = 0x50;
	final public static byte mBack_SPEED_2 = (byte) (mBack_SPEED_1 + 0x01);
	final public static byte mBack_SPEED_3 = (byte) (mBack_SPEED_2 + 0x01);
	final public static byte mBack_SPEED_4 = (byte) (mBack_SPEED_3 + 0x01);
	final public static byte mBack_SPEED_5 = (byte) (mBack_SPEED_4 + 0x01);
	/*
	 * Pressure massage
	 */
	// auto
	final public static byte mPressure_Auto_ON = 0x60;
	final public static byte mPressure_Auto_OFF = (byte) (mPressure_Auto_ON + 0x01);
	// stretch
	final public static byte mPressure_Stretch_ON = 0x62;
	final public static byte mPressure_Stretch_OFF = (byte) (mPressure_Stretch_ON + 0x01);
	// hand
	final public static byte mPressure_Hand_ON = 0x64;
	final public static byte mPressure_Hand_OFF = 0x65;
	// waist
	final public static byte mPressure_Waist_ON = 0x66;
	final public static byte mPressure_Waist_OFF = 0x67;
	// seat
	final public static byte mPressure_Seat_ON = 0x68;
	final public static byte mPressure_Seat_OFF = 0x69;
	// leg
	final public static byte mPressure_Leg_ON = 0x6A;
	final public static byte mPressure_Leg_OFF = 0x6B;
	// foot
	final public static byte mPressure_foot_ON = 0x6C;
	final public static byte mPressure_foot_OFF = 0x6D;
	// manual
	final public static byte mPressure_Manual_OFF = 0x6E;
	// speed
	final public static byte mPressure_SPEED_1 = 0x70;
	final public static byte mPressure_SPEED_2 = (byte) (mPressure_SPEED_1 + 0x01);
	final public static byte mPressure_SPEED_3 = (byte) (mPressure_SPEED_2 + 0x01);
	final public static byte mPressure_SPEED_4 = 0x73;
	final public static byte mPressure_SPEED_5 = 0x74;
	/*
	 * brace
	 */
	// leg
	final public static byte mBrace_Leg_Straighten = (byte) 0x80;
	final public static byte mBrace_Leg_Bend = (byte) 0x81;
	final public static byte mBrace_Leg_Straighten_Bend_Stop = (byte) 0x82;
	// back
	final public static byte mBrace_Back_Down = (byte) 0x83;
	final public static byte mBrace_Back_Up = (byte) 0x84;
	final public static byte mBrace_Back_Stop = (byte) 0x85;
	// leg
	final public static byte mBrace_Leg_Stretch = (byte) 0x86;
	final public static byte mBrace_Leg_Shrink = (byte) 0x87;
	final public static byte mBrace_Leg_Stretch_Shrink_Stop = (byte) 0x88;
	// stretch on/off
	final public static byte mBrace_Stretch_ON = (byte) 0x89;
	final public static byte mBrace_Stretch_OFF = (byte) 0x8A;
	// zero gravity/sleep
	final public static byte mBrace_Zero_Gravity = (byte) 0x8C;
	final public static byte mBrace_Sleep = (byte) 0xA8;
	final public static byte mBrace_Zero_Gravity_Sleep_Stop = (byte) 0x8D;
	/*
	 * VIBRATION MOTOR
	 */
	final public static byte mVibration_OFF = (byte) 0x90;
	final public static byte mVibration_SPEED_1_PowerOn = (byte) 0x91;
	final public static byte mVibration_SPEED_2 = (byte) (mVibration_SPEED_1_PowerOn + 0x01);
	final public static byte mVibration_SPEED_3 = (byte) (mVibration_SPEED_2 + 0x01);
	final public static byte mVibration_SPEED_4 = (byte) (mVibration_SPEED_3 + 0x01);
	final public static byte mVibration_SPEED_5 = (byte) (mVibration_SPEED_4 + 0x01);
	/*
	 * twist foot motor
	 */
	final public static byte mTwist_OFF = (byte) 0xA0;
	final public static byte mTwist_SPEED_1_PowerOn = (byte) 0xA1;
	final public static byte mTwist_SPEED_2 = (byte) (mTwist_SPEED_1_PowerOn + 0x01);
	final public static byte mTwist_SPEED_3 = (byte) (mTwist_SPEED_2 + 0x01);
	final public static byte mTwist_SPEED_4 = (byte) (mTwist_SPEED_3 + 0x01);
	final public static byte mTwist_SPEED_5 = (byte) (mTwist_SPEED_4 + 0x01);
	/**
	 * Read state
	 */
	final public static byte mReadState = (byte) 0xAA;

	/**
	 * music
	 */
	final public static byte mMusic_Power_ON = (byte) 0xB0;
	final public static byte mMusic_Power_OFF = (byte) 0xB7;
	final public static byte mMusic_Previous = (byte) 0xB3;
	final public static byte mMusic_Next = (byte) 0xB4;
	final public static byte mMusic_Volume_UP = (byte) 0xB5;
	final public static byte mMusic_Volume_DOWN = (byte) 0xB6;
	final public static byte mMusic_Play = (byte) 0xB1;
	final public static byte mMusic_Pause = (byte) 0xB2;
	/**
	 * zu liao
	 */
	final public static byte mPedicure_Fast = (byte) 0xCE;
	final public static byte mPedicure_Soft = (byte) 0xCC;
	final public static byte mPedicure_OFF = (byte) 0xCB;
	/**
	 * heat 
	 */
	//调换method/feet heat com   trs   0711
	 final public static byte mThermalWaist_ON  =  0x76;
	 final public static byte mThermalWaist_OFF   =  0x77;//1124
	 
	final public static byte mThemalMethod_ON = 0x78;
	final public static byte mThemalMethod_OFF = 0x79;
	final public static byte mThermalFeet_ON=0x7A;
	final public static byte mThermalFeet_OFF=0x7B;
	public static byte[] mSendData;
	/**
	 * shoulder height
	 * 
	 * @author
	 * 
	 */
	final public static byte mShoulderUp = 0x5C;
	final public static byte mShoulderDown = 0x5D;
	final public static byte mShoulderStop = 0x5E;

	public enum OPERATION {
		POWER_ON, POWER_OFF,
		
		LED_ON,LED_OFF,
		
		BURNNING_ON,TEMP_RAISE,TEMP_REDUCE,

		STOP, STOPTIMER, TIMER_10, TIMER_20, TIMER_30, TIMER_45,FATIGUERECOVERY_ON, FATIGUERECOVERY_OFF,
		/**
		 * pain ease
		 */
		PAINEASE_ON, PAINEASE_OFF,
		/**
		 * mood relax
		 */
		MOODRELAX_ON, MOODRELAX_OFF,
		/**
		 * yin lv
		 */
		XiaoYaoFangSong,
		
		XiuShenBaoJian,
		
		RHYTHM_ON,
		/**
		 * 3D
		 */
		_3DEXPERIENCE_ON,
		/**
		 * 
		 */
		OFFICEWORKER_ON,
		/**
		 * 
		 */
		SHOPPER_ON,
		/**
		 * 
		 */
		YOGIOPENBACK_ON,
		/**
		 * 
		 */
		ATHLETEMODE_ON,
		/**
		 * 
		 */
		HEALTH_ON,
		//GUN YA
		PULSE_SHIATSU,
		//ZHI ROU
		FINGER_KNEAD,
		//CUO BEI
		BACK_RUBBING,

		// WHOLE
		BACK_WHOLE_ON, BACK_WHOLE_OFF,
		// SHOULDER
		BACK_SHOULDER_ON, BACK_SHOULDER_OFF,
		// BACK
		BACK_BACK_ON, BACK_BACK_OFF,
		// WAIST
		BACK_WAIST_ON, BACK_WAIST_OFF,
		// CUSTOM
		BACK_CUSTOM_ON, BACK_CUSTOM_OFF,
		// KNEADING
		BACK_KNEADING_ON, BACK_KNEADING_OFF,
		// TAPPING
		BACK_TAPPING_ON, BACK_TAPPING_OFF,
		// CLAPPING
		BACK_CLAPPING_ON, BACK_CLAPPING_OFF,
		// FINGER
		BACK_FINGER_ON, BACK_FINGER_OFF,
		// KNEADING+TAPPING
		BACK_KNEADING_TAPPING_ON, BACK_KNEADING_TAPPING_OFF,
		// SHOULDER HEIGHT
		BACK_SHOULDERHEIGHT_1, BACK_SHOULDERHEIGHT_2, BACK_SHOULDERHEIGHT_3, BACK_SHOULDERHEIGHT_4, BACK_SHOULDERHEIGHT_5,
		// PART OF BACK
		BACK_ALL, BACK_SHOULDER, BACK_BACK, BACK_WAIST, BACK_FIXED,
		// ADJUST
		BACK_ADJUST_UP, BACK_ADJUST_DOWN, BACK_ADJUST_STOP,
		// 3D
		BACK_3D_FORWARD, BACK_3D_BACKWARD, BACK_3D_STOP,
		// WIDE CENTER NARROW
		BACK_WIDE, BACK_CENTER, BACK_NARROW,
		// SPEED
		BACK_SPEED_1, BACK_SPEED_2, BACK_SPEED_3, BACK_SPEED_4, BACK_SPEED_5,
		/**
		 * PRESSURE MASSAGE
		 */
		// AUTO
		PRESSURE_AUTO_ON, PRESSURE_AUTO_OFF,
		// STRETCH
		PRESSURE_STRETCH_ON, PRESSURE_STRETCH_OFF,
		// HAND
		PRESSURE_HAND_ON, PRESSURE_HAND_OFF,
		// WAIST
		PRESSURE_WAIST_ON, PRESSURE_WAIST_OFF,
		// SEAT
		PRESSURE_SEAT_ON, PRESSURE_SEAT_OFF,
		// LEG
		PRESSURE_LEG_ON, PRESSURE_LEG_OFF,
		// FOOT
		PRESSURE_FOOT_ON, PRESSURE_FOOT_OFF,
		// MANUAL
		PRESSURE_MANUAL_OFF,
		// SPEED
		PRESSURE_SPEED_1, PRESSURE_SPEED_2, PRESSURE_SPEED_3,PRESSURE_SPEED_4,PRESSURE_SPEED_5,
		/**
		 * BRACE
		 */
		// LEG
		BRACE_LEG_STRAIGHTEN, BRACE_LEG_BEND, BRACE_LEG_STRAIGHTEN_BEND_STOP,
		// BACK
		BRACE_BACK_DOWN, BRACE_BACK_UP, BRACE_BACK_STOP,
		// LEG
		BRACE_LEG_STRETCH, BRACE_LEG_SHRINK, BRACE_LEG_STRETCH_SHRINK_STOP,
		// STRETCH ON/OFF
		BRACE_STRETCH_ON, BRACE_STRETCH_OFF,
		// ZERO GRAVITY/SLEEP
		BRACE_ZERO_GRAVITY, BRACE_SLEEP, BRACE_ZERO_GRAVITY_SLEEP_STOP,

		/**
		 * VIBRATION MOTOR
		 */
		VIBRATION_OFF, VIBRATION_SPEED_1_POWERON, VIBRATION_SPEED_2, VIBRATION_SPEED_3, VIBRATION_SPEED_4, VIBRATION_SPEED_5,
		/**
		 * TWIST FOOT MOTOR
		 */
		TWIST_OFF, TWIST_SPEED_1_POWERON, TWIST_SPEED_2, TWIST_SPEED_3, TWIST_SPEED_4, TWIST_SPEED_5,
		/**
		 * READ STATE
		 */
		READSTATE,

		/**
		 * MUSIC
		 */
		MUSIC_POWER_ON, MUSIC_POWER_OFF, MUSIC_PREVIOUS, MUSIC_NEXT, MUSIC_VOLUME_UP, MUSIC_VOLUME_DOWN, MUSIC_PLAY, MUSIC_PAUSE,
		/**
		 * 
		 */
		PEDICURE_FAST, PEDICURE_SOFT, PEDICURE_OFF,
		/**
		 * heat 
		 */
		THEMALMETHOD_ON, THEMALMETHOD_OFF,THERMALFEET_OFF,THERMALFEET_ON,THERMALWAIST_ON,THERMALWAIST_OFF,
		/**
		 * shoulder height
		 */
		SHOULDER_UP,SHOULDER_DOWN,SHOULDER_STOP
	}

	public static byte[] getSendFrame(OPERATION operation) {

		byte Command = getCommandData(operation);

		return mSendData = new byte[] { mAddress, Command, (byte) ~Command };
	}

	public static byte getCommandData(OPERATION operation) {
		byte mCommand = mReadState;
		switch (operation) {
		case POWER_ON:
			mCommand = mPower_ON;
			break;
		case POWER_OFF:
			mCommand = mPower_OFF;
			break;
			
		case LED_ON:
			mCommand = mLed_ON;
			break;
		case LED_OFF:
			mCommand = mLed_OFF;
			break;
			
		case TEMP_RAISE:
			mCommand=mTemp_Raise;
			break;
			
		case TEMP_REDUCE:
			mCommand=mTemp_Reduce;
			break;

		case STOP:
			mCommand = mStop;
			break;
		case BURNNING_ON:
			mCommand=mBurnning;
			break;
		case STOPTIMER:
			mCommand = mStopTimer;
			break;
		case TIMER_10:
			mCommand = mTimer_10;
			break;
		case TIMER_20:
			mCommand = mTimer_20;
			break;
		case TIMER_30:
			mCommand = mTimer_30;
			break;
		case TIMER_45:
			mCommand=mTimer_45;
			break;
		case FATIGUERECOVERY_ON:
			mCommand = mFatigueRecovery_ON;
			break;
		case FATIGUERECOVERY_OFF:
			mCommand = mFatigueRecovery_OFF;
			break;

		case PAINEASE_ON:
			mCommand = mPainEase_ON;
			break;
		case PAINEASE_OFF:
			mCommand = mPainEase_OFF;
			break;

		case MOODRELAX_ON:
			mCommand = mMoodRelax_ON;
			break;
		case MOODRELAX_OFF:
			mCommand = mMoodRelax_OFF;
			break;
		case XiaoYaoFangSong:
			mCommand=mXiaoYaoFangSong;
			break;
		case XiuShenBaoJian:
			 mCommand=mXiuShenBaoJian;
			 break;
		case RHYTHM_ON:
			mCommand = mRhythm_ON;
			break;
		// 3D
		case _3DEXPERIENCE_ON:
			mCommand = m3DExperience_ON;
			break;
		//
		case OFFICEWORKER_ON:
			mCommand = mOfficeWorker_ON;
			break;
		//
		case SHOPPER_ON:
			mCommand = mShopper_ON;
			break;
		//
		case YOGIOPENBACK_ON:
			mCommand = mYogiOpenBack_ON;
			break;
		//
		case ATHLETEMODE_ON:
			mCommand = mAthleteMode_ON;
			break;
		//
		case HEALTH_ON:
			mCommand=mHealth_ON;
			break;

		// WHOLE
		case BACK_WHOLE_ON:
			mCommand = mBack_Whole_ON;
			break;
		case BACK_WHOLE_OFF:
			mCommand = mBack_Whole_OFF;
			break;
		// SHOULDER
		case BACK_SHOULDER_ON:
			mCommand = mBack_Shoulder_ON;
			break;
		case BACK_SHOULDER_OFF:
			mCommand = mBack_Shoulder_OFF;
			break;
		// BACK
		case BACK_BACK_ON:
			mCommand = mBack_Back_ON;
			break;
		case BACK_BACK_OFF:
			mCommand = mBack_back_OFF;
			break;
		// WAIST
		case BACK_WAIST_ON:
			mCommand = mBack_Waist_ON;
			break;
		case BACK_WAIST_OFF:
			mCommand = mBack_Waist_OFF;
			break;
		// CUSTOM
		case BACK_CUSTOM_ON:
			mCommand = mBack_Custom_ON;
			break;
		case BACK_CUSTOM_OFF:
			mCommand = mBack_Custom_OFF;
			break;
		// KNEADING
		case BACK_KNEADING_ON:
			mCommand = mBack_Kneading_ON;
			break;
		case BACK_KNEADING_OFF:
			mCommand = mBack_Kneading_OFF;
			break;
		// TAPPING
		case BACK_TAPPING_ON:
			mCommand = mBack_Tapping_ON;
			break;
		case BACK_TAPPING_OFF:
			mCommand = mBack_Tapping_OFF;
			break;
		// CLAPPING
		case BACK_CLAPPING_ON:
			mCommand = mBack_Clapping_ON;
			break;
		case BACK_CLAPPING_OFF:
			mCommand = mBack_Clapping_OFF;
			break;
		// FINGER
		case BACK_FINGER_ON:
			mCommand = mBack_Finger_ON;
			break;
		case BACK_FINGER_OFF:
			mCommand = mBack_Finger_OFF;
			break;
		// KNEADING+TAPPING
		case BACK_KNEADING_TAPPING_ON:
			mCommand = mBack_Kneading_Tapping_ON;
			break;
		case BACK_KNEADING_TAPPING_OFF:
			mCommand = mBack_Kneading_Tapping_OFF;
			break;
		case BACK_RUBBING:
			mCommand=mBack_Rubbing_ON;
				break;
		case PULSE_SHIATSU:
			mCommand=mPulse_Shiatsu_ON;
			break;
		case FINGER_KNEAD:
			mCommand=mFinger_Knead_ON;
			break;
		// SHOULDER HEIGHT
		case BACK_SHOULDERHEIGHT_1:
			mCommand = mBack_ShoulderHeight_1;
			break;
		case BACK_SHOULDERHEIGHT_2:
			mCommand = mBack_ShoulderHeight_2;
			break;
		case BACK_SHOULDERHEIGHT_3:
			mCommand = mBack_ShoulderHeight_3;
			break;
		case BACK_SHOULDERHEIGHT_4:
			mCommand = mBack_ShoulderHeight_4;
			break;
		case BACK_SHOULDERHEIGHT_5:
			mCommand = mBack_ShoulderHeight_5;
			break;
		// PART OF BACK
		case BACK_ALL:
			mCommand = mBack_All;
			break;
		case BACK_SHOULDER:
			mCommand = mBack_Shoulder;
			break;
		case BACK_BACK:
			mCommand = mBack_Back;
			break;
		case BACK_WAIST:
			mCommand = mBack_Waist;
			break;
		case BACK_FIXED:
			mCommand = mBack_Fixed;
			break;
		// ADJUST
		case BACK_ADJUST_UP:
			mCommand = mBack_Adjust_UP;
			break;
		case BACK_ADJUST_DOWN:
			mCommand = mBack_Adjust_DOWN;
			break;
		case BACK_ADJUST_STOP:
			mCommand = mBack_Adjust_STOP;
			break;
		// 3D
		case BACK_3D_FORWARD:
			mCommand = mBack_3D_FORWARD;
			break;
		case BACK_3D_BACKWARD:
			mCommand = mBack_3D_BACKWARD;
			break;
		case BACK_3D_STOP:
			mCommand = mBack_3D_STOP;
			break;
		// WIDE CENTER NARROW
		case BACK_WIDE:
			mCommand = mBack_WIDE;
			break;
		case BACK_CENTER:
			mCommand = mBack_CENTER;
			break;
		case BACK_NARROW:
			mCommand = mBack_NARROW;
			break;
		// SPEED
		case BACK_SPEED_1:
			mCommand = mBack_SPEED_1;
			break;
		case BACK_SPEED_2:
			mCommand = mBack_SPEED_2;
			break;
		case BACK_SPEED_3:
			mCommand = mBack_SPEED_3;
			break;
		case BACK_SPEED_4:
			mCommand = mBack_SPEED_4;
			break;
		case BACK_SPEED_5:
			mCommand = mBack_SPEED_5;
			break;
		/*
		 * PRESSURE MASSAGE
		 */
		// AUTO
		case PRESSURE_AUTO_ON:
			mCommand = mPressure_Auto_ON;
			break;
		case PRESSURE_AUTO_OFF:
			mCommand = mPressure_Auto_OFF;
			break;
		// STRETCH
		case PRESSURE_STRETCH_ON:
			mCommand = mPressure_Stretch_ON;
			break;
		case PRESSURE_STRETCH_OFF:
			mCommand = mPressure_Stretch_OFF;
			break;
		// HAND
		case PRESSURE_HAND_ON:
			mCommand = mPressure_Hand_ON;
			break;
		case PRESSURE_HAND_OFF:
			mCommand = mPressure_Hand_OFF;
			break;
		// WAIST
		case PRESSURE_WAIST_ON:
			mCommand = mPressure_Waist_ON;
			break;
		case PRESSURE_WAIST_OFF:
			mCommand = mPressure_Waist_OFF;
			break;
		// SEAT
		case PRESSURE_SEAT_ON:
			mCommand = mPressure_Seat_ON;
			break;
		case PRESSURE_SEAT_OFF:
			mCommand = mPressure_Seat_OFF;
			break;
		// LEG
		case PRESSURE_LEG_ON:
			mCommand = mPressure_Leg_ON;
			break;
		case PRESSURE_LEG_OFF:
			mCommand = mPressure_Leg_OFF;
			break;
		// FOOT
		case PRESSURE_FOOT_ON:
			mCommand = mPressure_foot_ON;
			break;
		case PRESSURE_FOOT_OFF:
			mCommand = mPressure_foot_OFF;
			break;
		// MANUAL
		case PRESSURE_MANUAL_OFF:
			mCommand = mPressure_Manual_OFF;
			break;
		// SPEED
		case PRESSURE_SPEED_1:
			mCommand = mPressure_SPEED_1;
			break;
		case PRESSURE_SPEED_2:
			mCommand = mPressure_SPEED_2;
			break;
		case PRESSURE_SPEED_3:
			mCommand = mPressure_SPEED_3;
			break;
		case PRESSURE_SPEED_4:
			mCommand=mPressure_SPEED_4;
			break;
		case PRESSURE_SPEED_5:
			mCommand=mPressure_SPEED_5;
			break;
		/*
		 * BRACE
		 */
		// LEG
		case BRACE_LEG_STRAIGHTEN:
			mCommand = mBrace_Leg_Straighten;
			break;
		case BRACE_LEG_BEND:
			mCommand = mBrace_Leg_Bend;
			break;
		case BRACE_LEG_STRAIGHTEN_BEND_STOP:
			mCommand = mBrace_Leg_Straighten_Bend_Stop;
			break;
		// BACK
		case BRACE_BACK_DOWN:
			mCommand = mBrace_Back_Down;
			break;
		case BRACE_BACK_UP:
			mCommand = mBrace_Back_Up;
			break;
		case BRACE_BACK_STOP:
			mCommand = mBrace_Back_Stop;
			break;
		// LEG
		case BRACE_LEG_STRETCH:
			mCommand = mBrace_Leg_Stretch;
			break;
		case BRACE_LEG_SHRINK:
			mCommand = mBrace_Leg_Shrink;
			break;
		case BRACE_LEG_STRETCH_SHRINK_STOP:
			mCommand = mBrace_Leg_Stretch_Shrink_Stop;
			break;
		case BRACE_ZERO_GRAVITY:
			mCommand = mBrace_Zero_Gravity;
			break;
		case BRACE_SLEEP:
			mCommand = mBrace_Sleep;
			break;
		case BRACE_ZERO_GRAVITY_SLEEP_STOP:
			mCommand = mBrace_Zero_Gravity_Sleep_Stop;
			break;

		// STRETCH ON/OFF
		case BRACE_STRETCH_ON:
			mCommand = mBrace_Stretch_ON;
			break;
		case BRACE_STRETCH_OFF:
			mCommand = mBrace_Stretch_OFF;
			break;
		/*
		 * VIBRATION MOTOR
		 */
		case VIBRATION_OFF:
			mCommand = mVibration_OFF;
			break;
		case VIBRATION_SPEED_1_POWERON:
			mCommand = mVibration_SPEED_1_PowerOn;
			break;
		case VIBRATION_SPEED_2:
			mCommand = mVibration_SPEED_2;
			break;
		case VIBRATION_SPEED_3:
			mCommand = mVibration_SPEED_3;
			break;
		case VIBRATION_SPEED_4:
			mCommand = mVibration_SPEED_4;
			break;
		case VIBRATION_SPEED_5:
			mCommand = mVibration_SPEED_5;
			break;
		/*
		 * TWIST FOOT MOTOR
		 */
		case TWIST_OFF:
			mCommand = mTwist_OFF;
			break;
		case TWIST_SPEED_1_POWERON:
			mCommand = mTwist_SPEED_1_PowerOn;
			break;
		case TWIST_SPEED_2:
			mCommand = mTwist_SPEED_2;
			break;
		case TWIST_SPEED_3:
			mCommand = mTwist_SPEED_3;
			break;
		case TWIST_SPEED_4:
			mCommand = mTwist_SPEED_4;
			break;
		case TWIST_SPEED_5:
			mCommand = mTwist_SPEED_5;
			break;
		/**
		 * 
		 */
		case PEDICURE_FAST:
			mCommand = mPedicure_Fast;
			break;
		case PEDICURE_SOFT:
			mCommand = mPedicure_Soft;
			break;
		case PEDICURE_OFF:
			mCommand = mPedicure_OFF;
			break;
		case THERMALFEET_ON:
			mCommand = mThermalFeet_ON;
			break;
		case THERMALFEET_OFF:
			mCommand = mThermalFeet_OFF;
			break;
		case THERMALWAIST_ON:
			mCommand=mThermalWaist_ON;
			break;
		case THERMALWAIST_OFF:
			mCommand=mThermalWaist_OFF;
			break;
		/**
		 * 
		 */
		case THEMALMETHOD_ON:
			mCommand = mThemalMethod_ON;
			break;
		case THEMALMETHOD_OFF:
			mCommand = mThemalMethod_OFF;
			break;
		

		/*
		 * 
		 */
		case MUSIC_POWER_ON:
			mCommand = mMusic_Power_ON;
			break;
		case MUSIC_POWER_OFF:
			mCommand = mMusic_Power_OFF;
			break;
		case MUSIC_PREVIOUS:
			mCommand = mMusic_Previous;
			break;
		case MUSIC_NEXT:
			mCommand = mMusic_Next;
			break;
		case MUSIC_VOLUME_UP:
			mCommand = mMusic_Volume_UP;
			break;
		case MUSIC_VOLUME_DOWN:
			mCommand = mMusic_Volume_DOWN;
			break;
		case MUSIC_PLAY:
			mCommand = mMusic_Play;
			break;
		case MUSIC_PAUSE:
			mCommand = mMusic_Pause;
			break;
			/**
			 * shoulder height
			 */
		case SHOULDER_UP:
			mCommand=mShoulderUp;
			break;
		case SHOULDER_DOWN:
			mCommand=mShoulderDown;
			break;
		case SHOULDER_STOP:
			mCommand=mShoulderStop;
			break;
			
		/*
		 * READ STATE
		 */
		/*
		 * case READSTATE: mCommand = mReadState; break;
		 */
		// Default is read state
		default:
			// TODO:Throw a exception
			break;
		}
		return mCommand;
	}

	public byte[] setSendData(byte[] mSendData) {
		DataFrame.mSendData = mSendData;//this.msenddata---->DataFrame.mSenddata
		Log.d(TAG, "msenddata------------->"+mSendData);
		return mSendData;
	}

}
