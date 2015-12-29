package com.kangtai.MassageChairUI.WLAN;

import java.util.ArrayList;
import java.util.Map;

import com.kangtai.MassageChairUI.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IpAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<Map<String, Object>> list;
    public IpAdapter(Context context,ArrayList<Map<String, Object>> ping){	
    	this.mContext = context;
    	this.list = ping;
    }
    
    public void setIpData(ArrayList<Map<String, Object>> list){
    	this.list = list;
    }

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return list.get(arg0).get("ip");
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    if(convertView == null){
	    	convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
	    }
	    TextView tv = (TextView) convertView.findViewById(R.id.ip_address);
	    tv.setText(list.get(position).get("ip").toString());
	    Log.i("Light", list.get(position).get("ip").toString());
		return convertView;
	}

}
