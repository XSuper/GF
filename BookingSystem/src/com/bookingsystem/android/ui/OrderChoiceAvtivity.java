package com.bookingsystem.android.ui;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.alipay.Keys;
import com.bookingsystem.android.alipay.Result;
import com.bookingsystem.android.alipay.Rsa;
import com.bookingsystem.android.util.StringUtils;

public class OrderChoiceAvtivity extends MBaseActivity implements OnClickListener{

	@InjectView(id=R.id.payprice)
	TextView payprice;
	@InjectView(id=R.id.submit)
	Button submit;
	
	
	@InjectExtra(name="subject")
	String subject;
	
	@InjectExtra(name="body")
	String body;
	@InjectExtra(name="payprice")
	String price;
	@InjectExtra(name="orderid")
	String orderid;
	
	String out_trade_no;
	public static final String TAG = "alipay-sdk-orderchoice";
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);
			
			int code = result.getResuleCode();
			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				switch (code) {
				case 4000:
				case 6001:
				case 6002:
				case 8000:
					showToast(result.getResuleStr());
					break;
				case 9000:
					final DialogInterface.OnClickListener cancle = new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							ActivityTack.getInstanse().removeActivityByClass(OrderChoiceAvtivity.class);
						}
						
					};
					showDialog("支付成功", "是否跳转到我的订单页面查看", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Intent i = new Intent(getBaseContext(), MyOrderActivity.class);
							i.putExtra("type", 1);
							startActivity(i);
							ActivityTack.getInstanse().removeActivityByClass(OrderChoiceAvtivity.class);
						}
						
					},cancle);
					//ActivityTack.getInstanse().removeActivityByClass(QCOrder.class);
					ActivityTack.getInstanse().removeActivityByClass(PLOrder.class);
					ActivityTack.getInstanse().removeActivityByClass(GroundOrder.class);
					break;

				default:
					showToast(result.getResuleStr());
					break;
				}
				
				
			}
				break;
			default:
				break;
			}
		}

		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_order_choice);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("提交订单");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		
		ViewUtil.bindView(payprice, price, "price");
		
		submit.setOnClickListener(this);
	}	
	
	
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(out_trade_no);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(price);
//		sb.append(0.01);
		sb.append("\"&notify_url=\"");
		
		
//		sb.append("\"&defaultbank=\"ICBCB2C");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://pay.gocen.cn:8060/payment/integrationNotifyUrlController.htm"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	
	private void doPay(){
		String info = getNewOrderInfo();
		String sign = Rsa.sign(info, Keys.PRIVATE);
		sign = URLEncoder.encode(sign);
		info += "&sign=\"" + sign + "\"&" + getSignType();
		Log.i("ExternalPartner", "start pay");
		// start the pay.
		Log.i(TAG, "info = " + info);

		final String orderInfo = info;
		new Thread() {
			public void run() {
				//设置为沙箱模式，不设置默认为线上环境
				//alipay.setSandBox(true);
				
				// 构造PayTask 对象
				PayTask alipay = new PayTask(OrderChoiceAvtivity.this);
				// 调用支付接口
				String result = alipay.pay(orderInfo);

				Log.i(TAG, "result = " + result);
				Message msg = new Message();
				msg.what = RQF_PAY;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d("orderchoiceactivity", "outTradeNo: " + key);
		return key;
	}
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	@Override
	public void onClick(View arg0) {
		DhNet net = new DhNet(Constant.BASEURL+"&a=orderstatus");
		net.addParam("orderid",orderid);
		net.addParam("token", MApplication.user.token);
		net.doPostInDialog(new NetTask(this){
			
			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				Log.e("sdasda", response.result);
				JSONObject jo = null;
				int status = -1;
				try {
					jo = (JSONObject) response.jSONFromData();
					status = JSONUtil.getInt(jo, "status",-1);
					out_trade_no = JSONUtil.getString(jo, "trade_no");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(status ==0){
					if(!StringUtils.isEmpty(out_trade_no)){
						Log.v("订单号", out_trade_no);
						doPay();
					}else{
						showToast("订单号有误");
					}
				}else{
					showToast(response.msg);
				}
				
			}
		});
		
	}
}
