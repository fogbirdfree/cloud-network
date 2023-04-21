package com.dongruan.graduation.networkdiskfileservice.utils;

import com.dongruan.graduation.networkdiskcommon.pojo.FastDFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * @author: duyubo
 */
@Component
public class FileUtils {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 保存文件到FastDFS中
     *
     * @author: duyubo
     */
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath = {};
        String fileName = multipartFile.getOriginalFilename();
        String ext = null;
        if (fileName != null) {
            ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        byte[] fileBuff;
        InputStream inputStream = multipartFile.getInputStream();
        int len1 = inputStream.available();
        fileBuff = new byte[len1];
        inputStream.read(fileBuff);
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, fileBuff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file);
        } catch (Exception e) {
            logger.error("upload file Exception!", e);
        }
        return fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
    }

    public void zipFile(File inputFile, ZipOutputStream zipoutputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {

                    // 创建输入流读取文件
                    FileInputStream fis = new FileInputStream(inputFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    // 将文件写入zip内，即将文件进行打包
                    ZipEntry ze = new ZipEntry(inputFile.getName());
                    zipoutputStream.putNextEntry(ze);

                    byte[] b = new byte[1024];
                    long l = 0;
                    while (l < inputFile.length()) {
                        int j = bis.read(b, 0, 1024);
                        l += j;
                        zipoutputStream.write(b, 0, j);
                    }
                    bis.close();
                    fis.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                zipFile(file, zipoutputStream);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除临时文件
     *
     * @author: duybo
     */
    private static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp;
        if (tempList != null) {
            for (String s : tempList) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + s);
                } else {
                    temp = new File(path + File.separator + s);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    delAllFile(path + "/" + s);
                    delFolder(path + "/" + s);
                }
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @author: duyubo
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            File myFilePath = new File(folderPath);
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
