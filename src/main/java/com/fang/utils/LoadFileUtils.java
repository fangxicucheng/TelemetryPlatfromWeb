package com.fang.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadFileUtils {
    public static List<byte[]> readFile(String filePath) throws IOException {
        List<byte[]> receiveBytesList = new ArrayList<>();
        if (filePath.contains(".dat")) {
            byte[] bytes = FileUtils.readFileToByteArray(new File(filePath));
            if (filePath.contains("宽带节点")) {
                for (int i = 0; i < bytes.length; i =i +285) {
                    if (i + 285 <= bytes.length)
                        receiveBytesList.add(Arrays.copyOfRange(bytes, i, i + 285));
                }
            } else {
                for (int i = 0; i < bytes.length; i = i+93) {
                    if (i + 93 <= bytes.length)
                        receiveBytesList.add(Arrays.copyOfRange(bytes, i, i + 93));
                   // System.out.println(i);
                }
            }
        } else if (filePath.contains(".txt")) {
            String content = FileUtils.readFileToString(new File(filePath));
            String[] strArray = content.replaceAll("\\r", "").split("\\n");
            if (strArray!=null&&strArray.length>0) {
                for (String str : strArray) {
                    receiveBytesList.add(str.getBytes("US-ASCII"));
                }
            }
        }
        return receiveBytesList;
    }
}
