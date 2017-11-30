package com.witlife.mobileguard.utils;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }

    public static String getFileMd5(String path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(path);

            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }

            byte[] result = digest.digest();

            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int i = b & 0xff;// 将字节转为整数
                String hexString = Integer.toHexString(i);// 将整数转为16进制

                if (hexString.length() == 1) {
                    hexString = "0" + hexString;// 如果长度等于1, 加0补位
                }

                sb.append(hexString);
            }

            //System.out.println(sb.toString());// 打印得到的md5
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
