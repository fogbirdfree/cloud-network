package com.dongruan.graduation.networkdiskssoserver.dao;

import com.dongruan.graduation.networkdiskssoserver.entity.CapacityDO;
import org.apache.ibatis.annotations.Param;

//@Repository
public interface CapacityDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CapacityDO record);

    int insertSelective(CapacityDO record);

    CapacityDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CapacityDO record);

    int updateByPrimaryKey(CapacityDO record);

    /**
     * 获取容量
     *
     * @author: quhailong
     * @date: 2019/9/24
     */
    CapacityDO getCapacity(@Param("userId") String userId);

    /**
     * 修改容量
     *
     * @author: quhailong
     * @date: 2019/9/24
     */
    Integer updateCapacity(CapacityDO capacityDO);

    /**
     * 保存容量
     *
     * @author: quhailong
     * @date: 2019/9/25
     */
    Integer saveCapacity(CapacityDO capacityDO);
}