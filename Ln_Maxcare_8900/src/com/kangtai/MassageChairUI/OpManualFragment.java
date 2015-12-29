package com.kangtai.MassageChairUI;

import java.util.ArrayList;
import java.util.HashMap;

import com.kangtai.MassageChairUI.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OpManualFragment extends Fragment {

	private int[] mManualListString = { R.string.mode, R.string.part,
			R.string.width, R.string.speed, R.string.threeD};
	private int[] mManualListImage = { R.drawable.op_manual_mode_background,
			R.drawable.op_manual_part_background, R.drawable.op_manual_width_background,
			R.drawable.op_manual_speed_background,
			R.drawable.op_manual_3d_background };
	private ListView mListViewManual;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.op_manual_view, null);
		mListViewManual = (ListView) getActivity().findViewById(R.id.lv_manual);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/* �������д������ */
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mManualListString[i]);// ����ͼƬ map.put("ItemTitle",
													// "��"+i+"��");
			map.put("ItemText",getResources().getString( mManualListImage[i]));
			map.put("ItemArrow", R.drawable.op_manual_arrow);
			listItem.add(map);
		}

		SimpleAdapter mSimpleAdapter = new SimpleAdapter(this.getActivity(), listItem,// ��Ҫ�󶨵�����
				R.layout.op_manual_list_item,// ÿһ�еĲ���//��̬�����е�����Դ�ļ���Ӧ�����岼�ֵ�View��
				new String[] { "ItemImage", "ItemText","ItemArrow" }, new int[] {
						R.id.img_manual_icon, R.id.tv_manual_info,R.id.img_manual_arrow });

		mListViewManual.setAdapter(mSimpleAdapter);
		return view;
	}

}
