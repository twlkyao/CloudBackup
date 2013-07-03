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
	private static final int TIME_OUT = 1000 * 5; 		// ��ʱʱ��5��
	private static final String METHOD_POST = "POST";	//��������"POST"
	private static final String METHOD_GET = "GET";		//��������"GET"
	private static final int HTTP_OK = 200;				//HTTP������

	/**
	 * @function ��"GET"��ʽģ��Http����
	 * @param urlStr��URL��ַ
	 * @return
	 */
	
	public String httpGet(String urlStr) {
		URL url = null;					//�½�һ��URLʵ�������ڴ洢�����ַ
		HttpURLConnection conn = null;	//�½�һ��HTTP URL����ʵ��
		InputStream inStream = null;	//������
		String response = null;			//��Ӧ
		try {
			url = new URL(urlStr);		//�½�һ����urlStrָ����URLʵ��
			conn = (HttpURLConnection) url.openConnection();	//�½�һ������
			conn.setDoInput(true);		//����URL������������
			conn.setConnectTimeout(TIME_OUT);	//����URL���ӵĳ�ʱʱ�䣬��λΪ����
			conn.setRequestMethod(METHOD_GET);	//�������󷽷�ΪGET������ֻ���ڽ�������֮ǰ���ã�������׳��쳣
			conn.setRequestProperty("accept", "*/*");	//�����ض�������ͷ
			conn.connect();						//��һ������
			int statusCode = conn.getResponseCode();	//�����Ӧ��
			if (statusCode == HTTP_OK) {	//��Ӧ��ΪHTTP_OK
				inStream = conn.getInputStream();	//�õ����������ڶ�ȡ�ӷ������˷���������
				System.out.println("״̬��:" + statusCode);
				response = getResponse(inStream);	//�õ�д���ݵķ�����
			} else {
				response = "�����룺"+statusCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();	//�Ͽ�����
		}
		return response;		//������Ӧ
	}

	/**
	 * ��"POST"��ʽģ��Http����
	 * @param urlStr��URL��ַ
	 * @param params������Ĳ���
	 * @return
	 */
	public String httpPost(String urlStr, String params) {
		byte[] data = params.getBytes();
		URL url = null;
		HttpURLConnection conn = null;		//�½�һ��HttpURLConnectionʵ��
		InputStream inStream = null;		//�½�һ��������ʵ��
		String response = null;
		try
		{
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();	//�½�һ������
			conn.setConnectTimeout(TIME_OUT);	//���ó�ʱʱ��
			conn.setDoOutput(true);				//����URL�����������
			conn.setDoInput(true);				//����URL������������
			conn.setUseCaches(false);			//���ò�����ʹ��cache�������ڽ�������֮ǰ����
			conn.setRequestMethod(METHOD_POST);	//����Http����ʽ
			conn.setRequestProperty("Connection", "Keep-Alive");	//�������ӵ�����ͷ
			conn.setRequestProperty("Charset", "UTF-8");			//���ñ���
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));			//�������ݳ���
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");	//������������
			conn.connect();				//��һ������
			DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());	//�õ�һ�����������д����
			outputStream.write(data);	//��������д����
			outputStream.flush();		//д������
			outputStream.close();		//�ر������
			int responseCode = conn.getResponseCode();	//����Http������
			if (responseCode == HTTP_OK) {	//�����ɹ�
				inStream = conn.getInputStream();	//�õ���������ڶ�����
				response = getResponse(inStream);	//�õ��������ķ�����
			} else {
				response = "�����룺"+responseCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			conn.disconnect();			//�Ͽ�����
		}
		return response;
	}
	
	/**
	 * ���������������
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	private String getResponse(InputStream inStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();	//�½�һ��32Byte�������
		int len = -1;
		byte[] buffer = new byte[1024];				//����1KB�Ļ�����
		while((len = inStream.read(buffer))!=-1)	//��������������ݣ�������д������
		{
			outputStream.write(buffer, 0, len);		//��buffer�д�0��ʼ��len������д�������outputStream
		}
		byte[] data = outputStream.toByteArray();	//��outputStream������ת��ΪByte
		return new String(data);					//�������������
	}
}
