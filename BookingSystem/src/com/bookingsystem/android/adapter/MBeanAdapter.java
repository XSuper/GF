package com.bookingsystem.android.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.duohuo.dhroid.adapter.BeanAdapter;
import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.FieldMapImpl;
import net.duohuo.dhroid.adapter.NetJSONAdapter.DataBulider;
import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MBeanAdapter extends BeanAdapter<JSONObject> {

	public List<FieldMap> fields;
	DataBulider dataBulider;

	/**
	 * 添加Field
	 * 
	 * @param key
	 * @param refid
	 * @return
	 */
	public MBeanAdapter addField(String key, Integer refid) {
		FieldMap bigMap = new FieldMapImpl(key, refid);
		fields.add(bigMap);
		return this;
	}

	/**
	 * 添加Field
	 * 
	 * @param key
	 * @param refid
	 * @param type
	 * @return
	 */
	public MBeanAdapter addField(String key, Integer refid, String type) {
		FieldMap bigMap = new FieldMapImpl(key, refid, type);
		fields.add(bigMap);
		return this;
	}

	/**
	 * 添加Field
	 * 
	 * @param fieldMap
	 * @return
	 */
	public MBeanAdapter addField(FieldMap fieldMap) {
		fields.add(fieldMap);
		return this;
	}

	@Override
	public String getTItemId(int position) {
		JSONObject jo = getTItem(position);
		String key = getJumpKey();
		if (TextUtils.isEmpty(key)) {
			key = "id";
		}
		String id = JSONUtil.getString(jo, key);
		if (TextUtils.isEmpty(id)) {
			id = position + "";
		}
		return id;
	}

	@Override
	public long getItemId(int position) {
		JSONObject jo = getTItem(position);
		if (jo != null && jo.has("id")) {
			try {
				return jo.getInt("id");
			} catch (Exception e) {
				return position;
			}
		}
		return position;
	}

	/**
	 * 数据绑定
	 */
	@Override
	public void bindView(View itemV, int position, JSONObject item) {
		// 使用大家的viewholder模式
		ViewHolder viewHolder = ViewHolder.getHolder(itemV);
		JSONObject jo = (JSONObject) item;
		for (Iterator<FieldMap> iterator = fields.iterator(); iterator
				.hasNext();) {
			FieldMap fieldMap = iterator.next();
			View v = viewHolder.getView(fieldMap.getRefId());
			String value = JSONUtil.getString(jo, fieldMap.getKey());
			if (fieldMap instanceof FieldMapImpl && fixer != null) {
				Object gloValue = fixer.fix(value, fieldMap.getType());
				bindValue(position, v, gloValue,
						fixer.imageOptions(fieldMap.getType()));
			} else {
				Object ovalue = fieldMap.fix(itemV, position, value, jo);
				DisplayImageOptions options = null;
				if (fixer != null) {
					options = fixer.imageOptions(fieldMap.getType());
				}
				bindValue(position, v, ovalue, options);
			}
		}
	}

	public MBeanAdapter(Context context, int mResource) {
		super(context, mResource);
		// TODO Auto-generated constructor stub
		fields = new ArrayList<FieldMap>();
	}

}
