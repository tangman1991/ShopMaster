//package com.example.shopmaster.common.httpapi;
//
//import java.io.BufferedInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.Iterator;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.apache.commons.httpclient.ConnectTimeoutException;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//
//import com.example.application.AppApplication;
//import com.example.logutil.LogUtil;
//
//public class HttpApi {
//	public static HttpApi instance;
//	// 工人数量
//	private static final int THREAD_WORKER_COUNT = 5;
//	// 线程池
//	private static final ExecutorService THREAD_WORKER = Executors
//			.newFixedThreadPool(THREAD_WORKER_COUNT);
//
//	private HttpApi() {
//	}
//
//	public static HttpApi getInstance() {
//		if (null == instance) {
//			instance = new HttpApi();
//		}
//		return instance;
//	}
//
//	private long preTime;
//
//	/**
//	 * 关闭线程池
//	 */
//	public void shutdown() {
//		THREAD_WORKER.shutdown();
//	}
//
//	/**
//	 * 可以并发访问
//	 * @param  Abst
//	 *          请求类
//	 */
//	@SuppressWarnings("deprecation")
//	public HttpAbst postStringRequest(HttpAbst Abst) {
//		preTime = System.currentTimeMillis();
//		PostMethod myPost = null;
//		BufferedInputStream bis = null;
//		String str = null;
//		try {
////			if (!NetHelp.isNetworkConn()) {
////				myAbst.erroCode = 3;
////				return str;
////			}
//			String requestParams = Abst.postParams().toString();
//			LogUtil.e("HttpApi url="+Abst.requestUrl()+",request="+requestParams);
//			if("DES".equals(Abst.sercet)){//DES加密
////			    requestParams = DES.encryptDesNoIps(requestParams);//des加密
////			}else if("MD5".equals(Abst.sercet)){
////
////          }else if("RSA".equals(Abst.sercet)){
//
//			}
//			myPost = new PostMethod(Abst.requestUrl());
//			myPost.getParams().setParameter(
//					HttpMethodParams.HTTP_CONTENT_CHARSET, Abst.enCode);
//			myPost.getParams()
//					.setParameter(
//							HttpMethodParams.USER_AGENT,
//							"mozilla/5.0 (linux; u; android 4.1.2; zh-cn; mi-one plus build/jzo54k) applewebkit/534.30 (khtml, like gecko) version/4.0 mobile safari/534.30 mocuz/"
//									+ AppApplication.getAppApplication().getVersion());//添加浏览器头
//			myPost.getParams().setParameter("Accept-Encoding", "identity");//不让服务器进行gzip压缩
//			myPost.setRequestBody(requestParams);
//
//			HttpClient client = new HttpClient();
//			client.getParams().setSoTimeout(Abst.timeOut);
//			client.getParams().setConnectionManagerTimeout(Abst.timeOut);
//
//			int statusCode = client.executeMethod(myPost);
//			if (statusCode == HttpStatus.SC_OK) {
//				bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
//				byte[] strByte = readInputStream(bis);
//				// RecieveMessageDispose recieveMessageDispose = new
//				// RecieveMessageDispose(strByte);
//				// strByte = recieveMessageDispose.disposeReciveMessge();
//				str = new String(strByte, Abst.enCode);
//				str = str.replace("\uFEFF", "");
//				if("DES".equals(Abst.sercet)){//DES解密
////					str = DES.decrypt3DesNOIps(str, "");
////				}else if("MD5".equals(Abst.sercet)){
////
////				}else if("RSA".equals(Abst.sercet)){
//
//				}
//				JSONObject dataJson = new JSONObject(str);
//				Abst.code = 0;
//				Abst.getData(dataJson);
//				LogUtil.e("HttpAi response="+dataJson.toString());
//			}
//		}catch(ConnectTimeoutException ex) {
//			Abst.code = 2;
//		}catch(Exception e){
//			Abst.code = 1;
//		}finally {
//			LogUtil.e("HttpApi 耗时："+(System.currentTimeMillis() - preTime) / 1000 + "秒");
//			if (null != bis) {
//				myPost.releaseConnection();
//				try {
//					bis.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return Abst;
//	}
//
//
//	/**
//	 * 从输入流里面得到返回为二进制的数据
//	 *
//	 * @param inStream
//	 *            输入流
//	 * @return byte[] 二进制数据
//	 * @throws Exception
//	 */
//	public byte[] readInputStream(BufferedInputStream inStream)
//			throws Exception {
//		// 构造一个ByteArrayOutputStream
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		// 设置一个缓冲区
//		byte[] buffer = new byte[1024];
//		int len = 0;
//		// 判断输入流长度是否等于-1，即非空
//		while ((len = inStream.read(buffer)) != -1) {
//			// 把缓冲区的内容写入到输出流中，从0开始读取，长度为len
//			outStream.write(buffer, 0, len);
//		}
//		// 关闭输入流
//		inStream.close();
//		return outStream.toByteArray();
//	}
//
//
//	/**
//	 * post请求（包含文件）
//	 *
//	 */
//	public HttpAbst postFileRequest(HttpAbst Abst, int timeOut,String Code) {
//		try {
//			LogUtil.e("HttpApi url="+Abst.requestUrl()+",request="+Abst.postParams().toString());
//			// 请求实体
//			HttpPost request = new HttpPost(Abst.requestUrl());
//			// 参数实体
//			MultipartEntity mpEntity = new MultipartEntity();
//			JSONObject jsonObject = Abst.postParams();
//			Iterator<?> iterator = jsonObject.keys();
//			while (iterator.hasNext()) {
//				String key = iterator.next().toString();
//				Object object = jsonObject.get(key);
//				// 参数传输
//				if (object instanceof String) {
//					mpEntity.addPart(key, new StringBody((String) object,
//							Charset.forName("UTF-8")));
//				}
//				// 文件传输
//				else if (object instanceof File) {
//					mpEntity.addPart(key, new FileBody((File) object));
//				}
//			}
//
//			// 请求httpRequest
//			request.setEntity(mpEntity);
//			// Http客户端的配置参数，执行请求，获得响应实体
//			HttpParams httpParameters = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
//			HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
//
//			// 默认Http客户端，执行请求，获得响应实体
//			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
//
//			// 响应实体
//			HttpResponse httpResponse = httpclient.execute(request);
//			// HttpStatus.SC_OK表示连接成功
//			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				HttpEntity entity = httpResponse.getEntity();
//				if (entity != null) {
//					// 取得返回的字符串
//					byte[] strByte = EntityUtils.toByteArray(entity);
//					// RecieveMessageDispose recieveMessageDispose = new
//					// RecieveMessageDispose(strByte);
//					// strByte = recieveMessageDispose.disposeReciveMessge();
//					String result = new String(strByte, "UTF-8");
//
////					result = DES.decrypt3DesNOIps(result, "");
//					JSONObject dataJson = new JSONObject(result);
//					LogUtil.e("HttpAi_file response="+dataJson.toString());
//					Abst.code=0;
//					Abst.getData(dataJson);
//				}
//
//			}
//		}catch(ConnectTimeoutException ex) {
//			Abst.code = 2;
//		}catch(Exception e){
//			Abst.code = 1;
//		}finally{
//
//		}
//		return Abst;
//	}
//
//}
