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
	 * @function ѹ���ļ�
	 * @param desFilePath
	 * @param inputFile
	 * @throws IOException
	 */
//	public void Zip(String desFilePath, File inputFile) throws IOException {
	public void Zip(String desFilePath, String srcFilePath) throws IOException {
		File inputFile = new File(srcFilePath);	//�����ļ�·�����½�һ���ļ�
		ZipOutputStream zipOutputStream = new ZipOutputStream(new 
				FileOutputStream(desFilePath));	//�½�ZipOutputStream����
		Zip(zipOutputStream, inputFile, "");	//���ú�����ѹ���ļ�
		System.out.println("ѹ���С���");
		zipOutputStream.close();	//�ر���
	}
	
	/**
	 * 
	 * @param zipOutputStream
	 * @param file
	 * @param base
	 * @throws IOException
	 */
	public void Zip(ZipOutputStream zipOutputStream, File file, String base) throws IOException {	//��������
//	public void Zip(ZipOutputStream zipOutputStream, String srcFilePath, String base) throws IOException {	//��������
//		File file = new File(srcFilePath);
		if(file.isDirectory()) {						//�ж�·�����Ƿ�Ϊ�ļ�Ŀ¼
			File [] fileList = file.listFiles();		//��ȡ·������
			zipOutputStream.putNextEntry(new ZipEntry(base + "/"));	//д���Ŀ¼��entry
			base = (0 == base.length()) ? "" : base + "/";	//�жϲ����Ƿ�Ϊ��
			for(int i = 0; i < fileList.length; i++) {	//ѭ�����������е��ļ�
				Zip(zipOutputStream, fileList[i], base + fileList[i]);	
			}
		} else {
			zipOutputStream.putNextEntry(new ZipEntry(base));	//�����µĽ����
			FileInputStream in = new FileInputStream(file);		//����FileInputStream����
			int b;	
			System.out.println(base);
			while((b= in.read()) != -1) {	//���û�е�������β��
				zipOutputStream.write(b);	//���ֽ�д�뵱ǰZIP��Ŀ
			}
			in.close();	//�ر���
		}
	}
	
	/*public void Zip(String desFilePath, File file, String base) throws IOException {	//��������
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(desFilePath));	//�½�ZipOutputStream����
		if(file.isDirectory()) {						//�ж�·�����Ƿ�Ϊ�ļ�Ŀ¼
			File [] fileList = file.listFiles();		//��ȡ·������
			zipOutputStream.putNextEntry(new ZipEntry(base + "/"));	//д���Ŀ¼��entry
			base = (0 == base.length()) ? "" : base + "/";	//�жϲ����Ƿ�Ϊ��
			for(int i = 0; i < fileList.length; i++) {	//ѭ�����������е��ļ�
				Zip(desFilePath, fileList[i], base + fileList[i]);	
			}
		} else {										//�ļ�
			zipOutputStream.putNextEntry(new ZipEntry(base));	//�����µĽ����
			FileInputStream in = new FileInputStream(file);		//����FileInputStream����
			int b;	
			System.out.println(base);
			while((b= in.read()) != -1) {	//���û�е����ļ���������β��
				zipOutputStream.write(b);	//���ֽ�д�뵱ǰZIP��Ŀ
			}
			in.close();	//�ر���
		}
		zipOutputStream.close();
	}*/
	
	/**
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void Unzip(String srcFilePath) throws IOException {	
		ZipInputStream zin;	//����ZipInputStream����
		zin = new ZipInputStream(new FileInputStream(srcFilePath));	//ʵ��������ָ��Ҫ��ѹ���ļ�
		ZipEntry entry = zin.getNextEntry();						//��ȡ��һ��ZipEntry
		while((entry != null) && (!entry.isDirectory())) {	//���entry��Ϊ�գ�������ͬһ��Ŀ¼��
			File file = new File(entry.getName());	//��ȡ�ļ�Ŀ¼
			System.out.println(file);	
			if(!file.exists()) {							//������ļ�������
				file.mkdirs();								//�����ļ������ļ���
				file.createNewFile();						//�����ļ�
			}
			zin.closeEntry();								//�رյ�ǰentry
			System.out.println(entry.getName() + "��ѹ�ɹ�");
		}
		zin.close();										//�ر���
	} 
}
