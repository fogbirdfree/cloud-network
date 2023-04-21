package com.dongruan.graduation.networkdiskfileservice.service.impl;

import com.dongruan.graduation.networkdiskcommon.param.CheckDirWhetherParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateDirParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateVirtualAddressParam;
import com.dongruan.graduation.networkdiskcommon.param.QuickUploadFileParam;
import com.dongruan.graduation.networkdiskcommon.param.UploadFileParam;
import com.dongruan.graduation.networkdiskcommon.response.VirtualAddressDTO;
import com.dongruan.graduation.networkdiskcommon.utils.IDUtils;
import com.dongruan.graduation.networkdiskcommon.utils.JSONUtils;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskfileservice.dao.FileDao;
import com.dongruan.graduation.networkdiskfileservice.entity.FileDO;
import com.dongruan.graduation.networkdiskfileservice.remote.CoreRemote;
import com.dongruan.graduation.networkdiskfileservice.remote.ShareRemote;
import com.dongruan.graduation.networkdiskfileservice.service.FileService;
import com.dongruan.graduation.networkdiskfileservice.utils.FastDFSClient;
import com.dongruan.graduation.networkdiskfileservice.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

@Component
public class FileServiceImpl implements FileService {
    @Autowired
    private FileDao fileDao;
    @Autowired
    private CoreRemote coreRemote;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ShareRemote shareRemote;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void uploadFile(UploadFileParam param) throws IOException {
        String parentPath = param.getParentPath();
        MultipartFile file = param.getFile();
        if (parentPath != null) {
            parentPath = URLDecoder.decode(parentPath, "UTF-8");
        } else {
            parentPath = "/";
        }
        String upPath = "";
        synchronized (this) {
            if (file.getOriginalFilename().contains("/")) {
                upPath = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("/"));
                if (upPath.contains("/")) {
                    upPath = upPath.substring(upPath.lastIndexOf("/") + 1);
                }
                CheckDirWhetherParam checkDirWhetherParam = new CheckDirWhetherParam();
                checkDirWhetherParam.setUid(param.getUid());
                checkDirWhetherParam.setDirName(upPath);
                checkDirWhetherParam.setParentPath(parentPath);
                Integer count = coreRemote.checkDirWhether(checkDirWhetherParam).getRespData();
                if (count == 0) {
                    CreateDirParam createDirParam = new CreateDirParam();
                    createDirParam.setDirName(upPath);
                    createDirParam.setParentPath(parentPath);
                    createDirParam.setUid(param.getUid());
                    coreRemote.createDir(createDirParam);
                }
            }
            String md5 = redisTemplate.opsForValue().get("fileMd5:" + param.getFid());
            redisTemplate.delete("fileMd5:" + param.getFid());
            Integer count = checkMd5Whether(md5);
            FileDO fileDO;
            if (count > 0) {
                fileDO = fileDao.getFileByMd5(md5);
            } else {
                String path = fileUtils.saveFile(file);
                fileDO = saveFileToDatabase(file, path, md5);
            }
            CreateVirtualAddressParam createVirtualAddressParam = new CreateVirtualAddressParam();
            createVirtualAddressParam.setFid(fileDO.getFileId());
            createVirtualAddressParam.setFileName(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/") + 1));
            createVirtualAddressParam.setFileSize(fileDO.getFileSize() + "");
            createVirtualAddressParam.setFileType(fileDO.getFileType() + "");
            createVirtualAddressParam.setMd5(md5);
            createVirtualAddressParam.setUid(param.getUid());
            if (parentPath.equals("/")) {
                createVirtualAddressParam.setParentPath(parentPath + upPath);
            } else {
                createVirtualAddressParam.setParentPath(upPath.equals("") ? parentPath : parentPath + "/" + upPath);
            }
            coreRemote.createVirtualAddress(createVirtualAddressParam);
        }
    }

