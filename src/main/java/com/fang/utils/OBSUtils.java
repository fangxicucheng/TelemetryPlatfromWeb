package com.fang.utils;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.DeleteObjectRequest;
import com.obs.services.model.ListObjectsRequest;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.List;

public class OBSUtils {
    private static String endPoint = "https://obs.cn-north-4.myhuaweicloud.com";
    private static String bucketName = "yunguan";
    private static String AK = "OCLD1TBOTN2HZ7Y67MWV";
    private static String SK = "pqOWpeOs3orKuZBmacuB94lDOOcN0V1BXkCIlf0Y";

    public static ObsClient getObsClient() {
        return new ObsClient(AK, SK, endPoint);
    }

    public void SaveFile(List<byte[]> bytesList, String fileName) {

        if (bytesList.size() == 0) {
            return;
        }
        String objcetPath = getObjectName(fileName);
        int length = 0;
        bytesList.ForEach(t = > {length += t.Length; });

        if (length == 0) {
            return;
        }

        byte[] bytes = new byte[length];
        int index = 0;
        for (var i = 0; i < bytesList.Count; i++) {
            for (var i1 = 0; i1 < bytesList[i].Length; i1++) {
                bytes[index] = bytesList[i][i1];
                index++;
            }
        }

        ObsClient obsClient = ;
        try {


            Stream steam = new MemoryStream(bytes);
            PutObjectRequest putRequest = new PutObjectRequest
            {
                BucketName = bucketName,
                        ObjectKey = objcetPath,
                        InputStream = steam
            } ;


            obsClient.BeginPutObject(putRequest, delegate(IAsyncResult ar)
            {

                try {
                    PutObjectResponse response = obsClient.EndPutObject(ar);
                    Console.WriteLine("put object response:{0}", response.StatusCode);
                } catch (ObsException ex) {
                    Console.WriteLine("ErrorCode{0}", ex.ErrorCode);
                    Console.WriteLine("ErrorMessage{0}", ex.Message);
                }
            },null);
            /*Console.WriteLine("put object response:{0}", putObjectResponse.StatusCode);*/
        } catch (ObsException ex) {
            Console.WriteLine("ErrorCode{0}", ex.ErrorCode);
            Console.WriteLine("ErrorMessage{0}", ex.Message);
        }

    }

    public List<String> ListFiles() {
        List<String> fileList = new ArrayList<>();
        ObsClient client = getObsClient();

        try {
            ListObjectsRequest request = new ListObjectsRequest();
            request.setBucketName(this.bucketName);
            ObjectListing objectListing = client.listObjects(request);

            foreach(ObsObject entry in response.ObsObjects)
            {
                fileList.Add(entry.ObjectKey);
            }

        } catch (ObsException ex) {
            ex.printStackTrace();
        }
        return fileList;
    }

    /// <summary>
    /// 获取华为存储地址
    /// </summary>
    /// <param name="filePath"></param>
    /// <returns></returns>
    private String getObjectName(String path) {
        String objectPath = "";
        try {
            objectPath = getRealPath(path);

        } catch (Exception e) {

        }

        return objectPath;
    }

    /// <summary>
    /// 获取文件名+不带文件夹路径
    /// </summary>
    /// <param name="path"></param>
    /// <returns></returns>
    private String getRealPath(String path) {
        return path.replaceAll("D:/卫星遥测数据监控平台", "");
    }


    /// <summary>
    /// 判断文件是否存在
    /// </summary>
    /// <param name="path"></param>
    /// <returns></returns>
    public bool FileExist(string path) {
        bool fileExists = false;


        ObsClient client = new ObsClient(AK, SK, endpoint);
        try {
            string objectPath = getObjectName(path);
            HeadObjectRequest request = new HeadObjectRequest() {
                BucketName =bucketName,
                ObjectKey =objectPath
            };
            fileExists = client.HeadObject(request);

        } catch (Exception) {

        }

        return fileExists;

    }

    /// <summary>
    /// 删除文件
    /// </summary>
    /// <param name="fileName"></param>
    public void DeleteFile(String fileName) {
        ObsClient client = new ObsClient(AK, SK, endpoint);
        try {
            DeleteObjectRequest request = new DeleteObjectRequest() {
                BucketName =bucketName,
                ObjectKey =fileName
            };
            DeleteObjectResponse response = client.DeleteObject(request);
            Console.WriteLine("Delete object response:{0}", response.StatusCode);
        } catch (ObsException ex) {
            Console.WriteLine("ErrorCode:{0}", ex.ErrorCode);
            Console.WriteLine("Error Message:{0}", ex.ErrorMessage);
        }
    }
}
