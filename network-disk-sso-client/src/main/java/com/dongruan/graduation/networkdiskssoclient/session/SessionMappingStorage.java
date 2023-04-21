package com.dongruan.graduation.networkdiskssoclient.session;

import javax.servlet.http.HttpSession;

/**
 * 借鉴CAS
 * 
 * @author duyubo
 */
public interface SessionMappingStorage {

    HttpSession removeSessionByMappingId(String accessToken);

    void removeBySessionById(String sessionId);

    void addSessionById(String accessToken, HttpSession session);
}
