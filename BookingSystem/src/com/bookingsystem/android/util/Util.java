package com.bookingsystem.android.util;

import com.bookingsystem.android.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Util {

	
	//解决 scrollview 和 listview 嵌套的问题
	public static void setListViewHeightBasedOnChildren(ListView listView) { 
        ListAdapter listAdapter = listView.getAdapter();  
        if (listAdapter == null) { 
            // pre-condition 
            return; 
        } 
 
        int totalHeight = 0; 
        for (int i = 0; i < listAdapter.getCount(); i++) { 
            View listItem = listAdapter.getView(i, null, listView); 
            listItem.measure(0, 0); 
            totalHeight += listItem.getMeasuredHeight(); 
        } 
 
        ViewGroup.LayoutParams params = listView.getLayoutParams(); 
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
        listView.setLayoutParams(params); 
    } 
	
	
	public static boolean setWeatherImg(ImageView img,String weather){
		if(weather.contains("雷")){
			img.setImageResource(R.drawable.lei);
			return true;
		}else if(weather.contains("雪")){
			img.setImageResource(R.drawable.xue);
			return true;
		}else if(weather.contains("风")){
			img.setImageResource(R.drawable.feng);
			return true;
		}else if(weather.contains("雨")){
			img.setImageResource(R.drawable.yu);
			return true;
		}else if(weather.contains("多云")){
			img.setImageResource(R.drawable.duoyun);
			return true;
		}else if(weather.contains("晴")){
			img.setImageResource(R.drawable.qin);
			return true;
		}
		return false;
	}
}
