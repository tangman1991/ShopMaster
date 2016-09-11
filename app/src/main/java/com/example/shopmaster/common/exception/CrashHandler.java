package com.example.shopmaster.common.exception;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Properties;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.format.Time;


/**
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。
 *                           如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框实现该接口
 *                           并注册为程序中的默认未捕获异常处理
 *                           这样当未捕获异常发生时，就可以做些异常处理操作
 *                           例如：收集异常信息，发送错误报告 等。
 *
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements UncaughtExceptionHandler {

	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;
	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private Properties mDeviceCrashInfo = new Properties(); 
	private static final String VERSION_NAME = "versionName"; 
	private static final String VERSION_CODE = "versionCode"; 
	private static final String STACK_TRACE = "STACK_TRACE";
	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".cr";

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() { 
		if (INSTANCE == null) { 
			INSTANCE = new CrashHandler(); 
		} 
		return INSTANCE; 
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 *
	 * @param ctx
	 */
	public void init(Context ctx) { 
		mContext = ctx; 
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler(); 
		Thread.setDefaultUncaughtExceptionHandler(this); 
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override 
	public void uncaughtException(Thread thread, Throwable ex) { 
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex); 
		} else {
			// Sleep一会后结束程序
			// 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//
//			}
			android.os.Process.killProcess(android.os.Process.myPid()); 
			System.exit(0); 
		} 
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 *
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) { 
		if (ex == null) { 

			return true; 
		} 
		final String msg = ex.getLocalizedMessage(); 
		if(msg == null) {
			return false;
		}
		// 使用Toast来显示异常信息
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				Toast toast = Toast.makeText(mContext, "程序出错啦,我们会尽快修改:\r\n" + msg,
//						Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
//				Looper.loop();
//			}
//		}.start();
		// 收集设备信息
		collectCrashDeviceInfo(mContext); 
		saveCrashInfoToFile(ex);
		return true; 
	}

	/**
	 * 收集程序崩溃的设备信息
	 *
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, ""+pi.versionCode);
			}
		} catch (NameNotFoundException e) {

		}
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		// 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), ""+field.get(null));
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 收集程序崩溃的设备信息
	 *
	 * @param ex
	 */
	private String saveCrashInfoToFile(Throwable ex) { 
		Writer info = new StringWriter(); 
		PrintWriter printWriter = new PrintWriter(info); 
		ex.printStackTrace(printWriter); 
		Throwable cause = ex.getCause(); 
		while (cause != null) { 
			cause.printStackTrace(printWriter); 
			cause = cause.getCause(); 
		} 
		String result = info.toString(); 
		printWriter.close(); 
		mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());
		mDeviceCrashInfo.put(STACK_TRACE, result); 
		try { 
			//long timestamp = System.currentTimeMillis(); 
			Time t = new Time("GMT+8"); 
			t.setToNow();
			int date = t.year * 10000 + t.month * 100 + t.monthDay;
			int time = t.hour * 10000 + t.minute * 100 + t.second;
			String fileName = "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION; 
			FileOutputStream trace = mContext.openFileOutput(fileName, 
					Context.MODE_PRIVATE); 
			mDeviceCrashInfo.store(trace, ""); 
			trace.flush(); 
			trace.close(); 
			return fileName; 
		} catch (Exception e) { 			

		}
		return null; 
	}
}

