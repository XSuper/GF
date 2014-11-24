package com.bookingsystem.android.ui;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ab.util.AbMd5;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.R;
import com.bookingsystem.android.util.StringUtils;

public class FindPassNewPassActivity extends BaseActivity implements
		OnClickListener {

	@InjectView(id= R.id.password1)
	EditText v_passwd1;
	
	@InjectView(id= R.id.password2)
	EditText v_passwd2;
	@InjectView(id= R.id.submit)
	Button v_submit;

	@InjectView(id= R.id.phone)
	TextView v_phone;
	@InjectExtra(name="mid")
	String mid;
	@InjectExtra(name="phone")
	String phone;
	@InjectExtra(name="token")
	String token;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_findpass_newpass);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("重置密码");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		v_submit.setOnClickListener(this);
		v_phone.setText(phone);
		
		initTitleRightLayout();
	}

	
	private void submit() {
		String txt_1 = v_passwd1.getText().toString();
		String txt_2 = v_passwd2.getText().toString();
		if (StringUtils.isEmpty(txt_1)) {
			showToast("密码不能为空");
			return;
		} 
		else if (!StringUtils.isPassWord(txt_1)) {
			showToast("请输入6-16位字符");
		} 
		else if (StringUtils.isEmpty(txt_2)) {
			showToast("请重复输入密码");
			return;
		} 
		else if (!txt_1.equals(txt_2)) {
			showToast("两次输入密码不一致");
		} 
		else {
			DhNet net = new DhNet(Constant.BASEURL + "&a=findpasswd2");
			net.addParam("userid", mid);
			net.addParam("pwd", AbMd5.MD5(txt_1));
			net.addParam("confirmpwd", AbMd5.MD5(txt_2));
			net.addParam("token",token);
			net.doPostInDialog(new NetTask(this) {

				@Override
				public void doInUI(Response response, Integer transfer) {
					// TODO Auto-generated method stub
					if (response.isSuccess()) {
						showToast("重置密码成功");
//						Intent i = new Intent(FindPassNewPassActivity.this, LoginActivity.class);
//						startActivity(i);
						finish();

					} else {
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
