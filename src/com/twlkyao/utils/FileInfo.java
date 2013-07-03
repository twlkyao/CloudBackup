/**
 * 作者：	齐士垚
 * 日期：	2013.5.6
 * 功能：	对文件进行操作
 */

package com.twlkyao.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.util.EncodingUtils;

public class FileInfo {
	
	/**
	 * @function 读取文件
	 * @param filePath 文件路径名
	 * @return byte[] 文件内容
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	public byte[] readFile(String filePath) throws FileNotFoundException,IOException {
		String temp;			//用于临时存储文件内容
		FileInputStream fin;	//声明一个文件输入流
	
		fin = new FileInputStream(filePath);//新建一个文件输入流，用于读取文件
		int length;
		length = fin.available();			//返回文件的大小
		byte[] buffer = new byte[length];	//新建一个字节数组，用于存储文件数据
		fin.read(buffer);					//将文件的数据读入到字节数组
		temp = EncodingUtils.getString(buffer, "UTF-8");	//将文件进行UTF-8编码
		fin.close();						//关闭文件输入流 
		return temp.getBytes();				//返回编码后的文件内容
	}
	
	/**
	 * @function 计算文件大小（以字节为单位）
	 * @param filePath 文件路径名
	 * @return size 文件大小
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int fileSize(String filePath) throws FileNotFoundException,IOException{
		FileInputStream fin;	//声明一个文件输入流
		fin = new FileInputStream(filePath);	//新建一个文件输入流，用于读取文件
		int size;
		size = fin.available();					//返回文件的大小
		fin.close();							//关闭文件输入流 
		return size;
	}
	
	/**
	 * @function 向SD卡中写入文件
	 * @param filePath 文件路径
	 * @param message 待写入的文件内容
	 */
	public void writeFile(String filePath,String message){ 
		try{ 
			FileOutputStream fout;	//声明一个文件输出流
			fout = new FileOutputStream(filePath);	//创建一个新的filePath指定的文件输出流，如果文件不存在，
													//则新建文件，如果文件已存在，则在文件中追加内容
			byte [] bytes = message.getBytes(); 
			fout.write(bytes);	//向文件中写入数据
			fout.close(); 		//关闭文件输出流
		} catch(Exception e){ 
			e.printStackTrace(); 
		} 
	}
	
}
