package net.duohuo.dhroid;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.dialog.DialogImpl;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.Instance.InstanceScope;
import net.duohuo.dhroid.ioc.Ioc;
import net.duohuo.dhroid.ioc.IocContainer;
import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
/**
 * 完成一些系统的初始化的工作
 * @author Administrator
 *
 */
public class Dhroid {
	public static void init(Application app){
		Ioc.initApplication(app);
		//对话框的配置
		Ioc.bind(DialogImpl.class).to(IDialog.class).scope(InstanceScope.SCOPE_PROTOTYPE);
		
		ImageLoaderConfiguration	 imageconfig = new ImageLoaderConfiguration.Builder(app.getApplicationContext())
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.diskCacheSize(50 * 1024 * 1024) // 50 Mb
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.build();
		ImageLoader.getInstance().init(imageconfig);
		//数据库初始化
		DhDB db=IocContainer.getShare().get(DhDB.class);
		db.init("dhdbname", Const.DATABASE_VERSION);
	}
}
