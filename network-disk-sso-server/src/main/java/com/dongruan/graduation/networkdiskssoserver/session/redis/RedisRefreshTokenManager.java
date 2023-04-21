package com.dongruan.graduation.networkdiskssoserver.session.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dongruan.graduation.networkdiskssoserver.common.RefreshTokenContent;
import com.dongruan.graduation.networkdiskssoserver.session.RefreshTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 分布式刷新凭证管理
 * 
 * @author Joe
 */
@Component
@ConditionalOnProperty(name = "sso.session.manager", havingValue = "redis")
public class RedisRefreshTokenManager implements RefreshTokenManager {
	
	@Value("${sso.timeout}")
    private int timeout;
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void create(String refreshToken, RefreshTokenContent refreshTokenContent) {
		redisTemplate.opsForValue().set(refreshToken, JSON.toJSONString(refreshTokenContent), getExpiresIn(),
				TimeUnit.SECONDS);
	}

	@Override
	public RefreshTokenContent validate(String refreshToken) {
		String rtc = redisTemplate.opsForValue().get(refreshToken);
		if (!StringUtils.isEmpty(rtc)) {
			redisTemplate.delete(refreshToken);
		}
		return JSONObject.parseObject(rtc, RefreshTokenContent.class);
	}
	
	/*
	 * refreshToken时效和登录session时效一致
	 */
	@Override
	public int getExpiresIn() {
		return timeout;
	}
}
