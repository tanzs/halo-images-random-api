package com.aiym.haloapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Tanzs
 * @date 2025/7/9 下午2:37
 * @description
 */
@Component
public class updateRandomImageKey {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点执行
    public void refreshPhotoListCache() {
        redisTemplate.delete("photo:name:list");
    }

}
