//package com.example.shopmaster.common.httpapi;
//
//
//import org.json.JSONObject;
//
//public abstract class HttpAbst {
//
//	public PostMethod myPost = null;
//	public Gson gson = new Gson();
//
//	/***
//	 * 返回结果 0成功  1失敗,默认失败 2 链接超时 3 没有网络
//	 */
//	public int code = 1;
//
//	/***
//	 * 信息提示
//	 */
//	public String msg = "";
//
//	/**
//	 * 超时时间，默认是3min;
//	 * */
//	public int timeOut=3*60*1000;
//
//	/**
//	 * 编码格式，默认是utf-8
//	 * */
//	public String enCode="utf-8";
//
//	/**
//	 * 加密格式，默认是DES
//	 * */
//	public String sercet="DES";
//
//	/**
//	 * 请求地址
//	 */
//	public abstract String requestUrl();
//
//	/**
//	 * 请求参数封装
//	 */
//	public abstract JSONObject postParams();
//
//	/**
//	 * 请求结果解析
//	 */
//	public abstract void getData(JSONObject jsonObject);
//
//	/***
//	 * 强制释放当前网络请求链接
//	 *
//	 * @param
//	 */
//	public void releaseConnection() {
//		try {
//			if (null != myPost) {
//				myPost.releaseConnection();
//				myPost = null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//	}
//
//}
