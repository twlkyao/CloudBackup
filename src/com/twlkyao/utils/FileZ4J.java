/**
 * ���ߣ�		����
 * �޸��ߣ�	��ʿ��
 * ���ڣ�		2013.6.6
 * ���ܣ�		ʵ���ļ���ѹ���ͽ�ѹ�������ܵ�ѹ���ͽ�ѹ��
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
	 * @function ʹ�ø�������ѹ��ָ���ļ����ļ��е�ָ��λ��
	 * @param srcFilePath ��ѹ���ļ�·�������ļ���·��
	 * @param destFilePath ѹ���ļ��洢·��
	 * @param isCreateDir �Ƿ���ѹ���ļ��д���Ŀ¼������ѹ���ļ�ΪĿ¼ʱ��Ч�����Ϊ�٣���ֱ��ѹ��Ŀ¼���ļ���ѹ���ļ�
	 * @param passwd ѹ��ʹ�õ�����
	 * @return ���յ�ѹ���ļ���ŵľ���·��,���Ϊnull��˵��ѹ��ʧ��
	 */
    public String zip(String srcFilePath, String destFilePath, boolean isCreateDir, String passwd) {  
        File srcFile = new File(srcFilePath);		//�����ļ�·�����½��ļ�ʵ��
        destFilePath = buildDestinationZipFilePath(srcFile, destFilePath);	//���Ŀ��Υ����·��
        ZipParameters zipParameters = new ZipParameters();  
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           //ѹ����ʽ  
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);    //ѹ������  
//        if (!StringUtils.isEmpty(passwd)) {
        if(!passwd.isEmpty()) {	//�������ǿ�
            zipParameters.setEncryptFiles(true);	//���ü��ܲ���Ϊ��
            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); //ʹ�ñ�׼���ܷ�ʽ
            zipParameters.setPassword(passwd.toCharArray());						//���ü�������
        }  
        try {  
            ZipFile zipFile = new ZipFile(destFilePath);	//��Ŀ���ļ�Ϊ·������һ���µ�ZipFileʵ��
            if(srcFile.isDirectory()) {						//���ԭ�ļ�·����Ŀ¼
                //���������Ŀ¼�Ļ�,��ֱ�ӰѸ���Ŀ¼�µ��ļ�ѹ����ѹ���ļ�,��û��Ŀ¼�ṹ  
                if(!isCreateDir) {							//���������Ŀ¼����ֱ�ӽ�ԭ�ļ�·���µ��ļ�ѹ�����ļ�
                    File [] subFiles = srcFile.listFiles();	//����ԭ�ļ�Ŀ¼�µ��ļ�����
                    ArrayList<File> temp = new ArrayList<File>();  
                    Collections.addAll(temp, subFiles);		//��subFiles��ӵ�temp��
                    zipFile.addFiles(temp, zipParameters);	//�������ļ���ӵ�zipFile��
                    return destFilePath;					//����ѹ���ļ�·��
                }
                zipFile.addFolder(srcFile, zipParameters);	//��ԭ�ļ�Ŀ¼���뵽zipFile��
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
     * @function ����ѹ���ļ����·��,��������ڽ��ᴴ�� 
     * ����Ŀ������ļ�������Ŀ¼,Ҳ���ܲ���,�˷�������ת������ѹ���ļ��Ĵ��·�� 
     * @param srcFilePath Դ�ļ� 
     * @param destFilePath ѹ��Ŀ��·�� 
     * @return ��ȷ��ѹ���ļ����·�� 
     */  
    private static String buildDestinationZipFilePath(File srcFilePath, String destFilePath) {  
    	if (destFilePath.isEmpty()) {	//��������ѹ���ļ�·���ǿյ�
            if (srcFilePath.isDirectory()) {	//���ԭ�ļ�·����Ŀ¼������ԭ�ļ���·���½�ѹ���ļ�
                destFilePath = srcFilePath.getParent() + File.separator + srcFilePath.getName() + ".zip";  
            } else {							//���ԭ�ļ�·������Ŀ¼������ԭ�ļ��������½�ѹ���ļ�
                String fileName = srcFilePath.getName().substring(0, srcFilePath.getName().lastIndexOf("."));  
                destFilePath = srcFilePath.getParent() + File.separator + fileName + ".zip";  
            }  
        } else {						//��������ѹ���ļ�·���ǿ�
            createDestDirectoryIfNecessary(destFilePath);  	// ��ָ��·�������ڵ�����½��䴴������  
            if (destFilePath.endsWith(File.separator)) {	//ָ��Ŀ��·����Ŀ¼
                String fileName = "";  
                if (srcFilePath.isDirectory()) {			//���ԭ�ļ�����Ŀ¼
                    fileName = srcFilePath.getName();  		//�ļ�����ԭ�ļ���
                } else {									//ԭ�ļ������ļ�
                    fileName = srcFilePath.getName().substring(0, srcFilePath.getName().lastIndexOf("."));	//�ļ�����ԭ�ļ�����ȥ���ĺ�׺��
                }  
                destFilePath += fileName + ".zip";			//Ŀ���ļ������ļ�������zip��׺
            }  
        }  
        return destFilePath;	//����Ŀ���ļ���
    }

	
	/** 
     * @function ʹ�ø��������ѹָ����ZIPѹ���ļ���ָ��Ŀ¼ 
     * ���ָ��Ŀ¼������,�����Զ�����,���Ϸ���·���������쳣���׳� 
     * @param zipFilePath ָ����ZIPѹ���ļ�·�� 
     * @param destFilePath ��ѹ����Ŀ¼ 
     * @param passwd ZIP�ļ������� 
     * @return ��ѹ���ļ����� 
     * @throws ZipException ѹ���ļ����𻵻��߽�ѹ��ʧ���׳� 
     */  
//    public File [] unzip(File zipFile, String dest, String passwd) throws ZipException {  
    public File [] unzip(String zipFilePath, String destFilePath, String passwd) throws ZipException {
        ZipFile zFile = new ZipFile(zipFilePath);	//����ָ����Zipѹ���ļ�·������ѹ���ļ�
        zFile.setFileNameCharset("UTF-8");		//�ø������ַ��������ļ���
        if (!zFile.isValidZipFile()) {			//��֤Zip�ļ��Ƿ�����Ч�ļ�
            throw new ZipException("ѹ���ļ����Ϸ�,���ܱ���.");	//������Ϸ��׳��쳣
        }
        File destDir = new File(destFilePath);	//��Ŀ���ļ�Ŀ¼�����µ��ļ�
        if (destDir.isDirectory() && !destDir.exists()) {	//����ļ���Ŀ¼���Ҳ�����
            destDir.mkdir();					//�½�Ŀ¼
        }
        if (zFile.isEncrypted()) {				//����ļ�����
            zFile.setPassword(passwd.toCharArray());	//����ѹ���ļ��Ľ�������
        }
        zFile.extractAll(destFilePath);			//��ȡ���е��ļ���Ŀ��Ŀ¼
        List<FileHeader> headerList = zFile.getFileHeaders();	//����Zip�ļ��е��ļ�ͷ������
        List<File> extractedFileList = new ArrayList<File>();	//�½�һ��List�����ڴ洢��ѹ����ļ�
        for(FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {	//����ļ�ͷ��Ŀ¼
                extractedFileList.add(new File(destDir,fileHeader.getFileName()));	//����ѹ���ļ���ӵ��ļ���
            }  
        }  
        File [] extractedFiles = new File[extractedFileList.size()];	//�½�һ��File���飬���ڴ洢��ѹ�����ļ�
        extractedFileList.toArray(extractedFiles);						//��List�浽File������
        return extractedFiles;											//���ؽ�ѹ����ļ�����
    }

    /** 
     * ʹ�ø��������ѹָ����ZIPѹ���ļ�����ǰĿ¼ 
     * @param zipFilePath ָ����ZIPѹ���ļ�·�� 
     * @param passwd ZIP�ļ������� 
     * @return ��ѹ���ļ����� 
     * @throws ZipException ѹ���ļ����𻵻��߽�ѹ��ʧ���׳� 
     */  
    public  File [] unzip(String zipFilePath, String passwd) throws ZipException {  
        File zipFile = new File(zipFilePath);		//����ָ����ѹ���ļ�·���������ļ�ʵ��
        File parentDir = zipFile.getParentFile();	//�õ�ѹ���ļ��ĸ�Ŀ¼
        return unzip(zipFilePath, parentDir.getAbsolutePath(), passwd);	//��ѹѹ���ļ�
    }  

    /** 
     * @function �ڱ�Ҫ������´���ѹ���ļ����Ŀ¼,����ָ���Ĵ��·����û�б����� 
     * @param destFilePath ָ���Ĵ��·��,�п��ܸ�·����û�б����� 
     */  
    private static void createDestDirectoryIfNecessary(String destFilePath) {  
        File destDir = null;  
        if (destFilePath.endsWith(File.separator)) {	//���·����Ŀ¼����Linux����"/"����Windows����"\\"
            destDir = new File(destFilePath); 			//�����µ��ļ�
        } else { 										//���·�����ļ���
            destDir = new File(destFilePath.substring(0, destFilePath.lastIndexOf(File.separator)));  
        }  
        if (!destDir.exists()) {						//���Ŀ¼������
        	destDir.mkdirs();							//�½�Ŀ¼
        }
    }
}
