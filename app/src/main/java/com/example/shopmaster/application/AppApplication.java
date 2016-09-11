package com.example.shopmaster.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.shopmaster.common.exception.CrashHandler;
import com.example.shopmaster.database.DBUtil;

/**
 * Created by zyn on 2016/9/9.
 */
public class AppApplication extends Application{

    private Context context;
    //全局applicatioin
    private  static AppApplication  mAppApplication;
    //数据库
    private DBUtil dBUtil;
    //是否开启log             true:开启; false:关闭
    private   boolean isLog = true;
    //是否开启报错捕获                                true:开启; false:关闭
    private   boolean isException = true;
    //版本
    private   String version = "";

    //单例模式
    public static AppApplication getAppApplication(){
        if(null == mAppApplication){
            mAppApplication = new AppApplication();
        }
        return mAppApplication;
    }

    public  DBUtil getDBUtil() {
        if(null == dBUtil){
            dBUtil=DBUtil.getInstance(context);
        }
        return dBUtil;
    }

    public boolean isLog() {
        return isLog;
    }

    public  boolean isException() {
        return isException;
    }

    public  String getVersion() {
        return version;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO Auto-generated method stub
        mAppApplication=this;
        context=this.getApplicationContext();
        // 启动数据库
        getDBUtil();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            version = pi.versionName;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(isException){
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }

    }
}
