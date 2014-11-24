package net.duohuo.dhroid.activity;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.ioc.InjectUtil;
import android.os.Bundle;

/***
 *  
 * @author duohuo-jinghao 
 *
 */
public  class BaseActivity extends AbActivity   {	
	
	private ActivityTack tack=ActivityTack.getInstanse();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tack.addActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ImageLoader.getInstance().clearMemoryCache();
	}
	@Override
	public void finish() {
		super.finish();
		tack.removeActivity(this);
		
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		if(Const.auto_inject){
			InjectUtil.inject(this);
		}
	}
	
	public void setAbContentView(int layoutResID) {
		super.setAbContentView(layoutResID);
		if(Const.auto_inject){
			InjectUtil.inject(this);
		}
	}
}
