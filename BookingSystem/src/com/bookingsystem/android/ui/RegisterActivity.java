package com.bookingsystem.android.ui;

import java.util.Timer;
import java.util.TimerTask;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.UserShared;
import com.bookingsystem.android.bean.User;
import com.bookingsystem.android.util.StringUtils;

public class RegisterActivity extends MBaseActivity implements OnClickListener {
	@InjectView(id = R.id.phonenum)
	EditText phone;
	@InjectView(id = R.id.password)
	EditText password;
	@InjectView(id = R.id.submit)
	Button login;
	@InjectView(id = R.id.link)
	TextView link;
	@InjectView(id = R.id.check)
	CheckBox check;
	String phonenum, passwordnum, codenum;
	@InjectView(id = R.id.code)
	EditText code;
	@InjectView(id = R.id.code_btn)
	Button btnCode;

	@Inject
	DhDB db;

	Timer timer;
	
	Handler handler = new Handler() { 
		 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
 
            Log.d("debug", "handleMessage方法所在的线程：" 
                    + Thread.currentThread().getName()); 
 
            // Handler处理消息  
            if (msg.what > 0) { 
            	btnCode.setText("发送验证码["+msg.what+"s]"); 
            } else { 
            	btnCode.setText("发送验证码"); 
            	btnCode.setClickable(true);
                // 结束Timer计时器  
                timer.cancel(); 
            } 
        } 
    };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_register);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setTitleText("用户注册");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		initTitleRightLayout();
		login.setOnClickListener(this);
		link.setOnClickListener(this);
		btnCode.setOnClickListener(this);
		timer = new Timer();
		// 定义Handler  
	     
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == link){
			Intent i = new Intent(this, AgreementActivity.class);
			startActivity(i);
		}
		if(view == btnCode){
			phonenum = phone.getText().toString();
			if(AbStrUtil.isEmpty(phonenum)){
				showToast("请先输入手机号码");
				return;
			}else if(!StringUtils.isPhone(phonenum)){
				showToast("请先输入正确的手机号");
				return;
			}
			String url = Constant.BASEURL + "&a=smscode";
			DhNet net = new DhNet(url);
			net.addParam("mobile", phonenum);
			showProgressDialog();
			net.doPost(new NetTask(this) {
				
				@Override
				public void doInUI(Response response, Integer transfer) {
					// TODO Auto-generated method stub
					removeProgressDialog();
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
			                    btnCode.setClickable(false);
							}
						};
						timer.schedule(timerTask, 0, 1000);// 倒计时间隔为1秒  
					}else{
						showToast(response.getMsg());
					}
					
				}
			});
		}
		if (view == login) {
			phonenum = phone.getText().toString();
			passwordnum = password.getText().toString();
			codenum = code.getText().toString();
			if (StringUtils.isPhone(phonenum)) {
				if (!AbStrUtil.isEmpty(codenum)) {
					if (StringUtils.isPassWord(passwordnum)) {
						if (check.isChecked()) {
							String url = Constant.BASEURL + "&a=register";
							DhNet net = new DhNet(url);
							net.addParam("mobile", phonenum);
							net.addParam("pwd", AbMd5.MD5(passwordnum));
							net.addParam("confirmpwd", AbMd5.MD5(passwordnum));
							net.addParam("smscode",codenum);
							showProgressDialog();
							net.doPost(new NetTask(this) {
								@Override
								public void doInUI(Response response,
										Integer transfer) {
									// TODO Auto-generated method stub
									removeProgressDialog();
									if (response.isSuccess()) {
										User user = response
												.modelFromData(User.class);
										Log.v("register", response.result);
										if (user != null
												&& !AbStrUtil.isEmpty(user
														.getMid())) {
											((MApplication) getApplication()).user = user;
											((MApplication) getApplication()).islogin = true;
											//db.save(user);
											UserShared userShared = UserShared.getInstance();
											userShared.user = user;
											userShared.commit();
											finish();
										} else {
											showToast("注册失败");
										}
									} else {
										showToast(response.getMsg());
									}
								}
							});
						} else {
							showToast("请确定阅读服务条款");
						}
					} else {
						showToast("请输入6-16位数字和字母密码");
					}

				} else {
					showToast("请输入验证码");
				}
			} else {
				showToast("请输入手机号");
			}
		}

	}

	public void initTitleRightLayout() {
		mAbTitleBar.clearRightView();
//		Button btn = new Button(this);
//		//btn.setBackgroundDrawable(null);
//		btn.setLayoutParams(layoutParamsWF);
//		btn.setText("一键注册");
//		
//		//步骤一
//
//		try {
//			XmlResourceParser xpp=Resources.getSystem().getXml
//					(R.color.color_white_txt);
//		     ColorStateList csl= ColorStateList.createFromXml(getResources(),
//		     xpp);
//		     btn.setTextColor(csl);
//		} catch (Exception e) {
//		}
//		btn.setTextSize(18);
//		btn.setPadding(5, 0, 15, 0);
//		btn.setGravity(Gravity.CENTER);
//		mAbTitleBar.addRightView(btn);
//		btn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				showToast("跳转一件注册");
//			}
//		});
//		View v = mInflater.inflate(R.layout.view_top_right, null);
//		Button btn = (Button)v.findViewById(R.id.tbtn);
//		btn.setText("一键注册");
//		mAbTitleBar.addRightView(v);
//		
		

	}

}
