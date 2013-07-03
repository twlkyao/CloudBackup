/**
 * ���ߣ�	��ʿ��
 * ���ڣ�2013.5.7
 * ���ܣ����ļ���SHA1��MD5ժҪ�����ҿ�����֤�ļ���ժҪֵ
 */

package com.twlkyao.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestMethod {
	
	/**
	 * @function �����ļ���SHA1ֵ����16���Ʊ�ʾ
	 * @param filePath �ļ�·��
	 * @return byte[] �ļ���SHA1ֵ
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public byte[] SHA1(String filePath) throws FileNotFoundException, IOException {
		File file=new File(filePath);	//����ָ����·������һ���µ��ļ�
		FileInputStream fileInputStream = new FileInputStream(file);	//����һ���µ��ļ�������
		MessageDigest md;	//����һ��MessageDigestʵ��
		try {
			md = MessageDigest.getInstance("SHA-1");	//�õ�һ���µ�MessageDigestʵ����ʹ��SHA1�㷨
			byte[] buffer = new byte[1024 * 10];		//�½�һ��10KB buffer[]�����ڴ洢�ļ�����
			int length = 0;								//��ʼ����ȡ���ļ����ݳ���
			while ((length = fileInputStream.read(buffer)) >0) {
				md.update(buffer, 0, length);			//�ö���ͨ��ʹ�� update()�����������ݣ�ÿ�θ���
		    }
			return md.digest();							//���ڸ��������ĸ������ݣ�digest()����ֻ�ܱ�����һ�Ρ�
														//�ڵ��� digest()֮��MessageDigest�����������ó����ʼ״̬��
		} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} finally{
			fileInputStream.close();					//�ڼ���SHA1ֵ֮�󣬹ر��ļ�������
		}
		return null;
	}
	
	/**
	 * @function �����ļ���MD5ֵ����16���Ʊ�ʾ
	 * @param filePath �ļ�·��
	 * @return byte[] �ļ���MD5ֵ
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public byte[] MD5(String filePath) throws FileNotFoundException, IOException {

		File file=new File(filePath);	//����ָ����·������һ���µ��ļ�
		FileInputStream fileInputStream = new FileInputStream(file);	//����һ���µ��ļ�������
		MessageDigest md;	//����һ��MessageDigestʵ��
		try {
			md = MessageDigest.getInstance("MD5");		//�õ�һ���µ�MessageDigestʵ����ʹ��MD5�㷨
			byte[] buffer = new byte[1024 * 10];		//�½�һ��1KB��buffer[]�����ڴ洢�ļ�����
			int length = 0;								//��ʼ����ȡ���ļ����ݳ���
			while ((length = fileInputStream.read(buffer)) >0) {
				md.update(buffer, 0, length);			//�ö���ͨ��ʹ�� update()�����������ݣ�ÿ�θ���
		    }
			return md.digest();							//���ڸ��������ĸ������ݣ�digest()����ֻ�ܱ�����һ�Ρ�
														//�ڵ��� digest()֮��MessageDigest�����������ó����ʼ״̬��
		} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} finally{
			fileInputStream.close();					//�ڼ���SHA1ֵ֮�󣬹ر��ļ�������
		}
		return null;
	}
	
	
	/**
	 * @function ��������G����ļ�
	 * @param path
	 * @return
	 * @throws OutOfMemoryError
	 * @throws IOException
	 */
	/**
	public byte[] getFileSha1(String path) throws OutOfMemoryError,IOException {
		File file=new File(path);		//����ָ����·������һ���µ��ļ�
		FileInputStream in = new FileInputStream(file);	//����һ���µ��ļ�������
		MessageDigest md;	//����һ��MessageDigestʵ��
		try {
			md = MessageDigest.getInstance("SHA-1");	//�õ�һ���µ�MessageDigestʵ����ʹ��MD5�㷨
		
			byte[] buffer = new byte[1024 * 1024 * 10];	//�½�һ��10MB��buffer[]�����ڴ洢�ļ�����
			int len = 0;
			while ((len = in.read(buffer)) >0) {		//�ö���ͨ��ʹ�� update()�����������ݣ�ÿ�θ���
				//�ö���ͨ��ʹ�� update()������������
				md.update(buffer, 0, len);
		    }
			return md.digest();						//���ڸ��������ĸ������ݣ�digest()����ֻ�ܱ�����һ�Ρ�
													//�ڵ��� digest()֮��MessageDigest�����������ó����ʼ״̬��
		} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} finally{
			in.close();								//�ڼ���SHA1ֵ֮�󣬹ر��ļ�������
		}
		return null;
	}
	*/
	
	/**
	 * @function У���ļ���ժҪֵ
	 * @param b ժҪֵ����
	 * @param filePath ��У����ļ���·��
	 * @param method У�鷽��
	 * @return result У���������
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public boolean isEqual(byte[] b, String filePath, String method) throws FileNotFoundException, IOException{
		boolean result =false;	//��ʼ�����Ϊfalse
		if(method.equals("MD5")){	//У�鷽��ΪMD5
			if (MessageDigest.isEqual(b, MD5(filePath))) {
				result = true;
			} else {
				result = false;
			}
		}
		else if(method.equals("SHA-1")){	//У�鷽��ΪSHA-1
			if (MessageDigest.isEqual(b, SHA1(filePath))) {
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}
	
	/**��byte����ת��Ϊ16���Ƶ��ַ���
	 * @function ��256���Ƶ�byteת��Ϊ16���Ƶ��ַ���
	 * @param b byte����
	 * @return str 16�����ַ���
	 */
	public String byte2Str(byte[] b)
	{
		String str = "";	//���ڴ洢�����
		String stmp = "";	//���ڴ洢ÿ��byteת��Ϊ16���ƵĽ��
		for (int i = 0; i < b.length; i++) {
			stmp = (Integer.toHexString(b[i] & 0xff));	//�Ƚ�byteת��Ϊ������Ȼ������ת��Ϊ16���ƵĴ�
			if (stmp.length() == 1)	//�������2λ��ǰ�߲�0��Ȼ��׷�ӵ������
				str = str + "0" + stmp;
			else					//��ת�����׷�ӵ������
				str = str + stmp;
		}
		return str;
	}
	
	
	/**
	 * @function �����ļ�·�������ļ���С����λΪ�ֽ�
	 * @param filePath �ļ�·����
	 * @return size �ļ���С����λΪ�ֽ�
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public long FileSize(String filePath) throws FileNotFoundException,IOException{
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
		FileInputStream fin;					//�ļ�������
		fin = new FileInputStream(filePath);	//�½�һ���ļ������������ڶ�ȡ�ļ�
		int size;
		size = fin.available();				//�����ļ��Ĵ�С
		fin.close();						//�ر��ļ������� 
		return size;
	}
		return 0;
}
	/**
	 * @function �����ļ�·�������ļ��Ĵ�С����λΪ�ֽ�
	 * @param filePath �ļ�·����
	 * @return �ļ���С����λΪ�ֽ�
	 */
	public long getSize(String filePath) {	
		File file = new File(filePath);		//����·���½�һ���ļ�
		if (file.exists() && file.isFile()) {	//����ļ����ڲ��Ҳ���Ŀ¼
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				return fis.available();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	return 0;
}
	
	/**
	 * fuction �����ļ�·�������ļ��Ĵ�С����λΪ�ֽ�
	 * @param filePath �ļ�·����
	 * @return size �ļ���С
	 */
	public long fileSize(String filePath) {
		File file = new File(filePath);
		long size = file.length();			//��ȡ�ļ��Ĵ�С����λΪ�ֽ�
		return size;
	}
}
