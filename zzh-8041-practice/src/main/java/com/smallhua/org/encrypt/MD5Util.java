package com.smallhua.org.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class MD5Util{
    protected static char hexDigits[] = {
            '0','1','2','3','4','5','6','7','8',
            '9','a','b','c','d','e','f'
    };

    protected static MessageDigest messageDigest = null;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(MD5Util.class.getName() + "初始化失败");
            e.printStackTrace();
        }
    }

    public static String getMD5String(String s){
        byte[] bytes = s.getBytes();
        messageDigest.update(bytes);

        return byteToHex(messageDigest.digest());
    }

    public static String getMD5String(File file) throws Exception {
        FileInputStream fi = new FileInputStream(file);
        byte[] buff = new byte[1024];
        int numRead;
        while((numRead = fi.read(buff)) > 0){
            messageDigest.update(buff, 0, numRead);
        }
        fi.close();

        return byteToHex(messageDigest.digest());
    }

    private static String byteToHex(byte[] digest) {
        int len = digest.length;
        StringBuilder sb = new StringBuilder(2*len);
        for (int i = 0; i < len; i++) {
            byte b = digest[i];
            sb.append(hexDigits[(b & 0xf0) >>> 4]);
            sb.append(hexDigits[(b & 0xf)]);
        }

        return sb.toString();
    }
}