package com.twlkyao.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileZip {

	/**
	 * @function 压缩文件
	 * @param desFilePath
	 * @param inputFile
	 * @throws IOException
	 */
//	public void Zip(String desFilePath, File inputFile) throws IOException {
	public void Zip(String desFilePath, String srcFilePath) throws IOException {
		File inputFile = new File(srcFilePath);	//根据文件路径，新建一个文件
		ZipOutputStream zipOutputStream = new ZipOutputStream(new 
				FileOutputStream(desFilePath));	//新建ZipOutputStream对象
		Zip(zipOutputStream, inputFile, "");	//调用函数，压缩文件
		System.out.println("压缩中……");
		zipOutputStream.close();	//关闭流
	}
	
	/**
	 * 
	 * @param zipOutputStream
	 * @param file
	 * @param base
	 * @throws IOException
	 */
	public void Zip(ZipOutputStream zipOutputStream, File file, String base) throws IOException {	//方法重载
//	public void Zip(ZipOutputStream zipOutputStream, String srcFilePath, String base) throws IOException {	//方法重载
//		File file = new File(srcFilePath);
		if(file.isDirectory()) {						//判断路径名是否为文件目录
			File [] fileList = file.listFiles();		//获取路径数组
			zipOutputStream.putNextEntry(new ZipEntry(base + "/"));	//写入此目录的entry
			base = (0 == base.length()) ? "" : base + "/";	//判断参数是否为空
			for(int i = 0; i < fileList.length; i++) {	//循环遍历数组中的文件
				Zip(zipOutputStream, fileList[i], base + fileList[i]);	
			}
		} else {
			zipOutputStream.putNextEntry(new ZipEntry(base));	//创建新的进入点
			FileInputStream in = new FileInputStream(file);		//创建FileInputStream对象
			int b;	
			System.out.println(base);
			while((b= in.read()) != -1) {	//如果没有到达流的尾部
				zipOutputStream.write(b);	//将字节写入当前ZIP条目
			}
			in.close();	//关闭流
		}
	}
	
	/*public void Zip(String desFilePath, File file, String base) throws IOException {	//方法重载
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(desFilePath));	//新建ZipOutputStream对象
		if(file.isDirectory()) {						//判断路径名是否为文件目录
			File [] fileList = file.listFiles();		//获取路径数组
			zipOutputStream.putNextEntry(new ZipEntry(base + "/"));	//写入此目录的entry
			base = (0 == base.length()) ? "" : base + "/";	//判断参数是否为空
			for(int i = 0; i < fileList.length; i++) {	//循环遍历数组中的文件
				Zip(desFilePath, fileList[i], base + fileList[i]);	
			}
		} else {										//文件
			zipOutputStream.putNextEntry(new ZipEntry(base));	//创建新的进入点
			FileInputStream in = new FileInputStream(file);		//创建FileInputStream对象
			int b;	
			System.out.println(base);
			while((b= in.read()) != -1) {	//如果没有到达文件输入流的尾部
				zipOutputStream.write(b);	//将字节写入当前ZIP条目
			}
			in.close();	//关闭流
		}
		zipOutputStream.close();
	}*/
	
	/**
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void Unzip(String srcFilePath) throws IOException {	
		ZipInputStream zin;	//创建ZipInputStream对象
		zin = new ZipInputStream(new FileInputStream(srcFilePath));	//实例化对象，指明要解压的文件
		ZipEntry entry = zin.getNextEntry();						//获取下一个ZipEntry
		while((entry != null) && (!entry.isDirectory())) {	//如果entry不为空，并不在同一个目录下
			File file = new File(entry.getName());	//获取文件目录
			System.out.println(file);	
			if(!file.exists()) {							//如果该文件不存在
				file.mkdirs();								//创建文件所在文件夹
				file.createNewFile();						//创建文件
			}
			zin.closeEntry();								//关闭当前entry
			System.out.println(entry.getName() + "解压成功");
		}
		zin.close();										//关闭流
	} 
}
