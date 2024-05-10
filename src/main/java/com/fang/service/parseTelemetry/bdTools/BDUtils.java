package com.fang.service.parseTelemetry.bdTools;

import com.fang.utils.CrcUtils;
import com.fang.utils.StringConvertUtils;

import java.util.Arrays;

public class BDUtils {


    public static boolean bdSelfValidate(byte[] buf_receive)
    {
        int crcSum = buf_receive[buf_receive.length - 2] * 256 + buf_receive[buf_receive.length - 1];
        byte[] needCheckBytes = Arrays.copyOfRange(buf_receive, 0, buf_receive.length - 2);
        if (crcSum == CrcUtils.calculateCrcSum(needCheckBytes))
        {
            return true;
        }
        return false;
    }
    public static boolean validateBDMsgHeader(String dataContent){
        boolean result=false;
        if(dataContent.startsWith("EBE9")||dataContent.startsWith("B2A4")){
            result=true;
        }
        return result;
    }

    public static boolean bdMsgValidate(String content)
    {
        if (content.contains("$BDTXR")&&content.contains("*"))
        {
            content = content.replaceAll("\r", "").replaceAll("\n", "");
            String[] contentArray = content.replaceAll("\\$", "").split("\\*");
            String needCheck = contentArray[0];
            char checkSum = (char)0;
            for (char c : needCheck.toCharArray()) {
                checkSum ^= c;
            }
            if (contentArray[contentArray.length-1].equals(String.format("%04x", (int)checkSum)))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean dataValidate(byte[] buf_receive)
    {
        byte checkValue = buf_receive[5];
        byte checkSum = 0;
        for (int i = 6; i < buf_receive.length; i++)
        {
            checkSum += buf_receive[i];
        }
        if (checkSum == checkValue)
        {
            return true;
        }
        return false;
    }


    public static byte[] getDataBytes(String hexStr){

        return StringConvertUtils.hexStringToByteArray(hexStr);
    }

}
