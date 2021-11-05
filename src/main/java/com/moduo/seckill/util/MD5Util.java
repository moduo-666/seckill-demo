package com.moduo.seckill.util;


import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;


/**
 * 加密规则：MD5(MD5（pass+salt）+salt)
 * 第一个salt: 1ab2c3d4
 * 第二个salt：从数据库中取
 * @author Wu Zicong
 * @create 2021-09-22 11:19
 */
@Component
public class MD5Util {
   public static String md5(String src){
       return DigestUtils.md5DigestAsHex(src.getBytes());
   }
   private static String salt ="1ab2c3d4";
   public static String inputPassToFromPass(String inputPass){
       return md5("" + salt.charAt(1)+salt.charAt(2)+salt.charAt(3)+inputPass);
   }
   private static String fromPassToDBPass(String inputPass,String salt){
       return md5("" + salt.charAt(1)+salt.charAt(2)+salt.charAt(3)+inputPass);
   }
   public static String inputPassToDBPass(String inputPass,String salt){
       return fromPassToDBPass(inputPassToFromPass(inputPass),salt);
   }

    public static void main(String[] args) {
       //36bae1a6e93543f2299cbb4db7cdf730
        String s1 = inputPassToFromPass("123456");
        System.out.println(s1);
        //f8605d8a89768fbb33f9df10afe92270
        String s = inputPassToDBPass(s1,"1ab2c3d4");
        System.out.println(s);
    }
}
