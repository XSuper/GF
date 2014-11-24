package com.bookingsystem.android.welcome;

import net.duohuo.dhroid.activity.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bookingsystem.android.HomeActivity;
import com.bookingsystem.android.R;

public class WelcomeActivity extends BaseActivity implements
		OnViewChangeListener {

	private MyScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private Button startBtn;
	private LinearLayout pointLLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		abSharedPreferences.edit().putString("isfirst", "No").commit();
		setContentView(R.layout.welcome_layout);
		initView();

	}

	private void initView() {
		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		pointLLayout = (LinearLayout) findViewById(R.id.llayout);
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(onClick);
		count = mScrollLayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
				Intent intent = new Intent(WelcomeActivity.this,
						HomeActivity.class);
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
				
				break;
			}
		}
	};

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}
}