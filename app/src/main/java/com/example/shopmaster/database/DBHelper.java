package com.example.shopmaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    
	private static final String DATABASE_NAME = "tm.db";  
    private static final int DATABASE_VERSION = 1;  
	private Context context;
	
	//表名
	private static final String  TABLE_NAME="";
	
	public DBHelper(Context context) {		
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		this.context=context;
	}
    
	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		//创建数据库
		String tableSql=" create table if not exists "+TABLE_NAME
		      + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
			  + " name VARCHAR, "
			  + " age INTEGER, "
			  + " info TEXT ) ";
		arg0.execSQL(tableSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		onCreate(arg0);
	}

}
