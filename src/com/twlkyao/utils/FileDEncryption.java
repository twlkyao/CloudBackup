/**
 * @function �ļ��ӽ���
 * @author ��ʿ��
 * @time 2013.5.20
 */
package com.twlkyao.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class FileDEncryption {
	/**
	 * @function ���ݸ�������Կ���õ����������ڼӽ��ܵ���Կ
	 * @param arrBTmp ��������Կ
	 * @param alg �����Ĳ�����Կ���㷨
	 * @return Key ���������ڼӽ��ܵ���Կ
	 */
	public Key getKey(byte[] arrBTmp, String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
//			System.out.println("�޸��㷨"+alg);
			Log.d("FileDEncryption", "�޸��㷨" + alg);
			return null;
		}
		byte[] arrB;
		if(alg.equals("DES")){
			arrB = new byte[8];
		}
		else if(alg.equals("DESede")){
			arrB = new byte[24];
		}
		else{
			arrB = new byte[16];
		}
		int i=0;
		int j=0;
		while(i < arrB.length){
			if(j>arrBTmp.length-1){	//���볤�Ȳ������0���
				j=0;
			}
			arrB[i] = arrBTmp[j];
			i++;
			j++;
		}
		Key key = new SecretKeySpec(arrB, alg);	//����һ����Կ
		return key;
	}
	
	/**
	 * @function ���ݸ�������Կ���㷨���м���
	 * @param filePath1 �����ܵ��ļ���·��
	 * @param filePath2 ���ܺ��ļ���·��
	 * @param strKey ������Կ
	 * @param alg �����㷨
	 * @return ���ܺ������
	 */
	public void encrypt(String filePath1, String filePath2, String strKey, String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
//			System.out.println("alg type not find: "+alg);
			Log.d("FileDEncryption", "�޸��㷨" + alg);
			return;
		}
		File file_in = new File(filePath1);
		File file_out = new File(filePath2);
		FileInputStream fin;
		FileOutputStream fout;
		byte[] bytes = new byte[1024];
		Cipher c;
		Key key = getKey(strKey.getBytes(),alg);
		try {
			fin = new FileInputStream(file_in);
			fout = new FileOutputStream(file_out);
			c = Cipher.getInstance(alg);
			c.init(Cipher.ENCRYPT_MODE, key);
			while((fin.read(bytes))!=-1) {
				System.out.println("��" + bytes.toString());
				byte[] out = c.doFinal(bytes);
				System.out.println("д" + out.toString());
				fout.write(out);	
			}
			fin.close();
			fout.close();
		
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @function ���ݸ�������Կ���㷨�������ݽ��н���
	 * @param filePath �����ܵ��ļ�·��
	 * @param strKey ������Կ
	 * @param alg �����㷨
	 * @return String ���ܺ������
	 */
	public void decrypt(String filePath1, String filePath2, String strKey, String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
//			System.out.println("alg type not find: "+alg);
			Log.d("FileDEncryption", "�޸��㷨" + alg);
			return;
		}
		File file_in = new File(filePath1);
		File file_out = new File(filePath2);
		FileInputStream fin;
		FileOutputStream fout;
		byte[] bytes = new byte[1024];
		Cipher c;
		Key key = getKey(strKey.getBytes(),alg);
		try {
			fin = new FileInputStream(file_in);
			fout = new FileOutputStream(file_out);
			c = Cipher.getInstance(alg);
			c.init(Cipher.DECRYPT_MODE, key);
			while((fin.read(bytes))!=-1) {
				System.out.println("��" + bytes.toString());
				byte[] out = c.doFinal(bytes);
				System.out.println("д" + out.toString());
				fout.write(out);	
			}
			fin.close();
			fout.close();
		
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}