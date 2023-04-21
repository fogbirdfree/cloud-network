package com.dongruan.graduation.networkdiskssoserver.service.impl;

import com.dongruan.graduation.networkdiskcommon.utils.RSAUtils;
import com.dongruan.graduation.networkdiskssoserver.service.EdgeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author: duyubo
 * @date: 2021年04月02日, 0002 15:26
 * @description:
 */

@Component
public class EdgeServiceImpl implements EdgeService {

    private static Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5]+$");

    @Override
    public void regCheckPwd(String password, String RSAKey) throws Exception {
        try {
            password = RSAUtils.decryptDataOnJava(password, RSAKey);
        } catch (Exception e) {
            throw new Exception("fail");
        }
        if (!(password.length() < 6 || password.length() > 14)) {
            if (p.matcher(password).matches() || password.contains(" ")) {
                throw new Exception("支持数字，大小写字母和标点符号，不允许有空格");
            }
        } else {
            throw new Exception("密码长度为6~14个字符");
        }
    }

    @Override
    public Map<String, Object> getPublicKey() throws Exception {
        Map<String, Object> keyMap = RSAUtils.genKeyPair();
        String publicKey = RSAUtils.getPublicKey(keyMap);
        String privateKey = RSAUtils.getPrivateKey(keyMap);
        Map<String, Object> map = new HashMap<>();
        map.put("publicKey", publicKey);
        map.put("RSAKey", privateKey);
        return map;
    }
}
