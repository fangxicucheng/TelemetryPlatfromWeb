package com.fang.utils;

import org.apache.commons.io.FileUtils;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadFileUtils {
    public static List<byte[]> readFile(String filePath) throws IOException {
        List<byte[]> receiveBytesList = new ArrayList<>();
        byte[] bytes=readFileBytes(filePath);
        if(bytes==null){
            return receiveBytesList;
        }
        if (filePath.contains(".dat")) {
            if (filePath.contains("宽带节点")) {
                for (int i = 0; i < bytes.length; i = i + 285) {
                    if (i + 285 <= bytes.length)
                        receiveBytesList.add(Arrays.copyOfRange(bytes, i, i + 285));
                }
            } else {
                for (int i = 0; i < bytes.length; i = i + 93) {
                    if (i + 93 <= bytes.length)
                        receiveBytesList.add(Arrays.copyOfRange(bytes, i, i + 93));
                }
            }
        } else if (filePath.contains(".txt")) {

            String content = new String(bytes, StandardCharsets.UTF_8);
            String[] strArray = content.replaceAll("\\r", "").split("\\n");
            if (strArray != null && strArray.length > 0) {
                for (String str : strArray) {
                    receiveBytesList.add(str.getBytes("US-ASCII"));
                }
            }
        }
        return receiveBytesList;
    }

    private static byte[] readFileBytes(String filePath) throws IOException {
        byte[] result = null;
        File file = new File(filePath);
        if (file.exists()) {
            result = FileUtils.readFileToByteArray(new File(filePath));
        } else {
            result = OBSUtils.readFileFromObs(filePath);
        }
        return result;

    }
}
