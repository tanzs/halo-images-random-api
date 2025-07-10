package com.aiym.haloapi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class IpRateLimiter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean isLimited(String ip) {
        String key = "limit:image:ip:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }
        return count != null && count > 60;
    }
}
