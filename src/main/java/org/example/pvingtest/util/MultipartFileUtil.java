package org.example.pvingtest.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @package: org.example.pvingtest.util
 * @className: MultipartFileUtil
 * @author: alexwang
 * @description: TODO
 * @date: 2024/5/27 16:27
 */

public class MultipartFileUtil {
    public static File multipartFileToFile(MultipartFile multipartFile) {
        File file = null;
        //判断是否为null
        if (multipartFile.equals("") || multipartFile.getSize() <= 0) {
            return file;
        }
        //MultipartFile转换为File
        InputStream ins = null;
        OutputStream os = null;
        try {
            ins = multipartFile.getInputStream();
            file = new File(multipartFile.getOriginalFilename());
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static String getFileType(MultipartFile file) {
        // 获取文件的ContentType
        String contentType = file.getContentType();
        // 如果ContentType不存在，尝试获取文件名的扩展名作为文件类型
        if (contentType == null || contentType.isEmpty()) {
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.lastIndexOf(".") != -1) {
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            }
        }
        return contentType;
    }
}
