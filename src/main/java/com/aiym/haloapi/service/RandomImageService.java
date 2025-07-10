package com.aiym.haloapi.service;

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
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return cached;

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM extensions WHERE name LIKE '/registry/core.halo.run/photos/photo-%'", Integer.class);

        if (count == null || count == 0) return null;

        int offset = new Random().nextInt(count);
        String randomJson = jdbcTemplate.queryForObject(
                "SELECT data FROM extensions WHERE name LIKE '/registry/core.halo.run/photos/photo-%' LIMIT 1 OFFSET ?",
                new Object[]{offset}, String.class);

        if (randomJson == null) return null;

        JsonNode root = objectMapper.readTree(randomJson);
        String url = root.path("spec").path("url").asText();

        if (url != null && !url.isBlank()) {
            redisTemplate.opsForValue().set(cacheKey, url, Duration.ofHours(2));
        }

        return url;
    }
}
