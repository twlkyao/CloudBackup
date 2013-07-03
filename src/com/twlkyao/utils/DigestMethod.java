/**
 * 作者：	齐士垚
 * 日期：2013.5.7
 * 功能：对文件进SHA1和MD5摘要，并且可以验证文件的摘要值
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
	 * @function 计算文件的SHA1值，以16进制表示
	 * @param filePath 文件路径
	 * @return byte[] 文件的SHA1值
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public byte[] SHA1(String filePath) throws FileNotFoundException, IOException {
		File file=new File(filePath);	//利用指定的路径构建一个新的文件
		FileInputStream fileInputStream = new FileInputStream(file);	//构建一个新的文件输入流
		MessageDigest md;	//声明一个MessageDigest实例
		try {
			md = MessageDigest.getInstance("SHA-1");	//得到一个新的MessageDigest实例，使用SHA1算法
			byte[] buffer = new byte[1024 * 10];		//新建一个10KB buffer[]，用于存储文件内容
			int length = 0;								//初始化读取的文件内容长度
			while ((length = fileInputStream.read(buffer)) >0) {
				md.update(buffer, 0, length);			//该对象通过使用 update()方法处理数据，每次更新
		    }
			return md.digest();							//对于给定数量的更新数据，digest()方法只能被调用一次。
														//在调用 digest()之后，MessageDigest对象被重新设置成其初始状态。
		} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} finally{
			fileInputStream.close();					//在计算SHA1值之后，关闭文件输入流
		}
		return null;
	}
	
	/**
	 * @function 计算文件的MD5值，以16进制表示
	 * @param filePath 文件路径
	 * @return byte[] 文件的MD5值
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public byte[] MD5(String filePath) throws FileNotFoundException, IOException {

		File file=new File(filePath);	//利用指定的路径构建一个新的文件
		FileInputStream fileInputStream = new FileInputStream(file);	//构建一个新的文件输入流
		MessageDigest md;	//声明一个MessageDigest实例
		try {
			md = MessageDigest.getInstance("MD5");		//得到一个新的MessageDigest实例，使用MD5算法
			byte[] buffer = new byte[1024 * 10];		//新建一个1KB的buffer[]，用于存储文件内容
			int length = 0;								//初始化读取的文件内容长度
			while ((length = fileInputStream.read(buffer)) >0) {
				md.update(buffer, 0, length);			//该对象通过使用 update()方法处理数据，每次更新
		    }
			return md.digest();							//对于给定数量的更新数据，digest()方法只能被调用一次。
														//在调用 digest()之后，MessageDigest对象被重新设置成其初始状态。
		} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} finally{
			fileInputStream.close();					//在计算SHA1值之后，关闭文件输入流
		}
		return null;
	}
	
	
	/**
	 * @function 适用于上G大的文件
	 * @param path
	 * @return
	 * @throws OutOfMemoryError
	 * @throws IOException
	 */
	/**
	public byte[] getFileSha1(String path) throws OutOfMemoryError,IOException {
		File file=new File(path);		//利用指定的路径构建一个新的文件
		FileInputStream in = new FileInputStream(file);	//构建一个新的文件输入流
		MessageDigest md;	//声明一个MessageDigest实例
		try {
			md = MessageDigest.getInstance("SHA-1");	//得到一个新的MessageDigest实例，使用MD5算法
		
			byte[] buffer = new byte[1024 * 1024 * 10];	//新建一个10MB的buffer[]，用于存储文件内容
			int len = 0;
			while ((len = in.read(buffer)) >0) {		//该对象通过使用 update()方法处理数据，每次更新
				//该对象通过使用 update()方法处理数据
				md.update(buffer, 0, len);
		    }
			return md.digest();						//对于给定数量的更新数据，digest()方法只能被调用一次。
													//在调用 digest()之后，MessageDigest对象被重新设置成其初始状态。
		} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} finally{
			in.close();								//在计算SHA1值之后，关闭文件输入流
		}
		return null;
	}
	*/
	
	/**
	 * @function 校验文件的摘要值
	 * @param b 摘要值数组
	 * @param filePath 待校验的文件的路径
	 * @param method 校验方法
	 * @return result 校验结果的真假
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public boolean isEqual(byte[] b, String filePath, String method) throws FileNotFoundException, IOException{
		boolean result =false;	//初始化结果为false
		if(method.equals("MD5")){	//校验方法为MD5
			if (MessageDigest.isEqual(b, MD5(filePath))) {
				result = true;
			} else {
				result = false;
			}
		}
		else if(method.equals("SHA-1")){	//校验方法为SHA-1
			if (MessageDigest.isEqual(b, SHA1(filePath))) {
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}
	
	/**将byte数组转换为16进制的字符串
	 * @function 将256进制的byte转换为16进制的字符串
	 * @param b byte数组
	 * @return str 16进制字符串
	 */
	public String byte2Str(byte[] b)
	{
		String str = "";	//用于存储最后结果
		String stmp = "";	//用于存储每个byte转换为16进制的结果
		for (int i = 0; i < b.length; i++) {
			stmp = (Integer.toHexString(b[i] & 0xff));	//先将byte转换为整数，然后将整数转换为16进制的串
			if (stmp.length() == 1)	//如果不足2位，前边补0，然后追加到结果中
				str = str + "0" + stmp;
			else					//将转换结果追加到结果中
				str = str + stmp;
		}
		return str;
	}
	
	
	/**
	 * @function 根据文件路径计算文件大小，单位为字节
	 * @param filePath 文件路径名
	 * @return size 文件大小，单位为字节
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public long FileSize(String filePath) throws FileNotFoundException,IOException{
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
		FileInputStream fin;					//文件输入流
		fin = new FileInputStream(filePath);	//新建一个文件输入流，用于读取文件
		int size;
		size = fin.available();				//返回文件的大小
		fin.close();						//关闭文件输入流 
		return size;
	}
		return 0;
}
	/**
	 * @function 根据文件路径计算文件的大小，单位为字节
	 * @param filePath 文件路径名
	 * @return 文件大小，单位为字节
	 */
	public long getSize(String filePath) {	
		File file = new File(filePath);		//根据路径新建一个文件
		if (file.exists() && file.isFile()) {	//如果文件存在并且不是目录
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
	 * fuction 根据文件路径计算文件的大小，单位为字节
	 * @param filePath 文件路径名
	 * @return size 文件大小
	 */
	public long fileSize(String filePath) {
		File file = new File(filePath);
		long size = file.length();			//获取文件的大小，单位为字节
		return size;
	}
}
