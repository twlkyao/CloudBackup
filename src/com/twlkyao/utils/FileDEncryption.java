/**
 * @function 文件加解密
 * @author 齐士垚
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
	 * @function 根据给定的密钥，得到真正的用于加解密的密钥
	 * @param arrBTmp 给定的密钥
	 * @param alg 给定的产生密钥的算法
	 * @return Key 真正的用于加解密的密钥
	 */
	public Key getKey(byte[] arrBTmp, String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
//			System.out.println("无该算法"+alg);
			Log.d("FileDEncryption", "无该算法" + alg);
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
			if(j>arrBTmp.length-1){	//密码长度不足的用0填充
				j=0;
			}
			arrB[i] = arrBTmp[j];
			i++;
			j++;
		}
		Key key = new SecretKeySpec(arrB, alg);	//生成一个密钥
		return key;
	}
	
	/**
	 * @function 根据给定的密钥和算法进行加密
	 * @param filePath1 待加密的文件的路径
	 * @param filePath2 加密后文件的路径
	 * @param strKey 加密密钥
	 * @param alg 加密算法
	 * @return 加密后的数据
	 */
	public void encrypt(String filePath1, String filePath2, String strKey, String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
//			System.out.println("alg type not find: "+alg);
			Log.d("FileDEncryption", "无该算法" + alg);
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
				System.out.println("读" + bytes.toString());
				byte[] out = c.doFinal(bytes);
				System.out.println("写" + out.toString());
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
	 * @function 根据给定的密钥好算法，对数据进行解密
	 * @param filePath 待解密的文件路径
	 * @param strKey 解密密钥
	 * @param alg 解密算法
	 * @return String 解密后的数据
	 */
	public void decrypt(String filePath1, String filePath2, String strKey, String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
//			System.out.println("alg type not find: "+alg);
			Log.d("FileDEncryption", "无该算法" + alg);
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
				System.out.println("读" + bytes.toString());
				byte[] out = c.doFinal(bytes);
				System.out.println("写" + out.toString());
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