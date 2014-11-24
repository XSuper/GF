package com.bookingsystem.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.ab.util.AbStrUtil;

/**
 * 字符串操作工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static Pattern phoner = Pattern.compile("^1\\d{10}$");
	// private final static Pattern phoner = Pattern.compile("\\d*");
	private final static Pattern passworder = Pattern.compile("[\\da-zA-Z]*");
	// private final static Pattern passworder = Pattern.compile("\\d*");
	// private final static SimpleDateFormat dateFormater = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// private final static SimpleDateFormat dateFormater2 = new
	// SimpleDateFormat("yyyy-MM-dd");

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {

			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 得到当前的时间 描述 : <描述函数实现的功能>. <br>
	 * <p>
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return dateFormater.get().format(new Date());
	}

	/**
	 * date转换成字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date) {
		return dateFormater2.get().format(date);
	}

	/**
	 * 将毫秒数字符串转换成 yyyy-MM-dd HH:mm:ss 格式的字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String strToDataStr(String time) {
		Date date = new Date(toLong(time, 0));
		return dateFormater.get().format(date);
	}

	/**
	 * 将毫秒数字符串转换成 yyyy-MM-dd 格式的字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String strToDataStr2(String time) {
		Date date = new Date(toLong(time, 0));
		return dateFormater2.get().format(date);
	}

	public static String longToDataStr(Long time) {
		Date date = new Date(time);
		return dateFormater.get().format(date);
	}

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date toDateYMD(String sdate) {
		try {
			return dateFormater2.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static boolean isOneDay(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		return curDate.equals(paramDate);

	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		return friendly_time(time);
	}

	public static String friendly_time(Date time) {
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断是否为手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (phone == null || phone.trim().length() == 0)
			return false;

		return phoner.matcher(phone).matches();
	}

	/**
	 * 判断是否为合法密码
	 * 
	 * @param passWord
	 * @return
	 */
	public static boolean isPassWord(String passWord) {
		if (passWord == null || passWord.trim().length() < 6
				|| passWord.trim().length() > 16)
			return false;

		return passworder.matcher(passWord).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;

	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static long toLong(String str, int defValue) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 对象转浮点
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static float toFloat(String obj) {
		try {
			return Float.parseFloat(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 对象转浮点
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static double toDouble(Object obj) {
		try {
			if(obj instanceof Integer||obj instanceof Float||obj instanceof Double){
				return (Double)obj;
			}else{
				return Double.parseDouble(obj.toString());
				
			}
		} catch (Exception e) {
		}
		return 0;
	}
	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 将百度地图得到的地名去掉省市
	 * 
	 * @param str
	 * @return
	 */
	public static String placeToString(String str) {
		if (str == null) {
			return "";
		}
		if (str.indexOf("省") == str.length() - 1) {
			str = str.substring(0, str.indexOf("省"));
		}
		if (str.indexOf("市") == str.length() - 1) {
			str = str.substring(0, str.indexOf("市"));
		}
		if (str.indexOf("区") == str.length() - 1) {
			str = str.substring(0, str.indexOf("区"));
		}

		return str;
	}

	/**
	 * 判断响应是否为异常
	 * 
	 * @param str
	 * @return
	 */
	public static boolean responseError(String str) {
		if (isEmpty(str.trim()) || "[]".equals(str.trim())
				|| "{}".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static String paresPLFlag(String str) {
		if (AbStrUtil.isEmpty(str)) {
			return "";
		} else {
			String matching = str;
			if (matching.contains("longlegged")) {
				matching = matching.replace("longlegged", "长腿");
			}
			if (matching.contains("sexy")) {
				matching = matching.replace("sexy", "性感");
			}
			if (matching.contains("lolita")) {
				matching = matching.replace("lolita", "萝莉");
			}
			if (matching.contains("longhair")) {
				matching = matching.replace("longhair", "长发");
			}
			if (matching.contains("lovely")) {
				matching = matching.replace("lovely", "可爱");
			}
			if (matching.contains("face")) {
				matching = matching.replace("face", "素颜");
			}
			if (matching.contains("spirited")) {
				matching = matching.replace("spirited", "英俊");
			}
			if (matching.contains("handsome")) {
				matching = matching.replace("handsome", "帅气");
			}
			if (matching.contains("humor")) {
				matching = matching.replace("humor", "幽默");
			}
			if (matching.contains("connotation")) {
				matching = matching.replace("connotation", "内涵");
			}
			if (matching.contains("romantic")) {
				matching = matching.replace("romantic", "风流");
			}
			if (matching.contains("student")) {
				matching = matching.replace("student", "学生");
			}
			if (matching.contains("primary")) {
				matching = matching.replace("primary", "初级教练");
			}
			if (matching.contains("intermediate")) {
				matching = matching.replace("intermediate", "中级教练");
			}
			if (matching.contains("senior")) {
				matching = matching.replace("senior", "高级教练");
			}
			if (matching.contains("silver")) {
				matching = matching.replace("silver", "银牌教练");
			}
			if (matching.contains("gold")) {
				matching = matching.replace("gold", "金牌教练");
			}
			if (matching.contains("novice")) {
				matching = matching.replace("novice", "新手");
			}
			if (matching.contains("80")) {
				matching = matching.replace("80", "80杆内 ");
			}
			if (matching.contains("90")) {
				matching = matching.replace("90", "90杆内");
			}
			if (matching.contains("100")) {
				matching = matching.replace("100", "100杆内");
			}
			if (matching.contains("110")) {
				matching = matching.replace("110", "110杆内");
			}
			if (matching.contains("120")) {
				matching = matching.replace("120", "120杆内");
			}
			if (matching.contains(",")) {
				matching = matching.replace(",", " ");
			}
			
			return matching;
		}
	}

	/**
	 * 球场配套==》中文
	 * 
	 * @param matching
	 * @return
	 */
	public static String parseMatching(String str) {
		if (AbStrUtil.isEmpty(str)) {
			return "";
		} else {
			String matching = str;
			if (matching.contains("coffee")) {
				matching = matching.replace("coffee", "咖啡厅");
			}
			if (matching.contains("dressing")) {
				matching = matching.replace("dressing", "更衣室");
			}
			if (matching.contains("clubhotel")) {
				matching = matching.replace("clubhotel", "会所酒店");
			}
			if (matching.contains("western")) {
				matching = matching.replace("western", "西餐厅");
			}
			if (matching.contains("chinese")) {
				matching = matching.replace("chinese", "中餐厅");
			}
			if (matching.contains("billiardroom")) {
				matching = matching.replace("billiardroom", "桌球室");
			}
			if (matching.contains("cigar")) {
				matching = matching.replace("cigar", "雪茄房");
			}
			if (matching.contains("cinema")) {
				matching = matching.replace("cinema", "影院");
			}
			if (matching.contains("sauna")) {
				matching = matching.replace("sauna", "桑拿");
			}
			if (matching.contains("golfstore")) {
				matching = matching.replace("golfstore", "球具专卖店");
			}
			if (matching.contains("spas")) {
				matching = matching.replace("spas", "温泉浴池");
			}
			if (matching.contains("fitness")) {
				matching = matching.replace("fitness", "健身中心");
			}
			if (matching.contains("massage")) {
				matching = matching.replace("massage", "按摩推拿");
			}
			if (matching.contains("other")) {
				matching = matching.replace("other", "其他服务");
			}
			if (matching.contains("teahouse")) {
				matching = matching.replace("teahouse", "茶艺馆");
			}
			if (matching.contains("playground")) {
				matching = matching.replace("playground", "儿童游乐场");
			}
			if (matching.contains("practice")) {
				matching = matching.replace("practice", "练习场");
			}
			if (matching.contains("cara")) {
				matching = matching.replace("cara", "卡拉OK");
			}
			if (matching.contains("chess")) {
				matching = matching.replace("chess", "棋牌室");
			}
			if (matching.contains("library")) {
				matching = matching.replace("library", "图书馆/阅读室");
			}
			if (matching.contains("footmassage")) {
				matching = matching.replace("footmassage", "足浴按摩");
			}
			if (matching.contains("golfcoach")) {
				matching = matching.replace("golfcoach", "球场教练");
			}
			if (matching.contains("barbecue")) {
				matching = matching.replace("barbecue", "烧烤场");
			}
			if (matching.contains("swimming")) {
				matching = matching.replace("swimming", "酒吧");
			}
			if (matching.contains("bathroom")) {
				matching = matching.replace("bathroom", "沐浴室");
			}
			return matching;
		}

	}

}
