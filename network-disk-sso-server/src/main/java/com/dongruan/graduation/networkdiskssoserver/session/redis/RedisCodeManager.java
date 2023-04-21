package com.dongruan.graduation.networkdiskssoserver.session.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dongruan.graduation.networkdiskssoserver.common.AuthContent;
import com.dongruan.graduation.networkdiskssoserver.session.CodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 分布式授权码管理
 * 
 * @author Joe
 */
@Component
@ConditionalOnProperty(name = "sso.session.manager", havingValue = "redis")
public class RedisCodeManager implements CodeManager {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void create(String code, AuthContent authContent) {
		redisTemplate.opsForValue().set(code, JSON.toJSONString(authContent), getExpiresIn(), TimeUnit.SECONDS);
	}

	@Override
	public AuthContent validate(String code) {
		String cc = redisTemplate.opsForValue().get(code);
		if (!StringUtils.isEmpty(cc)) {
			redisTemplate.delete(code);
		}
		return JSONObject.parseObject(cc, AuthContent.class);
	}
}
