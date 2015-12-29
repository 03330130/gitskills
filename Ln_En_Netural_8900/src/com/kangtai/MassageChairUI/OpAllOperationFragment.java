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

public class OpAllOperationFragment extends Fragment {

	private ListView mListViewAuto;
	private ListView mListViewManual;
	private int[] mManualListString = { R.string.mode, R.string.part,
			R.string.width, R.string.speed, R.string.threeD};
	private int[] mManualListImage = { R.drawable.op_manual_mode_background,
			R.drawable.op_manual_part_background, R.drawable.op_manual_width_background,
			R.drawable.op_manual_speed_background,
			R.drawable.op_manual_3d_background };
	
	private int[] mAutoListString = { R.string.zyys, R.string.sthh,
			R.string.jgsz, R.string.qnty, R.string.xyfs, R.string.xsbj,
			R.string.ylam, R.string.blsbz, R.string.gwdr, R.string.yjkb,
			R.string.ydyms, R.string.ysbj };
	private int[] mAutoListImage = { R.drawable.op_auto_zyys_background,
			R.drawable.op_auto_sthh_normal, R.drawable.op_auto_jgsz_background,
			R.drawable.op_auto_ksty_background,
			R.drawable.op_auto_xyfs_background,
			R.drawable.op_auto_xsbj_background,
			R.drawable.op_auto_ylam_background,
			R.drawable.op_auto_blsbz_background,
			R.drawable.op_auto_gwdr_background,
			R.drawable.op_auto_yjkb_background,
			R.drawable.op_auto_ydyms_background,
			R.drawable.op_auto_ysbj_background };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.op_bottom_view, null);
		//Auto List
		mListViewAuto = (ListView) getActivity().findViewById(R.id.lv_auto);
		ArrayList<HashMap<String, Object>> autoListItem = new ArrayList<HashMap<String, Object>>();/* 在数组中存放数据 */
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mAutoListImage[i]);// 加入图片 map.put("ItemTitle",
													// "第"+i+"行");
			map.put("ItemText",getResources().getString( mAutoListString[i]));
			autoListItem.add(map);
		}

		SimpleAdapter mAutoSimpleAdapter = new SimpleAdapter(this.getActivity(), autoListItem,// 需要绑定的数据
				R.layout.op_auto_list_item,// 每一行的布局//动态数组中的数据源的键对应到定义布局的View中
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.img_auto_icon, R.id.tv_auto_info });

		mListViewAuto.setAdapter(mAutoSimpleAdapter);//
		// Manual List
		mListViewManual = (ListView) getActivity().findViewById(R.id.lv_manual);
		ArrayList<HashMap<String, Object>> manualListItem = new ArrayList<HashMap<String, Object>>();/* 在数组中存放数据 */
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mManualListString[i]);// 加入图片 map.put("ItemTitle",
													// "第"+i+"行");
			map.put("ItemText",getResources().getString( mManualListImage[i]));
			map.put("ItemArrow", R.drawable.op_manual_arrow);
			manualListItem.add(map);
		}

		SimpleAdapter mManualSimpleAdapter = new SimpleAdapter(this.getActivity(), manualListItem,// 需要绑定的数据
				R.layout.op_manual_list_item,// 每一行的布局//动态数组中的数据源的键对应到定义布局的View中
				new String[] { "ItemImage", "ItemText","ItemArrow" }, new int[] {
						R.id.img_manual_icon, R.id.tv_manual_info,R.id.img_manual_arrow });

		mListViewManual.setAdapter(mManualSimpleAdapter);
		return view;
	}

}