    private FileDO saveFileToDatabase(MultipartFile file, String path, String md5) {
        String fileName = file.getOriginalFilename();
        if (file.getOriginalFilename().contains("/")) {
            fileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/") + 1);
        }
        String fid = IDUtils.showNextId(new Random().nextInt(30)).toString();
        FileDO fileDO = new FileDO();
        fileDO.setFileId(fid);
        fileDO.setOriginalName(fileName);
        fileDO.setFileLocation(path);
        fileDO.setFileSize((int) file.getSize());
        fileDO.setFileMd5(md5);
        fileDO.setCreateTime(new Date());
        String contentType = file.getContentType();
        if ("image/jpeg".equals(contentType) || "image/gif".equals(contentType) || "image/png".equals(contentType) || "image/jpg".equals(contentType)) {
            fileDO.setFileType(1);
        } else if ("application/pdf".equals(contentType) || "application/msword".equals(contentType) || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType) || "application/x-ppt".equals(contentType) || "application/vnd.openxmlformats-officedocument.presentationml.presentation".equals(contentType) || "txt".equals(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1))) {
            fileDO.setFileType(2);
        } else if ("video/mpeg4".equals(contentType) || "video/avi".equals(contentType) || "application/vnd.rn-realmedia-vbr".equals(contentType) || "video/mpg".equals(contentType) || "video/x-ms-wmv".equals(contentType) || "video/mp4".equals(contentType)) {
            fileDO.setFileType(3);
        } else if ("application/x-bittorrent".equals(contentType)) {
            fileDO.setFileType(4);
        } else if ("audio/mp3".equals(contentType) || "audio/vnd.rn-realaudio".equals(contentType) || "audio/mp4".equals(contentType)) {
            fileDO.setFileType(5);
        } else {
            fileDO.setFileType(6);
        }
        fileDao.saveFile(fileDO);
        return fileDO;
    }

    @Override
    public void quickUploadFile(QuickUploadFileParam param) throws UnsupportedEncodingException {
        String parentPath = param.getParentPath();
        String fileName = param.getFileName();
        if (parentPath != null) {
            parentPath = URLDecoder.decode(parentPath, "UTF-8");
        } else {
            parentPath = "/";
        }
        synchronized (this) {
            String upPath = "";
            FileDO fileDO = fileDao.getFileByMd5(param.getMd5());
            CreateVirtualAddressParam createVirtualAddressParam = new CreateVirtualAddressParam();
            createVirtualAddressParam.setFid(fileDO.getFileId());
            createVirtualAddressParam.setFileName(fileName.substring(fileName.lastIndexOf("/") + 1));
            createVirtualAddressParam.setFileSize(fileDO.getFileSize() + "");
            createVirtualAddressParam.setFileType(fileDO.getFileType() + "");
            createVirtualAddressParam.setMd5(param.getMd5());
            createVirtualAddressParam.setUid(param.getUid());
            if (fileName.contains("/")) {
                upPath = fileName.substring(0, fileName.lastIndexOf("/"));
                if (upPath.contains("/")) {
                    upPath = upPath.substring(upPath.lastIndexOf("/") + 1);
                }
                CheckDirWhetherParam checkDirWhetherParam = new CheckDirWhetherParam();
                checkDirWhetherParam.setUid(param.getUid());
                checkDirWhetherParam.setDirName(upPath);
                checkDirWhetherParam.setParentPath(parentPath);
                Integer count = coreRemote.checkDirWhether(checkDirWhetherParam).getRespData();
                if (count == 0) {
                    CreateDirParam createDirParam = new CreateDirParam();
                    createDirParam.setDirName(upPath);
                    createDirParam.setParentPath(parentPath);
                    createDirParam.setUid(param.getUid());
                    coreRemote.createDir(createDirParam);
                }
                createVirtualAddressParam.setParentPath(parentPath + upPath);
                if (parentPath.equals("/")) {
                    createVirtualAddressParam.setParentPath(parentPath + upPath);
                } else {
                    createVirtualAddressParam.setParentPath(upPath.equals("") ? parentPath : parentPath + "/" + upPath);
                }
            } else {
                createVirtualAddressParam.setParentPath(parentPath);
            }
            coreRemote.createVirtualAddress(createVirtualAddressParam);
            redisTemplate.delete("fileMd5:" + param.getFid());
        }
    }

    @Override
    public Integer checkMd5Whether(String fileMd5) {
        return fileDao.checkMd5Whether(fileMd5);
    }

    @Override
    public void download(String uid, String vid, HttpServletResponse res) throws IOException {
        List<String> vidList = JSONUtils.parseObject(vid, List.class);
        download(uid, vidList, res);
    }
    private void download(String uid, List<String> vidList, HttpServletResponse res) throws IOException {
        String fileName2 = coreRemote.getFilenameByVid(vidList.get(0)).getRespData();
        Map<String, String> map = getFileId(vidList, uid);
        if (map.size() == 1) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String groupName;
                String remoteFileName;
                String fileName = entry.getKey();
                groupName = entry.getValue().substring(0, entry.getValue().indexOf("/"));
                remoteFileName = entry.getValue().substring(groupName.length() + 1);
                InputStream inputStream = FastDFSClient.downFile(groupName, remoteFileName);
                res.setContentType("application/octet-stream");
                res.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
                byte[] buff = new byte[1024];
                BufferedInputStream bis = null;
                OutputStream os;
                try {
                    os = res.getOutputStream();
                    bis = new BufferedInputStream(inputStream);
                    int i = bis.read(buff);
                    while (i != -1) {
                        os.write(buff, 0, buff.length);
                        os.flush();
                        i = bis.read(buff);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (map.size() > 1) {
            String folderName = UUID.randomUUID().toString().replaceAll("-", "");
            File fileDir = new File(folderName);
            fileDir.mkdir();
            String fileName3 = "【批量下载】" + fileName2.substring(0, fileName2.lastIndexOf(".")) + "等.zip";
            String zipFilePath = folderName + "/" + fileName3;
            File zip = new File(zipFilePath);
            if (!zip.exists()) {
                zip.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(zip);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String groupName;
                String remoteFileName;
                String fileName = entry.getKey();
                groupName = entry.getValue().substring(0, entry.getValue().indexOf("/"));
                remoteFileName = entry.getValue().substring(groupName.length() + 1);
                InputStream inputStream = FastDFSClient.downFile(groupName, remoteFileName);
                File file = new File(folderName + "/" + fileName);
                OutputStream os = new FileOutputStream(file);
                int bytesRead;
                byte[] buffer = new byte[8192];
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
                os.close();
                inputStream.close();
                fileUtils.zipFile(file, zos);
            }
            zos.closeEntry();
            zos.close();
            fos.close();
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName3.getBytes(), "ISO-8859-1"));
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os;
            try {
                InputStream fis = new BufferedInputStream(new FileInputStream(zipFilePath));
                os = res.getOutputStream();
                bis = new BufferedInputStream(fis);
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            FileUtils.delFolder(folderName);
        }
    }

    private Map<String, String> getFileId(List<String> vidList, String uid) {
        Map<String, String> map = new HashMap<>();
        for (String vid : vidList) {
            VirtualAddressDTO virtualaddressDTO = coreRemote.getVirtualaddress(vid, uid).getRespData();
            FileDO fileDO = fileDao.getFileByFid(virtualaddressDTO.getFileId());
            map.put(virtualaddressDTO.getFileName(), fileDO.getFileLocation());
        }
        return map;
    }

    @Override
    public void downloadShare(String lockPassword, String shareId, HttpServletResponse res) throws IOException {
        RestResult<String> apiResult = shareRemote.getUid(shareId, lockPassword);
        if (apiResult.getRespCode() == 1) {
            Map<String, Object> respMap = apiResult.getRespMap();
            List<String> vidList = (List<String>) respMap.get("vid");
            String uid = respMap.get("uid").toString();
            download(uid, vidList, res);
            shareRemote.addShareDownload(shareId);
        }
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        return fileUtils.saveFile(file);
    }
}
