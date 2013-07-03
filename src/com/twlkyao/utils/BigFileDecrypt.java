package com.twlkyao.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class BigFileDecrypt {
	String CIPHER_DES = "DES";
	String CIPHER_DES_CBC = "CBC";
	
	/**
	 * @function ʹ����Կkey�����ļ�
	 * @param filePath1 ԭ�ļ�·��
	 * @param filePath2 �����ļ�·��
	 * @param key ������Կ
	 * @return 
	 */
    public static boolean enCryptoFile(String filePath1 ,String filePath2, String key)
    {
            //keyתΪ��Ϊbytes
            byte[] newkey = new byte[8];
            newkey = key.getBytes();
            //�����ļ�ת����Ϊbytes
            File file_in = new File(filePath1);
            //��������ļ�
            File file_out = new File(filePath2);
            FileOutputStream out = null;
            try {
            	 byte[] filebyte = getBytesFromFile(file_in);
                    out = new FileOutputStream(file_out);
                    
                    filebyte = CryptoUtils.encrypt(filebyte, newkey);
                    out.write(filebyte,0,filebyte.length);
                    //�д��󣬰�������
                    
                    out.close();
                    return true;
            } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    System.out.println("File enCrypto Error!");
            }
            return false;
    }
    
    /**
	 * @function ʹ����Կkey�����ļ�
	 * @param filePath1 ԭ�ļ�·��
	 * @param filePath2 �����ļ�·��
	 * @param key ������Կ
	 * @return 
	 */
    public static boolean deCryptoFile(String filePath1 ,String filePath2, String key)
    {
            //keyתΪ��Ϊbytes
            byte[] newkey = new byte[8];
            newkey = key.getBytes();
            //�����ļ�ת����Ϊbytes
            File file_in = new File(filePath1);
            byte[] filebyte;
			
			
			
            //��������ļ�
            File file_out = new File(filePath2);
            FileOutputStream out = null;
            try {
            		filebyte = getBytesFromFile(file_in);
                    out = new FileOutputStream(file_out);
                    
                    filebyte = CryptoUtils.decrypt(filebyte, newkey);
                    
                    //�д��󣬰�������
                   
                    out.write(filebyte,0,filebyte.length);
                   
                    out.close();
                    return true;
            }catch(Exception e)
            {
                    e.printStackTrace();
                    System.out.println("File deCrypto Error!");
                    return false;
            }
    }

    /**
	 * @function ʹ����Կkey�����ļ�
	 * @param filePath1 ԭ�ļ�·��
	 * @param filePath2 �����ļ�·��
	 * @param key ������Կ
	 * @return 
	 */
    public boolean enCrByteFile(String filePath1 ,String filePath2, String key)
    {        
//            Date date = new Date(); //����ʼ ʱ��
//            String dateStr1 = new Timestamp(date.getTime()).toString();
//            System.out.println("���ܿ�ʼtime=" + dateStr1);

            //keyתΪ��Ϊbytes
            byte[] newkey = new byte[8];
            newkey = key.getBytes();
            
            // ��ʼ��һ������des��ʼ����iv����, ���μ��ܱ����Ҫ��
            IvParameterSpec iv = new IvParameterSpec(new byte[] { 0x12, 0x34, 0x56,
                    0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF });
            
            DESKeySpec dks = null;
            try {
                    dks = new DESKeySpec(newkey);
            } catch (InvalidKeyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }

            SecretKeyFactory keyFactory = null;
            try {
            	keyFactory = SecretKeyFactory.getInstance(CIPHER_DES);
            } catch (NoSuchAlgorithmException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }
            SecretKey secureKey = null;
            try {
                    secureKey = keyFactory.generateSecret(dks);
            } catch (InvalidKeySpecException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }

            Cipher cipher = null;
            try {
                    cipher = Cipher.getInstance(CIPHER_DES_CBC);
            } catch (NoSuchAlgorithmException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            } catch (NoSuchPaddingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }

            try {
                    cipher.init(Cipher.ENCRYPT_MODE, secureKey, iv);
            } catch (InvalidKeyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            } catch (InvalidAlgorithmParameterException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }

            //return cipher.doFinal(src);
    

            try{
            File file_in = new File(filePath1);
            File file_out = new File(filePath2);
            FileInputStream in = new FileInputStream(file_in);
            FileOutputStream out =new FileOutputStream(file_out);
            byte[] bytes = new byte[1024];
            //String end = "<!~!>";
            //System.out.print(end);
            //byte [] bend = end.getBytes();
            while((in.read(bytes))!=-1)
            {
                    //byte[] bout = CryptoUtils.encrypt(bytes, newkey);
                    byte[] bout = cipher.doFinal(bytes);//
                    //System.out.println(bytes.length);
                    //System.out.println(bout.length);
                    out.write(bout,0,1032);
                    //out.write(bend,0,bend.length);
            }
            in.close();
            out.close();
            return true;
            }catch(Exception e)
            {
                    e.printStackTrace();
                    System.out.println("File enCrypto Error!");
                    return false;
            }
    }
    

    /**
	 * @function ʹ����Կkey�����ļ�
	 * @param filePath1 ԭ�ļ�·��
	 * @param filePath2 �����ļ�·��
	 * @param key ������Կ
	 * @return 
	 */
    public boolean deCrByteFile(String filePath1 ,String filePath2, String key)
    {
            //keyתΪ��Ϊbytes
            byte[] newkey = new byte[8];
            newkey = key.getBytes();
            
            
		    // ��ʼ��һ������des��ʼ����iv����, ���μ��ܱ����Ҫ��
		    IvParameterSpec iv = new IvParameterSpec(new byte[] { 0x12, 0x34, 0x56,
		                    0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF });
		            
		    DESKeySpec dks = null;
		            try {
		                    dks = new DESKeySpec(newkey);
		            } catch (InvalidKeyException e1) {
		                    // TODO Auto-generated catch block
		                    e1.printStackTrace();
		            }
		            SecretKeyFactory keyFactory = null;
		            try {
		            	keyFactory = SecretKeyFactory.getInstance(CIPHER_DES);
		            } catch (NoSuchAlgorithmException e1) {
		            	// TODO Auto-generated catch block
		            	e1.printStackTrace();
            }
		            SecretKey secureKey = null;
		            try {
		            	secureKey = keyFactory.generateSecret(dks);
		            } catch (InvalidKeySpecException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }

		            Cipher cipher = null;
		            try {
                    cipher = Cipher.getInstance(CIPHER_DES_CBC);
            } catch (NoSuchAlgorithmException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            } catch (NoSuchPaddingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }
		            try {
                    cipher.init(Cipher.ENCRYPT_MODE, secureKey, iv);
            } catch (InvalidKeyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            } catch (InvalidAlgorithmParameterException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }
            
            try{
            File file_in = new File(filePath1);
            File file_out = new File(filePath2);
            FileInputStream in = new FileInputStream(file_in);
            FileOutputStream out =new FileOutputStream(file_out);
            byte[] bytes = new byte[1032];
            //String end = "<!~!>";
            //System.out.print(end);
            //byte [] bend = end.getBytes();
            while((in.read(bytes))!=-1)
            {
            	
                    byte[] bout = CryptoUtils.decrypt(bytes, newkey);
                   
                  
            	out.write(bout,0,bout.length);
            	
                    //out.write(bend,0,bend.length);
            }
            in.close();
            out.close();
            return true;
            }catch(Exception e)
            {
                    e.printStackTrace();
                    System.out.println("File deCrypto Error!");
                    return false;
            }
    }
    
    /**
     * @function ����ļ������ݣ���byte�����ʾ
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
	public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        
        // Get the size of the file
        long length = file.length();
        
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
                // File is too large
        }
        
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
        
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                   && (numRead = is.read(bytes, offset, Math.min(bytes.length - offset, 512*1024))) >= 0) {
                offset += numRead;
        }
        
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
                throw new IOException("Could not completely read file "+file.getName());
        }
        
        // Close the input stream and return bytes
        is.close();
        return bytes;
}
}
