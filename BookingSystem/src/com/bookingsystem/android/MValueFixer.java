package com.bookingsystem.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.duohuo.dhroid.adapter.ValueFix;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.bookingsystem.android.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MValueFixer implements ValueFix {

	// 头像用圆角
	public static DisplayImageOptions optionsHeadRound;
	public static Map<String, DisplayImageOptions> imageOptions;

	public MValueFixer() {
		imageOptions = new HashMap<String, DisplayImageOptions>();
		DisplayImageOptions optionsDefault = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.imgdefault)
				.showImageForEmptyUri(R.drawable.imgdefault)
				.showImageOnFail(R.drawable.imgdefault).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		imageOptions.put("default", optionsDefault);
	}

	@Override
	public Object fix(Object o, String type) {
		if (o == null)
			return null;
		if ("time".equals(type)) {
			return getStandardTime(Long.parseLong(o.toString()) * 1000,
					"yyyy-MM-dd");
		} else if ("groundType".equals(type)) {
			if (o.toString().trim().equals("1")) {
				return "俱乐部";
			} else {
				return "练习场";
			}
		} else if ("price".equals(type)) {
			return " ¥" + StringUtils.toDouble(o);
		} else if ("priceRMB".equals(type)) {
			return " ¥" + StringUtils.toDouble(o) + "RMB";
		} else if ("address".equals(type)) {
			return " 地址：" + o;
		} else if ("addresshide".equals(type)) {
			if (o.toString().length() < 9) {
				return " 地址：" + o.toString();
			}
			return " 地址：" + o.toString().substring(0, 6) + "XXXX"
					+ o.toString().substring(o.toString().length() - 2);
		} else if ("age".equals(type)) {
			return o + "岁";
		} else if ("flag".equals(type)) {
			return StringUtils.paresPLFlag(o.toString());
		} else if ("isshuttle".equals(type)) {
			if (StringUtils.toInt(o) == 1) {
				return " 需要预订者接送他（她）到球场: 是";
			} else {
				return " 需要预订者接送他（她）到球场: 否";

			}
		} else if ("mobile".equals(type)) {
			return " 电话: " + o;
		} else if ("mobilepass".equals(type)) {
			String str = o.toString();
			if (str.length() < 9) {

				return " 电话: " + str;
			}
			String f = str.substring(0, 6);
			String e = str.substring(9);
			str = f + "***" + e;
			return " 电话: " + str;
		} else if ("cost".equals(type)) {
			String str = o.toString();
			str = str.replace("1", "果岭费");
			str = str.replace("2", "球童");
			str = str.replace("3", "球车");
			str = str.replace("4", "衣柜");
			str = str.replace(",", "/");
			return str;
		} else if ("telpass".equals(type)) {
			String str = o.toString();
			if (str.length() < 9) {

				return str;
			}
			String f = str.substring(0, 6);
			String e = str.substring(9);
			str = f + "***" + e;
			return str;
		} else if ("orderstatus-txt".equals(type)) {
			if ("0".equals(o.toString())) {
				return "未支付";
			} else if ("1".equals(o.toString())) {
				return "已支付";
			} else if ("2".equals(o.toString())) {
				return "已完成";
			}

		} else if ("orderstatus-btn".equals(type)) {
			if ("0".equals(o.toString().trim())) {
				return "立即付款";
			} else if ("1".equals(o.toString().trim())) {
				return "申请退款";
			} else if ("2".equals(o.toString().trim())) {
				return "已完成";
			} else if ("3".equals(o.toString().trim())) {
				return "退款中";
			} else if ("4".equals(o.toString().trim())) {
				return "退款成功";
			} else if ("5".equals(o.toString().trim())) {
				return "已审核";
			}

		} else if ("pricetype".equals(type)) {
			if ("1".equals(o.toString().trim())) {
				return "元/盒";
			} else if ("2".equals(o.toString().trim())) {
				return "元/小时";
			}

		}
		return o;
	}

	/**
	 * 时间转字符串
	 * 
	 * @param timestamp
	 * @param pattern
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStandardTime(long timestamp, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = new Date(timestamp);
		sdf.format(date);
		return sdf.format(date);
	}

	@Override
	public DisplayImageOptions imageOptions(String type) {
		DisplayImageOptions option = imageOptions.get(type);
		if (option == null) {
			option = imageOptions.get("default");
		}
		return option;
	}

}
