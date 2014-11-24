package com.bookingsystem.android.ui;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class LoginActivity extends MBaseActivity implements OnClickListener{
	@InjectView(id=R.id.phonenum)
	EditText phone;
	@InjectView(id=R.id.password)
	EditText password;
	@InjectView(id=R.id.forgetPass)
	TextView forgetPass;
	@InjectView(id=R.id.submit)
	Button login;
	
	
	
	String phonenum,passwordnum;
	@Inject
	DhDB db;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_login);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setTitleText("用户登陆");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		initTitleRightLayout();
		login.setOnClickListener(this);
		forgetPass.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == login){
			phonenum = phone.getText().toString();
			passwordnum = password.getText().toString();
			if(StringUtils.isPhone(phonenum)){
				if(StringUtils.isPassWord(passwordnum)){
					DhNet net = new DhNet(Constant.BASEURL+"&a=login");
					net.addParam("mobile", phonenum);
					net.addParam("pwd", AbMd5.MD5(passwordnum));
					showProgressDialog();
					net.doPost(new NetTask(this) {
						
						@Override
						public void doInUI(Response response, Integer transfer) {
							// TODO Auto-generated method stub
							removeProgressDialog();
							if(response.isSuccess()){
								User user= response.modelFromData(User.class);
								
								Log.v("login",response.result );
								//Log.v("login-user",user.getMobile() );
								if(user!=null&&!AbStrUtil.isEmpty(user.getMid())){
									((MApplication)getApplication()).user = user;
									((MApplication)getApplication()).islogin = true;
									//db.save(user);
									UserShared userShared = UserShared.getInstance();
									userShared.user = user;
									userShared.commit();
									finish();
									
								}else {
									showToast(response.getMsg());
								}
							}else{
								showToast(response.getMsg());
							}
							
						}
					});
				}else{
					showToast("请输入6-16位数字和字母密码");
				}
			}else{
				showToast("请输入手机号");
			}
		}
		
		if(view == forgetPass){
			Intent i = new Intent(this,FindPassValidateActivity.class);
			startActivity(i);
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
				Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

}
