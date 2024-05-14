package com.fang.utils;

import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.config.satellite.paraParser.ParaParser;
import com.fang.utils.CrcUtils;
import com.fang.utils.StringConvertUtils;

import java.util.Arrays;

public class BDUtils {


    public static void parseBdFrameInfo(FrameInfo frameInfo, byte[] receiveBytes, SatelliteConfigClass satelliteConfigClass) {
        String receiveStr = StringConvertUtils.bytesConvertToASCIIStr(receiveBytes);
        String[] receiveStrArray = receiveStr.split(",");
        String[] dataStrArray = receiveStrArray[5].split("\\*");
        byte[] frameBytes = getDataBytes(dataStrArray[0]);
        frameInfo.setFrameFlag(0);
        if (matchBDSelfFrame(frameBytes)) {
            setBDSelfFrameInfo(frameBytes, frameInfo,satelliteConfigClass);
        } else if (matchBDMsgFrame(frameBytes)) {

        }else{
            frameInfo.setValid(false);
            frameInfo.setDataBytes(receiveBytes);
        }


    }

    public static boolean matchBDSelfFrame(byte[] frameBytes) {
        return frameBytes[0] == 0xB2 && frameBytes[1] == 0xA4;
    }

    public static boolean matchBDMsgFrame(byte[] frameBytes) {
        return frameBytes[0] == 0xEB && frameBytes[1] == 0xE9;
    }

    public static void setBDSelfFrameInfo(byte[] frameBytes, FrameInfo frameInfo, SatelliteConfigClass satelliteConfigClass) {
        boolean isValid = bdSelfValidate(frameBytes);
        frameInfo.setFrameCode(1);
        frameInfo.setCatalogCode(255);
        frameInfo.setSerialNum(8);
        frameInfo.setReuseChannel(0);
        frameInfo.setDataBytes(Arrays.copyOfRange(frameBytes, 0, 27));
        FrameConfigClass frame = satelliteConfigClass.getFrameConfigClassByFrameCode(frameInfo.getCatalogCode(), frameInfo.getFrameCode(), frameInfo.getReuseChannel());
        if (frame != null) {
            if(frame.getFrameName().contains("X测控遥测应答帧")&&isValid)
            {


                ParseUtils.setFrameInfo(Arrays.copyOfRange(frameBytes,frameBytes.length-64,frameBytes.length-1),frameInfo,satelliteConfigClass);

            }
            else
            {
                frameInfo.setFrameConfigClass(frame);
                frameInfo.setValid(isValid);
                frameInfo.setDataBytes(frameBytes);
            }

        } else {
            frameInfo.setValid(false);
            frameInfo.setDataBytes(frameBytes);
        }
    }


    public static boolean bdValidateFrameBytes(byte[] frameBytes)
    {
        byte checkValue = frameBytes[5];
        byte checkSum = 0;
        for (int i = 6; i < frameBytes.length; i++)
        {
            checkSum += frameBytes[i];
        }

        if (checkSum == checkValue)
        {
            return true;
        }


        return false;
    }

    public static void setBDMsgFrameInfo(FrameInfo frameInfo, byte[] frameBytes,SatelliteConfigClass satelliteConfigClass) {

        if(satelliteConfigClass.isBCBDSatellite()){

            frameInfo.setCatalogCode((frameBytes[5] & 0xf0) >> 4);
            frameInfo.setFrameCode(frameBytes[5] & 0x0f);
            frameInfo.setReuseChannel(0);
            frameInfo.setSerialNum(frameBytes[6]&0x70>>4);

            FrameConfigClass frame = satelliteConfigClass.getFrameConfigClassByFrameCode(frameInfo.getCatalogCode(), frameInfo.getFrameCode(), frameInfo.getReuseChannel());
            if(frame!=null){
                frameInfo.setValid(bcBDValidateFrameBytes(frameBytes));
                frameInfo.setDataBytes(Arrays.copyOfRange(frameBytes,8,frameBytes[3]+7));
            }else{
                frameInfo.setValid(false);
                frameInfo.setDataBytes(frameBytes);
            }

        }else{

            frameInfo.setCatalogCode((frameBytes[8] & 0xf0) >> 4);
            frameInfo.setFrameCode(frameBytes[8] & 0x0f);
            frameInfo.setReuseChannel(0);
            frameInfo.setSerialNum(frameBytes[9]&0x70>>4);
            frameInfo.setValid(bdValidateFrameBytes(frameBytes));
        }


    }

    public static boolean bdSelfValidate(byte[] buf_receive) {
        int crcSum = buf_receive[buf_receive.length - 2] * 256 + buf_receive[buf_receive.length - 1];
        byte[] needCheckBytes = Arrays.copyOfRange(buf_receive, 0, buf_receive.length - 2);
        if (crcSum == CrcUtils.calculateCrcSum(needCheckBytes)) {
            return true;
        }
        return false;
    }

    public static boolean validateBDMsgHeader(String dataContent) {
        boolean result = false;
        if (dataContent.startsWith("EBE9") || dataContent.startsWith("B2A4")) {
            result = true;
        }
        return result;
    }

    public static boolean bcBDValidateFrameBytes(byte[] receiveBytes) {
        byte checkValue = receiveBytes[2 ];
        byte checkSum = 0;
        for (int i = 3 ; i < receiveBytes.length; i++) {
            checkSum += receiveBytes[i];
        }
        if (checkSum == checkValue) {
            return true;
        }

        return false;

    }

    public static boolean bdMsgValidate(String content) {
        if (content.contains("$BDTXR") && content.contains("*")) {
            content = content.replaceAll("\r", "").replaceAll("\n", "");
            String[] contentArray = content.replaceAll("\\$", "").split("\\*");
            String needCheck = contentArray[0];
            char checkSum = (char) 0;
            for (char c : needCheck.toCharArray()) {
                checkSum ^= c;
            }
            if (contentArray[contentArray.length - 1].equals(String.format("%04x", (int) checkSum))) {
                return true;
            }
        }
        return false;
    }

//    public static boolean dataValidate(byte[] buf_receive) {
//        byte checkValue = buf_receive[5];
//        byte checkSum = 0;
//        for (int i = 6; i < buf_receive.length; i++) {
//            checkSum += buf_receive[i];
//        }
//        if (checkSum == checkValue) {
//            return true;
//        }
//        return false;
//    }


    public static byte[] getDataBytes(String hexStr) {

        return StringConvertUtils.hexStringToByteArray(hexStr);
    }

}
