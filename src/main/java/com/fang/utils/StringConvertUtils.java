package com.fang.utils;

import javax.sound.sampled.AudioFormat;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class StringConvertUtils {


    public static String removeInvisibleCharacters(String input){
        String cleaned = input.replaceAll("\\p{C}", "");
        return cleaned;
    }
    public static String removeUnableNumberCharacters(String input){
        String cleaned=input.replaceAll("[^0-9A-Fa-f]+","");
        return cleaned;
    }

    public static boolean testStringEmpty(String str){
        boolean result=false;

        if (str==null) {

            result=true;
        }
        if(str.isEmpty()){
            result=true;
        }

        return false;
    }

    public static Double strConvertToDouble(String str){
        Double result=null;
        String input = removeUnableNumberCharacters(str);
        if(input.contains("0x")||input.contains("0X")){
            result = ((double) Long.parseLong(input.replaceAll("0x", "").replaceAll("0X", ""), 16));

        }else{

            result= (double) Integer.parseInt(str);
        }
        return result;
    }
    public static byte[] hexStringToByteArray(String hexString) {
        // 去除空格和无效字符
        hexString = hexString.replaceAll("\\s", "").replaceAll("0X","").replaceAll("0x","").replaceAll(",","");

        // 检查字符串长度是否为偶数
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("16进制字符串长度必须是偶数。");
        }

        // 创建字节数组
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];

        // 将16进制字符串逐对转换为字节
        for (int i = 0; i < length; i += 2) {
            // 取两个16进制字符
            String hexPair = hexString.substring(i, i + 2);
            // 将16进制字符转换为字节
            byte byteValue = (byte) Integer.parseInt(hexPair, 16);
            // 存入字节数组
            byteArray[i / 2] = byteValue;
        }

        return byteArray;
    }
    public static String bytesConvertToASCIIStr(byte[] bytes){
        return new String(bytes, StandardCharsets.US_ASCII);
    }


}
