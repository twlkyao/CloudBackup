package com.twlkyao.utils;

import javax.crypto.*;
import javax.crypto.spec.*;


public class CryptoUtils {
        
        public static byte[] encrypt(byte[] key, byte[] msg) {
                try {
                        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
                        return cipher.doFinal(msg);
                } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                }
        }
        
        public static byte[] decrypt(byte[] key, byte[] encrypted) {
                try {
                        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
                        return cipher.doFinal(encrypted);
                } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                }
        }
        
        /**
         * Decripta a capability do UFID e verifica as permissoes
         * @param ufid UFID a ser verificado
         * @return se as permissoes conferem
         */
       /* public static boolean permissionVerify(UFID ufid) {
        byte[] decrypted = CryptoUtils.decrypt("0123456789123456", ufid.getCapability());
        
                byte operations = ufid.getOperations();
                byte opCapa = decrypted[decrypted.length -1];
                for (int i = 0; i < 5; i++) {
                        if (BitUtils.isBitSet(opCapa, i) != BitUtils.isBitSet(operations, i)){
                                return false;
                        }
                }
                return true;    
        }*/

}