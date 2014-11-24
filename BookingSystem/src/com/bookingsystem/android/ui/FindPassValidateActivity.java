package com.bookingsystem.android.ui;

import java.util.Timer;
import java.util.TimerTask;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bookingsystem.android.Constant;
import com.bookingsystem.android.R;
import com.bookingsystem.android.util.StringUtils;

public class FindPassValidateActivity extends BaseActivity implements OnClickListener{

	@InjectView(id=R.id.phonenum)
	TextView v_phonenum;
	@InjectView(id=R.id.code)
	TextView v_code;
	@InjectView(id=R.id.code_btn)
	Button v_codebtn;
	@InjectView(id=R.id.submit)
	Button v_submit;
	
	
Timer timer;
	
	Handler handler = new Handler() { 
		 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
 
            Log.d("debug", "handleMessage方法所在的线程：" 
                    + Thread.currentThread().getName()); 
 
            // Handler处理消息  
            if (msg.what > 0) { 
            	v_codebtn.setText(msg.what+"s后再次获取"); 
            } else { 
            	v_codebtn.setText("获取验证码"); 
            	v_codebtn.setClickable(true);
                // 结束Timer计时器  
                timer.cancel(); 
            } 
        } 
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_findpass_validate);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("找回密码");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		
		v_codebtn.setOnClickListener(this);
		v_submit.setOnClickListener(this);
		initTitleRightLayout();
		timer = new Timer();
	}

	//请求验证码
	private void getCode(){
		String txt_phone = v_phonenum.getText().toString();
		if(StringUtils.isEmpty(txt_phone)){
			showToast("手机号不能为空");
			return;
		}else if(!StringUtils.isPhone(txt_phone)){
			showToast("请填写正确的手机号");
			return;
		}else{
			DhNet net = new DhNet(Constant.BASEURL+"&a=smscode");
			net.addParam("type", 2);
			net.addParam("mobile",txt_phone);
			net.doGetInDialog(new NetTask(this) {
				
				@Override
				public void doInUI(Response response, Integer transfer) {
					// TODO Auto-generated method stub
					Log.v("find pass",response.result);
					if(response.isSuccess()){
						showToast("验证码发送成功,请注意查收");
						TimerTask timerTask = new TimerTask() {
							int i = 300;
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Message msg = new Message(); 
			                    msg.what = i--; 
			                    handler.sendMessage(msg); 
			                    v_codebtn.setClickable(false);
							}
						};
						timer.schedule(timerTask, 0, 1000);// 倒计时间隔为1秒  
					}else{
						showToast(response.msg);
					}
					
				}
			});
		}
	}
	
	
	private void submit(){
		final String txt_phone = v_phonenum.getText().toString();
		String txt_code = v_code.getText().toString();
		if(StringUtils.isEmpty(txt_phone)){
			showToast("手机号不能为空");
			return;
		}else if(!StringUtils.isPhone(txt_phone)){
			showToast("请填写正确的手机号");
			return;
		}else if(StringUtils.isEmpty(txt_code)){
			showToast("请输入短信收到的验证码");
		}else{
			DhNet net =  new DhNet(Constant.BASEURL+"&a=findpasswd1");
			net.addParam("mobile", txt_phone);
			net.addParam("smscode",txt_code);
			net.doPostInDialog(new NetTask(this) {
				
				@Override
				public void doInUI(Response response, Integer transfer) {
					// TODO Auto-generated method stub
					if(response.isSuccess()){
						String mid = JSONUtil.getString(response.jSONFromData(),"mid");
						String token = JSONUtil.getString(response.jSONFromData(),"token");
						Intent i = new Intent(FindPassValidateActivity.this, FindPassNewPassActivity.class);
						i.putExtra("mid",mid);
						i.putExtra("token",token);
						i.putExtra("phone",txt_phone);
						startActivity(i);
						finish();
						
					}else{
						showToast(response.msg);
					}
					
				}
			});
			
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.code_btn:
			getCode();
			break;
		case R.id.submit:
			submit();
		break;
		}
		
	}
	
	public  void initTitleRightLayout() {
		mAbTitleBar.clearRightView();
		View v = mInflater.inflate(R.layout.view_top_right, null);
		Button btn = (Button)v.findViewById(R.id.tbtn);
		btn.setText("注册");
		mAbTitleBar.addRightView(v);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}
}
