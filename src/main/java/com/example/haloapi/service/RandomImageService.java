package com.example.haloapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class RandomImageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getRandomImage(String postid) throws JsonProcessingException {
        String cacheKey = "random:image:postid:" + postid;
        String redisPhotoKeyList = "photo:name:list";

        // 先查缓存：这个 postid 是否已经返回过
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return cached;

        // 检查 Redis 是否已缓存所有 photo 名称
        Long listSize = redisTemplate.opsForList().size(redisPhotoKeyList);
        if (listSize == null || listSize == 0) {
            // 缓存为空，从数据库加载所有 name
            List<String> nameList = jdbcTemplate.queryForList(
                    "SELECT name FROM extensions WHERE name LIKE '/registry/core.halo.run/photos/photo-%'",
                    String.class
            );

            if (nameList.isEmpty()) return null;

            // 存入 Redis 列表
            redisTemplate.opsForList().rightPushAll(redisPhotoKeyList, nameList);
        }

        // 获取 Redis 中列表长度（用于随机）
        long total = redisTemplate.opsForList().size(redisPhotoKeyList);
        if (total == 0) return null;

        int index = new Random().nextInt((int) total);
        String randomName = redisTemplate.opsForList().index(redisPhotoKeyList, index);
        if (randomName == null) return null;

        // 精确查询该 name 对应的 JSON 数据
        String randomJson = jdbcTemplate.queryForObject(
                "SELECT data FROM extensions WHERE name = ?",
                String.class,
                randomName
        );

        if (randomJson == null) return null;

        JsonNode root = objectMapper.readTree(randomJson);
        String url = root.path("spec").path("url").asText();

        if (url != null && !url.isBlank()) {
            // 缓存返回值，避免重复查
            redisTemplate.opsForValue().set(cacheKey, url, Duration.ofHours(2));
        }

        return url;
    }
}
