package com.fang.service.resourceReader;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ResourceReaderService {
    public  List<String>readResourceFile(String fileName) throws IOException {
        List<String> result=new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        // 从类路径中加载资源文件
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
      //  Map<String,String> satelliteInfoMap=new HashMap<>();
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    //String[] nameArray = line.split(",");
                    //satelliteInfoMap.put(nameArray[1],nameArray[0]);
                    result.add(line);
                }
            }
        } else {
            System.out.println("File not found!");
        }

        inputStream.close();



        return result;
    }
}
