package com.fang.utils;

import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.config.satellite.paraParser.FrameInfo;

import java.util.Arrays;

public class ParseMCUtils {
    public static boolean validateKDJDBytes(byte[] dataBytes) {
        boolean result = false;
        //= frameInfo.getDataBytes();
        if (dataBytes != null && dataBytes.length == 224) {
            if (dataBytes[0] != 0xFA || dataBytes[1] != 0xF2 || dataBytes[2] != 0x20) {
                result = false;
            } else {
                int crcSum = dataBytes[dataBytes.length - 2] * 256 + dataBytes[dataBytes.length - 1];

                byte[] needCheckBytes = Arrays.copyOfRange(dataBytes, 8, 221);
                result = CrcUtils.calculateCrcSum(needCheckBytes) == crcSum;
            }
        }
        return result;
        // frameInfo.setValid(result);
    }
    public static void setKDJDFrameInfo(byte[] dataBytes, FrameInfo frameInfo, SatelliteConfigClass satelliteConfigClass) {
        boolean isValid = validateKDJDBytes(dataBytes);
        byte byte_212 = dataBytes[8 + 212];
        byte byte_213 = dataBytes[8 + 213];
        frameInfo.setDataBytes(dataBytes);
        frameInfo.setSerialNum(byte_213 & 0x07);
        frameInfo.setValid(isValid);
        frameInfo.setFrameFlag(byte_213 & 0x08);
        if (isValid) {
            frameInfo.setCatalogCode((byte_213 & 0xf0) >> 4);
            frameInfo.setFrameCode(byte_212 & 0x1f);
            frameInfo.setReuseChannel((byte_212 & 0xe0) >> 5);
            FrameConfigClass frame = satelliteConfigClass.getFrameConfigClassByFrameCode(frameInfo.getCatalogCode(), frameInfo.getFrameCode(), frameInfo.getReuseChannel());
            frameInfo.setFrameConfigClass(frame);
        }
    }
    public static void parseNormalFrameInfo(FrameInfo frameInfo, byte[] receiveBytes, SatelliteConfigClass satelliteConfigClass) {

        if (receiveBytes.length != 93) {
            frameInfo.setValid(false);
            frameInfo.setDataBytes(receiveBytes);
            frameInfo.setFrameFlag(0);
            return;
        }
        byte[] dataBytes = getDataBytes(receiveBytes);
        frameInfo.setDataBytes(dataBytes);
        setNormalFrameInfo(dataBytes, frameInfo, satelliteConfigClass);
    }
    public static byte[] getDataBytes(byte[] receiveBytes) {
        byte[] dataBtyes = new byte[receiveBytes.length - 29];
        dataBtyes[0] = receiveBytes[receiveBytes.length - 2];
        dataBtyes[1] = receiveBytes[receiveBytes.length - 1];
        System.arraycopy(receiveBytes, 29, dataBtyes, 2, receiveBytes.length - 31);
        return dataBtyes;
    }
    public static boolean validateNormalFrameBytes(byte[] dataBytes) {
        byte checksum = 0;
        byte checksum1 = 0; //
        if (!(dataBytes[0] == 0xEB && dataBytes[1] == 0x90)) {
            return false;
        }

        byte byte_Last = dataBytes[dataBytes.length - 1];
        for (int i = 0; i < dataBytes.length - 1; i++) {
            checksum += dataBytes[i];
        }

        checksum1 = (byte) (~(char) checksum + 1);
        if (checksum == byte_Last) {
            return true;
        }

        if (checksum1 == byte_Last) {
            return true;
        }

        return false; //误码
    }
    public static void setNormalFrameInfo(byte[] dataBytes, FrameInfo frameInfo, SatelliteConfigClass satelliteConfigClass) {
        byte byte_61 = dataBytes[61];
        byte byte_62 = dataBytes[62];
        frameInfo.setSerialNum(byte_62 & 0x07);
        frameInfo.setCatalogCode(byte_62 & 0xf0 >> 4);
        frameInfo.setFrameFlag(byte_62 & 0x08);
        frameInfo.setDataBytes(dataBytes);
        if (satelliteConfigClass.isHasSixBitWidthFrameCode()) {
            frameInfo.setReuseChannel(byte_61 & 0xC0 >> 6);
            frameInfo.setFrameCode(byte_61 & 0x3f);
        } else {
            frameInfo.setReuseChannel(byte_61 & 0xe0 >> 5);
            frameInfo.setFrameCode(byte_61 & 0x1f);
        }
        if (satelliteConfigClass.isGPSatellite()) {
            if (frameInfo.getFrameCode() == 4 && frameInfo.getCatalogCode() == 3) {
                if (!(dataBytes[2] == 0x41 || dataBytes[2] == 0x42)) {
                    frameInfo.setCatalogCode(4);
                }
            }
        }
        FrameConfigClass frame = satelliteConfigClass.getFrameConfigClassByFrameCode(frameInfo.getCatalogCode(), frameInfo.getFrameCode(), frameInfo.getReuseChannel());
        frameInfo.setFrameConfigClass(frame);
        frameInfo.setValid(validateNormalFrameBytes(dataBytes));

    }
    public static byte[] getKDJDDataBytes(byte[] receiveBytes) {

        byte[] dataBytes = new byte[224];
        for (int i = 0; i < 3; i++) {
            dataBytes[i] = receiveBytes[285 - 3 + i];
        }
        for (int i = 3; i < 8; i++) {
            dataBytes[i] = receiveBytes[i + 29];
        }
        for (int i = 8; i < 224; i++) {
            dataBytes[i] = receiveBytes[i + 29 + 40 - 8 - 3];
        }

        return dataBytes;

    }
    public static void parseMCFrameInfo(FrameInfo frameInfo, byte[] receiveBytes, SatelliteConfigClass satelliteConfigClass) {
        //byte[] dataBytes = getDataBytes(receiveBytes);
        if (satelliteConfigClass.isKDJD()) {
            parseKDJDFrameInfo(frameInfo, receiveBytes, satelliteConfigClass);
        } else {
            parseNormalFrameInfo(frameInfo, receiveBytes, satelliteConfigClass);
        }
    }
    public static void parseKDJDFrameInfo(FrameInfo frameInfo, byte[] receiveBytes, SatelliteConfigClass satelliteConfigClass) {
        if (receiveBytes.length != 285) {
            frameInfo.setValid(false);
            frameInfo.setDataBytes(receiveBytes);
            return;
        }
        byte[] dataBytes = getKDJDDataBytes(receiveBytes);
        setKDJDFrameInfo(dataBytes, frameInfo, satelliteConfigClass);
    }


}
