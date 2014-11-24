package com.bookingsystem.android.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.FieldMapImpl;
import net.duohuo.dhroid.adapter.ValueFix;
import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.util.AbStrUtil;
import com.bookingsystem.android.bean.Ground;
import com.bookingsystem.android.ui.GroundDetail;
import com.bookingsystem.android.ui.PLDetail;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JsonObjectToView {
	public List<FieldMap> fields;
	public ValueFix fixer;
	public Context context;
	public Class jumpClazz;
	public String jumpKey;
	public String jumpAs;
	
	DhDB db;
	public Class getJumpClazz() {
		return jumpClazz;
	}
	
	public String getJumpKey() {
		return jumpKey;
	}

	public String getJumpAs() {
		return jumpAs;
	}



	public void setJump(Class jumpClazz,String jumpkey,String as) {
		this.jumpClazz = jumpClazz;
		this.jumpKey=jumpkey;
		this.jumpAs=as;
	}

	public JsonObjectToView(Context context){
		fields = new ArrayList<FieldMap>();
		fixer = IocContainer.getShare().get(ValueFix.class);
		db = IocContainer.getShare().get(DhDB.class);
		this.context = context;
		
		
	}
	
	/**
	 * 添加Field
	 * @param key
	 * @param refid
	 * @return
	 */
	public JsonObjectToView addField(String key, Integer refid) {
		FieldMap bigMap = new FieldMapImpl(key, refid);
		fields.add(bigMap);
		return this;
	}

	/**
	 * 添加Field
	 * @param key
	 * @param refid
	 * @param type
	 * @return
	 */
	public JsonObjectToView addField(String key, Integer refid, String type) {
		FieldMap bigMap = new FieldMapImpl(key, refid, type);
		fields.add(bigMap);
		return this;
	}

	/**
	 * 添加Field
	 * @param fieldMap
	 * @return
	 */
	public JsonObjectToView addField(FieldMap fieldMap) {
		fields.add(fieldMap);
		return this;
	}

	/**
	 * List<JSONObject> ==》ArrayList<View>  并绑定值
	 */
	public ArrayList<View> parseAndBind(int resource, List<JSONObject> lists) {
		ArrayList<View> views = new ArrayList<View>();
		LayoutInflater inflater =  LayoutInflater.from(context);
		for(int position=0;position<lists.size();position++){
			final JSONObject jo = lists.get(position);
			View view = inflater.inflate(resource, null);
			for (Iterator<FieldMap> iterator = fields.iterator(); iterator
					.hasNext();) {
				FieldMap fieldMap = iterator.next();
				View v = view.findViewById(fieldMap.getRefId());
				String value = JSONUtil.getString(jo, fieldMap.getKey());
				if (fieldMap instanceof FieldMapImpl && fixer != null) {
					Object gloValue = fixer.fix(value, fieldMap.getType());
					bindValue(position, v, gloValue);
				} else {
					Object ovalue = fieldMap.fix(view, position, value, jo);
					bindValue(position, v, ovalue);
				}
				
			}
			views.add(view);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					Intent it = new Intent(context, getJumpClazz());
					it.putExtra(getJumpAs(),
							JSONUtil.getString(jo, getJumpKey()));
					
					if(getJumpClazz()==PLDetail.class){
						//添加性别
						it.putExtra("sex", JSONUtil
								.getInt(jo, "sex"));
					}else if(getJumpClazz()==GroundDetail.class){
						//添加性别
						it.putExtra("type", JSONUtil
								.getInt(jo, "type"));
						Log.v("extra", "向detail 传入type为"+JSONUtil
								.getInt(jo, "type"));
					}
					context.startActivity(it);
					
					
					if(getJumpClazz()==GroundDetail.class){
						Ground g = db.load(Ground.class, JSONUtil.getString(jo, "id"));
						if(g!= null&&!AbStrUtil.isEmpty(g.getId())){
							
						}else{
							g = new Ground();
							g.setId(JSONUtil.getString(jo, "id"));
							g.setName(JSONUtil.getString(jo, "name"));
							g.setAddress(JSONUtil.getString(jo, "address"));
							g.setClubid(JSONUtil.getString(jo, "clubid"));
							g.setData(JSONUtil.getString(jo, "data"));
							g.setPic1(JSONUtil.getString(jo, "pic1"));
							g.setModel(JSONUtil.getString(jo, "model"));
							g.setPrice(JSONUtil.getString(jo, "price"));
							db.save(g);
						}
					}
					
				}
			});
			
		}
		return views;
	}
	/**
	 * 将值和控件绑定 可以防止图片的移位
	 * 
	 * @param position
	 * @param v
	 * @param o
	 */
	public void bindValue(final Integer position, View v, Object o) {
		if (o == null)
			o = "";
		if (v instanceof ImageView) {
			ImageView imagev = (ImageView) v;
			if (o instanceof Drawable) {
				imagev.setImageDrawable((Drawable) o);
			} else if (o instanceof Bitmap) {
				imagev.setImageBitmap((Bitmap) o);
			} else if (o instanceof Integer) {
				imagev.setImageResource((Integer) o);
			} else if (o instanceof String) {
				if(AbStrUtil.isEmpty((String)o)){
					((ImageView)v).setImageDrawable(null);
				}else{
					ImageLoader.getInstance().displayImage((String) o,
							(ImageView) v);
				}
			}
		} else if (v instanceof TextView) {
			if (o instanceof CharSequence) {
				((TextView) v).setText((CharSequence) o);
			} else {
				((TextView) v).setText(o.toString());
			}
		}
	}
	
	/**
	 * 
	 * 大家都用的viewholder
	 * 
	 */
	public class ViewHolder {
		Map<Integer, View> views;

		public ViewHolder() {
			super();
			views = new HashMap<Integer, View>();
		}

		public void put(Integer id, View v) {
			views.put(id, v);
		}

		public View get(Integer id) {
			return views.get(id);
		}

	}
}
