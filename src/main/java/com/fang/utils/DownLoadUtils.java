package com.fang.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

public class DownLoadUtils {

    public static void download(HttpServletResponse response, String filename, String path) throws IOException {
        response.setCharacterEncoding("UTF-8");
        if (filename != null) {
            FileInputStream is = null;
            BufferedInputStream bs = null;
            OutputStream os = null;
            try {
                File file = new File(path);
                if (file.exists()) {
                    // 设置Headers
                    response.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
                    // 设置下载的文件的名称-该方式已解决中文乱码问题
                    response.setHeader("Content-Disposition",
                            "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
                    is = new FileInputStream(file);
                    bs = new BufferedInputStream(is);
                    os = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = bs.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } else {
                    String error = "下载的文件资源不存在";
                    response.sendRedirect("error=" + error);
                }
            } finally {
                if (is != null) {
                    is.close();
                }
                if (bs != null) {
                    bs.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            }
        }
    }
}
