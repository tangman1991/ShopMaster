package com.example.shopmaster.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DBUtil {
	private static DBUtil mInstance;
	private Context mContext;
	private DBHelper dBHelp;
	
	/**
	 * 初始化
	 */
	private DBUtil(Context context){
		mContext = context;
		dBHelp = new DBHelper(context);
	}
	
	/**
	 * 单例模式
	 */
	public static DBUtil getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBUtil(context);
		}
		return mInstance;
	}
	/**
	 * 关闭数据库
	 */
	private void closeDB() {
		dBHelp.close();
		dBHelp = null;
		// if(mSQLiteDatabase.isOpen())
		// mSQLiteDatabase.close();
		// mSQLiteDatabase = null;
		mInstance = null;
	}
	
	/**
	 * 添加
	 * */
	private void addData(String tableName,ContentValues values){
		SQLiteDatabase sqlite=null;
		try{
			sqlite=dBHelp.getWritableDatabase();
			sqlite.beginTransaction();
			sqlite.insert(tableName, null, values);
			sqlite.endTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(null != sqlite){			
			sqlite.close();
		}
		
	}
	/**
	 * 更新
	 * */
	private void updateData(String tableName,ContentValues values,
			        String whereClause, String[] whereArgs){
		SQLiteDatabase sqlite=null;
		try{
			sqlite=dBHelp.getWritableDatabase();
			sqlite.beginTransaction();
			sqlite.update(tableName, values, whereClause, whereArgs);
			sqlite.endTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(null != sqlite){			
			sqlite.close();
		}
		
	}
	/**
	 * 删除
	 * */
	private void deleteData(String tableName,String whereClause, String[] whereArgs){
		SQLiteDatabase sqlite=null;
		try{
			sqlite=dBHelp.getWritableDatabase();
			sqlite.beginTransaction();
			sqlite.delete(tableName, whereClause, whereArgs);
			sqlite.endTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(null != sqlite){			
			sqlite.close();
		}		
	}
	/**
	 * 搜索列表
	 * */
	private List<Map<String, String>> selectData(boolean distinct,String tableName,
			String[] columns, String selection,String[] selectionArgs, String groupBy,
			String having,String orderBy, String limit){
		SQLiteDatabase sqlite=null;
		List<Map<String,String>> list=null;
		Cursor cursor = null;
		Map<String, String> map = null;
		try{
			sqlite=dBHelp.getWritableDatabase();
			sqlite.beginTransaction();
			cursor=sqlite.query(distinct, tableName, columns, selection, 
					selectionArgs, groupBy, having, orderBy, limit);
			int length=cursor.getColumnCount();
			while(cursor.moveToNext()){
				map= new HashMap<String, String>();
				for (int i = 0; i < length; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
				list.add(map);
			}
			sqlite.endTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(null != cursor){
			cursor.close();
		}
		if(null != sqlite){			
			sqlite.close();
		}
		return list;
	}
}
