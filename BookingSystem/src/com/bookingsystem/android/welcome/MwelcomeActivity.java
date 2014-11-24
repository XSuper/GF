package com.bookingsystem.android.welcome;

import java.util.List;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.adapter.ValueFix;
import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.dialog.DialogImpl;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.Instance.InstanceScope;
import net.duohuo.dhroid.ioc.IocContainer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.ab.global.AbConstant;
import com.ab.util.AbAppUtil;
import com.ab.util.AbStrUtil;
import com.bookingsystem.android.HomeActivity;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.MValueFixer;
import com.bookingsystem.android.R;
import com.bookingsystem.android.bean.Place;
import com.bookingsystem.android.bean.User;
import com.bookingsystem.android.util.CityUtil;

public class MwelcomeActivity extends Activity {
	
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final View view = new LinearLayout(this);
		view.setBackgroundResource(R.drawable.welcome);
		setContentView(view);

		SharedPreferences abSharedPreferences = getSharedPreferences(
				AbConstant.SHAREPATH, Context.MODE_PRIVATE);
		final boolean notFirst = abSharedPreferences.contains("isfirst");

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.9f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);

		

		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Class target = null;
				if (notFirst) {

					target = HomeActivity.class;
				} else {
					target = WelcomeActivity.class;
				}

				Intent intent = new Intent(MwelcomeActivity.this, target);
				startActivity(intent);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
