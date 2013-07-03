package com.twlkyao.cloudbackup;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *@author coolszy
 *@date 2011-6-16
 *@blog http://blog.csdn.net/coolszy
 */

public class BasicHttpClient {
	private static final int TIME_OUT = 1000 * 5; 		// 超时时间5秒
	private static final String METHOD_POST = "POST";	//操作方法"POST"
	private static final String METHOD_GET = "GET";		//操作方法"GET"
	private static final int HTTP_OK = 200;				//HTTP返回码

	/**
	 * @function 以"GET"方式模拟Http请求
	 * @param urlStr：URL地址
	 * @return
	 */
	
	public String httpGet(String urlStr) {
		URL url = null;					//新建一个URL实例，用于存储请求地址
		HttpURLConnection conn = null;	//新建一个HTTP URL连接实例
		InputStream inStream = null;	//输入流
		String response = null;			//响应
		try {
			url = new URL(urlStr);		//新建一个由urlStr指定的URL实例
			conn = (HttpURLConnection) url.openConnection();	//新建一个连接
			conn.setDoInput(true);		//设置URL连接允许输入
			conn.setConnectTimeout(TIME_OUT);	//设置URL连接的超时时间，单位为毫秒
			conn.setRequestMethod(METHOD_GET);	//设置请求方法为GET方法，只能在建立连接之前设置，否则会抛出异常
			conn.setRequestProperty("accept", "*/*");	//设置特定的请求头
			conn.connect();						//打开一个连接
			int statusCode = conn.getResponseCode();	//获得响应码
			if (statusCode == HTTP_OK) {	//响应码为HTTP_OK
				inStream = conn.getInputStream();	//得到输入流用于读取从服务器端发来的数据
				System.out.println("状态码:" + statusCode);
				response = getResponse(inStream);	//得到写数据的返回码
			} else {
				response = "返回码："+statusCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();	//断开连接
		}
		return response;		//返回响应
	}

	/**
	 * 以"POST"方式模拟Http请求
	 * @param urlStr：URL地址
	 * @param params：传入的参数
	 * @return
	 */
	public String httpPost(String urlStr, String params) {
		byte[] data = params.getBytes();
		URL url = null;
		HttpURLConnection conn = null;		//新建一个HttpURLConnection实例
		InputStream inStream = null;		//新建一个输入流实例
		String response = null;
		try
		{
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();	//新建一个连接
			conn.setConnectTimeout(TIME_OUT);	//设置超时时间
			conn.setDoOutput(true);				//设置URL连接允许输出
			conn.setDoInput(true);				//设置URL连接允许输入
			conn.setUseCaches(false);			//设置不允许使用cache，必须在建立连接之前设置
			conn.setRequestMethod(METHOD_POST);	//设置Http请求方式
			conn.setRequestProperty("Connection", "Keep-Alive");	//设置连接的请求头
			conn.setRequestProperty("Charset", "UTF-8");			//设置编码
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));			//设置内容长度
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");	//设置内容类型
			conn.connect();				//打开一个连接
			DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());	//得到一个输出流用于写数据
			outputStream.write(data);	//向网络中写数据
			outputStream.flush();		//写入数据
			outputStream.close();		//关闭输出流
			int responseCode = conn.getResponseCode();	//返回Http请求码
			if (responseCode == HTTP_OK) {	//操作成功
				inStream = conn.getInputStream();	//得到输出流用于读数据
				response = getResponse(inStream);	//得到输入流的返回码
			} else {
				response = "返回码："+responseCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			conn.disconnect();			//断开连接
		}
		return response;
	}
	
	/**
	 * 获得输入流的数据
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	private String getResponse(InputStream inStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();	//新建一个32Byte的输出流
		int len = -1;
		byte[] buffer = new byte[1024];				//开辟1KB的缓冲区
		while((len = inStream.read(buffer))!=-1)	//如果缓冲区有数据，将数据写入网络
		{
			outputStream.write(buffer, 0, len);		//将buffer中从0开始到len的数据写到输出流outputStream
		}
		byte[] data = outputStream.toByteArray();	//将outputStream的内容转换为Byte
		return new String(data);					//返回输出的数据
	}
}
