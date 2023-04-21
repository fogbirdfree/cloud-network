package com.dongruan.graduation.networkdiskcoreservice.service.impl;

import com.dongruan.graduation.networkdiskcommon.param.CheckDirWhetherParam;
import com.dongruan.graduation.networkdiskcommon.param.CopyOrMoveFileParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateDirParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateVirtualAddressParam;
import com.dongruan.graduation.networkdiskcommon.param.ListFileParam;
import com.dongruan.graduation.networkdiskcommon.param.ListFolderParam;
import com.dongruan.graduation.networkdiskcommon.param.RenameFileOrDirParam;
import com.dongruan.graduation.networkdiskcommon.param.SearchFileParam;
import com.dongruan.graduation.networkdiskcommon.pojo.FolderInfo;
import com.dongruan.graduation.networkdiskcommon.utils.IDUtils;
import com.dongruan.graduation.networkdiskcommon.utils.JSONUtils;
import com.dongruan.graduation.networkdiskcoreservice.dao.CapacityDao;
import com.dongruan.graduation.networkdiskcoreservice.dao.VirtualAddressDao;
import com.dongruan.graduation.networkdiskcoreservice.entity.CapacityDO;
import com.dongruan.graduation.networkdiskcoreservice.entity.VirtualAddressDO;
import com.dongruan.graduation.networkdiskcoreservice.service.CapacityService;
import com.dongruan.graduation.networkdiskcoreservice.service.VirtualAddressService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Component
public class VirtualAddressServiceImpl implements VirtualAddressService {

    @Autowired
    private VirtualAddressDao virtualAddressDao;

    @Autowired
    private CapacityDao capacityDao;

    @Autowired
    private CapacityService capacityService;

    private static Pattern p = Pattern.compile("^[a-zA-Z0-9\u4E00-\u9FA5_]+$");

    @Override
    public List<VirtualAddressDO> listVirtualAddress(ListFileParam param) throws UnsupportedEncodingException {
        String path = param.getPath();
        if (path != null) {
            path = URLDecoder.decode(path, "UTF-8");
        }
        PageHelper.startPage(param.getPage(), 100);
        if (param.getDesc().equals(1)) {
            if ("all".equals(param.getType())) {
                PageHelper.orderBy("dir_whether desc," + param.getOrder() + " desc");
            } else {
                PageHelper.orderBy(param.getOrder() + " desc");
            }
        } else {
            if ("all".equals(param.getType())) {
                PageHelper.orderBy("dir_whether desc," + param.getOrder() + " asc");
            } else {
                PageHelper.orderBy(param.getOrder() + " asc");
            }
        }
        Integer type;
        String parentPath = null;

        switch (param.getType()) {
            case "all":
                parentPath = path;
                type = null;
                break;
            case "pic":
                type = 1;
                break;
            case "doc":
                type = 2;
                break;
            case "video":
                type = 3;
                break;
            case "mbt":
                type = 4;
                break;
            case "music":
                type = 5;
                break;
            case "other":
                type = 6;
                break;
            default:
                type = 7;
        }
        return virtualAddressDao.listVirtualAddress(param.getUid(), parentPath, type);
    }

