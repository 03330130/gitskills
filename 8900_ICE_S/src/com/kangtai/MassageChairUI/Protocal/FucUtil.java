package com.kangtai.MassageChairUI.Protocal;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

//import com.iflytek.cloud.ErrorCode;
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechUtility;

/**
 * �����Ժ�����չ��
 */
public class FucUtil {
	/**
	 * ��ȡassetĿ¼���ļ���
	 * @return content
	 */
	public static String readFile(Context mContext,String file,String code)
	{
		int len = 0;
		byte []buf = null;
		String result = "";
		try {
			InputStream in = mContext.getAssets().open(file);			
			len  = in.available();
			buf = new byte[len];
			in.read(buf, 0, len);
			
			result = new String(buf,code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ��ȡ����+�Ƿ����������д��Դ����δ������ת����Դ����ҳ��
	 *1.PLUS_LOCAL_ALL: ����������Դ 
      2.PLUS_LOCAL_ASR: ����ʶ����Դ
      3.PLUS_LOCAL_TTS: ���غϳ���Դ
	 */
//	public static String checkLocalResource(){
//		String resource = SpeechUtility.getUtility().getParameter(SpeechConstant.PLUS_LOCAL_ASR);
//		try {
//			JSONObject result = new JSONObject(resource);
//			int ret = result.getInt(SpeechUtility.TAG_RESOURCE_RET);
//			switch (ret) {
//			case ErrorCode.SUCCESS:
//				JSONArray asrArray = result.getJSONObject("result").optJSONArray("asr");
//				if (asrArray != null) {
//					int i = 0;
//					// ��ѯ�����������д��Դ
//					for (; i < asrArray.length(); i++) {
//						if("iat".equals(asrArray.getJSONObject(i).get(SpeechConstant.DOMAIN))){
//							//asrArray�а������ԡ������ֶΣ�����������֧�ַ��Եı�����д��
//							//�磺"accent": "mandarin","language": "zh_cn"
//							break;
//						}
//					}
//					if (i >= asrArray.length()) {
//						
//						SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_ASR);	
//						return "û����д��Դ����ת����Դ����ҳ��";
//					}
//				}else {
//					SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_ASR);
//					return "û����д��Դ����ת����Դ����ҳ��";
//				}
//				break;
//			case ErrorCode.ERROR_VERSION_LOWER:
//				return "����+�汾���ͣ�����º�ʹ�ñ��ع���";
//			case ErrorCode.ERROR_INVALID_RESULT:
//				SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_ASR);
//				return "��ȡ���������ת����Դ����ҳ��";
////			case ErrorCode.ERROR_SYSTEM_PREINSTALL:
//				//����+Ϊ����Ԥ�ð汾��
//			default:
//				break;
//			}
//		} catch (Exception e) {
//			SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_ASR);
//			return "��ȡ���������ת����Դ����ҳ��";
//		}
//		return "";
//	}
}
