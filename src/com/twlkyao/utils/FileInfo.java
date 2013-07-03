/**
 * ���ߣ�	��ʿ��
 * ���ڣ�	2013.5.6
 * ���ܣ�	���ļ����в���
 */

package com.twlkyao.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.util.EncodingUtils;

public class FileInfo {
	
	/**
	 * @function ��ȡ�ļ�
	 * @param filePath �ļ�·����
	 * @return byte[] �ļ�����
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	public byte[] readFile(String filePath) throws FileNotFoundException,IOException {
		String temp;			//������ʱ�洢�ļ�����
		FileInputStream fin;	//����һ���ļ�������
	
		fin = new FileInputStream(filePath);//�½�һ���ļ������������ڶ�ȡ�ļ�
		int length;
		length = fin.available();			//�����ļ��Ĵ�С
		byte[] buffer = new byte[length];	//�½�һ���ֽ����飬���ڴ洢�ļ�����
		fin.read(buffer);					//���ļ������ݶ��뵽�ֽ�����
		temp = EncodingUtils.getString(buffer, "UTF-8");	//���ļ�����UTF-8����
		fin.close();						//�ر��ļ������� 
		return temp.getBytes();				//���ر������ļ�����
	}
	
	/**
	 * @function �����ļ���С�����ֽ�Ϊ��λ��
	 * @param filePath �ļ�·����
	 * @return size �ļ���С
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int fileSize(String filePath) throws FileNotFoundException,IOException{
		FileInputStream fin;	//����һ���ļ�������
		fin = new FileInputStream(filePath);	//�½�һ���ļ������������ڶ�ȡ�ļ�
		int size;
		size = fin.available();					//�����ļ��Ĵ�С
		fin.close();							//�ر��ļ������� 
		return size;
	}
	
	/**
	 * @function ��SD����д���ļ�
	 * @param filePath �ļ�·��
	 * @param message ��д����ļ�����
	 */
	public void writeFile(String filePath,String message){ 
		try{ 
			FileOutputStream fout;	//����һ���ļ������
			fout = new FileOutputStream(filePath);	//����һ���µ�filePathָ�����ļ������������ļ������ڣ�
													//���½��ļ�������ļ��Ѵ��ڣ������ļ���׷������
			byte [] bytes = message.getBytes(); 
			fout.write(bytes);	//���ļ���д������
			fout.close(); 		//�ر��ļ������
		} catch(Exception e){ 
			e.printStackTrace(); 
		} 
	}
	
}
