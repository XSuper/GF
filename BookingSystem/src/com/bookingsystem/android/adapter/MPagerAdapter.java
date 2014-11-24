package com.bookingsystem.android.adapter;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MPagerAdapter extends PagerAdapter {
	private ArrayList<View> mList;

	
	/**
	 * 刷新列表
	 * @param mdata
	 */
	public void refresh(ArrayList<View> views) {
		this.mList = views;
		notifyDataSetChanged();
	}
	
	
	public MPagerAdapter(ArrayList<View> views) {
		mList = views;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		View view = mList.get(position);
		((ViewPager) container).addView(view, 0);
		return view;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public Parcelable saveState() {
		return super.saveState();
	}

	@Override
	public void startUpdate(View container) {
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		super.restoreState(state, loader);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mList.get(position));
	}

}
