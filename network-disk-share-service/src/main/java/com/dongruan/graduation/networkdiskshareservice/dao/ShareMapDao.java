package com.dongruan.graduation.networkdiskshareservice.dao;

import com.dongruan.graduation.networkdiskshareservice.entity.ShareMapDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ShareMapDao {

    int insert(ShareMapDO record);

    /**
     * 保存分享文件对应
     *
     * @author: quhailong
     * @date: 2019/9/26
     */
    Integer saveShareMap(ShareMapDO shareMapDO);

    /**
     * 根据分享ID删除分享文件对应
     *
     * @author: quhailong
     * @date: 2019/9/26
     */
    Integer removeShareMapByShareIdList(@Param("shareIdList") List<String> shareIdList);

    /**
     * 根据分享ID获取分享文件对应
     *
     * @author: quhailong
     * @date: 2019/9/26
     */
    List<ShareMapDO> listShareMapByShareId(@Param("shareId") String shareId);
}