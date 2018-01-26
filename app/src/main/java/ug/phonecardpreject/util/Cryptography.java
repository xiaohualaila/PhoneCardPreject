package ug.phonecardpreject.util;

import com.yuwei.utils.Hex;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 加密和解密的类;
 */
public class Cryptography {

    public static final byte[] UPWD = {
            (byte) 0x12, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x55, (byte) 0x98, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFD, (byte) 0xFF, (byte) 0xFF};
    /**
     * 写认证秘钥
     */

    public static final byte[] WPWD = {
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    /**
     * 读认证秘钥
     */
    public static final byte[] RPWD = {
            0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
            0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11};
    public static final String MODE = "DES/ECB/NoPadding";
//    public static final String MODE = "DES/CBC/PKCS5Padding";

    public static void main(String[] args) {

/*        byte[] content = Hex.hexStringToByteArray("1111111122222222");
        String password = "12548795623598524502652362695256";
        byte[] encrypted2 = encrypted(content, password);
        System.out.println("-----------------------------");
        byte[] c = Hex.hexStringToByteArray("F046C5250435F15B");
        byte[] decrypted = decrypted(c, password);


        System.out.println("：" + byteToHexString(encrypted2).equals("F046C5250435F15B"));
        System.out.println("：" + byteToHexString(decrypted).equals("1111111122222222"));*/
    }

    public static byte[] encrypted(byte[] content, byte[] password) {   //16进制的字符串MD5(明文+denghs)转化为数组,写的认证秘钥;
        return encrypted(content, Hex.toHexString(password));             //认证秘钥的字节数组转化为16进制的数组
    }

    public static byte[] encrypted(byte[] content, String password) {   //完整加密秘钥:12ffffffffffffff5598fffffffdffff;
        byte[] pwd1 = Hex.hexStringToByteArray(password.substring(0, password.length() / 2));//将完整秘钥的前面一部分12ffffffffffffff;
        //秘钥的前部分数组看看;
        System.out.println(" pwd1--------->> " + Hex.toHexString(pwd1));//pwd1为12ffffffffffffff;
        byte[] pwd2 = Hex.hexStringToByteArray(password.substring(password.length() / 2, password.length()));
        //
        System.out.println(" pwd2--------->> " + Hex.toHexString(pwd2));//5598fffffffdffff;
        byte[] encrypted = DES_CBC_Encrypt(content, pwd1);//f37242002b2504d610a747690531a0bc()(md5(明文+denghs)的字节数组用秘钥1进行加密);
        System.out.println(" encrypted--------->> " + Hex.toHexString(encrypted));
        byte[] decrypted = DES_CBC_Decrypt(encrypted, pwd2);//解密:用前半部分加密生成的明文再用后半部分的秘钥再进行解密生成:38a1786c8e7936683d3ed18ad62a36cd
        System.out.println(" decrypted--------->> " + Hex.toHexString(decrypted));
//        System.out.println("解密后：" + Hex.toHexString(decrypted));
        byte[] encrypted2 = DES_CBC_Encrypt(decrypted, pwd1);//加密得出密文;
        System.out.println(" encrypted2--------->> " + Hex.toHexString(encrypted2));
        System.out.println("加密后：" + Hex.toHexString(encrypted2));
        return encrypted2;//最终结果返回:009d2d80b6d60dd93f120240127ea855;
    }

    public static byte[] decrypted(byte[] content, byte[] password) {
        return decrypted(content, Hex.toHexString(password));
    }

    public static byte[] decrypted(String content, byte[] password) {
        return decrypted(Hex.hexStringToByteArray(content), Hex.toHexString(password));
    }

    public static byte[] decrypted(byte[] content, String password) {
        System.out.println("解密之前：" + Hex.toHexString(content));
        byte[] pwd1 = Hex.hexStringToByteArray(password.substring(0, password.length() / 2));
        byte[] pwd2 = Hex.hexStringToByteArray(password.substring(password.length() / 2, password.length()));
        byte[] decrypted = DES_CBC_Decrypt(content, pwd1);
        System.out.println(" decrypted--------->> " + Hex.toHexString(decrypted));
        byte[] encrypted = DES_CBC_Encrypt(decrypted, pwd2);
        System.out.println(" encrypted--------->> " + Hex.toHexString(encrypted));
        byte[] encrypted2 = DES_CBC_Decrypt(encrypted, pwd1);
        System.out.println(" encrypted2--------->> " + Hex.toHexString(encrypted2));
        System.out.println("解密后：" + Hex.toHexString(encrypted2));
        return encrypted2;
    }

    public static byte[] DES_CBC_Encrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
//			SecretKey key2=new SecretKeySpec(keyBytes, "DES");//SecretKeySpec类同时实现了Key和KeySpec接口
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    public static byte[] DES_CBC_Decrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.DECRYPT_MODE, key);
//			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static byte xor(byte[] value) {
        byte b = 14;
        for (int i = 0; i < value.length - 1; i++) {
            b ^= value[i];
        }
        return b;
    }

    public static boolean xorVerify(byte[] value) {
        byte v = value[value.length - 1];
        byte b = xor(value);
        return v == b;
    }
}