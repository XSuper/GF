package com.bookingsystem.android.ui;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;

public class RefundActivity extends BaseActivity {

	@InjectView(id=R.id.refund_reason)
	Spinner refundReson;
	@InjectView(id=R.id.refund_money)
	TextView refundMoney;
	@InjectView(id=R.id.my_reason)
	EditText myReason;
	@InjectView(id=R.id.submit)
	Button submit;
	
	String orderid;
	double money;
	int refundtype = 1;
	DhNet http = new DhNet(Constant.BASEURL+"&a=applyrefund");
	
	private static final String[] m_arr = {"行程安排原因","天气原因","个人原因","其他原因"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_refund);

		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("申请退款");
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		
		orderid = getIntent().getStringExtra("orderid");
		money = getIntent().getDoubleExtra("money",0);
		refundMoney.setText(String.format("%.2f", money*0.85));
        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_arr);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refundReson.setAdapter(ada);
        refundReson.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
            	//
            	refundtype = arg2+1;
            }
            public void onNothingSelected(AdapterView<?> arg0){
                //
            }
        });
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refund();
			}
		});
        
	}
	
	public void refund(){
		
		http.addParam("orderid", orderid);
		http.addParam("uid", MApplication.user.mid);
		http.addParam("token", MApplication.user.token);
		http.addParam("price", String.format("%.2f", money*0.85));
		http.addParam("refundtype",refundtype);
		http.addParam("reason",myReason.getText().toString()+"");
		http.doPostInDialog("请稍后", new NetTask(this) {
			
			@Override
			public void doInUI(Response response, Integer transfer) {
				
				int err = JSONUtil.getInt(response.jSON(),"err");
				if(err==0){
					showToast("申请成功");
					finish();
				}else{
					showToast(JSONUtil.getString(response.jSON(),"msg"));
				}
			}
			
		});
	}
}
