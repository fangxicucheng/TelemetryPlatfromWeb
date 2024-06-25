package com.fang.utils;

import com.obs.services.ObsClient;
import com.obs.services.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OBSUtils {
    private static String endPoint = "https://obs.cn-north-4.myhuaweicloud.com";
    private static String bucketName = "yunguan";
    private static String AK = "OCLD1TBOTN2HZ7Y67MWV";
    private static String SK = "pqOWpeOs3orKuZBmacuB94lDOOcN0V1BXkCIlf0Y";

    public static ObsClient getObsClient() {
        return new ObsClient(AK, SK, endPoint);
    }

    public static void saveFile(byte[] bytes, String fileName) {

        String objcetPath = getObjectName(fileName);
        ObsClient obsClient = getObsClient();
        for (int i = 0; i < 3; i++) {

            try {
                obsClient.putObject(bucketName, objcetPath, new ByteArrayInputStream(bytes));

                break;
            } catch (Exception e) {
            }
        }
        try {
            obsClient.close();
        } catch (IOException e) {

        }

    }

    /// <summary>
    /// 获取华为存储地址
    /// </summary>
    /// <param name="filePath"></param>
    /// <returns></returns>
    private static String getObjectName(String path) {
        String objectPath = "";
        try {
            objectPath = getRealPath(path);
        } catch (Exception e) {
        }
        return objectPath;
    }

    public static byte[] readFileFromObs(String filePath){
        byte[] result=null;
        if(fileExist(filePath)){
            ObsClient obsClient = getObsClient();
            try {
                String objectName = getRealPath(filePath);
                GetObjectRequest request=new GetObjectRequest(bucketName,objectName);
                ObsObject object = obsClient.getObject(request);
                InputStream inputStream = object.getObjectContent();
                result = inputStream.readAllBytes();
            } catch (Exception e) {
            }finally {
                try {
                    obsClient.close();
                } catch (IOException e) {

                }
            }
        }
        return result;
    }

    /// <summary>
    /// 获取文件名+不带文件夹路径
    /// </summary>
    /// <param name="path"></param>
    /// <returns></returns>
    private static String getRealPath(String path) {
        String[] pathArray = path.replaceAll("\\\\","/").split(":/");
        return pathArray[pathArray.length-1];
    }


    /// <summary>
    /// 判断文件是否存在
    /// </summary>
    /// <param name="path"></param>
    /// <returns></returns>
    public static boolean fileExist(String filePath) {
        boolean fileExists = false;
        ObsClient client = getObsClient();
        try {
            String objectName = getObjectName(filePath);
            GetObjectMetadataRequest request = new GetObjectMetadataRequest(bucketName, objectName);
            fileExists = client.doesObjectExist(request);
        } catch (Exception e) {

        } finally {
            try {
                client.close();
            } catch (IOException e) {

            }
        }
        return fileExists;
    }

    /// <summary>
    /// 删除文件
    /// </summary>
    /// <param name="fileName"></param>
    public static void deleteFile(String filePath) {
        ObsClient client = getObsClient();
        String objectName = getObjectName(filePath);
        try {
            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, objectName);
            DeleteObjectResult deleteObjectResult = client.deleteObject(request);

        } catch (Exception ex) {

        } finally {
            try {
                client.close();
            } catch (IOException e) {
                //
            }
        }
    }
}
