package com.dongruan.graduation.networkdiskshareservice.service;

import org.springframework.ui.Model;

/**
 * @author: duyubo
 * @date: 2021年04月02日, 0002 14:32
 * @description:
 */
public interface PageService {
    String sHandle(Model model, String shareId, String uid);
}
