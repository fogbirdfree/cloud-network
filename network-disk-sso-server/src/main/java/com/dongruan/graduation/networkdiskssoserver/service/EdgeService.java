package com.dongruan.graduation.networkdiskssoserver.service;

import java.util.Map;

/**
 * @author: duyubo
 * @date: 2021年04月02日, 0002 15:25
 * @description:
 */
public interface EdgeService {
    void regCheckPwd(String password, String RSAKey) throws Exception;
    Map<String, Object> getPublicKey() throws Exception;
}
