/**
 * 作者：		网络
 * 修改者：	齐士垚
 * 日期：		2013.6.6
 * 功能：		实现文件的压缩和解压缩，加密的压缩和解压缩
 */
package com.twlkyao.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//import org.apache.commons.lang3.StringUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class FileZ4J {
	
	/**
	 * @function 使用给定密码压缩指定文件或文件夹到指定位置
	 * @param srcFilePath 待压缩文件路径或者文件夹路径
	 * @param destFilePath 压缩文件存储路径
	 * @param isCreateDir 是否在压缩文件中创建目录，仅在压缩文件为目录时有效，如果为假，则直接压缩目录下文件到压缩文件
	 * @param passwd 压缩使用的密码
	 * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败
	 */
    public String zip(String srcFilePath, String destFilePath, boolean isCreateDir, String passwd) {  
        File srcFile = new File(srcFilePath);		//根据文件路径，新建文件实例
        destFilePath = buildDestinationZipFilePath(srcFile, destFilePath);	//获得目标违建的路径
        ZipParameters zipParameters = new ZipParameters();  
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           //压缩方式  
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);    //压缩级别  
//        if (!StringUtils.isEmpty(passwd)) {
        if(!passwd.isEmpty()) {	//如果密码非空
            zipParameters.setEncryptFiles(true);	//设置加密参数为真
            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); //使用标准加密方式
            zipParameters.setPassword(passwd.toCharArray());						//设置加密密码
        }  
        try {  
            ZipFile zipFile = new ZipFile(destFilePath);	//以目标文件为路径创建一个新的ZipFile实例
            if(srcFile.isDirectory()) {						//如果原文件路径是目录
                //如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构  
                if(!isCreateDir) {							//如果不创建目录，则直接将原文件路径下的文件压缩到文件
                    File [] subFiles = srcFile.listFiles();	//返回原文件目录下的文件数组
                    ArrayList<File> temp = new ArrayList<File>();  
                    Collections.addAll(temp, subFiles);		//将subFiles添加到temp中
                    zipFile.addFiles(temp, zipParameters);	//将输入文件添加到zipFile中
                    return destFilePath;					//返回压缩文件路径
                }
                zipFile.addFolder(srcFile, zipParameters);	//将原文件目录加入到zipFile中
            } else {  
                zipFile.addFile(srcFile, zipParameters);  
            }  
            return destFilePath;  
        } catch (ZipException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
	
    /** 
     * @function 构建压缩文件存放路径,如果不存在将会创建 
     * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径 
     * @param srcFilePath 源文件 
     * @param destFilePath 压缩目标路径 
     * @return 正确的压缩文件存放路径 
     */  
    private static String buildDestinationZipFilePath(File srcFilePath, String destFilePath) {  
    	if (destFilePath.isEmpty()) {	//如果传入的压缩文件路径是空的
            if (srcFilePath.isDirectory()) {	//如果原文件路径是目录，则以原文件的路径新建压缩文件
                destFilePath = srcFilePath.getParent() + File.separator + srcFilePath.getName() + ".zip";  
            } else {							//如果原文件路径不是目录，则以原文件的名字新建压缩文件
                String fileName = srcFilePath.getName().substring(0, srcFilePath.getName().lastIndexOf("."));  
                destFilePath = srcFilePath.getParent() + File.separator + fileName + ".zip";  
            }  
        } else {						//如果传入的压缩文件路径非空
            createDestDirectoryIfNecessary(destFilePath);  	// 在指定路径不存在的情况下将其创建出来  
            if (destFilePath.endsWith(File.separator)) {	//指定目标路径是目录
                String fileName = "";  
                if (srcFilePath.isDirectory()) {			//如果原文件名是目录
                    fileName = srcFilePath.getName();  		//文件名是原文件名
                } else {									//原文件名是文件
                    fileName = srcFilePath.getName().substring(0, srcFilePath.getName().lastIndexOf("."));	//文件名是原文件名出去最后的后缀名
                }  
                destFilePath += fileName + ".zip";			//目标文件名是文件名加上zip后缀
            }  
        }  
        return destFilePath;	//返回目标文件名
    }

	
	/** 
     * @function 使用给定密码解压指定的ZIP压缩文件到指定目录 
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出 
     * @param zipFilePath 指定的ZIP压缩文件路径 
     * @param destFilePath 解压到的目录 
     * @param passwd ZIP文件的密码 
     * @return 解压后文件数组 
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出 
     */  
//    public File [] unzip(File zipFile, String dest, String passwd) throws ZipException {  
    public File [] unzip(String zipFilePath, String destFilePath, String passwd) throws ZipException {
        ZipFile zFile = new ZipFile(zipFilePath);	//按照指定的Zip压缩文件路径构建压缩文件
        zFile.setFileNameCharset("UTF-8");		//用给定的字符集编码文件名
        if (!zFile.isValidZipFile()) {			//验证Zip文件是否是有效文件
            throw new ZipException("压缩文件不合法,可能被损坏.");	//如果不合法抛出异常
        }
        File destDir = new File(destFilePath);	//用目标文件目录构建新的文件
        if (destDir.isDirectory() && !destDir.exists()) {	//如果文件是目录并且不存在
            destDir.mkdir();					//新建目录
        }
        if (zFile.isEncrypted()) {				//如果文件加密
            zFile.setPassword(passwd.toCharArray());	//设置压缩文件的解密密码
        }
        zFile.extractAll(destFilePath);			//抽取所有的文件到目标目录
        List<FileHeader> headerList = zFile.getFileHeaders();	//返回Zip文件中的文件头，存在
        List<File> extractedFileList = new ArrayList<File>();	//新建一个List，用于存储解压后的文件
        for(FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {	//如果文件头是目录
                extractedFileList.add(new File(destDir,fileHeader.getFileName()));	//将解压的文件添加到文件中
            }  
        }  
        File [] extractedFiles = new File[extractedFileList.size()];	//新建一个File数组，用于存储解压出的文件
        extractedFileList.toArray(extractedFiles);						//将List存到File数组中
        return extractedFiles;											//返回解压后的文件数组
    }

    /** 
     * 使用给定密码解压指定的ZIP压缩文件到当前目录 
     * @param zipFilePath 指定的ZIP压缩文件路径 
     * @param passwd ZIP文件的密码 
     * @return 解压后文件数组 
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出 
     */  
    public  File [] unzip(String zipFilePath, String passwd) throws ZipException {  
        File zipFile = new File(zipFilePath);		//根据指定的压缩文件路径，构建文件实例
        File parentDir = zipFile.getParentFile();	//得到压缩文件的父目录
        return unzip(zipFilePath, parentDir.getAbsolutePath(), passwd);	//解压压缩文件
    }  

    /** 
     * @function 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建 
     * @param destFilePath 指定的存放路径,有可能该路径并没有被创建 
     */  
    private static void createDestDirectoryIfNecessary(String destFilePath) {  
        File destDir = null;  
        if (destFilePath.endsWith(File.separator)) {	//如果路径是目录，在Linux上是"/"，在Windows上是"\\"
            destDir = new File(destFilePath); 			//创建新的文件
        } else { 										//如果路径是文件名
            destDir = new File(destFilePath.substring(0, destFilePath.lastIndexOf(File.separator)));  
        }  
        if (!destDir.exists()) {						//如果目录不存在
        	destDir.mkdirs();							//新建目录
        }
    }
}