    @Override
    public Map<String, Object> listFolder(ListFolderParam param) throws UnsupportedEncodingException {
        HashMap<String, Object> map = null;
        String parentPath = param.getParentPath();
        if (parentPath != null) {
            parentPath = URLDecoder.decode(parentPath, "UTF-8");
        }
        List<FolderInfo> folderInfos = new ArrayList<>();
        PageHelper.orderBy("file_name desc");
        List<VirtualAddressDO> virtualAddressDOList = virtualAddressDao.listVirtualAddress(param.getUid(), parentPath, 0);
        if (virtualAddressDOList != null && virtualAddressDOList.size() > 0) {
            for (VirtualAddressDO virtualAddressDO : virtualAddressDOList) {
                List<VirtualAddressDO> inVirtualAddressDOList = virtualAddressDao.listVirtualAddress(param.getUid(), "/".equals(parentPath) ? parentPath + virtualAddressDO.getFileName() : parentPath + "/" + virtualAddressDO.getFileName(), 0);
                FolderInfo folderInfo = new FolderInfo();
                if ("/".equals(parentPath)) {
                    folderInfo.setPath(parentPath + virtualAddressDO.getFileName());
                } else {
                    folderInfo.setPath(parentPath + "/" + virtualAddressDO.getFileName());
                }
                if (inVirtualAddressDOList != null && inVirtualAddressDOList.size() > 0) {
                    folderInfo.setDir_empty(1);
                } else {
                    folderInfo.setDir_empty(0);
                }
                folderInfos.add(folderInfo);
            }
            if (folderInfos.size() > 0) {
                int i = 0;
                map = new HashMap<>();
                for (FolderInfo folderInfo : folderInfos) {
                    map.put(i++ + "", JSONUtils.toJSONString(folderInfo));
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> listVirtualAddressLikeFileName(SearchFileParam param) {
        Map<String, Object> map = null;
        PageHelper.startPage(param.getPage(), 100);
        PageHelper.orderBy("dir_whether desc," + param.getOrder() + " desc");
        List<VirtualAddressDO> virtualAddressDOList = virtualAddressDao.listVirtualAddressLikeFileName(param.getUid(), param.getKey());
        if (virtualAddressDOList != null && virtualAddressDOList.size() > 0) {
            map = new HashMap<>();
            int i = 0;
            for (VirtualAddressDO virtualAddressDO : virtualAddressDOList) {
                map.put(i++ + "", JSONUtils.toJSONString(virtualAddressDO));
            }
        }
        return map;
    }

    @Override
    public Integer checkVirtualAddress(CheckDirWhetherParam param) {
        return virtualAddressDao.checkVirtualAddress(param.getUid(), param.getParentPath(), null, param.getDirName());
    }

    @Override
    public boolean renameFileOrDir(RenameFileOrDirParam param) {
        VirtualAddressDO virtualAddressDO = virtualAddressDao.getVirtualAddress(param.getVid());
        String suffix = "";
        if(virtualAddressDO.getAddrType() != 0){
            suffix = virtualAddressDO.getFileName().substring(virtualAddressDO.getFileName().lastIndexOf("."));
        }
        Integer count = virtualAddressDao.checkVirtualAddress(virtualAddressDO.getUserId(), virtualAddressDO.getParentPath(), virtualAddressDO.getAddrType(), virtualAddressDO.getAddrType() != 0 ? param.getNewName() + suffix : param.getNewName());
        if (count == 0 || param.getFlag() != null) {
            if (virtualAddressDO.getAddrType() != 0) {
                changeFileName(param.getNewName(), virtualAddressDO, count);
            } else {
                changeDirFileName(param.getNewName(), virtualAddressDO, count);
            }
            return true;
        }
        return false;
    }

    @Override
    public VirtualAddressDO getVirtualAddress(String vid) {
        return virtualAddressDao.getVirtualAddress(vid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteFile(String vid) {
        List<String> vidList = JSONUtils.parseObject(vid, List.class);
        if (vidList != null && vidList.size() > 0) {
            for (String id : vidList) {
                VirtualAddressDO virtualAddressDO = getVirtualAddress(id);
                if (virtualAddressDO.getAddrType() != 0) {
                    delFile(virtualAddressDO);
                } else {
                    delDirFile(virtualAddressDO);
                }
            }
        }
    }

    @Override
    public String createDir(CreateDirParam param) throws Exception {
        if (!p.matcher(param.getDirName()).matches()) {
            throw new Exception("文件夹长度必须小于20，并且不能包含特殊字符，只能为数字、字母、中文、下划线");
        }
        Integer count = virtualAddressDao.checkVirtualAddress(param.getUid(), param.getParentPath(), null, param.getDirName());
        VirtualAddressDO virtualAddressDO = new VirtualAddressDO();
        virtualAddressDO.setAddrType(0);
        virtualAddressDO.setDirWhether(1);
        virtualAddressDO.setCreateTime(new Date());
        virtualAddressDO.setFileId(null);
        if (count > 0) {
            virtualAddressDO.setFileName(param.getDirName() + "(" + count + ")");
        } else {
            virtualAddressDO.setFileName(param.getDirName());
        }
        virtualAddressDO.setFileSize(null);
        virtualAddressDO.setFileMd5(null);
        virtualAddressDO.setParentPath(param.getParentPath());
        virtualAddressDO.setUserId(param.getUid());
        virtualAddressDO.setUpdateTime(new Date());
        virtualAddressDO.setUuid(IDUtils.showNextId(new Random().nextInt(30)).toString());
        virtualAddressDao.saveVirtualAddress(virtualAddressDO);
        return JSONUtils.toJSONString(virtualAddressDO);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void copyOrMoveFile(CopyOrMoveFileParam param) throws Exception {
        List<String> vidList = JSONUtils.parseObject(param.getVid(), List.class);
        for (String vid : vidList) {
            VirtualAddressDO virtualAddressDO = virtualAddressDao.getVirtualAddress(vid);
            if ((param.getDest().equals(virtualAddressDO.getParentPath()) || param.getDest().indexOf((virtualAddressDO.getParentPath().equals("/") ? virtualAddressDO.getParentPath() : virtualAddressDO.getParentPath() + "/") + virtualAddressDO.getFileName()) == 0)) {
                if (param.getOpera().equals("copyOK")) {
                    throw new Exception("不能将文件复制到自身或其子文件夹中");
                } else {
                    throw new Exception("不能将文件移动到自身或其子文件夹中");
                }
            }
            if (virtualAddressDO.getAddrType() != 0) {
                if (!copyOrMove(virtualAddressDO, param.getDest(), param.getOpera())) {
                    throw new Exception("容量不足无法操作");
                }
            } else {
//                if ((param.getDest().equals(virtualAddressDO.getParentPath()) || param.getDest().indexOf((virtualAddressDO.getParentPath().equals("/") ? virtualAddressDO.getParentPath() : virtualAddressDO.getParentPath() + "/") + virtualAddressDO.getFileName()) == 0)) {
//                    if (param.getOpera().equals("copyOK")) {
//                        throw new Exception("不能将文件复制到自身或其子文件夹中");
//                    } else {
//                        throw new Exception("不能将文件复制到自身或其子文件夹中");
//                    }
//                }
                if (!copyOrMoveDirFile(virtualAddressDO, param.getDest(), param.getOpera())) {
                    throw new Exception("容量不足无法操作");
                }
            }
        }
    }

    @Override
    public Integer createVirtualAddress(CreateVirtualAddressParam param) {
        String pre = param.getFileName().substring(0, param.getFileName().lastIndexOf("."));
        String suffix = param.getFileName().substring(param.getFileName().lastIndexOf("."));
        String fileName = param.getFileName();
        Integer count = virtualAddressDao.checkVirtualAddress(param.getUid(), param.getParentPath(), null, fileName);
        VirtualAddressDO virtualAddressDO = new VirtualAddressDO();
        int n = 1;
        while (count > 0) {
            fileName = pre + "(" + n++ + ")" + suffix;
            count = virtualAddressDao.checkVirtualAddress(param.getUid(), param.getParentPath(), null, fileName);
        }
        virtualAddressDO.setFileName(fileName);
        virtualAddressDO.setUuid(IDUtils.showNextId(new Random().nextInt(30)).toString());
        virtualAddressDO.setFileId(param.getFid());
        virtualAddressDO.setUserId(param.getUid());
        virtualAddressDO.setFileMd5(param.getMd5());
        virtualAddressDO.setAddrType(param.getMd5() == null ? 0 : Integer.parseInt(param.getFileType()));
        virtualAddressDO.setDirWhether(param.getMd5() == null ? 1 : 0);
        virtualAddressDO.setFileSize(param.getMd5() == null ? 0 : Integer.parseInt(param.getFileSize()));
        virtualAddressDO.setParentPath(param.getParentPath() == null ? "/" : param.getParentPath());
        virtualAddressDO.setCreateTime(new Date());
        virtualAddressDO.setUpdateTime(new Date());
        Integer result = 0;
        CapacityDO capacity = capacityDao.getCapacity(param.getUid());
        if ((capacity.getUsedCapacity() + virtualAddressDO.getFileSize()) <= capacity.getTotalCapacity()) {
            result = virtualAddressDao.saveVirtualAddress(virtualAddressDO);
            capacity.setUsedCapacity(capacity.getUsedCapacity() + virtualAddressDO.getFileSize());
            capacityService.updateCapacity(capacity);
        }
        return result;
    }

//
//    @Override
//    public Integer updateVirtualAddress(VirtualAddressDO virtualAddressDO) {
//        return virtualaddressDao.updateVirtualAddress(virtualAddressDO);
//    }
//
//    @Override
//    public List<VirtualAddressDO> listVirtualAddressLikeFilePath(String userId, String parentPath) {
//        return virtualaddressDao.listVirtualAddressLikeFilePath(userId, parentPath);
//    }
//
//    @Override
//    public Integer removeVirtualAddress(String uuid) {
//        return virtualaddressDao.removeVirtualAddress(uuid);
//    }
//
//    @Override
//    public Integer saveVirtualAddress(VirtualAddressDO virtualAddressDO) {
//        return virtualaddressDao.saveVirtualAddress(virtualAddressDO);
//    }

    /**
     * 修改文件名称
     *
     * @author: duyubo
     */
    private void changeFileName(String newName, VirtualAddressDO virtualAddressDO, Integer count) {
        if (count > 0) {
            virtualAddressDO.setFileName(newName + "(" + count + ")" + virtualAddressDO.getFileName().substring(virtualAddressDO.getFileName().lastIndexOf(".")));
        } else {
            virtualAddressDO.setFileName(newName + virtualAddressDO.getFileName().substring(virtualAddressDO.getFileName().lastIndexOf(".")));
        }
        virtualAddressDO.setUpdateTime(new Date());
        virtualAddressDao.updateVirtualAddress(virtualAddressDO);
    }

    /**
     * 修改文件夹名称
     *
     * @author: quhailong
     * @date: 2019/9/24
     */
    private void changeDirFileName(String newName, VirtualAddressDO virtualAddressDO, Integer count) {
        String oldName = virtualAddressDO.getFileName();
        if (count > 0) {
            virtualAddressDO.setFileName(newName + "(" + count + ")");
        } else {
            virtualAddressDO.setFileName(newName);
        }
        virtualAddressDO.setUpdateTime(new Date());
        virtualAddressDao.updateVirtualAddress(virtualAddressDO);

        List<VirtualAddressDO> virtualAddressDOList = virtualAddressDao.listVirtualAddressLikeFilePath(virtualAddressDO.getUserId(), virtualAddressDO.getParentPath().equals("/") ? virtualAddressDO.getParentPath() + oldName : virtualAddressDO.getParentPath() + "/" + oldName);
        if (virtualAddressDOList != null && virtualAddressDOList.size() > 0) {
            for (VirtualAddressDO virtualAddressLike : virtualAddressDOList) {
                String suff;
                String pre;
                if (virtualAddressLike.getParentPath().equals("/")) {
                    suff = virtualAddressLike.getParentPath().substring((virtualAddressDO.getParentPath() + oldName).length());
                } else {
                    suff = virtualAddressLike.getParentPath().substring((virtualAddressDO.getParentPath() + "/" + oldName).length());
                }
                if (virtualAddressLike.getParentPath().equals("/")) {
                    pre = virtualAddressLike.getParentPath() + virtualAddressDO.getFileName();
                } else {
                    pre = virtualAddressLike.getParentPath() + "/" + virtualAddressDO.getFileName();
                }
                virtualAddressLike.setParentPath(pre + suff);
                virtualAddressDao.updateVirtualAddress(virtualAddressLike);
            }
        }
    }

    private void delFile(VirtualAddressDO virtualAddressDO) {
        virtualAddressDao.removeVirtualAddress(virtualAddressDO.getUuid());
        CapacityDO capacityDO = capacityDao.getCapacity(virtualAddressDO.getUserId());
        capacityDO.setUsedCapacity(capacityDO.getUsedCapacity() - virtualAddressDO.getFileSize());
        capacityDao.updateCapacity(capacityDO);
    }

    private void delDirFile(VirtualAddressDO virtualAddressDO) {
        virtualAddressDao.removeVirtualAddress(virtualAddressDO.getUuid());
        List<VirtualAddressDO> virtualAddressDOList = virtualAddressDao.listVirtualAddressLikeFilePath(virtualAddressDO.getUserId(), virtualAddressDO.getParentPath().equals("/") ? virtualAddressDO.getParentPath() + virtualAddressDO.getFileName() : virtualAddressDO.getParentPath() + "/" + virtualAddressDO.getFileName());
        if (virtualAddressDOList != null && virtualAddressDOList.size() > 0) {
            for (VirtualAddressDO virtualAddress : virtualAddressDOList) {
                virtualAddressDao.removeVirtualAddress(virtualAddress.getUuid());
                CapacityDO capacityDO = capacityDao.getCapacity(virtualAddress.getUserId());
                capacityDO.setUsedCapacity(capacityDO.getUsedCapacity() - virtualAddress.getFileSize());
                capacityDao.updateCapacity(capacityDO);
            }
        }
    }

    private boolean copyOrMove(VirtualAddressDO virtualAddressDO, String dest, String opera) {
        String pre = virtualAddressDO.getFileName().substring(0, virtualAddressDO.getFileName().lastIndexOf("."));
        String suffix = virtualAddressDO.getFileName().substring(virtualAddressDO.getFileName().lastIndexOf("."));
        Integer count = virtualAddressDao.checkVirtualAddress(virtualAddressDO.getUserId(), dest, null, virtualAddressDO.getFileName());
        if (count > 0) {
            virtualAddressDO.setFileName(pre + "(" + count + ")" + suffix);
        }
        virtualAddressDO.setUuid(IDUtils.showNextId(new Random().nextInt(30)).toString());
        virtualAddressDO.setParentPath(dest);
        virtualAddressDO.setCreateTime(new Date());
        virtualAddressDO.setUpdateTime(new Date());
        if (opera.equals("copyOK")) {
            CapacityDO capacity = capacityDao.getCapacity(virtualAddressDO.getUserId());
            if ((capacity.getUsedCapacity() + virtualAddressDO.getFileSize()) > capacity.getTotalCapacity()) {
                return false;
            }
            virtualAddressDao.saveVirtualAddress(virtualAddressDO);
            capacity.setUsedCapacity(capacity.getUsedCapacity() + virtualAddressDO.getFileSize());
            capacityDao.updateCapacity(capacity);
        } else {
            virtualAddressDao.updateVirtualAddress(virtualAddressDO);
        }
        return true;
    }

    private boolean copyOrMoveDirFile(VirtualAddressDO virtualAddressDO, String dest, String opera) {
        Integer count = virtualAddressDao.checkVirtualAddress(virtualAddressDO.getUserId(), dest, null, virtualAddressDO.getFileName());
        String uuid = IDUtils.showNextId(new Random().nextInt(30)).toString();
        if (opera.equals("moveOK")) {
            virtualAddressDao.removeVirtualAddress(virtualAddressDO.getUuid());
        }
        VirtualAddressDO virtualAddressDONew = new VirtualAddressDO();
        virtualAddressDONew.setAddrType(0);
        virtualAddressDONew.setCreateTime(new Date());
        virtualAddressDONew.setFileId(null);
        if (count > 0) {
            virtualAddressDONew.setFileName(virtualAddressDO.getFileName() + "(" + count + ")");
        }else{
            virtualAddressDONew.setFileName(virtualAddressDO.getFileName());
        }
        virtualAddressDONew.setFileSize(null);
        virtualAddressDONew.setFileMd5(null);
        virtualAddressDONew.setParentPath(dest);
        virtualAddressDONew.setUserId(virtualAddressDO.getUserId());
        virtualAddressDONew.setUpdateTime(new Date());
        virtualAddressDONew.setUuid(uuid);
        virtualAddressDONew.setDirWhether(1);


        List<VirtualAddressDO> virtualAddressDOList = virtualAddressDao.listVirtualAddressLikeFilePath(virtualAddressDO.getUserId(), virtualAddressDO.getParentPath().equals("/") ? virtualAddressDO.getParentPath() + virtualAddressDO.getFileName() : virtualAddressDO.getParentPath() + "/" + virtualAddressDO.getFileName());
        if (virtualAddressDOList != null && virtualAddressDOList.size() > 0) {
            for (VirtualAddressDO virtualAddressLike : virtualAddressDOList) {
                String oldUuid = virtualAddressLike.getUuid();
                virtualAddressLike.setUuid(IDUtils.showNextId(new Random().nextInt(30)).toString());
                virtualAddressLike.setParentPath((dest.equals("/") ? dest : dest + "/") + virtualAddressDONew.getFileName() + virtualAddressDONew.getParentPath().substring(((virtualAddressDO.getParentPath().equals("/") ? virtualAddressDO.getParentPath() : virtualAddressDO.getParentPath() + "/") + virtualAddressDO.getFileName()).length(), virtualAddressLike.getParentPath().length()));
                virtualAddressLike.setCreateTime(new Date());
                virtualAddressLike.setUpdateTime(new Date());
                if (opera.equals("moveOK")) {
                    virtualAddressDao.removeVirtualAddress(oldUuid);
                    virtualAddressDao.saveVirtualAddress(virtualAddressLike);
                } else {
                    CapacityDO capacity = capacityDao.getCapacity(virtualAddressLike.getUserId());
                    if ((capacity.getUsedCapacity() + virtualAddressLike.getFileSize()) > capacity.getTotalCapacity()) {
                        return false;
                    }
                    capacity.setUsedCapacity(capacity.getUsedCapacity() + virtualAddressLike.getFileSize());
                    capacityDao.updateCapacity(capacity);
                    virtualAddressDao.saveVirtualAddress(virtualAddressLike);
                }
            }
        }
        virtualAddressDao.saveVirtualAddress(virtualAddressDONew);
        return true;
    }
}
