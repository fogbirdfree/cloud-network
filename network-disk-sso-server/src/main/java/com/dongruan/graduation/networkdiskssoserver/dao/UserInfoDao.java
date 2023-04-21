package com.dongruan.graduation.networkdiskssoserver.dao;

import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskssoserver.entity.UserInfoDO;
import com.dongruan.graduation.networkdiskssoserver.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao {
    /**
     * 根据登录凭证获取用户信息
     *
     * @author: quhailong
     * @date: 2019/9/25
     */
    UserInfoDO getUserInfoByPassport(@Param("passport") String passport);

    /**
     * 根据用户名或手机号获取用户信息
     *
     * @author: quhailong
     * @date: 2019/9/25
     */
    UserInfoDO getUserInfoByUserNameOrPhone(@Param("userName") String userName, @Param("phone") String phone);

    /**
     * 保存用户信息
     *
     * @author: quhailong
     * @date: 2019/9/25
     */
    Integer saveUserInfo(UserInfoDO userInfoDO);

    /**
     * 更新用户信息
     *
     * @author: quhailong
     * @date: 2019/9/26
     */
    Integer updateUserInfo(UserInfoDO userInfoDO);

    /**
     * 根据用户ID获取用户信息
     *
     * @author: quhailong
     * @date: 2019/9/26
     */
    UserInfoDO getUserInfoByUserId(@Param("userId") String userId);

    UserInfoDO getUserInfoById(@Param("id") String id);
}