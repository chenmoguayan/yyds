package org.hj.yygh.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author hj
 * @data 2023/3/27 17:25
 */
public class ImageBase64Util {

    public static void main(String[] args) {
        String imageFile= "D:\\yygh_work\\xh.png";// 待处理的图片
        System.out.println(getImageString(imageFile));
    }
    public static String getImageString(String imageFile){
        InputStream is = null;
        try {
            byte[] data = null;
            is = new FileInputStream(new File(imageFile));
            data = new byte[is.available()];
            is.read(data);
            return new String(Base64.encodeBase64(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
