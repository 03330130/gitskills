package com.kangtai.MassageChairUI.Protocal;

public class StateFrame {
	public final static byte mAddress = (byte) 0xF1;
	/*
	 * public byte mState_0; public byte mState_1; public byte mState_2; public
	 * byte mState_3; public byte mState_4; public byte mState_5; public byte
	 * mState_6; public byte mState_7; public byte mState_8; public byte
	 * mState_9; public byte mState_10; public byte mState_11;
	 */
	public byte mCheckSum = 0;
	public byte[] mState = new byte[13];

	public StateFrame(byte[] state) {
		this.mState = state;
	}

	public Boolean checkSum() {
		for (int i = 1; i < mState.length; i++)
			mCheckSum = (byte) (mCheckSum ^ mState[i]);
		return (mCheckSum == mState[12]);
	}

	public void checkState() {
		for (int i = 1; i < mState.length; i++) {
			switch (i) {
			case 1:// 正在进行的气压按摩点

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			}

		}
	}

	public enum State {
		ST_1, ST_2, ST_3, ST_4, ST_5, ST_6, ST_7, ST_8, ST_9, ST_10, ST_11
	}

	public void checkState(State state) {
		switch (state) {
		case ST_1:

			break;
		case ST_2:

			break;
		case ST_3:

			break;
		case ST_4:

			break;
		case ST_5:

			break;
		case ST_6:

			break;
		case ST_7:

			break;
		case ST_8:

			break;
		case ST_9:

			break;
		case ST_10:

			break;
		case ST_11:

			break;
		}
	}

	/**
	 * Byte转Bit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}
}
