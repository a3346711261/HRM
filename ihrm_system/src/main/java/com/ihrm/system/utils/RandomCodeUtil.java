package com.ihrm.system.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 随机数工具类(随机生成数字、字母、数字字母组合、中文姓名)
 */
@Component
public class RandomCodeUtil {

    /**
     * 生成相应长度的数字字母组合的随机数
     * @return String
     */
    public static String getRandStrCode() {
        int size = 6;
        String temp = "ABCDEFGHJKLMNPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
        int length = temp.length();
        int p;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            p = r.nextInt(length);
            sb.append(temp.substring(p, p + 1));
        }
        return sb.toString();
    }

     /**
      * 生成指定长度的数字随机数
      * @param  长度
      * @return String
      */
    public static  String getRandNumberCode ()    {
        int length = 6;
        Random random = new Random();
        String result="";
        for(int i=0;i<length;i++){
            result+=random.nextInt(10);
        }
        return result;
    }
//
//     /**
//      * 生成指定长度的数字随机数,不能以0开头
//      * @param length 长度
//      * @return String
//      */
//    public static  String getRandNumber (int length)    {
//        //第一位随机数
//        String temp = "123456789";
//        int len = temp.length();
//        int p;
//        Random r = new Random();
//        StringBuilder sb = new StringBuilder();
//        p = r.nextInt(len);
//        sb.append(temp.substring(p, p + 1));
//
//        //除第一位以外其他随机数
//        for(int i=0;i<length-1;i++){
//            sb.append(r.nextInt(10));
//        }
//        return sb.toString();
//    }
    

}